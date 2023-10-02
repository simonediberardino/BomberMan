package game.viewcontrollers;

import game.utils.Paths;

public class InventoryElementControllerLives extends InventoryElementController{
    public InventoryElementControllerLives() {
        super(0, Paths.getPowerUpsFolder()  + "/lives_up.png");
    }
}
