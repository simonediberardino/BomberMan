package game.domain.world.domain.entity.actors.abstracts.entity_interactable.logic

import game.domain.level.behavior.GameBehavior
import game.domain.world.domain.entity.actors.abstracts.base.Entity
import game.domain.world.domain.entity.actors.abstracts.base.logic.EntityLogic
import game.domain.world.domain.entity.actors.abstracts.entity_interactable.EntityInteractable
import game.domain.world.domain.entity.actors.impl.explosion.abstractexpl.AbstractExplosion
import game.domain.world.domain.entity.actors.impl.models.State
import game.domain.world.domain.entity.geo.Coordinates
import game.domain.world.domain.entity.geo.Direction
import game.mappers.toEntityNetwork
import game.network.events.forward.AttackEntityEventForwarder
import game.presentation.ui.panels.game.PitchPanel
import game.utils.Utility.timePassed
import game.utils.dev.Log
import game.utils.time.now

abstract class EntityInteractableLogic(
        override val entity: EntityInteractable
) : EntityLogic(entity), IEntityInteractableLogic {
    override fun attack(e: Entity?) {
        val gameBehavior: GameBehavior = object : GameBehavior() {
            override fun hostBehavior(): () -> Unit {
                return {
                    if (!(e == null || e.state.isImmune || e.state.state == State.DIED)) {
                        val attackDamage: Int = entity.state.attackDamage
                        e.logic.onAttackReceived(attackDamage)
                        AttackEntityEventForwarder().invoke(e.toEntityNetwork(), attackDamage)
                    }
                }
            }

            override fun clientBehavior(): () -> Unit {
                return {}
            }

        }
        gameBehavior.invoke()
    }

    override fun move(coordinates: Coordinates) {
        Log.i("Move $coordinates")
        entity.info.position = coordinates
        onMove(coordinates)
    }

    final override fun interact(e: Entity?) {
        if (e == null) {
            interactAndUpdateLastInteract(null)
            return
        }

        /*if (canInteractWith(e) && e.logic.canBeInteractedBy(entity)) {
            interactAndUpdateLastInteract(e)
        } else if (e is EntityInteractable && e.logic.canInteractWith(entity) && canBeInteractedBy(e)) {
            e.logic.interactAndUpdateLastInteract(entity)
        }*/
        // SUPER TODO CHECK THIS!
        if (canInteractWith(e) && e.logic.canBeInteractedBy(entity)) {
            entity.logic.interactAndUpdateLastInteract(e)

            if (e is EntityInteractable) {
                e.logic.interactAndUpdateLastInteract(entity)
            }
        }
    }

    @Synchronized
    override fun interactAndUpdateLastInteract(e: Entity?) {
        if (timePassed(entity.state.lastInteractionTime) < EntityInteractable.INTERACTION_DELAY_MS) {
            return
        }
        doInteract(e) // Interact with the entity.
        if (e is EntityInteractable) {
            updateLastInteract(e) // Update the last interaction for this entity.
        }
    }

    private fun updateLastInteract(e: EntityInteractable) {
        e.state.lastInteractionTime = now()
    }


    /**
     * Moves or interacts with other entities in the given direction and with the default step size and offset.
     *
     * @param d the direction to move or interact in
     * @return true if the entity can move in the given direction, false otherwise
     */
    override fun moveOrInteract(d: Direction?, stepSize: Int): Boolean {
        return moveOrInteract(d, stepSize, false)
    }

    /**
     * Moves or interacts with other entities in the given direction and with the given step size and default offset.
     *
     * @param d        the direction to move or interact in
     * @param stepSize the step size to use
     */
    override fun moveOrInteract(d: Direction?, stepSize: Int, ignoreMapBorders: Boolean): Boolean {
        d ?: return false

        val nextTopLeftCoords = Coordinates.nextCoords(entity.info.position, d, stepSize)

        if (!nextTopLeftCoords.validate(entity)) {
            if (!ignoreMapBorders) {
                interact(null)
                return false
            }
        } else {
            val coordinatesInArea = Coordinates.getAllBlocksInAreaFromDirection(
                    entity,
                    d,
                    stepSize
            )

            val allEntitiesCanBeInteractedWith = coordinatesInArea.all { c: Coordinates? ->
                val entitiesOnBlock = Coordinates.getEntitiesOnBlock(c)
                entitiesOnBlock.isEmpty() || entitiesOnBlock.all { e: Entity ->
                    !entity.logic.canBeInteractedBy(e) && !canInteractWith(e) && !(isObstacle(e) && e !== entity)
                }
            }

            if (allEntitiesCanBeInteractedWith) {
                move(nextTopLeftCoords)
                return true
            }
        }

        // Get the coordinates of the next positions that will be occupied if the entity moves in a certain direction
        // with a given step size
        val nextOccupiedCoords = Coordinates.getNewCoordinatesOnDirection(
                entity.info.position,
                d, stepSize,
                PitchPanel.GRID_SIZE / 3 / 2,
                entity.state.size
        )

        // Get a list of entities that are present in the next occupied coordinates
        val interactedEntities = Coordinates.getEntitiesOnCoordinates(nextOccupiedCoords)

        // If there are no entities present in the next occupied coordinates, update the entity's position
        if (interactedEntities.isEmpty()) {
            move(nextTopLeftCoords)
            return true
        }

        // Initialize a flag to indicate whether the entity can move
        var canMove = true

        var encounteredObstacle = false

        try {
            for (e in interactedEntities) {
                if (isObstacle(e)) {
                    interact(e)
                    encounteredObstacle = true
                }
                canMove = false
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

        if (!encounteredObstacle) {
            // Interact with non-null entities in the 'interactedEntities' list
            interactedEntities.stream().forEach { e: Entity? -> interact(e ?: return@forEach) }
        }

        // If the entity can move or it is immune to bombs, update the entity's position
        //if the entity is instance of explosion, it'll be able to move further anyway but no more explosions will be generated in constructor
        if (entity is AbstractExplosion && !canMove) {
            (entity as AbstractExplosion).logic.onObstacle(nextTopLeftCoords)
        } else if (canMove) {
            move(nextTopLeftCoords)
        }

        // Return whether the entity can move or not
        return canMove
    }

    override fun isObstacle(e: Entity?): Boolean {
        return e == null || entity.state.obstacles.any { c: Class<out Entity> -> c.isInstance(e) }
                && entity.state.whitelistObstacles.none { c: Class<out Entity> -> c.isInstance(e) }
    }

    override fun canInteractWith(e: Entity?): Boolean {
        return e == null || entity.state.interactionEntities.any { c: Class<out Entity?> -> c.isInstance(e) }
    }

}