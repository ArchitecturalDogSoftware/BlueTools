package net.architecturaldog.bluetools.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.architecturaldog.bluetools.content.item.client.GeneratedPartItemModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.FabricBakedModelManager;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.ReferencedModelsCollector;
import net.minecraft.resource.ResourceReloader;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(BakedModelManager.class)
public abstract class BakedModelManagerMixin implements ResourceReloader, AutoCloseable, FabricBakedModelManager {

    @ModifyExpressionValue(
        method = "collect",
        at = @At(
            value = "NEW",
            target = "(Ljava/util/Map;Lnet/minecraft/client/render/model/UnbakedModel;)" +
                "Lnet/minecraft/client/render/model/ReferencedModelsCollector;"
        )
    )
    private static @NotNull ReferencedModelsCollector addBuiltinModels(
        final @NotNull ReferencedModelsCollector original
    )
    {
        original.addSpecialModel(
            GeneratedPartItemModel.MODEL_6_LAYERS.getModelId(),
            GeneratedPartItemModel.MODEL_6_LAYERS
        );

        return original;
    }

}
