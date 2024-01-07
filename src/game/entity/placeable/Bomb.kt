package game.entity.placeable

import game.Bomberman
import game.entity.EntityTypes
import game.entity.blocks.DestroyableBlock
import game.entity.blocks.HardBlock
import game.entity.bomb.AbstractExplosion
import game.entity.bomb.FireExplosion
import game.entity.models.*
import game.sound.AudioManager
import game.sound.SoundModel
import game.ui.panels.game.PitchPanel
import game.utils.Paths
import game.utils.Utility
import java.awt.image.BufferedImage
import java.util.*

/**
 * Represents a bomb entity that can explode and cause damage in the game.
 *
 * @param caller The entity that spawned the bomb.
 */
open class Bomb(caller: Character) : PlaceableEntity(caller), Explosive {

    /**
     * Constructor for creating a bomb with a specific ID.
     *
     * @param id The unique identifier for the bomb.
     * @param caller The entity that spawned the bomb.
     */
    constructor(id: Long, caller: Character) : this(caller) {
        this.id = id
    }

    /**
     * Gets the path to the folder containing bomb-related assets.
     *
     * @return The assets path for the bomb.
     */
    override fun getEntitiesAssetsPath(): String = "${Paths.entitiesFolder}/bomb/"

    /**
     * Gets the current image representing the bomb.
     *
     * @return The current image of the bomb.
     */
    override fun getImage(): BufferedImage {
        val imagesCount = 3
        val images = Array(imagesCount) { i -> "${entitiesAssetsPath}bomb_$i.png" }

        // Check if enough time has passed for an image refresh
        if (Utility.timePassed(lastImageUpdate) < imageRefreshRate) {
            return image
        }

        // Load the next image in the sequence
        val img = loadAndSetImage(images[lastImageIndex])
        AudioManager.getInstance().play(SoundModel.BOMB_CLOCK)
        lastImageIndex = (lastImageIndex + 1) % images.size

        return img
    }

    /**
     * Handles the interaction between the bomb and another entity.
     *
     * @param e The entity to interact with.
     */
    override fun doInteract(e: Entity?) {}

    /**
     * Gets the type of the entity.
     *
     * @return The type of the entity.
     */
    override fun getType(): EntityTypes = EntityTypes.Bomb

    /**
     * Callback for the bomb exploding. Initiates the explosion in all directions.
     */
    var onExplodeCallback: (() -> Unit)? = null

    /**
     * Initiates the explosion of the bomb in all directions.
     */
    fun explode() {
        if (!isSpawned)
            return

        despawnAndNotify()
        AudioManager.getInstance().play(SoundModel.EXPLOSION)

        // Trigger explosions in all directions
        Direction.values().forEach {
            FireExplosion(user, coords, it, this).explode()
        }

        // Invoke the explode callback if set
        onExplodeCallback?.invoke()
    }

    /**
     * Triggers the bomb to explode after a set time delay.
     */
    fun trigger() {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                explode()
            }
        }, EXPLODE_TIMER.toLong())
    }

    /**
     * Gets the size of the bomb.
     *
     * @return The size of the bomb.
     */
    override fun getSize(): Int = BOMB_SIZE

    /**
     * Gets the set of obstacle entities affected by the bomb explosion.
     *
     * @return The set of obstacle entities.
     */
    override val explosionObstacles: Set<Class<out Entity>> =
            setOf(HardBlock::class.java, DestroyableBlock::class.java)

    /**
     * Gets the set of entities affected by the bomb explosion.
     *
     * @return The set of entities affected by the explosion.
     */
    override val explosionInteractionEntities: Set<Class<out Entity>> =
            setOf(DestroyableBlock::class.java, Character::class.java, Bomb::class.java)

    /**
     * Gets the maximum explosion distance for the bomb.
     *
     * @return The maximum explosion distance.
     */
    override val maxExplosionDistance: Int
        get() = Bomberman.getMatch().player?.currExplosionLength ?: 0

    /**
     * Handles mouse click interaction with the bomb.
     */
    override fun onMouseClickInteraction() {
        eliminated()
    }

    /**
     * Destroys the bomb, initiating the explosion.
     */
    override fun destroy() = explode()

    /**
     * Gets the set of base passive interaction entities for the bomb.
     *
     * @return The set of base passive interaction entities.
     */
    override fun getBasePassiveInteractionEntities(): Set<Class<out Entity>> =
            setOf(FireExplosion::class.java, AbstractExplosion::class.java)

    /**
     * Callback for handling the bomb's reaction to an explosion.
     *
     * @param explosion The explosion that triggered the callback.
     */
    override fun onExplosion(explosion: AbstractExplosion) = explode()

    /**
     * Companion object containing constant values for the Bomb class.
     */
    companion object {
        val BOMB_SIZE = PitchPanel.COMMON_DIVISOR * 2
        const val PLACE_INTERVAL: Long = 1000
        const val EXPLODE_TIMER = 5000
    }
}

