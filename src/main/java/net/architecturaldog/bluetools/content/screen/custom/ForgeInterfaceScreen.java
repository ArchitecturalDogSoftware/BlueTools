package net.architecturaldog.bluetools.content.screen.custom;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ForgeInterfaceScreen<T extends ForgeInterfaceScreenHandler> extends HandledScreen<T>
    implements ScreenHandlerProvider<T>
{

    private final int titleX = 8;
    private final int titleY = 6;
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/gui/container/crafting_table.png");

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
            -12566464,
            false
        );
    }

    @Override
    protected void drawBackground(DrawContext context, float deltaTicks, int mouseX, int mouseY) {
        int i = this.x;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(
            RenderPipelines.GUI_TEXTURED,
            TEXTURE,
            i,
            j,
            0.0F,
            0.0F,
            this.backgroundWidth,
            this.backgroundHeight,
            256,
            256
        );
    }

}
