package cn.Laba.Distance.command.commands;
import cn.Laba.Distance.api.verify.SHWID;
import cn.Laba.Distance.command.Command;
import cn.Laba.Distance.manager.Fucker;
import cn.Laba.Distance.manager.ModuleManager;
import cn.Laba.Distance.manager.RenderManager;
import cn.Laba.Distance.ui.ClientNotification;
import cn.Laba.Distance.util.misc.Helper;
import cn.Laba.Distance.module.modules.player.AutoTP;
import cn.Laba.Distance.ui.notifications.user.Notifications;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;


public class Tp
        extends Command {
    public Tp() {
        super("tp", new String[]{"TP"}, "", "传送玩家到指定坐标或玩家");
    }


    @Override
    public String execute(String[] args) {
        if (SHWID.BITCH != 0){
            RenderManager.doRender();
        }
        if (SHWID.hahaha != 1){
            Fucker.dofuck();
        }
        if (SHWID.id != 1){
            RenderManager.doRender();
            Fucker.dofuck();
        }
        if (SHWID.id2 != 2){
            RenderManager.doRender();
            Fucker.dofuck();
        }
        boolean cantp = true;
        if (args.length == 3) {
            AutoTP tp = (AutoTP) ModuleManager.getModuleByClass(AutoTP.class);
            int x = 0;
            int y = 120;
            int z = 0;
            try {
                x = Integer.parseInt(args[0]);
                y = Integer.parseInt(args[1]);
                z = Integer.parseInt(args[2]);
            }catch (NumberFormatException e){
                Helper.sendClientMessage("输入值不正常!请输入整数", ClientNotification.Type.warning);
                cantp = false;
            }
            if (cantp) {
                Notifications.getManager().post("TP","正在尝试传送...");
                tp.tp(x,y,z);
            }
        }else if(args.length == 1){
            AutoTP tp = (AutoTP)ModuleManager.getModuleByClass(AutoTP.class);
            Entity targetEntity = null;
            for (Entity entity : mc.theWorld.loadedEntityList) {
                if (entity instanceof EntityPlayer && mc.thePlayer != entity) {
                    if (entity.getName().equalsIgnoreCase(args[0])) {
                        targetEntity = entity;
                        break;
                    }
                }
            }
            if (targetEntity == null) {
                Notifications.getManager().post("TP","无法定位目标:" + args[0]);
                return null;
            } else {
                Notifications.getManager().post("TP","已定位目标: " + targetEntity.getName() + "!");
                Helper.sendClientMessage("正在尝试起飞...", ClientNotification.Type.info);
                tp.tp(targetEntity);

            }
        }else {
            Helper.sendClientMessage(".tp <x> <y> <z> 或者 .tp <playername>", ClientNotification.Type.warning);
        }
        return null;
    }
}

