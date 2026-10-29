package ru.funitems;

import org.bukkit.Material;
import java.util.List;

public enum CustomItem {
    TRAP("trap", Material.NETHERITE_SCRAP, "Трапка", List.of("&7ПКМ — клетка вокруг игрока")),
    PLAST("plast", Material.PRISMARINE_SHARD, "Пласт", List.of("&7ПКМ — стена перед игроком")),
    DISORIENT("disorient", Material.ENDER_EYE, "Дезориентация", List.of("&7ПКМ — эффекты врагам рядом")),
    DIVINE_AURA("divine_aura", Material.PHANTOM_MEMBRANE, "Божья аура", List.of("&7ПКМ — защита и регенерация")),
    REVEAL_DUST("reveal_dust", Material.GLOWSTONE_DUST, "Явная пыль", List.of("&7ПКМ — подсветка игроков")),
    FREEZE_SNOWBALL("freeze_snowball", Material.SNOWBALL, "Снежок-заморозка", List.of("&7Бросьте в игрока для заморозки")),
    FIRE_SWIRL("fire_swirl", Material.FIRE_CHARGE, "Огненный смерч", List.of("&7ПКМ — огонь и подбрасывание")),
    HOLY_WATER("holy_water", Material.GLASS_BOTTLE, "Святая вода", List.of("&7ПКМ — снимает негативные эффекты")),
    TOTEM("totem", Material.TOTEM_OF_UNDYING, "Тотем ФанТайм", List.of("&7Обычный тотем бессмертия")),
    TOTEM_GOD("totem_god", Material.TOTEM_OF_UNDYING, "Божественный тотем", List.of("&7Уникальное оформление тотема")),
    TOTEM_PVP("totem_pvp", Material.TOTEM_OF_UNDYING, "ПВП-тотем", List.of("&7Уникальное оформление тотема")),
    TOTEM_HEAL("totem_heal", Material.TOTEM_OF_UNDYING, "Тотем лечения", List.of("&7Уникальное оформление тотема")),
    WOODEN_SWORD("wooden_sword", Material.WOODEN_SWORD, "Wooden меч", List.of("&7Оружие для PvP")),
    STONE_SWORD("stone_sword", Material.STONE_SWORD, "Stone меч", List.of("&7Оружие для PvP")),
    IRON_SWORD("iron_sword", Material.IRON_SWORD, "Iron меч", List.of("&7Оружие для PvP")),
    GOLDEN_SWORD("golden_sword", Material.GOLDEN_SWORD, "Golden меч", List.of("&7Оружие для PvP")),
    DIAMOND_SWORD("diamond_sword", Material.DIAMOND_SWORD, "Diamond меч", List.of("&7Оружие для PvP")),
    NETHERITE_SWORD("netherite_sword", Material.NETHERITE_SWORD, "Netherite меч", List.of("&7Оружие для PvP")),
    LEATHER_HELMET("leather_helmet", Material.LEATHER_HELMET, "Leather шлем", List.of("&7Элемент комплекта брони")),
    LEATHER_CHESTPLATE("leather_chestplate", Material.LEATHER_CHESTPLATE, "Leather нагрудник", List.of("&7Элемент комплекта брони")),
    LEATHER_LEGGINGS("leather_leggings", Material.LEATHER_LEGGINGS, "Leather поножи", List.of("&7Элемент комплекта брони")),
    LEATHER_BOOTS("leather_boots", Material.LEATHER_BOOTS, "Leather ботинки", List.of("&7Элемент комплекта брони")),
    CHAINMAIL_HELMET("chainmail_helmet", Material.CHAINMAIL_HELMET, "Chainmail шлем", List.of("&7Элемент комплекта брони")),
    CHAINMAIL_CHESTPLATE("chainmail_chestplate", Material.CHAINMAIL_CHESTPLATE, "Chainmail нагрудник", List.of("&7Элемент комплекта брони")),
    CHAINMAIL_LEGGINGS("chainmail_leggings", Material.CHAINMAIL_LEGGINGS, "Chainmail поножи", List.of("&7Элемент комплекта брони")),
    CHAINMAIL_BOOTS("chainmail_boots", Material.CHAINMAIL_BOOTS, "Chainmail ботинки", List.of("&7Элемент комплекта брони")),
    IRON_HELMET("iron_helmet", Material.IRON_HELMET, "Iron шлем", List.of("&7Элемент комплекта брони")),
    IRON_CHESTPLATE("iron_chestplate", Material.IRON_CHESTPLATE, "Iron нагрудник", List.of("&7Элемент комплекта брони")),
    IRON_LEGGINGS("iron_leggings", Material.IRON_LEGGINGS, "Iron поножи", List.of("&7Элемент комплекта брони")),
    IRON_BOOTS("iron_boots", Material.IRON_BOOTS, "Iron ботинки", List.of("&7Элемент комплекта брони")),
    GOLDEN_HELMET("golden_helmet", Material.GOLDEN_HELMET, "Golden шлем", List.of("&7Элемент комплекта брони")),
    GOLDEN_CHESTPLATE("golden_chestplate", Material.GOLDEN_CHESTPLATE, "Golden нагрудник", List.of("&7Элемент комплекта брони")),
    GOLDEN_LEGGINGS("golden_leggings", Material.GOLDEN_LEGGINGS, "Golden поножи", List.of("&7Элемент комплекта брони")),
    GOLDEN_BOOTS("golden_boots", Material.GOLDEN_BOOTS, "Golden ботинки", List.of("&7Элемент комплекта брони")),
    DIAMOND_HELMET("diamond_helmet", Material.DIAMOND_HELMET, "Diamond шлем", List.of("&7Элемент комплекта брони")),
    DIAMOND_CHESTPLATE("diamond_chestplate", Material.DIAMOND_CHESTPLATE, "Diamond нагрудник", List.of("&7Элемент комплекта брони")),
    DIAMOND_LEGGINGS("diamond_leggings", Material.DIAMOND_LEGGINGS, "Diamond поножи", List.of("&7Элемент комплекта брони")),
    DIAMOND_BOOTS("diamond_boots", Material.DIAMOND_BOOTS, "Diamond ботинки", List.of("&7Элемент комплекта брони")),
    NETHERITE_HELMET("netherite_helmet", Material.NETHERITE_HELMET, "Netherite шлем", List.of("&7Элемент комплекта брони")),
    NETHERITE_CHESTPLATE("netherite_chestplate", Material.NETHERITE_CHESTPLATE, "Netherite нагрудник", List.of("&7Элемент комплекта брони")),
    NETHERITE_LEGGINGS("netherite_leggings", Material.NETHERITE_LEGGINGS, "Netherite поножи", List.of("&7Элемент комплекта брони")),
    NETHERITE_BOOTS("netherite_boots", Material.NETHERITE_BOOTS, "Netherite ботинки", List.of("&7Элемент комплекта брони")),
    TURTLE_HELMET("turtle_helmet", Material.TURTLE_HELMET, "Turtle шлем", List.of("&7Элемент комплекта брони")),
    GOLDEN_APPLE("golden_apple", Material.GOLDEN_APPLE, "Золотое яблоко", List.of("&7Еда для PvP")),
    ENCHANTED_GOLDEN_APPLE("enchanted_golden_apple", Material.ENCHANTED_GOLDEN_APPLE, "Зачарованное золотое яблоко", List.of("&7Редкий расходник")),
    ENDER_PEARL("ender_pearl", Material.ENDER_PEARL, "Эндер-жемчуг", List.of("&7Телепортация")),
    CHORUS_FRUIT("chorus_fruit", Material.CHORUS_FRUIT, "Плод хоруса", List.of("&7Телепортация")),
    WIND_CHARGE("wind_charge", Material.WIND_CHARGE, "Ветряной заряд", List.of("&7Отбрасывание и мобильность")),
    FIREWORK_ROCKET("firework_rocket", Material.FIREWORK_ROCKET, "Фейерверк", List.of("&7Для полёта на элитрах")),
    ENDER_CHEST("ender_chest", Material.ENDER_CHEST, "Эндер-сундук", List.of("&7Хранилище")),
    OBSIDIAN("obsidian", Material.OBSIDIAN, "Обсидиан", List.of("&7Блок для трапок")),
    CRYING_OBSIDIAN("crying_obsidian", Material.CRYING_OBSIDIAN, "Плачущий обсидиан", List.of("&7Блок для построек")),
    COBWEB("cobweb", Material.COBWEB, "Паутина", List.of("&7Замедляет противников")),
    WATER_BUCKET("water_bucket", Material.WATER_BUCKET, "Ведро воды", List.of("&7Вода для PvP")),
    LAVA_BUCKET("lava_bucket", Material.LAVA_BUCKET, "Ведро лавы", List.of("&7Лава для PvP")),
    SHIELD("shield", Material.SHIELD, "Щит", List.of("&7Защита")),
    CROSSBOW("crossbow", Material.CROSSBOW, "Арбалет", List.of("&7Дальняя атака")),
    BOW("bow", Material.BOW, "Лук", List.of("&7Дальняя атака")),
    TRIDENT("trident", Material.TRIDENT, "Трезубец", List.of("&7Оружие дальнего боя")),
    MACE("mace", Material.MACE, "Булава", List.of("&7Тяжёлое оружие")),
    GOLDEN_CARROT("golden_carrot", Material.GOLDEN_CARROT, "Золотая морковь", List.of("&7Еда для PvP")),
    SPLASH_POTION("splash_potion", Material.SPLASH_POTION, "Взрывное зелье", List.of("&7Расходник")),
    LINGERING_POTION("lingering_potion", Material.LINGERING_POTION, "Туманное зелье", List.of("&7Расходник")),
    EXPERIENCE_BOTTLE("experience_bottle", Material.EXPERIENCE_BOTTLE, "Пузырёк опыта", List.of("&7Опыт"));

    private final String id;
    private final Material material;
    private final String defaultName;
    private final List<String> lore;

    CustomItem(String id, Material material, String defaultName, List<String> lore) {
        this.id = id;
        this.material = material;
        this.defaultName = defaultName;
        this.lore = lore;
    }
    public String id() { return id; }
    public Material material() { return material; }
    public String defaultName() { return defaultName; }
    public List<String> lore() { return lore; }
    public static CustomItem byId(String id) {
        for (CustomItem item : values()) if (item.id.equalsIgnoreCase(id)) return item;
        return null;
    }
}
