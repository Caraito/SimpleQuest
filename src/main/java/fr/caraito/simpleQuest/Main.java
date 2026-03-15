package fr.caraito.simpleQuest;

import fr.caraito.simpleQuest.commands.AdminQuestConfig;
import fr.caraito.simpleQuest.commands.Quest;
import fr.caraito.simpleQuest.commands.QuestAdmin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;
import java.util.Objects;

public final class Main extends JavaPlugin {

    public static Main instance;
    private static final String DEFAULT_LANGUAGE = "fr";

    @Override
    public void onEnable() {

        instance = this;

        saveDefaultConfig();
        ensureLanguageConfig();

        Quest questCommand = new Quest(this);
        QuestAdmin questAdminCommand = new QuestAdmin(this);
        AdminQuestConfig adminQuestConfig = new AdminQuestConfig(this);

        getLogger().info(tr("log.enabled"));
        getLogger().info(tr("log.version", getDescription().getVersion()));
        getLogger().info(tr("log.author"));

        Objects.requireNonNull(getCommand("quest")).setExecutor(questCommand);
        Objects.requireNonNull(getCommand("adminquest")).setExecutor(questAdminCommand);
        Objects.requireNonNull(getCommand("adminquestconfig")).setExecutor(adminQuestConfig);

        getServer().getPluginManager().registerEvents(questCommand, this);
        getServer().getPluginManager().registerEvents(adminQuestConfig, this);

    }

    @Override
    public void onDisable() {
        getLogger().info(tr("log.disabled"));
        getLogger().info(tr("log.thanks"));
    }

    public static Main getInstance() {
        return instance;
    }

    public String getLanguage() {
        String language = getConfig().getString("language", DEFAULT_LANGUAGE);

        language = language.toLowerCase(Locale.ROOT);
        if (!language.equals("fr") && !language.equals("en")) {
            return DEFAULT_LANGUAGE;
        }

        return language;
    }

    public void setLanguage(String language) {
        String normalizedLanguage = language == null ? DEFAULT_LANGUAGE : language.toLowerCase(Locale.ROOT);
        if (!normalizedLanguage.equals("fr") && !normalizedLanguage.equals("en")) {
            normalizedLanguage = DEFAULT_LANGUAGE;
        }

        getConfig().set("language", normalizedLanguage);
        saveConfig();
    }

    public String tr(String key, Object... args) {
        return Messages.get(getLanguage(), key, args);
    }

    public String trForLanguage(String language, String key, Object... args) {
        return Messages.get(language, key, args);
    }

    private void ensureLanguageConfig() {
        String configuredLanguage = getConfig().getString("language");
        if (configuredLanguage == null) {
            setLanguage(DEFAULT_LANGUAGE);
            return;
        }

        String normalizedLanguage = configuredLanguage.toLowerCase(Locale.ROOT);
        if (!normalizedLanguage.equals("fr") && !normalizedLanguage.equals("en")) {
            setLanguage(DEFAULT_LANGUAGE);
        }
    }

}
