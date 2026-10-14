package net.onelitefeather.vulpes.item;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import net.minestom.server.codec.Result;
import net.minestom.server.codec.Transcoder;
import net.minestom.server.component.DataComponent;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

/**
 * Applies data components which are given as key and JSON pairs to an item stack builder.
 * <p>
 * The generated item entries use this helper so the generated code stays free of logic.
 */
@ApiStatus.Internal
final class ItemComponentDecoder {

    private ItemComponentDecoder() {
    }

    /**
     * Decodes every key and JSON pair with the Minestom codec of the component and sets the result on the builder.
     *
     * @param builder    the builder to apply the components to
     * @param keyAndJson alternating component keys and JSON values
     * @return the given builder
     * @throws IllegalArgumentException if the array has an odd length
     * @throws IllegalStateException    if a component is unknown or its value is not valid JSON or cannot be decoded
     */
    static ItemStack.Builder apply(ItemStack.Builder builder, String... keyAndJson) {
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
        Result<T> result = component.decode(Transcoder.JSON, json);
        if (result instanceof Result.Ok<T> ok) {
            builder.set(component, ok.value());
            return;
        }
        String reason = result instanceof Result.Error<T> error ? error.message() : "no value decoded";
        throw new IllegalStateException("Unable to decode data component " + key + ": " + reason);
    }
}
