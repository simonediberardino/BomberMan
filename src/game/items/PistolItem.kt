package game.items

import game.entity.blocks.DestroyableBlock
import game.entity.blocks.HardBlock
import game.entity.bomb.AbstractExplosion
import game.entity.bomb.AbstractExplosion.Companion.SIZE
import game.entity.bomb.Bomb
import game.entity.bomb.PistolExplosion
import game.entity.models.Coordinates
import game.entity.models.Enemy
import game.entity.models.Entity
import game.entity.models.Explosive
import game.sound.AudioManager
import game.sound.SoundModel
import game.utils.Paths.itemsPath
import game.utils.Utility.timePassed

class PistolItem : UsableItem(), Explosive {
    private var bullets = 5
    override val explosionObstacles: Set<Class<out Entity>>
        get() = setOf(
                HardBlock::class.java,
                DestroyableBlock::class.java
        )

    override val explosionInteractionEntities: Set<Class<out Entity>>
        get() = setOf(
                Enemy::class.java,
                Bomb::class.java
        )

    override val maxExplosionDistance: Int
        get() {
            return 3
        }

    override fun use() {
        if (timePassed(owner.lastPlacedBombTime) < Bomb.PLACE_INTERVAL) {
            return
        }
        owner.lastPlacedBombTime = System.currentTimeMillis()
        bullets--

        val explosion = PistolExplosion(
                owner,
                Coordinates.nextCoords(owner.coords, owner.currDirection, SIZE),
                owner.currDirection,
                1,
                this
        )

        AudioManager.getInstance().play(SoundModel.EXPLOSION)
        explosion.explode()
        if (bullets == 0) {
            remove()
        }
    }

    override val imagePath: String
        get() {
            return "$itemsPath/pistol.png"
        }

    override val count: Int
        get() {
            return bullets
        }
}