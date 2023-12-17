package game.powerups

import game.entity.models.BomberEntity
import game.entity.models.Coordinates
import game.utils.Paths.powerUpsFolder
import java.awt.image.BufferedImage

class TransparentBombsPowerUp(coordinates: Coordinates?) : PowerUp(coordinates) {
    override fun getImage(): BufferedImage = loadAndSetImage("$powerUpsFolder/transparent_bomb_powerup.png")

    override fun doApply(entity: BomberEntity) {
        entity.forceSetBombsNotSolid(true)
        entity.setBombsSolid(false)
    }

    override fun cancel(entity: BomberEntity) {
        entity.forceSetBombsNotSolid(false)
        entity.setBombsSolid(true)
    }
}