package cn.Laba.Distance.module.modules.world;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class BlockData {
    public BlockPos position;
    public EnumFacing face;

    public BlockData(BlockPos position, EnumFacing face) {
        this.position = position;
        this.face = face;
    }
}
