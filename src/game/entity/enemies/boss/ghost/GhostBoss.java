package game.entity.enemies.boss.ghost;

import game.Bomberman;
import game.entity.enemies.npcs.GhostEnemy;
import game.entity.enemies.boss.Boss;
import game.entity.models.Coordinates;
import game.entity.models.Direction;
import game.entity.models.Entity;
import game.events.RunnablePar;
import game.sound.AudioManager;
import game.sound.SoundModel;
import game.ui.panels.game.PitchPanel;
import game.utils.GradientCallbackHandler;
import game.utils.Paths;
import game.utils.Utility;

import javax.swing.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GhostBoss extends Boss {
    private static final String SKIN_PATH_TEMPLATE = "%s/ghost_boss/ghost_with_axe_%s_%s.png";
    private static final int ACTION_CHANCE = 1;
    private static final int INVISIBLE_DURATION = 3500;
    private static final int ATTACK_RESET_DELAY = 1000;
    private static final int INVISIBLE_DELAY = 10000;
    private static final int MAX_GHOST_ENEMY_SPAWNED = 5;
    private static final int GHOST_SPAWN_TIME_DELAY = 10000;
    private static final int LIGHTS_EVENT_DELAY = 20000;
    private static long lastLightsEvent = System.currentTimeMillis();
    private static final int MAX_GHOSTS_ALIVE = 10;
    private final static int BOSS_ATTACK_VERTICAL_RANGE = 2;
    private final static int BOSS_ATTACK_HORIZONTAL_RANGE = 1;
    private boolean isInvisibleTaskRunning = false;
    private long lastInvisibleTime = 0;
    private long lastGhostSpawnTime;
    private static int lock1;
    private static int lock2;

    public GhostBoss() {
        super();
        this.hitboxSizetoWidthRatio= 0.648f;
        this.hitboxSizeToHeightRatio = 0.70f;
        paddingTopFunction = new RunnablePar() {
            @Override
            public <T> Object execute(T par) {
                setPaddingTop(0);
                return 0;
            }
        };
    }

    @Override
    public void attack(Entity e){
        attackAnimationAndSoundFX();
        super.attack(e);
    }

    @Override
    protected Map<Integer, Integer> healthStatusMap() {
        return new HashMap<>();
    }

    @Override
    protected String getImageFromRageStatus() {
        return String.format(SKIN_PATH_TEMPLATE, Paths.getEnemiesFolder(), imageDirection.toString().toLowerCase(), currRageStatus);
    }

    @Override
    public String[] getCharacterOrientedImages() {
        return new String[] { getImageFromRageStatus() };
    }

    @Override
    protected java.util.List<Direction> getImageDirections() {
        return Arrays.asList(Direction.RIGHT, Direction.LEFT);
    }

    private void attackAnimationAndSoundFX() {
        if(currRageStatus == 1) return;

        AudioManager.getInstance().play(SoundModel.AXE_HIT);
        updateRageStatus(1);
        Timer t = new Timer(ATTACK_RESET_DELAY, (l) -> updateRageStatus(0));
        t.setRepeats(false);
        t.start();
    }

    /**
     * This method makes the Ghost disappear and then reappear by changing its alpha value gradually.
     */
    private void disappearAndReappear() {
        synchronized ((Object) lock2) {
            if (isInvisibleTaskRunning) {
                // If an invisible task is already running, exit the method.
                return;
            }

            // Check if enough time has passed since the last invisibility to start the task.
            if (Utility.timePassed(lastInvisibleTime) < INVISIBLE_DELAY) {
                return;
            }

            // Record the current time as the last invisibility time.
            lastInvisibleTime = System.currentTimeMillis();
            // Set the flag to indicate that an invisible task is running.
            isInvisibleTaskRunning = true;

            // Define the start and end alpha values, and the step for alpha increment/decrement.
            float startAlpha = 1f, endAlpha = 0f, step = 0.01f;

            // Create a callback handler for the task of gradually showing the object.
            GradientCallbackHandler showTask = new GradientCallbackHandler(new RunnablePar() {
                @Override
                public <T> Object execute(T par) {
                    // Set the alpha value of the object to the given parameter value.
                    setAlpha((Float) par);

                    if ((Float) par >= startAlpha) {
                        // If the alpha value has reached or exceeded the start alpha, mark the invisible task as finished.
                        isInvisibleTaskRunning = false;
                    }
                    return null;
                }
            }, endAlpha, startAlpha, -step);

            // Create a callback handler for the task of gradually hiding the object.
            GradientCallbackHandler hideTask = new GradientCallbackHandler(new RunnablePar() {
                @Override
                public <T> Object execute(T par) {
                    // Set the alpha value of the object to the given parameter value.
                    setAlpha((Float) par);

                    if (!((Float) par <= endAlpha)) {
                        // If the alpha value is not yet less than or equal to the end alpha, exit the method.
                        return null;
                    }

                    // Create a timer to delay the execution of the show task after the object is hidden.
                    Timer t = new Timer(INVISIBLE_DURATION, (l) -> showTask.execute());
                    t.setRepeats(false);
                    t.start();

                    return null;
                }
            }, startAlpha, endAlpha, step);

            // Start the task of gradually hiding the object.
            hideTask.execute();
        }
    }

    private void spawnGhosts(int n) {
        if(Utility.timePassed(lastGhostSpawnTime) < GHOST_SPAWN_TIME_DELAY) return;

        lastGhostSpawnTime = System.currentTimeMillis();

        for(int i = 0; i < n; i++){
            if(Bomberman.getMatch().getEnemiesAlive() >= MAX_GHOSTS_ALIVE) return;
            new GhostEnemy().spawnAtRandomCoordinates();
        }
    }

    private static void performLightsAnimation() {
        synchronized ((Object) lock1) {
            SwingUtilities.invokeLater(() -> {
                Timer timer = new Timer(0, null);
                var ref = new Object() {
                    int count = 0;
                };

                timer.addActionListener(e -> {
                    int rand = (int) (Math.random() * 10000);

                    switch (ref.count) {
                        case 0: case 2: case 4: PitchPanel.turnOffLights(); break;
                        case 1: case 3: PitchPanel.turnOnLights(); break;
                    }

                    timer.setDelay(rand);

                    if(ref.count >= 5 || Bomberman.isGameEnded()) {
                        PitchPanel.turnOnLights();
                        timer.stop();
                    }

                    ref.count++;
                });

                timer.setInitialDelay(0);
                timer.start();
            });
        }
    }

    private void attack() {
        java.util.List<Coordinates> coordsOfUnderneathEntityBlocks = Coordinates.getAllBlocksInAreaFromDirection(this,Direction.DOWN,BOSS_ATTACK_VERTICAL_RANGE);
        java.util.List<Coordinates>  coordsOfEntitysImageDirectionBlocks = Coordinates.getAllBlocksInAreaFromDirection(this,imageDirection, BOSS_ATTACK_HORIZONTAL_RANGE);
        coordsOfUnderneathEntityBlocks.addAll(coordsOfEntitysImageDirectionBlocks);

        //merge the 2 lists into one another
        coordsOfUnderneathEntityBlocks.forEach(c-> Coordinates.getEntitiesOnBlock(c).forEach(this::interact));
        attackAnimationAndSoundFX();
    }

    @Override
    public void doUpdate(boolean gamestate) {
        Utility.runPercentage(ACTION_CHANCE, this::attack);
        Utility.runPercentage(ACTION_CHANCE, this::disappearAndReappear);
        Utility.runPercentage(ACTION_CHANCE, () -> {
            if (Utility.timePassed(lastLightsEvent) <= LIGHTS_EVENT_DELAY) {
                return;
            }

            performLightsAnimation();
            lastLightsEvent = System.currentTimeMillis();
        });

        Utility.runPercentage(ACTION_CHANCE, () -> {
            int enemyCount = (int) (Math.random() * MAX_GHOST_ENEMY_SPAWNED);     //spawn ghost enemies
            spawnGhosts(enemyCount);
        });

        super.doUpdate(gamestate);
    }
}
