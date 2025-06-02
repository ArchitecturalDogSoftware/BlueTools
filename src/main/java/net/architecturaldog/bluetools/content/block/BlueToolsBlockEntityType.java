package net.architecturaldog.bluetools.content.block;

import dev.jaxydog.lodestone.api.CommonLoaded;
import net.architecturaldog.bluetools.BlueTools;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.Set;

public class BlueToolsBlockEntityType<T extends BlockEntity> extends BlockEntityType<T> implements CommonLoaded {

    private final String path;

    public BlueToolsBlockEntityType(String path, BlockEntityFactory<? extends T> factory, Set<Block> blocks) {
        super(factory, blocks);

        this.path = path;

        assert !blocks.isEmpty();
    }

    @Override
    public void loadCommon() {
        Registry.register(Registries.BLOCK_ENTITY_TYPE, this.getLoaderId(), this);
    }

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id(this.path);
    }

}
