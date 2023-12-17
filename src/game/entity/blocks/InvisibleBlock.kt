package game.entity.blocks

import game.entity.models.Coordinates
import game.entity.models.Entity
import java.awt.image.BufferedImage

class InvisibleBlock(coordinates: Coordinates?) : HardBlock(coordinates) {
    override fun doInteract(e: Entity?) {}
    override fun getImage(): BufferedImage? {
        return null
    }
}
