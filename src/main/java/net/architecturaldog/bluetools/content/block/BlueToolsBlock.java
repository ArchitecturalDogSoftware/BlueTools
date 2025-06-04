package net.architecturaldog.bluetools.content.block;

import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.block.utility.CustomBlock;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class BlueToolsBlock extends Block implements CustomBlock {

    private final String path;

    public BlueToolsBlock(String path, Settings settings) {
        super(settings
            .registryKey(RegistryKey.of(RegistryKeys.BLOCK, BlueTools.id(path)))
            .lootTable(Optional.of(RegistryKey.of(RegistryKeys.LOOT_TABLE, BlueTools.id(path)))));

        this.path = path;
    }

    public Block asBlock() {
        return this;
    }

    public Identifier getLoaderId() {
        return BlueTools.id(this.path);
    }

}
