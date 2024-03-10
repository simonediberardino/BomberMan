package game.domain.world.domain.entity.actors.impl.placeable.base.state

import game.domain.world.domain.entity.actors.abstracts.base.Entity
import game.domain.world.domain.entity.actors.abstracts.character.Character
import game.domain.world.domain.entity.actors.impl.blocks.base_block.logic.BlockEntityLogic
import game.domain.world.domain.entity.actors.impl.blocks.base_block.properties.BlockEntityState
import game.domain.world.domain.entity.actors.impl.blocks.movable_block.MovableBlock
import game.domain.world.domain.entity.actors.impl.models.State
import game.presentation.ui.panels.game.PitchPanel
import java.util.concurrent.atomic.AtomicReference

open class PlaceableEntityState(
        entity: Entity,
        isSpawned: Boolean = Entity.DEFAULT.SPAWNED,
        isImmune: Boolean = Entity.DEFAULT.IMMUNE,
        state: AtomicReference<State>? = Entity.DEFAULT.STATE,
        isInvisible: Boolean = Entity.DEFAULT.IS_INVISIBLE,
        size: Int = PitchPanel.GRID_SIZE,
        alpha: Float = Entity.DEFAULT.ALPHA,
        interactionEntities: MutableSet<Class<out Entity>> = Entity.DEFAULT.INTERACTION_ENTITIES,
        lastImageUpdate: Long = Entity.DEFAULT.LAST_IMAGE_UPDATE
) : BlockEntityState(entity = entity,
        isSpawned = isSpawned,
        isImmune = isImmune,
        state = state,
        isInvisible = isInvisible,
        size = size,
        alpha = alpha,
        interactionEntities = interactionEntities,
        lastImageUpdate = lastImageUpdate) {
    open lateinit var caller: Character
}