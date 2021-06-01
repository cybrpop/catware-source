/*     */ package me.earth.phobos.features.modules.combat;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.DamageUtil;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.InventoryUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.ClickType;
/*     */ import net.minecraft.item.ItemSword;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketEntityAction;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class Killaura extends Module {
/*  22 */   private final Timer timer = new Timer(); public static Entity target;
/*  23 */   public Setting<Float> range = register(new Setting("Range", Float.valueOf(6.0F), Float.valueOf(0.1F), Float.valueOf(7.0F)));
/*  24 */   public Setting<Boolean> autoSwitch = register(new Setting("AutoSwitch", Boolean.valueOf(false)));
/*  25 */   public Setting<Boolean> delay = register(new Setting("Delay", Boolean.valueOf(true)));
/*  26 */   public Setting<Boolean> rotate = register(new Setting("Rotate", Boolean.valueOf(true)));
/*  27 */   public Setting<Boolean> stay = register(new Setting("Stay", Boolean.valueOf(true), v -> ((Boolean)this.rotate.getValue()).booleanValue()));
/*  28 */   public Setting<Boolean> armorBreak = register(new Setting("ArmorBreak", Boolean.valueOf(false)));
/*  29 */   public Setting<Boolean> eating = register(new Setting("Eating", Boolean.valueOf(true)));
/*  30 */   public Setting<Boolean> onlySharp = register(new Setting("Axe/Sword", Boolean.valueOf(true)));
/*  31 */   public Setting<Boolean> teleport = register(new Setting("Teleport", Boolean.valueOf(false)));
/*  32 */   public Setting<Float> raytrace = register(new Setting("Raytrace", Float.valueOf(6.0F), Float.valueOf(0.1F), Float.valueOf(7.0F), v -> !((Boolean)this.teleport.getValue()).booleanValue(), "Wall Range."));
/*  33 */   public Setting<Float> teleportRange = register(new Setting("TpRange", Float.valueOf(15.0F), Float.valueOf(0.1F), Float.valueOf(50.0F), v -> ((Boolean)this.teleport.getValue()).booleanValue(), "Teleport Range."));
/*  34 */   public Setting<Boolean> lagBack = register(new Setting("LagBack", Boolean.valueOf(true), v -> ((Boolean)this.teleport.getValue()).booleanValue()));
/*  35 */   public Setting<Boolean> teekaydelay = register(new Setting("32kDelay", Boolean.valueOf(false)));
/*  36 */   public Setting<Integer> time32k = register(new Setting("32kTime", Integer.valueOf(5), Integer.valueOf(1), Integer.valueOf(50)));
/*  37 */   public Setting<Integer> multi = register(new Setting("32kPackets", Integer.valueOf(2), v -> !((Boolean)this.teekaydelay.getValue()).booleanValue()));
/*  38 */   public Setting<Boolean> multi32k = register(new Setting("Multi32k", Boolean.valueOf(false)));
/*  39 */   public Setting<Boolean> players = register(new Setting("Players", Boolean.valueOf(true)));
/*  40 */   public Setting<Boolean> mobs = register(new Setting("Mobs", Boolean.valueOf(false)));
/*  41 */   public Setting<Boolean> animals = register(new Setting("Animals", Boolean.valueOf(false)));
/*  42 */   public Setting<Boolean> vehicles = register(new Setting("Entities", Boolean.valueOf(false)));
/*  43 */   public Setting<Boolean> projectiles = register(new Setting("Projectiles", Boolean.valueOf(false)));
/*  44 */   public Setting<Boolean> tps = register(new Setting("TpsSync", Boolean.valueOf(true)));
/*  45 */   public Setting<Boolean> packet = register(new Setting("Packet", Boolean.valueOf(false)));
/*  46 */   public Setting<Boolean> swing = register(new Setting("Swing", Boolean.valueOf(true)));
/*  47 */   public Setting<Boolean> sneak = register(new Setting("State", Boolean.valueOf(false)));
/*  48 */   public Setting<Boolean> info = register(new Setting("Info", Boolean.valueOf(true)));
/*  49 */   private final Setting<TargetMode> targetMode = register(new Setting("Target", TargetMode.CLOSEST));
/*  50 */   public Setting<Float> health = register(new Setting("Health", Float.valueOf(6.0F), Float.valueOf(0.1F), Float.valueOf(36.0F), v -> (this.targetMode.getValue() == TargetMode.SMART)));
/*     */   
/*     */   public Killaura() {
/*  53 */     super("Killaura", "Kills aura.", Module.Category.COMBAT, true, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/*  58 */     if (!((Boolean)this.rotate.getValue()).booleanValue()) {
/*  59 */       doKillaura();
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayerEvent(UpdateWalkingPlayerEvent event) {
/*  65 */     if (event.getStage() == 0 && ((Boolean)this.rotate.getValue()).booleanValue()) {
/*  66 */       if (((Boolean)this.stay.getValue()).booleanValue() && target != null) {
/*  67 */         Phobos.rotationManager.lookAtEntity(target);
/*     */       }
/*  69 */       doKillaura();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void doKillaura() {
/*  75 */     if (((Boolean)this.onlySharp.getValue()).booleanValue() && !EntityUtil.holdingWeapon((EntityPlayer)mc.field_71439_g)) {
/*  76 */       target = null;
/*     */       return;
/*     */     } 
/*  79 */     int i, wait = (!((Boolean)this.delay.getValue()).booleanValue() || (EntityUtil.holding32k((EntityPlayer)mc.field_71439_g) && !((Boolean)this.teekaydelay.getValue()).booleanValue())) ? 0 : (i = (int)(DamageUtil.getCooldownByWeapon((EntityPlayer)mc.field_71439_g) * (((Boolean)this.tps.getValue()).booleanValue() ? Phobos.serverManager.getTpsFactor() : 1.0F)));
/*  80 */     if (!this.timer.passedMs(wait) || (!((Boolean)this.eating.getValue()).booleanValue() && mc.field_71439_g.func_184587_cr() && (!mc.field_71439_g.func_184592_cb().func_77973_b().equals(Items.field_185159_cQ) || mc.field_71439_g.func_184600_cs() != EnumHand.OFF_HAND))) {
/*     */       return;
/*     */     }
/*  83 */     if (this.targetMode.getValue() != TargetMode.FOCUS || target == null || (mc.field_71439_g.func_70068_e(target) >= MathUtil.square(((Float)this.range.getValue()).floatValue()) && (!((Boolean)this.teleport.getValue()).booleanValue() || mc.field_71439_g.func_70068_e(target) >= MathUtil.square(((Float)this.teleportRange.getValue()).floatValue()))) || (!mc.field_71439_g.func_70685_l(target) && !EntityUtil.canEntityFeetBeSeen(target) && mc.field_71439_g.func_70068_e(target) >= MathUtil.square(((Float)this.raytrace.getValue()).floatValue()) && !((Boolean)this.teleport.getValue()).booleanValue())) {
/*  84 */       target = getTarget();
/*     */     }
/*  86 */     if (target == null)
/*     */       return; 
/*     */     int sword;
/*  89 */     if (((Boolean)this.autoSwitch.getValue()).booleanValue() && (sword = InventoryUtil.findHotbarBlock(ItemSword.class)) != -1) {
/*  90 */       InventoryUtil.switchToHotbarSlot(sword, false);
/*     */     }
/*  92 */     if (((Boolean)this.rotate.getValue()).booleanValue()) {
/*  93 */       Phobos.rotationManager.lookAtEntity(target);
/*     */     }
/*  95 */     if (((Boolean)this.teleport.getValue()).booleanValue()) {
/*  96 */       Phobos.positionManager.setPositionPacket(target.field_70165_t, EntityUtil.canEntityFeetBeSeen(target) ? target.field_70163_u : (target.field_70163_u + target.func_70047_e()), target.field_70161_v, true, true, !((Boolean)this.lagBack.getValue()).booleanValue());
/*     */     }
/*  98 */     if (EntityUtil.holding32k((EntityPlayer)mc.field_71439_g) && !((Boolean)this.teekaydelay.getValue()).booleanValue()) {
/*  99 */       if (((Boolean)this.multi32k.getValue()).booleanValue()) {
/* 100 */         for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/* 101 */           if (!EntityUtil.isValid((Entity)player, ((Float)this.range.getValue()).floatValue()))
/* 102 */             continue;  teekayAttack((Entity)player);
/*     */         } 
/*     */       } else {
/* 105 */         teekayAttack(target);
/*     */       } 
/* 107 */       this.timer.reset();
/*     */       return;
/*     */     } 
/* 110 */     if (((Boolean)this.armorBreak.getValue()).booleanValue()) {
/* 111 */       mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71069_bz.field_75152_c, 9, mc.field_71439_g.field_71071_by.field_70461_c, ClickType.SWAP, (EntityPlayer)mc.field_71439_g);
/* 112 */       EntityUtil.attackEntity(target, ((Boolean)this.packet.getValue()).booleanValue(), ((Boolean)this.swing.getValue()).booleanValue());
/* 113 */       mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71069_bz.field_75152_c, 9, mc.field_71439_g.field_71071_by.field_70461_c, ClickType.SWAP, (EntityPlayer)mc.field_71439_g);
/* 114 */       EntityUtil.attackEntity(target, ((Boolean)this.packet.getValue()).booleanValue(), ((Boolean)this.swing.getValue()).booleanValue());
/*     */     } else {
/* 116 */       boolean sneaking = mc.field_71439_g.func_70093_af();
/* 117 */       boolean sprint = mc.field_71439_g.func_70051_ag();
/* 118 */       if (((Boolean)this.sneak.getValue()).booleanValue()) {
/* 119 */         if (sneaking) {
/* 120 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
/*     */         }
/* 122 */         if (sprint) {
/* 123 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.STOP_SPRINTING));
/*     */         }
/*     */       } 
/* 126 */       EntityUtil.attackEntity(target, ((Boolean)this.packet.getValue()).booleanValue(), ((Boolean)this.swing.getValue()).booleanValue());
/* 127 */       if (((Boolean)this.sneak.getValue()).booleanValue()) {
/* 128 */         if (sprint) {
/* 129 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_SPRINTING));
/*     */         }
/* 131 */         if (sneaking) {
/* 132 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
/*     */         }
/*     */       } 
/*     */     } 
/* 136 */     this.timer.reset();
/*     */   }
/*     */   
/*     */   private void teekayAttack(Entity entity) {
/* 140 */     for (int i = 0; i < ((Integer)this.multi.getValue()).intValue(); i++) {
/* 141 */       startEntityAttackThread(entity, i * ((Integer)this.time32k.getValue()).intValue());
/*     */     }
/*     */   }
/*     */   
/*     */   private void startEntityAttackThread(Entity entity, int time) {
/* 146 */     (new Thread(() -> {
/*     */           Timer timer = new Timer();
/*     */           timer.reset();
/*     */           try {
/*     */             Thread.sleep(time);
/* 151 */           } catch (InterruptedException ex) {
/*     */             Thread.currentThread().interrupt();
/*     */           } 
/*     */           EntityUtil.attackEntity(entity, true, ((Boolean)this.swing.getValue()).booleanValue());
/* 155 */         })).start();
/*     */   }
/*     */   
/*     */   private Entity getTarget() {
/* 159 */     Entity target = null;
/* 160 */     double distance = ((Boolean)this.teleport.getValue()).booleanValue() ? ((Float)this.teleportRange.getValue()).floatValue() : ((Float)this.range.getValue()).floatValue();
/* 161 */     double maxHealth = 36.0D;
/* 162 */     for (Entity entity : mc.field_71441_e.field_72996_f) {
/* 163 */       if (((!((Boolean)this.players.getValue()).booleanValue() || !(entity instanceof EntityPlayer)) && (!((Boolean)this.animals.getValue()).booleanValue() || !EntityUtil.isPassive(entity)) && (!((Boolean)this.mobs.getValue()).booleanValue() || !EntityUtil.isMobAggressive(entity)) && (!((Boolean)this.vehicles.getValue()).booleanValue() || !EntityUtil.isVehicle(entity)) && (!((Boolean)this.projectiles.getValue()).booleanValue() || !EntityUtil.isProjectile(entity))) || (entity instanceof net.minecraft.entity.EntityLivingBase && EntityUtil.isntValid(entity, distance)) || (!((Boolean)this.teleport.getValue()).booleanValue() && !mc.field_71439_g.func_70685_l(entity) && !EntityUtil.canEntityFeetBeSeen(entity) && mc.field_71439_g.func_70068_e(entity) > MathUtil.square(((Float)this.raytrace.getValue()).floatValue())))
/*     */         continue; 
/* 165 */       if (target == null) {
/* 166 */         target = entity;
/* 167 */         distance = mc.field_71439_g.func_70068_e(entity);
/* 168 */         maxHealth = EntityUtil.getHealth(entity);
/*     */         continue;
/*     */       } 
/* 171 */       if (entity instanceof EntityPlayer && DamageUtil.isArmorLow((EntityPlayer)entity, 18)) {
/* 172 */         target = entity;
/*     */         break;
/*     */       } 
/* 175 */       if (this.targetMode.getValue() == TargetMode.SMART && EntityUtil.getHealth(entity) < ((Float)this.health.getValue()).floatValue()) {
/* 176 */         target = entity;
/*     */         break;
/*     */       } 
/* 179 */       if (this.targetMode.getValue() != TargetMode.HEALTH && mc.field_71439_g.func_70068_e(entity) < distance) {
/* 180 */         target = entity;
/* 181 */         distance = mc.field_71439_g.func_70068_e(entity);
/* 182 */         maxHealth = EntityUtil.getHealth(entity);
/*     */       } 
/* 184 */       if (this.targetMode.getValue() != TargetMode.HEALTH || EntityUtil.getHealth(entity) >= maxHealth)
/*     */         continue; 
/* 186 */       target = entity;
/* 187 */       distance = mc.field_71439_g.func_70068_e(entity);
/* 188 */       maxHealth = EntityUtil.getHealth(entity);
/*     */     } 
/* 190 */     return target;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/* 195 */     if (((Boolean)this.info.getValue()).booleanValue() && target instanceof EntityPlayer) {
/* 196 */       return target.func_70005_c_();
/*     */     }
/* 198 */     return null;
/*     */   }
/*     */   
/*     */   public enum TargetMode {
/* 202 */     FOCUS,
/* 203 */     CLOSEST,
/* 204 */     HEALTH,
/* 205 */     SMART;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\combat\Killaura.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */