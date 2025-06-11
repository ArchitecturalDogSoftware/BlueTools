package net.architecturaldog.bluetools.content.screen.custom;

import net.architecturaldog.bluetools.content.screen.BlueToolsScreenHandlerTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;

public class ForgeInterfaceScreenHandler extends ScreenHandler {

    PropertyDelegate propertyDelegate;

    public ForgeInterfaceScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new ArrayPropertyDelegate(2));
    }

    public ForgeInterfaceScreenHandler(
        final int syncId,
        PlayerInventory playerInventory,
        PropertyDelegate propertyDelegate
    )
    {
        super(BlueToolsScreenHandlerTypes.FORGE_INTERFACE, syncId);
        this.propertyDelegate = propertyDelegate;
        this.addProperties(propertyDelegate);
    }

    public int getCurrentVolume() {
        return this.propertyDelegate.get(1);
    }

    public int getMaxVolume() {
        return this.propertyDelegate.get(0);
    }

    @Override
    public ItemStack quickMove(final PlayerEntity player, final int slot) {
        return null;
    }

    @Override
    public boolean canUse(final PlayerEntity player) {
        return true;
    }

}
