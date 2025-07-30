package net.architecturaldog.bluetools.content.block.custom;

import net.architecturaldog.bluetools.content.block.BlueToolsBlockEntityTypes;
import net.architecturaldog.bluetools.content.screen.custom.ForgeInterfaceScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Optional;

public class ForgeInterfaceBlockEntity extends LockableContainerBlockEntity
    implements ExtendedScreenHandlerFactory<ForgeInterfaceScreenHandler.Data>
{

    public static final int MAX_SIDE_VALUE = 10;
    public static final int WIDTH_MAX_VALUE = MAX_SIDE_VALUE * 2 + 1;
    public static final int HEIGHT_MAX_VALUE = 10;
    private static final AbstractBlock.ContextPredicate IS_FORGE_BLOCK =
        (state, world, pos) -> state.isOf(Blocks.BRICKS);

    private int maxVolume;
    private int currentVolume;

    public ForgeInterfaceBlockEntity(final BlockPos pos, final BlockState state) {
        super(BlueToolsBlockEntityTypes.FORGE_INTERFACE.getValue(), pos, state);
    }

    public void tick(ServerWorld world, BlockView blockWorld, BlockPos pos, BlockState state) {
        Optional<Integer> config = checkValidConfiguration(blockWorld, pos);
        config.ifPresentOrElse(
            volume -> {
                world.setBlockState(
                    pos,
                    state.with(ForgeInterfaceBlock.VOLUME, volume).with(ForgeInterfaceBlock.ACTIVE, true)
                );
                this.maxVolume = volume;
            }, () -> world.setBlockState(
                pos,
                state.with(ForgeInterfaceBlock.VOLUME, 0).with(ForgeInterfaceBlock.ACTIVE, false)
            )
        );
    }

    private static Optional<Integer> checkValidConfiguration(BlockView world, BlockPos pos) {
        BlockPos.Mutable checkPos = new BlockPos.Mutable();
        checkPos.set(pos);
        int[] check = checkValidStageOne(world, pos, checkPos);
        if (Arrays.equals(check, new int[] { 0, 0 }) || Arrays.stream(check).anyMatch(i -> i < 0)) {
            return Optional.empty();
        }
        int endPointOne = check[0];
        int endPointTwo = check[1];
        int length = 1 + endPointOne + endPointTwo;

        check = checkValidStageTwo(world, pos, checkPos, endPointOne, endPointTwo);
        if (Arrays.equals(check, new int[] { 0, 0 }) || Arrays.stream(check).anyMatch(i -> i < 0)) {
            return Optional.empty();
        }
        endPointOne = check[0];
        endPointTwo = check[1];
        length = 1 + endPointOne + endPointTwo;

        if (!checkValidStageThree(world, pos, checkPos, length, endPointOne)) {
            return Optional.empty();
        }
        int height = checkValidStageFour(world, pos, checkPos, endPointOne, endPointTwo);
        if (height < 2) {
            return Optional.empty();
        }
        if (!checkValidStageFive(world, pos, checkPos, endPointTwo, height, length)) {
            return Optional.empty();
        }

        if (!checkValidStageSix(world, pos, checkPos, endPointTwo, length)) {
            return Optional.empty();
        }
        return Optional.of(length * length * height);
    }

    private static int[] checkValidStageOne(
        BlockView world,
        BlockPos pos,
        BlockPos.Mutable checkPos
    )
    {
        Direction[] findDirections = findDirections(world, pos);
        Direction direction1 = findDirections[0];
        Direction direction2 = findDirections[1];

        int d1 = 0;
        int d2 = 0;
        for (int i = 0; i < MAX_SIDE_VALUE; i++) {
            checkPos.move(direction1);
            if (IS_FORGE_BLOCK.test(world.getBlockState(checkPos), world, checkPos)) {
                d1++;
            } else {
                break;
            }
        }
        checkPos.set(pos);
        for (int i = 0; i < MAX_SIDE_VALUE; i++) {
            checkPos.move(direction2);
            if (IS_FORGE_BLOCK.test(world.getBlockState(checkPos), world, checkPos)) {
                d2++;
            } else {
                break;
            }
        }
        int[] directions = new int[] { d1, d2 };
        return directions;
    }

    private static int[] checkValidStageTwo(
        BlockView world,
        BlockPos pos,
        BlockPos.Mutable checkPos,
        int endPointOne,
        int endPointTwo
    )
    {
        Direction[] directions = findDirections(world, pos);
        Direction direction1 = directions[0];
        Direction direction2 = directions[1];
        Direction direction = directions[2];

        checkPos.set(pos);
        checkPos.move(direction1, endPointOne);
        checkPos.move(direction);
        if (IS_FORGE_BLOCK.test(world.getBlockState(checkPos), world, checkPos)) {
            endPointOne--;
        }
        checkPos.set(pos);
        checkPos.move(direction2, endPointTwo);
        checkPos.move(direction);
        if (IS_FORGE_BLOCK.test(world.getBlockState(checkPos), world, checkPos)) {
            endPointTwo--;
        }
        int stageOneLength = 1 + endPointOne + endPointTwo;

        if (checkForgeSide(world, checkPos, pos, direction, direction1, endPointOne, stageOneLength) &&
            checkForgeSide(world, checkPos, pos, direction, direction2, endPointTwo, stageOneLength))
        {
            int[] width = new int[] { endPointOne, endPointTwo };
            return width;
        } else {
            return new int[] { 0, 0 };
        }

    }

    private static boolean checkValidStageThree(
        BlockView world,
        BlockPos pos,
        BlockPos.Mutable checkPos,
        int stageTwoLength,
        int endPointOne
    )
    {
        Direction[] directions = findDirections(world, pos);
        Direction direction1 = directions[0];
        Direction direction2 = directions[1];
        Direction direction = directions[2];
        checkPos.set(pos);
        checkPos.move(direction1, endPointOne);
        checkPos.move(direction, stageTwoLength + 1);
        for (int i = 0; i < stageTwoLength; i++) {
            if (!IS_FORGE_BLOCK.test(world.getBlockState(checkPos), world, checkPos)) {
                return false;
            }
            checkPos.move(direction2);
        }
        return true;
    }

    private static int checkValidStageFour(
        BlockView world,
        BlockPos pos,
        BlockPos.Mutable checkPos,
        int endPointOne,
        int endPointTwo
    )
    {
        Direction[] directions = findDirections(world, pos);
        Direction side1 = directions[0];
        Direction side2 = directions[1];
        Direction forward = directions[2];
        int height = 1;
        checkPos.set(pos);
        for (int y = 1; y < HEIGHT_MAX_VALUE; y++) {
            checkPos.move(Direction.UP);
            if (!checkForgeSide(
                world,
                checkPos,
                pos.up(y),
                side1,
                side2,
                endPointTwo,
                (endPointOne + endPointTwo + 1)
            ))
            {
                return height;
            }
            if (!checkForgeSide(
                world,
                checkPos,
                pos.up(y),
                forward,
                side1,
                endPointOne,
                (endPointOne + endPointTwo + 1)
            ))
            {
                return height;
            }
            if (!checkForgeSide(
                world,
                checkPos,
                pos.up(y),
                forward,
                side2,
                endPointTwo,
                (endPointOne + endPointTwo + 1)
            ))
            {
                return height;
            }
            if (!checkForgeSide(
                world,
                checkPos,
                pos.up(y).offset(forward, endPointOne + endPointTwo + 2),
                side1,
                side2,
                endPointTwo,
                (endPointOne + endPointTwo + 1)
            ))
            {
                return height;
            }

            height++;
        }
        return height;
    }

    private static boolean checkValidStageFive(
        BlockView world,
        BlockPos pos,
        BlockPos.Mutable checkPos,
        int endPointTwo,
        int height,
        int length
    )
    {
        Direction[] directions = findDirections(world, pos);
        Direction side1 = directions[0];
        Direction side2 = directions[1];
        Direction forward = directions[2];
        Direction back = directions[3];

        checkPos.set(pos);
        checkPos.move(forward);
        checkPos.move(side2, endPointTwo);
        for (int y = 0; y <= height; y++) {
            for (int x = 0; x < length; x++) {
                for (int z = 0; z < length; z++) {
                    if (!world.getBlockState(checkPos).isAir()) {
                        return false;
                    }
                    checkPos.move(forward);
                }
                checkPos.move(side1);
                checkPos.move(back, length);
            }
            checkPos.set(pos);
            checkPos.move(Direction.UP, y);
            checkPos.move(forward);
            checkPos.move(side2, endPointTwo);

        }
        return true;
    }

    private static boolean checkValidStageSix(
        BlockView world,
        BlockPos pos,
        BlockPos.Mutable checkPos,

        int endPointTwo,
        int length
    )
    {
        Direction[] directions = findDirections(world, pos);
        Direction side1 = directions[0];
        Direction side2 = directions[1];
        Direction forward = directions[2];
        Direction back = directions[3];

        checkPos.set(pos);
        checkPos.move(Direction.DOWN);
        checkPos.move(forward);
        checkPos.move(side2, endPointTwo);
        for (int x = 0; x < length; x++) {
            for (int z = 0; z < length; z++) {
                if (!IS_FORGE_BLOCK.test(world.getBlockState(checkPos), world, checkPos)) {
                    return false;
                }
                checkPos.move(forward);
            }
            checkPos.move(side1);
            checkPos.move(back, length);
        }
        return true;
    }

    private static boolean checkForgeSide(
        BlockView world,
        BlockPos.Mutable checkPos,
        BlockPos pos,
        Direction forwardDirection,
        Direction sideDirection,
        int endPoint,
        int length
    )
    {
        checkPos.set(pos);
        checkPos.move(sideDirection, endPoint + 1);
        checkPos.move(forwardDirection);
        for (int i = 0; i < length; i++) {
            if (!IS_FORGE_BLOCK.test(world.getBlockState(checkPos), world, checkPos)) {
                return false;
            }
            checkPos.move(forwardDirection);
        }
        return true;
    }

    private static Direction[] findDirections(BlockView world, BlockPos pos) {
        Direction direction;
        Direction direction1;
        Direction direction2;
        Direction back;
        direction = switch (world.getBlockState(pos).get(ForgeInterfaceBlock.FACING)) {
            case NORTH -> Direction.SOUTH;
            case EAST -> Direction.WEST;
            case SOUTH -> Direction.NORTH;
            case WEST -> Direction.EAST;
            default -> Direction.NORTH;
        };
        back = direction.rotateClockwise(Direction.Axis.Y).rotateClockwise(Direction.Axis.Y);
        if (world.getBlockState(pos).get(ForgeInterfaceBlock.FACING) == Direction.NORTH ||
            world.getBlockState(pos).get(ForgeInterfaceBlock.FACING) == Direction.SOUTH)
        {
            direction1 = Direction.WEST;
            direction2 = Direction.EAST;
        } else {
            direction1 = Direction.NORTH;
            direction2 = Direction.SOUTH;
        }

        return new Direction[] { direction1, direction2, direction, back };
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable(BlueToolsBlockEntityTypes.FORGE_INTERFACE.getLoaderId().toTranslationKey("container"));
    }

    @Override
    protected DefaultedList<ItemStack> getHeldStacks() {
        return DefaultedList.ofSize(0, ItemStack.EMPTY);
    }

    @Override
    protected void setHeldStacks(final DefaultedList<ItemStack> inventory) {

    }

    @Override
    public @Nullable ScreenHandler createMenu(
        final int syncId,
        final PlayerInventory playerInventory,
        final PlayerEntity playerEntity
    )
    {
        if (playerEntity instanceof final ServerPlayerEntity serverPlayerEntity) {
            return new ForgeInterfaceScreenHandler(
                syncId,
                playerInventory,
                this.getScreenOpeningData(serverPlayerEntity)
            );
        } else {
            return new ForgeInterfaceScreenHandler(syncId, playerInventory, null);
        }
    }

    @Override
    protected ScreenHandler createScreenHandler(final int syncId, final PlayerInventory playerInventory) {
        if (playerInventory.player instanceof final ServerPlayerEntity serverPlayerEntity) {
            return new ForgeInterfaceScreenHandler(
                syncId,
                playerInventory,
                this.getScreenOpeningData(serverPlayerEntity)
            );
        }
        return new ForgeInterfaceScreenHandler(syncId, playerInventory, null);
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public ForgeInterfaceScreenHandler.Data getScreenOpeningData(final ServerPlayerEntity serverPlayerEntity) {
        return new ForgeInterfaceScreenHandler.Data(this.maxVolume);
    }

}