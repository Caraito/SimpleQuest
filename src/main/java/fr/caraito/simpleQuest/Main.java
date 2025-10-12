package fr.caraito.simpleQuest;

import fr.caraito.simpleQuest.commands.Quest;
import fr.caraito.simpleQuest.commands.QuestAdmin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Main instance;

    @Override
    public void onEnable() {

        instance = this;

        getLogger().info("SimpleQuest activated");
        getLogger().info("Version " + getDescription().getVersion());
        getLogger().info("Plugin by Caraito" );
        saveDefaultConfig();

        getCommand("quest").setExecutor(new Quest(this));
        getCommand("adminquest").setExecutor(new QuestAdmin(this));

        getServer().getPluginManager().registerEvents(new Quest(this), this);

    }

    @Override
    public void onDisable() {
        getLogger().info("SimpleQuest deactivated");
        getLogger().info("Thanks for using SimpleQuest !");
    }

    public static Main getInstance() {
        return instance;
    }

}
