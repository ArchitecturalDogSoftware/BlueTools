package net.architecturaldog.bluetools.content.screen.custom;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class ForgeInterfaceScreen<T extends ForgeInterfaceScreenHandler> extends HandledScreen<T>
    implements ScreenHandlerProvider<T>
{

    private final int titleX = 8;
    private final int titleY = 6;

    public ForgeInterfaceScreen(
        final T handler,
        final PlayerInventory inventory,
        final Text title

    )
    {
        super(handler, inventory, title);
    }

    @Override
    protected void drawForeground(final DrawContext context, final int mouseX, final int mouseY) {
        context.drawText(
            this.textRenderer,
            String.format("Max Volume: %s", this.handler.getMaxVolume()),
            this.titleX,
            this.titleY,
            4210752,
            false
        );
    }

    @Override
    protected void drawBackground(
        final DrawContext context,
        final float deltaTicks,
        final int mouseX,
        final int mouseY
    )
    {

    }

}
