package game.network.events.process

import game.Bomberman
import game.domain.world.domain.entity.actors.abstracts.base.Entity
import game.domain.events.models.HttpEvent
import game.utils.dev.Extensions.getOrTrim
import game.utils.dev.Log

class DespawnedEntityHttpEventProcessor : HttpEvent {
    override fun invoke(vararg extras: Any) {
        val info = extras[0] as Map<String, String>
        val entityId = info.getOrTrim("entityId")?.toLong() ?: return

        Log.i("DespawnedEntityHttpEventProcessor received $entityId")

        val entity: Entity = Bomberman.match.getEntityById(entityId) ?: return
        entity.logic.despawn()
    }
}