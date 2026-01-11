package net.architecturaldog.bluetools.content.item.client;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.GeneratedItemModel;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.client.render.model.json.ModelElementFace;
import net.minecraft.client.texture.SpriteContents;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.AxisRotation;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.*;
import java.util.function.IntFunction;

@Environment(EnvType.CLIENT)
public abstract class AbstractGeneratedItemModel implements UnbakedModel {

    private final @NotNull Identifier modelId;
    protected final @NotNull List<String> layers;
    protected final @NotNull ModelTextures.Textures textures;

    public AbstractGeneratedItemModel(final int layerCount, final @NotNull IntFunction<Identifier> layersToIdentifier) {
        assert layerCount > 0 : "At least one layer must be present";

        this.modelId = layersToIdentifier.apply(layerCount);
        this.layers = new ObjectArrayList<>(layerCount);

        for (int layer = 0; layer < layerCount; layer += 1) {
            layers.add("layer" + layer);
        }

        this.textures = new ModelTextures.Textures.Builder()
            .addTextureReference("particle", layerCount == 1 ? "layer0" : this.layers.get(layerCount - 2))
            .build();
    }

    public final @NotNull Identifier getModelId() {
        return this.modelId;
    }

    public final @NotNull List<String> getLayers() {
        return this.layers;
    }

    @Override
    public final ModelTextures.Textures textures() {
        return this.textures;
    }

    @Override
    public final @NotNull Geometry geometry() {
        return this::bakeGeometry;
    }

    @Override
    public @NotNull GuiLight guiLight() {
        return GuiLight.ITEM;
    }

    protected @NotNull BakedGeometry bakeGeometry(
        final @NotNull ModelTextures textures,
        final @NotNull Baker baker,
        final @NotNull ModelBakeSettings settings,
        final @NotNull SimpleModel model
    )
    {
        final @NotNull List<ModelElement> modelElements = new ObjectArrayList<>();

        for (int layerIndex = 0; layerIndex < this.getLayers().size(); layerIndex += 1) {
            final @NotNull String layer = this.getLayers().get(layerIndex);
            final @Nullable SpriteIdentifier spriteId = textures.get(layer);

            if (Objects.isNull(spriteId)) break;

            final @NotNull SpriteContents spriteContents = baker.getSpriteGetter().get(spriteId, model).getContents();

            modelElements.addAll(this.addLayerElements(layerIndex, layer, spriteContents));
        }

        return UnbakedGeometry.bakeGeometry(modelElements, textures, baker, settings, model);
    }

    protected @NotNull List<ModelElement> addLayerElements(
        final int tintIndex,
        final @NotNull String layerName,
        final @NotNull SpriteContents spriteContents
    )
    {
        final @NotNull Map<Direction, ModelElementFace> faceDirectionMap = Map
            .of(
                Direction.SOUTH,
                new ModelElementFace(null, tintIndex, layerName, GeneratedItemModel.FACING_SOUTH_UV, AxisRotation.R0),
                Direction.NORTH,
                new ModelElementFace(null, tintIndex, layerName, GeneratedItemModel.FACING_NORTH_UV, AxisRotation.R0)
            );

        final @NotNull List<ModelElement> modelElements = new ObjectArrayList<>();

        modelElements.add(new ModelElement(new Vector3f(0F, 0F, 7.5F), new Vector3f(16F, 16F, 8.5F), faceDirectionMap));
        modelElements.addAll(this.addSubComponents(spriteContents, layerName, tintIndex));

        return modelElements;
    }

    protected @NotNull List<ModelElement> addSubComponents(
        final @NotNull SpriteContents spriteContents,
        final @NotNull String layerName,
        final int tintIndex
    )
    {
        final float scaleX = 16F / spriteContents.getWidth();
        final float scaleY = 16F / spriteContents.getHeight();

        final @NotNull List<ModelElement> modelElements = new ObjectArrayList<>();

        for (final @NotNull GeneratedItemModel.class_12295 frame : this.getFrames(spriteContents)) {
            final float x = frame.x();
            final float y = frame.y();
            final GeneratedItemModel.Side side = frame.facing();

            float minU = x + 0.1F;
            float maxU = x + 1F - 0.1F;
            float minV, maxV;

            if (side.isVertical()) {
                minV = y + 0.1F;
                maxV = y + 1F - 0.1F;
            } else {
                minV = y + 1F - 0.1F;
                maxV = y + 0.1F;
            }

            float minX = x, maxX = x;
            float minY = y, maxY = y;

            switch (side) {
                case UP -> maxX += 1F;
                case DOWN -> {
                    maxX += 1F;
                    minY += 1F;
                    maxY += 1F;
                }
                case LEFT -> maxY += 1F;
                case RIGHT -> {
                    minX += 1F;
                    maxX += 1F;
                    maxY += 1F;
                }
            }

            minX *= scaleX;
            maxX *= scaleX;
            minY *= scaleY;
            maxY *= scaleY;
            minU *= scaleX;
            minV *= scaleX;
            maxU *= scaleY;
            maxV *= scaleY;
            minY = 16F - minY;
            maxY = 16F - maxY;

            final @NotNull ModelElementFace.UV uv = new ModelElementFace.UV(minU, minV, maxU, maxV);
            final @NotNull Map<Direction, ModelElementFace> map = Map
                .of(
                    side.getDirection(),
                    new ModelElementFace(null, tintIndex, layerName, uv, AxisRotation.R0)
                );

            modelElements.add(switch (side) {
                case UP -> new ModelElement(new Vector3f(minX, minY, 7.5F), new Vector3f(maxX, minY, 8.5F), map);
                case DOWN -> new ModelElement(new Vector3f(minX, maxY, 7.5F), new Vector3f(maxX, maxY, 8.5F), map);
                case LEFT -> new ModelElement(new Vector3f(minX, minY, 7.5F), new Vector3f(minX, maxY, 8.5F), map);
                case RIGHT -> new ModelElement(new Vector3f(maxX, minY, 7.5F), new Vector3f(maxX, maxY, 8.5F), map);
            });
        }

        return modelElements;
    }

    protected @NotNull Collection<GeneratedItemModel.class_12295> getFrames(final @NotNull SpriteContents sprite) {
        final @NotNull Set<GeneratedItemModel.class_12295> frames = new ObjectArraySet<>();

        sprite.getDistinctFrameCount().forEach(frameIndex -> {
            final int width = sprite.getWidth();
            final int height = sprite.getHeight();

            for (int y = 0; y < width; y += 1) {
                for (int x = 0; x < height; x += 1) {
                    final boolean isOpaque = !this.isTransparent(sprite, frameIndex, x, y, width, height);

                    if (!isOpaque) continue;

                    this.buildCube(GeneratedItemModel.Side.UP, frames, sprite, frameIndex, x, y, width, height);
                    this.buildCube(GeneratedItemModel.Side.DOWN, frames, sprite, frameIndex, x, y, width, height);
                    this.buildCube(GeneratedItemModel.Side.LEFT, frames, sprite, frameIndex, x, y, width, height);
                    this.buildCube(GeneratedItemModel.Side.RIGHT, frames, sprite, frameIndex, x, y, width, height);
                }
            }
        });

        return frames;
    }

    protected boolean isTransparent(
        final @NotNull SpriteContents spriteContents,
        final int frameIndex,
        final int x,
        final int y,
        final int width,
        final int height
    )
    {
        return x < 0 || y < 0 || x >= width || y >= height || spriteContents.isPixelTransparent(frameIndex, x, y);
    }

    protected void buildCube(
        final @NotNull GeneratedItemModel.Side side,
        final @NotNull Set<GeneratedItemModel.class_12295> frames,
        final @NotNull SpriteContents spriteContents,
        int frameIndex,
        int x,
        int y,
        int width,
        int height
    )
    {
        final int offsetX = x - side.getDirection().getOffsetX();
        final int offsetY = y - side.getDirection().getOffsetY();

        if (this.isTransparent(spriteContents, frameIndex, offsetX, offsetY, width, height)) {
            frames.add(new GeneratedItemModel.class_12295(side, x, y));
        }
    }

}
