package net.architecturaldog.bluetools.content.item;

import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.item.utility.CustomItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class BlueToolsItem extends Item implements CustomItem {

    private final String path;

    public BlueToolsItem(String path, Settings settings) {
        super(settings);

        this.path = path;
    }

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id(this.path);
    }

}
