package fr.caraito.simpleQuest.commands;

import fr.caraito.simpleQuest.Main;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class QuestAdmin implements CommandExecutor {
    public QuestAdmin(Main main) {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        FileConfiguration config = Main.getInstance().getConfig();

        if (player.hasPermission("simplequest.admin")) {
            if (args.length == 0) {

                player.sendMessage("[§aSimple §eQuest§r] §7- §eAdmin commands : ");
                player.sendMessage("[§aSimple §eQuest§r] §7- §a--> §b/quest setup §7<§6kill§7 // §6break§7 // §6deposit§7> <§dmaterial§7> <§damount§7> <§dreward§7> <§damount§7> <§dquest ID§7>");
                player.sendMessage("[§aSimple §eQuest§r] §7- §a--> §b/quest remove §7<§dquest ID§7>");
                return true;

            }

            // Commande admin : setup
            if (args[0].equalsIgnoreCase("setup") && args.length == 7) {
                String type = args[1].toLowerCase(); // kill, break, deposit
                String materiel = args[2].toUpperCase();
                int quantite;
                String recompense = args[4].toUpperCase();
                int quantiteRecompense;
                String id = args[6];

                try {
                    quantite = Integer.parseInt(args[3]);
                    quantiteRecompense = Integer.parseInt(args[5]);
                } catch (NumberFormatException e) {
                    player.sendMessage("[§aSimple §eQuest§r] §7- §cInvalid amount or reward amount.");
                    return true;
                }

                // Vérification stricte du matériel et de la récompense
                boolean materielValide = false;
                boolean recompenseValide = false;
                if (type.equals("break") || type.equals("deposit")) {
                    materielValide = Material.getMaterial(materiel) != null;
                } else if (type.equals("kill")) {
                    try {
                        materielValide = EntityType.valueOf(materiel) != null;
                    } catch (IllegalArgumentException ex) {
                        materielValide = false;
                    }
                }
                recompenseValide = Material.getMaterial(recompense) != null;
                if (!materielValide) {
                    player.sendMessage("[§aSimple §eQuest§r] §7- §cInvalid material or entity for this quest type.");
                    return true;
                }
                if (!recompenseValide) {
                    player.sendMessage("[§aSimple §eQuest§r] §7- §cInvalid reward, it must be a recognized Minecraft item.");
                    return true;
                }

                // Vérification de l'unicité de l'ID
                String path = "Quetes." + id;
                if (config.contains(path)) {
                    // Chercher le prochain ID disponible
                    int maxId = 0;
                    for (String existingId : config.getConfigurationSection("Quetes").getKeys(false)) {
                        try {
                            int val = Integer.parseInt(existingId);
                            if (val > maxId) maxId = val;
                        } catch (NumberFormatException ignored) {}
                    }
                    int nextId = maxId + 1;
                    player.sendMessage("[§aSimple §eQuest§r] §7- §cThe ID §e" + id + " §calready exists. §aNext available ID: §e" + nextId);
                    return true;
                }

                // Sauvegarde dans la config
                config.set(path + ".type", type);
                config.set(path + ".materiel", materiel);
                config.set(path + ".quantite", quantite);
                config.set(path + ".recompense", recompense);
                config.set(path + ".quantiteRecompense", quantiteRecompense);
                Main.getInstance().saveConfig();

                player.sendMessage("[§aSimple §eQuest§r] §7- §aQuest successfully added!");
                return true;
            }

            // Commande admin : remove
            if (args[0].equalsIgnoreCase("remove") && args.length == 2) {
                String id = args[1];
                String path = "Quetes." + id;
                if (config.contains(path)) {
                    config.set(path, null);
                    // Supprimer la progression et la complétion pour tous les joueurs
                    if (config.getConfigurationSection("QueteProgress") != null) {
                        for (String playerName : config.getConfigurationSection("QueteProgress").getKeys(false)) {
                            config.set("QueteProgress." + playerName + "." + id, null);
                        }
                    }
                    if (config.getConfigurationSection("QueteDone") != null) {
                        for (String playerName : config.getConfigurationSection("QueteDone").getKeys(false)) {
                            config.set("QueteDone." + playerName + "." + id, null);
                        }
                    }
                    Main.getInstance().saveConfig();
                    player.sendMessage("[§aSimple §eQuest§r] §7- §aQuest removed!");
                } else {
                    player.sendMessage("[§aSimple §eQuest§r] §7- §cQuest ID not found.");
                }
                return true;
            }

            player.sendMessage("[§aSimple §eQuest§r] §7- §cInvalid command usage.");
            return true;

        } else {

            player.sendMessage("[§aSimple §eQuest§r] §7- §cYou do not have permission to execute this command.");
            return true;

            }

    }
}
