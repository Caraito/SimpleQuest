package fr.caraito.simpleQuest.commands;

import fr.caraito.simpleQuest.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class AdminQuestConfig implements CommandExecutor, Listener {

    private final Main main;

    public AdminQuestConfig(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(main.tr("command.player-only"));
            return true;
        }

        if (!player.hasPermission("simplequest.admin")) {
            player.sendMessage(main.tr("admin.no-permission"));
            return true;
        }

        openConfigInventory(player);
        return true;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if (!(event.getInventory().getHolder() instanceof AdminQuestConfigHolder)) {
            return;
        }

        event.setCancelled(true);

        if (event.getCurrentItem() == null || event.getCurrentItem().getType() != Material.PAPER) {
            return;
        }

        String currentLanguage = main.getLanguage();
        String nextLanguage = "fr".equals(currentLanguage) ? "en" : "fr";
        main.setLanguage(nextLanguage);

        player.sendMessage(main.tr(nextLanguage.equals("fr") ? "config.changed.fr" : "config.changed.en"));
        Bukkit.getScheduler().runTask(main, () -> openConfigInventory(player));
    }

    private void openConfigInventory(Player player) {
        String currentLanguage = main.getLanguage();
        String targetLanguage = "fr".equals(currentLanguage) ? "en" : "fr";
        AdminQuestConfigHolder holder = new AdminQuestConfigHolder();
        Inventory inventory = Bukkit.createInventory(holder, 9, main.tr("config.gui.title"));
        holder.setInventory(inventory);

        ItemStack languageSwitch = new ItemStack(Material.PAPER);
        ItemMeta meta = languageSwitch.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(main.trForLanguage(targetLanguage, "config.gui.toggle.label"));
            meta.setLore(Arrays.asList(
                    main.tr("config.gui.current", currentLanguage.toUpperCase()),
                    main.tr("config.gui.click")
            ));
            languageSwitch.setItemMeta(meta);
        }

        inventory.setItem(4, languageSwitch);
        player.openInventory(inventory);
    }

    private static final class AdminQuestConfigHolder implements InventoryHolder {
        private Inventory inventory;

        public void setInventory(Inventory inventory) {
            this.inventory = inventory;
        }

        @Override
        public Inventory getInventory() {
            return inventory;
        }
    }
}

