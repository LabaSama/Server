/*
 * Decompiled with CFR 0.150.
 */
package cn.Laba.Distance.util.entity.entitycheck.checks;

import cn.Laba.Distance.util.entity.entitycheck.ICheck;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public final class WallCheck
        implements ICheck {
    @Override
    public boolean validate(Entity entity) {
        return Minecraft.getMinecraft().thePlayer.canEntityBeSeen(entity);
    }
}

