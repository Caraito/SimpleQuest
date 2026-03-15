package fr.caraito.simpleQuest;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class Messages {

    private static final String PREFIX = "[§aSimple §eQuest§r] §7- ";
    private static final Map<String, String> FR = new HashMap<>();
    private static final Map<String, String> EN = new HashMap<>();

    static {
        FR.put("log.enabled", "SimpleQuest activé");
        FR.put("log.version", "Version %s");
        FR.put("log.author", "Plugin par Caraito");
        FR.put("log.disabled", "SimpleQuest désactivé");
        FR.put("log.thanks", "Merci d'utiliser SimpleQuest !");

        FR.put("command.player-only", PREFIX + "§cCette commande doit être utilisée par un joueur.");

        FR.put("quest.help.title", PREFIX + "§eCommandes de quête :");
        FR.put("quest.help.info", PREFIX + "§a--> §b/quest info");
        FR.put("quest.help.info-id", PREFIX + "§a--> §b/quest info §7<§dID§7>");
        FR.put("quest.help.deposit", PREFIX + "§a--> §b/quest deposite §7<§dID§7>");
        FR.put("quest.no-quests", PREFIX + "§cAucune quête disponible.");
        FR.put("quest.list.title", PREFIX + "§eListe des quêtes :");
        FR.put("quest.list.entry", PREFIX + "§bID : §e%s §7| §aCible : §d%s");
        FR.put("quest.not-found", PREFIX + "§cQuête introuvable.");
        FR.put("quest.details.title", PREFIX + "§eDétails de la quête :");
        FR.put("quest.details.header", PREFIX + "§eQuête §b%s §e:");
        FR.put("quest.details.type", PREFIX + "§aType : §6%s");
        FR.put("quest.details.target", PREFIX + "§aMatériau/Entité : §d%s");
        FR.put("quest.details.amount", PREFIX + "§aQuantité : §e%d");
        FR.put("quest.details.reward", PREFIX + "§aRécompense : §b%s §ex%d");
        FR.put("quest.details.status", PREFIX + "§eStatut : %s");
        FR.put("quest.status.completed", "§aTerminée");
        FR.put("quest.status.not-completed", "§cNon terminée");
        FR.put("quest.not-deposit", PREFIX + "§cCette quête n'est pas une quête de dépôt.");
        FR.put("quest.already-completed", PREFIX + "§eVous avez déjà terminé cette quête.");
        FR.put("quest.invalid-material", PREFIX + "§cMatériau invalide.");
        FR.put("quest.not-enough", PREFIX + "§cVous n'avez pas assez de §e%s §cpour terminer la quête.");
        FR.put("quest.deposit.completed", PREFIX + "§aVous avez terminé la quête de dépôt !");
        FR.put("quest.reward.received", PREFIX + "§aVous avez reçu §e%d §b%s§a !");
        FR.put("quest.invalid-command", PREFIX + "§cCommande invalide. Utilisez §e/quest §cpour l'aide.");
        FR.put("quest.kill.completed", PREFIX + "§aQuête de chasse terminée ! §aRécompense : §e%d §b%s");
        FR.put("quest.kill.progress", "§eQuête de chasse [§b%s§e] : §a%d/%d");
        FR.put("quest.break.completed", PREFIX + "§aQuête de minage terminée ! §aRécompense : §e%d §b%s");
        FR.put("quest.break.progress", "§eQuête de minage [§b%s§e] : §a%d/%d");

        FR.put("admin.help.title", PREFIX + "§eCommandes admin :");
        FR.put("admin.help.setup", PREFIX + "§a--> §b/adminquest setup §7<§6kill§7 // §6break§7 // §6deposit§7> <§dmateriel/entite§7> <§dquantite§7> <§drecompense§7> <§dquantite§7> <§dID§7>");
        FR.put("admin.help.remove", PREFIX + "§a--> §b/adminquest remove §7<§dID§7>");
        FR.put("admin.invalid-number", PREFIX + "§cQuantité ou récompense invalide.");
        FR.put("admin.invalid-target", PREFIX + "§cMatériau ou entité invalide pour ce type de quête.");
        FR.put("admin.invalid-reward", PREFIX + "§cRécompense invalide, elle doit être un objet Minecraft reconnu.");
        FR.put("admin.id-exists", PREFIX + "§cL'ID §e%s §cexiste déjà. §aProchain ID disponible : §e%d");
        FR.put("admin.quest-added", PREFIX + "§aQuête ajoutée avec succès !");
        FR.put("admin.quest-removed", PREFIX + "§aQuête supprimée !");
        FR.put("admin.id-not-found", PREFIX + "§cID de quête introuvable.");
        FR.put("admin.invalid-usage", PREFIX + "§cUtilisation invalide de la commande.");
        FR.put("admin.no-permission", PREFIX + "§cVous n'avez pas la permission d'exécuter cette commande.");

        FR.put("config.gui.title", "§8Configuration SimpleQuest");
        FR.put("config.gui.toggle.label", "§aPasser en français");
        FR.put("config.gui.current", "§7Langue actuelle : §e%s");
        FR.put("config.gui.click", "§7Clique pour changer la langue du plugin.");
        FR.put("config.changed.fr", PREFIX + "§aLa langue du plugin est maintenant en français.");
        FR.put("config.changed.en", PREFIX + "§aThe plugin language is now English.");

        EN.put("log.enabled", "SimpleQuest enabled");
        EN.put("log.version", "Version %s");
        EN.put("log.author", "Plugin by Caraito");
        EN.put("log.disabled", "SimpleQuest disabled");
        EN.put("log.thanks", "Thanks for using SimpleQuest!");

        EN.put("command.player-only", PREFIX + "§cThis command can only be used by a player.");

        EN.put("quest.help.title", PREFIX + "§eQuest commands:");
        EN.put("quest.help.info", PREFIX + "§a--> §b/quest info");
        EN.put("quest.help.info-id", PREFIX + "§a--> §b/quest info §7<§dID§7>");
        EN.put("quest.help.deposit", PREFIX + "§a--> §b/quest deposite §7<§dID§7>");
        EN.put("quest.no-quests", PREFIX + "§cNo quests available.");
        EN.put("quest.list.title", PREFIX + "§eQuest list:");
        EN.put("quest.list.entry", PREFIX + "§bID: §e%s §7| §aTarget: §d%s");
        EN.put("quest.not-found", PREFIX + "§cQuest not found.");
        EN.put("quest.details.title", PREFIX + "§eQuest details:");
        EN.put("quest.details.header", PREFIX + "§eQuest §b%s §e:");
        EN.put("quest.details.type", PREFIX + "§aType: §6%s");
        EN.put("quest.details.target", PREFIX + "§aMaterial/Entity: §d%s");
        EN.put("quest.details.amount", PREFIX + "§aAmount: §e%d");
        EN.put("quest.details.reward", PREFIX + "§aReward: §b%s §ex%d");
        EN.put("quest.details.status", PREFIX + "§eStatus: %s");
        EN.put("quest.status.completed", "§aCompleted");
        EN.put("quest.status.not-completed", "§cNot completed");
        EN.put("quest.not-deposit", PREFIX + "§cThis quest is not a deposit quest.");
        EN.put("quest.already-completed", PREFIX + "§eYou have already completed this quest.");
        EN.put("quest.invalid-material", PREFIX + "§cInvalid material.");
        EN.put("quest.not-enough", PREFIX + "§cYou do not have enough §e%s §cto complete the quest.");
        EN.put("quest.deposit.completed", PREFIX + "§aYou have completed the deposit quest!");
        EN.put("quest.reward.received", PREFIX + "§aYou received §e%d §b%s§a!");
        EN.put("quest.invalid-command", PREFIX + "§cInvalid command. Use §e/quest §cfor help.");
        EN.put("quest.kill.completed", PREFIX + "§aKill quest completed! §aReward: §e%d §b%s");
        EN.put("quest.kill.progress", "§eKill quest [§b%s§e]: §a%d/%d");
        EN.put("quest.break.completed", PREFIX + "§aBreak quest completed! §aReward: §e%d §b%s");
        EN.put("quest.break.progress", "§eBreak quest [§b%s§e]: §a%d/%d");

        EN.put("admin.help.title", PREFIX + "§eAdmin commands:");
        EN.put("admin.help.setup", PREFIX + "§a--> §b/adminquest setup §7<§6kill§7 // §6break§7 // §6deposit§7> <§dmaterial/entity§7> <§damount§7> <§dreward§7> <§damount§7> <§dquest ID§7>");
        EN.put("admin.help.remove", PREFIX + "§a--> §b/adminquest remove §7<§dquest ID§7>");
        EN.put("admin.invalid-number", PREFIX + "§cInvalid amount or reward amount.");
        EN.put("admin.invalid-target", PREFIX + "§cInvalid material or entity for this quest type.");
        EN.put("admin.invalid-reward", PREFIX + "§cInvalid reward, it must be a recognized Minecraft item.");
        EN.put("admin.id-exists", PREFIX + "§cThe ID §e%s §calready exists. §aNext available ID: §e%d");
        EN.put("admin.quest-added", PREFIX + "§aQuest successfully added!");
        EN.put("admin.quest-removed", PREFIX + "§aQuest removed!");
        EN.put("admin.id-not-found", PREFIX + "§cQuest ID not found.");
        EN.put("admin.invalid-usage", PREFIX + "§cInvalid command usage.");
        EN.put("admin.no-permission", PREFIX + "§cYou do not have permission to execute this command.");

        EN.put("config.gui.title", "§8SimpleQuest Settings");
        EN.put("config.gui.toggle.label", "§aSwitch to English");
        EN.put("config.gui.current", "§7Current language: §e%s");
        EN.put("config.gui.click", "§7Click to change the plugin language.");
        EN.put("config.changed.fr", PREFIX + "§aLa langue du plugin est maintenant en français.");
        EN.put("config.changed.en", PREFIX + "§aThe plugin language is now English.");
    }

    private Messages() {
    }

    public static String get(String language, String key, Object... args) {
        String normalizedLanguage = language == null ? "fr" : language.toLowerCase(Locale.ROOT);
        Map<String, String> selectedMessages = "en".equals(normalizedLanguage) ? EN : FR;
        String pattern = selectedMessages.getOrDefault(key, FR.getOrDefault(key, key));
        return args.length == 0 ? pattern : String.format(pattern, args);
    }
}

