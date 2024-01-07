package game.entity.enemies.npcs

import game.entity.EntityTypes
import game.utils.Paths.enemiesFolder

class Helicopter() : FlyingEnemy() {
    constructor(id: Long) : this() {
        this.id = id
    }

    override val entitiesAssetsPath: String get() ="$enemiesFolder/heli"

    override fun getCharacterOrientedImages(): Array<String> = Array(3) { index ->
        "$entitiesAssetsPath/heli_${imageDirection.toString().lowercase()}_$index.gif"
    }

    override val type: EntityTypes
        get() = EntityTypes.Helicopter
}