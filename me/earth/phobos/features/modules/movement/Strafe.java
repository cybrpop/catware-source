/*     */ package me.earth.phobos.features.modules.movement;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.Objects;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.MoveEvent;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.player.Freecam;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.MobEffects;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class Strafe
/*     */   extends Module
/*     */ {
/*     */   private static Strafe INSTANCE;
/*  24 */   private final Setting<Mode> mode = register(new Setting("Mode", Mode.NCP));
/*  25 */   private final Setting<Boolean> limiter = register(new Setting("SetGround", Boolean.valueOf(true)));
/*  26 */   private final Setting<Boolean> bhop2 = register(new Setting("Hop", Boolean.valueOf(true)));
/*  27 */   private final Setting<Boolean> limiter2 = register(new Setting("Bhop", Boolean.valueOf(false)));
/*  28 */   private final Setting<Boolean> noLag = register(new Setting("NoLag", Boolean.valueOf(false)));
/*  29 */   private final Setting<Integer> specialMoveSpeed = register(new Setting("Speed", Integer.valueOf(100), Integer.valueOf(0), Integer.valueOf(150)));
/*  30 */   private final Setting<Integer> potionSpeed = register(new Setting("Speed1", Integer.valueOf(130), Integer.valueOf(0), Integer.valueOf(150)));
/*  31 */   private final Setting<Integer> potionSpeed2 = register(new Setting("Speed2", Integer.valueOf(125), Integer.valueOf(0), Integer.valueOf(150)));
/*  32 */   private final Setting<Integer> dFactor = register(new Setting("DFactor", Integer.valueOf(159), Integer.valueOf(100), Integer.valueOf(200)));
/*  33 */   private final Setting<Integer> acceleration = register(new Setting("Accel", Integer.valueOf(2149), Integer.valueOf(1000), Integer.valueOf(2500)));
/*  34 */   private final Setting<Float> speedLimit = register(new Setting("SpeedLimit", Float.valueOf(35.0F), Float.valueOf(20.0F), Float.valueOf(60.0F)));
/*  35 */   private final Setting<Float> speedLimit2 = register(new Setting("SpeedLimit2", Float.valueOf(60.0F), Float.valueOf(20.0F), Float.valueOf(60.0F)));
/*  36 */   private final Setting<Integer> yOffset = register(new Setting("YOffset", Integer.valueOf(400), Integer.valueOf(350), Integer.valueOf(500)));
/*  37 */   private final Setting<Boolean> potion = register(new Setting("Potion", Boolean.valueOf(false)));
/*  38 */   private final Setting<Boolean> wait = register(new Setting("Wait", Boolean.valueOf(true)));
/*  39 */   private final Setting<Boolean> hopWait = register(new Setting("HopWait", Boolean.valueOf(true)));
/*  40 */   private final Setting<Integer> startStage = register(new Setting("Stage", Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(4)));
/*  41 */   private final Setting<Boolean> setPos = register(new Setting("SetPos", Boolean.valueOf(true)));
/*  42 */   private final Setting<Boolean> setNull = register(new Setting("SetNull", Boolean.valueOf(false)));
/*  43 */   private final Setting<Integer> setGroundLimit = register(new Setting("GroundLimit", Integer.valueOf(138), Integer.valueOf(0), Integer.valueOf(1000)));
/*  44 */   private final Setting<Integer> groundFactor = register(new Setting("GroundFactor", Integer.valueOf(13), Integer.valueOf(0), Integer.valueOf(50)));
/*  45 */   private final Setting<Integer> step = register(new Setting("SetStep", Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(2), v -> (this.mode.getValue() == Mode.BHOP)));
/*  46 */   private final Setting<Boolean> setGroundNoLag = register(new Setting("NoGroundLag", Boolean.valueOf(true)));
/*  47 */   private int stage = 1;
/*     */   private double moveSpeed;
/*     */   private double lastDist;
/*  50 */   private int cooldownHops = 0;
/*     */   private boolean waitForGround = false;
/*  52 */   private final Timer timer = new Timer();
/*  53 */   private int hops = 0;
/*     */   
/*     */   public Strafe() {
/*  56 */     super("Strafe", "AirControl etc.", Module.Category.MOVEMENT, true, false, false);
/*  57 */     INSTANCE = this;
/*     */   }
/*     */   
/*     */   public static Strafe getInstance() {
/*  61 */     if (INSTANCE == null) {
/*  62 */       INSTANCE = new Strafe();
/*     */     }
/*  64 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   public static double getBaseMoveSpeed() {
/*  68 */     double baseSpeed = 0.272D;
/*  69 */     if (mc.field_71439_g.func_70644_a(MobEffects.field_76424_c)) {
/*  70 */       int amplifier = ((PotionEffect)Objects.<PotionEffect>requireNonNull(mc.field_71439_g.func_70660_b(MobEffects.field_76424_c))).func_76458_c();
/*  71 */       baseSpeed *= 1.0D + 0.2D * amplifier;
/*     */     } 
/*  73 */     return baseSpeed;
/*     */   }
/*     */   
/*     */   public static double round(double value, int places) {
/*  77 */     if (places < 0) {
/*  78 */       throw new IllegalArgumentException();
/*     */     }
/*  80 */     BigDecimal bigDecimal = (new BigDecimal(value)).setScale(places, RoundingMode.HALF_UP);
/*  81 */     return bigDecimal.doubleValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  86 */     if (!mc.field_71439_g.field_70122_E) {
/*  87 */       this.waitForGround = true;
/*     */     }
/*  89 */     this.hops = 0;
/*  90 */     this.timer.reset();
/*  91 */     this.moveSpeed = getBaseMoveSpeed();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  96 */     this.hops = 0;
/*  97 */     this.moveSpeed = 0.0D;
/*  98 */     this.stage = ((Integer)this.startStage.getValue()).intValue();
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/* 103 */     if (event.getStage() == 0) {
/* 104 */       this.lastDist = Math.sqrt((mc.field_71439_g.field_70165_t - mc.field_71439_g.field_70169_q) * (mc.field_71439_g.field_70165_t - mc.field_71439_g.field_70169_q) + (mc.field_71439_g.field_70161_v - mc.field_71439_g.field_70166_s) * (mc.field_71439_g.field_70161_v - mc.field_71439_g.field_70166_s));
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onMove(MoveEvent event) {
/* 110 */     if (event.getStage() != 0 || shouldReturn()) {
/*     */       return;
/*     */     }
/* 113 */     if (!mc.field_71439_g.field_70122_E) {
/* 114 */       if (((Boolean)this.wait.getValue()).booleanValue() && this.waitForGround) {
/*     */         return;
/*     */       }
/*     */     } else {
/* 118 */       this.waitForGround = false;
/*     */     } 
/* 120 */     if (this.mode.getValue() == Mode.NCP) {
/* 121 */       doNCP(event);
/* 122 */     } else if (this.mode.getValue() == Mode.BHOP) {
/* 123 */       float moveForward = mc.field_71439_g.field_71158_b.field_192832_b;
/* 124 */       float moveStrafe = mc.field_71439_g.field_71158_b.field_78902_a;
/* 125 */       float rotationYaw = mc.field_71439_g.field_70177_z;
/* 126 */       if (((Integer)this.step.getValue()).intValue() == 1) {
/* 127 */         mc.field_71439_g.field_70138_W = 0.6F;
/*     */       }
/* 129 */       if (((Boolean)this.limiter2.getValue()).booleanValue() && mc.field_71439_g.field_70122_E && Phobos.speedManager.getSpeedKpH() < ((Float)this.speedLimit2.getValue()).floatValue()) {
/* 130 */         this.stage = 2;
/*     */       }
/* 132 */       if (((Boolean)this.limiter.getValue()).booleanValue() && round(mc.field_71439_g.field_70163_u - (int)mc.field_71439_g.field_70163_u, 3) == round(((Integer)this.setGroundLimit.getValue()).intValue() / 1000.0D, 3) && (!((Boolean)this.setGroundNoLag.getValue()).booleanValue() || EntityUtil.isEntityMoving((Entity)mc.field_71439_g))) {
/* 133 */         if (((Boolean)this.setNull.getValue()).booleanValue()) {
/* 134 */           mc.field_71439_g.field_70181_x = 0.0D;
/*     */         } else {
/* 136 */           mc.field_71439_g.field_70181_x -= ((Integer)this.groundFactor.getValue()).intValue() / 100.0D;
/* 137 */           event.setY(event.getY() - ((Integer)this.groundFactor.getValue()).intValue() / 100.0D);
/* 138 */           if (((Boolean)this.setPos.getValue()).booleanValue()) {
/* 139 */             mc.field_71439_g.field_70163_u -= ((Integer)this.groundFactor.getValue()).intValue() / 100.0D;
/*     */           }
/*     */         } 
/*     */       }
/* 143 */       if (this.stage == 1 && EntityUtil.isMoving()) {
/* 144 */         this.stage = 2;
/* 145 */         this.moveSpeed = getMultiplier() * getBaseMoveSpeed() - 0.01D;
/* 146 */       } else if (this.stage == 2 && EntityUtil.isMoving()) {
/* 147 */         this.stage = 3;
/* 148 */         mc.field_71439_g.field_70181_x = ((Integer)this.yOffset.getValue()).intValue() / 1000.0D;
/* 149 */         event.setY(((Integer)this.yOffset.getValue()).intValue() / 1000.0D);
/* 150 */         if (this.cooldownHops > 0) {
/* 151 */           this.cooldownHops--;
/*     */         }
/* 153 */         this.hops++;
/* 154 */         double accel = (((Integer)this.acceleration.getValue()).intValue() == 2149) ? 2.149802D : (((Integer)this.acceleration.getValue()).intValue() / 1000.0D);
/* 155 */         this.moveSpeed *= accel;
/* 156 */       } else if (this.stage == 3) {
/* 157 */         this.stage = 4;
/* 158 */         double difference = 0.66D * (this.lastDist - getBaseMoveSpeed());
/* 159 */         this.moveSpeed = this.lastDist - difference;
/*     */       } else {
/* 161 */         if (mc.field_71441_e.func_184144_a((Entity)mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72317_d(0.0D, mc.field_71439_g.field_70181_x, 0.0D)).size() > 0 || (mc.field_71439_g.field_70124_G && this.stage > 0)) {
/* 162 */           this.stage = (((Boolean)this.bhop2.getValue()).booleanValue() && Phobos.speedManager.getSpeedKpH() >= ((Float)this.speedLimit.getValue()).floatValue()) ? 0 : ((mc.field_71439_g.field_191988_bg != 0.0F || mc.field_71439_g.field_70702_br != 0.0F) ? 1 : 0);
/*     */         }
/* 164 */         this.moveSpeed = this.lastDist - this.lastDist / ((Integer)this.dFactor.getValue()).intValue();
/*     */       } 
/* 166 */       this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
/* 167 */       if (((Boolean)this.hopWait.getValue()).booleanValue() && ((Boolean)this.limiter2.getValue()).booleanValue() && this.hops < 2) {
/* 168 */         this.moveSpeed = EntityUtil.getMaxSpeed();
/*     */       }
/* 170 */       if (moveForward == 0.0F && moveStrafe == 0.0F) {
/* 171 */         event.setX(0.0D);
/* 172 */         event.setZ(0.0D);
/* 173 */         this.moveSpeed = 0.0D;
/* 174 */       } else if (moveForward != 0.0F) {
/* 175 */         if (moveStrafe >= 1.0F) {
/* 176 */           rotationYaw += (moveForward > 0.0F) ? -45.0F : 45.0F;
/* 177 */           moveStrafe = 0.0F;
/* 178 */         } else if (moveStrafe <= -1.0F) {
/* 179 */           rotationYaw += (moveForward > 0.0F) ? 45.0F : -45.0F;
/* 180 */           moveStrafe = 0.0F;
/*     */         } 
/* 182 */         if (moveForward > 0.0F) {
/* 183 */           moveForward = 1.0F;
/* 184 */         } else if (moveForward < 0.0F) {
/* 185 */           moveForward = -1.0F;
/*     */         } 
/*     */       } 
/* 188 */       double motionX = Math.cos(Math.toRadians((rotationYaw + 90.0F)));
/* 189 */       double motionZ = Math.sin(Math.toRadians((rotationYaw + 90.0F)));
/* 190 */       if (this.cooldownHops == 0) {
/* 191 */         event.setX(moveForward * this.moveSpeed * motionX + moveStrafe * this.moveSpeed * motionZ);
/* 192 */         event.setZ(moveForward * this.moveSpeed * motionZ - moveStrafe * this.moveSpeed * motionX);
/*     */       } 
/* 194 */       if (((Integer)this.step.getValue()).intValue() == 2) {
/* 195 */         mc.field_71439_g.field_70138_W = 0.6F;
/*     */       }
/* 197 */       if (moveForward == 0.0F && moveStrafe == 0.0F) {
/* 198 */         this.timer.reset();
/* 199 */         event.setX(0.0D);
/* 200 */         event.setZ(0.0D);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private void doNCP(MoveEvent event) {
/*     */     double motionY;
/* 206 */     if (!((Boolean)this.limiter.getValue()).booleanValue() && mc.field_71439_g.field_70122_E) {
/* 207 */       this.stage = 2;
/*     */     }
/* 209 */     switch (this.stage) {
/*     */       case 0:
/* 211 */         this.stage++;
/* 212 */         this.lastDist = 0.0D;
/*     */         break;
/*     */       
/*     */       case 2:
/* 216 */         motionY = 0.40123128D;
/* 217 */         if ((mc.field_71439_g.field_191988_bg == 0.0F && mc.field_71439_g.field_70702_br == 0.0F) || !mc.field_71439_g.field_70122_E)
/*     */           break; 
/* 219 */         if (mc.field_71439_g.func_70644_a(MobEffects.field_76430_j)) {
/* 220 */           motionY += ((mc.field_71439_g.func_70660_b(MobEffects.field_76430_j).func_76458_c() + 1) * 0.1F);
/*     */         }
/* 222 */         mc.field_71439_g.field_70181_x = motionY;
/* 223 */         event.setY(mc.field_71439_g.field_70181_x);
/* 224 */         this.moveSpeed *= 2.149D;
/*     */         break;
/*     */       
/*     */       case 3:
/* 228 */         this.moveSpeed = this.lastDist - 0.76D * (this.lastDist - getBaseMoveSpeed());
/*     */         break;
/*     */       
/*     */       default:
/* 232 */         if (mc.field_71441_e.func_184144_a((Entity)mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72317_d(0.0D, mc.field_71439_g.field_70181_x, 0.0D)).size() > 0 || (mc.field_71439_g.field_70124_G && this.stage > 0)) {
/* 233 */           this.stage = (((Boolean)this.bhop2.getValue()).booleanValue() && Phobos.speedManager.getSpeedKpH() >= ((Float)this.speedLimit.getValue()).floatValue()) ? 0 : ((mc.field_71439_g.field_191988_bg != 0.0F || mc.field_71439_g.field_70702_br != 0.0F) ? 1 : 0);
/*     */         }
/* 235 */         this.moveSpeed = this.lastDist - this.lastDist / 159.0D;
/*     */         break;
/*     */     } 
/* 238 */     this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
/* 239 */     double forward = mc.field_71439_g.field_71158_b.field_192832_b;
/* 240 */     double strafe = mc.field_71439_g.field_71158_b.field_78902_a;
/* 241 */     double yaw = mc.field_71439_g.field_70177_z;
/* 242 */     if (forward == 0.0D && strafe == 0.0D) {
/* 243 */       event.setX(0.0D);
/* 244 */       event.setZ(0.0D);
/* 245 */     } else if (forward != 0.0D && strafe != 0.0D) {
/* 246 */       forward *= Math.sin(0.7853981633974483D);
/* 247 */       strafe *= Math.cos(0.7853981633974483D);
/*     */     } 
/* 249 */     event.setX((forward * this.moveSpeed * -Math.sin(Math.toRadians(yaw)) + strafe * this.moveSpeed * Math.cos(Math.toRadians(yaw))) * 0.99D);
/* 250 */     event.setZ((forward * this.moveSpeed * Math.cos(Math.toRadians(yaw)) - strafe * this.moveSpeed * -Math.sin(Math.toRadians(yaw))) * 0.99D);
/* 251 */     this.stage++;
/*     */   }
/*     */   
/*     */   private float getMultiplier() {
/* 255 */     float baseSpeed = ((Integer)this.specialMoveSpeed.getValue()).intValue();
/* 256 */     if (((Boolean)this.potion.getValue()).booleanValue() && mc.field_71439_g.func_70644_a(MobEffects.field_76424_c)) {
/* 257 */       int amplifier = ((PotionEffect)Objects.<PotionEffect>requireNonNull(mc.field_71439_g.func_70660_b(MobEffects.field_76424_c))).func_76458_c() + 1;
/* 258 */       baseSpeed = (amplifier >= 2) ? ((Integer)this.potionSpeed2.getValue()).intValue() : ((Integer)this.potionSpeed.getValue()).intValue();
/*     */     } 
/* 260 */     return baseSpeed / 100.0F;
/*     */   }
/*     */   
/*     */   private boolean shouldReturn() {
/* 264 */     return (Phobos.moduleManager.isModuleEnabled(Freecam.class) || Phobos.moduleManager.isModuleEnabled(Phase.class) || Phobos.moduleManager.isModuleEnabled(ElytraFlight.class) || Phobos.moduleManager.isModuleEnabled(Flight.class));
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketReceive(PacketEvent.Receive event) {
/* 269 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketPlayerPosLook && ((Boolean)this.noLag.getValue()).booleanValue()) {
/* 270 */       this.stage = (this.mode.getValue() == Mode.BHOP && (((Boolean)this.limiter2.getValue()).booleanValue() || ((Boolean)this.bhop2.getValue()).booleanValue())) ? 1 : 4;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/* 276 */     if (this.mode.getValue() != Mode.NONE) {
/* 277 */       if (this.mode.getValue() == Mode.NCP) {
/* 278 */         return this.mode.currentEnumName().toUpperCase();
/*     */       }
/* 280 */       return this.mode.currentEnumName();
/*     */     } 
/* 282 */     return null;
/*     */   }
/*     */   
/*     */   public enum Mode {
/* 286 */     NONE,
/* 287 */     NCP,
/* 288 */     BHOP;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\movement\Strafe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */