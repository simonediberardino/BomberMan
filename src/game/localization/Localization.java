package game.localization;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Optional;

public class Localization {
    public static final HashMap<Locale, String> MY_BOMBERMAN = new HashMap<>() {{
        put(Locale.ENGLISH, "MY BOMBERMAN");
        put(Locale.ITALY, "IL MIO BOMBERMAN");
    }};

    public static final HashMap<Locale, String> KEY_RIGHT = new HashMap<>() {{
        put(Locale.ENGLISH, "RIGHT");
        put(Locale.ITALY, "DESTRA");
    }};

    public static final HashMap<Locale, String> KEY_LEFT = new HashMap<>() {{
        put(Locale.ENGLISH, "LEFT");
        put(Locale.ITALY, "SINISTRA");
    }};

    public static final HashMap<Locale, String> KEY_FORWARD = new HashMap<>() {{
        put(Locale.ENGLISH, "FORWARD");
        put(Locale.ITALY, "AVANTI");
    }};

    public static final HashMap<Locale, String> KEY_BACK = new HashMap<>() {{
        put(Locale.ENGLISH, "BACK");
        put(Locale.ITALY, "INDIETRO");
    }};

    public static final HashMap<Locale, String> KEY_BOMB = new HashMap<>() {{
        put(Locale.ENGLISH, "BOMB");
        put(Locale.ITALY, "BOMBA");
    }};

    public static final HashMap<Locale, String> DEATHS = new HashMap<>() {{
        put(Locale.ENGLISH, "DEATHS");
        put(Locale.ITALY, "MORTI");
    }};

    public static final HashMap<Locale, String> PLAYER_STATS = new HashMap<>() {{
        put(Locale.ENGLISH, "PLAYER STATS");
        put(Locale.ITALY, "STATISTICHE");
    }};

    public static final HashMap<Locale, String> LOST_GAMES = new HashMap<>() {{
        put(Locale.ENGLISH, "LOST GAMES");
        put(Locale.ITALY, "PARTITE PERSE");
    }};

    public static final HashMap<Locale, String> KILLS = new HashMap<>() {{
        put(Locale.ENGLISH, "KILLS");
        put(Locale.ITALY, "UCCISIONI");
    }};

    public static final HashMap<Locale, String> ROUNDS = new HashMap<>() {{
        put(Locale.ENGLISH, "ROUNDS PASSED");
        put(Locale.ITALY, "ROUND VINTI");
    }};

    public static final HashMap<Locale, String> POINTS = new HashMap<>() {{
        put(Locale.ENGLISH, "POINTS");
        put(Locale.ITALY, "PUNTI");
    }};

    public static final HashMap<Locale, String> LIVES = new HashMap<>() {{
        put(Locale.ENGLISH, "LIVES");
        put(Locale.ITALY, "VITE");
    }};

    public static final HashMap<Locale, String> WELCOME_TEXT = new HashMap<>() {{
        put(Locale.ENGLISH, "Welcome %user% to Bomberman!");
        put(Locale.ITALY, "Benvenuto %user% su Bomberman!");
    }};

    public static final HashMap<Locale, String> MYSTERY_BOX_ERROR_MONEY = new HashMap<>() {{
        put(Locale.ENGLISH, "YOU DON'T HAVE ENOUGH COINS [%price%]");
        put(Locale.ITALY, "NON HAI ABBASTANZA MONETE [%price%]");
    }};


    public static final HashMap<Locale, String> MYSTERY_BOX_CONFIRM = new HashMap<>() {{
        put(Locale.ENGLISH, "CLICK AGAIN TO CONFIRM YOUR PURCHASE [%price%]");
        put(Locale.ITALY, "PREMI DI NUOVO PER CONFERMARE [%price%]");
    }};


    public static final HashMap<Locale, String> PRESS_ESC_CONTINUE = new HashMap<>() {{
        put(Locale.ENGLISH, "PRESS ESCAPE TO RESUME");
        put(Locale.ITALY, "PREMI ESCAPE PER CONTINUARE");
    }};
    
    public static final HashMap<Locale, String> STARTING_LEVEL = new HashMap<>() {{
        put(Locale.ENGLISH, "STARTING %level%!");
        put(Locale.ITALY, "%level% INIZIATO!");
    }};

    public static final HashMap<Locale, String> ISLAND = new HashMap<>() {{
        put(Locale.ENGLISH, "ISLAND");
        put(Locale.ITALY, "ISOLA");
    }};

    public static final HashMap<Locale, String> YOU_DIED = new HashMap<>() {{
        put(Locale.ENGLISH, "YOU DIED! YOU HAVE %lives% LIVES LEFT.");
        put(Locale.ITALY, "SEI MORTO! HAI %lives% VITE RIMANENTI.");
    }};

    public static final HashMap<Locale, String> ARENA_DIED = new HashMap<>() {{
        put(Locale.ENGLISH, "YOU DIED! YOU SURVIVED %rounds% ROUNDS.");
        put(Locale.ITALY, "SEI MORTO! SEI SOPRAVVISSUTO %rounds% ROUND.");
    }};

    public static final HashMap<Locale, String> RESET_WORLD = new HashMap<>() {{
        put(Locale.ENGLISH, "RESET WORLD");
        put(Locale.ITALY, "RESETTA MONDO");
    }};

    public static final HashMap<Locale, String> LOADING = new HashMap<>() {{
        put(Locale.ENGLISH, "LOADING");
        put(Locale.ITALY, "CARICAMENTO");
    }};

    public static final HashMap<Locale, String> PLAY_AGAIN = new HashMap<>() {{
        put(Locale.ENGLISH, "PLAY AGAIN");
        put(Locale.ITALY, "GIOCA DI NUOVO");
    }};

    public static final HashMap<Locale, String> MAIN_MENU = new HashMap<>() {{
        put(Locale.ENGLISH, "MAIN MENU");
        put(Locale.ITALY, "MENU PRINCIPALE");
    }};

    public static final HashMap<Locale, String> PLAY = new HashMap<>() {{
        put(Locale.ENGLISH, "PLAY STORYMODE");
        put(Locale.ITALY, "GIOCA CAMPAGNA");
    }};

    public static final HashMap<Locale, String> START_ARENA = new HashMap<>() {{
        put(Locale.ENGLISH, "SELECT ARENA");
        put(Locale.ITALY, "GIOCA ARENA");
    }};

    public static final HashMap<Locale, String> PROFILE = new HashMap<>() {{
        put(Locale.ENGLISH, "SHOW PROFILE");
        put(Locale.ITALY, "VISUALIZZA PROFILO");
    }};

    public static final HashMap<Locale, String> QUIT = new HashMap<>() {{
        put(Locale.ENGLISH, "EXIT THE GAME");
        put(Locale.ITALY, "ESCI DAL GIOCO");
    }};

    public static final HashMap<Locale, String> RESUME_GAME = new HashMap<>() {{
        put(Locale.ENGLISH, "RESUME GAME");
        put(Locale.ITALY, "CONTINUA LA PARTITA");
    }};

    public static final HashMap<Locale, String> QUIT_GAME = new HashMap<>() {{
        put(Locale.ENGLISH, "QUIT THE MATCH");
        put(Locale.ITALY, "ESCI DALLA PARTITA");
    }};

    public static final HashMap<Locale, String> USERNAME = new HashMap<>() {{
        put(Locale.ENGLISH, "USERNAME");
        put(Locale.ITALY, "USERNAME");
    }};

    public static final HashMap<Locale, String> SETTINGS = new HashMap<>() {{
        put(Locale.ENGLISH, "SETTINGS");
        put(Locale.ITALY, "IMPOSTAZIONI");
    }};


    public static final HashMap<Locale, String> PLAYER = new HashMap<>() {{
        put(Locale.ENGLISH, "PLAYER");
        put(Locale.ITALY, "GIOCATORE");
    }};

    public static final HashMap<Locale, String> APP_NAME = new HashMap<>() {{
        put(Locale.ENGLISH, "JBOMBERMAN");
        put(Locale.ITALY, "JBOMBERMAN");
    }};

    public static final HashMap<Locale, String> STARTING_ROUND = new HashMap<>() {{
        put(Locale.ENGLISH, "STARTING ROUND %round%");
        put(Locale.ITALY, "INIZIA IL ROUND %round%");
    }};

    public static final HashMap<Locale, String> AUDIO_VOLUME = new HashMap<>() {{
        put(Locale.ENGLISH, "AUDIO");
        put(Locale.ITALY, "AUDIO");
    }};

    public static final HashMap<Locale, String> ARENA_WORLD_1 = new HashMap<>() {{
        put(Locale.ENGLISH, "ARENA WORLD 1");
        put(Locale.ITALY, "ARENA MONDO 1");
    }};

    public static final HashMap<Locale, String> ARENA_WORLD_2 = new HashMap<>() {{
        put(Locale.ENGLISH, "ARENA WORLD 2");
        put(Locale.ITALY, "ARENA MONDO 2");
    }};

    public static Locale[] getSupportedLanguages() {
        return new Locale[]{
                Locale.ENGLISH,
                Locale.ITALY
        };
    }

    public static String get(HashMap<Locale, String> localizationMap) {
        Optional<Locale> locale = Arrays.stream(getSupportedLanguages()).filter(l -> l.getLanguage().equals(Locale.getDefault().getLanguage())).findFirst();
        return localizationMap.get(locale.orElse(Locale.ENGLISH));
    }
}
