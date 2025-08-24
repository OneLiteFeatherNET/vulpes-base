package net.theevilreaper.vulpes.font;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.util.Codec;
import net.theevilreaper.vulpes.registries.Registry;
import net.theevilreaper.vulpes.registries.RegistryFactory;
import net.theevilreaper.vulpes.registries.RegistryResources;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public record BitFontSymbol(@NotNull Key key, int ascent, int height, @NotNull List<String> symbols) implements FontSymbol {

    public static final Registry<BitFontSymbol> REGISTRY = RegistryFactory.createRegistry(Key.key("vulpes", "fonts"), BitFontSymbol::loadEntries, RegistryResources.BITMAP_FONTS);
    private static Codec<BitFontSymbol, JsonObject, Throwable, Throwable> BIT_FONT_SYMBOL_CODEC = new BitFontSymbol.BitFontSymbolCodec();
    private static final Logger LOGGER = LoggerFactory.getLogger(BitFontSymbol.class);
    private static final Gson GSON = new Gson();

    static BitFontSymbol get(Key key) {
        return REGISTRY.get(key);
    }

    private static List<BitFontSymbol> loadEntries(InputStream inputStream) {
        JsonArray jsonElements = GSON.fromJson(new InputStreamReader(inputStream, StandardCharsets.UTF_8), JsonArray.class);
        return jsonElements.asList().stream().map(JsonElement::getAsJsonObject).map(jsonObject -> {
            try {
                return BIT_FONT_SYMBOL_CODEC.decode(jsonObject);
            } catch (Throwable e) {
                LOGGER.error("Failed to decode font symbol: {}", jsonObject, e);
                return null;
            }
        }).filter(Objects::nonNull).toList();
    }

    static class BitFontSymbolCodec implements Codec<BitFontSymbol, JsonObject, Throwable, Throwable> {

        @Override
        public @NotNull BitFontSymbol decode(@NotNull JsonObject encoded) throws Throwable {
            return GSON.fromJson(encoded, BitFontSymbol.class);
        }

        @Override
        public @NotNull JsonObject encode(@NotNull BitFontSymbol decoded) throws Throwable {
            return GSON.toJsonTree(decoded).getAsJsonObject();
        }
    }

}
