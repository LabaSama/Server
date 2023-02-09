package cn.Laba.Distance.module.modules.move;

import cn.Laba.Distance.api.EventHandler;
import cn.Laba.Distance.api.events.World.EventPreUpdate;
import cn.Laba.Distance.api.value.Numbers;
import cn.Laba.Distance.manager.ModuleManager;
import cn.Laba.Distance.module.Module;
import cn.Laba.Distance.module.ModuleType;
import cn.Laba.Distance.util.time.TimerUtil;
import cn.Laba.Distance.module.modules.player.Blink;

public class HytJump extends Module {
    public static Numbers<Double> motionY = new Numbers<>("MotionY", 2d, 1d, 8d, 1d);
    public static Numbers<Double> DisableDelay = new Numbers<>("DisableDelay", 300d, 0d, 1000d, 50d);
    public static TimerUtil timer = new TimerUtil();
    public HytJump(){
        super("HytJump",new String[]{"hytjump"}, ModuleType.Movement);
        addValues(motionY,DisableDelay);
    }
    @Override
    public void onEnable(){
        timer.reset();
        if (!ModuleManager.getModuleByClass(Blink.class).isEnabled())ModuleManager.getModuleByClass(Blink.class).setEnabled(true);
        mc.timer.timerSpeed = 2.0f;
        mc.thePlayer.motionY = motionY.getValue();
    }
    @EventHandler
    public void onUpdate(EventPreUpdate e){
        if(timer.hasReached(DisableDelay.getValue())){
            setEnabled(false);
        }
    }
    @Override
    public void onDisable(){
        mc.timer.timerSpeed = 1.0f;
        ModuleManager.getModuleByClass(Blink.class).setEnabled(false);
    }

}
