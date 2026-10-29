package ru.funitems;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public final class ItemListener implements Listener {

    private static final List<PotionEffectType> NEGATIVE_EFFECTS = List.of(
            PotionEffectType.POISON, PotionEffectType.WITHER, PotionEffectType.SLOWNESS,
            PotionEffectType.WEAKNESS, PotionEffectType.BLINDNESS, PotionEffectType.NAUSEA,
            PotionEffectType.HUNGER, PotionEffectType.MINING_FATIGUE, PotionEffectType.LEVITATION,
            PotionEffectType.DARKNESS, PotionEffectType.UNLUCK);

    private final FunItemsPlugin plugin;
    private final NamespacedKey freezeKey;

    public ItemListener(FunItemsPlugin plugin) {
        this.plugin = plugin;
        this.freezeKey = new NamespacedKey(plugin, "freeze_snowball");
    }

    // ---------- использование предметов по ПКМ ----------

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Player player = event.getPlayer();
        ItemStack hand = player.getInventory().getItemInMainHand();
        CustomItem type = plugin.identify(hand);
        if (type == null || type == CustomItem.FREEZE_SNOWBALL) {
            return; // снежок обрабатывается при запуске снаряда
        }

        // запрещаем обычное действие предмета (поджечь, набрать воды и т.д.)
        event.setUseItemInHand(Event.Result.DENY);

        if (!player.hasPermission("funitems.use")) {
            player.sendMessage(plugin.msg("no-permission"));
            return;
        }
        long left = plugin.remainingCooldown(player.getUniqueId(), type);
        if (left > 0) {
            player.sendMessage(String.format(plugin.msg("cooldown"), left));
            return;
        }

        boolean used = switch (type) {
            case TRAP -> useTrap(player);
            case PLAST -> usePlast(player);
            case DISORIENT -> useDisorient(player);
            case DIVINE_AURA -> useDivineAura(player);
            case REVEAL_DUST -> useRevealDust(player);
            case FIRE_SWIRL -> useFireSwirl(player);
            case HOLY_WATER -> useHolyWater(player);
            default -> false;
        };
        if (!used) {
            return;
        }

        plugin.startCooldown(player.getUniqueId(), type);
        hand.setAmount(hand.getAmount() - 1);
        player.getInventory().setItemInMainHand(hand.getAmount() <= 0 ? null : hand);
    }

    // ---------- трапка ----------

    private boolean useTrap(Player player) {
        String path = "items.trap.";
        Material material = parseMaterial(plugin.getConfig().getString(path + "block"), Material.OBSIDIAN);
        int duration = plugin.getConfig().getInt(path + "duration", 15);

        Block center = player.getLocation().getBlock();
        List<Block> placed = new ArrayList<>();
        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -1; dy <= 3; dy++) {
                for (int dz = -2; dz <= 2; dz++) {
                    boolean shell = Math.abs(dx) == 2 || Math.abs(dz) == 2 || dy == -1 || dy == 3;
                    if (!shell) {
                        continue;
                    }
                    Block block = center.getRelative(dx, dy, dz);
                    if (block.getType().isAir()) {
                        block.setType(material);
                        placed.add(block);
                    }
                }
            }
        }
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1f, 0.8f);
        scheduleRemoval(placed, material, duration);
        return true;
    }

    // ---------- пласт ----------

    private boolean usePlast(Player player) {
        String path = "items.plast.";
        Material material = parseMaterial(plugin.getConfig().getString(path + "block"), Material.PRISMARINE_BRICKS);
        int duration = plugin.getConfig().getInt(path + "duration", 10);

        BlockFace facing = player.getFacing();
        int rx = -facing.getModZ();
        int rz = facing.getModX();
        Block origin = player.getLocation().getBlock()
                .getRelative(facing.getModX() * 3, 0, facing.getModZ() * 3);

        List<Block> placed = new ArrayList<>();
        for (int w = -2; w <= 2; w++) {
            for (int h = 0; h <= 4; h++) {
                Block block = origin.getRelative(rx * w, h, rz * w);
                if (block.getType().isAir()) {
                    block.setType(material);
                    placed.add(block);
                }
            }
        }
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_STONE_PLACE, 1f, 0.7f);
        scheduleRemoval(placed, material, duration);
        return true;
    }

    private void scheduleRemoval(List<Block> blocks, Material material, int seconds) {
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            for (Block block : blocks) {
                if (block.getType() == material) {
                    block.setType(Material.AIR);
                }
            }
        }, Math.max(1, seconds) * 20L);
    }

    // ---------- дезориентация ----------

    private boolean useDisorient(Player player) {
        double radius = plugin.getConfig().getDouble("items.disorient.radius", 8.0);
        int ticks = plugin.getConfig().getInt("items.disorient.duration", 8) * 20;
        for (Player target : nearbyPlayers(player, radius)) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, ticks, 0));
            target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, ticks / 2, 0));
            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, ticks, 1));
        }
        Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(Particle.PORTAL, loc, 120, radius / 3, 1, radius / 3, 0.5);
        player.getWorld().playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 0.6f);
        return true;
    }

    // ---------- божья аура ----------

    private boolean useDivineAura(Player player) {
        int ticks = plugin.getConfig().getInt("items.divine_aura.duration", 10) * 20;
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, ticks, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, ticks, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, ticks, 1));
        Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(Particle.END_ROD, loc, 60, 0.6, 1, 0.6, 0.05);
        player.getWorld().playSound(loc, Sound.BLOCK_BEACON_ACTIVATE, 1f, 1.2f);
        return true;
    }

    // ---------- явная пыль ----------

    private boolean useRevealDust(Player player) {
        double radius = plugin.getConfig().getDouble("items.reveal_dust.radius", 15.0);
        int ticks = plugin.getConfig().getInt("items.reveal_dust.duration", 12) * 20;
        for (Player target : nearbyPlayers(player, radius)) {
            target.removePotionEffect(PotionEffectType.INVISIBILITY);
            target.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, ticks, 0));
        }
        Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(Particle.END_ROD, loc, 80, radius / 3, 1, radius / 3, 0.02);
        player.getWorld().playSound(loc, Sound.ENTITY_EVOKER_CAST_SPELL, 1f, 1.4f);
        return true;
    }

    // ---------- огненный смерч ----------

    private boolean useFireSwirl(Player player) {
        double radius = plugin.getConfig().getDouble("items.fire_swirl.radius", 6.0);
        int fireTicks = plugin.getConfig().getInt("items.fire_swirl.fire_seconds", 6) * 20;
        double launch = plugin.getConfig().getDouble("items.fire_swirl.launch", 0.9);
        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            if (entity instanceof LivingEntity living && !living.equals(player)) {
                living.setFireTicks(fireTicks);
                living.setVelocity(living.getVelocity().add(new org.bukkit.util.Vector(0, launch, 0)));
            }
        }
        Location loc = player.getLocation().add(0, 0.5, 0);
        player.getWorld().spawnParticle(Particle.FLAME, loc, 150, radius / 2, 1, radius / 2, 0.03);
        player.getWorld().playSound(loc, Sound.ITEM_FIRECHARGE_USE, 1f, 0.8f);
        return true;
    }

    // ---------- святая вода ----------

    private boolean useHolyWater(Player player) {
        int ticks = plugin.getConfig().getInt("items.holy_water.regen_seconds", 8) * 20;
        for (PotionEffectType type : NEGATIVE_EFFECTS) {
            player.removePotionEffect(type);
        }
        player.setFireTicks(0);
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, ticks, 1));
        Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, loc, 40, 0.5, 1, 0.5, 0.1);
        player.getWorld().playSound(loc, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1.5f);
        return true;
    }

    // ---------- снежок-заморозка ----------

    @EventHandler
    public void onLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof Snowball snowball) || !(snowball.getShooter() instanceof Player player)) {
            return;
        }
        ItemStack main = player.getInventory().getItemInMainHand();
        ItemStack off = player.getInventory().getItemInOffHand();
        boolean isFreeze = plugin.identify(main) == CustomItem.FREEZE_SNOWBALL
                || plugin.identify(off) == CustomItem.FREEZE_SNOWBALL;
        if (!isFreeze) {
            return;
        }
        long left = plugin.remainingCooldown(player.getUniqueId(), CustomItem.FREEZE_SNOWBALL);
        if (left > 0) {
            event.setCancelled(true);
            player.sendMessage(String.format(plugin.msg("cooldown"), left));
            return;
        }
        plugin.startCooldown(player.getUniqueId(), CustomItem.FREEZE_SNOWBALL);
        snowball.getPersistentDataContainer().set(freezeKey, PersistentDataType.BYTE, (byte) 1);
    }

    @EventHandler
    public void onHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if (!(projectile instanceof Snowball)
                || !projectile.getPersistentDataContainer().has(freezeKey, PersistentDataType.BYTE)) {
            return;
        }
        if (!(event.getHitEntity() instanceof Player target)) {
            return;
        }
        int ticks = plugin.getConfig().getInt("items.freeze_snowball.duration", 5) * 20;
        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, ticks, 6));
        target.setFreezeTicks(target.getMaxFreezeTicks());
        Location loc = target.getLocation().add(0, 1, 0);
        target.getWorld().spawnParticle(Particle.SNOWFLAKE, loc, 60, 0.4, 0.8, 0.4, 0.05);
        target.getWorld().playSound(loc, Sound.BLOCK_GLASS_BREAK, 1f, 1.3f);
    }

    // ---------- вспомогательное ----------

    private List<Player> nearbyPlayers(Player source, double radius) {
        List<Player> result = new ArrayList<>();
        for (Entity entity : source.getNearbyEntities(radius, radius, radius)) {
            if (entity instanceof Player other && !other.equals(source)) {
                result.add(other);
            }
        }
        return result;
    }

    private Material parseMaterial(String name, Material fallback) {
        if (name == null) {
            return fallback;
        }
        Material material = Material.matchMaterial(name);
        return material != null && material.isBlock() ? material : fallback;
    }
}
