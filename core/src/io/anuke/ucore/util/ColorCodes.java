package io.anuke.ucore.util;

/* renamed from: io.anuke.ucore.util.ColorCodes */
public class ColorCodes {
    public static String BACK_BLUE;
    public static String BACK_DEFAULT;
    public static String BACK_GREEN = "\u001b[42m";
    public static String BACK_RED;
    public static String BACK_YELLOW;
    public static String BLACK;
    public static String BLUE;
    public static String BOLD;
    public static String CYAN;
    public static String FLUSH;
    public static String GREEN;
    public static String LIGHT_BLUE;
    public static String LIGHT_CYAN;
    public static String LIGHT_GREEN;
    public static String LIGHT_MAGENTA;
    public static String LIGHT_RED;
    public static String LIGHT_YELLOW;
    public static String PURPLE;
    public static String RED;
    public static String RESET;
    public static String UNDERLINED;
    public static String WHITE;
    public static String YELLOW;

    static {
        FLUSH = "\u001b[H\u001b[2J";
        RESET = "\u001b[0m";
        BOLD = "\u001b[1m";
        UNDERLINED = "\u001b[4m";
        BLACK = "\u001b[30m";
        RED = "\u001b[31m";
        GREEN = "\u001b[32m";
        YELLOW = "\u001b[33m";
        BLUE = "\u001b[34m";
        PURPLE = "\u001b[35m";
        CYAN = "\u001b[36m";
        LIGHT_RED = "\u001b[91m";
        LIGHT_GREEN = "\u001b[92m";
        LIGHT_YELLOW = "\u001b[93m";
        LIGHT_BLUE = "\u001b[94m";
        LIGHT_MAGENTA = "\u001b[95m";
        LIGHT_CYAN = "\u001b[96m";
        WHITE = "\u001b[37m";
        BACK_DEFAULT = "\u001b[49m";
        BACK_RED = "\u001b[41m";
        BACK_YELLOW = "\u001b[43m";
        BACK_BLUE = "\u001b[44m";
        if (System.getProperty("os.name").startsWith("Windows")) {
            BACK_BLUE = "";
            BACK_YELLOW = "";
            BACK_RED = "";
            BACK_DEFAULT = "";
            WHITE = "";
            LIGHT_CYAN = "";
            LIGHT_MAGENTA = "";
            LIGHT_BLUE = "";
            LIGHT_YELLOW = "";
            LIGHT_GREEN = "";
            LIGHT_RED = "";
            CYAN = "";
            PURPLE = "";
            BLUE = "";
            YELLOW = "";
            GREEN = "";
            RED = "";
            BLACK = "";
            UNDERLINED = "";
            BOLD = "";
            RESET = "";
            FLUSH = "";
        }
    }
}
