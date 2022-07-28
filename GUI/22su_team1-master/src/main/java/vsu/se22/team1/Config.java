package vsu.se22.team1;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Utility class for reading the program's configuration file.
 */
public final class Config {
    /**
     * The configuration file is located in the user's home directory.
     * <br>
     * On Windows, this is <code>C:\Users\[name]\.config\vsu-se22-team1</code>
     * <br>
     * On Linux, this is <code>~/.config/vsu-se22-team1</code>
     */
    private static final File CONFIG_DIR = new File(
            System.getProperty("user.home") + File.separator + ".config" + File.separator + "vsu-se22-team1");
    public static final File CONFIG_FILE = new File(CONFIG_DIR, "config.properties");
    /**
     * Hashed passwords are stored in the config directory for the sake of simplicity.
     * In a real application, these would likely be stored in a database on a separate machine.
     */
    private static final File PASSWORD_FILE = new File(CONFIG_DIR, ".passwd");
    private static final Map<String, String> CONFIG = new HashMap<>();

    static {
        CONFIG_DIR.mkdirs();
        if(!CONFIG_FILE.exists()) {
            try {
                saveDefaultConfig();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        try {
            loadConfig();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return True if the given username is associated with the given hashed password, otherwise false.
     */
    public static boolean checkPassword(String username, String password) throws IOException {
        if(!PASSWORD_FILE.exists()) {
            return false;
        }
        List<String> lines = Files.readAllLines(PASSWORD_FILE.toPath());
        for(String line : lines) {
            if(line.isEmpty()) continue;
            String[] split = line.split(":", 2);
            if(split[0].equals(username)) {
                return split[1].equals(password);
            }
        }
        return false;
    }

    public static void writePassword(String username, String password) throws IOException {
        if(!PASSWORD_FILE.exists()) {
            PASSWORD_FILE.createNewFile();
        }
        Files.write(PASSWORD_FILE.toPath(), String.format("%s:%s\n", username, password).getBytes(),
                StandardOpenOption.APPEND);
    }

    public static boolean userExists(String username) throws IOException {
        if(!PASSWORD_FILE.exists()) {
            return false;
        }
        List<String> lines = Files.readAllLines(PASSWORD_FILE.toPath());
        for(String line : lines) {
            if(line.isEmpty()) continue;
            if(line.split(":", 2)[0].equals(username)) {
                return true;
            }
        }
        return false;
    }

    private static void saveDefaultConfig() throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(Config.class.getResourceAsStream("/config.properties")),
                        StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        reader.close();
        Files.write(CONFIG_FILE.toPath(), sb.toString().getBytes());
    }

    private static void loadConfig() throws IOException {
        List<String> lines = Files.readAllLines(CONFIG_FILE.toPath());
        for(String line : lines) {
            if(line.isEmpty() || line.startsWith("#")) continue;
            String[] split = line.split("=", 2);
            CONFIG.put(split[0], split[1]);
        }
    }

    public static void saveConfig() throws IOException {
        StringBuilder sb = new StringBuilder();
        for(String key : CONFIG.keySet()) {
            sb.append(key);
            sb.append("=");
            sb.append(get(key));
            sb.append("\n");
        }
        Files.write(CONFIG_FILE.toPath(), sb.toString().getBytes());
    }

    public static String get(String key) {
        return CONFIG.get(key);
    }

    public static String put(String key, String value) {
        return CONFIG.put(key, value);
    }
}
