package net.theevilreaper.vulpes.api.meta;

import org.jetbrains.annotations.NotNull;
import org.jglrxavpok.hephaistos.nbt.NBT;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The class is the basic implementation of the {@link Metadata} interface.
 * It contains only the implementation for that method which can be implemented without the given context.
 * @version 1.0.0
 * @since 1.0.0
 * @author theEvilReaper
 */
public abstract non-sealed class BaseMetaData implements Metadata {

    private final Map<String, Object> data;

    /**
     * Creates a new instance from the {@link BaseMetaData} object.
     * @param withDefaults true to add defaults vales otherwise false
     */
    protected BaseMetaData(boolean withDefaults) {
        this.data = new HashMap<>();

        if (withDefaults) {
            this.initDefaults();
        }
    }

    /**
     * Init some default values to a {@link Metadata} implementation.
     * The method must be implemented by a subtype
     */
    abstract void initDefaults();

    /**
     * Returns an indicator if the metadata structure contains data.
     * @return true when it has some data otherwise false
     */
    public boolean hasMetaData() {
        return !data.isEmpty();
    }

    /**
     * Writes the given data from a class into a {@link NBT} object.
     * @return the {@link NBT} object with the content
     */
    @Override
    public @NotNull NBT toNBT() {
        return MetaDataWriter.writeData(this.data);
    }

    /**
     * Reads a content from an {@link NBTCompound} and put this into the map which stores the data
     * @param nbtCompound the object to read
     */
    @Override
    public void readNBT(@NotNull NBTCompound nbtCompound) {
        MetaDataWriter.readData(nbtCompound.asMapView(), this::addData);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseMetaData that = (BaseMetaData) o;
        return Objects.equals(getIdentifier(), that.getIdentifier());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdentifier());
    }

    /**
     * Add a new metadata entry to the object with a key as identifier.
     * @param key the key which is used as identifier for the data object
     * @param value the value to add
     * @return the added data value
     * @param <T> the return type for the data object
     */
    @Override
    public <T> T addData(@NotNull String key, T value) {
        return (T) this.data.put(key, value);
    }

    /**
     * Removes a metadata value entry by its corresponding key.
     * @param key the key to remove the entry
     * @return the removed value
     * @param <T> the return type for the data object
     */
    @Override
    public <T> T removeData(@NotNull String key) {
        return (T) this.data.remove(key);
    }

    /**
     * Returns the metadata value by a given key.
     * @param key the key to get the metadata
     * @return the data which matches with the id
     * @param <T> the return type for the data object
     */
    @Override
    public <T> T getMetaData(@NotNull String key) {
        return (T) this.data.get(key);
    }
}
