package game.entity.models;

import game.Bomberman;
import game.entity.Player;
import game.entity.bomb.AbstractExplosion;
import game.hardwareinput.Command;
import game.hardwareinput.ControllerManager;
import game.entity.enemies.npcs.Zombie;
import game.sound.AudioManager;
import game.sound.SoundModel;
import game.ui.panels.game.PitchPanel;
import game.utils.Utility;
import game.values.DrawPriority;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import static game.entity.models.Direction.*;
import static game.entity.models.Direction.DOWN;


/**
 * Represents a character in the game, which can move and interact with the environment.
 */
public abstract class Character extends MovingEntity {
    public static final int SIZE = PitchPanel.PIXEL_UNIT * 4 * 2;
    protected final List<Direction> imagePossibleDirections = getImageDirections();
    protected long lastDirectionUpdate = 0;
    protected Direction currDirection = DOWN;
    /**
     * The last direction this character was moving in.
     */
    protected Direction previousDirection = null;
    protected Direction imageDirection = null;
    /**
     * Whether this character is alive or not.
     */
    protected volatile boolean isAlive = true;
    protected boolean isImmune = false;
    protected volatile AtomicReference<State> state = new AtomicReference<>();
    protected boolean canMove = true;

    private int maxHp = 100;
    private int healthPoints = maxHp;

    /**
     * Returns an array of file names for the left-facing icons for this character.
     *
     * @return an array of file names for the left-facing icons
     */

    public Character(Coordinates coordinates) {
        super(coordinates);
    }

    public abstract String[] getCharacterOrientedImages();

    private void setImageDirection() {
        if (imagePossibleDirections.contains(currDirection)) imageDirection = currDirection;
        else if (imageDirection == null) imageDirection = imagePossibleDirections.get(0);
    }

    /**
     * Returns whether this character is alive or not.
     *
     * @return true if this character is alive, false otherwise
     */
    public boolean getAliveState() {
        return isAlive;
    }

    public void setAliveState(boolean s) {
        isAlive = s;
    }

    /**
     * Returns the size of this character.
     *
     * @return the size of this character
     */
    @Override
    public int getSize() {
        return SIZE;
    }

    /**
     * Returns the image for this character based on its current state.
     *
     * @return the image for this character
     */

    @Override
    public BufferedImage getImage() {
        setImageDirection();
        if (this.image != null) {
            return this.image;
        } else {
            currDirection = Direction.DOWN;
            return loadAndSetImage(getCharacterOrientedImages()[0]);
        }
    }

    public boolean isImmune() {
        return isImmune;
    }

    public void setImmune(boolean immune) {
        if (!isAlive) return;

        isImmune = immune;
        state.set(immune ? State.IMMUNE : State.ALIVE);
    }

    protected boolean useOnlyBaseIcons() {
        return false;
    }

    @Override
    protected void onDespawn() {
        super.onDespawn();
    }

    @Override
    protected void onSpawn() {
        super.onSpawn();
        state.set(State.ALIVE);
        isAlive = true;
    }

    protected void playStepSound() {
        if (this instanceof Player) {
            int a = 0;
        }
        SoundModel stepSound = getStepSound();
        if (stepSound != null) AudioManager.getInstance().play(stepSound, false);
    }


    private String[] refreshDirectionAndGetCharsImages() {
        setImageDirection();
        return getCharacterOrientedImages();
    }

    protected void updateLastDirection(Direction d) {
        // If the character doesn't have custom images for each direction, do not check if the direction has changed;
        if (useOnlyBaseIcons()) {
            String[] baseIcons = refreshDirectionAndGetCharsImages();

            if (Utility.timePassed(lastImageUpdate) > getImageRefreshRate()) {
                // If it's time to refresh the image, increment the image index.
                lastImageIndex++;
                playStepSound();
            } else return;

            // Ensure the icon index is within bounds.
            if (lastImageIndex < 0 || lastImageIndex >= baseIcons.length)
                lastImageIndex = 0;

            loadAndSetImage(baseIcons[lastImageIndex]);

            return;
        }

        // If both previousDirection and currDirection are null, initialize currDirection to d and load the front-facing image.

        if (previousDirection == null && currDirection == null) {
            currDirection = d;
            loadAndSetImage(getCharacterOrientedImages()[0]);
            return;
        }


        // Update previousDirection and currDirection to reflect the new direction.
        previousDirection = currDirection;
        currDirection = d;

        String[] baseIcons = refreshDirectionAndGetCharsImages();

        // If the previousDirection and current direction are different, reset the image index and last direction update time.
        if (previousDirection != d) {
            lastImageIndex = 0;
            lastDirectionUpdate = System.currentTimeMillis();
        } else if (Utility.timePassed(lastImageUpdate) > getImageRefreshRate()) {
            // If it's time to refresh the image, increment the image index.
            lastImageIndex++;
            playStepSound();
        } else {
            // Otherwise, don't update the image yet.
            return;
        }

        // Ensure the icon index is within bounds.
        if (lastImageIndex < 0 || lastImageIndex >= baseIcons.length)
            lastImageIndex = 0;

        loadAndSetImage(baseIcons[lastImageIndex]);
    }

    @Override
    public BufferedImage loadAndSetImage(String imagePath) {
        if (state == null) return super.loadAndSetImage(imagePath);

        String[] toks = imagePath.split(Pattern.quote("."));
        String extension = toks[1];
        String fileName = toks[0];

        String imagePathWithStatus = String.format("%s_%s.%s", fileName, state.toString().toLowerCase(), extension);

        File f = new File(imagePathWithStatus);
        return f.exists() ? super.loadAndSetImage(imagePathWithStatus) : super.loadAndSetImage(imagePath);
    }

    /**
     * Attempts to move the entity in the specified direction and updates the last direction if the move was successful.
     *
     * @param d The direction to move the entity in.
     * @return true if the move was successful, false otherwise.
     */
    public final boolean move(Direction d) {
        // If the entity is not alive, return true.
        if (!getAliveState()) return true;

        // Try to move or interact with the entity in the specified direction.
        if (moveOrInteract(d)) {
            // If the move or interaction was successful, update the last direction and return true.
            updateLastDirection(d);
            return true;
        }

        // Otherwise, return false.
        return false;
    }


    @Override
    public Set<Class<? extends Entity>> getObstacles() {
        return super.getObstacles();
    }

    public float getSpeed() {
        return 1f;
    }

    @Override
    protected float getDelayObserverUpdate() {
        return super.getDelayObserverUpdate() / getSpeed();
    }

    /**
     * Starts a damage animation that makes the entity invisible and visible
     * repeatedly for a certain number of iterations with a delay between each iteration.
     * The animation lasts for a duration of {@code EntityInteractable.INTERACTION_DELAY_MS} milliseconds.
     */
    protected void startDamageAnimation() {
        // Duration of each iteration of the animation
        int durationMs = 100;
        // Calculate the number of iterations required to reach the total duration
        int iterations = (int) (EntityInteractable.INTERACTION_DELAY_MS / durationMs);

        // Create a Timer object to schedule the animation iterations
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            // Counter to keep track of the number of iterations
            int count = 0;

            @Override
            public void run() {
                // If the number of iterations has been reached, cancel the timer and return
                if (count >= iterations || !isSpawned()) {
                    timer.cancel();
                    return;
                }

                try {
                    // Make the entity invisible and wait for the specified duration
                    setInvisible(true);
                    Thread.sleep(durationMs);
                    // Make the entity visible again and wait for the specified duration
                    setInvisible(false);
                    Thread.sleep(durationMs);
                } catch (InterruptedException ignored) {
                }

                // Increment the counter to keep track of the number of iterations
                count++;
            }
        }, 0, durationMs * 2); // Schedule the timer to repeat with a fixed delay of durationMs * 2 between iterations
    }

    protected List<Direction> getImageDirections() {
        return Arrays.asList(Direction.values());
    }

    public int getMaxHp() {
        return maxHp;
    }

    protected void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    /**
     * Returns the current health points of the entity.
     *
     * @return The current health points of the entity.
     */
    protected int getHp() {
        return healthPoints;
    }

    /**
     * Sets the health points of the entity to the specified value.
     *
     * @param newHp The new health points to set for the entity.
     */
    protected void setHp(int newHp) {
        healthPoints = newHp;
    }

    protected int getHpPercentage() {
        return (int) (((float) getHp() / (float) getMaxHp()) * 100);
    }

    @Override
    public DrawPriority getDrawPriority() {
        return DrawPriority.DRAW_PRIORITY_2;
    }

    /**
     * Removes the specified amount of damage from the entity's health points.
     * If the health points reach 0 or below, the entity is despawned.
     * Otherwise, a damage animation is started.
     *
     * @param damage The amount of damage to remove from the entity's health points.
     */
    protected final void attackReceived(int damage) {
        synchronized ((Object) lastDamageTime) {
            if (Utility.timePassed(lastDamageTime) < INTERACTION_DELAY_MS)
                return;

            lastDamageTime = System.currentTimeMillis();

            // Reduce the health points by the specified amount
            healthPoints -= damage;

            startDamageAnimation();

            // If the health points reach 0 or below, despawn the entity
            if (healthPoints <= 0) {
                healthPoints = 0;
                eliminated();
            } else {
                onHit(damage);
            }
        }
    }

    public void eliminated() {
        if (!getAliveState()) {
            return;
        }
        setAliveState(false);
        state.set(State.DIED);
        onEliminated();
    }


    protected void onEndedDeathAnimation() {
    }

    protected void onEliminated() {
        canMove = false;
        AudioManager.getInstance().play(getDeathSound());

        // Create a Timer object to schedule the animation iterations
        javax.swing.Timer timer = new javax.swing.Timer((int) INTERACTION_DELAY_MS, (e) -> {
            onEndedDeathAnimation();

            despawn();
        });

        timer.setRepeats(false);
        timer.start();
    }

    protected SoundModel getDeathSound() {
        return SoundModel.ENTITY_DEATH;
    }

    protected void onHit(int damage) {
    }

    @Override
    public void onExplosion(AbstractExplosion explosion) {
        explosion.attack(this);
    }
}
