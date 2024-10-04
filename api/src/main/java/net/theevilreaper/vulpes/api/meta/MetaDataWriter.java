package net.theevilreaper.vulpes.api.meta;

import net.theevilreaper.vulpes.api.meta.functional.MetaDataAdder;
import org.jetbrains.annotations.NotNull;
import org.jglrxavpok.hephaistos.nbt.NBT;
import org.jglrxavpok.hephaistos.nbt.NBTByte;

import java.util.Map;

/**
 * The class handles the read and write logic of the metadata values from a region.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
public final class MetaDataWriter {

    private MetaDataWriter() {
        // Nothing to do here
    }

    /**
     * Reads the data from a {@link org.jglrxavpok.hephaistos.nbt.NBTCompound}.
     * @param data the map which contains the data
     * @param metaDataAdder the reference to add the data to a {@link Metadata} reference
     */
    static void readData(@NotNull Map<String, NBT> data, @NotNull MetaDataAdder metaDataAdder) {
        data.forEach((value, nbt) -> {
            if (nbt instanceof NBTByte nbtByte && nbtByte.getValue() <= 1) {
                metaDataAdder.addData(value, nbtByte.asBoolean());
            } else {
                metaDataAdder.addData(value, nbt.getValue());
            }
        });
    }

    /**
     * Writes the map which contains the data from a {@link Metadata} implementation into a {@link NBT} structure.
     * @param data the map with the data to write
     * @return the created {@link NBT} object
     */
    static @NotNull NBT writeData(@NotNull Map<String, Object> data) {
        return NBT.Compound(writer -> {
            for (var entry : data.entrySet()) {
                if (entry.getValue() instanceof Integer value) {
                    writer.put(entry.getKey(), NBT.Int(value));
                } else if (entry.getValue() instanceof Double value) {
                    writer.put(entry.getKey(), NBT.Double(value));
                } else if (entry.getValue() instanceof Float value) {
                    writer.put(entry.getKey(), NBT.Float(value));
                } else if (entry.getValue() instanceof Boolean value) {
                    writer.put(entry.getKey(), NBT.Boolean(value));
                } else if (entry.getValue() instanceof Byte value) {
                    writer.put(entry.getKey(), NBT.Byte(value));
                } else if (entry.getValue() instanceof Short value) {
                    writer.put(entry.getKey(), NBT.Short(value));
                } else if (entry.getValue() instanceof Long value) {
                    writer.put(entry.getKey(), NBT.Long(value));
                } else if (entry.getValue() instanceof String value) {
                    writer.put(entry.getKey(), NBT.String(value));
                } else {
                    throw new IllegalArgumentException("The given type " + entry.getValue().getClass().getSimpleName() + " is not supported");
                }
            }
        });
    }
}
