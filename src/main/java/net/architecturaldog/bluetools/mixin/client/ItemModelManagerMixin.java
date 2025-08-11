package net.architecturaldog.bluetools.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.architecturaldog.bluetools.content.component.BlueToolsComponentTypes;
import net.architecturaldog.bluetools.content.component.PartComponent;
import net.architecturaldog.bluetools.content.component.ToolComponent;
import net.architecturaldog.bluetools.content.item.PartItem;
import net.architecturaldog.bluetools.content.item.ToolItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ItemAsset;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
@Mixin(ItemModelManager.class)
public abstract class ItemModelManagerMixin {

    @Shadow
    @Final
    private Function<Identifier, ItemAsset.Properties> propertiesGetter;

    @Shadow
    @Final
    private Function<Identifier, ItemModel> modelGetter;

    @Unique
    private @NotNull Identifier getModelIdentifier(final @NotNull PartComponent component) {
        return Identifier.of(
            component.partEntry().key().getValue().getNamespace(),
            "part/" + component.partEntry().key().getValue().getPath()
        );
    }

    @Unique
    private @NotNull Identifier getModelIdentifier(final @NotNull ToolComponent component) {
        return Identifier.of(
            component.toolEntry().key().getValue().getNamespace(),
            "tool/" + component.toolEntry().key().getValue().getPath()
        );
    }

    @ModifyExpressionValue(
        method = "update",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;get(Lnet/minecraft/component/ComponentType;)Ljava/lang/Object;"
        )
    )
    private @Nullable Object overridePartUpdate(
        final @Nullable Object original,
        final @NotNull @Local(argsOnly = true) ItemRenderState renderState,
        final @NotNull @Local(argsOnly = true) ItemStack stack,
        final @NotNull @Local(argsOnly = true) ItemDisplayContext context,
        final @Nullable @Local(argsOnly = true) World world,
        final @Nullable @Local(argsOnly = true) LivingEntity user,
        final @Local(argsOnly = true) int seed
    )
    {
        final @Nullable PartComponent partComponent;
        final @Nullable ToolComponent toolComponent;

        if (stack.getItem() instanceof PartItem &&
            Objects.nonNull(partComponent = stack.get(BlueToolsComponentTypes.PART.getValue()))
        )
        {
            final @NotNull Identifier modelIdentifier = this.getModelIdentifier(partComponent);
            final @NotNull ItemModelManager self = (ItemModelManager) (Object) this;
            final @Nullable ClientWorld clientWorld = world instanceof ClientWorld w ? w : null;

            renderState.setOversizedInGui(this.propertiesGetter.apply(modelIdentifier).oversizedInGui());
            this.modelGetter.apply(modelIdentifier).update(renderState, stack, self, context, clientWorld, user, seed);

            return null;
        } else if (stack.getItem() instanceof ToolItem &&
            Objects.nonNull(toolComponent = stack.get(BlueToolsComponentTypes.TOOL.getValue()))
        )
        {
            final @NotNull Identifier modelIdentifier = this.getModelIdentifier(toolComponent);
            final @NotNull ItemModelManager self = (ItemModelManager) (Object) this;
            final @Nullable ClientWorld clientWorld = world instanceof ClientWorld w ? w : null;

            renderState.setOversizedInGui(this.propertiesGetter.apply(modelIdentifier).oversizedInGui());
            this.modelGetter.apply(modelIdentifier).update(renderState, stack, self, context, clientWorld, user, seed);

            return null;
        }

        return original;
    }

    @ModifyReturnValue(method = "hasHandAnimationOnSwap", at = @At("RETURN"))
    private boolean overridePartHasHandAnimationOnSwap(
        final boolean original,
        final @NotNull @Local(argsOnly = true) ItemStack stack,
        final @Nullable @Local Identifier identifier
    )
    {
        if (stack.getItem() instanceof PartItem) {
            final @Nullable PartComponent component = stack.get(BlueToolsComponentTypes.PART.getValue());

            if (Objects.isNull(component)) return original;

            return this.propertiesGetter.apply(this.getModelIdentifier(component)).handAnimationOnSwap();
        } else if (stack.getItem() instanceof ToolItem) {
            final @Nullable ToolComponent component = stack.get(BlueToolsComponentTypes.TOOL.getValue());

            if (Objects.isNull(component)) return original;

            return this.propertiesGetter.apply(this.getModelIdentifier(component)).handAnimationOnSwap();
        }

        return original;
    }

}
