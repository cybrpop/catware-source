/*     */ package me.earth.phobos.features.modules.movement;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.ClientEvent;
/*     */ import me.earth.phobos.event.events.MoveEvent;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.event.events.PushEvent;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import me.earth.phobos.util.Util;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.init.MobEffects;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketConfirmTeleport;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.network.play.server.SPacketPlayerPosLook;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ 
/*     */ public class Flight extends Module {
/*  30 */   private static Flight INSTANCE = new Flight();
/*  31 */   private final Fly flySwitch = new Fly();
/*  32 */   public Setting<Mode> mode = register(new Setting("Mode", Mode.PACKET));
/*  33 */   public Setting<Boolean> better = register(new Setting("Better", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.PACKET)));
/*  34 */   public Setting<Format> format = register(new Setting("Format", Format.DAMAGE, v -> (this.mode.getValue() == Mode.DAMAGE)));
/*  35 */   public Setting<PacketMode> type = register(new Setting("Type", PacketMode.Y, v -> (this.mode.getValue() == Mode.PACKET)));
/*  36 */   public Setting<Boolean> phase = register(new Setting("Phase", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.PACKET && ((Boolean)this.better.getValue()).booleanValue())));
/*  37 */   public Setting<Float> speed = register(new Setting("Speed", Float.valueOf(0.1F), Float.valueOf(0.0F), Float.valueOf(10.0F), v -> (this.mode.getValue() == Mode.PACKET || this.mode.getValue() == Mode.DESCEND || this.mode.getValue() == Mode.DAMAGE), "The speed."));
/*  38 */   public Setting<Boolean> noKick = register(new Setting("NoKick", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.PACKET || this.mode.getValue() == Mode.DAMAGE)));
/*  39 */   public Setting<Boolean> noClip = register(new Setting("NoClip", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.DAMAGE)));
/*  40 */   public Setting<Boolean> groundSpoof = register(new Setting("GroundSpoof", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SPOOF)));
/*  41 */   public Setting<Boolean> antiGround = register(new Setting("AntiGround", Boolean.valueOf(true), v -> (this.mode.getValue() == Mode.SPOOF)));
/*  42 */   public Setting<Integer> cooldown = register(new Setting("Cooldown", Integer.valueOf(1), v -> (this.mode.getValue() == Mode.DESCEND)));
/*  43 */   public Setting<Boolean> ascend = register(new Setting("Ascend", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.DESCEND)));
/*  44 */   private final List<CPacketPlayer> packets = new ArrayList<>();
/*  45 */   private int teleportId = 0;
/*  46 */   private int counter = 0;
/*     */   private double moveSpeed;
/*     */   private double lastDist;
/*     */   private int level;
/*  50 */   private final Timer delayTimer = new Timer();
/*     */   
/*     */   public Flight() {
/*  53 */     super("Flight", "Makes you fly.", Module.Category.MOVEMENT, true, false, false);
/*  54 */     setInstance();
/*     */   }
/*     */   
/*     */   public static Flight getInstance() {
/*  58 */     if (INSTANCE == null) {
/*  59 */       INSTANCE = new Flight();
/*     */     }
/*  61 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  65 */     INSTANCE = this;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onTickEvent(TickEvent.ClientTickEvent event) {
/*  70 */     if (fullNullCheck() || this.mode.getValue() != Mode.DESCEND) {
/*     */       return;
/*     */     }
/*  73 */     if (event.phase == TickEvent.Phase.END) {
/*  74 */       if (!mc.field_71439_g.func_184613_cA()) {
/*  75 */         if (this.counter < 1) {
/*  76 */           this.counter += ((Integer)this.cooldown.getValue()).intValue();
/*  77 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, false));
/*  78 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 0.03D, mc.field_71439_g.field_70161_v, true));
/*     */         } else {
/*  80 */           this.counter--;
/*     */         } 
/*     */       }
/*     */     } else {
/*  84 */       mc.field_71439_g.field_70181_x = ((Boolean)this.ascend.getValue()).booleanValue() ? ((Float)this.speed.getValue()).floatValue() : -((Float)this.speed.getValue()).floatValue();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  91 */     if (fullNullCheck()) {
/*     */       return;
/*     */     }
/*  94 */     if (this.mode.getValue() == Mode.PACKET) {
/*  95 */       this.teleportId = 0;
/*  96 */       this.packets.clear();
/*  97 */       CPacketPlayer.Position bounds = new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, 0.0D, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E);
/*  98 */       this.packets.add(bounds);
/*  99 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)bounds);
/*     */     } 
/* 101 */     if (this.mode.getValue() == Mode.CREATIVE) {
/* 102 */       mc.field_71439_g.field_71075_bZ.field_75100_b = true;
/* 103 */       if (mc.field_71439_g.field_71075_bZ.field_75098_d) {
/*     */         return;
/*     */       }
/* 106 */       mc.field_71439_g.field_71075_bZ.field_75101_c = true;
/*     */     } 
/* 108 */     if (this.mode.getValue() == Mode.SPOOF) {
/* 109 */       this.flySwitch.enable();
/*     */     }
/* 111 */     if (this.mode.getValue() == Mode.DAMAGE) {
/* 112 */       this.level = 0;
/* 113 */       if (this.format.getValue() == Format.PACKET && mc.field_71441_e != null) {
/* 114 */         this.teleportId = 0;
/* 115 */         this.packets.clear();
/* 116 */         CPacketPlayer.Position bounds = new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, (mc.field_71439_g.field_70163_u <= 10.0D) ? 255.0D : 1.0D, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E);
/* 117 */         this.packets.add(bounds);
/* 118 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)bounds);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/* 125 */     if (this.mode.getValue() == Mode.DAMAGE) {
/* 126 */       if (this.format.getValue() == Format.DAMAGE) {
/* 127 */         if (event.getStage() == 0) {
/* 128 */           mc.field_71439_g.field_70181_x = 0.0D;
/* 129 */           double motionY = 0.41999998688697815D;
/* 130 */           if (mc.field_71439_g.field_70122_E) {
/* 131 */             if (mc.field_71439_g.func_70644_a(MobEffects.field_76430_j)) {
/* 132 */               motionY += ((mc.field_71439_g.func_70660_b(MobEffects.field_76430_j).func_76458_c() + 1) * 0.1F);
/*     */             }
/* 134 */             mc.field_71439_g.field_70181_x = motionY;
/* 135 */             Phobos.positionManager.setPlayerPosition(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70181_x, mc.field_71439_g.field_70161_v);
/* 136 */             this.moveSpeed *= 2.149D;
/*     */           } 
/*     */         } 
/* 139 */         if (mc.field_71439_g.field_70173_aa % 2 == 0) {
/* 140 */           mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + MathUtil.getRandom(1.2354235325235235E-14D, 1.2354235325235233E-13D), mc.field_71439_g.field_70161_v);
/*     */         }
/* 142 */         if (mc.field_71474_y.field_74314_A.func_151470_d()) {
/* 143 */           mc.field_71439_g.field_70181_x += (((Float)this.speed.getValue()).floatValue() / 2.0F);
/*     */         }
/* 145 */         if (mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 146 */           mc.field_71439_g.field_70181_x -= (((Float)this.speed.getValue()).floatValue() / 2.0F);
/*     */         }
/*     */       } 
/* 149 */       if (this.format.getValue() == Format.NORMAL) {
/* 150 */         mc.field_71439_g.field_70181_x = mc.field_71474_y.field_74314_A.func_151470_d() ? ((Float)this.speed.getValue()).floatValue() : (mc.field_71474_y.field_74311_E.func_151470_d() ? -((Float)this.speed.getValue()).floatValue() : 0.0D);
/* 151 */         if (((Boolean)this.noKick.getValue()).booleanValue() && mc.field_71439_g.field_70173_aa % 5 == 0) {
/* 152 */           Phobos.positionManager.setPlayerPosition(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 0.03125D, mc.field_71439_g.field_70161_v, true);
/*     */         }
/* 154 */         double[] dir = EntityUtil.forward(((Float)this.speed.getValue()).floatValue());
/* 155 */         mc.field_71439_g.field_70159_w = dir[0];
/* 156 */         mc.field_71439_g.field_70179_y = dir[1];
/*     */       } 
/* 158 */       if (this.format.getValue() == Format.PACKET) {
/* 159 */         if (this.teleportId <= 0) {
/* 160 */           CPacketPlayer.Position bounds = new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, (mc.field_71439_g.field_70163_u <= 10.0D) ? 255.0D : 1.0D, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E);
/* 161 */           this.packets.add(bounds);
/* 162 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)bounds);
/*     */           return;
/*     */         } 
/* 165 */         mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/* 166 */         double posY = -1.0E-8D;
/* 167 */         if (!mc.field_71474_y.field_74314_A.func_151470_d() && !mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 168 */           if (EntityUtil.isMoving()) {
/* 169 */             double x; for (x = 0.0625D; x < ((Float)this.speed.getValue()).floatValue(); x += 0.262D) {
/* 170 */               double[] dir = EntityUtil.forward(x);
/* 171 */               mc.field_71439_g.func_70016_h(dir[0], posY, dir[1]);
/* 172 */               move(dir[0], posY, dir[1]);
/*     */             } 
/*     */           } 
/* 175 */         } else if (mc.field_71474_y.field_74314_A.func_151470_d()) {
/* 176 */           for (int i = 0; i <= 3; i++) {
/* 177 */             mc.field_71439_g.func_70016_h(0.0D, (mc.field_71439_g.field_70173_aa % 20 == 0) ? -0.03999999910593033D : (0.062F * i), 0.0D);
/* 178 */             move(0.0D, (mc.field_71439_g.field_70173_aa % 20 == 0) ? -0.03999999910593033D : (0.062F * i), 0.0D);
/*     */           } 
/* 180 */         } else if (mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 181 */           for (int i = 0; i <= 3; i++) {
/* 182 */             mc.field_71439_g.func_70016_h(0.0D, posY - 0.0625D * i, 0.0D);
/* 183 */             move(0.0D, posY - 0.0625D * i, 0.0D);
/*     */           } 
/*     */         } 
/*     */       } 
/* 187 */       if (this.format.getValue() == Format.SLOW) {
/* 188 */         double posX = mc.field_71439_g.field_70165_t;
/* 189 */         double posY = mc.field_71439_g.field_70163_u;
/* 190 */         double posZ = mc.field_71439_g.field_70161_v;
/* 191 */         boolean ground = mc.field_71439_g.field_70122_E;
/* 192 */         mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/* 193 */         if (!mc.field_71474_y.field_74314_A.func_151470_d() && !mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 194 */           double[] dir = EntityUtil.forward(0.0625D);
/* 195 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(posX + dir[0], posY, posZ + dir[1], ground));
/* 196 */           mc.field_71439_g.func_70634_a(posX + dir[0], posY, posZ + dir[1]);
/* 197 */         } else if (mc.field_71474_y.field_74314_A.func_151470_d()) {
/* 198 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(posX, posY + 0.0625D, posZ, ground));
/* 199 */           mc.field_71439_g.func_70634_a(posX, posY + 0.0625D, posZ);
/* 200 */         } else if (mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 201 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(posX, posY - 0.0625D, posZ, ground));
/* 202 */           mc.field_71439_g.func_70634_a(posX, posY - 0.0625D, posZ);
/*     */         } 
/* 204 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(posX + mc.field_71439_g.field_70159_w, (mc.field_71439_g.field_70163_u <= 10.0D) ? 255.0D : 1.0D, posZ + mc.field_71439_g.field_70179_y, ground));
/*     */       } 
/* 206 */       if (this.format.getValue() == Format.DELAY) {
/* 207 */         if (this.delayTimer.passedMs(1000L)) {
/* 208 */           this.delayTimer.reset();
/*     */         }
/* 210 */         if (this.delayTimer.passedMs(600L)) {
/* 211 */           mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/*     */           return;
/*     */         } 
/* 214 */         if (this.teleportId <= 0) {
/* 215 */           CPacketPlayer.Position bounds = new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, (mc.field_71439_g.field_70163_u <= 10.0D) ? 255.0D : 1.0D, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E);
/* 216 */           this.packets.add(bounds);
/* 217 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)bounds);
/*     */           return;
/*     */         } 
/* 220 */         mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/* 221 */         double posY = -1.0E-8D;
/* 222 */         if (!mc.field_71474_y.field_74314_A.func_151470_d() && !mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 223 */           if (EntityUtil.isMoving()) {
/* 224 */             double[] dir = EntityUtil.forward(0.2D);
/* 225 */             mc.field_71439_g.func_70016_h(dir[0], posY, dir[1]);
/* 226 */             move(dir[0], posY, dir[1]);
/*     */           } 
/* 228 */         } else if (mc.field_71474_y.field_74314_A.func_151470_d()) {
/* 229 */           mc.field_71439_g.func_70016_h(0.0D, 0.06199999898672104D, 0.0D);
/* 230 */           move(0.0D, 0.06199999898672104D, 0.0D);
/* 231 */         } else if (mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 232 */           mc.field_71439_g.func_70016_h(0.0D, 0.0625D, 0.0D);
/* 233 */           move(0.0D, 0.0625D, 0.0D);
/*     */         } 
/*     */       } 
/* 236 */       if (((Boolean)this.noClip.getValue()).booleanValue()) {
/* 237 */         mc.field_71439_g.field_70145_X = true;
/*     */       }
/*     */     } 
/* 240 */     if (event.getStage() == 0) {
/* 241 */       if (this.mode.getValue() == Mode.CREATIVE) {
/* 242 */         mc.field_71439_g.field_71075_bZ.func_75092_a(((Float)this.speed.getValue()).floatValue());
/* 243 */         mc.field_71439_g.field_71075_bZ.field_75100_b = true;
/* 244 */         if (mc.field_71439_g.field_71075_bZ.field_75098_d) {
/*     */           return;
/*     */         }
/* 247 */         mc.field_71439_g.field_71075_bZ.field_75101_c = true;
/*     */       } 
/* 249 */       if (this.mode.getValue() == Mode.VANILLA) {
/* 250 */         mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/* 251 */         mc.field_71439_g.field_70747_aH = ((Float)this.speed.getValue()).floatValue();
/* 252 */         if (((Boolean)this.noKick.getValue()).booleanValue() && mc.field_71439_g.field_70173_aa % 4 == 0) {
/* 253 */           mc.field_71439_g.field_70181_x = -0.03999999910593033D;
/*     */         }
/* 255 */         double[] dir = MathUtil.directionSpeed(((Float)this.speed.getValue()).floatValue());
/* 256 */         if (mc.field_71439_g.field_71158_b.field_78902_a != 0.0F || mc.field_71439_g.field_71158_b.field_192832_b != 0.0F) {
/* 257 */           mc.field_71439_g.field_70159_w = dir[0];
/* 258 */           mc.field_71439_g.field_70179_y = dir[1];
/*     */         } else {
/* 260 */           mc.field_71439_g.field_70159_w = 0.0D;
/* 261 */           mc.field_71439_g.field_70179_y = 0.0D;
/*     */         } 
/* 263 */         if (mc.field_71474_y.field_74314_A.func_151470_d()) {
/* 264 */           mc.field_71439_g.field_70181_x = ((Boolean)this.noKick.getValue()).booleanValue() ? ((mc.field_71439_g.field_70173_aa % 20 == 0) ? -0.03999999910593033D : ((Float)this.speed.getValue()).floatValue()) : (mc.field_71439_g.field_70181_x += ((Float)this.speed.getValue()).floatValue());
/*     */         }
/* 266 */         if (mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 267 */           mc.field_71439_g.field_70181_x -= ((Float)this.speed.getValue()).floatValue();
/*     */         }
/*     */       } 
/* 270 */       if (this.mode.getValue() == Mode.PACKET && !((Boolean)this.better.getValue()).booleanValue()) {
/* 271 */         doNormalPacketFly();
/*     */       }
/* 273 */       if (this.mode.getValue() == Mode.PACKET && ((Boolean)this.better.getValue()).booleanValue()) {
/* 274 */         doBetterPacketFly();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void doNormalPacketFly() {
/* 280 */     if (this.teleportId <= 0) {
/* 281 */       CPacketPlayer.Position bounds = new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, 0.0D, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E);
/* 282 */       this.packets.add(bounds);
/* 283 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)bounds);
/*     */       return;
/*     */     } 
/* 286 */     mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/* 287 */     if (mc.field_71441_e.func_184144_a((Entity)mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72321_a(-0.0625D, 0.0D, -0.0625D)).isEmpty()) {
/* 288 */       double ySpeed = 0.0D;
/* 289 */       ySpeed = mc.field_71474_y.field_74314_A.func_151470_d() ? (((Boolean)this.noKick.getValue()).booleanValue() ? ((mc.field_71439_g.field_70173_aa % 20 == 0) ? -0.03999999910593033D : 0.06199999898672104D) : 0.06199999898672104D) : (mc.field_71474_y.field_74311_E.func_151470_d() ? -0.062D : (mc.field_71441_e.func_184144_a((Entity)mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72321_a(-0.0625D, -0.0625D, -0.0625D)).isEmpty() ? ((mc.field_71439_g.field_70173_aa % 4 == 0) ? (((Boolean)this.noKick.getValue()).booleanValue() ? -0.04F : 0.0F) : 0.0D) : 0.0D));
/* 290 */       double[] directionalSpeed = MathUtil.directionSpeed(((Float)this.speed.getValue()).floatValue());
/* 291 */       if (mc.field_71474_y.field_74314_A.func_151470_d() || mc.field_71474_y.field_74311_E.func_151470_d() || mc.field_71474_y.field_74351_w.func_151470_d() || mc.field_71474_y.field_74368_y.func_151470_d() || mc.field_71474_y.field_74366_z.func_151470_d() || mc.field_71474_y.field_74370_x.func_151470_d()) {
/* 292 */         if (directionalSpeed[0] != 0.0D || directionalSpeed[1] != 0.0D) {
/* 293 */           if (mc.field_71439_g.field_71158_b.field_78901_c && (mc.field_71439_g.field_70702_br != 0.0F || mc.field_71439_g.field_191988_bg != 0.0F)) {
/* 294 */             mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/* 295 */             move(0.0D, 0.0D, 0.0D);
/* 296 */             for (int i = 0; i <= 3; i++) {
/* 297 */               mc.field_71439_g.func_70016_h(0.0D, ySpeed * i, 0.0D);
/* 298 */               move(0.0D, ySpeed * i, 0.0D);
/*     */             } 
/* 300 */           } else if (mc.field_71439_g.field_71158_b.field_78901_c) {
/* 301 */             mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/* 302 */             move(0.0D, 0.0D, 0.0D);
/* 303 */             for (int i = 0; i <= 3; i++) {
/* 304 */               mc.field_71439_g.func_70016_h(0.0D, ySpeed * i, 0.0D);
/* 305 */               move(0.0D, ySpeed * i, 0.0D);
/*     */             } 
/*     */           } else {
/* 308 */             for (int i = 0; i <= 2; i++) {
/* 309 */               mc.field_71439_g.func_70016_h(directionalSpeed[0] * i, ySpeed * i, directionalSpeed[1] * i);
/* 310 */               move(directionalSpeed[0] * i, ySpeed * i, directionalSpeed[1] * i);
/*     */             } 
/*     */           } 
/*     */         }
/* 314 */       } else if (((Boolean)this.noKick.getValue()).booleanValue() && mc.field_71441_e.func_184144_a((Entity)mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72321_a(-0.0625D, -0.0625D, -0.0625D)).isEmpty()) {
/* 315 */         mc.field_71439_g.func_70016_h(0.0D, (mc.field_71439_g.field_70173_aa % 2 == 0) ? 0.03999999910593033D : -0.03999999910593033D, 0.0D);
/* 316 */         move(0.0D, (mc.field_71439_g.field_70173_aa % 2 == 0) ? 0.03999999910593033D : -0.03999999910593033D, 0.0D);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void doBetterPacketFly() {
/* 322 */     if (this.teleportId <= 0) {
/* 323 */       CPacketPlayer.Position bounds = new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, 10000.0D, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E);
/* 324 */       this.packets.add(bounds);
/* 325 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)bounds);
/*     */       return;
/*     */     } 
/* 328 */     mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/* 329 */     if (mc.field_71441_e.func_184144_a((Entity)mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72321_a(-0.0625D, 0.0D, -0.0625D)).isEmpty()) {
/* 330 */       double ySpeed = 0.0D;
/* 331 */       ySpeed = mc.field_71474_y.field_74314_A.func_151470_d() ? (((Boolean)this.noKick.getValue()).booleanValue() ? ((mc.field_71439_g.field_70173_aa % 20 == 0) ? -0.03999999910593033D : 0.06199999898672104D) : 0.06199999898672104D) : (mc.field_71474_y.field_74311_E.func_151470_d() ? -0.062D : (mc.field_71441_e.func_184144_a((Entity)mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72321_a(-0.0625D, -0.0625D, -0.0625D)).isEmpty() ? ((mc.field_71439_g.field_70173_aa % 4 == 0) ? (((Boolean)this.noKick.getValue()).booleanValue() ? -0.04F : 0.0F) : 0.0D) : 0.0D));
/* 332 */       double[] directionalSpeed = MathUtil.directionSpeed(((Float)this.speed.getValue()).floatValue());
/* 333 */       if (mc.field_71474_y.field_74314_A.func_151470_d() || mc.field_71474_y.field_74311_E.func_151470_d() || mc.field_71474_y.field_74351_w.func_151470_d() || mc.field_71474_y.field_74368_y.func_151470_d() || mc.field_71474_y.field_74366_z.func_151470_d() || mc.field_71474_y.field_74370_x.func_151470_d()) {
/* 334 */         if (directionalSpeed[0] != 0.0D || directionalSpeed[1] != 0.0D) {
/* 335 */           if (mc.field_71439_g.field_71158_b.field_78901_c && (mc.field_71439_g.field_70702_br != 0.0F || mc.field_71439_g.field_191988_bg != 0.0F)) {
/* 336 */             mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/* 337 */             move(0.0D, 0.0D, 0.0D);
/* 338 */             for (int i = 0; i <= 3; i++) {
/* 339 */               mc.field_71439_g.func_70016_h(0.0D, ySpeed * i, 0.0D);
/* 340 */               move(0.0D, ySpeed * i, 0.0D);
/*     */             } 
/* 342 */           } else if (mc.field_71439_g.field_71158_b.field_78901_c) {
/* 343 */             mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/* 344 */             move(0.0D, 0.0D, 0.0D);
/* 345 */             for (int i = 0; i <= 3; i++) {
/* 346 */               mc.field_71439_g.func_70016_h(0.0D, ySpeed * i, 0.0D);
/* 347 */               move(0.0D, ySpeed * i, 0.0D);
/*     */             } 
/*     */           } else {
/* 350 */             for (int i = 0; i <= 2; i++) {
/* 351 */               mc.field_71439_g.func_70016_h(directionalSpeed[0] * i, ySpeed * i, directionalSpeed[1] * i);
/* 352 */               move(directionalSpeed[0] * i, ySpeed * i, directionalSpeed[1] * i);
/*     */             } 
/*     */           } 
/*     */         }
/* 356 */       } else if (((Boolean)this.noKick.getValue()).booleanValue() && mc.field_71441_e.func_184144_a((Entity)mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72321_a(-0.0625D, -0.0625D, -0.0625D)).isEmpty()) {
/* 357 */         mc.field_71439_g.func_70016_h(0.0D, (mc.field_71439_g.field_70173_aa % 2 == 0) ? 0.03999999910593033D : -0.03999999910593033D, 0.0D);
/* 358 */         move(0.0D, (mc.field_71439_g.field_70173_aa % 2 == 0) ? 0.03999999910593033D : -0.03999999910593033D, 0.0D);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/* 365 */     if (this.mode.getValue() == Mode.SPOOF) {
/* 366 */       if (fullNullCheck()) {
/*     */         return;
/*     */       }
/* 369 */       if (!mc.field_71439_g.field_71075_bZ.field_75101_c) {
/* 370 */         this.flySwitch.disable();
/* 371 */         this.flySwitch.enable();
/* 372 */         mc.field_71439_g.field_71075_bZ.field_75100_b = false;
/*     */       } 
/* 374 */       mc.field_71439_g.field_71075_bZ.func_75092_a(0.05F * ((Float)this.speed.getValue()).floatValue());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 380 */     if (this.mode.getValue() == Mode.CREATIVE && mc.field_71439_g != null) {
/* 381 */       mc.field_71439_g.field_71075_bZ.field_75100_b = false;
/* 382 */       mc.field_71439_g.field_71075_bZ.func_75092_a(0.05F);
/* 383 */       if (mc.field_71439_g.field_71075_bZ.field_75098_d) {
/*     */         return;
/*     */       }
/* 386 */       mc.field_71439_g.field_71075_bZ.field_75101_c = false;
/*     */     } 
/* 388 */     if (this.mode.getValue() == Mode.SPOOF) {
/* 389 */       this.flySwitch.disable();
/*     */     }
/* 391 */     if (this.mode.getValue() == Mode.DAMAGE) {
/* 392 */       Phobos.timerManager.reset();
/* 393 */       mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/* 394 */       this.moveSpeed = Strafe.getBaseMoveSpeed();
/* 395 */       this.lastDist = 0.0D;
/* 396 */       if (((Boolean)this.noClip.getValue()).booleanValue()) {
/* 397 */         mc.field_71439_g.field_70145_X = false;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/* 404 */     return this.mode.currentEnumName();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLogout() {
/* 409 */     if (isOn()) {
/* 410 */       disable();
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onMove(MoveEvent event) {
/* 416 */     if (event.getStage() == 0 && this.mode.getValue() == Mode.DAMAGE && this.format.getValue() == Format.DAMAGE) {
/* 417 */       double forward = mc.field_71439_g.field_71158_b.field_192832_b;
/* 418 */       double strafe = mc.field_71439_g.field_71158_b.field_78902_a;
/* 419 */       float yaw = mc.field_71439_g.field_70177_z;
/* 420 */       if (forward == 0.0D && strafe == 0.0D) {
/* 421 */         event.setX(0.0D);
/* 422 */         event.setZ(0.0D);
/*     */       } 
/* 424 */       if (forward != 0.0D && strafe != 0.0D) {
/* 425 */         forward *= Math.sin(0.7853981633974483D);
/* 426 */         strafe *= Math.cos(0.7853981633974483D);
/*     */       } 
/* 428 */       if (this.level != 1 || (mc.field_71439_g.field_191988_bg == 0.0F && mc.field_71439_g.field_70702_br == 0.0F)) {
/* 429 */         if (this.level == 2) {
/* 430 */           this.level++;
/* 431 */         } else if (this.level == 3) {
/* 432 */           this.level++;
/* 433 */           double difference = ((mc.field_71439_g.field_70173_aa % 2 == 0) ? -0.05D : 0.1D) * (this.lastDist - Strafe.getBaseMoveSpeed());
/* 434 */           this.moveSpeed = this.lastDist - difference;
/*     */         } else {
/* 436 */           if (mc.field_71441_e.func_184144_a((Entity)mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72317_d(0.0D, mc.field_71439_g.field_70181_x, 0.0D)).size() > 0 || mc.field_71439_g.field_70124_G) {
/* 437 */             this.level = 1;
/*     */           }
/* 439 */           this.moveSpeed = this.lastDist - this.lastDist / 159.0D;
/*     */         } 
/*     */       } else {
/* 442 */         this.level = 2;
/* 443 */         double boost = mc.field_71439_g.func_70644_a(MobEffects.field_76424_c) ? 1.86D : 2.05D;
/* 444 */         this.moveSpeed = boost * Strafe.getBaseMoveSpeed() - 0.01D;
/*     */       } 
/* 446 */       this.moveSpeed = Math.max(this.moveSpeed, Strafe.getBaseMoveSpeed());
/* 447 */       double mx = -Math.sin(Math.toRadians(yaw));
/* 448 */       double mz = Math.cos(Math.toRadians(yaw));
/* 449 */       event.setX(forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz);
/* 450 */       event.setZ(forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx);
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketSend(PacketEvent.Send event) {
/* 456 */     if (event.getStage() == 0) {
/*     */       
/* 458 */       if (this.mode.getValue() == Mode.PACKET) {
/* 459 */         if (fullNullCheck()) {
/*     */           return;
/*     */         }
/* 462 */         if (event.getPacket() instanceof CPacketPlayer && !(event.getPacket() instanceof CPacketPlayer.Position)) {
/* 463 */           event.setCanceled(true);
/*     */         }
/* 465 */         if (event.getPacket() instanceof CPacketPlayer) {
/* 466 */           CPacketPlayer packet = (CPacketPlayer)event.getPacket();
/* 467 */           if (this.packets.contains(packet)) {
/* 468 */             this.packets.remove(packet);
/*     */             return;
/*     */           } 
/* 471 */           event.setCanceled(true);
/*     */         } 
/*     */       } 
/* 474 */       if (this.mode.getValue() == Mode.SPOOF) {
/* 475 */         if (fullNullCheck()) {
/*     */           return;
/*     */         }
/* 478 */         if (!((Boolean)this.groundSpoof.getValue()).booleanValue() || !(event.getPacket() instanceof CPacketPlayer) || !mc.field_71439_g.field_71075_bZ.field_75100_b) {
/*     */           return;
/*     */         }
/* 481 */         CPacketPlayer packet = (CPacketPlayer)event.getPacket();
/* 482 */         if (!packet.field_149480_h) {
/*     */           return;
/*     */         }
/* 485 */         AxisAlignedBB range = mc.field_71439_g.func_174813_aQ().func_72321_a(0.0D, -mc.field_71439_g.field_70163_u, 0.0D).func_191195_a(0.0D, -mc.field_71439_g.field_70131_O, 0.0D);
/* 486 */         List<AxisAlignedBB> collisionBoxes = mc.field_71439_g.field_70170_p.func_184144_a((Entity)mc.field_71439_g, range);
/* 487 */         AtomicReference<Double> newHeight = new AtomicReference<>(Double.valueOf(0.0D));
/* 488 */         collisionBoxes.forEach(box -> newHeight.set(Double.valueOf(Math.max(((Double)newHeight.get()).doubleValue(), box.field_72337_e))));
/* 489 */         packet.field_149477_b = ((Double)newHeight.get()).doubleValue();
/* 490 */         packet.field_149474_g = true;
/*     */       } 
/* 492 */       if (this.mode.getValue() == Mode.DAMAGE && (this.format.getValue() == Format.PACKET || this.format.getValue() == Format.DELAY)) {
/* 493 */         if (event.getPacket() instanceof CPacketPlayer && !(event.getPacket() instanceof CPacketPlayer.Position)) {
/* 494 */           event.setCanceled(true);
/*     */         }
/* 496 */         if (event.getPacket() instanceof CPacketPlayer) {
/* 497 */           CPacketPlayer packet = (CPacketPlayer)event.getPacket();
/* 498 */           if (this.packets.contains(packet)) {
/* 499 */             this.packets.remove(packet);
/*     */             return;
/*     */           } 
/* 502 */           event.setCanceled(true);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketReceive(PacketEvent.Receive event) {
/* 510 */     if (event.getStage() == 0) {
/*     */       
/* 512 */       if (this.mode.getValue() == Mode.PACKET) {
/* 513 */         if (fullNullCheck()) {
/*     */           return;
/*     */         }
/* 516 */         if (event.getPacket() instanceof SPacketPlayerPosLook) {
/* 517 */           SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
/* 518 */           if (mc.field_71439_g.func_70089_S() && mc.field_71441_e.func_175667_e(new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v)) && !(mc.field_71462_r instanceof net.minecraft.client.gui.GuiDownloadTerrain)) {
/* 519 */             if (this.teleportId <= 0) {
/* 520 */               this.teleportId = packet.func_186965_f();
/*     */             } else {
/* 522 */               event.setCanceled(true);
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/* 527 */       if (this.mode.getValue() == Mode.SPOOF) {
/* 528 */         if (fullNullCheck()) {
/*     */           return;
/*     */         }
/* 531 */         if (!((Boolean)this.antiGround.getValue()).booleanValue() || !(event.getPacket() instanceof SPacketPlayerPosLook) || !mc.field_71439_g.field_71075_bZ.field_75100_b) {
/*     */           return;
/*     */         }
/* 534 */         SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
/* 535 */         double oldY = mc.field_71439_g.field_70163_u;
/* 536 */         mc.field_71439_g.func_70107_b(packet.field_148940_a, packet.field_148938_b, packet.field_148939_c);
/* 537 */         AxisAlignedBB range = mc.field_71439_g.func_174813_aQ().func_72321_a(0.0D, (256.0F - mc.field_71439_g.field_70131_O) - mc.field_71439_g.field_70163_u, 0.0D).func_191195_a(0.0D, mc.field_71439_g.field_70131_O, 0.0D);
/* 538 */         List<AxisAlignedBB> collisionBoxes = mc.field_71439_g.field_70170_p.func_184144_a((Entity)mc.field_71439_g, range);
/* 539 */         AtomicReference<Double> newY = new AtomicReference<>(Double.valueOf(256.0D));
/* 540 */         collisionBoxes.forEach(box -> newY.set(Double.valueOf(Math.min(((Double)newY.get()).doubleValue(), box.field_72338_b - mc.field_71439_g.field_70131_O))));
/* 541 */         packet.field_148938_b = Math.min(oldY, ((Double)newY.get()).doubleValue());
/*     */       } 
/* 543 */       if (this.mode.getValue() == Mode.DAMAGE && (this.format.getValue() == Format.PACKET || this.format.getValue() == Format.DELAY) && event.getPacket() instanceof SPacketPlayerPosLook) {
/* 544 */         SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
/* 545 */         if (mc.field_71439_g.func_70089_S() && mc.field_71441_e.func_175667_e(new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v)) && !(mc.field_71462_r instanceof net.minecraft.client.gui.GuiDownloadTerrain)) {
/* 546 */           if (this.teleportId <= 0) {
/* 547 */             this.teleportId = packet.func_186965_f();
/*     */           } else {
/* 549 */             event.setCanceled(true);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onSettingChange(ClientEvent event) {
/* 558 */     if (event.getStage() == 2 && event.getSetting() != null && event.getSetting().getFeature() != null && event.getSetting().getFeature().equals(this) && isEnabled() && !event.getSetting().equals(this.enabled)) {
/* 559 */       disable();
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPush(PushEvent event) {
/* 565 */     if (event.getStage() == 1 && this.mode.getValue() == Mode.PACKET && ((Boolean)this.better.getValue()).booleanValue() && ((Boolean)this.phase.getValue()).booleanValue()) {
/* 566 */       event.setCanceled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   private void move(double x, double y, double z) {
/* 571 */     CPacketPlayer.Position pos = new CPacketPlayer.Position(mc.field_71439_g.field_70165_t + x, mc.field_71439_g.field_70163_u + y, mc.field_71439_g.field_70161_v + z, mc.field_71439_g.field_70122_E);
/* 572 */     this.packets.add(pos);
/* 573 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)pos);
/* 574 */     Object bounds = ((Boolean)this.better.getValue()).booleanValue() ? createBoundsPacket(x, y, z) : new CPacketPlayer.Position(mc.field_71439_g.field_70165_t + x, 0.0D, mc.field_71439_g.field_70161_v + z, mc.field_71439_g.field_70122_E);
/* 575 */     this.packets.add((CPacketPlayer)bounds);
/* 576 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)bounds);
/* 577 */     this.teleportId++;
/* 578 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketConfirmTeleport(this.teleportId - 1));
/* 579 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketConfirmTeleport(this.teleportId));
/* 580 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketConfirmTeleport(this.teleportId + 1));
/*     */   }
/*     */   
/*     */   private CPacketPlayer createBoundsPacket(double x, double y, double z) {
/* 584 */     switch ((PacketMode)this.type.getValue()) {
/*     */       case Up:
/* 586 */         return (CPacketPlayer)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t + x, 10000.0D, mc.field_71439_g.field_70161_v + z, mc.field_71439_g.field_70122_E);
/*     */       
/*     */       case Down:
/* 589 */         return (CPacketPlayer)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t + x, -10000.0D, mc.field_71439_g.field_70161_v + z, mc.field_71439_g.field_70122_E);
/*     */       
/*     */       case Zero:
/* 592 */         return (CPacketPlayer)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t + x, 0.0D, mc.field_71439_g.field_70161_v + z, mc.field_71439_g.field_70122_E);
/*     */       
/*     */       case Y:
/* 595 */         return (CPacketPlayer)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t + x, (mc.field_71439_g.field_70163_u + y <= 10.0D) ? 255.0D : 1.0D, mc.field_71439_g.field_70161_v + z, mc.field_71439_g.field_70122_E);
/*     */       
/*     */       case X:
/* 598 */         return (CPacketPlayer)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t + x + 75.0D, mc.field_71439_g.field_70163_u + y, mc.field_71439_g.field_70161_v + z, mc.field_71439_g.field_70122_E);
/*     */       
/*     */       case Z:
/* 601 */         return (CPacketPlayer)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t + x, mc.field_71439_g.field_70163_u + y, mc.field_71439_g.field_70161_v + z + 75.0D, mc.field_71439_g.field_70122_E);
/*     */       
/*     */       case XZ:
/* 604 */         return (CPacketPlayer)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t + x + 75.0D, mc.field_71439_g.field_70163_u + y, mc.field_71439_g.field_70161_v + z + 75.0D, mc.field_71439_g.field_70122_E);
/*     */     } 
/*     */     
/* 607 */     return (CPacketPlayer)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t + x, 2000.0D, mc.field_71439_g.field_70161_v + z, mc.field_71439_g.field_70122_E);
/*     */   }
/*     */   
/*     */   private enum PacketMode {
/* 611 */     Up,
/* 612 */     Down,
/* 613 */     Zero,
/* 614 */     Y,
/* 615 */     X,
/* 616 */     Z,
/* 617 */     XZ;
/*     */   }
/*     */   
/*     */   public enum Format
/*     */   {
/* 622 */     DAMAGE,
/* 623 */     SLOW,
/* 624 */     DELAY,
/* 625 */     NORMAL,
/* 626 */     PACKET;
/*     */   }
/*     */   
/*     */   public enum Mode
/*     */   {
/* 631 */     CREATIVE,
/* 632 */     VANILLA,
/* 633 */     PACKET,
/* 634 */     SPOOF,
/* 635 */     DESCEND,
/* 636 */     DAMAGE;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class Fly
/*     */   {
/*     */     private Fly() {}
/*     */     
/*     */     protected void enable() {
/* 645 */       Util.mc.func_152344_a(() -> {
/*     */             if (Util.mc.field_71439_g == null || Util.mc.field_71439_g.field_71075_bZ == null) {
/*     */               return;
/*     */             }
/*     */             Util.mc.field_71439_g.field_71075_bZ.field_75101_c = true;
/*     */             Util.mc.field_71439_g.field_71075_bZ.field_75100_b = true;
/*     */           });
/*     */     }
/*     */     
/*     */     protected void disable() {
/* 655 */       Util.mc.func_152344_a(() -> {
/*     */             if (Util.mc.field_71439_g == null || Util.mc.field_71439_g.field_71075_bZ == null) {
/*     */               return;
/*     */             }
/*     */             PlayerCapabilities gmCaps = new PlayerCapabilities();
/*     */             Util.mc.field_71442_b.func_178889_l().func_77147_a(gmCaps);
/*     */             PlayerCapabilities capabilities = Util.mc.field_71439_g.field_71075_bZ;
/*     */             capabilities.field_75101_c = gmCaps.field_75101_c;
/* 663 */             capabilities.field_75100_b = (gmCaps.field_75101_c && capabilities.field_75100_b);
/*     */             capabilities.func_75092_a(gmCaps.func_75093_a());
/*     */           });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\movement\Flight.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */