package fr.caraito.simpleQuest.commands;

import fr.caraito.simpleQuest.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class Quest implements CommandExecutor, Listener {
    public Quest(Main main) {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        FileConfiguration config = Main.getInstance().getConfig();

        // Commandes joueur
        if (args.length == 0) {

            player.sendMessage("[§aSimple §eQuest§r] §7- §eQuest commands: ");
            player.sendMessage("[§aSimple §eQuest§r] §7- §a--> §b/quest info");
            player.sendMessage("[§aSimple §eQuest§r] §7- §a--> §b/quest info §7<§dID§7>");
            player.sendMessage("[§aSimple §eQuest§r] §7- §a--> §b/quest deposite §7<§dID§7>");
            return true;

        }

        // /quete info : liste toutes les quêtes
        if (args[0].equalsIgnoreCase("info") && args.length == 1) {
            if (config.getConfigurationSection("Quetes") == null || config.getConfigurationSection("Quetes").getKeys(false).isEmpty()) {
                player.sendMessage("[§aSimple §eQuest§r] §7- §cNo quests available.");
                return true;
            }
            player.sendMessage("[§aSimple §eQuest§r] §7- §eQuest list:");
            for (String id : config.getConfigurationSection("Quetes").getKeys(false)) {
                // Affiche le matériel/mob au lieu du type
                String materiel = config.getString("Quetes." + id + ".materiel");
                player.sendMessage("[§aSimple §eQuest§r] §7- §bID: §e" + id + " §7| §aTarget: §d" + materiel);
            }
            return true;
        }

        // /quete info <ID> : détails de la quête
        if (args[0].equalsIgnoreCase("info") && args.length == 2) {
            String id = args[1];
            String path = "Quetes." + id;
            if (!config.contains(path)) {
                player.sendMessage("[§aSimple §eQuest§r] §7- §cQuest not found.");
                return true;
            }
            String type = config.getString(path + ".type");
            String materiel = config.getString(path + ".materiel");
            int quantite = config.getInt(path + ".quantite");
            String recompense = config.getString(path + ".recompense");
            int quantiteRecompense = config.getInt(path + ".quantiteRecompense");

            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("[§aSimple §eQuest§r] §7- §eQuest details:");
            player.sendMessage("");

            player.sendMessage("[§aSimple §eQuest§r] §7- §eQuest §b" + id + " :");
            player.sendMessage("[§aSimple §eQuest§r] §7- §aType: §6" + type);
            player.sendMessage("[§aSimple §eQuest§r] §7- §aMaterial/Entity: §d" + materiel);
            player.sendMessage("[§aSimple §eQuest§r] §7- §aAmount: §e" + quantite);
            player.sendMessage("[§aSimple §eQuest§r] §7- §aReward: §b" + recompense + " §ex" + quantiteRecompense);
            player.sendMessage("[§aSimple §eQuest§r] §7- §eStatus: " + (config.getBoolean("QueteDone." + player.getName() + "." + id, false) ? "§aCompleted" : "§cNot completed"));
            return true;
        }

        // /quete deposite <ID> : dépôt d'objet pour une quête de type deposit
        if (args[0].equalsIgnoreCase("deposite") && args.length == 2) {
            String id = args[1];
            String path = "Quetes." + id;
            if (!config.contains(path)) {
                player.sendMessage("[§aSimple §eQuest§r] §7- §cQuest not found.");
                return true;
            }
            String type = config.getString(path + ".type");
            if (!type.equals("deposit")) {
                player.sendMessage("[§aSimple §eQuest§r] §7- §cThis quest is not a deposit quest.");
                return true;
            }
            // Vérifier si déjà faite
            if (config.getBoolean("QueteDone." + player.getName() + "." + id, false)) {
                player.sendMessage("[§aSimple §eQuest§r] §7- §eYou have already completed this quest.");
                return true;
            }
            String materiel = config.getString(path + ".materiel");
            int quantite = config.getInt(path + ".quantite");
            Material mat = Material.getMaterial(materiel);
            if (mat == null) {
                player.sendMessage("[§aSimple §eQuest§r] §7- §cInvalid material.");
                return true;
            }
            int count = 0;
            for (int i = 0; i < player.getInventory().getSize(); i++) {
                if (player.getInventory().getItem(i) != null && player.getInventory().getItem(i).getType() == mat) {
                    count += player.getInventory().getItem(i).getAmount();
                }
            }
            if (count < quantite) {
                player.sendMessage("[§aSimple §eQuest§r] §7- §cYou do not have enough §e" + materiel + " §cto complete the quest.");
                return true;
            }
            // Retirer les items
            int toRemove = quantite;
            for (int i = 0; i < player.getInventory().getSize(); i++) {
                if (player.getInventory().getItem(i) != null && player.getInventory().getItem(i).getType() == mat) {
                    int stackAmount = player.getInventory().getItem(i).getAmount();
                    if (stackAmount <= toRemove) {
                        player.getInventory().setItem(i, null);
                        toRemove -= stackAmount;
                    } else {
                        player.getInventory().getItem(i).setAmount(stackAmount - toRemove);
                        toRemove = 0;
                    }
                    if (toRemove == 0) break;
                }
            }
            player.sendMessage("[§aSimple §eQuest§r] §7- §aYou have completed the deposit quest!");
            // Récompense à donner
            String recompense = config.getString(path + ".recompense");
            int quantiteRecompense = config.getInt(path + ".quantiteRecompense");
            Material rewardMat = Material.getMaterial(recompense);
            if (rewardMat != null) {
                org.bukkit.inventory.ItemStack reward = new org.bukkit.inventory.ItemStack(rewardMat, quantiteRecompense);
                player.getInventory().addItem(reward);
                player.sendMessage("[§aSimple §eQuest§r] §7- §aYou received §e" + quantiteRecompense + " §b" + recompense + "§a!");
            }
            // Marquer la quête comme faite pour ce joueur
            config.set("QueteDone." + player.getName() + "." + id, true);
            Main.getInstance().saveConfig();
            return true;
        }
        player.sendMessage("[§aSimple §eQuest§r] §7- §cInvalid command. Use §e/quest §cfor help.");
        return false;
    }

    // Gestion des quêtes kill
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;
        Player player = event.getEntity().getKiller();
        FileConfiguration config = Main.getInstance().getConfig();
        if (config.getConfigurationSection("Quetes") == null) return;

        for (String id : config.getConfigurationSection("Quetes").getKeys(false)) {
            String path = "Quetes." + id;
            String type = config.getString(path + ".type");
            if (!"kill".equals(type)) continue;
            // Vérifier si déjà faite
            if (config.getBoolean("QueteDone." + player.getName() + "." + id, false)) continue;
            String materiel = config.getString(path + ".materiel");
            int quantite = config.getInt(path + ".quantite");
            String recompense = config.getString(path + ".recompense");
            int quantiteRecompense = config.getInt(path + ".quantiteRecompense");

            if (event.getEntityType().name().equalsIgnoreCase(materiel)) {
                String progressPath = "QueteProgress." + player.getName() + "." + id;
                int progress = config.getInt(progressPath, 0) + 1;
                config.set(progressPath, progress);
                Main.getInstance().saveConfig();

                if (progress >= quantite) {
                    config.set(progressPath, null); // Reset progress
                    Material rewardMat = Material.getMaterial(recompense);
                    if (rewardMat != null) {
                        player.getInventory().addItem(new ItemStack(rewardMat, quantiteRecompense));
                        player.sendMessage("[§aSimple §eQuest§r] §7- §aKill quest completed! §aReward: §e" + quantiteRecompense + " §b" + recompense);
                    }
                    // Marquer la quête comme faite pour ce joueur
                    config.set("QueteDone." + player.getName() + "." + id, true);
                    Main.getInstance().saveConfig();
                } else {
                    // Remplace le message de progression par une action bar
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§eKill quest [§b" + id + "§e]: §a" + progress + "/" + quantite));
                }
            }
        }
    }

    // Gestion des quêtes break
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = Main.getInstance().getConfig();
        if (config.getConfigurationSection("Quetes") == null) return;

        for (String id : config.getConfigurationSection("Quetes").getKeys(false)) {
            String path = "Quetes." + id;
            String type = config.getString(path + ".type");
            if (!"break".equals(type)) continue;
            // Vérifier si déjà faite
            if (config.getBoolean("QueteDone." + player.getName() + "." + id, false)) continue;
            String materiel = config.getString(path + ".materiel");
            int quantite = config.getInt(path + ".quantite");
            String recompense = config.getString(path + ".recompense");
            int quantiteRecompense = config.getInt(path + ".quantiteRecompense");

            if (event.getBlock().getType().name().equalsIgnoreCase(materiel)) {
                String progressPath = "QueteProgress." + player.getName() + "." + id;
                int progress = config.getInt(progressPath, 0) + 1;
                config.set(progressPath, progress);
                Main.getInstance().saveConfig();

                if (progress >= quantite) {
                    config.set(progressPath, null); // Reset progress
                    Material rewardMat = Material.getMaterial(recompense);
                    if (rewardMat != null) {
                        player.getInventory().addItem(new ItemStack(rewardMat, quantiteRecompense));
                        player.sendMessage("[§aSimple §eQuest§r] §7- §aBreak quest completed! §aReward: §e" + quantiteRecompense + " §b" + recompense);
                    }
                    // Marquer la quête comme faite pour ce joueur
                    config.set("QueteDone." + player.getName() + "." + id, true);
                    Main.getInstance().saveConfig();
                } else {
                    // Remplace le message de progression par une action bar
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§eBreak quest [§b" + id + "§e]: §a" + progress + "/" + quantite));
                }
            }
        }
    }

}
