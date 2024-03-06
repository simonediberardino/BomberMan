package game.engine.world.domain.entity.actors.impl.enemies.boss.clown.hat

import game.engine.world.types.EntityTypes
import game.engine.world.domain.entity.actors.impl.enemies.boss.clown.orb.Orb
import game.engine.world.domain.entity.geo.Coordinates
import game.engine.world.domain.entity.geo.EnhancedDirection
import game.engine.world.domain.entity.actors.abstracts.base.Entity
import game.engine.world.domain.entity.actors.abstracts.base.EntityInfo
import game.engine.world.domain.entity.actors.abstracts.character.graphics.CharacterGraphicsBehavior
import game.engine.world.domain.entity.actors.abstracts.character.graphics.CharacterImageModel
import game.engine.world.domain.entity.actors.abstracts.character.graphics.ICharacterGraphicsBehavior
import game.engine.world.domain.entity.actors.impl.bomber_entity.base.BomberEntity
import game.engine.world.domain.entity.actors.impl.enemies.boss.clown.Clown
import game.engine.world.domain.entity.actors.impl.enemies.boss.clown.hat.logic.HatEntityLogic
import game.engine.world.domain.entity.actors.impl.enemies.boss.clown.hat.properties.HatEntityState
import game.engine.world.domain.entity.actors.impl.enemies.boss.clown.orb.properties.OrbEntityProperties
import game.utils.Paths

open class Hat : Orb {
    constructor(coordinates: Coordinates?, enhancedDirection: EnhancedDirection?) : super(coordinates, enhancedDirection) {}

    constructor() : this(null, null)

    constructor(id: Long) : super(id)

    override val logic: HatEntityLogic = HatEntityLogic(entity = this)
    override val properties: OrbEntityProperties = OrbEntityProperties(types = EntityTypes.Hat)
    override val graphicsBehavior: ICharacterGraphicsBehavior = CharacterGraphicsBehavior(entity = this)
    override val state: HatEntityState = HatEntityState(entity = this)
    override val info: EntityInfo = EntityInfo()
    override val image: CharacterImageModel = object : CharacterImageModel(
            entity = this,
            entitiesAssetsPath = "${Paths.enemiesFolder}/clown/hat") {
        override fun characterOrientedImages(): Array<String> {
            return Array(10) { index ->
                "${entitiesAssetsPath}${index + 1}.png"
            }
        }
    }

    internal object DEFAULT {
        val INTERACTION_ENTITIES = mutableSetOf<Class<out Entity>>(BomberEntity::class.java, Clown::class.java)
        val SIZE = Orb.DEFAULT.SIZE * 3
        const val MAX_HP = 300
    }
}