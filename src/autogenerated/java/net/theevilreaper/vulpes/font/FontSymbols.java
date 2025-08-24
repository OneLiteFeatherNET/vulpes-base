package net.theevilreaper.vulpes.font;

import net.kyori.adventure.key.Key;

public sealed interface FontSymbols permits FontSymbol {

    FontSymbol TEST = BitFontSymbol.get(Key.key("test", "test"));
}
