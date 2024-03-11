package game.domain.match

import game.Bomberman
import game.data.data.DataInputOutput
import game.data.data.SortedLinkedList
import game.domain.world.domain.entity.actors.impl.bomber_entity.player.Player
import game.domain.world.domain.entity.actors.impl.placeable.bomb.Bomb
import game.domain.world.domain.entity.actors.impl.bomber_entity.base.BomberEntity
import game.domain.world.domain.entity.actors.abstracts.base.Entity
import game.input.ControllerManager
import game.input.ControllerManager.Companion.setDefaultCommandDelay
import game.input.MouseControllerManager
import game.domain.world.domain.entity.items.BombItem
import game.domain.world.domain.entity.items.UsableItem
import game.domain.level.levels.Level
import game.domain.level.behavior.PlayLevelSoundTrackBehavior
import game.network.events.forward.UseItemHttpEventForwarder
import game.network.gamehandler.ClientGameHandler
import game.network.gamehandler.OnlineGameHandler
import game.network.gamehandler.ServerGameHandler
import game.domain.tasks.GamePausedObserver
import game.domain.tasks.GameTickerObservable
import game.presentation.ui.pages.PausePanel
import game.presentation.ui.panels.game.MatchPanel
import game.utils.dev.Log
import game.utils.Utility.timePassed
import game.presentation.ui.viewcontrollers.*
import game.utils.time.now
import java.util.*
import kotlin.collections.HashMap

class BomberManMatch(var currentLevel: Level?, val onlineGameHandler: OnlineGameHandler?) {
    // Timestamp of the last game pause state
    private var lastGamePauseStateTime = now()

    // List of entities sorted by a linked list
    private val _entitiesList: SortedLinkedList<Entity> = SortedLinkedList()
    private val _entitiesMap: HashMap<Long, Entity> = HashMap()

    // Manager for mouse controllers
    val mouseControllerManager: MouseControllerManager = MouseControllerManager()

    // Manager for general controllers (nullable)
    var controllerManager: ControllerManager? = ControllerManager()
        private set

    // Observable for game tick events (nullable)
    var gameTickerObservable: GameTickerObservable? = GameTickerObservable()
        private set

    // Controllers for inventory elements (lateinit and nullable)
    lateinit var inventoryElementControllerPoints: InventoryElementController
        private set
    lateinit var inventoryElementControllerBombs: InventoryElementController
        private set
    lateinit var inventoryElementControllerLives: InventoryElementController
        private set
    var inventoryElementControllerRounds: InventoryElementController? = null
        private set

    // Player information (nullable)
    var player: Player? = null

    // List of bombs in the game
    val bombs = ArrayList<Bomb>()

    // Current game state (default: false)
    var gameState = false

    // Number of enemies currently alive (read-only)
    var enemiesAlive = 0
        private set

    private constructor() : this(null, null)

    init {
        setupViewControllers()
        setDefaultCommandDelay()
        Log.i("${javaClass.simpleName}, onlineGameHandler: $onlineGameHandler, ${onlineGameHandler?.isRunning()}")

        if (onlineGameHandler?.isRunning() != true) {
            onlineGameHandler?.onStart()
        }
    }

    /**
     * Sets up various view controllers based on the current game state.
     */
    private fun setupViewControllers() {
        // Set up points and bombs controllers
        setupPointsController()
        setupBombsController()

        // Set up rounds or lives controller based on the game level type
        if (currentLevel!!.info.isArenaLevel) {
            setupRoundsController()
        } else {
            setupLivesController()
        }

        // Update the inventory weapon controller with the current player's weapon information
        updateInventoryWeaponController()
    }

    /**
     * Sets up the points controller with the current score from DataInputOutput.
     */
    private fun setupPointsController() {
        inventoryElementControllerPoints = InventoryElementControllerPoints()
        inventoryElementControllerPoints.setNumItems(DataInputOutput.getInstance().score.toInt())
    }

    /**
     * Sets up the bombs controller.
     */
    private fun setupBombsController() {
        inventoryElementControllerBombs = InventoryElementControllerBombs()
    }

    /**
     * Sets up the rounds controller for arena levels.
     */
    private fun setupRoundsController() {
        inventoryElementControllerRounds = InventoryElementControllerRounds()
    }

    /**
     * Sets up the lives controller for non-arena levels.
     */
    private fun setupLivesController() {
        inventoryElementControllerLives = InventoryElementControllerLives()
        inventoryElementControllerLives.setNumItems(DataInputOutput.getInstance().lives)
    }


    /**
     * Gives an item to the specified owner (BomberEntity).
     *
     * @param owner The BomberEntity receiving the item.
     * @param item The UsableItem to be given.
     * @param combineSameItem Flag indicating whether to combine items of the same type.
     */
    fun give(owner: BomberEntity, item: UsableItem, combineSameItem: Boolean = false) {
        if (combineSameItem && owner.state.weapon.javaClass == item.javaClass) {
            // Combine items if requested and owner already has the same type of item
            owner.state.weapon.combineItems(item)
        } else {
            // Set the new item as the owner's weapon and update related components
            owner.state.weapon = item
            owner.state.weapon.owner = owner
            updateInventoryWeaponController()
        }
    }

    fun useItem(owner: BomberEntity) {
        owner.state.weapon.use()
        UseItemHttpEventForwarder().invoke(owner.toDto(), owner.state.weapon.type)
    }


    /**
     * Removes the current item from the specified owner (BomberEntity) and replaces it with a BombItem.
     *
     * @param owner The BomberEntity from which to remove the item.
     */
    fun removeItem(owner: BomberEntity) {
        // Replace the current item with a BombItem and update related components
        owner.state.weapon = BombItem()
        owner.state.weapon.owner = owner
        updateInventoryWeaponController()
    }

    /**
     * Updates the inventory weapon controller with the current player's weapon information.
     * Does nothing if the player is not set.
     */
    fun updateInventoryWeaponController() {
        player ?: return
        val playerItem = player!!.state.weapon
        inventoryElementControllerBombs.setImagePath(playerItem.imagePath)
        inventoryElementControllerBombs.setNumItems(playerItem.count)
    }

    /**
     * Checks if the game is running in client mode.
     *
     * @return True if the client game handler is not null and connected, false otherwise.
     */
    val isClient: Boolean
        get() = onlineGameHandler is ClientGameHandler /*&& onlineGameHandler.connected*/

    /**
     * Checks if the game is running in server mode.
     *
     * @return True if the server game handler is not null and running, false otherwise.
     */
    val isServer: Boolean
        get() = onlineGameHandler is ServerGameHandler || onlineGameHandler == null /*&& onlineGameHandler.running*/

    /**
     * Adds a bomb to the list of bombs in the game.
     *
     * @param bomb The Bomb to be added.
     */
    fun addBomb(bomb: Bomb) {
        bombs.add(bomb)
    }

    /**
     * Removes a bomb from the list of bombs in the game.
     *
     * @param bomb The Bomb to be removed.
     */
    fun removeBomb(bomb: Bomb) {
        bombs.remove(bomb)
    }

    /**
     * Returns a copy of the list of _entities to prevent external modifications.
     */
    fun getEntities(): List<Entity> = synchronized(_entitiesList) { LinkedList(_entitiesList) }

    fun getEntityById(entityId: Long): Entity? = _entitiesMap[entityId]

    fun addEntity(entity: Entity) {
        Log.i("Adding entity, ${entity.info.type}")
        synchronized(_entitiesList) {
            _entitiesList.add(entity)
        }

        synchronized(_entitiesMap) {
            _entitiesMap.put(entity.info.id, entity)
        }

        System.gc()
    }

    fun removeEntity(entity: Entity) {
        synchronized(_entitiesList) {
            _entitiesList.removeIf { it.info.id == entity.info.id }
        }

        synchronized(_entitiesMap) {
            _entitiesMap.remove(entity.info.id)
        }

        System.gc()
    }

    /**
     * Toggles the game state between paused and resumed.
     */
    fun toggleGameState() {
        if (timePassed(lastGamePauseStateTime) < 500) {
            return
        }

        lastGamePauseStateTime = now()

        if (gameState) {
            pauseGame()
        } else {
            resumeGame()
        }
    }


    /**
     * Pauses the game by stopping the game ticker, setting the game state to false,
     * and displaying the pause panel.
     */
    private fun pauseGame() {
        // Stop the game ticker to pause game events
        gameTickerObservable?.stop()

        // Update the game state to indicate it is paused
        gameState = false

        // Show the pause panel to the player
        Bomberman.showActivity(PausePanel::class.java)
    }

    /**
     * Resumes the game by resuming the game ticker, setting the game state to true,
     * showing the match panel, and playing the level soundtrack.
     */
    private fun resumeGame() {
        // Resume the game ticker to continue game events
        gameTickerObservable?.resume()

        // Update the game state to indicate it is resumed
        gameState = true

        // Show the match panel to the player
        Bomberman.showActivity(MatchPanel::class.java)

        // Play the level soundtrack
        PlayLevelSoundTrackBehavior(currentLevel ?: return).invoke()
    }

    /**
     * Decreases the count of enemies currently alive in the game.
     */
    fun decreaseEnemiesAlive() {
        enemiesAlive--
    }

    /**
     * Increases the count of enemies currently alive in the game.
     */
    fun increaseEnemiesAlive() {
        enemiesAlive++
    }

    /**
     * Performs cleanup operations and releases resources associated with the game.
     */
    fun destroy() {
        // Pause the game to ensure safe destruction
        pauseGame()

        // Destroy all _entities in the game
        destroyEntities()

        // Clear graphics callback in the Bomberman frame's pitch panel
        clearGraphicsCallback()

        // Stop the sound associated with the current game level
        stopLevelSound()

        // Reset the game state variables
        resetState()

        // Stop the movement task for mouse controllers
        stopMovementTask()

        // Unregister all observables and controllers
        unregisterAllObservablesAndControllers()

        // Perform garbage collection to release memory
        performGarbageCollection()
    }

    /**
     * Destroys all _entities in the game by despawning them.
     */
    private fun destroyEntities() {
        getEntities().forEach { it.logic.eliminated() }
    }

    /**
     * Clears the graphics callback in the Bomberman frame's pitch panel.
     */
    private fun clearGraphicsCallback() {
        Bomberman.getBombermanFrame().pitchPanel.clearGraphicsCallback()
    }

    /**
     * Stops the sound associated with the current game level.
     */
    private fun stopLevelSound() {
        currentLevel?.currentLevelSound?.stop()
    }

    /**
     * Resets the game state variables, setting them to default values or null.
     */
    private fun resetState() {
        player = null
        currentLevel = null
        enemiesAlive = 0
        _entitiesList.clear()
    }

    /**
     * Stops the movement task for mouse controllers.
     */
    private fun stopMovementTask() {
        mouseControllerManager.stopMovementTask()
    }

    /**
     * Unregisters all observables and controllers associated with the game.
     */
    private fun unregisterAllObservablesAndControllers() {
        gameTickerObservable?.unregisterAll()
        gameTickerObservable = null

        controllerManager = null
    }

    /**
     * Initiates the garbage collection process to release unused memory.
     */
    private fun performGarbageCollection() {
        System.gc()
    }
}
