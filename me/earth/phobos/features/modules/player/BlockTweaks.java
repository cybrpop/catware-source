/*     */ package me.earth.phobos.features.modules.player;
/*     */ 
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.EnumCreatureAttribute;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Enchantments;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.ItemSword;
/*     */ import net.minecraft.item.ItemTool;
/*     */ import net.minecraft.network.play.client.CPacketUseEntity;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.event.entity.player.AttackEntityEvent;
/*     */ import net.minecraftforge.event.entity.player.PlayerInteractEvent;
/*     */ import net.minecraftforge.event.world.BlockEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ 
/*     */ public class BlockTweaks
/*     */   extends Module
/*     */ {
/*  33 */   private static BlockTweaks INSTANCE = new BlockTweaks();
/*  34 */   public Setting<Boolean> autoTool = register(new Setting("AutoTool", Boolean.valueOf(false)));
/*  35 */   public Setting<Boolean> autoWeapon = register(new Setting("AutoWeapon", Boolean.valueOf(false)));
/*  36 */   public Setting<Boolean> noFriendAttack = register(new Setting("NoFriendAttack", Boolean.valueOf(false)));
/*  37 */   public Setting<Boolean> noBlock = register(new Setting("NoHitboxBlock", Boolean.valueOf(true)));
/*  38 */   public Setting<Boolean> noGhost = register(new Setting("NoGlitchBlocks", Boolean.valueOf(false)));
/*  39 */   public Setting<Boolean> destroy = register(new Setting("Destroy", Boolean.valueOf(false), v -> ((Boolean)this.noGhost.getValue()).booleanValue()));
/*  40 */   private int lastHotbarSlot = -1;
/*  41 */   private int currentTargetSlot = -1;
/*     */   private boolean switched = false;
/*     */   
/*     */   public BlockTweaks() {
/*  45 */     super("BlockTweaks", "Some tweaks for blocks.", Module.Category.PLAYER, true, false, false);
/*  46 */     setInstance();
/*     */   }
/*     */   
/*     */   public static BlockTweaks getINSTANCE() {
/*  50 */     if (INSTANCE == null) {
/*  51 */       INSTANCE = new BlockTweaks();
/*     */     }
/*  53 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  57 */     INSTANCE = this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  62 */     if (this.switched) {
/*  63 */       equip(this.lastHotbarSlot, false);
/*     */     }
/*  65 */     this.lastHotbarSlot = -1;
/*  66 */     this.currentTargetSlot = -1;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onBreak(BlockEvent.BreakEvent event) {
/*  71 */     if (fullNullCheck() || !((Boolean)this.noGhost.getValue()).booleanValue() || !((Boolean)this.destroy.getValue()).booleanValue()) {
/*     */       return;
/*     */     }
/*  74 */     if (!(mc.field_71439_g.func_184614_ca().func_77973_b() instanceof net.minecraft.item.ItemBlock)) {
/*  75 */       BlockPos pos = mc.field_71439_g.func_180425_c();
/*  76 */       removeGlitchBlocks(pos);
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onBlockInteract(PlayerInteractEvent.LeftClickBlock event) {
/*  82 */     if (((Boolean)this.autoTool.getValue()).booleanValue() && ((Speedmine.getInstance()).mode.getValue() != Speedmine.Mode.PACKET || Speedmine.getInstance().isOff() || !((Boolean)(Speedmine.getInstance()).tweaks.getValue()).booleanValue()) && !fullNullCheck() && event.getPos() != null) {
/*  83 */       equipBestTool(mc.field_71441_e.func_180495_p(event.getPos()));
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onAttack(AttackEntityEvent event) {
/*  89 */     if (((Boolean)this.autoWeapon.getValue()).booleanValue() && !fullNullCheck() && event.getTarget() != null) {
/*  90 */       equipBestWeapon(event.getTarget());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketSend(PacketEvent.Send event) {
/*  98 */     if (fullNullCheck())
/*     */       return;  CPacketUseEntity packet;
/*     */     Entity entity;
/* 101 */     if (((Boolean)this.noFriendAttack.getValue()).booleanValue() && event.getPacket() instanceof CPacketUseEntity && (entity = (packet = (CPacketUseEntity)event.getPacket()).func_149564_a((World)mc.field_71441_e)) != null && Phobos.friendManager.isFriend(entity.func_70005_c_())) {
/* 102 */       event.setCanceled(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/* 108 */     if (!fullNullCheck()) {
/* 109 */       if (mc.field_71439_g.field_71071_by.field_70461_c != this.lastHotbarSlot && mc.field_71439_g.field_71071_by.field_70461_c != this.currentTargetSlot) {
/* 110 */         this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*     */       }
/* 112 */       if (!mc.field_71474_y.field_74312_F.func_151470_d() && this.switched) {
/* 113 */         equip(this.lastHotbarSlot, false);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void removeGlitchBlocks(BlockPos pos) {
/* 119 */     for (int dx = -4; dx <= 4; dx++) {
/* 120 */       for (int dy = -4; dy <= 4; dy++) {
/* 121 */         for (int dz = -4; dz <= 4; dz++) {
/* 122 */           BlockPos blockPos = new BlockPos(pos.func_177958_n() + dx, pos.func_177956_o() + dy, pos.func_177952_p() + dz);
/* 123 */           if (mc.field_71441_e.func_180495_p(blockPos).func_177230_c().equals(Blocks.field_150350_a))
/* 124 */             mc.field_71442_b.func_187099_a(mc.field_71439_g, mc.field_71441_e, blockPos, EnumFacing.DOWN, new Vec3d(0.5D, 0.5D, 0.5D), EnumHand.MAIN_HAND); 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void equipBestTool(IBlockState blockState) {
/* 131 */     int bestSlot = -1;
/* 132 */     double max = 0.0D;
/* 133 */     for (int i = 0; i < 9; i++) {
/*     */ 
/*     */       
/* 136 */       ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i); float speed;
/* 137 */       if (!stack.field_190928_g && (speed = stack.func_150997_a(blockState)) > 1.0F) { int eff; if ((speed = (float)(speed + (((eff = EnchantmentHelper.func_77506_a(Enchantments.field_185305_q, stack)) > 0) ? (Math.pow(eff, 2.0D) + 1.0D) : 0.0D))) > max)
/*     */         
/* 139 */         { max = speed;
/* 140 */           bestSlot = i; }  }
/*     */     
/* 142 */     }  equip(bestSlot, true);
/*     */   }
/*     */   
/*     */   public void equipBestWeapon(Entity entity) {
/* 146 */     int bestSlot = -1;
/* 147 */     double maxDamage = 0.0D;
/* 148 */     EnumCreatureAttribute creatureAttribute = EnumCreatureAttribute.UNDEFINED;
/* 149 */     if (EntityUtil.isLiving(entity)) {
/* 150 */       EntityLivingBase base = (EntityLivingBase)entity;
/* 151 */       creatureAttribute = base.func_70668_bt();
/*     */     } 
/* 153 */     for (int i = 0; i < 9; i++) {
/*     */       
/* 155 */       ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
/* 156 */       if (!stack.field_190928_g)
/* 157 */         if (stack.func_77973_b() instanceof ItemTool) {
/* 158 */           double damage = ((ItemTool)stack.func_77973_b()).field_77865_bY + EnchantmentHelper.func_152377_a(stack, creatureAttribute);
/* 159 */           if (damage > maxDamage) {
/* 160 */             maxDamage = damage;
/* 161 */             bestSlot = i;
/*     */           } 
/*     */         } else {
/* 164 */           double damage; if (stack.func_77973_b() instanceof ItemSword && (damage = ((ItemSword)stack.func_77973_b()).func_150931_i() + EnchantmentHelper.func_152377_a(stack, creatureAttribute)) > maxDamage)
/*     */           
/* 166 */           { maxDamage = damage;
/* 167 */             bestSlot = i; } 
/*     */         }  
/* 169 */     }  equip(bestSlot, true);
/*     */   }
/*     */   
/*     */   private void equip(int slot, boolean equipTool) {
/* 173 */     if (slot != -1) {
/* 174 */       if (slot != mc.field_71439_g.field_71071_by.field_70461_c) {
/* 175 */         this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*     */       }
/* 177 */       this.currentTargetSlot = slot;
/* 178 */       mc.field_71439_g.field_71071_by.field_70461_c = slot;
/* 179 */       mc.field_71442_b.func_78750_j();
/* 180 */       this.switched = equipTool;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\player\BlockTweaks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */