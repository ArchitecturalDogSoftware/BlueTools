package net.architecturaldog.bluetools.content.item;

import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.item.utility.CustomItem;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;

public class BlueToolsBlockItem extends BlockItem implements CustomItem {

    private final String path;

    public BlueToolsBlockItem(String path, Block block, Settings settings) {
        super(block, settings);

        this.path = path;
    }

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id(this.path);
    }

}
