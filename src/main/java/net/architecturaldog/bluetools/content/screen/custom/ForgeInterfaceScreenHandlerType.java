package net.architecturaldog.bluetools.content.screen.custom;

import net.architecturaldog.bluetools.content.screen.BlueToolsScreenHandlerType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class ForgeInterfaceScreenHandlerType
    extends BlueToolsScreenHandlerType<ForgeInterfaceScreenHandler, ForgeInterfaceScreen<ForgeInterfaceScreenHandler>>
{

    public ForgeInterfaceScreenHandlerType(
        final String path
    )
    {
        super(ForgeInterfaceScreenHandler::new, path);
    }

    @Override
    protected ForgeInterfaceScreen<ForgeInterfaceScreenHandler> create(
        final ForgeInterfaceScreenHandler handler,
        final PlayerInventory playerInventory,
        final Text title
    )
    {
        return new ForgeInterfaceScreen<>(handler, playerInventory, title);
    }

}
