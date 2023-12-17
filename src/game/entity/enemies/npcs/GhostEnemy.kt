package game.entity.enemies.npcs

import game.entity.EntityTypes
import game.entity.models.Direction
import game.entity.models.Entity
import game.ui.panels.game.PitchPanel
import game.utils.Paths.enemiesFolder
import java.util.*

class GhostEnemy : IntelligentEnemy() {
    init {
        hitboxSizetoWidthRatio = 0.837f
        hitboxSizeToHeightRatio = 1f
    }

    override fun getCharacterOrientedImages(): Array<String> {
        return arrayOf("$enemiesFolder/mini_ghost/ghost_${imageDirection.toString().lowercase()}.png")
    }

    override fun getImageDirections(): List<Direction> {
        return listOf(Direction.RIGHT, Direction.LEFT)
    }

    override fun getObstacles(): Set<Class<out Entity?>> {
        return interactionsEntities
    }

    override fun getSize(): Int {
        return PitchPanel.COMMON_DIVISOR * 2
    }

    override fun getType(): EntityTypes {
        return EntityTypes.GhostEnemy
    }
}