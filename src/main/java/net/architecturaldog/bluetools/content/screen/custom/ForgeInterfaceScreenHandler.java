package net.architecturaldog.bluetools.content.screen.custom;

import net.architecturaldog.bluetools.content.screen.BlueToolsScreenHandlerTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class ForgeInterfaceScreenHandler extends ScreenHandler {

    private final Data data;

    public ForgeInterfaceScreenHandler(
        final int syncId,
        PlayerInventory playerInventory,
        Data data
    )
    {
        super(BlueToolsScreenHandlerTypes.FORGE_INTERFACE.getValue(), syncId);
        if (data == null) {
            this.data = new Data(0);
        } else {
            this.data = data;
        }
        this.addPlayerSlots(playerInventory, 8, 84);
    }

    public int getMaxVolume() {
        return this.data.maxVolume();
    }

    @Override
    public ItemStack quickMove(final PlayerEntity player, final int slot) {
        return null;
    }

    @Override
    public boolean canUse(final PlayerEntity player) {
        return true;
    }

    public record Data(int maxVolume) {

        public static final PacketCodec<? super RegistryByteBuf, Data> CODEC = PacketCodecs.INTEGER.xmap(
            Data::new,
            Data::maxVolume
        );

    }

    @ApiStatus.Internal
    public static final class Factory
        implements BlueToolsScreenHandlerTypes.ExtendedFactory<ForgeInterfaceScreenHandler,
        ForgeInterfaceScreen<ForgeInterfaceScreenHandler>, Data>
    {

        @Override
        public ForgeInterfaceScreenHandler create(final int syncId, final PlayerInventory inventory, final Data data) {
            return new ForgeInterfaceScreenHandler(syncId, inventory, data);
        }

        @Environment(EnvType.CLIENT)
        @Override
        public @NotNull ForgeInterfaceScreen<ForgeInterfaceScreenHandler> createScreen(
            final @NotNull ForgeInterfaceScreenHandler handler,
            final @NotNull PlayerInventory playerInventory,
            final @NotNull Text title
        )
        {
            return new ForgeInterfaceScreen<>(handler, playerInventory, title);
        }

    }

}
