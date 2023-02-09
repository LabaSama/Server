package cn.Laba.Distance.module.modules.player;

import cn.Laba.Distance.api.EventHandler;
import cn.Laba.Distance.api.events.World.EventPreUpdate;
import cn.Laba.Distance.module.Module;
import cn.Laba.Distance.module.ModuleType;
import cn.Laba.Distance.ui.notifications.user.Notifications;
import cn.Laba.Distance.util.time.MSTimer;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;

public class AutoFish extends Module {
    MSTimer rodOutTimer = new MSTimer();
    public AutoFish() {
        super("AutoFish", new String[]{"autofish"}, ModuleType.Player);
    }
    public static boolean isCatchTiming;
    public static boolean needFishing = false;

    @Override
    public void onEnable(){
        needFishing = false;
        isCatchTiming = false;
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        if (mc.thePlayer.fishEntity != null) {
            rodOutTimer.reset();
            if (mc.thePlayer.inventory.currentItem != grabRodSlot()) {
                return;
            }
            if (isCatchTiming) {
                isCatchTiming = false;
                Notifications.getManager().post("AutoFish", "貌似有东西上钩了( >ω< )");
                mc.rightClickMouse();
                needFishing = true;
            }
        }else {
            if (needFishing && rodOutTimer.hasTimePassed(500)){
                needFishing = false;
                mc.rightClickMouse();
            }
        }
    }
    private int grabRodSlot() {
        for(int i2 = 0; i2 < 9; ++i2) {
            ItemStack itemStack = mc.thePlayer.inventory.mainInventory[i2];
            if(itemStack != null && itemStack.getItem() instanceof ItemFishingRod) {
                return i2;
            }
        }
        return -1;
    }
}
