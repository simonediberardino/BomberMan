package game.entity;

import game.Bomberman;
import game.data.DataInputOutput;
import game.entity.bomb.AbstractExplosion;
import game.events.UpdateCurrentAvailableBombsEvent;
import game.hardwareinput.Command;
import game.entity.models.*;
import game.events.DeathGameEvent;
import game.entity.models.Coordinates;
import game.hardwareinput.ControllerManager;
import game.powerups.PowerUp;
import game.sound.SoundModel;
import game.ui.panels.game.PitchPanel;
import game.ui.panels.menus.GameOverPanel;
import game.utils.Paths;

import javax.swing.Timer;
import java.util.*;

import static game.entity.models.Direction.*;
import static game.entity.models.Direction.DOWN;
import static game.ui.panels.game.PitchPanel.GRID_SIZE;


public class Player extends BomberEntity {
    public static final Coordinates SPAWN_OFFSET = new Coordinates((GRID_SIZE - SIZE) / 2, GRID_SIZE - SIZE);
    private final Set<Class<? extends Entity>> interactionEntities = new HashSet<>();

    public Player(Coordinates coordinates) {
        super(coordinates);
        this.hitboxSizeToHeightRatio = 0.733f;
    }

    private void updateBombs() {
        int maxBombs = DataInputOutput.getInstance().getObtainedBombs();
        new UpdateCurrentAvailableBombsEvent().invoke(maxBombs);
    }

    @Override
    protected void doInteract(Entity e) {
    }

    @Override
    public Set<Class<? extends Entity>> getInteractionsEntities() {
        return this.interactionEntities;
    }

    @Override
    protected String getBasePath() {
        return Paths.getEntitiesFolder() + "/player";
    }

    @Override
    public String[] getCharacterOrientedImages() {
        return new String[]{
                String.format("%s/player_%s_%d.png", getBasePath(), imageDirection.toString().toLowerCase(), 1),
                String.format("%s/player_%s_%d.png", getBasePath(), imageDirection.toString().toLowerCase(), 2),
                String.format("%s/player_%s_%d.png", getBasePath(), imageDirection.toString().toLowerCase(), 1),
                String.format("%s/player_%s_%d.png", getBasePath(), imageDirection.toString().toLowerCase(), 0),
        };
    }

    public void handleMoveCommand(Command command, Direction oppositeDirection1, Direction oppositeDirection2) {
        boolean moveSuccessful = move(command.commandToDirection());

        if (moveSuccessful) {
            return;
        }

        List<Coordinates> oppositeBlocksCoordinates = getNewCoordinatesOnDirection(command.commandToDirection(), PitchPanel.PIXEL_UNIT, getSize());
        List<Entity> entitiesOpposite1 = Coordinates.getEntitiesOnBlock(oppositeBlocksCoordinates.get(0));
        List<Entity> entitiesOpposite2 = Coordinates.getEntitiesOnBlock(oppositeBlocksCoordinates.get(1));
        overpassBlock(entitiesOpposite1, entitiesOpposite2, oppositeDirection1, oppositeDirection2);
    }

    /**
     * Attempts to overpass a block in the opposite direction of the player's movement.
     * The method checks for the existence of obstacles and the player input to determine the appropriate direction to move.
     *
     * @param entitiesOpposite1 A list of entities on the first opposite coordinate of the player's movement.
     * @param entitiesOpposite2 A list of entities on the second opposite coordinate of the player's movement.
     * @param direction1        The first opposite direction to the player's movement.
     * @param direction2        The second opposite direction to the player's movement.
     */
    public void overpassBlock(List<Entity> entitiesOpposite1, List<Entity> entitiesOpposite2, Direction direction1, Direction direction2) {
        Command oppositeCommand1 = direction2.toCommand();
        Command oppositeCommand2 = direction1.toCommand();

        ControllerManager controllerManager = Bomberman.getMatch().getControllerManager();
        boolean doubleClick1 = controllerManager.isCommandPressed(oppositeCommand1);
        boolean doubleClick2 = controllerManager.isCommandPressed(oppositeCommand2);
        if (doubleClick2 || doubleClick1) return;
        // If the first direction has no obstacles and the second does, and the second direction is not double-clicked, move in the second direction.
        if (!entitiesOpposite1.isEmpty() && entitiesOpposite2.isEmpty()) {
            move(direction2);
        }
        // If the second direction has no obstacles and the first does, and the first direction is not double-clicked, move in the first direction.
        else if (!entitiesOpposite2.isEmpty() && entitiesOpposite1.isEmpty()) {
            move(direction1);
        }
    }


    @Override
    protected void onSpawn() {
        super.onSpawn();
        updateBombs();
        Bomberman.getMatch().getControllerManager().register(this);
        Bomberman.getBombermanFrame().getMatchPanel().refreshPowerUps(getActivePowerUps());
    }

    @Override
    protected void onEndedDeathAnimation() {
        Timer t = new Timer((int) SHOW_DEATH_PAGE_DELAY_MS, (e) -> showDeathPage());
        t.setRepeats(false);
        t.start();
    }

    private void showDeathPage() {
        Bomberman.destroyLevel();
        Bomberman.showActivity(GameOverPanel.class);
    }

    @Override
    protected void onEliminated() {
        super.onEliminated();
        new DeathGameEvent().invoke(null);
    }

    public void handleAction(Command command) {
        if (!Bomberman.getMatch().getGameState()) {
            return;
        }
        if (canMove) {
            switch (command) {
                // For move up and move down, use left and right as opposite directions respectively.
                case MOVE_UP:
                case MOVE_DOWN:
                    handleMoveCommand(command, LEFT, RIGHT);
                    break;
                // For move left and move right, use up and down as opposite directions respectively.
                case MOVE_LEFT:
                case MOVE_RIGHT:
                    handleMoveCommand(command, UP, DOWN);
                    break;
            }
        }

        switch (command) {
            case PLACE_BOMB:
                placeBomb();
                break;
        }
    }

    @Override
    public Coordinates getSpawnOffset() {
        return SPAWN_OFFSET;
    }

    // Handle the command entered by the player;
    @Override
    public void update(Object arg) {
        super.update(arg);
        handleAction((Command) arg);
    }

    @Override
    protected SoundModel getDeathSound() {
        return SoundModel.PLAYER_DEATH;
    }

    @Override
    protected Set<Class<? extends Entity>> getBasePassiveInteractionEntities() {
        return new HashSet<>(Arrays.asList(AbstractExplosion.class, Enemy.class, PowerUp.class));
    }

    @Override
    protected SoundModel getStepSound() {
        return SoundModel.STEP_SOUND;
    }
}
