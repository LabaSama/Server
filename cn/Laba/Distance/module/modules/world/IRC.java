package cn.Laba.Distance.module.modules.world;


import cms.mortalchen.chimera.irc.MyBufferedReader;
import cms.mortalchen.chimera.irc.MyPrintWriter;
import cms.mortalchen.chimera.irc.utils.IRCUtils;
import cms.mortalchen.chimera.irc.utils.packets.IRCPacket;
import cms.mortalchen.chimera.irc.utils.packets.IRCType;
import cms.mortalchen.chimera.irc.utils.packets.clientside.ClientHandShakePacket;
import cms.mortalchen.chimera.irc.utils.packets.clientside.ClientHeartPacket;
import cms.mortalchen.chimera.irc.utils.packets.serverside.ServerChatPacket;
import cms.mortalchen.chimera.irc.utils.packets.serverside.ServerCommandPacket;
import cms.mortalchen.chimera.irc.utils.packets.serverside.ServerHandShakePacket;
import cms.mortalchen.encryption.RSA;
import cn.Laba.Distance.Client;
import cn.Laba.Distance.api.EventHandler;
import cn.Laba.Distance.api.events.World.EventPreUpdate;
import cn.Laba.Distance.api.events.World.EventTick;
import cn.Laba.Distance.module.Module;
import cn.Laba.Distance.module.ModuleType;
import cn.Laba.Distance.util.time.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import javax.swing.*;
import java.io.InputStreamReader;
import java.net.Socket;


public class IRC extends Module {

    static MyPrintWriter pw;
    static MyBufferedReader br;
    Socket socket;
    private boolean messageThread;
    private final TimerUtil timer = new TimerUtil();
    private final TimerUtil timerUtil = new TimerUtil();
    public static IRC INSTANCE;
    public static String Nick;
    public static String readmsg;
    public static boolean Verifyed = true;

    public IRC() {
        super("IRC", new String[]{"IRC"}, ModuleType.World);
        timer.reset();
        INSTANCE = this;
    }

    public void processMessage(String msg1) {
        IRCPacket packet = IRCUtils.coverToPacket(msg1);
        switch (packet.type){
            case STOP: {
                try {
                    socket.close();
                } catch (Exception ignored) {

                }
                break;
            }
            case HEART: {
                pw.println(IRCUtils.toJson(new ClientHeartPacket(System.currentTimeMillis(), mc.getSession().getUsername())));
                break;
            }
            case CHAT: {
                if (isEnabled()) {
                    ServerChatPacket chatPacket = (ServerChatPacket) packet;
                    Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§a[DistanceIRC]§r" + chatPacket.content));
                }
                break;
            }
            case COMMAND: {
                ServerCommandPacket commandPacket = (ServerCommandPacket) packet;
                if (commandPacket.content.equals("crash")) {
                    System.err.println("[DistanceIRC]服务器将你踢出");
                    Runtime.getRuntime().exit(0);
                }
                break;
            }
            default:{
                System.out.println("Unknown packet:" + msg1);
                break;
            }
        }
    }

    public static void sendIRCMessage(String message) {
        //pw.println(IRCUtils.toJson(new ClientChatPacket(System.currentTimeMillis(), message)));
    }

    class connect extends Thread {
        connect() {
        }

        @Override
        public void run() {
            this.setName("Connect");
            try {
                if (!Client.Baned) {
                    messageThread = false;
                    socket = new Socket("disirc.casodo.cc", 44413);
                    pw = new MyPrintWriter(socket.getOutputStream(), true);
                    br = new MyBufferedReader(new InputStreamReader(socket.getInputStream()));

                    RSA.genKey();
                    pw.println(new ClientHandShakePacket(System.currentTimeMillis(),RSA.PUBLIC_KEY).toJson());
                    String message = br.readLine();
                    IRCPacket packet = IRCUtils.coverToPacket(message);
                    if (packet.type.equals(IRCType.HANDSHAKE)){
                        ServerHandShakePacket handShakePacket = (ServerHandShakePacket) packet;
                        RSA.SERVER_PUBLIC_KEY = handShakePacket.content;
                        pw.publicKey = RSA.SERVER_PUBLIC_KEY;
                        br.privateKey = RSA.PRIVATE_KEY;
                    }else {
                        JOptionPane.showMessageDialog(null,"服务器数据包异常","Distance",JOptionPane.ERROR_MESSAGE);
                        System.exit(-1);
                    }

                    messageThread = true;
                    new readMessage().start();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    class readMessage
            extends Thread {
        readMessage() {
        }

        @Override
        public void run() {
            this.setName("ReadMessage");
            try {
                while (messageThread) {
                    String msg1 = br.readLine();
                    if (msg1 == null) {
                        continue;
                    }
                    readmsg = msg1;
                    processMessage(msg1);
                }
            } catch (Exception e) {
                new reconnect().start();
                if (Verifyed){
                    Verify(Client.User,Client.Pass);
                }
            }
        }
    }

    class reconnect extends Thread {
        reconnect() {
        }

        @Override
        public void run() {
            this.setName("Reconnect");
            if (!Client.Baned) {
                new connect().start();
            }
        }
    }

    @EventHandler
    public void onTick(EventTick e) {
    }

    @EventHandler
    private void onPre(EventPreUpdate e) {
    }

    public static void sendRawIRCMessage(String rawmsg) {
        //pw.println(rawmsg);
    }

    public static void Verify(String username, String password) {
        Client.User = username;
        Client.Pass = password;
//        try {
//            FileManager.saveUser("userdata.txt", ObfuscatedUtils.encodeWithUserPass(Client.userName + ":" + password), false);
//            pw.println(IRCUtils.toJson(new ClientConnectPacket(System.currentTimeMillis(), "", username, password, HWIDUtil.getHWID())));
            Verifyed = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.exit(-144444);
//        }
    }
}
