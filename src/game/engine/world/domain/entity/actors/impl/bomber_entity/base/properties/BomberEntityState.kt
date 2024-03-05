package game.engine.world.domain.entity.actors.impl.bomber_entity.base.properties

import game.engine.hardwareinput.Command
import game.engine.world.domain.entity.actors.abstracts.base.Entity
import game.engine.world.domain.entity.actors.abstracts.character.Character
import game.engine.world.domain.entity.actors.abstracts.character.properties.CharacterEntityState
import game.engine.world.domain.entity.actors.abstracts.enemy.Enemy
import game.engine.world.domain.entity.actors.abstracts.entity_interactable.EntityInteractable
import game.engine.world.domain.entity.actors.abstracts.moving_entity.MovingEntity
import game.engine.world.domain.entity.actors.impl.explosion.abstractexpl.AbstractExplosion
import game.engine.world.domain.entity.actors.impl.placeable.Bomb
import game.engine.world.domain.entity.actors.impl.bomber_entity.base.BomberEntity
import game.engine.world.domain.entity.actors.impl.models.State
import game.engine.world.domain.entity.geo.Direction
import game.engine.world.domain.entity.items.UsableItem
import game.engine.world.domain.entity.pickups.powerups.PowerUp
import java.util.concurrent.atomic.AtomicReference

class BomberEntityState(
        entity: BomberEntity,
        isSpawned: Boolean = Entity.DEFAULT.SPAWNED,
        isImmune: Boolean = Entity.DEFAULT.IMMUNE,
        state: AtomicReference<State>? = Entity.DEFAULT.STATE,
        isInvisible: Boolean = Entity.DEFAULT.IS_INVISIBLE,
        size: Int = Character.DEFAULT.SIZE,
        alpha: Float = Entity.DEFAULT.ALPHA,
        interactionEntities: MutableSet<Class<out Entity>> = BomberEntity.DEFAULT.INTERACTION_ENTITIES,
        whitelistObstacles: MutableSet<Class<out Entity>> = EntityInteractable.DEFAULT.WHITELIST_OBSTACLES,
        obstacles: Set<Class<out Entity>> = EntityInteractable.DEFAULT.OBSTACLES,
        lastInteractionTime: Long = EntityInteractable.DEFAULT.LAST_INTERACTION_TIME,
        lastDamageTime: Long = EntityInteractable.DEFAULT.LAST_DAMAGE_TIME,
        attackDamage: Int = EntityInteractable.DEFAULT.ATTACK_DAMAGE,
        direction: Direction = MovingEntity.DEFAULT.DIRECTION,
        lastDirectionUpdate: Long = Character.DEFAULT.LAST_DIRECTION_UPDATE,
        commandQueue: MutableSet<Command> = Character.DEFAULT.COMMAND_QUEUE,
        previousDirection: Direction? = Character.DEFAULT.PREVIOUS_DIRECTION,
        canMove: Boolean = Character.DEFAULT.CAN_MOVE,
        maxHp: Int = Character.DEFAULT.MAX_HP,
        speed: Float = Character.DEFAULT.SPEED,
        imageDirection: Direction? = Character.DEFAULT.IMAGE_DIRECTION
) : CharacterEntityState(
        entity = entity,
        interactionEntities = interactionEntities,
        size = size,
        isSpawned = isSpawned,
        isImmune = isImmune,
        state = state,
        isInvisible = isInvisible,
        alpha = alpha,
        whitelistObstacles = whitelistObstacles,
        obstacles = obstacles,
        lastInteractionTime = lastInteractionTime,
        lastDamageTime = lastDamageTime,
        attackDamage = attackDamage,
        direction = direction,
        lastDirectionUpdate = lastDirectionUpdate,
        commandQueue = commandQueue,
        previousDirection = previousDirection,
        canMove = canMove,
        speed = speed,
        imageDirection = imageDirection,
        maxHp = maxHp
) {
    lateinit var weapon: UsableItem
    val entitiesClassListMouseClick: MutableList<Class<out Entity>> = mutableListOf()
    val entitiesClassListMouseDrag: MutableList<Class<out Entity>> = mutableListOf()
    var currExplosionLength = 0
    var placedBombs = 0
    var lastPlacedBombTime: Long = 0
    var currentBombs = 0

    var bombsSolid: Boolean = true
        set(value) {
            field = value

            // Adjust the whitelist of obstacles based on the bombs' solidity.
            if (!field) {
                whitelistObstacles.add(Bomb::class.java)
            } else if (!forceBombsSolid) {
                whitelistObstacles.remove(Bomb::class.java)
            }
        }

    var forceBombsSolid: Boolean = false
    var activePowerUps: MutableList<Class<out PowerUp>> = mutableListOf()
}
