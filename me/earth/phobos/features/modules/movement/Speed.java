/*     */ package me.earth.phobos.features.modules.movement;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.Random;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.ClientEvent;
/*     */ import me.earth.phobos.event.events.MoveEvent;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.BlockUtil;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.MobEffects;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.MovementInput;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class Speed
/*     */   extends Module {
/*  22 */   private static Speed INSTANCE = new Speed();
/*  23 */   public Setting<Mode> mode = register(new Setting("Mode", Mode.INSTANT));
/*  24 */   public Setting<Boolean> strafeJump = register(new Setting("Jump", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.INSTANT)));
/*  25 */   public Setting<Boolean> noShake = register(new Setting("NoShake", Boolean.valueOf(true), v -> (this.mode.getValue() != Mode.INSTANT)));
/*  26 */   public Setting<Boolean> useTimer = register(new Setting("UseTimer", Boolean.valueOf(false), v -> (this.mode.getValue() != Mode.INSTANT)));
/*  27 */   public Setting<Double> zeroSpeed = register(new Setting("0-Speed", Double.valueOf(0.0D), Double.valueOf(0.0D), Double.valueOf(100.0D), v -> (this.mode.getValue() == Mode.VANILLA)));
/*  28 */   public Setting<Double> speed = register(new Setting("Speed", Double.valueOf(10.0D), Double.valueOf(0.1D), Double.valueOf(100.0D), v -> (this.mode.getValue() == Mode.VANILLA)));
/*  29 */   public Setting<Double> blocked = register(new Setting("Blocked", Double.valueOf(10.0D), Double.valueOf(0.0D), Double.valueOf(100.0D), v -> (this.mode.getValue() == Mode.VANILLA)));
/*  30 */   public Setting<Double> unblocked = register(new Setting("Unblocked", Double.valueOf(10.0D), Double.valueOf(0.0D), Double.valueOf(100.0D), v -> (this.mode.getValue() == Mode.VANILLA)));
/*  31 */   public double startY = 0.0D;
/*     */   public boolean antiShake = false;
/*  33 */   public double minY = 0.0D;
/*     */   public boolean changeY = false;
/*  35 */   private double highChainVal = 0.0D;
/*  36 */   private double lowChainVal = 0.0D;
/*     */   private boolean oneTime = false;
/*  38 */   private double bounceHeight = 0.4D;
/*  39 */   private float move = 0.26F;
/*  40 */   private int vanillaCounter = 0;
/*     */   
/*     */   public Speed() {
/*  43 */     super("Speed", "Makes you faster", Module.Category.MOVEMENT, true, false, false);
/*  44 */     setInstance();
/*     */   }
/*     */   
/*     */   public static Speed getInstance() {
/*  48 */     if (INSTANCE == null) {
/*  49 */       INSTANCE = new Speed();
/*     */     }
/*  51 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  55 */     INSTANCE = this;
/*     */   }
/*     */   
/*     */   private boolean shouldReturn() {
/*  59 */     return (Phobos.moduleManager.isModuleEnabled("Freecam") || Phobos.moduleManager.isModuleEnabled("Phase") || Phobos.moduleManager.isModuleEnabled("ElytraFlight") || Phobos.moduleManager.isModuleEnabled("Strafe") || Phobos.moduleManager.isModuleEnabled("Flight"));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  64 */     if (shouldReturn() || mc.field_71439_g.func_70093_af() || mc.field_71439_g.func_70090_H() || mc.field_71439_g.func_180799_ab()) {
/*     */       return;
/*     */     }
/*  67 */     switch ((Mode)this.mode.getValue()) {
/*     */       case BOOST:
/*  69 */         doBoost();
/*     */         break;
/*     */       
/*     */       case ACCEL:
/*  73 */         doAccel();
/*     */         break;
/*     */       
/*     */       case ONGROUND:
/*  77 */         doOnground();
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/*  85 */     if (this.mode.getValue() != Mode.VANILLA || nullCheck()) {
/*     */       return;
/*     */     }
/*  88 */     switch (event.getStage()) {
/*     */       case 0:
/*  90 */         this.vanillaCounter = vanilla() ? ++this.vanillaCounter : 0;
/*  91 */         if (this.vanillaCounter != 4)
/*  92 */           break;  this.changeY = true;
/*  93 */         this.minY = (mc.field_71439_g.func_174813_aQ()).field_72338_b + (mc.field_71441_e.func_180495_p(mc.field_71439_g.func_180425_c()).func_185904_a().func_76230_c() ? (-((Double)this.blocked.getValue()).doubleValue() / 10.0D) : (((Double)this.unblocked.getValue()).doubleValue() / 10.0D)) + getJumpBoostModifier();
/*     */         return;
/*     */       
/*     */       case 1:
/*  97 */         if (this.vanillaCounter == 3) {
/*  98 */           mc.field_71439_g.field_70159_w *= ((Double)this.zeroSpeed.getValue()).doubleValue() / 10.0D;
/*  99 */           mc.field_71439_g.field_70179_y *= ((Double)this.zeroSpeed.getValue()).doubleValue() / 10.0D;
/*     */           break;
/*     */         } 
/* 102 */         if (this.vanillaCounter != 4)
/* 103 */           break;  mc.field_71439_g.field_70159_w /= ((Double)this.speed.getValue()).doubleValue() / 10.0D;
/* 104 */         mc.field_71439_g.field_70179_y /= ((Double)this.speed.getValue()).doubleValue() / 10.0D;
/* 105 */         this.vanillaCounter = 2;
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private double getJumpBoostModifier() {
/* 111 */     double boost = 0.0D;
/* 112 */     if (mc.field_71439_g.func_70644_a(MobEffects.field_76430_j)) {
/* 113 */       int amplifier = ((PotionEffect)Objects.<PotionEffect>requireNonNull(mc.field_71439_g.func_70660_b(MobEffects.field_76430_j))).func_76458_c();
/* 114 */       boost *= 1.0D + 0.2D * amplifier;
/*     */     } 
/* 116 */     return boost;
/*     */   }
/*     */   
/*     */   private boolean vanillaCheck() {
/* 120 */     if (mc.field_71439_g.field_70122_E);
/*     */ 
/*     */     
/* 123 */     return false;
/*     */   }
/*     */   
/*     */   private boolean vanilla() {
/* 127 */     return mc.field_71439_g.field_70122_E;
/*     */   }
/*     */   
/*     */   private void doBoost() {
/* 131 */     this.bounceHeight = 0.4D;
/* 132 */     this.move = 0.26F;
/* 133 */     if (mc.field_71439_g.field_70122_E) {
/* 134 */       this.startY = mc.field_71439_g.field_70163_u;
/*     */     }
/* 136 */     if (EntityUtil.getEntitySpeed((Entity)mc.field_71439_g) <= 1.0D) {
/* 137 */       this.lowChainVal = 1.0D;
/* 138 */       this.highChainVal = 1.0D;
/*     */     } 
/* 140 */     if (EntityUtil.isEntityMoving((Entity)mc.field_71439_g) && !mc.field_71439_g.field_70123_F && !BlockUtil.isBlockAboveEntitySolid((Entity)mc.field_71439_g) && BlockUtil.isBlockBelowEntitySolid((Entity)mc.field_71439_g)) {
/* 141 */       this.oneTime = true;
/* 142 */       this.antiShake = (((Boolean)this.noShake.getValue()).booleanValue() && mc.field_71439_g.func_184187_bx() == null);
/* 143 */       Random random = new Random();
/* 144 */       boolean rnd = random.nextBoolean();
/* 145 */       if (mc.field_71439_g.field_70163_u >= this.startY + this.bounceHeight) {
/* 146 */         mc.field_71439_g.field_70181_x = -this.bounceHeight;
/* 147 */         this.lowChainVal++;
/* 148 */         if (this.lowChainVal == 1.0D) {
/* 149 */           this.move = 0.075F;
/*     */         }
/* 151 */         if (this.lowChainVal == 2.0D) {
/* 152 */           this.move = 0.15F;
/*     */         }
/* 154 */         if (this.lowChainVal == 3.0D) {
/* 155 */           this.move = 0.175F;
/*     */         }
/* 157 */         if (this.lowChainVal == 4.0D) {
/* 158 */           this.move = 0.2F;
/*     */         }
/* 160 */         if (this.lowChainVal == 5.0D) {
/* 161 */           this.move = 0.225F;
/*     */         }
/* 163 */         if (this.lowChainVal == 6.0D) {
/* 164 */           this.move = 0.25F;
/*     */         }
/* 166 */         if (this.lowChainVal >= 7.0D) {
/* 167 */           this.move = 0.27895F;
/*     */         }
/* 169 */         if (((Boolean)this.useTimer.getValue()).booleanValue()) {
/* 170 */           Phobos.timerManager.setTimer(1.0F);
/*     */         }
/*     */       } 
/* 173 */       if (mc.field_71439_g.field_70163_u == this.startY) {
/* 174 */         mc.field_71439_g.field_70181_x = this.bounceHeight;
/* 175 */         this.highChainVal++;
/* 176 */         if (this.highChainVal == 1.0D) {
/* 177 */           this.move = 0.075F;
/*     */         }
/* 179 */         if (this.highChainVal == 2.0D) {
/* 180 */           this.move = 0.175F;
/*     */         }
/* 182 */         if (this.highChainVal == 3.0D) {
/* 183 */           this.move = 0.325F;
/*     */         }
/* 185 */         if (this.highChainVal == 4.0D) {
/* 186 */           this.move = 0.375F;
/*     */         }
/* 188 */         if (this.highChainVal == 5.0D) {
/* 189 */           this.move = 0.4F;
/*     */         }
/* 191 */         if (this.highChainVal >= 6.0D) {
/* 192 */           this.move = 0.43395F;
/*     */         }
/* 194 */         if (((Boolean)this.useTimer.getValue()).booleanValue()) {
/* 195 */           if (rnd) {
/* 196 */             Phobos.timerManager.setTimer(1.3F);
/*     */           } else {
/* 198 */             Phobos.timerManager.setTimer(1.0F);
/*     */           } 
/*     */         }
/*     */       } 
/* 202 */       EntityUtil.moveEntityStrafe(this.move, (Entity)mc.field_71439_g);
/*     */     } else {
/* 204 */       if (this.oneTime) {
/* 205 */         mc.field_71439_g.field_70181_x = -0.1D;
/* 206 */         this.oneTime = false;
/*     */       } 
/* 208 */       this.highChainVal = 0.0D;
/* 209 */       this.lowChainVal = 0.0D;
/* 210 */       this.antiShake = false;
/* 211 */       speedOff();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void doAccel() {
/* 216 */     this.bounceHeight = 0.4D;
/* 217 */     this.move = 0.26F;
/* 218 */     if (mc.field_71439_g.field_70122_E) {
/* 219 */       this.startY = mc.field_71439_g.field_70163_u;
/*     */     }
/* 221 */     if (EntityUtil.getEntitySpeed((Entity)mc.field_71439_g) <= 1.0D) {
/* 222 */       this.lowChainVal = 1.0D;
/* 223 */       this.highChainVal = 1.0D;
/*     */     } 
/* 225 */     if (EntityUtil.isEntityMoving((Entity)mc.field_71439_g) && !mc.field_71439_g.field_70123_F && !BlockUtil.isBlockAboveEntitySolid((Entity)mc.field_71439_g) && BlockUtil.isBlockBelowEntitySolid((Entity)mc.field_71439_g)) {
/* 226 */       this.oneTime = true;
/* 227 */       this.antiShake = (((Boolean)this.noShake.getValue()).booleanValue() && mc.field_71439_g.func_184187_bx() == null);
/* 228 */       Random random = new Random();
/* 229 */       boolean rnd = random.nextBoolean();
/* 230 */       if (mc.field_71439_g.field_70163_u >= this.startY + this.bounceHeight) {
/* 231 */         mc.field_71439_g.field_70181_x = -this.bounceHeight;
/* 232 */         this.lowChainVal++;
/* 233 */         if (this.lowChainVal == 1.0D) {
/* 234 */           this.move = 0.075F;
/*     */         }
/* 236 */         if (this.lowChainVal == 2.0D) {
/* 237 */           this.move = 0.175F;
/*     */         }
/* 239 */         if (this.lowChainVal == 3.0D) {
/* 240 */           this.move = 0.275F;
/*     */         }
/* 242 */         if (this.lowChainVal == 4.0D) {
/* 243 */           this.move = 0.35F;
/*     */         }
/* 245 */         if (this.lowChainVal == 5.0D) {
/* 246 */           this.move = 0.375F;
/*     */         }
/* 248 */         if (this.lowChainVal == 6.0D) {
/* 249 */           this.move = 0.4F;
/*     */         }
/* 251 */         if (this.lowChainVal == 7.0D) {
/* 252 */           this.move = 0.425F;
/*     */         }
/* 254 */         if (this.lowChainVal == 8.0D) {
/* 255 */           this.move = 0.45F;
/*     */         }
/* 257 */         if (this.lowChainVal == 9.0D) {
/* 258 */           this.move = 0.475F;
/*     */         }
/* 260 */         if (this.lowChainVal == 10.0D) {
/* 261 */           this.move = 0.5F;
/*     */         }
/* 263 */         if (this.lowChainVal == 11.0D) {
/* 264 */           this.move = 0.5F;
/*     */         }
/* 266 */         if (this.lowChainVal == 12.0D) {
/* 267 */           this.move = 0.525F;
/*     */         }
/* 269 */         if (this.lowChainVal == 13.0D) {
/* 270 */           this.move = 0.525F;
/*     */         }
/* 272 */         if (this.lowChainVal == 14.0D) {
/* 273 */           this.move = 0.535F;
/*     */         }
/* 275 */         if (this.lowChainVal == 15.0D) {
/* 276 */           this.move = 0.535F;
/*     */         }
/* 278 */         if (this.lowChainVal == 16.0D) {
/* 279 */           this.move = 0.545F;
/*     */         }
/* 281 */         if (this.lowChainVal >= 17.0D) {
/* 282 */           this.move = 0.545F;
/*     */         }
/* 284 */         if (((Boolean)this.useTimer.getValue()).booleanValue()) {
/* 285 */           Phobos.timerManager.setTimer(1.0F);
/*     */         }
/*     */       } 
/* 288 */       if (mc.field_71439_g.field_70163_u == this.startY) {
/* 289 */         mc.field_71439_g.field_70181_x = this.bounceHeight;
/* 290 */         this.highChainVal++;
/* 291 */         if (this.highChainVal == 1.0D) {
/* 292 */           this.move = 0.075F;
/*     */         }
/* 294 */         if (this.highChainVal == 2.0D) {
/* 295 */           this.move = 0.175F;
/*     */         }
/* 297 */         if (this.highChainVal == 3.0D) {
/* 298 */           this.move = 0.375F;
/*     */         }
/* 300 */         if (this.highChainVal == 4.0D) {
/* 301 */           this.move = 0.6F;
/*     */         }
/* 303 */         if (this.highChainVal == 5.0D) {
/* 304 */           this.move = 0.775F;
/*     */         }
/* 306 */         if (this.highChainVal == 6.0D) {
/* 307 */           this.move = 0.825F;
/*     */         }
/* 309 */         if (this.highChainVal == 7.0D) {
/* 310 */           this.move = 0.875F;
/*     */         }
/* 312 */         if (this.highChainVal == 8.0D) {
/* 313 */           this.move = 0.925F;
/*     */         }
/* 315 */         if (this.highChainVal == 9.0D) {
/* 316 */           this.move = 0.975F;
/*     */         }
/* 318 */         if (this.highChainVal == 10.0D) {
/* 319 */           this.move = 1.05F;
/*     */         }
/* 321 */         if (this.highChainVal == 11.0D) {
/* 322 */           this.move = 1.1F;
/*     */         }
/* 324 */         if (this.highChainVal == 12.0D) {
/* 325 */           this.move = 1.1F;
/*     */         }
/* 327 */         if (this.highChainVal == 13.0D) {
/* 328 */           this.move = 1.15F;
/*     */         }
/* 330 */         if (this.highChainVal == 14.0D) {
/* 331 */           this.move = 1.15F;
/*     */         }
/* 333 */         if (this.highChainVal == 15.0D) {
/* 334 */           this.move = 1.175F;
/*     */         }
/* 336 */         if (this.highChainVal == 16.0D) {
/* 337 */           this.move = 1.175F;
/*     */         }
/* 339 */         if (this.highChainVal >= 17.0D) {
/* 340 */           this.move = 1.175F;
/*     */         }
/* 342 */         if (((Boolean)this.useTimer.getValue()).booleanValue()) {
/* 343 */           if (rnd) {
/* 344 */             Phobos.timerManager.setTimer(1.3F);
/*     */           } else {
/* 346 */             Phobos.timerManager.setTimer(1.0F);
/*     */           } 
/*     */         }
/*     */       } 
/* 350 */       EntityUtil.moveEntityStrafe(this.move, (Entity)mc.field_71439_g);
/*     */     } else {
/* 352 */       if (this.oneTime) {
/* 353 */         mc.field_71439_g.field_70181_x = -0.1D;
/* 354 */         this.oneTime = false;
/*     */       } 
/* 356 */       this.antiShake = false;
/* 357 */       this.highChainVal = 0.0D;
/* 358 */       this.lowChainVal = 0.0D;
/* 359 */       speedOff();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void doOnground() {
/* 364 */     this.bounceHeight = 0.4D;
/* 365 */     this.move = 0.26F;
/* 366 */     if (mc.field_71439_g.field_70122_E) {
/* 367 */       this.startY = mc.field_71439_g.field_70163_u;
/*     */     }
/* 369 */     if (EntityUtil.getEntitySpeed((Entity)mc.field_71439_g) <= 1.0D) {
/* 370 */       this.lowChainVal = 1.0D;
/* 371 */       this.highChainVal = 1.0D;
/*     */     } 
/* 373 */     if (EntityUtil.isEntityMoving((Entity)mc.field_71439_g) && !mc.field_71439_g.field_70123_F && !BlockUtil.isBlockAboveEntitySolid((Entity)mc.field_71439_g) && BlockUtil.isBlockBelowEntitySolid((Entity)mc.field_71439_g)) {
/* 374 */       this.oneTime = true;
/* 375 */       this.antiShake = (((Boolean)this.noShake.getValue()).booleanValue() && mc.field_71439_g.func_184187_bx() == null);
/* 376 */       Random random = new Random();
/* 377 */       boolean rnd = random.nextBoolean();
/* 378 */       if (mc.field_71439_g.field_70163_u >= this.startY + this.bounceHeight) {
/* 379 */         mc.field_71439_g.field_70181_x = -this.bounceHeight;
/* 380 */         this.lowChainVal++;
/* 381 */         if (this.lowChainVal == 1.0D) {
/* 382 */           this.move = 0.075F;
/*     */         }
/* 384 */         if (this.lowChainVal == 2.0D) {
/* 385 */           this.move = 0.175F;
/*     */         }
/* 387 */         if (this.lowChainVal == 3.0D) {
/* 388 */           this.move = 0.275F;
/*     */         }
/* 390 */         if (this.lowChainVal == 4.0D) {
/* 391 */           this.move = 0.35F;
/*     */         }
/* 393 */         if (this.lowChainVal == 5.0D) {
/* 394 */           this.move = 0.375F;
/*     */         }
/* 396 */         if (this.lowChainVal == 6.0D) {
/* 397 */           this.move = 0.4F;
/*     */         }
/* 399 */         if (this.lowChainVal == 7.0D) {
/* 400 */           this.move = 0.425F;
/*     */         }
/* 402 */         if (this.lowChainVal == 8.0D) {
/* 403 */           this.move = 0.45F;
/*     */         }
/* 405 */         if (this.lowChainVal == 9.0D) {
/* 406 */           this.move = 0.475F;
/*     */         }
/* 408 */         if (this.lowChainVal == 10.0D) {
/* 409 */           this.move = 0.5F;
/*     */         }
/* 411 */         if (this.lowChainVal == 11.0D) {
/* 412 */           this.move = 0.5F;
/*     */         }
/* 414 */         if (this.lowChainVal == 12.0D) {
/* 415 */           this.move = 0.525F;
/*     */         }
/* 417 */         if (this.lowChainVal == 13.0D) {
/* 418 */           this.move = 0.525F;
/*     */         }
/* 420 */         if (this.lowChainVal == 14.0D) {
/* 421 */           this.move = 0.535F;
/*     */         }
/* 423 */         if (this.lowChainVal == 15.0D) {
/* 424 */           this.move = 0.535F;
/*     */         }
/* 426 */         if (this.lowChainVal == 16.0D) {
/* 427 */           this.move = 0.545F;
/*     */         }
/* 429 */         if (this.lowChainVal >= 17.0D) {
/* 430 */           this.move = 0.545F;
/*     */         }
/* 432 */         if (((Boolean)this.useTimer.getValue()).booleanValue()) {
/* 433 */           Phobos.timerManager.setTimer(1.0F);
/*     */         }
/*     */       } 
/* 436 */       if (mc.field_71439_g.field_70163_u == this.startY) {
/* 437 */         mc.field_71439_g.field_70181_x = this.bounceHeight;
/* 438 */         this.highChainVal++;
/* 439 */         if (this.highChainVal == 1.0D) {
/* 440 */           this.move = 0.075F;
/*     */         }
/* 442 */         if (this.highChainVal == 2.0D) {
/* 443 */           this.move = 0.175F;
/*     */         }
/* 445 */         if (this.highChainVal == 3.0D) {
/* 446 */           this.move = 0.375F;
/*     */         }
/* 448 */         if (this.highChainVal == 4.0D) {
/* 449 */           this.move = 0.6F;
/*     */         }
/* 451 */         if (this.highChainVal == 5.0D) {
/* 452 */           this.move = 0.775F;
/*     */         }
/* 454 */         if (this.highChainVal == 6.0D) {
/* 455 */           this.move = 0.825F;
/*     */         }
/* 457 */         if (this.highChainVal == 7.0D) {
/* 458 */           this.move = 0.875F;
/*     */         }
/* 460 */         if (this.highChainVal == 8.0D) {
/* 461 */           this.move = 0.925F;
/*     */         }
/* 463 */         if (this.highChainVal == 9.0D) {
/* 464 */           this.move = 0.975F;
/*     */         }
/* 466 */         if (this.highChainVal == 10.0D) {
/* 467 */           this.move = 1.05F;
/*     */         }
/* 469 */         if (this.highChainVal == 11.0D) {
/* 470 */           this.move = 1.1F;
/*     */         }
/* 472 */         if (this.highChainVal == 12.0D) {
/* 473 */           this.move = 1.1F;
/*     */         }
/* 475 */         if (this.highChainVal == 13.0D) {
/* 476 */           this.move = 1.15F;
/*     */         }
/* 478 */         if (this.highChainVal == 14.0D) {
/* 479 */           this.move = 1.15F;
/*     */         }
/* 481 */         if (this.highChainVal == 15.0D) {
/* 482 */           this.move = 1.175F;
/*     */         }
/* 484 */         if (this.highChainVal == 16.0D) {
/* 485 */           this.move = 1.175F;
/*     */         }
/* 487 */         if (this.highChainVal >= 17.0D) {
/* 488 */           this.move = 1.2F;
/*     */         }
/* 490 */         if (((Boolean)this.useTimer.getValue()).booleanValue()) {
/* 491 */           if (rnd) {
/* 492 */             Phobos.timerManager.setTimer(1.3F);
/*     */           } else {
/* 494 */             Phobos.timerManager.setTimer(1.0F);
/*     */           } 
/*     */         }
/*     */       } 
/* 498 */       EntityUtil.moveEntityStrafe(this.move, (Entity)mc.field_71439_g);
/*     */     } else {
/* 500 */       if (this.oneTime) {
/* 501 */         mc.field_71439_g.field_70181_x = -0.1D;
/* 502 */         this.oneTime = false;
/*     */       } 
/* 504 */       this.antiShake = false;
/* 505 */       this.highChainVal = 0.0D;
/* 506 */       this.lowChainVal = 0.0D;
/* 507 */       speedOff();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 513 */     if (this.mode.getValue() == Mode.ONGROUND || this.mode.getValue() == Mode.BOOST) {
/* 514 */       mc.field_71439_g.field_70181_x = -0.1D;
/*     */     }
/* 516 */     this.changeY = false;
/* 517 */     Phobos.timerManager.setTimer(1.0F);
/* 518 */     this.highChainVal = 0.0D;
/* 519 */     this.lowChainVal = 0.0D;
/* 520 */     this.antiShake = false;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onSettingChange(ClientEvent event) {
/* 525 */     if (event.getStage() == 2 && event.getSetting().equals(this.mode) && this.mode.getPlannedValue() == Mode.INSTANT) {
/* 526 */       mc.field_71439_g.field_70181_x = -0.1D;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/* 532 */     return this.mode.currentEnumName();
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onMode(MoveEvent event) {
/* 537 */     if (!shouldReturn() && event.getStage() == 0 && this.mode.getValue() == Mode.INSTANT && !nullCheck() && !mc.field_71439_g.func_70093_af() && !mc.field_71439_g.func_70090_H() && !mc.field_71439_g.func_180799_ab() && (mc.field_71439_g.field_71158_b.field_192832_b != 0.0F || mc.field_71439_g.field_71158_b.field_78902_a != 0.0F)) {
/* 538 */       if (mc.field_71439_g.field_70122_E && ((Boolean)this.strafeJump.getValue()).booleanValue()) {
/* 539 */         mc.field_71439_g.field_70181_x = 0.4D;
/* 540 */         event.setY(0.4D);
/*     */       } 
/* 542 */       MovementInput movementInput = mc.field_71439_g.field_71158_b;
/* 543 */       float moveForward = movementInput.field_192832_b;
/* 544 */       float moveStrafe = movementInput.field_78902_a;
/* 545 */       float rotationYaw = mc.field_71439_g.field_70177_z;
/* 546 */       if (moveForward == 0.0D && moveStrafe == 0.0D) {
/* 547 */         event.setX(0.0D);
/* 548 */         event.setZ(0.0D);
/*     */       } else {
/* 550 */         if (moveForward != 0.0D) {
/* 551 */           if (moveStrafe > 0.0D) {
/* 552 */             rotationYaw += ((moveForward > 0.0D) ? -45 : 45);
/* 553 */           } else if (moveStrafe < 0.0D) {
/* 554 */             rotationYaw += ((moveForward > 0.0D) ? 45 : -45);
/*     */           } 
/* 556 */           moveStrafe = 0.0F;
/* 557 */           float f = (moveForward == 0.0F) ? moveForward : (moveForward = (moveForward > 0.0D) ? 1.0F : -1.0F);
/*     */         } 
/* 559 */         moveStrafe = (moveStrafe == 0.0F) ? moveStrafe : ((moveStrafe > 0.0D) ? 1.0F : -1.0F);
/* 560 */         event.setX(moveForward * EntityUtil.getMaxSpeed() * Math.cos(Math.toRadians((rotationYaw + 90.0F))) + moveStrafe * EntityUtil.getMaxSpeed() * Math.sin(Math.toRadians((rotationYaw + 90.0F))));
/* 561 */         event.setZ(moveForward * EntityUtil.getMaxSpeed() * Math.sin(Math.toRadians((rotationYaw + 90.0F))) - moveStrafe * EntityUtil.getMaxSpeed() * Math.cos(Math.toRadians((rotationYaw + 90.0F))));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void speedOff() {
/* 567 */     float yaw = (float)Math.toRadians(mc.field_71439_g.field_70177_z);
/* 568 */     if (BlockUtil.isBlockAboveEntitySolid((Entity)mc.field_71439_g)) {
/* 569 */       if (mc.field_71474_y.field_74351_w.func_151470_d() && !mc.field_71474_y.field_74311_E.func_151470_d() && mc.field_71439_g.field_70122_E) {
/* 570 */         mc.field_71439_g.field_70159_w -= MathUtil.sin(yaw) * 0.15D;
/* 571 */         mc.field_71439_g.field_70179_y += MathUtil.cos(yaw) * 0.15D;
/*     */       } 
/* 573 */     } else if (mc.field_71439_g.field_70123_F) {
/* 574 */       if (mc.field_71474_y.field_74351_w.func_151470_d() && !mc.field_71474_y.field_74311_E.func_151470_d() && mc.field_71439_g.field_70122_E) {
/* 575 */         mc.field_71439_g.field_70159_w -= MathUtil.sin(yaw) * 0.03D;
/* 576 */         mc.field_71439_g.field_70179_y += MathUtil.cos(yaw) * 0.03D;
/*     */       } 
/* 578 */     } else if (!BlockUtil.isBlockBelowEntitySolid((Entity)mc.field_71439_g)) {
/* 579 */       if (mc.field_71474_y.field_74351_w.func_151470_d() && !mc.field_71474_y.field_74311_E.func_151470_d() && mc.field_71439_g.field_70122_E) {
/* 580 */         mc.field_71439_g.field_70159_w -= MathUtil.sin(yaw) * 0.03D;
/* 581 */         mc.field_71439_g.field_70179_y += MathUtil.cos(yaw) * 0.03D;
/*     */       } 
/*     */     } else {
/* 584 */       mc.field_71439_g.field_70159_w = 0.0D;
/* 585 */       mc.field_71439_g.field_70179_y = 0.0D;
/*     */     } 
/*     */   }
/*     */   
/*     */   public enum Mode {
/* 590 */     INSTANT,
/* 591 */     ONGROUND,
/* 592 */     ACCEL,
/* 593 */     BOOST,
/* 594 */     VANILLA;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\movement\Speed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */