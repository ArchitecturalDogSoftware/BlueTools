package net.architecturaldog.bluetools.content.screen.custom;

import net.architecturaldog.bluetools.content.screen.BlueToolsScreenHandlerTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import org.jetbrains.annotations.ApiStatus;

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
        super(BlueToolsScreenHandlerTypes.FORGE_INTERFACE.getValue(), syncId);
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

    @ApiStatus.Internal
    public static final class Factory
        implements BlueToolsScreenHandlerTypes.Factory<ForgeInterfaceScreenHandler,
        ForgeInterfaceScreen<ForgeInterfaceScreenHandler>>
    {

        @Override
        public ForgeInterfaceScreenHandler create(final int syncId, final PlayerInventory playerInventory) {
            return new ForgeInterfaceScreenHandler(syncId, playerInventory);
        }

        @Environment(EnvType.CLIENT)
        @Override
        public ForgeInterfaceScreen<ForgeInterfaceScreenHandler> createScreen(
            final ForgeInterfaceScreenHandler handler,
            final PlayerInventory playerInventory,
            final Text title
        )
        {
            return new ForgeInterfaceScreen<>(handler, playerInventory, title);
        }

    }

}
