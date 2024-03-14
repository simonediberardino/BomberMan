package game.domain.world.domain.entity.items

import game.Bomberman
import game.domain.events.game.UpdateCurrentAvailableItemsEvent
import game.domain.world.domain.entity.actors.impl.placeable.bomb.Bomb
import game.network.events.forward.UpdateInfoEventForwarder
import game.utils.Utility.timePassed
import game.utils.dev.Log
import game.utils.file_system.Paths.entitiesFolder
import game.utils.time.now

class BombItem : UsableItem() {
    private lateinit var bombEntity: Bomb

    override fun use() {
        val match = Bomberman.match ?: return

        val isLocalPlayer = owner == match.player
        val isBombPlacementValid = isLocalPlayer &&
                (owner.state.currExplosionLength == 0 || owner.state.placedBombs >= owner.state.maxBombs || owner.state.currentBombs <= 0)

        val isBombPlacementIntervalValid = timePassed(owner.state.lastPlacedBombTime) >= Bomb.PLACE_INTERVAL

        if (!isBombPlacementValid || !isBombPlacementIntervalValid) {
            return
        }

        owner.state.lastPlacedBombTime = now()
        owner.state.placedBombs++
        owner.state.bombsSolid = (false)

        if (isLocalPlayer)
            UpdateCurrentAvailableItemsEvent().invoke(owner.state.currentBombs - 1)

        bombEntity = Bomb(owner)

        Log.e("Confirmation caller ${bombEntity.state.caller}")
        match.addBomb(bombEntity)
        UpdateInfoEventForwarder().invoke((bombEntity as game.domain.world.domain.entity.actors.abstracts.base.Entity).toEntityNetwork())

        bombEntity.logic.onExplodeCallback = {
            owner.state.placedBombs--
            match.removeBomb(bombEntity)

            if (isLocalPlayer)
                UpdateCurrentAvailableItemsEvent().invoke(owner.state.currentBombs + 1)
        }

        bombEntity.logic.spawn(forceSpawn = true)
        bombEntity.logic.trigger()
    }

    override fun combineItems(item: UsableItem) {
    }


    override val imagePath: String = "$entitiesFolder/bomb/bomb_0.png"

    override val count: Int
        get() = owner.state.currentBombs

    override val type: ItemsTypes = ItemsTypes.BombItem
}