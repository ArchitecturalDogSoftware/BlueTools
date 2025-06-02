package net.architecturaldog.bluetools.content.block;

import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.block.utility.CustomBlock;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;

public class BlueToolsBlock extends Block implements CustomBlock {

    private final String path;

    public BlueToolsBlock(String path, Settings settings) {
        super(settings);

        this.path = path;
    }

    public Block asBlock() {
        return this;
    }

    public Identifier getLoaderId() {
        return BlueTools.id(this.path);
    }

}
