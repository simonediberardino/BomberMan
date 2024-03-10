package game.domain.level.gamehandler.model

import game.domain.world.domain.entity.actors.abstracts.enemy.Enemy
import game.domain.level.levels.Level
import java.awt.Image

abstract class GameHandler(protected val level: Level) {
    open fun generate() {
        generateStone()
        generatePlayer()
        startLevel()
    }

    fun startLevel() {
        generateDestroyableBlock()
        spawnBoss()
        spawnEnemies()
        level.onStartLevel()
    }

    abstract fun generateStone()
    abstract fun generatePlayer()
    abstract fun generateDestroyableBlock()
    abstract fun spawnBoss()
    abstract fun spawnEnemies()
    abstract fun spawnEnemies(availableEnemies: Array<Class<out Enemy>>)
    abstract val borderImages: Array<Image?>
}