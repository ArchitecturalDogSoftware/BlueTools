package net.architecturaldog.bluetools.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.architecturaldog.bluetools.content.item.client.AbstractGeneratedItemModel;
import net.architecturaldog.bluetools.content.item.client.GeneratedPartItemModel;
import net.architecturaldog.bluetools.content.item.client.GeneratedToolItemModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.FabricBakedModelManager;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.ReferencedModelsCollector;
import net.minecraft.resource.ResourceReloader;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(BakedModelManager.class)
public abstract class BakedModelManagerMixin implements ResourceReloader, AutoCloseable, FabricBakedModelManager {

    @Unique
    private static void registerBuiltinModel(
        final @NotNull ReferencedModelsCollector collector,
        final @NotNull AbstractGeneratedItemModel model
    )
    {
        collector.addSpecialModel(model.getModelId(), model);
    }

    @ModifyExpressionValue(
        method = "collect",
        at = @At(
            value = "NEW",
            target = "(Ljava/util/Map;Lnet/minecraft/client/render/model/UnbakedModel;)" +
                "Lnet/minecraft/client/render/model/ReferencedModelsCollector;"
        )
    )
    private static @NotNull ReferencedModelsCollector addBuiltinModels(
        final @NotNull ReferencedModelsCollector collector
    )
    {
        BakedModelManagerMixin.registerBuiltinModel(collector, GeneratedPartItemModel.MODEL_6_LAYERS);
        BakedModelManagerMixin.registerBuiltinModel(collector, GeneratedToolItemModel.MODEL_6_LAYERS);
        BakedModelManagerMixin.registerBuiltinModel(collector, GeneratedToolItemModel.MODEL_12_LAYERS);
        BakedModelManagerMixin.registerBuiltinModel(collector, GeneratedToolItemModel.MODEL_18_LAYERS);
        BakedModelManagerMixin.registerBuiltinModel(collector, GeneratedToolItemModel.MODEL_24_LAYERS);
        BakedModelManagerMixin.registerBuiltinModel(collector, GeneratedToolItemModel.MODEL_30_LAYERS);
        BakedModelManagerMixin.registerBuiltinModel(collector, GeneratedToolItemModel.MODEL_36_LAYERS);

        return collector;
    }

}
