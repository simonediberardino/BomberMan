package game.viewcontrollers;

import game.ui.viewelements.inventory.InventoryElementView;
import game.utils.Utility;
import game.viewmodels.InventoryElementModel;

import javax.swing.*;
import java.awt.*;

public class InventoryElementController {
    private final InventoryElementModel model;
    private final InventoryElementView view;

    public InventoryElementController(int numItems, String imagePath) {
        model = new InventoryElementModel(numItems, imagePath);
        view = new InventoryElementView();
        updateView();
    }

    public void setNumItems(int numItems) {
        model.setNumItems(numItems);
        updateView();
    }

    public void setImagePath(String imagePath) {
        model.setImagePath(imagePath);
        updateView();
    }

    private void updateView() {
        Image img = Utility.loadImage(model.getImagePath());
        ImageIcon imageIcon = new ImageIcon(img.getScaledInstance(InventoryElementView.SIZE, InventoryElementView.SIZE, 0));
        view.setIcon(imageIcon);
        view.setNumItems(model.getNumItems());
    }

    public InventoryElementView getView() {
        return view;
    }
}
