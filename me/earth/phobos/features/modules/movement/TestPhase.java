/*     */ package me.earth.phobos.features.modules.movement;
/*     */ 
/*     */ import io.netty.util.internal.ConcurrentSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import me.earth.phobos.event.events.MoveEvent;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.event.events.PushEvent;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketConfirmTeleport;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.network.play.server.SPacketPlayerPosLook;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ 
/*     */ public class TestPhase
/*     */   extends Module
/*     */ {
/*     */   private static TestPhase instance;
/*  29 */   private final Set<CPacketPlayer> packets = (Set<CPacketPlayer>)new ConcurrentSet();
/*  30 */   private final Map<Integer, IDtime> teleportmap = new ConcurrentHashMap<>();
/*  31 */   public Setting<Boolean> flight = register(new Setting("Flight", Boolean.valueOf(true)));
/*  32 */   public Setting<Integer> flightMode = register(new Setting("FMode", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(1)));
/*  33 */   public Setting<Boolean> doAntiFactor = register(new Setting("Factorize", Boolean.valueOf(true)));
/*  34 */   public Setting<Double> antiFactor = register(new Setting("AntiFactor", Double.valueOf(2.5D), Double.valueOf(0.1D), Double.valueOf(3.0D)));
/*  35 */   public Setting<Double> extraFactor = register(new Setting("ExtraFactor", Double.valueOf(1.0D), Double.valueOf(0.1D), Double.valueOf(3.0D)));
/*  36 */   public Setting<Boolean> strafeFactor = register(new Setting("StrafeFactor", Boolean.valueOf(true)));
/*  37 */   public Setting<Integer> loops = register(new Setting("Loops", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(10)));
/*  38 */   public Setting<Boolean> clearTeleMap = register(new Setting("ClearMap", Boolean.valueOf(true)));
/*  39 */   public Setting<Integer> mapTime = register(new Setting("ClearTime", Integer.valueOf(30), Integer.valueOf(1), Integer.valueOf(500)));
/*  40 */   public Setting<Boolean> clearIDs = register(new Setting("ClearIDs", Boolean.valueOf(true)));
/*  41 */   public Setting<Boolean> setYaw = register(new Setting("SetYaw", Boolean.valueOf(true)));
/*  42 */   public Setting<Boolean> setID = register(new Setting("SetID", Boolean.valueOf(true)));
/*  43 */   public Setting<Boolean> setMove = register(new Setting("SetMove", Boolean.valueOf(false)));
/*  44 */   public Setting<Boolean> nocliperino = register(new Setting("NoClip", Boolean.valueOf(false)));
/*  45 */   public Setting<Boolean> sendTeleport = register(new Setting("Teleport", Boolean.valueOf(true)));
/*  46 */   public Setting<Boolean> resetID = register(new Setting("ResetID", Boolean.valueOf(true)));
/*  47 */   public Setting<Boolean> setPos = register(new Setting("SetPos", Boolean.valueOf(false)));
/*  48 */   public Setting<Boolean> invalidPacket = register(new Setting("InvalidPacket", Boolean.valueOf(true)));
/*  49 */   private int flightCounter = 0;
/*  50 */   private int teleportID = 0;
/*     */   
/*     */   public TestPhase() {
/*  53 */     super("Packetfly", "Uses packets to fly!", Module.Category.MOVEMENT, true, false, false);
/*  54 */     instance = this;
/*     */   }
/*     */   
/*     */   public static TestPhase getInstance() {
/*  58 */     if (instance == null) {
/*  59 */       instance = new TestPhase();
/*     */     }
/*  61 */     return instance;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onToggle() {}
/*     */ 
/*     */   
/*     */   public void onTick() {
/*  70 */     this.teleportmap.entrySet().removeIf(idTime -> (((Boolean)this.clearTeleMap.getValue()).booleanValue() && ((IDtime)idTime.getValue()).getTimer().passedS(((Integer)this.mapTime.getValue()).intValue())));
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/*  75 */     if (event.getStage() == 1) {
/*     */       return;
/*     */     }
/*  78 */     mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/*  79 */     double speed = 0.0D;
/*  80 */     boolean checkCollisionBoxes = checkHitBoxes();
/*  81 */     speed = (mc.field_71439_g.field_71158_b.field_78901_c && (checkCollisionBoxes || !EntityUtil.isMoving())) ? ((((Boolean)this.flight.getValue()).booleanValue() && !checkCollisionBoxes) ? ((((Integer)this.flightMode.getValue()).intValue() == 0) ? (resetCounter(10) ? -0.032D : 0.062D) : (resetCounter(20) ? -0.032D : 0.062D)) : 0.062D) : (mc.field_71439_g.field_71158_b.field_78899_d ? -0.062D : (!checkCollisionBoxes ? (resetCounter(4) ? (((Boolean)this.flight.getValue()).booleanValue() ? -0.04D : 0.0D) : 0.0D) : 0.0D));
/*  82 */     if (((Boolean)this.doAntiFactor.getValue()).booleanValue() && checkCollisionBoxes && EntityUtil.isMoving() && speed != 0.0D) {
/*  83 */       speed /= ((Double)this.antiFactor.getValue()).doubleValue();
/*     */     }
/*  85 */     double[] strafing = getMotion((((Boolean)this.strafeFactor.getValue()).booleanValue() && checkCollisionBoxes) ? 0.031D : 0.26D);
/*  86 */     for (int i = 1; i < ((Integer)this.loops.getValue()).intValue() + 1; i++) {
/*  87 */       mc.field_71439_g.field_70159_w = strafing[0] * i * ((Double)this.extraFactor.getValue()).doubleValue();
/*  88 */       mc.field_71439_g.field_70181_x = speed * i;
/*  89 */       mc.field_71439_g.field_70179_y = strafing[1] * i * ((Double)this.extraFactor.getValue()).doubleValue();
/*  90 */       sendPackets(mc.field_71439_g.field_70159_w, mc.field_71439_g.field_70181_x, mc.field_71439_g.field_70179_y, ((Boolean)this.sendTeleport.getValue()).booleanValue());
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onMove(MoveEvent event) {
/*  96 */     if (((Boolean)this.setMove.getValue()).booleanValue() && this.flightCounter != 0) {
/*  97 */       event.setX(mc.field_71439_g.field_70159_w);
/*  98 */       event.setY(mc.field_71439_g.field_70181_x);
/*  99 */       event.setZ(mc.field_71439_g.field_70179_y);
/* 100 */       if (((Boolean)this.nocliperino.getValue()).booleanValue() && checkHitBoxes()) {
/* 101 */         mc.field_71439_g.field_70145_X = true;
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketSend(PacketEvent.Send event) {
/*     */     CPacketPlayer packet;
/* 109 */     if (event.getPacket() instanceof CPacketPlayer && !this.packets.remove(packet = (CPacketPlayer)event.getPacket())) {
/* 110 */       event.setCanceled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPushOutOfBlocks(PushEvent event) {
/* 116 */     if (event.getStage() == 1) {
/* 117 */       event.setCanceled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketReceive(PacketEvent.Receive event) {
/* 123 */     if (event.getPacket() instanceof SPacketPlayerPosLook && !fullNullCheck()) {
/*     */       
/* 125 */       SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket(); BlockPos pos;
/* 126 */       if (mc.field_71439_g.func_70089_S() && mc.field_71441_e.func_175668_a(pos = new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v), false) && !(mc.field_71462_r instanceof net.minecraft.client.gui.GuiDownloadTerrain) && ((Boolean)this.clearIDs.getValue()).booleanValue()) {
/* 127 */         this.teleportmap.remove(Integer.valueOf(packet.func_186965_f()));
/*     */       }
/* 129 */       if (((Boolean)this.setYaw.getValue()).booleanValue()) {
/* 130 */         packet.field_148936_d = mc.field_71439_g.field_70177_z;
/* 131 */         packet.field_148937_e = mc.field_71439_g.field_70125_A;
/*     */       } 
/* 133 */       if (((Boolean)this.setID.getValue()).booleanValue()) {
/* 134 */         this.teleportID = packet.func_186965_f();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean checkHitBoxes() {
/* 140 */     return !mc.field_71441_e.func_184144_a((Entity)mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72321_a(-0.0625D, -0.0625D, -0.0625D)).isEmpty();
/*     */   }
/*     */   
/*     */   private boolean resetCounter(int counter) {
/* 144 */     if (++this.flightCounter >= counter) {
/* 145 */       this.flightCounter = 0;
/* 146 */       return true;
/*     */     } 
/* 148 */     return false;
/*     */   }
/*     */   
/*     */   private double[] getMotion(double speed) {
/* 152 */     float moveForward = mc.field_71439_g.field_71158_b.field_192832_b;
/* 153 */     float moveStrafe = mc.field_71439_g.field_71158_b.field_78902_a;
/* 154 */     float rotationYaw = mc.field_71439_g.field_70126_B + (mc.field_71439_g.field_70177_z - mc.field_71439_g.field_70126_B) * mc.func_184121_ak();
/* 155 */     if (moveForward != 0.0F) {
/* 156 */       if (moveStrafe > 0.0F) {
/* 157 */         rotationYaw += ((moveForward > 0.0F) ? -45 : 45);
/* 158 */       } else if (moveStrafe < 0.0F) {
/* 159 */         rotationYaw += ((moveForward > 0.0F) ? 45 : -45);
/*     */       } 
/* 161 */       moveStrafe = 0.0F;
/* 162 */       if (moveForward > 0.0F) {
/* 163 */         moveForward = 1.0F;
/* 164 */       } else if (moveForward < 0.0F) {
/* 165 */         moveForward = -1.0F;
/*     */       } 
/*     */     } 
/* 168 */     double posX = moveForward * speed * -Math.sin(Math.toRadians(rotationYaw)) + moveStrafe * speed * Math.cos(Math.toRadians(rotationYaw));
/* 169 */     double posZ = moveForward * speed * Math.cos(Math.toRadians(rotationYaw)) - moveStrafe * speed * -Math.sin(Math.toRadians(rotationYaw));
/* 170 */     return new double[] { posX, posZ };
/*     */   }
/*     */   
/*     */   private void sendPackets(double x, double y, double z, boolean teleport) {
/* 174 */     Vec3d vec = new Vec3d(x, y, z);
/* 175 */     Vec3d position = mc.field_71439_g.func_174791_d().func_178787_e(vec);
/* 176 */     Vec3d outOfBoundsVec = outOfBoundsVec(vec, position);
/* 177 */     packetSender((CPacketPlayer)new CPacketPlayer.Position(position.field_72450_a, position.field_72448_b, position.field_72449_c, mc.field_71439_g.field_70122_E));
/* 178 */     if (((Boolean)this.invalidPacket.getValue()).booleanValue()) {
/* 179 */       packetSender((CPacketPlayer)new CPacketPlayer.Position(outOfBoundsVec.field_72450_a, outOfBoundsVec.field_72448_b, outOfBoundsVec.field_72449_c, mc.field_71439_g.field_70122_E));
/*     */     }
/* 181 */     if (((Boolean)this.setPos.getValue()).booleanValue()) {
/* 182 */       mc.field_71439_g.func_70107_b(position.field_72450_a, position.field_72448_b, position.field_72449_c);
/*     */     }
/* 184 */     teleportPacket(position, teleport);
/*     */   }
/*     */   
/*     */   private void teleportPacket(Vec3d pos, boolean shouldTeleport) {
/* 188 */     if (shouldTeleport) {
/* 189 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketConfirmTeleport(++this.teleportID));
/* 190 */       this.teleportmap.put(Integer.valueOf(this.teleportID), new IDtime(pos, new Timer()));
/*     */     } 
/*     */   }
/*     */   
/*     */   private Vec3d outOfBoundsVec(Vec3d offset, Vec3d position) {
/* 195 */     return position.func_72441_c(0.0D, 1337.0D, 0.0D);
/*     */   }
/*     */   
/*     */   private void packetSender(CPacketPlayer packet) {
/* 199 */     this.packets.add(packet);
/* 200 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)packet);
/*     */   }
/*     */   
/*     */   private void clean() {
/* 204 */     this.teleportmap.clear();
/* 205 */     this.flightCounter = 0;
/* 206 */     if (((Boolean)this.resetID.getValue()).booleanValue()) {
/* 207 */       this.teleportID = 0;
/*     */     }
/* 209 */     this.packets.clear();
/*     */   }
/*     */   
/*     */   public static class IDtime {
/*     */     private final Vec3d pos;
/*     */     private final Timer timer;
/*     */     
/*     */     public IDtime(Vec3d pos, Timer timer) {
/* 217 */       this.pos = pos;
/* 218 */       this.timer = timer;
/* 219 */       this.timer.reset();
/*     */     }
/*     */     
/*     */     public Vec3d getPos() {
/* 223 */       return this.pos;
/*     */     }
/*     */     
/*     */     public Timer getTimer() {
/* 227 */       return this.timer;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\movement\TestPhase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */