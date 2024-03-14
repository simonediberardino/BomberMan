package game.domain.events.game

import game.Bomberman
import game.domain.world.domain.entity.actors.impl.bomber_entity.base.BomberEntity
import game.domain.events.models.GameEvent


class ExplosionLengthPowerUpEvent : GameEvent {
    override fun invoke(arg: Any?) {
        Bomberman.match.currentLevel!!.eventHandler.onUpdateBombsLengthEvent(arg as BomberEntity, arg.state.currExplosionLength + 1)
    }
}