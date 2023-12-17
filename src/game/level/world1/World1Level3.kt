package game.level.world1

import game.entity.enemies.npcs.Helicopter
import game.entity.enemies.npcs.YellowBall
import game.entity.models.Enemy
import game.level.Level

class World1Level3 : World1Level() {
    override val levelId: Int
        get() {
            return 3
        }

    override val startEnemiesCount: Int
        get() {
            return 7
        }

    override val availableEnemies: Array<Class<out Enemy>>
        get() {
            return arrayOf(
                    YellowBall::class.java,
                    Helicopter::class.java
            )
        }

    override val nextLevel: Class<out Level?>
        get() {
            return World1Level4::class.java
        }
}