package game.utils;

import game.Bomberman;
import game.level.Level;

public class Paths {
    public static String getPlayerDataPath() {
        return String.format("%s/data", Paths.getDataFolder());
    }

    public static String getDataFolder() {
        return "data";
    }

    public static String getAssetsFolder() {
        return "assets";
    }

    public static String getEntitiesFolder() {
        return String.format("%s/entities", getAssetsFolder());
    }

    public static String getEnemiesFolder() {
        return String.format("%s/enemies", getEntitiesFolder());
    }

    public static String getWorldsFolder() {
        return String.format("%s/worlds", getAssetsFolder());
    }

    public static String getCurrentLevelFolder() {
        Level level = Bomberman.getMatch().getCurrentLevel();
        return String.format("%s/%d/level/%d", getWorldsFolder(), level.getWorldId(), level.getLevelId());
    }

    public static String getCurrentWorldCommonFolder() {
        return String.format("%s/%d/common", getWorldsFolder(), Bomberman.getMatch().getCurrentLevel().getWorldId());
    }

    public static String getPowerUpsFolder() {
        return String.format("%s/powerups", getAssetsFolder());
    }

    public static String getPowerUpsBorderPath() {
        return String.format("%s/powerups_border.png", getMenuImagesPath());
    }

    public static String getMenuImagesPath() {
        return String.format("%s/menu/images", getAssetsFolder());
    }

    public static String getBackgroundImage() {
        return String.format("%s/background.jpg", getMenuImagesPath());
    }

    public static String getMainMenuWallpaper() {
        return String.format("%s/welcome.png", getMenuImagesPath());
    }

    public static String getDeathWallpaper() {
        return String.format("%s/death.jpg", getMenuImagesPath());
    }

    public static String getWorldSelectorPortalPath(int id) {
        return String.format("%s/world_selector/world%d.png", Paths.getAssetsFolder(), id);
    }

    public static String getPowerupsLogoPath() {
        return String.format("%s/powerups_logo.png", getPowerUpsFolder());
    }

    public static String getInventoryPath() {
        return String.format("%s/inventory", getAssetsFolder());
    }

    public static String getSoundsPath() {
        return String.format("%s/sounds", getAssetsFolder());
    }

    public static String getItemsPath() {
        return String.format("%s/items", getAssetsFolder());
    }

    public static String getDefaultSoundTrack() {
        return String.format("%s/menu/sound/soundtrack.wav", getAssetsFolder());
    }

    public static String getUiFolder() {
        return String.format("%s/ui", getAssetsFolder());
    }


    public static String getCursorPath() {
        return String.format("%s/cursor.png", getUiFolder());
    }

    public static String getIconPath() {
        return getMenuImagesPath() + "/frame_icon.png";
    }
}
