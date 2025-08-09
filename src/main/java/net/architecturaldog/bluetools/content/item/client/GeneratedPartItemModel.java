package net.architecturaldog.bluetools.content.item.client;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.architecturaldog.bluetools.BlueTools;
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

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public final class GeneratedPartItemModel implements UnbakedModel {

    public static final @NotNull GeneratedPartItemModel MODEL_6_LAYERS = new GeneratedPartItemModel(6);

    private final @NotNull Identifier modelId;
    private final @NotNull List<String> layers;
    private final @NotNull ModelTextures.Textures textures;

    public GeneratedPartItemModel(final int layerCount) {
        assert layerCount > 0 : "At least one layer must be present";

        this.modelId = BlueTools.id("builtin/generated/part_%d_layers".formatted(layerCount));
        this.layers = new ObjectArrayList<>(layerCount);

        for (int layer = 0; layer < layerCount; layer += 1) {
            layers.add("layer" + layer);
        }

        this.textures = new ModelTextures.Textures.Builder().addTextureReference(
            "particle",
            layerCount == 1 ? "layer0" : this.layers.get(layerCount - 2)
        ).build();
    }

    public @NotNull Identifier getModelId() {
        return this.modelId;
    }

    public @NotNull List<String> getLayers() {
        return this.layers;
    }

    @Override
    public ModelTextures.Textures textures() {
        return this.textures;
    }

    @Override
    public @NotNull Geometry geometry() {
        return this::bakeGeometry;
    }

    @Override
    public @NotNull GuiLight guiLight() {
        return GuiLight.ITEM;
    }

    private @NotNull BakedGeometry bakeGeometry(
        final @NotNull ModelTextures textures,
        final @NotNull Baker baker,
        final @NotNull ModelBakeSettings settings,
        final @NotNull SimpleModel model
    )
    {
        return this.bakeGeometry(textures, baker.getSpriteGetter(), settings, model);
    }

    private @NotNull BakedGeometry bakeGeometry(
        final @NotNull ModelTextures textures,
        final @NotNull ErrorCollectingSpriteGetter spriteGetter,
        final @NotNull ModelBakeSettings settings,
        final @NotNull SimpleModel model
    )
    {
        final @NotNull List<ModelElement> modelElements = new ObjectArrayList<>();

        for (int layerIndex = 0; layerIndex < this.getLayers().size(); layerIndex += 1) {
            final @NotNull String layer = this.getLayers().get(layerIndex);
            final @Nullable SpriteIdentifier spriteId = textures.get(layer);

            if (Objects.isNull(spriteId)) break;

            final @NotNull SpriteContents spriteContents = spriteGetter.get(spriteId, model).getContents();

            modelElements.addAll(this.addLayerElements(layerIndex, layer, spriteContents));
        }

        return UnbakedGeometry.bakeGeometry(modelElements, textures, spriteGetter, settings, model);
    }

    private @NotNull List<ModelElement> addLayerElements(
        final int tintIndex,
        final @NotNull String layerName,
        final @NotNull SpriteContents spriteContents
    )
    {
        final @NotNull Map<Direction, ModelElementFace> faceDirectionMap = Map.of(
            Direction.SOUTH,
            new ModelElementFace(null, tintIndex, layerName, GeneratedItemModel.FACING_SOUTH_UV, AxisRotation.R0),
            Direction.NORTH,
            new ModelElementFace(null, tintIndex, layerName, GeneratedItemModel.FACING_NORTH_UV, AxisRotation.R0)
        );

        final @NotNull List<ModelElement> modelElements = new ObjectArrayList<>();

        modelElements.add(new ModelElement(new Vector3f(0F, 0F, 7.5F), new Vector3f(16F, 16F, 8.5F), faceDirectionMap));
        modelElements.addAll(this.addSubComponents(tintIndex, layerName, spriteContents));

        return modelElements;
    }

    private @NotNull List<ModelElement> addSubComponents(
        final int tintIndex,
        final @NotNull String layerName,
        final @NotNull SpriteContents spriteContents
    )
    {
        final @NotNull List<ModelElement> modelElements = new ObjectArrayList<>();

        for (final @NotNull GeneratedItemModel.Frame frame : this.getFrames(spriteContents)) {
            final GeneratedItemModel.Side side = frame.getSide();

            final float scaleWidth = 16F / spriteContents.getWidth();
            final float scaleHeight = 16F / spriteContents.getHeight();
            final float min = frame.getMin();
            final float max = frame.getMax();
            final float level = frame.getLevel();

            float minX = 0F, minY = 0F, maxX = 0F, maxY = 0F, minU = 0F, maxU = 0F, minV = 0F, maxV = 0F;

            switch (side) {
                case UP -> {
                    minX = minU = min;
                    maxX = maxU = max + 1F;
                    minY = maxY = minV = level;
                    maxV = level + 1F;
                }
                case DOWN -> {
                    minX = minU = min;
                    maxX = maxU = max + 1F;
                    minV = level;
                    minY = maxY = maxV = level + 1F;
                }
                case LEFT -> {
                    minY = maxV = min;
                    maxY = minV = max + 1F;
                    minX = maxX = minU = level;
                    maxU = level + 1F;
                }
                case RIGHT -> {
                    minY = maxV = min;
                    maxY = minV = max + 1F;
                    minU = level;
                    minX = maxX = maxU = level + 1F;
                }
            }

            minX *= scaleWidth;
            maxX *= scaleWidth;
            minY *= scaleHeight;
            maxY *= scaleHeight;
            minY = 16F - minY;
            maxY = 16F - maxY;
            minU *= scaleWidth;
            maxU *= scaleWidth;
            minV *= scaleHeight;
            maxV *= scaleHeight;

            final @NotNull ModelElementFace.UV uv = new ModelElementFace.UV(minU, minV, maxU, maxV);
            final @NotNull Map<Direction, ModelElementFace> faces = Map.of(
                side.getDirection(),
                new ModelElementFace(null, tintIndex, layerName, uv, AxisRotation.R0)
            );

            modelElements.add(switch (side) {
                case UP -> new ModelElement(new Vector3f(minX, minY, 7.5F), new Vector3f(maxX, minY, 8.5F), faces);
                case DOWN -> new ModelElement(new Vector3f(minX, maxY, 7.5F), new Vector3f(maxX, maxY, 8.5F), faces);
                case LEFT -> new ModelElement(new Vector3f(minX, minY, 7.5F), new Vector3f(minX, maxY, 8.5F), faces);
                case RIGHT -> new ModelElement(new Vector3f(maxX, minY, 7.5F), new Vector3f(maxX, maxY, 8.5F), faces);
            });
        }

        return modelElements;
    }

    private @NotNull List<GeneratedItemModel.Frame> getFrames(final @NotNull SpriteContents spriteContents) {
        final @NotNull List<GeneratedItemModel.Frame> frames = new ObjectArrayList<>();

        spriteContents.getDistinctFrameCount().forEach(frameIndex -> {
            for (int y = 0; y < spriteContents.getWidth(); y += 1) {
                for (int x = 0; x < spriteContents.getHeight(); x += 1) {
                    final boolean isOpaque = !this.isTransparent(spriteContents, frameIndex, x, y);

                    this.buildCube(frames, GeneratedItemModel.Side.UP, spriteContents, frameIndex, x, y, isOpaque);
                    this.buildCube(frames, GeneratedItemModel.Side.DOWN, spriteContents, frameIndex, x, y, isOpaque);
                    this.buildCube(frames, GeneratedItemModel.Side.LEFT, spriteContents, frameIndex, x, y, isOpaque);
                    this.buildCube(frames, GeneratedItemModel.Side.RIGHT, spriteContents, frameIndex, x, y, isOpaque);
                }
            }
        });

        return frames;
    }

    private boolean isTransparent(
        final @NotNull SpriteContents spriteContents,
        final int frameIndex,
        final int x,
        final int y
    )
    {
        return x <= 0 || y <= 0 || x >= spriteContents.getWidth() || y >= spriteContents.getHeight() ||
            spriteContents.isPixelTransparent(frameIndex, x, y);
    }

    private void buildCube(
        final @NotNull List<GeneratedItemModel.Frame> frames,
        final @NotNull GeneratedItemModel.Side side,
        final @NotNull SpriteContents spriteContents,
        final int frameIndex,
        final int x,
        final int y,
        final boolean isOpaque
    )
    {
        if (isOpaque && this.isTransparent(spriteContents, frameIndex, x + side.getOffsetX(), y + side.getOffsetY())) {
            this.buildCube(frames, side, x, y);
        }
    }

    private void buildCube(
        final @NotNull List<GeneratedItemModel.Frame> frames,
        final @NotNull GeneratedItemModel.Side side,
        final int x,
        final int y
    )
    {
        final int level = side.isVertical() ? y : x;
        final int cross = side.isVertical() ? x : y;

        frames.stream().filter(
            existing -> existing.getSide().equals(side) && existing.getLevel() == level
        ).findFirst().ifPresentOrElse(
            frame -> frame.expand(cross),
            () -> frames.add(new GeneratedItemModel.Frame(side, cross, level))
        );
    }

}
