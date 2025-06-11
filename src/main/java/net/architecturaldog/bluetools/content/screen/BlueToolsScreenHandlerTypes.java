package net.architecturaldog.bluetools.content.screen;

import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.screen.custom.ForgeInterfaceScreenHandlerType;
import net.minecraft.util.Identifier;

public final class BlueToolsScreenHandlerTypes extends AutoLoader {

    public static final ForgeInterfaceScreenHandlerType FORGE_INTERFACE =
        new ForgeInterfaceScreenHandlerType("forge_interface");

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("screen_handler_types");
    }

}
