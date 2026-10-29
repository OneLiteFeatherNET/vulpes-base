package net.onelitefeather.vulpes;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.onelitefeather.vulpes.advancement.CustomAdvancement;
import net.onelitefeather.vulpes.attribute.CustomAttribute;
import net.onelitefeather.vulpes.font.CustomFontSymbol;
import net.onelitefeather.vulpes.font.FontSymbol;
import net.onelitefeather.vulpes.item.CustomItem;
import net.onelitefeather.vulpes.sound.CustomSound;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class VulpesTest {

    private static final Key IRON_SWORD = Key.key("cygnus", "iron_sword");
    private static final Key GOLD_SWORD = Key.key("cygnus", "gold_sword");
    private static final Key OLD_SWORD = Key.key("cygnus", "old_sword");

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    private static CustomItem item(Key key) {
        return CustomItem.of(key, ItemStack.of(Material.IRON_SWORD));
    }

    private static CustomAdvancement advancement(String path, @Nullable String parent) {
        return CustomAdvancement.builder(Key.key("cygnus", path))
                .parent(parent == null ? null : Key.key("cygnus", parent))
                .title(Component.text(path))
                .icon(ItemStack.of(Material.STONE))
                .build();
    }

    @Test
    void bootstrapCollectsItemsOfAllContents() {
        VulpesRegistries registries = Vulpes.bootstrap(TestContent.withItems(item(IRON_SWORD)),
                TestContent.withItems(item(GOLD_SWORD)));

        assertEquals(Set.of(IRON_SWORD, GOLD_SWORD), Set.copyOf(registries.items().keys()),
                "Items of every content should be registered");
    }

    @Test
    void bootstrapReturnsIndependentRegistries() {
        VulpesRegistries first = Vulpes.bootstrap(TestContent.withItems(item(IRON_SWORD)));
        VulpesRegistries second = Vulpes.bootstrap(TestContent.withItems(item(GOLD_SWORD)));

        assertEquals(Set.of(IRON_SWORD), Set.copyOf(first.items().keys()),
                "The first registries should contain only their own items");
        assertEquals(Set.of(GOLD_SWORD), Set.copyOf(second.items().keys()),
                "The second registries should contain only their own items");
    }

    @Test
    void bootstrapWithoutContentRegistersNothing() {
        VulpesRegistries registries = Vulpes.bootstrap();

        assertEquals(0, registries.items().size(), "Without content no item should be registered");
    }

    @Test
    void bootstrapRejectsNullContent() {
        assertThrows(NullPointerException.class, () -> Vulpes.bootstrap((VulpesContent) null),
                "A null content must be rejected");
    }

    @Test
    void bootstrapRejectsDuplicateKeyAcrossContentsNamingKey() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> Vulpes.bootstrap(TestContent.withItems(item(IRON_SWORD)), TestContent.withItems(item(IRON_SWORD))));

        assertTrue(ex.getMessage().contains("cygnus:iron_sword"), "Message should name the key: " + ex.getMessage());
    }

    @Test
    void bootstrapRejectsAliasDeclaredInTwoContentsNamingKey() {
        TestContent first = TestContent.withItems(item(IRON_SWORD)).withAliases(Map.of(OLD_SWORD, IRON_SWORD));
        TestContent second = TestContent.withItems(item(GOLD_SWORD)).withAliases(Map.of(OLD_SWORD, GOLD_SWORD));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> Vulpes.bootstrap(first, second));

        assertTrue(ex.getMessage().contains("cygnus:old_sword"), "Message should name the alias: " + ex.getMessage());
    }

    @Test
    void bootstrapRejectsAliasThatEqualsEntryKeyNamingKey() {
        TestContent content = TestContent.withItems(item(IRON_SWORD), item(GOLD_SWORD))
                .withAliases(Map.of(IRON_SWORD, GOLD_SWORD));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> Vulpes.bootstrap(content));

        assertTrue(ex.getMessage().contains("cygnus:iron_sword"), "Message should name the key: " + ex.getMessage());
    }

    @Test
    void bootstrapRejectsAliasWithoutEntryNamingBothKeys() {
        TestContent content = TestContent.withItems(item(IRON_SWORD)).withAliases(Map.of(OLD_SWORD, GOLD_SWORD));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> Vulpes.bootstrap(content));

        assertTrue(ex.getMessage().contains("cygnus:old_sword") && ex.getMessage().contains("cygnus:gold_sword"),
                "Message should name the alias and its target: " + ex.getMessage());
    }

    @Test
    void bootstrapRejectsMissingAdvancementParentNamingBothKeys() {
        TestContent content = TestContent.withAdvancements(advancement("child", "ghost"));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> Vulpes.bootstrap(content));

        assertTrue(ex.getMessage().contains("cygnus:child") && ex.getMessage().contains("cygnus:ghost"),
                "Message should name the advancement and its parent: " + ex.getMessage());
    }

    @Test
    void bootstrapRejectsAdvancementCycleNamingBothKeys() {
        TestContent content = TestContent.withAdvancements(advancement("a", "b"), advancement("b", "a"));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> Vulpes.bootstrap(content));

        assertTrue(ex.getMessage().contains("cygnus:a") && ex.getMessage().contains("cygnus:b"),
                "Message should name both keys of the cycle: " + ex.getMessage());
    }

    @Test
    void bootstrapRegistersEntriesOfEveryType() {
        Key attributeKey = Key.key("cygnus", "speed");
        Key soundKey = Key.key("cygnus", "ui.click");
        Key symbolKey = Key.key("cygnus", "hud");
        Key advancementKey = Key.key("cygnus", "root");
        TestContent content = new TestContent(
                List.of(item(IRON_SWORD)),
                List.of(CustomAttribute.of(attributeKey, "Speed", null, 0.0, 1.0, 10.0, false)),
                List.of(CustomSound.of(soundKey, null)),
                List.of(CustomFontSymbol.of(symbolKey, Key.key("cygnus", "font"),
                        new FontSymbol("cygnus:font/hud.png", "bitmap", 7, 8, List.of("a")))),
                List.of(advancement("root", null)),
                Map.of());

        VulpesRegistries registries = Vulpes.bootstrap(content);

        assertTrue(registries.attributes().contains(attributeKey), "Attributes should be registered");
        assertTrue(registries.sounds().contains(soundKey), "Sounds should be registered");
        assertTrue(registries.fontSymbols().contains(symbolKey), "Font symbols should be registered");
        assertTrue(registries.advancements().contains(advancementKey), "Advancements should be registered");
    }

    @Test
    void aliasResolvesToCurrentItem() {
        VulpesRegistries registries = Vulpes.bootstrap(TestContent.withItems(item(IRON_SWORD))
                .withAliases(Map.of(OLD_SWORD, IRON_SWORD)));

        CustomItem current = registries.items().get(IRON_SWORD);

        assertNotNull(current, "The current item should be registered");
        assertSame(current, registries.items().get(OLD_SWORD), "The alias should resolve to the current item");
    }

    @Test
    void aliasIsNotListedAsKeyOrValue() {
        VulpesRegistries registries = Vulpes.bootstrap(TestContent.withItems(item(IRON_SWORD))
                .withAliases(Map.of(OLD_SWORD, IRON_SWORD)));

        assertEquals(List.of(IRON_SWORD), List.copyOf(registries.items().keys()), "Alias keys should not be listed");
        assertEquals(1, registries.items().values().size(), "Aliases should not be listed as values");
    }
}
