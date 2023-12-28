package game.actorbehavior

import game.Bomberman
import game.entity.player.RemotePlayer
import game.utils.Extensions.getOrTrim

class PlayerConnectedBehavior(val info: Map<String, String>) : GameBehavior {
    override fun hostBehavior(): () -> Unit {
        return {
            val clientId = info.getOrTrim("id")?.toLong()
            println("PlayerConnectedBehavior: $clientId")

            val match = Bomberman.getMatch()
            val coordinates = (match.currentLevel)?.info?.playerSpawnCoordinates

            if (coordinates != null && clientId != null) {
                val player = RemotePlayer(coordinates, clientId, 1)
                player.spawn()
            }
        }
    }

    override fun clientBehavior(): () -> Unit {
        return {}
    }
}