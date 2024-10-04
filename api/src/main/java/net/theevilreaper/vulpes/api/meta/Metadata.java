package net.theevilreaper.vulpes.api.meta;

import org.jetbrains.annotations.NotNull;
import org.jglrxavpok.hephaistos.nbt.NBT;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

/**
 * The interface contains the basic structure for the Metadata system which is used in the Dungeon and the Editor.
 * @version 1.0.0
 * @since  1.0.0
 * @author thEvilReaper
 */
public sealed interface Metadata permits BaseMetaData {

    String AMOUNT = "amount";
    String TICKING = "ticking";
    String META = "metaType";
    String POSITION = "position";
    /**
     * Writes the metadata structure into {@link NBT}.
     * @return the created nbt object
     */
    @NotNull NBT toNBT();

    /**
     * Reads the an {@link NBTCompound} to get the data from it.
     * @param nbtCompound the object to read
     */
    void readNBT(@NotNull NBTCompound nbtCompound);

    /**
     * Returns an indicator if the metadata structure contains data.
     * @return true when it has some data otherwise false
     */
    boolean hasMetaData();

    /**
     * Add a new metadata entry to the object with a key as identifier.
     * @param key the key which is used as identifier for the data object
     * @param value the value to add
     * @return the added data value
     * @param <T> the return type for the data object
     */
    <T> T addData(@NotNull String key, T value);

    /**
     * Removes a metadata value entry by its corresponding key.
     * @param key the key to remove the entry
     * @return the removed value
     * @param <T> the return type for the data object
     */
    <T> T removeData(@NotNull String key);

    /**
     * Returns the metadata value by a given key.
     * @param key the key to get the metadata
     * @return the data which matches with the id
     * @param <T> the return type for the data object
     */
    <T> T getMetaData(@NotNull String key);

    /**
     * Returns the identifier from the metadata implementation.
     * @return the given identifier
     */
    @NotNull String getIdentifier();
}