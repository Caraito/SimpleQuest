package fr.caraito.simpleQuest.commands;

import fr.caraito.simpleQuest.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class Quest implements CommandExecutor, Listener {
    private final Main main;

    public Quest(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(main.tr("command.player-only"));
            return true;
        }
        FileConfiguration config = main.getConfig();

        // Commandes joueur
        if (args.length == 0) {

            player.sendMessage(main.tr("quest.help.title"));
            player.sendMessage(main.tr("quest.help.info"));
            player.sendMessage(main.tr("quest.help.info-id"));
            player.sendMessage(main.tr("quest.help.deposit"));
            return true;

        }

        // /quete info : liste toutes les quêtes
        if (args[0].equalsIgnoreCase("info") && args.length == 1) {
            ConfigurationSection questsSection = config.getConfigurationSection("Quetes");
            if (questsSection == null || questsSection.getKeys(false).isEmpty()) {
                player.sendMessage(main.tr("quest.no-quests"));
                return true;
            }
            player.sendMessage(main.tr("quest.list.title"));
            for (String id : questsSection.getKeys(false)) {
                String materiel = config.getString("Quetes." + id + ".materiel", "?");
                player.sendMessage(main.tr("quest.list.entry", id, materiel));
            }
            return true;
        }

        // /quete info <ID> : détails de la quête
        if (args[0].equalsIgnoreCase("info") && args.length == 2) {
            String id = args[1];
            String path = "Quetes." + id;
            if (!config.contains(path)) {
                player.sendMessage(main.tr("quest.not-found"));
                return true;
            }
            String type = config.getString(path + ".type", "unknown");
            String materiel = config.getString(path + ".materiel", "?");
            int quantite = config.getInt(path + ".quantite");
            String recompense = config.getString(path + ".recompense", "?");
            int quantiteRecompense = config.getInt(path + ".quantiteRecompense");

            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage(main.tr("quest.details.title"));
            player.sendMessage("");

            player.sendMessage(main.tr("quest.details.header", id));
            player.sendMessage(main.tr("quest.details.type", type));
            player.sendMessage(main.tr("quest.details.target", materiel));
            player.sendMessage(main.tr("quest.details.amount", quantite));
            player.sendMessage(main.tr("quest.details.reward", recompense, quantiteRecompense));
            player.sendMessage(main.tr(
                    "quest.details.status",
                    main.tr(config.getBoolean("QueteDone." + player.getName() + "." + id, false)
                            ? "quest.status.completed"
                            : "quest.status.not-completed")
            ));
            return true;
        }

        // /quete deposite <ID> : dépôt d'objet pour une quête de type deposit
        if ((args[0].equalsIgnoreCase("deposite") || args[0].equalsIgnoreCase("deposit")) && args.length == 2) {
            String id = args[1];
            String path = "Quetes." + id;
            if (!config.contains(path)) {
                player.sendMessage(main.tr("quest.not-found"));
                return true;
            }
            String type = config.getString(path + ".type", "");
            if (!"deposit".equalsIgnoreCase(type)) {
                player.sendMessage(main.tr("quest.not-deposit"));
                return true;
            }
            // Vérifier si déjà faite
            if (config.getBoolean("QueteDone." + player.getName() + "." + id, false)) {
                player.sendMessage(main.tr("quest.already-completed"));
                return true;
            }
            String materiel = config.getString(path + ".materiel", "");
            int quantite = config.getInt(path + ".quantite");
            Material mat = Material.getMaterial(materiel);
            if (mat == null) {
                player.sendMessage(main.tr("quest.invalid-material"));
                return true;
            }
            int count = 0;
            for (int i = 0; i < player.getInventory().getSize(); i++) {
                ItemStack currentItem = player.getInventory().getItem(i);
                if (currentItem != null && currentItem.getType() == mat) {
                    count += currentItem.getAmount();
                }
            }
            if (count < quantite) {
                player.sendMessage(main.tr("quest.not-enough", materiel));
                return true;
            }
            // Retirer les items
            int toRemove = quantite;
            for (int i = 0; i < player.getInventory().getSize(); i++) {
                ItemStack currentItem = player.getInventory().getItem(i);
                if (currentItem != null && currentItem.getType() == mat) {
                    int stackAmount = currentItem.getAmount();
                    if (stackAmount <= toRemove) {
                        player.getInventory().setItem(i, null);
                        toRemove -= stackAmount;
                    } else {
                        currentItem.setAmount(stackAmount - toRemove);
                        toRemove = 0;
                    }
                    if (toRemove == 0) break;
                }
            }
            player.sendMessage(main.tr("quest.deposit.completed"));
            // Récompense à donner
            String recompense = config.getString(path + ".recompense", "");
            int quantiteRecompense = config.getInt(path + ".quantiteRecompense");
            Material rewardMat = Material.getMaterial(recompense);
            if (rewardMat != null) {
                org.bukkit.inventory.ItemStack reward = new org.bukkit.inventory.ItemStack(rewardMat, quantiteRecompense);
                player.getInventory().addItem(reward);
                player.sendMessage(main.tr("quest.reward.received", quantiteRecompense, recompense));
            }
            // Marquer la quête comme faite pour ce joueur
            config.set("QueteDone." + player.getName() + "." + id, true);
            main.saveConfig();
            return true;
        }
        player.sendMessage(main.tr("quest.invalid-command"));
        return true;
    }

    // Gestion des quêtes kill
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;
        Player player = event.getEntity().getKiller();
        FileConfiguration config = main.getConfig();
        ConfigurationSection questsSection = config.getConfigurationSection("Quetes");
        if (questsSection == null) return;

        for (String id : questsSection.getKeys(false)) {
            String path = "Quetes." + id;
            String type = config.getString(path + ".type", "");
            if (!"kill".equals(type)) continue;
            // Vérifier si déjà faite
            if (config.getBoolean("QueteDone." + player.getName() + "." + id, false)) continue;
            String materiel = config.getString(path + ".materiel", "");
            int quantite = config.getInt(path + ".quantite");
            String recompense = config.getString(path + ".recompense", "");
            int quantiteRecompense = config.getInt(path + ".quantiteRecompense");

            if (event.getEntityType().name().equalsIgnoreCase(materiel)) {
                String progressPath = "QueteProgress." + player.getName() + "." + id;
                int progress = config.getInt(progressPath, 0) + 1;
                config.set(progressPath, progress);
                main.saveConfig();

                if (progress >= quantite) {
                    config.set(progressPath, null); // Reset progress
                    Material rewardMat = Material.getMaterial(recompense);
                    if (rewardMat != null) {
                        player.getInventory().addItem(new ItemStack(rewardMat, quantiteRecompense));
                        player.sendMessage(main.tr("quest.kill.completed", quantiteRecompense, recompense));
                    }
                    // Marquer la quête comme faite pour ce joueur
                    config.set("QueteDone." + player.getName() + "." + id, true);
                    main.saveConfig();
                } else {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(main.tr("quest.kill.progress", id, progress, quantite)));
                }
            }
        }
    }

    // Gestion des quêtes break
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = main.getConfig();
        ConfigurationSection questsSection = config.getConfigurationSection("Quetes");
        if (questsSection == null) return;

        for (String id : questsSection.getKeys(false)) {
            String path = "Quetes." + id;
            String type = config.getString(path + ".type", "");
            if (!"break".equals(type)) continue;
            // Vérifier si déjà faite
            if (config.getBoolean("QueteDone." + player.getName() + "." + id, false)) continue;
            String materiel = config.getString(path + ".materiel", "");
            int quantite = config.getInt(path + ".quantite");
            String recompense = config.getString(path + ".recompense", "");
            int quantiteRecompense = config.getInt(path + ".quantiteRecompense");

            if (event.getBlock().getType().name().equalsIgnoreCase(materiel)) {
                String progressPath = "QueteProgress." + player.getName() + "." + id;
                int progress = config.getInt(progressPath, 0) + 1;
                config.set(progressPath, progress);
                main.saveConfig();

                if (progress >= quantite) {
                    config.set(progressPath, null); // Reset progress
                    Material rewardMat = Material.getMaterial(recompense);
                    if (rewardMat != null) {
                        player.getInventory().addItem(new ItemStack(rewardMat, quantiteRecompense));
                        player.sendMessage(main.tr("quest.break.completed", quantiteRecompense, recompense));
                    }
                    // Marquer la quête comme faite pour ce joueur
                    config.set("QueteDone." + player.getName() + "." + id, true);
                    main.saveConfig();
                } else {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(main.tr("quest.break.progress", id, progress, quantite)));
                }
            }
        }
    }

}
