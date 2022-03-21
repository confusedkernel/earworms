package nottyl.earwormsbot;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {
    private static final Dotenv dotenv = Dotenv.load();

    /**
     * Read from dotenv file
     * @param key a query of type string
     * @return the variable requested
     */

    public static String get(String key) {
        return dotenv.get(key.toUpperCase());
    }
}
