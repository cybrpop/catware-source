/*     */ package me.earth.phobos.features.modules.combat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.BlockUtil;
/*     */ import me.earth.phobos.util.DamageUtil;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class AntiCrystal extends Module {
/*  22 */   private final List<BlockPos> targets = new ArrayList<>();
/*  23 */   private final Timer timer = new Timer();
/*  24 */   private final Timer breakTimer = new Timer();
/*  25 */   private final Timer checkTimer = new Timer();
/*  26 */   public Setting<Float> range = register(new Setting("Range", Float.valueOf(6.0F), Float.valueOf(0.0F), Float.valueOf(10.0F)));
/*  27 */   public Setting<Float> wallsRange = register(new Setting("WallsRange", Float.valueOf(3.5F), Float.valueOf(0.0F), Float.valueOf(10.0F)));
/*  28 */   public Setting<Float> minDmg = register(new Setting("MinDmg", Float.valueOf(6.0F), Float.valueOf(0.0F), Float.valueOf(100.0F)));
/*  29 */   public Setting<Float> selfDmg = register(new Setting("SelfDmg", Float.valueOf(2.0F), Float.valueOf(0.0F), Float.valueOf(10.0F)));
/*  30 */   public Setting<Integer> placeDelay = register(new Setting("PlaceDelay", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(500)));
/*  31 */   public Setting<Integer> breakDelay = register(new Setting("BreakDelay", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(500)));
/*  32 */   public Setting<Integer> checkDelay = register(new Setting("CheckDelay", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(500)));
/*  33 */   public Setting<Integer> wasteAmount = register(new Setting("WasteAmount", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(5)));
/*  34 */   public Setting<Boolean> switcher = register(new Setting("Switch", Boolean.valueOf(true)));
/*  35 */   public Setting<Boolean> rotate = register(new Setting("Rotate", Boolean.valueOf(true)));
/*  36 */   public Setting<Boolean> packet = register(new Setting("Packet", Boolean.valueOf(true)));
/*  37 */   public Setting<Integer> rotations = register(new Setting("Spoofs", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(20)));
/*  38 */   private float yaw = 0.0F;
/*  39 */   private float pitch = 0.0F;
/*     */   private boolean rotating = false;
/*  41 */   private int rotationPacketsSpoofed = 0;
/*     */   private Entity breakTarget;
/*     */   
/*     */   public AntiCrystal() {
/*  45 */     super("AntiCrystal", "Hacker shit", Module.Category.COMBAT, true, true, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onToggle() {
/*  50 */     this.rotating = false;
/*     */   }
/*     */   
/*     */   private Entity getDeadlyCrystal() {
/*  54 */     Entity bestcrystal = null;
/*  55 */     float highestDamage = 0.0F;
/*  56 */     for (Entity crystal : mc.field_71441_e.field_72996_f) {
/*     */       float damage;
/*  58 */       if (!(crystal instanceof net.minecraft.entity.item.EntityEnderCrystal) || mc.field_71439_g.func_70068_e(crystal) > 169.0D || (damage = DamageUtil.calculateDamage(crystal, (Entity)mc.field_71439_g)) < ((Float)this.minDmg.getValue()).floatValue())
/*     */         continue; 
/*  60 */       if (bestcrystal == null) {
/*  61 */         bestcrystal = crystal;
/*  62 */         highestDamage = damage;
/*     */         continue;
/*     */       } 
/*  65 */       if (damage <= highestDamage)
/*  66 */         continue;  bestcrystal = crystal;
/*  67 */       highestDamage = damage;
/*     */     } 
/*  69 */     return bestcrystal;
/*     */   }
/*     */   
/*     */   private int getSafetyCrystals(Entity deadlyCrystal) {
/*  73 */     int count = 0;
/*  74 */     for (Entity entity : mc.field_71441_e.field_72996_f) {
/*     */       float damage;
/*  76 */       if (entity instanceof net.minecraft.entity.item.EntityEnderCrystal || (damage = DamageUtil.calculateDamage(entity, (Entity)mc.field_71439_g)) > 2.0F || deadlyCrystal.func_70068_e(entity) > 144.0D)
/*     */         continue; 
/*  78 */       count++;
/*     */     } 
/*  80 */     return count;
/*     */   }
/*     */   
/*     */   private BlockPos getPlaceTarget(Entity deadlyCrystal) {
/*  84 */     BlockPos closestPos = null;
/*  85 */     float smallestDamage = 10.0F;
/*  86 */     for (BlockPos pos : BlockUtil.possiblePlacePositions(((Float)this.range.getValue()).floatValue())) {
/*  87 */       float damage = DamageUtil.calculateDamage(pos, (Entity)mc.field_71439_g);
/*  88 */       if (damage > 2.0F || deadlyCrystal.func_174818_b(pos) > 144.0D || (mc.field_71439_g.func_174818_b(pos) >= MathUtil.square(((Float)this.wallsRange.getValue()).floatValue()) && BlockUtil.rayTracePlaceCheck(pos, true, 1.0F)))
/*     */         continue; 
/*  90 */       if (closestPos == null) {
/*  91 */         smallestDamage = damage;
/*  92 */         closestPos = pos;
/*     */         continue;
/*     */       } 
/*  95 */       if (damage >= smallestDamage && (damage != smallestDamage || mc.field_71439_g.func_174818_b(pos) >= mc.field_71439_g.func_174818_b(closestPos)))
/*     */         continue; 
/*  97 */       smallestDamage = damage;
/*  98 */       closestPos = pos;
/*     */     } 
/* 100 */     return closestPos;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketSend(PacketEvent.Send event) {
/* 105 */     if (event.getStage() == 0 && ((Boolean)this.rotate.getValue()).booleanValue() && this.rotating) {
/* 106 */       if (event.getPacket() instanceof CPacketPlayer) {
/* 107 */         CPacketPlayer packet = (CPacketPlayer)event.getPacket();
/* 108 */         packet.field_149476_e = this.yaw;
/* 109 */         packet.field_149473_f = this.pitch;
/*     */       } 
/* 111 */       this.rotationPacketsSpoofed++;
/* 112 */       if (this.rotationPacketsSpoofed >= ((Integer)this.rotations.getValue()).intValue()) {
/* 113 */         this.rotating = false;
/* 114 */         this.rotationPacketsSpoofed = 0;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/* 121 */     if (!fullNullCheck() && this.checkTimer.passedMs(((Integer)this.checkDelay.getValue()).intValue())) {
/* 122 */       Entity deadlyCrystal = getDeadlyCrystal();
/* 123 */       if (deadlyCrystal != null) {
/* 124 */         BlockPos placeTarget = getPlaceTarget(deadlyCrystal);
/* 125 */         if (placeTarget != null) {
/* 126 */           this.targets.add(placeTarget);
/*     */         }
/* 128 */         placeCrystal(deadlyCrystal);
/* 129 */         this.breakTarget = getBreakTarget(deadlyCrystal);
/* 130 */         breakCrystal();
/*     */       } 
/* 132 */       this.checkTimer.reset();
/*     */     } 
/*     */   }
/*     */   
/*     */   public Entity getBreakTarget(Entity deadlyCrystal) {
/* 137 */     Entity smallestCrystal = null;
/* 138 */     float smallestDamage = 10.0F;
/* 139 */     for (Entity entity : mc.field_71441_e.field_72996_f) {
/*     */       float damage;
/* 141 */       if (!(entity instanceof net.minecraft.entity.item.EntityEnderCrystal) || (damage = DamageUtil.calculateDamage(entity, (Entity)mc.field_71439_g)) > ((Float)this.selfDmg.getValue()).floatValue() || entity.func_70068_e(deadlyCrystal) > 144.0D || (mc.field_71439_g.func_70068_e(entity) > MathUtil.square(((Float)this.wallsRange.getValue()).floatValue()) && EntityUtil.rayTraceHitCheck(entity, true)))
/*     */         continue; 
/* 143 */       if (smallestCrystal == null) {
/* 144 */         smallestCrystal = entity;
/* 145 */         smallestDamage = damage;
/*     */         continue;
/*     */       } 
/* 148 */       if (damage >= smallestDamage && (smallestDamage != damage || mc.field_71439_g.func_70068_e(entity) >= mc.field_71439_g.func_70068_e(smallestCrystal)))
/*     */         continue; 
/* 150 */       smallestCrystal = entity;
/* 151 */       smallestDamage = damage;
/*     */     } 
/* 153 */     return smallestCrystal;
/*     */   }
/*     */ 
/*     */   
/*     */   private void placeCrystal(Entity deadlyCrystal) {
/* 158 */     boolean offhand = (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP), bl = offhand;
/* 159 */     if (this.timer.passedMs(((Integer)this.placeDelay.getValue()).intValue()) && (((Boolean)this.switcher.getValue()).booleanValue() || mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP || offhand) && !this.targets.isEmpty() && getSafetyCrystals(deadlyCrystal) <= ((Integer)this.wasteAmount.getValue()).intValue()) {
/* 160 */       if (((Boolean)this.switcher.getValue()).booleanValue() && mc.field_71439_g.func_184614_ca().func_77973_b() != Items.field_185158_cP && !offhand) {
/* 161 */         doSwitch();
/*     */       }
/* 163 */       rotateToPos(this.targets.get(this.targets.size() - 1));
/* 164 */       BlockUtil.placeCrystalOnBlock(this.targets.get(this.targets.size() - 1), offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, true, true);
/* 165 */       this.timer.reset();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void doSwitch() {
/* 171 */     int crystalSlot = (mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP) ? mc.field_71439_g.field_71071_by.field_70461_c : -1, n = crystalSlot;
/* 172 */     if (crystalSlot == -1) {
/* 173 */       for (int l = 0; l < 9; ) {
/* 174 */         if (mc.field_71439_g.field_71071_by.func_70301_a(l).func_77973_b() != Items.field_185158_cP) { l++; continue; }
/* 175 */          crystalSlot = l;
/*     */       } 
/*     */     }
/*     */     
/* 179 */     if (crystalSlot != -1) {
/* 180 */       mc.field_71439_g.field_71071_by.field_70461_c = crystalSlot;
/*     */     }
/*     */   }
/*     */   
/*     */   private void breakCrystal() {
/* 185 */     if (this.breakTimer.passedMs(((Integer)this.breakDelay.getValue()).intValue()) && this.breakTarget != null && DamageUtil.canBreakWeakness((EntityPlayer)mc.field_71439_g)) {
/* 186 */       rotateTo(this.breakTarget);
/* 187 */       EntityUtil.attackEntity(this.breakTarget, ((Boolean)this.packet.getValue()).booleanValue(), true);
/* 188 */       this.breakTimer.reset();
/* 189 */       this.targets.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void rotateTo(Entity entity) {
/* 194 */     if (((Boolean)this.rotate.getValue()).booleanValue()) {
/* 195 */       float[] angle = MathUtil.calcAngle(mc.field_71439_g.func_174824_e(mc.func_184121_ak()), entity.func_174791_d());
/* 196 */       this.yaw = angle[0];
/* 197 */       this.pitch = angle[1];
/* 198 */       this.rotating = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void rotateToPos(BlockPos pos) {
/* 203 */     if (((Boolean)this.rotate.getValue()).booleanValue()) {
/* 204 */       float[] angle = MathUtil.calcAngle(mc.field_71439_g.func_174824_e(mc.func_184121_ak()), new Vec3d((pos.func_177958_n() + 0.5F), (pos.func_177956_o() - 0.5F), (pos.func_177952_p() + 0.5F)));
/* 205 */       this.yaw = angle[0];
/* 206 */       this.pitch = angle[1];
/* 207 */       this.rotating = true;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\combat\AntiCrystal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */