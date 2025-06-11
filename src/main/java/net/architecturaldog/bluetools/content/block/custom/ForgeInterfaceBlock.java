package net.architecturaldog.bluetools.content.block.custom;

import com.mojang.serialization.MapCodec;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.block.BlueToolsBlockEntityTypes;
import net.architecturaldog.bluetools.content.block.utility.CustomBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ForgeInterfaceBlock extends BlockWithEntity implements CustomBlock {

    private final String path;
    public static final EnumProperty<Direction> FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty ACTIVE = BooleanProperty.of("active");
    public static final IntProperty VOLUME = IntProperty.of(
        "volume",
        0,
        (int) Math.pow(ForgeInterfaceBlockEntity.WIDTH_MAX_VALUE, 2) * ForgeInterfaceBlockEntity.HEIGHT_MAX_VALUE
    );

    private final MapCodec<ForgeInterfaceBlock> codec = createCodec(settings -> new ForgeInterfaceBlock(
        this
            .getLoaderId()
            .getPath(),
        settings
    ));

    public ForgeInterfaceBlock(String path, Settings settings) {
        super(settings.registryKey(RegistryKey.of(RegistryKeys.BLOCK, BlueTools.id(path)))
            .lootTable(Optional.of(RegistryKey.of(RegistryKeys.LOOT_TABLE, BlueTools.id(path)))));

        this.path = path;

        this.setDefaultState(this.getDefaultState().with(ACTIVE, false).with(FACING, Direction.NORTH).with(VOLUME, 0));
    }

    @Override
    public @Nullable BlockState getPlacementState(final ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
        final World world,
        final BlockState state,
        final BlockEntityType<T> type
    )
    {
        if (world instanceof ServerWorld serverWorld) {
            return validateTicker(
                type,
                BlueToolsBlockEntityTypes.FORGE_INTERFACE,
                (worldx, pos, state2, blockEntity) -> blockEntity.tick(
                    serverWorld,
                    worldx,
                    pos,
                    state
                )
            );
        }
        return super.getTicker(world, state, type);
    }

    @Override
    protected ActionResult onUse(
        final BlockState state,
        final World world,
        final BlockPos pos,
        final PlayerEntity player,
        final BlockHitResult hit
    )
    {
        if (!state.get(ACTIVE)) {
            return ActionResult.PASS;
        }
        if (!world.isClient) {
            if (world.getBlockEntity(pos) instanceof ForgeInterfaceBlockEntity block) {
                player.openHandledScreen(block);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE, FACING, VOLUME);
    }

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id(this.path);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return this.codec;
    }

    @Override
    public Block asBlock() {
        return this;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(final BlockPos pos, final BlockState state) {
        return new ForgeInterfaceBlockEntity(pos, state);
    }

}
