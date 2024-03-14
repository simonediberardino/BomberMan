package game.domain.events.game

import game.Bomberman
import game.domain.events.models.GameEvent


class ScoreGameEvent : GameEvent {
    override fun invoke(arg: Any?) {
        Bomberman.match.currentLevel!!.eventHandler.onScoreGameEvent(arg as Int)
    }
}