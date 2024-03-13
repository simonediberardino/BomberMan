package game.domain.world.domain.entity.actors.impl.enemies.npcs.tank.properties

import game.input.Command
import game.domain.world.domain.entity.actors.abstracts.base.Entity
import game.domain.world.domain.entity.actors.abstracts.character.Character
import game.domain.world.domain.entity.actors.abstracts.enemy.Enemy
import game.domain.world.domain.entity.actors.abstracts.enemy.properties.EnemyEntityState
import game.domain.world.domain.entity.actors.abstracts.entity_interactable.EntityInteractable
import game.domain.world.domain.entity.actors.abstracts.moving_entity.MovingEntity
import game.domain.world.domain.entity.actors.impl.enemies.npcs.tank.TankEnemy
import game.domain.world.domain.entity.actors.impl.models.State
import game.domain.world.domain.entity.geo.Direction
import java.util.concurrent.atomic.AtomicReference

class TankEnemyState(
        entity: Entity,
        size: Int = Enemy.DEFAULT.SIZE,
        interactionEntities: MutableSet<Class<out Entity>> = Enemy.DEFAULT.INTERACTION_ENTITIES,
        speed: Float = Character.DEFAULT.SPEED,
        maxHp: Int = Character.DEFAULT.MAX_HP,
        var lastFire: Long = TankEnemy.DEFAULT.LAST_FIRE,
        var canShoot: Boolean = TankEnemy.DEFAULT.CAN_SHOOT,
        isSpawned: Boolean = Entity.DEFAULT.SPAWNED,
        isImmune: Boolean = Entity.DEFAULT.IMMUNE,
        state: State? = Entity.DEFAULT.STATE,
        isInvisible: Boolean = Entity.DEFAULT.IS_INVISIBLE,
        alpha: Float = Entity.DEFAULT.ALPHA,
        whitelistObstacles: MutableSet<Class<out Entity>> = EntityInteractable.DEFAULT.WHITELIST_OBSTACLES,
        obstacles: Set<Class<out Entity>> = Enemy.DEFAULT.OBSTACLES,
        lastInteractionTime: Long = EntityInteractable.DEFAULT.LAST_INTERACTION_TIME,
        lastDamageTime: Long = EntityInteractable.DEFAULT.LAST_DAMAGE_TIME,
        attackDamage: Int = EntityInteractable.DEFAULT.ATTACK_DAMAGE,
        direction: Direction = MovingEntity.DEFAULT.DIRECTION,
        lastDirectionUpdate: Long = Character.DEFAULT.LAST_DIRECTION_UPDATE,
        
        previousDirection: Direction? = Character.DEFAULT.PREVIOUS_DIRECTION,
        canMove: Boolean = Character.DEFAULT.CAN_MOVE,
        
) : EnemyEntityState(
        entity = entity,
        size = size,
        interactionEntities = interactionEntities,
        speed = speed,
        maxHp = maxHp,
        isSpawned = isSpawned,
        isImmune = isImmune,
        state = state,
        isInvisible = isInvisible,
        alpha = alpha,
        whitelistObstacles = whitelistObstacles,
        obstacles = obstacles,
        lastInteractionTime = lastInteractionTime,
        lastDamageTime = lastDamageTime,
        attackDamage = attackDamage,
        direction = direction,
        lastDirectionUpdate = lastDirectionUpdate,
        
        previousDirection = previousDirection,
        canMove = canMove,

)