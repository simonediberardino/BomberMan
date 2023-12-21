package game.level.actorbehavior

import game.Bomberman
import game.entity.Player
import game.entity.models.Coordinates

class GeneratePlayerBehavior(val coordinates: Coordinates): GameBehavior {
    override fun hostBehavior(): () -> Unit {
        return {
            Bomberman.getMatch().player = Player(coordinates)
            Bomberman.getMatch().player.spawn(false, false)
        }
    }

    override fun clientBehavior(): () -> Unit {
        return {}
    }
}