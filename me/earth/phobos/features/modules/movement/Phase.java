/*     */ package me.earth.phobos.features.modules.movement;
/*     */ 
/*     */ import io.netty.util.internal.ConcurrentSet;
/*     */ import java.util.Set;
/*     */ import me.earth.phobos.event.events.MoveEvent;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.event.events.PushEvent;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketConfirmTeleport;
/*     */ import net.minecraft.network.play.client.CPacketEntityAction;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.network.play.server.SPacketPlayerPosLook;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ 
/*     */ public class Phase
/*     */   extends Module
/*     */ {
/*  24 */   private static Phase INSTANCE = new Phase();
/*  25 */   public Setting<Mode> mode = register(new Setting("Mode", Mode.PACKETFLY));
/*  26 */   public Setting<PacketFlyMode> type = register(new Setting("Type", PacketFlyMode.SETBACK, v -> (this.mode.getValue() == Mode.PACKETFLY)));
/*  27 */   public Setting<Integer> xMove = register(new Setting("HMove", Integer.valueOf(625), Integer.valueOf(1), Integer.valueOf(1000), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK), "XMovement speed."));
/*  28 */   public Setting<Integer> yMove = register(new Setting("YMove", Integer.valueOf(625), Integer.valueOf(1), Integer.valueOf(1000), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK), "YMovement speed."));
/*  29 */   public Setting<Boolean> extra = register(new Setting("ExtraPacket", Boolean.valueOf(true), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK)));
/*  30 */   public Setting<Integer> offset = register(new Setting("Offset", Integer.valueOf(1337), Integer.valueOf(-1337), Integer.valueOf(1337), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK && ((Boolean)this.extra.getValue()).booleanValue()), "Up speed."));
/*  31 */   public Setting<Boolean> fallPacket = register(new Setting("FallPacket", Boolean.valueOf(true), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK)));
/*  32 */   public Setting<Boolean> teleporter = register(new Setting("Teleport", Boolean.valueOf(true), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK)));
/*  33 */   public Setting<Boolean> boundingBox = register(new Setting("BoundingBox", Boolean.valueOf(true), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK)));
/*  34 */   public Setting<Integer> teleportConfirm = register(new Setting("Confirm", Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(4), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK)));
/*  35 */   public Setting<Boolean> ultraPacket = register(new Setting("DoublePacket", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK)));
/*  36 */   public Setting<Boolean> updates = register(new Setting("Update", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK)));
/*  37 */   public Setting<Boolean> setOnMove = register(new Setting("SetMove", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK)));
/*  38 */   public Setting<Boolean> cliperino = register(new Setting("NoClip", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK && ((Boolean)this.setOnMove.getValue()).booleanValue())));
/*  39 */   public Setting<Boolean> scanPackets = register(new Setting("ScanPackets", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK)));
/*  40 */   public Setting<Boolean> resetConfirm = register(new Setting("Reset", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK)));
/*  41 */   public Setting<Boolean> posLook = register(new Setting("PosLook", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK)));
/*  42 */   public Setting<Boolean> cancel = register(new Setting("Cancel", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK && ((Boolean)this.posLook.getValue()).booleanValue())));
/*  43 */   public Setting<Boolean> cancelType = register(new Setting("SetYaw", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK && ((Boolean)this.posLook.getValue()).booleanValue() && ((Boolean)this.cancel.getValue()).booleanValue())));
/*  44 */   public Setting<Boolean> onlyY = register(new Setting("OnlyY", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK && ((Boolean)this.posLook.getValue()).booleanValue())));
/*  45 */   public Setting<Integer> cancelPacket = register(new Setting("Packets", Integer.valueOf(20), Integer.valueOf(0), Integer.valueOf(20), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK && ((Boolean)this.posLook.getValue()).booleanValue())));
/*  46 */   private final Set<CPacketPlayer> packets = (Set<CPacketPlayer>)new ConcurrentSet();
/*     */   private boolean teleport = true;
/*  48 */   private int teleportIds = 0;
/*     */   private int posLookPackets;
/*     */   
/*     */   public Phase() {
/*  52 */     super("Phase", "Makes you able to phase through blocks.", Module.Category.MOVEMENT, true, false, false);
/*  53 */     setInstance();
/*     */   }
/*     */   
/*     */   public static Phase getInstance() {
/*  57 */     if (INSTANCE == null) {
/*  58 */       INSTANCE = new Phase();
/*     */     }
/*  60 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  64 */     INSTANCE = this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  69 */     this.packets.clear();
/*  70 */     this.posLookPackets = 0;
/*  71 */     if (mc.field_71439_g != null) {
/*  72 */       if (((Boolean)this.resetConfirm.getValue()).booleanValue()) {
/*  73 */         this.teleportIds = 0;
/*     */       }
/*  75 */       mc.field_71439_g.field_70145_X = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/*  81 */     return this.mode.currentEnumName();
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onMove(MoveEvent event) {
/*  86 */     if (((Boolean)this.setOnMove.getValue()).booleanValue() && this.type.getValue() == PacketFlyMode.SETBACK && event.getStage() == 0 && !mc.func_71356_B() && this.mode.getValue() == Mode.PACKETFLY) {
/*  87 */       event.setX(mc.field_71439_g.field_70159_w);
/*  88 */       event.setY(mc.field_71439_g.field_70181_x);
/*  89 */       event.setZ(mc.field_71439_g.field_70179_y);
/*  90 */       if (((Boolean)this.cliperino.getValue()).booleanValue()) {
/*  91 */         mc.field_71439_g.field_70145_X = true;
/*     */       }
/*     */     } 
/*  94 */     if (this.type.getValue() == PacketFlyMode.NONE || event.getStage() != 0 || mc.func_71356_B() || this.mode.getValue() != Mode.PACKETFLY) {
/*     */       return;
/*     */     }
/*  97 */     if (!((Boolean)this.boundingBox.getValue()).booleanValue() && !((Boolean)this.updates.getValue()).booleanValue()) {
/*  98 */       doPhase(event);
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPush(PushEvent event) {
/* 104 */     if (event.getStage() == 1 && this.type.getValue() != PacketFlyMode.NONE) {
/* 105 */       event.setCanceled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onMove(UpdateWalkingPlayerEvent event) {
/* 111 */     if (fullNullCheck() || event.getStage() != 0 || this.type.getValue() != PacketFlyMode.SETBACK || this.mode.getValue() != Mode.PACKETFLY) {
/*     */       return;
/*     */     }
/* 114 */     if (((Boolean)this.boundingBox.getValue()).booleanValue()) {
/* 115 */       doBoundingBox();
/* 116 */     } else if (((Boolean)this.updates.getValue()).booleanValue()) {
/* 117 */       doPhase((MoveEvent)null);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void doPhase(MoveEvent event) {
/* 122 */     if (this.type.getValue() == PacketFlyMode.SETBACK && !((Boolean)this.boundingBox.getValue()).booleanValue()) {
/* 123 */       double[] dirSpeed = getMotion(this.teleport ? (((Integer)this.yMove.getValue()).intValue() / 10000.0D) : ((((Integer)this.yMove.getValue()).intValue() - 1) / 10000.0D));
/* 124 */       double posX = mc.field_71439_g.field_70165_t + dirSpeed[0];
/* 125 */       double posY = mc.field_71439_g.field_70163_u + (mc.field_71474_y.field_74314_A.func_151470_d() ? (this.teleport ? (((Integer)this.yMove.getValue()).intValue() / 10000.0D) : ((((Integer)this.yMove.getValue()).intValue() - 1) / 10000.0D)) : 1.0E-8D) - (mc.field_71474_y.field_74311_E.func_151470_d() ? (this.teleport ? (((Integer)this.yMove.getValue()).intValue() / 10000.0D) : ((((Integer)this.yMove.getValue()).intValue() - 1) / 10000.0D)) : 2.0E-8D);
/* 126 */       double posZ = mc.field_71439_g.field_70161_v + dirSpeed[1];
/* 127 */       CPacketPlayer.PositionRotation packetPlayer = new CPacketPlayer.PositionRotation(posX, posY, posZ, mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A, false);
/* 128 */       this.packets.add(packetPlayer);
/* 129 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)packetPlayer);
/* 130 */       if (((Integer)this.teleportConfirm.getValue()).intValue() != 3) {
/* 131 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketConfirmTeleport(this.teleportIds - 1));
/* 132 */         this.teleportIds++;
/*     */       } 
/* 134 */       if (((Boolean)this.extra.getValue()).booleanValue()) {
/* 135 */         CPacketPlayer.PositionRotation packet = new CPacketPlayer.PositionRotation(mc.field_71439_g.field_70165_t, ((Integer)this.offset.getValue()).intValue() + mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A, true);
/* 136 */         this.packets.add(packet);
/* 137 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)packet);
/*     */       } 
/* 139 */       if (((Integer)this.teleportConfirm.getValue()).intValue() != 1) {
/* 140 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketConfirmTeleport(this.teleportIds + 1));
/* 141 */         this.teleportIds++;
/*     */       } 
/* 143 */       if (((Boolean)this.ultraPacket.getValue()).booleanValue()) {
/* 144 */         CPacketPlayer.PositionRotation packet2 = new CPacketPlayer.PositionRotation(posX, posY, posZ, mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A, false);
/* 145 */         this.packets.add(packet2);
/* 146 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)packet2);
/*     */       } 
/* 148 */       if (((Integer)this.teleportConfirm.getValue()).intValue() == 4) {
/* 149 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketConfirmTeleport(this.teleportIds));
/* 150 */         this.teleportIds++;
/*     */       } 
/* 152 */       if (((Boolean)this.fallPacket.getValue()).booleanValue()) {
/* 153 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
/*     */       }
/* 155 */       mc.field_71439_g.func_70107_b(posX, posY, posZ);
/* 156 */       boolean bl = this.teleport = (!((Boolean)this.teleporter.getValue()).booleanValue() || !this.teleport);
/* 157 */       if (event != null) {
/* 158 */         event.setX(0.0D);
/* 159 */         event.setY(0.0D);
/* 160 */         event.setX(0.0D);
/*     */       } else {
/* 162 */         mc.field_71439_g.field_70159_w = 0.0D;
/* 163 */         mc.field_71439_g.field_70181_x = 0.0D;
/* 164 */         mc.field_71439_g.field_70179_y = 0.0D;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void doBoundingBox() {
/* 170 */     double[] dirSpeed = getMotion(this.teleport ? 0.02250000089406967D : 0.02239999920129776D);
/* 171 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(mc.field_71439_g.field_70165_t + dirSpeed[0], mc.field_71439_g.field_70163_u + (mc.field_71474_y.field_74314_A.func_151470_d() ? (this.teleport ? 0.0625D : 0.0624D) : 1.0E-8D) - (mc.field_71474_y.field_74311_E.func_151470_d() ? (this.teleport ? 0.0625D : 0.0624D) : 2.0E-8D), mc.field_71439_g.field_70161_v + dirSpeed[1], mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A, false));
/* 172 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(mc.field_71439_g.field_70165_t, -1337.0D, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A, true));
/* 173 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
/* 174 */     mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t + dirSpeed[0], mc.field_71439_g.field_70163_u + (mc.field_71474_y.field_74314_A.func_151470_d() ? (this.teleport ? 0.0625D : 0.0624D) : 1.0E-8D) - (mc.field_71474_y.field_74311_E.func_151470_d() ? (this.teleport ? 0.0625D : 0.0624D) : 2.0E-8D), mc.field_71439_g.field_70161_v + dirSpeed[1]);
/* 175 */     this.teleport = !this.teleport;
/* 176 */     mc.field_71439_g.field_70179_y = 0.0D;
/* 177 */     mc.field_71439_g.field_70181_x = 0.0D;
/* 178 */     mc.field_71439_g.field_70159_w = 0.0D;
/* 179 */     mc.field_71439_g.field_70145_X = this.teleport;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketReceive(PacketEvent.Receive event) {
/* 184 */     if (((Boolean)this.posLook.getValue()).booleanValue() && event.getPacket() instanceof SPacketPlayerPosLook) {
/* 185 */       SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
/* 186 */       if (mc.field_71439_g.func_70089_S() && mc.field_71441_e.func_175667_e(new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v)) && !(mc.field_71462_r instanceof net.minecraft.client.gui.GuiDownloadTerrain)) {
/* 187 */         if (this.teleportIds <= 0) {
/* 188 */           this.teleportIds = packet.func_186965_f();
/*     */         }
/* 190 */         if (((Boolean)this.cancel.getValue()).booleanValue() && ((Boolean)this.cancelType.getValue()).booleanValue()) {
/* 191 */           packet.field_148936_d = mc.field_71439_g.field_70177_z;
/* 192 */           packet.field_148937_e = mc.field_71439_g.field_70125_A;
/*     */           return;
/*     */         } 
/* 195 */         if (((Boolean)this.cancel.getValue()).booleanValue() && this.posLookPackets >= ((Integer)this.cancelPacket.getValue()).intValue() && (!((Boolean)this.onlyY.getValue()).booleanValue() || (!mc.field_71474_y.field_74351_w.func_151470_d() && !mc.field_71474_y.field_74366_z.func_151470_d() && !mc.field_71474_y.field_74370_x.func_151470_d() && !mc.field_71474_y.field_74368_y.func_151470_d()))) {
/* 196 */           this.posLookPackets = 0;
/* 197 */           event.setCanceled(true);
/*     */         } 
/* 199 */         this.posLookPackets++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketReceive(PacketEvent.Send event) {
/* 206 */     if (((Boolean)this.scanPackets.getValue()).booleanValue() && event.getPacket() instanceof CPacketPlayer) {
/* 207 */       CPacketPlayer packetPlayer = (CPacketPlayer)event.getPacket();
/* 208 */       if (this.packets.contains(packetPlayer)) {
/* 209 */         this.packets.remove(packetPlayer);
/*     */       } else {
/* 211 */         event.setCanceled(true);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private double[] getMotion(double speed) {
/* 217 */     float moveForward = mc.field_71439_g.field_71158_b.field_192832_b;
/* 218 */     float moveStrafe = mc.field_71439_g.field_71158_b.field_78902_a;
/* 219 */     float rotationYaw = mc.field_71439_g.field_70126_B + (mc.field_71439_g.field_70177_z - mc.field_71439_g.field_70126_B) * mc.func_184121_ak();
/* 220 */     if (moveForward != 0.0F) {
/* 221 */       if (moveStrafe > 0.0F) {
/* 222 */         rotationYaw += ((moveForward > 0.0F) ? -45 : 45);
/* 223 */       } else if (moveStrafe < 0.0F) {
/* 224 */         rotationYaw += ((moveForward > 0.0F) ? 45 : -45);
/*     */       } 
/* 226 */       moveStrafe = 0.0F;
/* 227 */       if (moveForward > 0.0F) {
/* 228 */         moveForward = 1.0F;
/* 229 */       } else if (moveForward < 0.0F) {
/* 230 */         moveForward = -1.0F;
/*     */       } 
/*     */     } 
/* 233 */     double posX = moveForward * speed * -Math.sin(Math.toRadians(rotationYaw)) + moveStrafe * speed * Math.cos(Math.toRadians(rotationYaw));
/* 234 */     double posZ = moveForward * speed * Math.cos(Math.toRadians(rotationYaw)) - moveStrafe * speed * -Math.sin(Math.toRadians(rotationYaw));
/* 235 */     return new double[] { posX, posZ };
/*     */   }
/*     */   
/*     */   public enum PacketFlyMode {
/* 239 */     NONE,
/* 240 */     SETBACK;
/*     */   }
/*     */   
/*     */   public enum Mode
/*     */   {
/* 245 */     PACKETFLY;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\movement\Phase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */