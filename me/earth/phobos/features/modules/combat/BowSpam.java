/*     */ package me.earth.phobos.features.modules.combat;
/*     */ 
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.InventoryUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemBow;
/*     */ import net.minecraft.item.ItemEndCrystal;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.network.play.client.CPacketPlayerDigging;
/*     */ import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ public class BowSpam
/*     */   extends Module {
/*  29 */   private final Timer timer = new Timer();
/*  30 */   public Setting<Mode> mode = register(new Setting("Mode", Mode.FAST));
/*  31 */   public Setting<Boolean> bowbomb = register(new Setting("BowBomb", Boolean.valueOf(false), v -> (this.mode.getValue() != Mode.BOWBOMB)));
/*  32 */   public Setting<Boolean> allowOffhand = register(new Setting("Offhand", Boolean.valueOf(true), v -> (this.mode.getValue() != Mode.AUTORELEASE)));
/*  33 */   public Setting<Integer> ticks = register(new Setting("Ticks", Integer.valueOf(3), Integer.valueOf(0), Integer.valueOf(20), v -> (this.mode.getValue() == Mode.BOWBOMB || this.mode.getValue() == Mode.FAST), "Speed"));
/*  34 */   public Setting<Integer> delay = register(new Setting("Delay", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(500), v -> (this.mode.getValue() == Mode.AUTORELEASE), "Speed"));
/*  35 */   public Setting<Boolean> tpsSync = register(new Setting("TpsSync", Boolean.valueOf(true)));
/*  36 */   public Setting<Boolean> autoSwitch = register(new Setting("AutoSwitch", Boolean.valueOf(false)));
/*  37 */   public Setting<Boolean> onlyWhenSave = register(new Setting("OnlyWhenSave", Boolean.valueOf(true), v -> ((Boolean)this.autoSwitch.getValue()).booleanValue()));
/*  38 */   public Setting<Target> targetMode = register(new Setting("Target", Target.LOWEST, v -> ((Boolean)this.autoSwitch.getValue()).booleanValue()));
/*  39 */   public Setting<Float> range = register(new Setting("Range", Float.valueOf(3.0F), Float.valueOf(0.0F), Float.valueOf(6.0F), v -> ((Boolean)this.autoSwitch.getValue()).booleanValue(), "Range of the target"));
/*  40 */   public Setting<Float> health = register(new Setting("Lethal", Float.valueOf(6.0F), Float.valueOf(0.1F), Float.valueOf(36.0F), v -> ((Boolean)this.autoSwitch.getValue()).booleanValue(), "When should it switch?"));
/*  41 */   public Setting<Float> ownHealth = register(new Setting("OwnHealth", Float.valueOf(20.0F), Float.valueOf(0.1F), Float.valueOf(36.0F), v -> ((Boolean)this.autoSwitch.getValue()).booleanValue(), "Own Health."));
/*     */   private boolean offhand = false;
/*     */   private boolean switched = false;
/*  44 */   private int lastHotbarSlot = -1;
/*     */   
/*     */   public BowSpam() {
/*  47 */     super("BowSpam", "Spams your bow", Module.Category.COMBAT, true, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  52 */     this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/*  57 */     if (event.getStage() != 0) {
/*     */       return;
/*     */     }
/*  60 */     if (((Boolean)this.autoSwitch.getValue()).booleanValue() && InventoryUtil.findHotbarBlock(ItemBow.class) != -1 && ((Float)this.ownHealth.getValue()).floatValue() <= EntityUtil.getHealth((Entity)mc.field_71439_g) && (!((Boolean)this.onlyWhenSave.getValue()).booleanValue() || EntityUtil.isSafe((Entity)mc.field_71439_g))) {
/*     */       
/*  62 */       EntityPlayer target = getTarget(); AutoCrystal crystal;
/*  63 */       if (target != null && (!(crystal = (AutoCrystal)Phobos.moduleManager.getModuleByClass(AutoCrystal.class)).isOn() || !InventoryUtil.holdingItem(ItemEndCrystal.class))) {
/*  64 */         Vec3d pos = target.func_174791_d();
/*  65 */         double xPos = pos.field_72450_a;
/*  66 */         double yPos = pos.field_72448_b;
/*  67 */         double zPos = pos.field_72449_c;
/*  68 */         if (mc.field_71439_g.func_70685_l((Entity)target)) {
/*  69 */           yPos += target.eyeHeight;
/*  70 */         } else if (EntityUtil.canEntityFeetBeSeen((Entity)target)) {
/*  71 */           yPos += 0.1D;
/*     */         } else {
/*     */           return;
/*     */         } 
/*  75 */         if (!(mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemBow)) {
/*  76 */           this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*  77 */           InventoryUtil.switchToHotbarSlot(ItemBow.class, false);
/*  78 */           mc.field_71474_y.field_74313_G.field_74513_e = true;
/*  79 */           this.switched = true;
/*     */         } 
/*  81 */         Phobos.rotationManager.lookAtVec3d(xPos, yPos, zPos);
/*  82 */         if (mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemBow) {
/*  83 */           this.switched = true;
/*     */         }
/*     */       } 
/*  86 */     } else if (event.getStage() == 0 && this.switched && this.lastHotbarSlot != -1) {
/*  87 */       InventoryUtil.switchToHotbarSlot(this.lastHotbarSlot, false);
/*  88 */       mc.field_71474_y.field_74313_G.field_74513_e = Mouse.isButtonDown(1);
/*  89 */       this.switched = false;
/*     */     } else {
/*  91 */       mc.field_71474_y.field_74313_G.field_74513_e = Mouse.isButtonDown(1);
/*     */     } 
/*  93 */     if (this.mode.getValue() == Mode.FAST && (this.offhand || mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() instanceof ItemBow) && mc.field_71439_g.func_184587_cr()) {
/*  94 */       float f = mc.field_71439_g.func_184612_cw();
/*  95 */       float f2 = ((Integer)this.ticks.getValue()).intValue();
/*  96 */       float f3 = ((Boolean)this.tpsSync.getValue()).booleanValue() ? Phobos.serverManager.getTpsFactor() : 1.0F;
/*  97 */       if (f >= f2 * f3) {
/*  98 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, mc.field_71439_g.func_174811_aO()));
/*  99 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItem(this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND));
/* 100 */         mc.field_71439_g.func_184597_cx();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onUpdate() {
/*     */     float f, f2, f3;
/* 107 */     this.offhand = (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151031_f && ((Boolean)this.allowOffhand.getValue()).booleanValue());
/* 108 */     switch ((Mode)this.mode.getValue()) {
/*     */       case AUTORELEASE:
/* 110 */         if ((!this.offhand && !(mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() instanceof ItemBow)) || !this.timer.passedMs((int)(((Integer)this.delay.getValue()).intValue() * (((Boolean)this.tpsSync.getValue()).booleanValue() ? Phobos.serverManager.getTpsFactor() : 1.0F))))
/*     */           break; 
/* 112 */         mc.field_71442_b.func_78766_c((EntityPlayer)mc.field_71439_g);
/* 113 */         this.timer.reset();
/*     */         break;
/*     */       
/*     */       case BOWBOMB:
/* 117 */         if ((!this.offhand && !(mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() instanceof ItemBow)) || !mc.field_71439_g.func_184587_cr())
/*     */           break; 
/* 119 */         f = mc.field_71439_g.func_184612_cw();
/* 120 */         f2 = ((Integer)this.ticks.getValue()).intValue();
/* 121 */         f3 = ((Boolean)this.tpsSync.getValue()).booleanValue() ? Phobos.serverManager.getTpsFactor() : 1.0F;
/* 122 */         if (f < f2 * f3)
/* 123 */           break;  mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, mc.field_71439_g.func_174811_aO()));
/* 124 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 0.0624D, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A, false));
/* 125 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 999.0D, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A, true));
/* 126 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItem(this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND));
/* 127 */         mc.field_71439_g.func_184597_cx();
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketSend(PacketEvent.Send event) {
/*     */     CPacketPlayerDigging packet;
/* 135 */     if (event.getStage() == 0 && ((Boolean)this.bowbomb.getValue()).booleanValue() && this.mode.getValue() != Mode.BOWBOMB && event.getPacket() instanceof CPacketPlayerDigging && (packet = (CPacketPlayerDigging)event.getPacket()).func_180762_c() == CPacketPlayerDigging.Action.RELEASE_USE_ITEM && (this.offhand || mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() instanceof ItemBow) && mc.field_71439_g.func_184612_cw() >= 20 && !mc.field_71439_g.field_70122_E) {
/* 136 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 0.10000000149011612D, mc.field_71439_g.field_70161_v, false));
/* 137 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 10000.0D, mc.field_71439_g.field_70161_v, true));
/*     */     } 
/*     */   }
/*     */   
/*     */   private EntityPlayer getTarget() {
/* 142 */     double maxHealth = 36.0D;
/* 143 */     EntityPlayer target = null;
/* 144 */     for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/* 145 */       if (player == null || EntityUtil.isDead((Entity)player) || EntityUtil.getHealth((Entity)player) > ((Float)this.health.getValue()).floatValue() || player.equals(mc.field_71439_g) || Phobos.friendManager.isFriend(player) || mc.field_71439_g.func_70068_e((Entity)player) > MathUtil.square(((Float)this.range.getValue()).floatValue()) || (!mc.field_71439_g.func_70685_l((Entity)player) && !EntityUtil.canEntityFeetBeSeen((Entity)player)))
/*     */         continue; 
/* 147 */       if (target == null) {
/* 148 */         target = player;
/* 149 */         maxHealth = EntityUtil.getHealth((Entity)player);
/*     */       } 
/* 151 */       if (this.targetMode.getValue() == Target.CLOSEST && mc.field_71439_g.func_70068_e((Entity)player) < mc.field_71439_g.func_70068_e((Entity)target)) {
/* 152 */         target = player;
/* 153 */         maxHealth = EntityUtil.getHealth((Entity)player);
/*     */       } 
/* 155 */       if (this.targetMode.getValue() != Target.LOWEST || EntityUtil.getHealth((Entity)player) >= maxHealth)
/*     */         continue; 
/* 157 */       target = player;
/* 158 */       maxHealth = EntityUtil.getHealth((Entity)player);
/*     */     } 
/* 160 */     return target;
/*     */   }
/*     */   
/*     */   public enum Mode {
/* 164 */     FAST,
/* 165 */     AUTORELEASE,
/* 166 */     BOWBOMB;
/*     */   }
/*     */   
/*     */   public enum Target
/*     */   {
/* 171 */     CLOSEST,
/* 172 */     LOWEST;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\combat\BowSpam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */