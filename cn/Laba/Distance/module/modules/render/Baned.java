package cn.Laba.Distance.module.modules.render;

import cn.Laba.Distance.module.Module;
import cn.Laba.Distance.module.ModuleType;
import cn.Laba.Distance.ui.gui.GuiBaned;
import net.minecraft.client.multiplayer.WorldClient;

public class Baned extends Module {
    public Baned(){
        super("DistanceBan",new String[]{"ban"}, ModuleType.World);
    }
    @Override
    public void onEnable(){
        if (mc.theWorld != null){
            mc.theWorld.sendQuittingDisconnectingPacket();
            mc.loadWorld((WorldClient)null);
        }
        mc.displayGuiScreen(new GuiBaned());
    }
}
