/*     */ package me.earth.phobos.features.modules.combat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.stream.Collectors;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.item.EntityEnderCrystal;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.init.MobEffects;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
/*     */ import net.minecraft.network.play.client.CPacketUseEntity;
/*     */ import net.minecraft.network.play.server.SPacketSpawnObject;
/*     */ import net.minecraft.util.CombatRules;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.NonNullList;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.util.math.RayTraceResult;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.world.Explosion;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class AutoCrystalCustom extends Module {
/*  39 */   private final Timer placeTimer = new Timer();
/*  40 */   private final Timer breakTimer = new Timer();
/*  41 */   private final Timer preditTimer = new Timer();
/*  42 */   private final Timer manualTimer = new Timer();
/*  43 */   private final Setting<Integer> attackFactor = register(new Setting("PredictDelay", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(200)));
/*  44 */   private final Setting<Integer> red = register(new Setting("Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255)));
/*  45 */   private final Setting<Integer> green = register(new Setting("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/*  46 */   private final Setting<Integer> blue = register(new Setting("Blue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255)));
/*  47 */   private final Setting<Integer> alpha = register(new Setting("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/*  48 */   private final Setting<Integer> boxAlpha = register(new Setting("BoxAlpha", Integer.valueOf(125), Integer.valueOf(0), Integer.valueOf(255)));
/*  49 */   private final Setting<Float> lineWidth = register(new Setting("LineWidth", Float.valueOf(1.0F), Float.valueOf(0.1F), Float.valueOf(5.0F)));
/*  50 */   public Setting<Boolean> place = register(new Setting("Place", Boolean.valueOf(true)));
/*  51 */   public Setting<Float> placeDelay = register(new Setting("PlaceDelay", Float.valueOf(4.0F), Float.valueOf(0.0F), Float.valueOf(300.0F)));
/*  52 */   public Setting<Float> placeRange = register(new Setting("PlaceRange", Float.valueOf(4.0F), Float.valueOf(0.1F), Float.valueOf(7.0F)));
/*  53 */   public Setting<Boolean> explode = register(new Setting("Break", Boolean.valueOf(true)));
/*  54 */   public Setting<Boolean> packetBreak = register(new Setting("PacketBreak", Boolean.valueOf(true)));
/*  55 */   public Setting<Boolean> predicts = register(new Setting("Predict", Boolean.valueOf(true)));
/*  56 */   public Setting<Boolean> rotate = register(new Setting("Rotate", Boolean.valueOf(true)));
/*  57 */   public Setting<Float> breakDelay = register(new Setting("BreakDelay", Float.valueOf(4.0F), Float.valueOf(0.0F), Float.valueOf(300.0F)));
/*  58 */   public Setting<Float> breakRange = register(new Setting("BreakRange", Float.valueOf(4.0F), Float.valueOf(0.1F), Float.valueOf(7.0F)));
/*  59 */   public Setting<Float> breakWallRange = register(new Setting("BreakWallRange", Float.valueOf(4.0F), Float.valueOf(0.1F), Float.valueOf(7.0F)));
/*  60 */   public Setting<Boolean> opPlace = register(new Setting("1.13 Place", Boolean.valueOf(true)));
/*  61 */   public Setting<Boolean> suicide = register(new Setting("AntiSuicide", Boolean.valueOf(true)));
/*  62 */   public Setting<Boolean> autoswitch = register(new Setting("AutoSwitch", Boolean.valueOf(true)));
/*  63 */   public Setting<Boolean> ignoreUseAmount = register(new Setting("IgnoreUseAmount", Boolean.valueOf(true)));
/*  64 */   public Setting<Integer> wasteAmount = register(new Setting("UseAmount", Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(5)));
/*  65 */   public Setting<Boolean> facePlaceSword = register(new Setting("FacePlaceSword", Boolean.valueOf(true)));
/*  66 */   public Setting<Float> targetRange = register(new Setting("TargetRange", Float.valueOf(4.0F), Float.valueOf(1.0F), Float.valueOf(12.0F)));
/*  67 */   public Setting<Float> minDamage = register(new Setting("MinDamage", Float.valueOf(4.0F), Float.valueOf(0.1F), Float.valueOf(20.0F)));
/*  68 */   public Setting<Float> facePlace = register(new Setting("FacePlaceHP", Float.valueOf(4.0F), Float.valueOf(0.0F), Float.valueOf(36.0F)));
/*  69 */   public Setting<Float> breakMaxSelfDamage = register(new Setting("BreakMaxSelf", Float.valueOf(4.0F), Float.valueOf(0.1F), Float.valueOf(12.0F)));
/*  70 */   public Setting<Float> breakMinDmg = register(new Setting("BreakMinDmg", Float.valueOf(4.0F), Float.valueOf(0.1F), Float.valueOf(7.0F)));
/*  71 */   public Setting<Float> minArmor = register(new Setting("MinArmor", Float.valueOf(4.0F), Float.valueOf(0.1F), Float.valueOf(80.0F)));
/*  72 */   public Setting<SwingMode> swingMode = register(new Setting("Swing", SwingMode.MainHand));
/*  73 */   public Setting<Boolean> render = register(new Setting("Render", Boolean.valueOf(true)));
/*  74 */   public Setting<Boolean> renderDmg = register(new Setting("RenderDmg", Boolean.valueOf(true)));
/*  75 */   public Setting<Boolean> box = register(new Setting("Box", Boolean.valueOf(true)));
/*  76 */   public Setting<Boolean> outline = register(new Setting("Outline", Boolean.valueOf(true)));
/*  77 */   private final Setting<Integer> cRed = register(new Setting("OL-Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.outline.getValue()).booleanValue()));
/*  78 */   private final Setting<Integer> cGreen = register(new Setting("OL-Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.outline.getValue()).booleanValue()));
/*  79 */   private final Setting<Integer> cBlue = register(new Setting("OL-Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.outline.getValue()).booleanValue()));
/*  80 */   private final Setting<Integer> cAlpha = register(new Setting("OL-Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.outline.getValue()).booleanValue()));
/*     */   EntityEnderCrystal crystal;
/*     */   private EntityLivingBase target;
/*     */   private BlockPos pos;
/*     */   private int hotBarSlot;
/*     */   private boolean armor;
/*     */   private boolean armorTarget;
/*     */   private int crystalCount;
/*     */   private int predictWait;
/*     */   private int predictPackets;
/*     */   private boolean packetCalc;
/*  91 */   private float yaw = 0.0F;
/*     */   private EntityLivingBase realTarget;
/*     */   private int predict;
/*  94 */   private float pitch = 0.0F;
/*     */   private boolean rotating = false;
/*     */   
/*     */   public AutoCrystalCustom() {
/*  98 */     super("AutoCrystal2.0", "Automatically places crystals (custom version)", Module.Category.COMBAT, true, false, false);
/*     */   }
/*     */   
/*     */   public static List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
/* 102 */     ArrayList<BlockPos> circleblocks = new ArrayList<>();
/* 103 */     int cx = loc.func_177958_n();
/* 104 */     int cy = loc.func_177956_o();
/* 105 */     int cz = loc.func_177952_p();
/* 106 */     int x = cx - (int)r;
/* 107 */     while (x <= cx + r) {
/* 108 */       int z = cz - (int)r;
/* 109 */       while (z <= cz + r) {
/* 110 */         int y = sphere ? (cy - (int)r) : cy;
/*     */         
/*     */         while (true) {
/* 113 */           float f = sphere ? (cy + r) : (cy + h), f2 = f;
/* 114 */           if (y >= f)
/* 115 */             break;  double dist = ((cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0));
/* 116 */           if (dist < (r * r) && (!hollow || dist >= ((r - 1.0F) * (r - 1.0F)))) {
/* 117 */             BlockPos l = new BlockPos(x, y + plus_y, z);
/* 118 */             circleblocks.add(l);
/*     */           } 
/* 120 */           y++;
/*     */         } 
/* 122 */         z++;
/*     */       } 
/* 124 */       x++;
/*     */     } 
/* 126 */     return circleblocks;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketSend(PacketEvent.Send event) {
/* 131 */     if (event.getStage() == 0 && ((Boolean)this.rotate.getValue()).booleanValue() && this.rotating && event.getPacket() instanceof CPacketPlayer) {
/* 132 */       CPacketPlayer packet = (CPacketPlayer)event.getPacket();
/* 133 */       packet.field_149476_e = this.yaw;
/* 134 */       packet.field_149473_f = this.pitch;
/* 135 */       this.rotating = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void rotateTo(Entity entity) {
/* 140 */     if (((Boolean)this.rotate.getValue()).booleanValue()) {
/* 141 */       float[] angle = MathUtil.calcAngle(AutoCrystal.mc.field_71439_g.func_174824_e(mc.func_184121_ak()), entity.func_174791_d());
/* 142 */       this.yaw = angle[0];
/* 143 */       this.pitch = angle[1];
/* 144 */       this.rotating = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void rotateToPos(BlockPos pos) {
/* 149 */     if (((Boolean)this.rotate.getValue()).booleanValue()) {
/* 150 */       float[] angle = MathUtil.calcAngle(AutoCrystal.mc.field_71439_g.func_174824_e(mc.func_184121_ak()), new Vec3d((pos.func_177958_n() + 0.5F), (pos.func_177956_o() - 0.5F), (pos.func_177952_p() + 0.5F)));
/* 151 */       this.yaw = angle[0];
/* 152 */       this.pitch = angle[1];
/* 153 */       this.rotating = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/* 159 */     this.placeTimer.reset();
/* 160 */     this.breakTimer.reset();
/* 161 */     this.predictWait = 0;
/* 162 */     this.hotBarSlot = -1;
/* 163 */     this.pos = null;
/* 164 */     this.crystal = null;
/* 165 */     this.predict = 0;
/* 166 */     this.predictPackets = 1;
/* 167 */     this.target = null;
/* 168 */     this.packetCalc = false;
/* 169 */     this.realTarget = null;
/* 170 */     this.armor = false;
/* 171 */     this.armorTarget = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 176 */     this.rotating = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/* 181 */     onCrystal();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/* 186 */     if (this.realTarget != null) {
/* 187 */       return this.realTarget.func_70005_c_();
/*     */     }
/* 189 */     return null;
/*     */   }
/*     */   
/*     */   public void onCrystal() {
/* 193 */     if (AutoCrystal.mc.field_71441_e == null || AutoCrystal.mc.field_71439_g == null) {
/*     */       return;
/*     */     }
/* 196 */     this.realTarget = null;
/* 197 */     manualBreaker();
/* 198 */     this.crystalCount = 0;
/* 199 */     if (!((Boolean)this.ignoreUseAmount.getValue()).booleanValue()) {
/* 200 */       for (Entity crystal : AutoCrystal.mc.field_71441_e.field_72996_f) {
/* 201 */         if (!(crystal instanceof EntityEnderCrystal) || !IsValidCrystal(crystal))
/* 202 */           continue;  boolean count = false;
/* 203 */         double damage = calculateDamage(this.target.func_180425_c().func_177958_n() + 0.5D, this.target.func_180425_c().func_177956_o() + 1.0D, this.target.func_180425_c().func_177952_p() + 0.5D, (Entity)this.target);
/* 204 */         if (damage >= ((Float)this.minDamage.getValue()).floatValue()) {
/* 205 */           count = true;
/*     */         }
/* 207 */         if (!count)
/* 208 */           continue;  this.crystalCount++;
/*     */       } 
/*     */     }
/* 211 */     this.hotBarSlot = -1;
/* 212 */     if (AutoCrystal.mc.field_71439_g.func_184592_cb().func_77973_b() != Items.field_185158_cP) {
/*     */       
/* 214 */       int crystalSlot = (AutoCrystal.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP) ? AutoCrystal.mc.field_71439_g.field_71071_by.field_70461_c : -1, n = crystalSlot;
/* 215 */       if (crystalSlot == -1) {
/* 216 */         for (int l = 0; l < 9; ) {
/* 217 */           if (AutoCrystal.mc.field_71439_g.field_71071_by.func_70301_a(l).func_77973_b() != Items.field_185158_cP) { l++; continue; }
/* 218 */            crystalSlot = l;
/* 219 */           this.hotBarSlot = l;
/*     */         } 
/*     */       }
/*     */       
/* 223 */       if (crystalSlot == -1) {
/* 224 */         this.pos = null;
/* 225 */         this.target = null;
/*     */         return;
/*     */       } 
/*     */     } 
/* 229 */     if (AutoCrystal.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151153_ao && AutoCrystal.mc.field_71439_g.func_184614_ca().func_77973_b() != Items.field_185158_cP) {
/* 230 */       this.pos = null;
/* 231 */       this.target = null;
/*     */       return;
/*     */     } 
/* 234 */     if (this.target == null) {
/* 235 */       this.target = (EntityLivingBase)getTarget();
/*     */     }
/* 237 */     if (this.target == null) {
/* 238 */       this.crystal = null;
/*     */       return;
/*     */     } 
/* 241 */     if (this.target.func_70032_d((Entity)AutoCrystal.mc.field_71439_g) > 12.0F) {
/* 242 */       this.crystal = null;
/* 243 */       this.target = null;
/*     */     } 
/* 245 */     this.crystal = AutoCrystal.mc.field_71441_e.field_72996_f.stream().filter(this::IsValidCrystal).map(p_Entity -> (EntityEnderCrystal)p_Entity).min(Comparator.comparing(p_Entity -> Float.valueOf(this.target.func_70032_d((Entity)p_Entity)))).orElse(null);
/* 246 */     if (this.crystal != null && ((Boolean)this.explode.getValue()).booleanValue() && this.breakTimer.passedMs(((Float)this.breakDelay.getValue()).longValue())) {
/* 247 */       this.breakTimer.reset();
/* 248 */       if (((Boolean)this.packetBreak.getValue()).booleanValue()) {
/* 249 */         rotateTo((Entity)this.crystal);
/* 250 */         AutoCrystal.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketUseEntity((Entity)this.crystal));
/*     */       } else {
/* 252 */         rotateTo((Entity)this.crystal);
/* 253 */         AutoCrystal.mc.field_71442_b.func_78764_a((EntityPlayer)AutoCrystal.mc.field_71439_g, (Entity)this.crystal);
/*     */       } 
/* 255 */       if (this.swingMode.getValue() == SwingMode.MainHand) {
/* 256 */         AutoCrystal.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/* 257 */       } else if (this.swingMode.getValue() == SwingMode.OffHand) {
/* 258 */         AutoCrystal.mc.field_71439_g.func_184609_a(EnumHand.OFF_HAND);
/*     */       } 
/*     */     } 
/* 261 */     if (this.placeTimer.passedMs(((Float)this.placeDelay.getValue()).longValue()) && ((Boolean)this.place.getValue()).booleanValue()) {
/* 262 */       this.placeTimer.reset();
/* 263 */       double damage = 0.5D;
/* 264 */       for (BlockPos blockPos : placePostions(((Float)this.placeRange.getValue()).floatValue())) {
/*     */         double targetRange;
/*     */         
/* 267 */         if (blockPos == null || this.target == null || !AutoCrystal.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(blockPos)).isEmpty() || (targetRange = this.target.func_70011_f(blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p())) > ((Float)this.targetRange.getValue()).floatValue() || this.target.field_70128_L || this.target.func_110143_aJ() + this.target.func_110139_bj() <= 0.0F)
/*     */           continue; 
/* 269 */         double targetDmg = calculateDamage(blockPos.func_177958_n() + 0.5D, blockPos.func_177956_o() + 1.0D, blockPos.func_177952_p() + 0.5D, (Entity)this.target);
/* 270 */         this.armor = false;
/* 271 */         for (ItemStack is : this.target.func_184193_aE()) {
/* 272 */           float green = (is.func_77958_k() - is.func_77952_i()) / is.func_77958_k();
/* 273 */           float red = 1.0F - green;
/* 274 */           int dmg = 100 - (int)(red * 100.0F);
/* 275 */           if (dmg > ((Float)this.minArmor.getValue()).floatValue())
/* 276 */             continue;  this.armor = true;
/*     */         } 
/* 278 */         if (targetDmg < ((Float)this.minDamage.getValue()).floatValue() && (((Boolean)this.facePlaceSword.getValue()).booleanValue() ? (this.target.func_110139_bj() + this.target.func_110143_aJ() > ((Float)this.facePlace.getValue()).floatValue()) : (AutoCrystal.mc.field_71439_g.func_184614_ca().func_77973_b() instanceof net.minecraft.item.ItemSword || this.target.func_110139_bj() + this.target.func_110143_aJ() > ((Float)this.facePlace.getValue()).floatValue())) && (((Boolean)this.facePlaceSword.getValue()).booleanValue() ? !this.armor : (AutoCrystal.mc.field_71439_g.func_184614_ca().func_77973_b() instanceof net.minecraft.item.ItemSword || !this.armor))) continue;  double selfDmg; if (((selfDmg = calculateDamage(blockPos.func_177958_n() + 0.5D, blockPos.func_177956_o() + 1.0D, blockPos.func_177952_p() + 0.5D, (Entity)AutoCrystal.mc.field_71439_g)) + (((Boolean)this.suicide.getValue()).booleanValue() ? 2.0D : 0.5D) >= (AutoCrystal.mc.field_71439_g.func_110143_aJ() + AutoCrystal.mc.field_71439_g.func_110139_bj()) && selfDmg >= targetDmg && targetDmg < (this.target.func_110143_aJ() + this.target.func_110139_bj())) || damage >= targetDmg)
/*     */           continue; 
/* 280 */         this.pos = blockPos;
/* 281 */         damage = targetDmg;
/*     */       } 
/* 283 */       if (damage == 0.5D) {
/* 284 */         this.pos = null;
/* 285 */         this.target = null;
/* 286 */         this.realTarget = null;
/*     */         return;
/*     */       } 
/* 289 */       if (this.hotBarSlot != -1 && ((Boolean)this.autoswitch.getValue()).booleanValue() && !AutoCrystal.mc.field_71439_g.func_70644_a(MobEffects.field_76437_t)) {
/* 290 */         AutoCrystal.mc.field_71439_g.field_71071_by.field_70461_c = this.hotBarSlot;
/*     */       }
/* 292 */       if (!((Boolean)this.ignoreUseAmount.getValue()).booleanValue()) {
/* 293 */         int crystalLimit = ((Integer)this.wasteAmount.getValue()).intValue();
/* 294 */         if (this.crystalCount >= crystalLimit) {
/*     */           return;
/*     */         }
/* 297 */         if (damage < ((Float)this.minDamage.getValue()).floatValue()) {
/* 298 */           crystalLimit = 1;
/*     */         }
/* 300 */         if (this.crystalCount < crystalLimit && this.pos != null) {
/* 301 */           rotateToPos(this.pos);
/* 302 */           AutoCrystal.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(this.pos, EnumFacing.UP, (AutoCrystal.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
/*     */         } 
/* 304 */       } else if (this.pos != null) {
/* 305 */         rotateToPos(this.pos);
/* 306 */         AutoCrystal.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(this.pos, EnumFacing.UP, (AutoCrystal.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
/*     */   public void onPacketReceive(PacketEvent.Receive event) {
/*     */     SPacketSpawnObject packet;
/* 314 */     if (event.getPacket() instanceof SPacketSpawnObject && (packet = (SPacketSpawnObject)event.getPacket()).func_148993_l() == 51 && ((Boolean)this.predicts.getValue()).booleanValue() && this.preditTimer.passedMs(((Integer)this.attackFactor.getValue()).longValue()) && ((Boolean)this.predicts.getValue()).booleanValue() && ((Boolean)this.explode.getValue()).booleanValue() && ((Boolean)this.packetBreak.getValue()).booleanValue() && this.target != null) {
/* 315 */       if (!isPredicting(packet)) {
/*     */         return;
/*     */       }
/* 318 */       CPacketUseEntity predict = new CPacketUseEntity();
/* 319 */       predict.field_149567_a = packet.func_149001_c();
/* 320 */       predict.field_149566_b = CPacketUseEntity.Action.ATTACK;
/* 321 */       AutoCrystal.mc.field_71439_g.field_71174_a.func_147297_a((Packet)predict);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isPredicting(SPacketSpawnObject packet) {
/* 326 */     BlockPos packPos = new BlockPos(packet.func_186880_c(), packet.func_186882_d(), packet.func_186881_e());
/* 327 */     if (AutoCrystal.mc.field_71439_g.func_70011_f(packet.func_186880_c(), packet.func_186882_d(), packet.func_186881_e()) > ((Float)this.breakRange.getValue()).floatValue()) {
/* 328 */       return false;
/*     */     }
/* 330 */     if (!canSeePos(packPos) && AutoCrystal.mc.field_71439_g.func_70011_f(packet.func_186880_c(), packet.func_186882_d(), packet.func_186881_e()) > ((Float)this.breakWallRange.getValue()).floatValue()) {
/* 331 */       return false;
/*     */     }
/* 333 */     double targetDmg = calculateDamage(packet.func_186880_c() + 0.5D, packet.func_186882_d() + 1.0D, packet.func_186881_e() + 0.5D, (Entity)this.target);
/* 334 */     if (EntityUtil.isInHole((Entity)AutoCrystal.mc.field_71439_g) && targetDmg >= 1.0D) {
/* 335 */       return true;
/*     */     }
/* 337 */     double selfDmg = calculateDamage(packet.func_186880_c() + 0.5D, packet.func_186882_d() + 1.0D, packet.func_186881_e() + 0.5D, (Entity)AutoCrystal.mc.field_71439_g);
/* 338 */     double d = ((Boolean)this.suicide.getValue()).booleanValue() ? 2.0D : 0.5D;
/* 339 */     if (selfDmg + d < (AutoCrystal.mc.field_71439_g.func_110143_aJ() + AutoCrystal.mc.field_71439_g.func_110139_bj()) && targetDmg >= (this.target.func_110139_bj() + this.target.func_110143_aJ())) {
/* 340 */       return true;
/*     */     }
/* 342 */     this.armorTarget = false;
/* 343 */     for (ItemStack is : this.target.func_184193_aE()) {
/* 344 */       float green = (is.func_77958_k() - is.func_77952_i()) / is.func_77958_k();
/* 345 */       float red = 1.0F - green;
/* 346 */       int dmg = 100 - (int)(red * 100.0F);
/* 347 */       if (dmg > ((Float)this.minArmor.getValue()).floatValue())
/* 348 */         continue;  this.armorTarget = true;
/*     */     } 
/* 350 */     if (targetDmg >= ((Float)this.breakMinDmg.getValue()).floatValue() && selfDmg <= ((Float)this.breakMaxSelfDamage.getValue()).floatValue()) {
/* 351 */       return true;
/*     */     }
/* 353 */     return (EntityUtil.isInHole((Entity)this.target) && this.target.func_110143_aJ() + this.target.func_110139_bj() <= ((Float)this.facePlace.getValue()).floatValue());
/*     */   }
/*     */   
/*     */   private boolean IsValidCrystal(Entity p_Entity) {
/* 357 */     if (p_Entity == null) {
/* 358 */       return false;
/*     */     }
/* 360 */     if (!(p_Entity instanceof EntityEnderCrystal)) {
/* 361 */       return false;
/*     */     }
/* 363 */     if (this.target == null) {
/* 364 */       return false;
/*     */     }
/* 366 */     if (p_Entity.func_70032_d((Entity)AutoCrystal.mc.field_71439_g) > ((Float)this.breakRange.getValue()).floatValue()) {
/* 367 */       return false;
/*     */     }
/* 369 */     if (!AutoCrystal.mc.field_71439_g.func_70685_l(p_Entity) && p_Entity.func_70032_d((Entity)AutoCrystal.mc.field_71439_g) > ((Float)this.breakWallRange.getValue()).floatValue()) {
/* 370 */       return false;
/*     */     }
/* 372 */     if (this.target.field_70128_L || this.target.func_110143_aJ() + this.target.func_110139_bj() <= 0.0F) {
/* 373 */       return false;
/*     */     }
/* 375 */     double targetDmg = calculateDamage(p_Entity.func_180425_c().func_177958_n() + 0.5D, p_Entity.func_180425_c().func_177956_o() + 1.0D, p_Entity.func_180425_c().func_177952_p() + 0.5D, (Entity)this.target);
/* 376 */     if (EntityUtil.isInHole((Entity)AutoCrystal.mc.field_71439_g) && targetDmg >= 1.0D) {
/* 377 */       return true;
/*     */     }
/* 379 */     double selfDmg = calculateDamage(p_Entity.func_180425_c().func_177958_n() + 0.5D, p_Entity.func_180425_c().func_177956_o() + 1.0D, p_Entity.func_180425_c().func_177952_p() + 0.5D, (Entity)AutoCrystal.mc.field_71439_g);
/* 380 */     double d = ((Boolean)this.suicide.getValue()).booleanValue() ? 2.0D : 0.5D;
/* 381 */     if (selfDmg + d < (AutoCrystal.mc.field_71439_g.func_110143_aJ() + AutoCrystal.mc.field_71439_g.func_110139_bj()) && targetDmg >= (this.target.func_110139_bj() + this.target.func_110143_aJ())) {
/* 382 */       return true;
/*     */     }
/* 384 */     this.armorTarget = false;
/* 385 */     for (ItemStack is : this.target.func_184193_aE()) {
/* 386 */       float green = (is.func_77958_k() - is.func_77952_i()) / is.func_77958_k();
/* 387 */       float red = 1.0F - green;
/* 388 */       int dmg = 100 - (int)(red * 100.0F);
/* 389 */       if (dmg > ((Float)this.minArmor.getValue()).floatValue())
/* 390 */         continue;  this.armorTarget = true;
/*     */     } 
/* 392 */     if (targetDmg >= ((Float)this.breakMinDmg.getValue()).floatValue() && selfDmg <= ((Float)this.breakMaxSelfDamage.getValue()).floatValue()) {
/* 393 */       return true;
/*     */     }
/* 395 */     return (EntityUtil.isInHole((Entity)this.target) && this.target.func_110143_aJ() + this.target.func_110139_bj() <= ((Float)this.facePlace.getValue()).floatValue());
/*     */   }
/*     */   
/*     */   EntityPlayer getTarget() {
/* 399 */     EntityPlayer closestPlayer = null;
/* 400 */     for (EntityPlayer entity : AutoCrystal.mc.field_71441_e.field_73010_i) {
/* 401 */       if (AutoCrystal.mc.field_71439_g == null || AutoCrystal.mc.field_71439_g.field_70128_L || entity.field_70128_L || entity == AutoCrystal.mc.field_71439_g || Phobos.friendManager.isFriend(entity.func_70005_c_()) || entity.func_70032_d((Entity)AutoCrystal.mc.field_71439_g) > 12.0F)
/*     */         continue; 
/* 403 */       this.armorTarget = false;
/* 404 */       for (ItemStack is : entity.func_184193_aE()) {
/* 405 */         float green = (is.func_77958_k() - is.func_77952_i()) / is.func_77958_k();
/* 406 */         float red = 1.0F - green;
/* 407 */         int dmg = 100 - (int)(red * 100.0F);
/* 408 */         if (dmg > ((Float)this.minArmor.getValue()).floatValue())
/* 409 */           continue;  this.armorTarget = true;
/*     */       } 
/* 411 */       if (EntityUtil.isInHole((Entity)entity) && entity.func_110139_bj() + entity.func_110143_aJ() > ((Float)this.facePlace.getValue()).floatValue() && !this.armorTarget && ((Float)this.minDamage.getValue()).floatValue() > 2.2F)
/*     */         continue; 
/* 413 */       if (closestPlayer == null) {
/* 414 */         closestPlayer = entity;
/*     */         continue;
/*     */       } 
/* 417 */       if (closestPlayer.func_70032_d((Entity)AutoCrystal.mc.field_71439_g) <= entity.func_70032_d((Entity)AutoCrystal.mc.field_71439_g))
/*     */         continue; 
/* 419 */       closestPlayer = entity;
/*     */     } 
/* 421 */     return closestPlayer;
/*     */   }
/*     */   
/*     */   private void manualBreaker() {
/*     */     RayTraceResult result;
/* 426 */     if (this.manualTimer.passedMs(200L) && AutoCrystal.mc.field_71474_y.field_74313_G.func_151470_d() && AutoCrystal.mc.field_71439_g.func_184592_cb().func_77973_b() != Items.field_151153_ao && AutoCrystal.mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() != Items.field_151153_ao && AutoCrystal.mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() != Items.field_151031_f && AutoCrystal.mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() != Items.field_151062_by && (result = AutoCrystal.mc.field_71476_x) != null) {
/* 427 */       if (result.field_72313_a.equals(RayTraceResult.Type.ENTITY)) {
/* 428 */         Entity entity = result.field_72308_g;
/* 429 */         if (entity instanceof EntityEnderCrystal) {
/* 430 */           if (((Boolean)this.packetBreak.getValue()).booleanValue()) {
/* 431 */             AutoCrystal.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketUseEntity(entity));
/*     */           } else {
/* 433 */             AutoCrystal.mc.field_71442_b.func_78764_a((EntityPlayer)AutoCrystal.mc.field_71439_g, entity);
/*     */           } 
/* 435 */           this.manualTimer.reset();
/*     */         } 
/* 437 */       } else if (result.field_72313_a.equals(RayTraceResult.Type.BLOCK)) {
/* 438 */         BlockPos mousePos = new BlockPos(AutoCrystal.mc.field_71476_x.func_178782_a().func_177958_n(), AutoCrystal.mc.field_71476_x.func_178782_a().func_177956_o() + 1.0D, AutoCrystal.mc.field_71476_x.func_178782_a().func_177952_p());
/* 439 */         for (Entity target : AutoCrystal.mc.field_71441_e.func_72839_b(null, new AxisAlignedBB(mousePos))) {
/* 440 */           if (!(target instanceof EntityEnderCrystal))
/* 441 */             continue;  if (((Boolean)this.packetBreak.getValue()).booleanValue()) {
/* 442 */             AutoCrystal.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketUseEntity(target));
/*     */           } else {
/* 444 */             AutoCrystal.mc.field_71442_b.func_78764_a((EntityPlayer)AutoCrystal.mc.field_71439_g, target);
/*     */           } 
/* 446 */           this.manualTimer.reset();
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean canSeePos(BlockPos pos) {
/* 453 */     return (AutoCrystal.mc.field_71441_e.func_147447_a(new Vec3d(AutoCrystal.mc.field_71439_g.field_70165_t, AutoCrystal.mc.field_71439_g.field_70163_u + AutoCrystal.mc.field_71439_g.func_70047_e(), AutoCrystal.mc.field_71439_g.field_70161_v), new Vec3d(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p()), false, true, false) == null);
/*     */   }
/*     */   
/*     */   private NonNullList<BlockPos> placePostions(float placeRange) {
/* 457 */     NonNullList<BlockPos> positions = NonNullList.func_191196_a();
/* 458 */     positions.addAll((Collection)getSphere(new BlockPos(Math.floor(AutoCrystal.mc.field_71439_g.field_70165_t), Math.floor(AutoCrystal.mc.field_71439_g.field_70163_u), Math.floor(AutoCrystal.mc.field_71439_g.field_70161_v)), placeRange, (int)placeRange, false, true, 0).stream().filter(pos -> canPlaceCrystal(pos, true)).collect(Collectors.toList()));
/* 459 */     return positions;
/*     */   }
/*     */   
/*     */   private boolean canPlaceCrystal(BlockPos blockPos, boolean specialEntityCheck) {
/* 463 */     BlockPos boost = blockPos.func_177982_a(0, 1, 0);
/* 464 */     BlockPos boost2 = blockPos.func_177982_a(0, 2, 0);
/*     */     try {
/* 466 */       if (!((Boolean)this.opPlace.getValue()).booleanValue()) {
/* 467 */         if (AutoCrystal.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150357_h && AutoCrystal.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150343_Z) {
/* 468 */           return false;
/*     */         }
/* 470 */         if (AutoCrystal.mc.field_71441_e.func_180495_p(boost).func_177230_c() != Blocks.field_150350_a || AutoCrystal.mc.field_71441_e.func_180495_p(boost2).func_177230_c() != Blocks.field_150350_a) {
/* 471 */           return false;
/*     */         }
/* 473 */         if (!specialEntityCheck) {
/* 474 */           return (AutoCrystal.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost)).isEmpty() && AutoCrystal.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost2)).isEmpty());
/*     */         }
/* 476 */         for (Entity entity : AutoCrystal.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost))) {
/* 477 */           if (entity instanceof EntityEnderCrystal)
/* 478 */             continue;  return false;
/*     */         } 
/* 480 */         for (Entity entity : AutoCrystal.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost2))) {
/* 481 */           if (entity instanceof EntityEnderCrystal)
/* 482 */             continue;  return false;
/*     */         } 
/*     */       } else {
/* 485 */         if (AutoCrystal.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150357_h && AutoCrystal.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150343_Z) {
/* 486 */           return false;
/*     */         }
/* 488 */         if (AutoCrystal.mc.field_71441_e.func_180495_p(boost).func_177230_c() != Blocks.field_150350_a) {
/* 489 */           return false;
/*     */         }
/* 491 */         if (!specialEntityCheck) {
/* 492 */           return AutoCrystal.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost)).isEmpty();
/*     */         }
/* 494 */         for (Entity entity : AutoCrystal.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost))) {
/* 495 */           if (entity instanceof EntityEnderCrystal)
/* 496 */             continue;  return false;
/*     */         } 
/*     */       } 
/* 499 */     } catch (Exception ignored) {
/* 500 */       return false;
/*     */     } 
/* 502 */     return true;
/*     */   }
/*     */   
/*     */   private float calculateDamage(double posX, double posY, double posZ, Entity entity) {
/* 506 */     float doubleExplosionSize = 12.0F;
/* 507 */     double distancedsize = entity.func_70011_f(posX, posY, posZ) / 12.0D;
/* 508 */     Vec3d vec3d = new Vec3d(posX, posY, posZ);
/* 509 */     double blockDensity = 0.0D;
/*     */     try {
/* 511 */       blockDensity = entity.field_70170_p.func_72842_a(vec3d, entity.func_174813_aQ());
/* 512 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/* 515 */     double v = (1.0D - distancedsize) * blockDensity;
/* 516 */     float damage = (int)((v * v + v) / 2.0D * 7.0D * 12.0D + 1.0D);
/* 517 */     double finald = 1.0D;
/* 518 */     if (entity instanceof EntityLivingBase) {
/* 519 */       finald = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion((World)AutoCrystal.mc.field_71441_e, null, posX, posY, posZ, 6.0F, false, true));
/*     */     }
/* 521 */     return (float)finald;
/*     */   }
/*     */   
/*     */   private float getBlastReduction(EntityLivingBase entity, float damageI, Explosion explosion) {
/* 525 */     float damage = damageI;
/* 526 */     if (entity instanceof EntityPlayer) {
/* 527 */       EntityPlayer ep = (EntityPlayer)entity;
/* 528 */       DamageSource ds = DamageSource.func_94539_a(explosion);
/* 529 */       damage = CombatRules.func_189427_a(damage, ep.func_70658_aO(), (float)ep.func_110148_a(SharedMonsterAttributes.field_189429_h).func_111126_e());
/* 530 */       int k = 0;
/*     */       try {
/* 532 */         k = EnchantmentHelper.func_77508_a(ep.func_184193_aE(), ds);
/* 533 */       } catch (Exception exception) {}
/*     */ 
/*     */       
/* 536 */       float f = MathHelper.func_76131_a(k, 0.0F, 20.0F);
/* 537 */       damage *= 1.0F - f / 25.0F;
/* 538 */       if (entity.func_70644_a(MobEffects.field_76429_m)) {
/* 539 */         damage -= damage / 4.0F;
/*     */       }
/* 541 */       damage = Math.max(damage, 0.0F);
/* 542 */       return damage;
/*     */     } 
/* 544 */     damage = CombatRules.func_189427_a(damage, entity.func_70658_aO(), (float)entity.func_110148_a(SharedMonsterAttributes.field_189429_h).func_111126_e());
/* 545 */     return damage;
/*     */   }
/*     */   
/*     */   private float getDamageMultiplied(float damage) {
/* 549 */     int diff = AutoCrystal.mc.field_71441_e.func_175659_aa().func_151525_a();
/* 550 */     return damage * ((diff == 0) ? 0.0F : ((diff == 2) ? 1.0F : ((diff == 1) ? 0.5F : 1.5F)));
/*     */   }
/*     */   
/*     */   public enum SwingMode {
/* 554 */     MainHand,
/* 555 */     OffHand,
/* 556 */     None;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\combat\AutoCrystalCustom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */