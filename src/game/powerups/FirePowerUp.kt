package game.powerups

import game.entity.bomb.AbstractExplosion.Companion.MAX_EXPLOSION_LENGTH
import game.entity.models.BomberEntity
import game.entity.models.Coordinates
import game.events.game.ExplosionLengthPowerUpEvent
import game.utils.Paths.powerUpsFolder
import java.awt.image.BufferedImage

class FirePowerUp
/**
 * Constructs an entity with the given coordinates.
 *
 * @param coordinates the coordinates of the entity
 */
(coordinates: Coordinates?) : PowerUp(coordinates) {
    override fun getImage(): BufferedImage {
        return loadAndSetImage("$powerUpsFolder/fire_up.png")
    }

    override val duration: Int
        get() {
            return 0
        }

    override fun doApply(entity: BomberEntity) {
        ExplosionLengthPowerUpEvent().invoke(entity)
    }

    override fun cancel(entity: BomberEntity) {}

    override fun canPickUp(entity: BomberEntity): Boolean {
        return entity.currExplosionLength <= MAX_EXPLOSION_LENGTH
    }
}