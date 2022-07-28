package vsu.se22.team1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Rather than hard-coding strings used in the GUI, they are stored in
 * separate files and loaded at runtime. This makes it easier to localize
 * the application.
 */
public final class I18n {
    /**
     * This string is displayed when the application encounters a translation
     * key with no value.
     */
    public static final String MISSING_TRANSLATION = "[{?}]";
    private static final String FALLBACK_LOCALE = "en-US";
    private static final HashMap<String, HashMap<String, String>> TRANSLATIONS = new HashMap<>();
    private static String currentLocale;

    static {
        try {
            loadTranslations();
        } catch(URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        setCurrentLocale(Locale.getDefault().toLanguageTag());
    }

    /**
     * Loads the translation files from the 'i18n' directory within the JAR.
     */
    private static void loadTranslations() throws IOException, URISyntaxException {
        final URI uri = I18n.class.getResource("/i18n").toURI();
        Map<String, String> env = new HashMap<>();
        env.put("create", "true");
        FileSystems.newFileSystem(uri, env);
        for(Iterator<Path> it = Files.walk(Paths.get(uri), 1).iterator(); it.hasNext();) {
            Path path = it.next();
            String locale = path.toString();
            if(locale.equals("/i18n")) {
                continue;
            }
            loadTranslation(locale.substring(6, locale.length() - 11), path);
        }
    }

    /**
     * Load all translations (key=value) for the given locale from the given path.
     * <br>
     * Blank lines and lines starting with # are ignored.
     */
    private static void loadTranslation(String locale, Path path) throws IOException {
        if(!TRANSLATIONS.containsKey(locale)) {
            TRANSLATIONS.put(locale, new HashMap<>());
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(I18n.class.getResourceAsStream(path.toString())), StandardCharsets.UTF_8));
        String line;
        while((line = reader.readLine()) != null) {
            if(line.length() == 0 || line.startsWith("#")) {
                continue;
            }
            String[] split = line.split("=", 2);
            TRANSLATIONS.get(locale).put(split[0], split[1]);
        }
        reader.close();
    }

    private static String translate(String locale, String key) {
        if(!TRANSLATIONS.containsKey(locale)) {
            System.err.println("Missing locale: " + locale + " (key: " + key + ")");
            return MISSING_TRANSLATION;
        }
        if(TRANSLATIONS.get(locale).containsKey(key)) {
            return TRANSLATIONS.get(locale).get(key);
        } else {
            System.err.printf("Missing translation (%s): %s%n", locale, key);
            // Attempt to fall back to en-US when the application encounters a missing key.
            return TRANSLATIONS.get(FALLBACK_LOCALE).getOrDefault(key, MISSING_TRANSLATION);
        }
    }

    public static String get(String key) {
        return translate(currentLocale, key);
    }

    public static String get(String key, Object... args) {
        return String.format(translate(currentLocale, key), args);
    }

    public static void useSystemLocale() {
        setCurrentLocale(Locale.getDefault().toLanguageTag());
    }

    public static void setCurrentLocale(String newLocale) {
        if(TRANSLATIONS.get(newLocale) == null) {
            System.err.println("Missing locale: " + newLocale);
            currentLocale = FALLBACK_LOCALE;
        } else {
            currentLocale = newLocale;
        }
        if(Main.managerFrame != null) Main.managerFrame.updateStrings();
    }

    public static Set<String> getSupportedLocales() {
        return TRANSLATIONS.keySet();
    }

    public static String getCurrentLocale() {
        return currentLocale;
    }

    public static String getLocalName(String locale) {
        return translate(locale, "language.local-name");
    }
}
