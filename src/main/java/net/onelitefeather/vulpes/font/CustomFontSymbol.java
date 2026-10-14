package net.onelitefeather.vulpes.font;

import net.kyori.adventure.key.Key;
import net.onelitefeather.vulpes.registries.Registry;
import net.onelitefeather.vulpes.registries.VulpesKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * A custom font symbol which is defined in code and available through a runtime registry.
 * <p>
 * The constants are inherited from the generated {@code CustomFontSymbols} interface, the keys
 * are available through {@link CustomFontSymbolKeys}.
 *
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public sealed interface CustomFontSymbol extends VulpesKey, CustomFontSymbols permits CustomFontSymbolImpl {

    /**
     * Returns the key of the font the glyphs live in, to be used with {@code Component.font(Key)}.
     *
     * @return the font key
     */
    Key font();

    /**
     * Returns the font provider definition of the symbol.
     *
     * @return the font symbol
     */
    FontSymbol symbol();

    /**
     * Returns the rows of glyphs of the symbol.
     *
     * @return the rows of glyphs
     */
    default List<String> chars() {
        return symbol().chars();
    }

    /**
     * Returns the symbol for the given key.
     *
     * @param key the key to look up
     * @return the symbol or null if no symbol is registered for the key
     */
    static @Nullable CustomFontSymbol fromKey(Key key) {
        return CustomFontSymbolImpl.REGISTRY.get(key);
    }

    /**
     * Returns all registered symbols.
     *
     * @return the registered symbols
     */
    static Collection<CustomFontSymbol> values() {
        return CustomFontSymbolImpl.REGISTRY.values();
    }

    /**
     * Returns the registry which contains all custom font symbols.
     *
     * @return the registry
     */
    static Registry<CustomFontSymbol> registry() {
        return CustomFontSymbolImpl.REGISTRY;
    }
}
