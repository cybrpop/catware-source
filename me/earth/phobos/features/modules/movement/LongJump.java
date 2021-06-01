/*     */ package me.earth.phobos.features.modules.movement;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.MoveEvent;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.Feature;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.MobEffects;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.MovementInput;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ 
/*     */ public class LongJump
/*     */   extends Module
/*     */ {
/*  31 */   private final Setting<Integer> timeout = register(new Setting("TimeOut", Integer.valueOf(2000), Integer.valueOf(0), Integer.valueOf(5000)));
/*  32 */   private final Setting<Float> boost = register(new Setting("Boost", Float.valueOf(4.48F), Float.valueOf(1.0F), Float.valueOf(20.0F)));
/*  33 */   private final Setting<Mode> mode = register(new Setting("Mode", Mode.DIRECT));
/*  34 */   private final Setting<Boolean> lagOff = register(new Setting("LagOff", Boolean.valueOf(false)));
/*  35 */   private final Setting<Boolean> autoOff = register(new Setting("AutoOff", Boolean.valueOf(false)));
/*  36 */   private final Setting<Boolean> disableStrafe = register(new Setting("DisableStrafe", Boolean.valueOf(false)));
/*  37 */   private final Setting<Boolean> strafeOff = register(new Setting("StrafeOff", Boolean.valueOf(false)));
/*  38 */   private final Setting<Boolean> step = register(new Setting("SetStep", Boolean.valueOf(false)));
/*  39 */   private final Timer timer = new Timer();
/*     */   private int stage;
/*     */   private int lastHDistance;
/*     */   private int airTicks;
/*     */   private int headStart;
/*     */   private int groundTicks;
/*     */   private double moveSpeed;
/*     */   private double lastDist;
/*     */   private boolean isSpeeding;
/*     */   private boolean beganJump = false;
/*     */   
/*     */   public LongJump() {
/*  51 */     super("LongJump", "Jumps long", Module.Category.MOVEMENT, true, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  56 */     this.timer.reset();
/*  57 */     this.headStart = 4;
/*  58 */     this.groundTicks = 0;
/*  59 */     this.stage = 0;
/*  60 */     this.beganJump = false;
/*  61 */     if (Strafe.getInstance().isOn() && ((Boolean)this.disableStrafe.getValue()).booleanValue()) {
/*  62 */       Strafe.getInstance().disable();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  68 */     Phobos.timerManager.setTimer(1.0F);
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketReceive(PacketEvent.Receive event) {
/*  73 */     if (((Boolean)this.lagOff.getValue()).booleanValue() && event.getPacket() instanceof net.minecraft.network.play.server.SPacketPlayerPosLook) {
/*  74 */       disable();
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onMove(MoveEvent event) {
/*  80 */     if (event.getStage() != 0) {
/*     */       return;
/*     */     }
/*  83 */     if (!this.timer.passedMs(((Integer)this.timeout.getValue()).intValue())) {
/*  84 */       event.setX(0.0D);
/*  85 */       event.setY(0.0D);
/*  86 */       event.setZ(0.0D);
/*     */       return;
/*     */     } 
/*  89 */     if (((Boolean)this.step.getValue()).booleanValue()) {
/*  90 */       mc.field_71439_g.field_70138_W = 0.6F;
/*     */     }
/*  92 */     doVirtue(event);
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onTickEvent(TickEvent.ClientTickEvent event) {
/*  97 */     if (Feature.fullNullCheck() || event.phase != TickEvent.Phase.START) {
/*     */       return;
/*     */     }
/* 100 */     if (Strafe.getInstance().isOn() && ((Boolean)this.strafeOff.getValue()).booleanValue()) {
/* 101 */       disable();
/*     */       return;
/*     */     } 
/* 104 */     switch ((Mode)this.mode.getValue()) {
/*     */       case TICK:
/* 106 */         doNormal((UpdateWalkingPlayerEvent)null);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/* 114 */     if (event.getStage() != 0) {
/*     */       return;
/*     */     }
/* 117 */     if (!this.timer.passedMs(((Integer)this.timeout.getValue()).intValue())) {
/* 118 */       event.setCanceled(true);
/*     */       return;
/*     */     } 
/* 121 */     doNormal(event); } private void doNormal(UpdateWalkingPlayerEvent event) { float direction; float xDir;
/*     */     float zDir;
/*     */     EntityPlayerSP player14;
/*     */     EntityPlayerSP player15;
/* 125 */     if (((Boolean)this.autoOff.getValue()).booleanValue() && this.beganJump && mc.field_71439_g.field_70122_E) {
/* 126 */       disable();
/*     */       return;
/*     */     } 
/* 129 */     switch ((Mode)this.mode.getValue()) {
/*     */       case VIRTUE:
/* 131 */         if (mc.field_71439_g.field_191988_bg != 0.0F || mc.field_71439_g.field_70702_br != 0.0F) {
/* 132 */           double xDist = mc.field_71439_g.field_70165_t - mc.field_71439_g.field_70169_q;
/* 133 */           double zDist = mc.field_71439_g.field_70161_v - mc.field_71439_g.field_70166_s;
/* 134 */           this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
/*     */           break;
/*     */         } 
/* 137 */         event.setCanceled(true);
/*     */         break;
/*     */       
/*     */       case TICK:
/* 141 */         if (event != null) {
/*     */           return;
/*     */         }
/*     */       
/*     */       case DIRECT:
/* 146 */         if (EntityUtil.isInLiquid() || EntityUtil.isOnLiquid()) {
/*     */           break;
/*     */         }
/* 149 */         if (mc.field_71439_g.field_70122_E) {
/* 150 */           this.lastHDistance = 0;
/*     */         }
/* 152 */         direction = mc.field_71439_g.field_70177_z + ((mc.field_71439_g.field_191988_bg < 0.0F) ? '´' : false) + ((mc.field_71439_g.field_70702_br > 0.0F) ? (-90.0F * ((mc.field_71439_g.field_191988_bg < 0.0F) ? -0.5F : ((mc.field_71439_g.field_191988_bg > 0.0F) ? 0.5F : 1.0F))) : 0.0F) - ((mc.field_71439_g.field_70702_br < 0.0F) ? (-90.0F * ((mc.field_71439_g.field_191988_bg < 0.0F) ? -0.5F : ((mc.field_71439_g.field_191988_bg > 0.0F) ? 0.5F : 1.0F))) : 0.0F);
/* 153 */         xDir = (float)Math.cos((direction + 90.0F) * Math.PI / 180.0D);
/* 154 */         zDir = (float)Math.sin((direction + 90.0F) * Math.PI / 180.0D);
/* 155 */         if (!mc.field_71439_g.field_70124_G) {
/* 156 */           this.airTicks++;
/* 157 */           this.isSpeeding = true;
/* 158 */           if (mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 159 */             mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(0.0D, 2.147483647E9D, 0.0D, false));
/*     */           }
/* 161 */           this.groundTicks = 0;
/* 162 */           if (!mc.field_71439_g.field_70124_G) {
/* 163 */             if (mc.field_71439_g.field_70181_x == -0.07190068807140403D) {
/* 164 */               EntityPlayerSP player = mc.field_71439_g;
/* 165 */               player.field_70181_x *= 0.3499999940395355D;
/* 166 */             } else if (mc.field_71439_g.field_70181_x == -0.10306193759436909D) {
/* 167 */               EntityPlayerSP player2 = mc.field_71439_g;
/* 168 */               player2.field_70181_x *= 0.550000011920929D;
/* 169 */             } else if (mc.field_71439_g.field_70181_x == -0.13395038817442878D) {
/* 170 */               EntityPlayerSP player3 = mc.field_71439_g;
/* 171 */               player3.field_70181_x *= 0.6700000166893005D;
/* 172 */             } else if (mc.field_71439_g.field_70181_x == -0.16635183030382D) {
/* 173 */               EntityPlayerSP player4 = mc.field_71439_g;
/* 174 */               player4.field_70181_x *= 0.6899999976158142D;
/* 175 */             } else if (mc.field_71439_g.field_70181_x == -0.19088711097794803D) {
/* 176 */               EntityPlayerSP player5 = mc.field_71439_g;
/* 177 */               player5.field_70181_x *= 0.7099999785423279D;
/* 178 */             } else if (mc.field_71439_g.field_70181_x == -0.21121925191528862D) {
/* 179 */               EntityPlayerSP player6 = mc.field_71439_g;
/* 180 */               player6.field_70181_x *= 0.20000000298023224D;
/* 181 */             } else if (mc.field_71439_g.field_70181_x == -0.11979897632390576D) {
/* 182 */               EntityPlayerSP player7 = mc.field_71439_g;
/* 183 */               player7.field_70181_x *= 0.9300000071525574D;
/* 184 */             } else if (mc.field_71439_g.field_70181_x == -0.18758479151225355D) {
/* 185 */               EntityPlayerSP player8 = mc.field_71439_g;
/* 186 */               player8.field_70181_x *= 0.7200000286102295D;
/* 187 */             } else if (mc.field_71439_g.field_70181_x == -0.21075983825251726D) {
/* 188 */               EntityPlayerSP player9 = mc.field_71439_g;
/* 189 */               player9.field_70181_x *= 0.7599999904632568D;
/*     */             } 
/* 191 */             if (mc.field_71439_g.field_70181_x < -0.2D && mc.field_71439_g.field_70181_x > -0.24D) {
/* 192 */               EntityPlayerSP player10 = mc.field_71439_g;
/* 193 */               player10.field_70181_x *= 0.7D;
/*     */             } 
/* 195 */             if (mc.field_71439_g.field_70181_x < -0.25D && mc.field_71439_g.field_70181_x > -0.32D) {
/* 196 */               EntityPlayerSP player11 = mc.field_71439_g;
/* 197 */               player11.field_70181_x *= 0.8D;
/*     */             } 
/* 199 */             if (mc.field_71439_g.field_70181_x < -0.35D && mc.field_71439_g.field_70181_x > -0.8D) {
/* 200 */               EntityPlayerSP player12 = mc.field_71439_g;
/* 201 */               player12.field_70181_x *= 0.98D;
/*     */             } 
/* 203 */             if (mc.field_71439_g.field_70181_x < -0.8D && mc.field_71439_g.field_70181_x > -1.6D) {
/* 204 */               EntityPlayerSP player13 = mc.field_71439_g;
/* 205 */               player13.field_70181_x *= 0.99D;
/*     */             } 
/*     */           } 
/* 208 */           Phobos.timerManager.setTimer(0.85F);
/* 209 */           double[] speedVals = { 0.420606D, 0.417924D, 0.415258D, 0.412609D, 0.409977D, 0.407361D, 0.404761D, 0.402178D, 0.399611D, 0.39706D, 0.394525D, 0.392D, 0.3894D, 0.38644D, 0.383655D, 0.381105D, 0.37867D, 0.37625D, 0.37384D, 0.37145D, 0.369D, 0.3666D, 0.3642D, 0.3618D, 0.35945D, 0.357D, 0.354D, 0.351D, 0.348D, 0.345D, 0.342D, 0.339D, 0.336D, 0.333D, 0.33D, 0.327D, 0.324D, 0.321D, 0.318D, 0.315D, 0.312D, 0.309D, 0.307D, 0.305D, 0.303D, 0.3D, 0.297D, 0.295D, 0.293D, 0.291D, 0.289D, 0.287D, 0.285D, 0.283D, 0.281D, 0.279D, 0.277D, 0.275D, 0.273D, 0.271D, 0.269D, 0.267D, 0.265D, 0.263D, 0.261D, 0.259D, 0.257D, 0.255D, 0.253D, 0.251D, 0.249D, 0.247D, 0.245D, 0.243D, 0.241D, 0.239D, 0.237D };
/* 210 */           if (mc.field_71474_y.field_74351_w.field_74513_e) {
/*     */             try {
/* 212 */               mc.field_71439_g.field_70159_w = xDir * speedVals[this.airTicks - 1] * 3.0D;
/* 213 */               mc.field_71439_g.field_70179_y = zDir * speedVals[this.airTicks - 1] * 3.0D;
/*     */             }
/* 215 */             catch (ArrayIndexOutOfBoundsException e) {
/*     */               return;
/*     */             }  break;
/*     */           } 
/* 219 */           mc.field_71439_g.field_70159_w = 0.0D;
/* 220 */           mc.field_71439_g.field_70179_y = 0.0D;
/*     */           break;
/*     */         } 
/* 223 */         Phobos.timerManager.setTimer(1.0F);
/* 224 */         this.airTicks = 0;
/* 225 */         this.groundTicks++;
/* 226 */         this.headStart--;
/* 227 */         player14 = mc.field_71439_g;
/* 228 */         player14.field_70159_w /= 13.0D;
/* 229 */         player15 = mc.field_71439_g;
/* 230 */         player15.field_70179_y /= 13.0D;
/* 231 */         if (this.groundTicks == 1) {
/* 232 */           updatePosition(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v);
/* 233 */           updatePosition(mc.field_71439_g.field_70165_t + 0.0624D, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v);
/* 234 */           updatePosition(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.419D, mc.field_71439_g.field_70161_v);
/* 235 */           updatePosition(mc.field_71439_g.field_70165_t + 0.0624D, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v);
/* 236 */           updatePosition(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.419D, mc.field_71439_g.field_70161_v);
/*     */           break;
/*     */         } 
/* 239 */         if (this.groundTicks > 2) {
/* 240 */           this.groundTicks = 0;
/* 241 */           mc.field_71439_g.field_70159_w = xDir * 0.3D;
/* 242 */           mc.field_71439_g.field_70179_y = zDir * 0.3D;
/* 243 */           mc.field_71439_g.field_70181_x = 0.42399999499320984D;
/* 244 */           this.beganJump = true;
/*     */         } 
/*     */         break;
/*     */     }  }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void doVirtue(MoveEvent event) {
/* 253 */     if (this.mode.getValue() == Mode.VIRTUE && (mc.field_71439_g.field_191988_bg != 0.0F || (mc.field_71439_g.field_70702_br != 0.0F && !EntityUtil.isOnLiquid() && !EntityUtil.isInLiquid()))) {
/* 254 */       if (this.stage == 0) {
/* 255 */         this.moveSpeed = ((Float)this.boost.getValue()).floatValue() * getBaseMoveSpeed();
/*     */       } else {
/* 257 */         event.setY(mc.field_71439_g.field_70181_x = 0.42D);
/* 258 */         this.moveSpeed *= 2.149D;
/* 259 */         if (this.stage == 2) {
/* 260 */           double difference = 0.66D * (this.lastDist - getBaseMoveSpeed());
/* 261 */           this.moveSpeed = this.lastDist - difference;
/*     */         } else {
/* 263 */           this.moveSpeed = this.lastDist - this.lastDist / 159.0D;
/*     */         } 
/* 265 */       }  setMoveSpeed(event, this.moveSpeed = Math.max(getBaseMoveSpeed(), this.moveSpeed));
/* 266 */       List<AxisAlignedBB> collidingList = mc.field_71441_e.func_184144_a((Entity)mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72317_d(0.0D, mc.field_71439_g.field_70181_x, 0.0D));
/* 267 */       List<AxisAlignedBB> collidingList2 = mc.field_71441_e.func_184144_a((Entity)mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72317_d(0.0D, -0.4D, 0.0D));
/* 268 */       if (!mc.field_71439_g.field_70124_G && (collidingList.size() > 0 || collidingList2.size() > 0)) {
/* 269 */         event.setY(mc.field_71439_g.field_70181_x = -0.001D);
/*     */       }
/* 271 */       this.stage++;
/* 272 */     } else if (this.stage > 0) {
/* 273 */       disable();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void invalidPacket() {
/* 278 */     updatePosition(0.0D, 2.147483647E9D, 0.0D);
/*     */   }
/*     */   
/*     */   private void updatePosition(double x, double y, double z) {
/* 282 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(x, y, z, mc.field_71439_g.field_70122_E));
/*     */   }
/*     */   
/*     */   private Block getBlock(BlockPos pos) {
/* 286 */     return mc.field_71441_e.func_180495_p(pos).func_177230_c();
/*     */   }
/*     */   
/*     */   private double getDistance(EntityPlayer player, double distance) {
/* 290 */     List<AxisAlignedBB> boundingBoxes = player.field_70170_p.func_184144_a((Entity)player, player.func_174813_aQ().func_72317_d(0.0D, -distance, 0.0D));
/* 291 */     if (boundingBoxes.isEmpty()) {
/* 292 */       return 0.0D;
/*     */     }
/* 294 */     double y = 0.0D;
/* 295 */     for (AxisAlignedBB boundingBox : boundingBoxes) {
/* 296 */       if (boundingBox.field_72337_e > y) {
/* 297 */         y = boundingBox.field_72337_e;
/*     */       }
/*     */     } 
/* 300 */     return player.field_70163_u - y;
/*     */   }
/*     */   
/*     */   private void setMoveSpeed(MoveEvent event, double speed) {
/* 304 */     MovementInput movementInput = mc.field_71439_g.field_71158_b;
/* 305 */     double forward = movementInput.field_192832_b;
/* 306 */     double strafe = movementInput.field_78902_a;
/* 307 */     float yaw = mc.field_71439_g.field_70177_z;
/* 308 */     if (forward == 0.0D && strafe == 0.0D) {
/* 309 */       event.setX(0.0D);
/* 310 */       event.setZ(0.0D);
/*     */     } else {
/* 312 */       if (forward != 0.0D) {
/* 313 */         if (strafe > 0.0D) {
/* 314 */           yaw += ((forward > 0.0D) ? -45 : 45);
/* 315 */         } else if (strafe < 0.0D) {
/* 316 */           yaw += ((forward > 0.0D) ? 45 : -45);
/*     */         } 
/* 318 */         strafe = 0.0D;
/* 319 */         if (forward > 0.0D) {
/* 320 */           forward = 1.0D;
/* 321 */         } else if (forward < 0.0D) {
/* 322 */           forward = -1.0D;
/*     */         } 
/*     */       } 
/* 325 */       event.setX(forward * speed * Math.cos(Math.toRadians((yaw + 90.0F))) + strafe * speed * Math.sin(Math.toRadians((yaw + 90.0F))));
/* 326 */       event.setZ(forward * speed * Math.sin(Math.toRadians((yaw + 90.0F))) - strafe * speed * Math.cos(Math.toRadians((yaw + 90.0F))));
/*     */     } 
/*     */   }
/*     */   
/*     */   private double getBaseMoveSpeed() {
/* 331 */     double baseSpeed = 0.2873D;
/* 332 */     if (mc.field_71439_g != null && mc.field_71439_g.func_70644_a(MobEffects.field_76424_c)) {
/* 333 */       int amplifier = ((PotionEffect)Objects.<PotionEffect>requireNonNull(mc.field_71439_g.func_70660_b(MobEffects.field_76424_c))).func_76458_c();
/* 334 */       baseSpeed *= 1.0D + 0.2D * (amplifier + 1);
/*     */     } 
/* 336 */     return baseSpeed;
/*     */   }
/*     */   
/*     */   public enum Mode {
/* 340 */     VIRTUE,
/* 341 */     DIRECT,
/* 342 */     TICK;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\movement\LongJump.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */