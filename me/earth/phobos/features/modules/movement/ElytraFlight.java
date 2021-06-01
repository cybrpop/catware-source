/*     */ package me.earth.phobos.features.modules.movement;
/*     */ 
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.MoveEvent;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.EntityEquipmentSlot;
/*     */ import net.minecraft.item.ItemElytra;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketEntityAction;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class ElytraFlight
/*     */   extends Module {
/*  25 */   private static ElytraFlight INSTANCE = new ElytraFlight();
/*  26 */   private final Timer timer = new Timer();
/*  27 */   private final Timer bypassTimer = new Timer();
/*  28 */   public Setting<Mode> mode = register(new Setting("Mode", Mode.FLY));
/*  29 */   public Setting<Integer> devMode = register(new Setting("Type", Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(3), v -> (this.mode.getValue() == Mode.BYPASS || this.mode.getValue() == Mode.BETTER), "EventMode"));
/*  30 */   public Setting<Float> speed = register(new Setting("Speed", Float.valueOf(1.0F), Float.valueOf(0.0F), Float.valueOf(10.0F), v -> (this.mode.getValue() != Mode.FLY && this.mode.getValue() != Mode.BOOST && this.mode.getValue() != Mode.BETTER && this.mode.getValue() != Mode.OHARE), "The Speed."));
/*  31 */   public Setting<Float> vSpeed = register(new Setting("VSpeed", Float.valueOf(0.3F), Float.valueOf(0.0F), Float.valueOf(10.0F), v -> (this.mode.getValue() == Mode.BETTER || this.mode.getValue() == Mode.OHARE), "Vertical Speed"));
/*  32 */   public Setting<Float> hSpeed = register(new Setting("HSpeed", Float.valueOf(1.0F), Float.valueOf(0.0F), Float.valueOf(10.0F), v -> (this.mode.getValue() == Mode.BETTER || this.mode.getValue() == Mode.OHARE), "Horizontal Speed"));
/*  33 */   public Setting<Float> glide = register(new Setting("Glide", Float.valueOf(1.0E-4F), Float.valueOf(0.0F), Float.valueOf(0.2F), v -> (this.mode.getValue() == Mode.BETTER), "Glide Speed"));
/*  34 */   public Setting<Float> tooBeeSpeed = register(new Setting("TooBeeSpeed", Float.valueOf(1.8000001F), Float.valueOf(1.0F), Float.valueOf(2.0F), v -> (this.mode.getValue() == Mode.TOOBEE), "Speed for flight on 2b2t"));
/*  35 */   public Setting<Boolean> autoStart = register(new Setting("AutoStart", Boolean.valueOf(true)));
/*  36 */   public Setting<Boolean> disableInLiquid = register(new Setting("NoLiquid", Boolean.valueOf(true)));
/*  37 */   public Setting<Boolean> infiniteDura = register(new Setting("InfiniteDura", Boolean.valueOf(false)));
/*  38 */   public Setting<Boolean> noKick = register(new Setting("NoKick", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.PACKET)));
/*  39 */   public Setting<Boolean> allowUp = register(new Setting("AllowUp", Boolean.valueOf(true), v -> (this.mode.getValue() == Mode.BETTER)));
/*  40 */   public Setting<Boolean> lockPitch = register(new Setting("LockPitch", Boolean.valueOf(false)));
/*     */   private boolean vertical;
/*     */   private Double posX;
/*     */   private Double flyHeight;
/*     */   private Double posZ;
/*     */   
/*     */   public ElytraFlight() {
/*  47 */     super("ElytraFlight", "Makes Elytra Flight better.", Module.Category.MOVEMENT, true, false, false);
/*  48 */     setInstance();
/*     */   }
/*     */   
/*     */   public static ElytraFlight getInstance() {
/*  52 */     if (INSTANCE == null) {
/*  53 */       INSTANCE = new ElytraFlight();
/*     */     }
/*  55 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  59 */     INSTANCE = this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  64 */     if (this.mode.getValue() == Mode.BETTER && !((Boolean)this.autoStart.getValue()).booleanValue() && ((Integer)this.devMode.getValue()).intValue() == 1) {
/*  65 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
/*     */     }
/*  67 */     this.flyHeight = null;
/*  68 */     this.posX = null;
/*  69 */     this.posZ = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/*  74 */     return this.mode.currentEnumName();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  79 */     if (this.mode.getValue() == Mode.BYPASS && ((Integer)this.devMode.getValue()).intValue() == 1 && mc.field_71439_g.func_184613_cA()) {
/*  80 */       mc.field_71439_g.field_70159_w = 0.0D;
/*  81 */       mc.field_71439_g.field_70181_x = -1.0E-4D;
/*  82 */       mc.field_71439_g.field_70179_y = 0.0D;
/*  83 */       double forwardInput = mc.field_71439_g.field_71158_b.field_192832_b;
/*  84 */       double strafeInput = mc.field_71439_g.field_71158_b.field_78902_a;
/*  85 */       double[] result = forwardStrafeYaw(forwardInput, strafeInput, mc.field_71439_g.field_70177_z);
/*  86 */       double forward = result[0];
/*  87 */       double strafe = result[1];
/*  88 */       double yaw = result[2];
/*  89 */       if (forwardInput != 0.0D || strafeInput != 0.0D) {
/*  90 */         mc.field_71439_g.field_70159_w = forward * ((Float)this.speed.getValue()).floatValue() * Math.cos(Math.toRadians(yaw + 90.0D)) + strafe * ((Float)this.speed.getValue()).floatValue() * Math.sin(Math.toRadians(yaw + 90.0D));
/*  91 */         mc.field_71439_g.field_70179_y = forward * ((Float)this.speed.getValue()).floatValue() * Math.sin(Math.toRadians(yaw + 90.0D)) - strafe * ((Float)this.speed.getValue()).floatValue() * Math.cos(Math.toRadians(yaw + 90.0D));
/*     */       } 
/*  93 */       if (mc.field_71474_y.field_74311_E.func_151470_d()) {
/*  94 */         mc.field_71439_g.field_70181_x = -1.0D;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onSendPacket(PacketEvent.Send event) {
/* 102 */     if (event.getPacket() instanceof CPacketPlayer && this.mode.getValue() == Mode.TOOBEE) {
/* 103 */       CPacketPlayer packet = (CPacketPlayer)event.getPacket();
/* 104 */       if (mc.field_71439_g.func_184613_cA());
/*     */     } 
/*     */ 
/*     */     
/* 108 */     if (event.getPacket() instanceof CPacketPlayer && this.mode.getValue() == Mode.TOOBEEBYPASS) {
/* 109 */       CPacketPlayer packet = (CPacketPlayer)event.getPacket();
/* 110 */       if (mc.field_71439_g.func_184613_cA());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onMove(MoveEvent event) {
/* 118 */     if (this.mode.getValue() == Mode.OHARE) {
/* 119 */       ItemStack itemstack = mc.field_71439_g.func_184582_a(EntityEquipmentSlot.CHEST);
/* 120 */       if (itemstack.func_77973_b() == Items.field_185160_cR && ItemElytra.func_185069_d(itemstack) && mc.field_71439_g.func_184613_cA()) {
/* 121 */         event.setY(mc.field_71474_y.field_74314_A.func_151470_d() ? ((Float)this.vSpeed.getValue()).floatValue() : (mc.field_71474_y.field_74311_E.func_151470_d() ? -((Float)this.vSpeed.getValue()).floatValue() : 0.0D));
/* 122 */         mc.field_71439_g.func_70024_g(0.0D, mc.field_71474_y.field_74314_A.func_151470_d() ? ((Float)this.vSpeed.getValue()).floatValue() : (mc.field_71474_y.field_74311_E.func_151470_d() ? -((Float)this.vSpeed.getValue()).floatValue() : 0.0D), 0.0D);
/* 123 */         mc.field_71439_g.field_184835_a = 0.0F;
/* 124 */         mc.field_71439_g.field_184836_b = 0.0F;
/* 125 */         mc.field_71439_g.field_184837_c = 0.0F;
/* 126 */         mc.field_71439_g.field_70701_bs = mc.field_71474_y.field_74314_A.func_151470_d() ? ((Float)this.vSpeed.getValue()).floatValue() : (mc.field_71474_y.field_74311_E.func_151470_d() ? -((Float)this.vSpeed.getValue()).floatValue() : 0.0F);
/* 127 */         double forward = mc.field_71439_g.field_71158_b.field_192832_b;
/* 128 */         double strafe = mc.field_71439_g.field_71158_b.field_78902_a;
/* 129 */         float yaw = mc.field_71439_g.field_70177_z;
/* 130 */         if (forward == 0.0D && strafe == 0.0D) {
/* 131 */           event.setX(0.0D);
/* 132 */           event.setZ(0.0D);
/*     */         } else {
/* 134 */           if (forward != 0.0D) {
/* 135 */             if (strafe > 0.0D) {
/* 136 */               yaw += ((forward > 0.0D) ? -45 : 45);
/* 137 */             } else if (strafe < 0.0D) {
/* 138 */               yaw += ((forward > 0.0D) ? 45 : -45);
/*     */             } 
/* 140 */             strafe = 0.0D;
/* 141 */             if (forward > 0.0D) {
/* 142 */               forward = 1.0D;
/* 143 */             } else if (forward < 0.0D) {
/* 144 */               forward = -1.0D;
/*     */             } 
/*     */           } 
/* 147 */           double cos = Math.cos(Math.toRadians((yaw + 90.0F)));
/* 148 */           double sin = Math.sin(Math.toRadians((yaw + 90.0F)));
/* 149 */           event.setX(forward * ((Float)this.hSpeed.getValue()).floatValue() * cos + strafe * ((Float)this.hSpeed.getValue()).floatValue() * sin);
/* 150 */           event.setZ(forward * ((Float)this.hSpeed.getValue()).floatValue() * sin - strafe * ((Float)this.hSpeed.getValue()).floatValue() * cos);
/*     */         } 
/*     */       } 
/* 153 */     } else if (event.getStage() == 0 && this.mode.getValue() == Mode.BYPASS && ((Integer)this.devMode.getValue()).intValue() == 3) {
/* 154 */       if (mc.field_71439_g.func_184613_cA()) {
/* 155 */         event.setX(0.0D);
/* 156 */         event.setY(-1.0E-4D);
/* 157 */         event.setZ(0.0D);
/* 158 */         double forwardInput = mc.field_71439_g.field_71158_b.field_192832_b;
/* 159 */         double strafeInput = mc.field_71439_g.field_71158_b.field_78902_a;
/* 160 */         double[] result = forwardStrafeYaw(forwardInput, strafeInput, mc.field_71439_g.field_70177_z);
/* 161 */         double forward = result[0];
/* 162 */         double strafe = result[1];
/* 163 */         double yaw = result[2];
/* 164 */         if (forwardInput != 0.0D || strafeInput != 0.0D) {
/* 165 */           event.setX(forward * ((Float)this.speed.getValue()).floatValue() * Math.cos(Math.toRadians(yaw + 90.0D)) + strafe * ((Float)this.speed.getValue()).floatValue() * Math.sin(Math.toRadians(yaw + 90.0D)));
/* 166 */           event.setY(forward * ((Float)this.speed.getValue()).floatValue() * Math.sin(Math.toRadians(yaw + 90.0D)) - strafe * ((Float)this.speed.getValue()).floatValue() * Math.cos(Math.toRadians(yaw + 90.0D)));
/*     */         } 
/* 168 */         if (mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 169 */           event.setY(-1.0D);
/*     */         }
/*     */       } 
/* 172 */     } else if (this.mode.getValue() == Mode.TOOBEE) {
/* 173 */       if (!mc.field_71439_g.func_184613_cA()) {
/*     */         return;
/*     */       }
/* 176 */       if (!mc.field_71439_g.field_71158_b.field_78901_c) {
/* 177 */         if (mc.field_71439_g.field_71158_b.field_78899_d) {
/* 178 */           mc.field_71439_g.field_70181_x = -(((Float)this.tooBeeSpeed.getValue()).floatValue() / 2.0F);
/* 179 */           event.setY(-(((Float)this.speed.getValue()).floatValue() / 2.0F));
/* 180 */         } else if (event.getY() != -1.01E-4D) {
/* 181 */           event.setY(-1.01E-4D);
/* 182 */           mc.field_71439_g.field_70181_x = -1.01E-4D;
/*     */         } 
/*     */       } else {
/*     */         return;
/*     */       } 
/* 187 */       setMoveSpeed(event, ((Float)this.tooBeeSpeed.getValue()).floatValue());
/* 188 */     } else if (this.mode.getValue() == Mode.TOOBEEBYPASS) {
/* 189 */       if (!mc.field_71439_g.func_184613_cA()) {
/*     */         return;
/*     */       }
/* 192 */       if (!mc.field_71439_g.field_71158_b.field_78901_c) {
/* 193 */         if (((Boolean)this.lockPitch.getValue()).booleanValue()) {
/* 194 */           mc.field_71439_g.field_70125_A = 4.0F;
/*     */         }
/*     */       } else {
/*     */         return;
/*     */       } 
/* 199 */       if (Phobos.speedManager.getSpeedKpH() > 180.0D) {
/*     */         return;
/*     */       }
/* 202 */       double yaw = Math.toRadians(mc.field_71439_g.field_70177_z);
/* 203 */       mc.field_71439_g.field_70159_w -= mc.field_71439_g.field_71158_b.field_192832_b * Math.sin(yaw) * 0.04D;
/* 204 */       mc.field_71439_g.field_70179_y += mc.field_71439_g.field_71158_b.field_192832_b * Math.cos(yaw) * 0.04D;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setMoveSpeed(MoveEvent event, double speed) {
/* 209 */     double forward = mc.field_71439_g.field_71158_b.field_192832_b;
/* 210 */     double strafe = mc.field_71439_g.field_71158_b.field_78902_a;
/* 211 */     float yaw = mc.field_71439_g.field_70177_z;
/* 212 */     if (forward == 0.0D && strafe == 0.0D) {
/* 213 */       event.setX(0.0D);
/* 214 */       event.setZ(0.0D);
/* 215 */       mc.field_71439_g.field_70159_w = 0.0D;
/* 216 */       mc.field_71439_g.field_70179_y = 0.0D;
/*     */     } else {
/* 218 */       if (forward != 0.0D) {
/* 219 */         if (strafe > 0.0D) {
/* 220 */           yaw += ((forward > 0.0D) ? -45 : 45);
/* 221 */         } else if (strafe < 0.0D) {
/* 222 */           yaw += ((forward > 0.0D) ? 45 : -45);
/*     */         } 
/* 224 */         strafe = 0.0D;
/* 225 */         if (forward > 0.0D) {
/* 226 */           forward = 1.0D;
/* 227 */         } else if (forward < 0.0D) {
/* 228 */           forward = -1.0D;
/*     */         } 
/*     */       } 
/* 231 */       double x = forward * speed * -Math.sin(Math.toRadians(yaw)) + strafe * speed * Math.cos(Math.toRadians(yaw));
/* 232 */       double z = forward * speed * Math.cos(Math.toRadians(yaw)) - strafe * speed * -Math.sin(Math.toRadians(yaw));
/* 233 */       event.setX(x);
/* 234 */       event.setZ(z);
/* 235 */       mc.field_71439_g.field_70159_w = x;
/* 236 */       mc.field_71439_g.field_70179_y = z;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onTick() {
/*     */     float yaw;
/* 242 */     if (!mc.field_71439_g.func_184613_cA()) {
/*     */       return;
/*     */     }
/* 245 */     switch ((Mode)this.mode.getValue()) {
/*     */       case BOOST:
/* 247 */         if (mc.field_71439_g.func_70090_H()) {
/* 248 */           mc.func_147114_u().func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
/*     */           return;
/*     */         } 
/* 251 */         if (mc.field_71474_y.field_74314_A.func_151470_d()) {
/* 252 */           mc.field_71439_g.field_70181_x += 0.08D;
/* 253 */         } else if (mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 254 */           mc.field_71439_g.field_70181_x -= 0.04D;
/*     */         } 
/* 256 */         if (mc.field_71474_y.field_74351_w.func_151470_d()) {
/* 257 */           float f = (float)Math.toRadians(mc.field_71439_g.field_70177_z);
/* 258 */           mc.field_71439_g.field_70159_w -= (MathHelper.func_76126_a(f) * 0.05F);
/* 259 */           mc.field_71439_g.field_70179_y += (MathHelper.func_76134_b(f) * 0.05F);
/*     */           break;
/*     */         } 
/* 262 */         if (!mc.field_71474_y.field_74368_y.func_151470_d())
/* 263 */           break;  yaw = (float)Math.toRadians(mc.field_71439_g.field_70177_z);
/* 264 */         mc.field_71439_g.field_70159_w += (MathHelper.func_76126_a(yaw) * 0.05F);
/* 265 */         mc.field_71439_g.field_70179_y -= (MathHelper.func_76134_b(yaw) * 0.05F);
/*     */         break;
/*     */       
/*     */       case FLY:
/* 269 */         mc.field_71439_g.field_71075_bZ.field_75100_b = true;
/*     */         break;
/*     */     } 
/*     */   }
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/*     */     double rotationYaw;
/* 276 */     if (mc.field_71439_g.func_184582_a(EntityEquipmentSlot.CHEST).func_77973_b() != Items.field_185160_cR) {
/*     */       return;
/*     */     }
/* 279 */     switch (event.getStage()) {
/*     */       case 0:
/* 281 */         if (((Boolean)this.disableInLiquid.getValue()).booleanValue() && (mc.field_71439_g.func_70090_H() || mc.field_71439_g.func_180799_ab())) {
/* 282 */           if (mc.field_71439_g.func_184613_cA()) {
/* 283 */             mc.func_147114_u().func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
/*     */           }
/*     */           return;
/*     */         } 
/* 287 */         if (((Boolean)this.autoStart.getValue()).booleanValue() && mc.field_71474_y.field_74314_A.func_151470_d() && !mc.field_71439_g.func_184613_cA() && mc.field_71439_g.field_70181_x < 0.0D && this.timer.passedMs(250L)) {
/* 288 */           mc.func_147114_u().func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
/* 289 */           this.timer.reset();
/*     */         } 
/* 291 */         if (this.mode.getValue() == Mode.BETTER) {
/* 292 */           double[] dir = MathUtil.directionSpeed((((Integer)this.devMode.getValue()).intValue() == 1) ? ((Float)this.speed.getValue()).floatValue() : ((Float)this.hSpeed.getValue()).floatValue());
/* 293 */           switch (((Integer)this.devMode.getValue()).intValue()) {
/*     */             case 1:
/* 295 */               mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/* 296 */               mc.field_71439_g.field_70747_aH = ((Float)this.speed.getValue()).floatValue();
/* 297 */               if (mc.field_71474_y.field_74314_A.func_151470_d()) {
/* 298 */                 mc.field_71439_g.field_70181_x += ((Float)this.speed.getValue()).floatValue();
/*     */               }
/* 300 */               if (mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 301 */                 mc.field_71439_g.field_70181_x -= ((Float)this.speed.getValue()).floatValue();
/*     */               }
/* 303 */               if (mc.field_71439_g.field_71158_b.field_78902_a != 0.0F || mc.field_71439_g.field_71158_b.field_192832_b != 0.0F) {
/* 304 */                 mc.field_71439_g.field_70159_w = dir[0];
/* 305 */                 mc.field_71439_g.field_70179_y = dir[1];
/*     */                 break;
/*     */               } 
/* 308 */               mc.field_71439_g.field_70159_w = 0.0D;
/* 309 */               mc.field_71439_g.field_70179_y = 0.0D;
/*     */               break;
/*     */             
/*     */             case 2:
/* 313 */               if (mc.field_71439_g.func_184613_cA()) {
/* 314 */                 if (this.flyHeight == null) {
/* 315 */                   this.flyHeight = Double.valueOf(mc.field_71439_g.field_70163_u);
/*     */                 }
/*     */               } else {
/* 318 */                 this.flyHeight = null;
/*     */                 return;
/*     */               } 
/* 321 */               if (((Boolean)this.noKick.getValue()).booleanValue()) {
/* 322 */                 this.flyHeight = Double.valueOf(this.flyHeight.doubleValue() - ((Float)this.glide.getValue()).floatValue());
/*     */               }
/* 324 */               this.posX = Double.valueOf(0.0D);
/* 325 */               this.posZ = Double.valueOf(0.0D);
/* 326 */               if (mc.field_71439_g.field_71158_b.field_78902_a != 0.0F || mc.field_71439_g.field_71158_b.field_192832_b != 0.0F) {
/* 327 */                 this.posX = Double.valueOf(dir[0]);
/* 328 */                 this.posZ = Double.valueOf(dir[1]);
/*     */               } 
/* 330 */               if (mc.field_71474_y.field_74314_A.func_151470_d()) {
/* 331 */                 this.flyHeight = Double.valueOf(mc.field_71439_g.field_70163_u + ((Float)this.vSpeed.getValue()).floatValue());
/*     */               }
/* 333 */               if (mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 334 */                 this.flyHeight = Double.valueOf(mc.field_71439_g.field_70163_u - ((Float)this.vSpeed.getValue()).floatValue());
/*     */               }
/* 336 */               mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t + this.posX.doubleValue(), this.flyHeight.doubleValue(), mc.field_71439_g.field_70161_v + this.posZ.doubleValue());
/* 337 */               mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/*     */               break;
/*     */             
/*     */             case 3:
/* 341 */               if (mc.field_71439_g.func_184613_cA()) {
/* 342 */                 if (this.flyHeight == null || this.posX == null || this.posX.doubleValue() == 0.0D || this.posZ == null || this.posZ.doubleValue() == 0.0D) {
/* 343 */                   this.flyHeight = Double.valueOf(mc.field_71439_g.field_70163_u);
/* 344 */                   this.posX = Double.valueOf(mc.field_71439_g.field_70165_t);
/* 345 */                   this.posZ = Double.valueOf(mc.field_71439_g.field_70161_v);
/*     */                 } 
/*     */               } else {
/* 348 */                 this.flyHeight = null;
/* 349 */                 this.posX = null;
/* 350 */                 this.posZ = null;
/*     */                 return;
/*     */               } 
/* 353 */               if (((Boolean)this.noKick.getValue()).booleanValue()) {
/* 354 */                 this.flyHeight = Double.valueOf(this.flyHeight.doubleValue() - ((Float)this.glide.getValue()).floatValue());
/*     */               }
/* 356 */               if (mc.field_71439_g.field_71158_b.field_78902_a != 0.0F || mc.field_71439_g.field_71158_b.field_192832_b != 0.0F) {
/* 357 */                 this.posX = Double.valueOf(this.posX.doubleValue() + dir[0]);
/* 358 */                 this.posZ = Double.valueOf(this.posZ.doubleValue() + dir[1]);
/*     */               } 
/* 360 */               if (((Boolean)this.allowUp.getValue()).booleanValue() && mc.field_71474_y.field_74314_A.func_151470_d()) {
/* 361 */                 this.flyHeight = Double.valueOf(mc.field_71439_g.field_70163_u + (((Float)this.vSpeed.getValue()).floatValue() / 10.0F));
/*     */               }
/* 363 */               if (mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 364 */                 this.flyHeight = Double.valueOf(mc.field_71439_g.field_70163_u - (((Float)this.vSpeed.getValue()).floatValue() / 10.0F));
/*     */               }
/* 366 */               mc.field_71439_g.func_70107_b(this.posX.doubleValue(), this.flyHeight.doubleValue(), this.posZ.doubleValue());
/* 367 */               mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/*     */               break;
/*     */           } 
/*     */         } 
/* 371 */         rotationYaw = Math.toRadians(mc.field_71439_g.field_70177_z);
/* 372 */         if (mc.field_71439_g.func_184613_cA()) {
/* 373 */           float speedScaled; double[] directionSpeedPacket; double[] directionSpeedBypass; switch ((Mode)this.mode.getValue()) {
/*     */             case VANILLA:
/* 375 */               speedScaled = ((Float)this.speed.getValue()).floatValue() * 0.05F;
/* 376 */               if (mc.field_71474_y.field_74314_A.func_151470_d()) {
/* 377 */                 mc.field_71439_g.field_70181_x += speedScaled;
/*     */               }
/* 379 */               if (mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 380 */                 mc.field_71439_g.field_70181_x -= speedScaled;
/*     */               }
/* 382 */               if (mc.field_71474_y.field_74351_w.func_151470_d()) {
/* 383 */                 mc.field_71439_g.field_70159_w -= Math.sin(rotationYaw) * speedScaled;
/* 384 */                 mc.field_71439_g.field_70179_y += Math.cos(rotationYaw) * speedScaled;
/*     */               } 
/* 386 */               if (!mc.field_71474_y.field_74368_y.func_151470_d())
/* 387 */                 break;  mc.field_71439_g.field_70159_w += Math.sin(rotationYaw) * speedScaled;
/* 388 */               mc.field_71439_g.field_70179_y -= Math.cos(rotationYaw) * speedScaled;
/*     */               break;
/*     */             
/*     */             case PACKET:
/* 392 */               freezePlayer((EntityPlayer)mc.field_71439_g);
/* 393 */               runNoKick((EntityPlayer)mc.field_71439_g);
/* 394 */               directionSpeedPacket = MathUtil.directionSpeed(((Float)this.speed.getValue()).floatValue());
/* 395 */               if (mc.field_71439_g.field_71158_b.field_78901_c) {
/* 396 */                 mc.field_71439_g.field_70181_x = ((Float)this.speed.getValue()).floatValue();
/*     */               }
/* 398 */               if (mc.field_71439_g.field_71158_b.field_78899_d) {
/* 399 */                 mc.field_71439_g.field_70181_x = -((Float)this.speed.getValue()).floatValue();
/*     */               }
/* 401 */               if (mc.field_71439_g.field_71158_b.field_78902_a != 0.0F || mc.field_71439_g.field_71158_b.field_192832_b != 0.0F) {
/* 402 */                 mc.field_71439_g.field_70159_w = directionSpeedPacket[0];
/* 403 */                 mc.field_71439_g.field_70179_y = directionSpeedPacket[1];
/*     */               } 
/* 405 */               mc.func_147114_u().func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
/* 406 */               mc.func_147114_u().func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
/*     */               break;
/*     */             
/*     */             case BYPASS:
/* 410 */               if (((Integer)this.devMode.getValue()).intValue() != 3)
/* 411 */                 break;  if (mc.field_71474_y.field_74314_A.func_151470_d()) {
/* 412 */                 mc.field_71439_g.field_70181_x = 0.019999999552965164D;
/*     */               }
/* 414 */               if (mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 415 */                 mc.field_71439_g.field_70181_x = -0.20000000298023224D;
/*     */               }
/* 417 */               if (mc.field_71439_g.field_70173_aa % 8 == 0 && mc.field_71439_g.field_70163_u <= 240.0D) {
/* 418 */                 mc.field_71439_g.field_70181_x = 0.019999999552965164D;
/*     */               }
/* 420 */               mc.field_71439_g.field_71075_bZ.field_75100_b = true;
/* 421 */               mc.field_71439_g.field_71075_bZ.func_75092_a(0.025F);
/* 422 */               directionSpeedBypass = MathUtil.directionSpeed(0.5199999809265137D);
/* 423 */               if (mc.field_71439_g.field_71158_b.field_78902_a != 0.0F || mc.field_71439_g.field_71158_b.field_192832_b != 0.0F) {
/* 424 */                 mc.field_71439_g.field_70159_w = directionSpeedBypass[0];
/* 425 */                 mc.field_71439_g.field_70179_y = directionSpeedBypass[1];
/*     */                 break;
/*     */               } 
/* 428 */               mc.field_71439_g.field_70159_w = 0.0D;
/* 429 */               mc.field_71439_g.field_70179_y = 0.0D;
/*     */               break;
/*     */           } 
/*     */         } 
/* 433 */         if (!((Boolean)this.infiniteDura.getValue()).booleanValue())
/* 434 */           break;  mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
/*     */         break;
/*     */       
/*     */       case 1:
/* 438 */         if (!((Boolean)this.infiniteDura.getValue()).booleanValue())
/* 439 */           break;  mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private double[] forwardStrafeYaw(double forward, double strafe, double yaw) {
/* 445 */     double[] result = { forward, strafe, yaw };
/* 446 */     if ((forward != 0.0D || strafe != 0.0D) && forward != 0.0D) {
/* 447 */       if (strafe > 0.0D) {
/* 448 */         result[2] = result[2] + ((forward > 0.0D) ? -45 : 45);
/* 449 */       } else if (strafe < 0.0D) {
/* 450 */         result[2] = result[2] + ((forward > 0.0D) ? 45 : -45);
/*     */       } 
/* 452 */       result[1] = 0.0D;
/* 453 */       if (forward > 0.0D) {
/* 454 */         result[0] = 1.0D;
/* 455 */       } else if (forward < 0.0D) {
/* 456 */         result[0] = -1.0D;
/*     */       } 
/*     */     } 
/* 459 */     return result;
/*     */   }
/*     */   
/*     */   private void freezePlayer(EntityPlayer player) {
/* 463 */     player.field_70159_w = 0.0D;
/* 464 */     player.field_70181_x = 0.0D;
/* 465 */     player.field_70179_y = 0.0D;
/*     */   }
/*     */   
/*     */   private void runNoKick(EntityPlayer player) {
/* 469 */     if (((Boolean)this.noKick.getValue()).booleanValue() && !player.func_184613_cA() && player.field_70173_aa % 4 == 0) {
/* 470 */       player.field_70181_x = -0.03999999910593033D;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 476 */     if (fullNullCheck() || mc.field_71439_g.field_71075_bZ.field_75098_d) {
/*     */       return;
/*     */     }
/* 479 */     mc.field_71439_g.field_71075_bZ.field_75100_b = false;
/*     */   }
/*     */   
/*     */   public enum Mode {
/* 483 */     VANILLA,
/* 484 */     PACKET,
/* 485 */     BOOST,
/* 486 */     FLY,
/* 487 */     BYPASS,
/* 488 */     BETTER,
/* 489 */     OHARE,
/* 490 */     TOOBEE,
/* 491 */     TOOBEEBYPASS;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\movement\ElytraFlight.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */