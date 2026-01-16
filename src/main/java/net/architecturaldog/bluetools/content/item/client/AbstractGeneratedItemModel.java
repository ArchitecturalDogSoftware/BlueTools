package net.architecturaldog.bluetools.content.item.client;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.IntFunction;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.BakedGeometry;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.Geometry;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelTextures;
import net.minecraft.client.render.model.SimpleModel;
import net.minecraft.client.render.model.UnbakedGeometry;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.GeneratedItemModel;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.client.render.model.json.ModelElementFace;
import net.minecraft.client.texture.SpriteContents;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.AxisRotation;
import net.minecraft.util.math.Direction;

@Environment(EnvType.CLIENT)
public abstract class AbstractGeneratedItemModel implements UnbakedModel {

    private final Identifier modelId;
    protected final List<String> layers;
    protected final ModelTextures.Textures textures;

    public AbstractGeneratedItemModel(final int layerCount, final IntFunction<Identifier> layersToIdentifier) {
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

    public final Identifier getModelId() {
        return this.modelId;
    }

    public final List<String> getLayers() {
        return this.layers;
    }

    @Override
    public final ModelTextures.Textures textures() {
        return this.textures;
    }

    @Override
    public final Geometry geometry() {
        return this::bakeGeometry;
    }

    @Override
    public GuiLight guiLight() {
        return GuiLight.ITEM;
    }

    protected BakedGeometry bakeGeometry(
        final ModelTextures textures,
        final Baker baker,
        final ModelBakeSettings settings,
        final SimpleModel model
    )
    {
        final List<ModelElement> modelElements = new ObjectArrayList<>();

        for (int layerIndex = 0; layerIndex < this.getLayers().size(); layerIndex += 1) {
            final String layer = this.getLayers().get(layerIndex);
            final @Nullable SpriteIdentifier spriteId = textures.get(layer);

            if (Objects.isNull(spriteId)) break;

            final SpriteContents spriteContents = baker.getSpriteGetter().get(spriteId, model).getContents();

            modelElements.addAll(this.addLayerElements(layerIndex, layer, spriteContents));
        }

        return UnbakedGeometry.bakeGeometry(modelElements, textures, baker, settings, model);
    }

    protected List<ModelElement> addLayerElements(
        final int tintIndex,
        final String layerName,
        final SpriteContents spriteContents
    )
    {
        final Map<Direction, ModelElementFace> faceDirectionMap = Map
            .of(
                Direction.SOUTH,
                new ModelElementFace(null, tintIndex, layerName, GeneratedItemModel.FACING_SOUTH_UV, AxisRotation.R0),
                Direction.NORTH,
                new ModelElementFace(null, tintIndex, layerName, GeneratedItemModel.FACING_NORTH_UV, AxisRotation.R0)
            );

        final List<ModelElement> modelElements = new ObjectArrayList<>();

        modelElements.add(new ModelElement(new Vector3f(0F, 0F, 7.5F), new Vector3f(16F, 16F, 8.5F), faceDirectionMap));
        modelElements.addAll(this.addSubComponents(spriteContents, layerName, tintIndex));

        return modelElements;
    }

    protected List<ModelElement> addSubComponents(
        final SpriteContents spriteContents,
        final String layerName,
        final int tintIndex
    )
    {
        final float scaleX = 16F / spriteContents.getWidth();
        final float scaleY = 16F / spriteContents.getHeight();

        final List<ModelElement> modelElements = new ObjectArrayList<>();

        for (final GeneratedItemModel.class_12295 frame : this.getFrames(spriteContents)) {
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

            final ModelElementFace.UV uv = new ModelElementFace.UV(minU, minV, maxU, maxV);
            final Map<Direction, ModelElementFace> map = Map
                .of(side.getDirection(), new ModelElementFace(null, tintIndex, layerName, uv, AxisRotation.R0));

            modelElements.add(switch (side) {
                case UP -> new ModelElement(new Vector3f(minX, minY, 7.5F), new Vector3f(maxX, minY, 8.5F), map);
                case DOWN -> new ModelElement(new Vector3f(minX, maxY, 7.5F), new Vector3f(maxX, maxY, 8.5F), map);
                case LEFT -> new ModelElement(new Vector3f(minX, minY, 7.5F), new Vector3f(minX, maxY, 8.5F), map);
                case RIGHT -> new ModelElement(new Vector3f(maxX, minY, 7.5F), new Vector3f(maxX, maxY, 8.5F), map);
            });
        }

        return modelElements;
    }

    protected Collection<GeneratedItemModel.class_12295> getFrames(final SpriteContents sprite) {
        final Set<GeneratedItemModel.class_12295> frames = new ObjectArraySet<>();

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
        final SpriteContents spriteContents,
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
        final GeneratedItemModel.Side side,
        final Set<GeneratedItemModel.class_12295> frames,
        final SpriteContents spriteContents,
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
