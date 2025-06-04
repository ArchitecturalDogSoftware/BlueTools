package net.architecturaldog.bluetools.content.block.custom;

import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.block.BlueToolsBlockEntityTypes;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

import java.util.Arrays;

public class ForgeInterfaceBlockEntity extends BlockEntity {

    private static final AbstractBlock.ContextPredicate IS_FORGE_BLOCK =
        (state, world, pos) -> state.isOf(Blocks.BRICKS);
    private int maxVolume;

    public ForgeInterfaceBlockEntity(final BlockPos pos, final BlockState state)
    {
        super(BlueToolsBlockEntityTypes.FORGE_INTERFACE, pos, state);
    }

    public void tick(ServerWorld world, BlockView blockWorld, BlockPos pos, BlockState state) {
        int config = checkValidConfiguration(blockWorld, pos);
        world.setBlockState(
            pos,
            state.with(ForgeInterfaceBlock.ACTIVE, config > 0)
        );

        this.maxVolume = config;
    }

    private static int checkValidConfiguration(BlockView world, BlockPos pos) {
        BlockPos.Mutable checkPos = new BlockPos.Mutable();
        checkPos.set(pos);
        int[] check = checkValidStageOne(world, pos, checkPos);
        if (Arrays.equals(check, new int[] { 0, 0 }) || Arrays.stream(check).anyMatch(i -> i < 0)) {
            BlueTools.LOGGER.info("stage 1 failed");
            return 0;
        }
        int endPointOne = check[0];
        int endPointTwo = check[1];
        int length = 1 + endPointOne + endPointTwo;

        check = checkValidStageTwo(world, pos, checkPos, endPointOne, endPointTwo);
        if (Arrays.equals(check, new int[] { 0, 0 }) || Arrays.stream(check).anyMatch(i -> i < 0)) {
            BlueTools.LOGGER.info("stage 2 failed");
            return 0;
        }
        endPointOne = check[0];
        endPointTwo = check[1];
        length = 1 + endPointOne + endPointTwo;

        if (!checkValidStageThree(world, pos, checkPos, length, endPointOne)) {
            BlueTools.LOGGER.info("stage 3 failed");
            return 0;
        }
        int height = checkValidStageFour(world, pos, checkPos, endPointOne, endPointTwo);
        BlueTools.LOGGER.info("Stage 4 {}", height);
        if (height < 2) {
            BlueTools.LOGGER.info("stage 4 failed");
            return 0;
        }
        if (!checkValidStageFive(world, pos, checkPos, endPointTwo, height, length)) {
            BlueTools.LOGGER.info("stage 5 failed");
            return 0;
        }

        if (!checkValidStageSix(world, pos, checkPos, endPointTwo, length)) {
            return 0;
        }
        return length * length * height;
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
        for (int i = 0; i < 10; i++) {
            checkPos.move(direction1);
            if (IS_FORGE_BLOCK.test(world.getBlockState(checkPos), world, checkPos)) {
                d1++;
            } else {
                break;
            }
        }
        checkPos.set(pos);
        for (int i = 0; i < 10; i++) {
            checkPos.move(direction2);
            if (IS_FORGE_BLOCK.test(world.getBlockState(checkPos), world, checkPos)) {
                d2++;
            } else {
                break;
            }
        }
        int[] directions = new int[] { d1, d2 };
        BlueTools.LOGGER.info("Stage 1 {}", directions);
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
            BlueTools.LOGGER.info("Stage 2 {}", width);
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
        BlueTools.LOGGER.info("Stage 3");
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
        for (int y = 1; y < 10; y++) {
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
                BlueTools.LOGGER.info("front side 1 failed");
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
                BlueTools.LOGGER.info("side 1 failed");
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
                BlueTools.LOGGER.info("side 2 failed");
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
                BlueTools.LOGGER.info("back side 1 failed");
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
                        BlueTools.LOGGER.info("stage 5 failed on {}, {}, {}", x, y, z);
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
        BlueTools.LOGGER.info("Stage 5");
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
                    BlueTools.LOGGER.info("stage 6 failed on {}, {}", x, z);
                    return false;
                }
                checkPos.move(forward);
            }
            checkPos.move(side1);
            checkPos.move(back, length);
        }
        BlueTools.LOGGER.info("Stage 6");
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

}