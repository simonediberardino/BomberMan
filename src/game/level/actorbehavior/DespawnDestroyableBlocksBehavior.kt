package game.level.actorbehavior

import game.Bomberman
import game.entity.blocks.DestroyableBlock
import game.entity.models.Entity

class DespawnDestroyableBlocksBehavior : GameBehavior {
    override fun hostBehavior(): () -> Unit {
        return {
            despawnDestroyableBlocks()
        }
    }

    override fun clientBehavior(): () -> Unit {
        return {}
    }

    private fun despawnDestroyableBlocks() {
        Bomberman.getMatch()
                .entities
                .stream()
                .filter { entity: Entity? -> entity is DestroyableBlock }
                .forEach { obj: Entity -> obj.despawn() }
    }

}
