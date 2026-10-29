package ru.funitems;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public final class FunItemsPlugin extends JavaPlugin implements TabExecutor {

    private NamespacedKey itemKey;
    private final Map<String, Long> cooldowns = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        itemKey = new NamespacedKey(this, "item");
        Bukkit.getPluginManager().registerEvents(new ItemListener(this), this);
        var cmd = getCommand("funitems");
        if (cmd != null) {
            cmd.setExecutor(this);
            cmd.setTabCompleter(this);
        }
        getLogger().info("FunItems включён: " + CustomItem.values().length + " предметов.");
    }

    public NamespacedKey itemKey() {
        return itemKey;
    }

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public String msg(String path) {
        return color(getConfig().getString("messages.prefix", "")
                + getConfig().getString("messages." + path, path));
    }

    // ---------- создание и распознавание предметов ----------

    public ItemStack create(CustomItem type, int amount) {
        ItemStack stack = new ItemStack(type.material(), Math.max(1, Math.min(amount, 64)));
        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(color(getConfig().getString("items." + type.id() + ".name", type.defaultName())));
            List<String> lore = new ArrayList<>();
            for (String line : type.lore()) {
                lore.add(color(line));
            }
            meta.setLore(lore);
            meta.getPersistentDataContainer().set(itemKey, PersistentDataType.STRING, type.id());
            stack.setItemMeta(meta);
        }
        return stack;
    }

    public CustomItem identify(ItemStack stack) {
        if (stack == null || !stack.hasItemMeta()) {
            return null;
        }
        String id = stack.getItemMeta().getPersistentDataContainer().get(itemKey, PersistentDataType.STRING);
        return id == null ? null : CustomItem.byId(id);
    }

    // ---------- кулдауны ----------

    /** Возвращает 0, если предмет можно использовать, иначе — сколько секунд осталось. */
    public long remainingCooldown(UUID player, CustomItem type) {
        Long until = cooldowns.get(player + ":" + type.id());
        if (until == null) {
            return 0;
        }
        long left = until - System.currentTimeMillis();
        return left > 0 ? (left + 999) / 1000 : 0;
    }

    public void startCooldown(UUID player, CustomItem type) {
        int seconds = getConfig().getInt("items." + type.id() + ".cooldown", 0);
        if (seconds > 0) {
            cooldowns.put(player + ":" + type.id(), System.currentTimeMillis() + seconds * 1000L);
        }
    }

    // ---------- команда ----------

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("funitems.admin")) {
            sender.sendMessage(msg("no-permission"));
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(color("&6/funitems give <игрок> <предмет> [кол-во] &7| &6list &7| &6reload"));
            return true;
        }
        switch (args[0].toLowerCase(Locale.ROOT)) {
            case "reload" -> {
                reloadConfig();
                sender.sendMessage(msg("reloaded"));
            }
            case "list" -> {
                for (CustomItem item : CustomItem.values()) {
                    sender.sendMessage(color("&7- &e" + item.id() + " &8(&f"
                            + getConfig().getString("items." + item.id() + ".name", item.id()) + "&8)"));
                }
            }
            case "give" -> {
                if (args.length < 3) {
                    sender.sendMessage(color("&cИспользование: /funitems give <игрок> <предмет> [кол-во]"));
                    return true;
                }
                Player target = Bukkit.getPlayerExact(args[1]);
                if (target == null) {
                    sender.sendMessage(msg("player-not-found"));
                    return true;
                }
                CustomItem type = CustomItem.byId(args[2]);
                if (type == null) {
                    sender.sendMessage(msg("unknown-item"));
                    return true;
                }
                int amount = 1;
                if (args.length >= 4) {
                    try {
                        amount = Integer.parseInt(args[3]);
                    } catch (NumberFormatException ignored) {
                        // оставляем 1
                    }
                }
                amount = Math.max(1, Math.min(amount, 64));
                target.getInventory().addItem(create(type, amount)).values()
                        .forEach(rest -> target.getWorld().dropItemNaturally(target.getLocation(), rest));
                sender.sendMessage(color(getConfig().getString("messages.prefix", "")
                        + String.format(getConfig().getString("messages.given", "%s x%d %s"),
                        type.id(), amount, target.getName())));
            }
            default -> sender.sendMessage(color("&cНеизвестная подкоманда."));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> result = new ArrayList<>();
        if (!sender.hasPermission("funitems.admin")) {
            return result;
        }
        if (args.length == 1) {
            for (String s : List.of("give", "list", "reload")) {
                if (s.startsWith(args[0].toLowerCase(Locale.ROOT))) {
                    result.add(s);
                }
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase(Locale.ROOT).startsWith(args[1].toLowerCase(Locale.ROOT))) {
                    result.add(p.getName());
                }
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            for (CustomItem item : CustomItem.values()) {
                if (item.id().startsWith(args[2].toLowerCase(Locale.ROOT))) {
                    result.add(item.id());
                }
            }
        }
        return result;
    }
}
