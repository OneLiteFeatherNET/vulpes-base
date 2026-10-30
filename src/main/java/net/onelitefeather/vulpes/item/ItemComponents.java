package net.onelitefeather.vulpes.item;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import net.minestom.server.MinecraftServer;
import net.minestom.server.codec.Result;
import net.minestom.server.codec.Transcoder;
import net.minestom.server.component.DataComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.registry.RegistryTranscoder;

/**
 * Applies vanilla data components, given as key and JSON pairs, to an item stack builder.
 * <p>
 * Generated items pass their vanilla component JSON straight through this helper, so the generated code needs
 * no logic of its own. The key is the component id, for example {@code minecraft:lore}, and the value is the
 * JSON that the component codec of Minestom decodes.
 *
 * @since 0.5.0
 */
public final class ItemComponents {

    private ItemComponents() {
    }

    /**
     * Decodes every key and JSON pair with the Minestom codec of the component and sets the result on the builder.
     * The server must be initialized with {@code MinecraftServer.init()} so registry-backed components can be decoded.
     *
     * @param builder    the builder to apply the components to
     * @param keyAndJson alternating component keys and JSON values
     * @return the given builder
     * @throws IllegalArgumentException if the array has an odd length
     * @throws IllegalStateException    if a component is unknown or its value is not valid JSON or cannot be decoded
     */
    public static ItemStack.Builder apply(ItemStack.Builder builder, String... keyAndJson) {
        if (keyAndJson.length % 2 != 0) {
            throw new IllegalArgumentException("Expected key and JSON value pairs but got " + keyAndJson.length + " arguments");
        }
        for (int i = 0; i < keyAndJson.length; i += 2) {
            String key = keyAndJson[i];
            DataComponent<?> component = DataComponent.fromKey(key);
            if (component == null) {
                throw new IllegalStateException("Unknown data component: " + key);
            }
            JsonElement json;
            try {
                json = JsonParser.parseString(keyAndJson[i + 1]);
            } catch (JsonParseException e) {
                throw new IllegalStateException("Invalid JSON for data component " + key + ": " + e.getMessage(), e);
            }
            applyComponent(builder, component, key, json);
        }
        return builder;
    }

    private static <T> void applyComponent(ItemStack.Builder builder, DataComponent<T> component, String key, JsonElement json) {
        Transcoder<JsonElement> transcoder = new RegistryTranscoder<>(Transcoder.JSON, MinecraftServer.getRegistries());
        Result<T> result = component.decode(transcoder, json);
        if (result instanceof Result.Ok<T> ok) {
            builder.set(component, ok.value());
            return;
        }
        String reason = result instanceof Result.Error<T> error ? error.message() : "no value decoded";
        throw new IllegalStateException("Unable to decode data component " + key + ": " + reason);
    }
}
