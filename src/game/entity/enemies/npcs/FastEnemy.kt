package game.entity.enemies.npcs

import game.entity.EntityTypes
import game.entity.models.Coordinates
import game.entity.models.Direction
import game.ui.panels.game.PitchPanel
import game.utils.Paths.enemiesFolder

/**
 * An enemy with increased speed multiplier;
 */
class FastEnemy : IntelligentEnemy {
    constructor() : super()
    constructor(id: Long) : super(id)
    constructor(coordinates: Coordinates?) : super(coordinates)

    init {
        hitboxSizeToHeightRatio = 0.527f
    }

    override val entitiesAssetsPath: String get() = "$enemiesFolder/fast_enemy/fast_enemy"

    override fun getCharacterOrientedImages(): Array<String> = Array(4) { index ->
        "${entitiesAssetsPath}_${imageDirection.toString().lowercase()}_$index.png"
    }

    override fun getSpeed(): Float = 1f

    override fun getImageDirections(): List<Direction> = listOf(Direction.RIGHT, Direction.LEFT)

    override val size: Int
        get() = PitchPanel.COMMON_DIVISOR * 2

    override val type: EntityTypes
        get() = EntityTypes.FastEnemy
}