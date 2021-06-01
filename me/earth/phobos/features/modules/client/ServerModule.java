/*     */ package me.earth.phobos.features.modules.client;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.mixin.mixins.accessors.IC00Handshake;
/*     */ import me.earth.phobos.util.TextUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketChatMessage;
/*     */ import net.minecraft.network.play.client.CPacketKeepAlive;
/*     */ import net.minecraft.network.play.server.SPacketChat;
/*     */ import net.minecraft.network.play.server.SPacketKeepAlive;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class ServerModule
/*     */   extends Module
/*     */ {
/*     */   private static ServerModule instance;
/*  24 */   private final AtomicBoolean connected = new AtomicBoolean(false);
/*  25 */   private final Timer pingTimer = new Timer();
/*  26 */   private final List<Long> pingList = new ArrayList<>();
/*  27 */   public Setting<String> ip = register(new Setting("CatwareIP", "0.0.0.0.0"));
/*  28 */   public Setting<String> port = register((new Setting("Port", "0")).setRenderName(true));
/*  29 */   public Setting<String> serverIP = register(new Setting("ServerIP", "AnarchyHvH.eu"));
/*  30 */   public Setting<Boolean> noFML = register(new Setting("RemoveFML", Boolean.valueOf(false)));
/*  31 */   public Setting<Boolean> getName = register(new Setting("GetName", Boolean.valueOf(false)));
/*  32 */   public Setting<Boolean> average = register(new Setting("Average", Boolean.valueOf(false)));
/*  33 */   public Setting<Boolean> clear = register(new Setting("ClearPings", Boolean.valueOf(false)));
/*  34 */   public Setting<Boolean> oneWay = register(new Setting("OneWay", Boolean.valueOf(false)));
/*  35 */   public Setting<Integer> delay = register(new Setting("KeepAlives", Integer.valueOf(10), Integer.valueOf(1), Integer.valueOf(50)));
/*  36 */   private long currentPing = 0L;
/*  37 */   private long serverPing = 0L;
/*  38 */   private StringBuffer name = null;
/*  39 */   private long averagePing = 0L;
/*  40 */   private String serverPrefix = "idk";
/*     */   
/*     */   public ServerModule() {
/*  43 */     super("PingBypass", "Manages Phobos`s internal Server", Module.Category.CLIENT, false, true, true);
/*  44 */     instance = this;
/*     */   }
/*     */   
/*     */   public static ServerModule getInstance() {
/*  48 */     if (instance == null) {
/*  49 */       instance = new ServerModule();
/*     */     }
/*  51 */     return instance;
/*     */   }
/*     */   
/*     */   public String getPlayerName() {
/*  55 */     if (this.name == null) {
/*  56 */       return null;
/*     */     }
/*  58 */     return this.name.toString();
/*     */   }
/*     */   
/*     */   public String getServerPrefix() {
/*  62 */     return this.serverPrefix;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLogout() {
/*  67 */     this.averagePing = 0L;
/*  68 */     this.currentPing = 0L;
/*  69 */     this.serverPing = 0L;
/*  70 */     this.pingList.clear();
/*  71 */     this.connected.set(false);
/*  72 */     this.name = null;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onReceivePacket(PacketEvent.Receive event) {
/*  77 */     if (event.getPacket() instanceof SPacketChat) {
/*  78 */       SPacketChat packet = (SPacketChat)event.getPacket();
/*  79 */       if (packet.field_148919_a.func_150260_c().startsWith("@Clientprefix"))
/*     */       {
/*  81 */         String prefix = packet.field_148919_a.func_150254_d().replace("@Clientprefix", "");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/*  88 */     if (mc.func_147114_u() != null && isConnected()) {
/*  89 */       if (((Boolean)this.getName.getValue()).booleanValue()) {
/*  90 */         mc.func_147114_u().func_147297_a((Packet)new CPacketChatMessage("@Servername"));
/*  91 */         this.getName.setValue(Boolean.valueOf(false));
/*     */       } 
/*  93 */       if (this.serverPrefix.equalsIgnoreCase("idk") && mc.field_71441_e != null) {
/*  94 */         mc.func_147114_u().func_147297_a((Packet)new CPacketChatMessage("@Servergetprefix"));
/*     */       }
/*  96 */       if (this.pingTimer.passedMs((((Integer)this.delay.getValue()).intValue() * 1000))) {
/*  97 */         mc.func_147114_u().func_147297_a((Packet)new CPacketKeepAlive(100L));
/*  98 */         this.pingTimer.reset();
/*     */       } 
/* 100 */       if (((Boolean)this.clear.getValue()).booleanValue()) {
/* 101 */         this.pingList.clear();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketReceive(PacketEvent.Receive event) {
/* 109 */     if (event.getPacket() instanceof SPacketChat)
/* 110 */     { SPacketChat packetChat = (SPacketChat)event.getPacket();
/* 111 */       if (packetChat.func_148915_c().func_150254_d().startsWith("@Client")) {
/* 112 */         this.name = new StringBuffer(TextUtil.stripColor(packetChat.func_148915_c().func_150254_d().replace("@Client", "")));
/* 113 */         event.setCanceled(true);
/*     */       }  }
/* 115 */     else { SPacketKeepAlive alive; if (event.getPacket() instanceof SPacketKeepAlive && (alive = (SPacketKeepAlive)event.getPacket()).func_149134_c() > 0L && alive.func_149134_c() < 1000L) {
/* 116 */         this.serverPing = alive.func_149134_c();
/* 117 */         this.currentPing = ((Boolean)this.oneWay.getValue()).booleanValue() ? (this.pingTimer.getPassedTimeMs() / 2L) : this.pingTimer.getPassedTimeMs();
/* 118 */         this.pingList.add(Long.valueOf(this.currentPing));
/* 119 */         this.averagePing = getAveragePing();
/*     */       }  }
/*     */   
/*     */   }
/*     */   @SubscribeEvent
/*     */   public void onPacketSend(PacketEvent.Send event) {
/*     */     IC00Handshake packet;
/*     */     String ip;
/* 127 */     if (event.getPacket() instanceof net.minecraft.network.handshake.client.C00Handshake && (ip = (packet = (IC00Handshake)event.getPacket()).getIp()).equals(this.ip.getValue())) {
/* 128 */       packet.setIp((String)this.serverIP.getValue());
/* 129 */       System.out.println(packet.getIp());
/* 130 */       this.connected.set(true);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/* 136 */     return this.averagePing + "ms";
/*     */   }
/*     */   
/*     */   private long getAveragePing() {
/* 140 */     if (!((Boolean)this.average.getValue()).booleanValue() || this.pingList.isEmpty()) {
/* 141 */       return this.currentPing;
/*     */     }
/* 143 */     int full = 0;
/* 144 */     for (Iterator<Long> iterator = this.pingList.iterator(); iterator.hasNext(); ) { long i = ((Long)iterator.next()).longValue();
/* 145 */       full = (int)(full + i); }
/*     */     
/* 147 */     return (full / this.pingList.size());
/*     */   }
/*     */   
/*     */   public boolean isConnected() {
/* 151 */     return this.connected.get();
/*     */   }
/*     */   
/*     */   public int getPort() {
/*     */     int result;
/*     */     try {
/* 157 */       result = Integer.parseInt((String)this.port.getValue());
/* 158 */     } catch (NumberFormatException e) {
/* 159 */       return -1;
/*     */     } 
/* 161 */     return result;
/*     */   }
/*     */   
/*     */   public long getServerPing() {
/* 165 */     return this.serverPing;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\client\ServerModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */