/*     */ package me.earth.phobos.features.modules.combat;
/*     */ 
/*     */ import me.earth.phobos.features.command.Command;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.BurrowUtil;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockChest;
/*     */ import net.minecraft.block.BlockEnderChest;
/*     */ import net.minecraft.block.BlockObsidian;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketEntityAction;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Burrow
/*     */   extends Module
/*     */ {
/*     */   private final Setting<Integer> offset;
/*     */   private final Setting<Boolean> rotate;
/*     */   private final Setting<Mode> mode;
/*     */   private BlockPos originalPos;
/*     */   private int oldSlot;
/*     */   Block returnBlock;
/*     */   
/*     */   public Burrow() {
/*  33 */     super("Burrow", "Puts you into a block", Module.Category.COMBAT, true, false, false);
/*  34 */     this.offset = register(new Setting("Offset", Integer.valueOf(3), Integer.valueOf(-5), Integer.valueOf(5)));
/*  35 */     this.rotate = register(new Setting("Rotate", Boolean.valueOf(false)));
/*  36 */     this.mode = register(new Setting("Mode", Mode.OBBY));
/*  37 */     this.oldSlot = -1;
/*  38 */     this.returnBlock = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  43 */     super.onEnable();
/*  44 */     this.originalPos = new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v);
/*  45 */     switch ((Mode)this.mode.getValue()) {
/*     */       case OBBY:
/*  47 */         this.returnBlock = Blocks.field_150343_Z;
/*     */         break;
/*     */       
/*     */       case ECHEST:
/*  51 */         this.returnBlock = Blocks.field_150477_bB;
/*     */         break;
/*     */       
/*     */       case EABypass:
/*  55 */         this.returnBlock = (Block)Blocks.field_150486_ae;
/*     */         break;
/*     */     } 
/*     */     
/*  59 */     if (mc.field_71441_e.func_180495_p(new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v)).func_177230_c().equals(this.returnBlock) || intersectsWithEntity(this.originalPos)) {
/*  60 */       toggle();
/*     */       return;
/*     */     } 
/*  63 */     this.oldSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  68 */     switch ((Mode)this.mode.getValue()) {
/*     */       case OBBY:
/*  70 */         if (BurrowUtil.findHotbarBlock(BlockObsidian.class) == -1) {
/*  71 */           Command.sendMessage("Can't find obby in hotbar!");
/*  72 */           toggle();
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case ECHEST:
/*  78 */         if (BurrowUtil.findHotbarBlock(BlockEnderChest.class) == -1) {
/*  79 */           Command.sendMessage("Can't find echest in hotbar!");
/*  80 */           toggle();
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case EABypass:
/*  86 */         if (BurrowUtil.findHotbarBlock(BlockChest.class) == -1) {
/*  87 */           Command.sendMessage("Can't find chest in hotbar!");
/*  88 */           toggle();
/*     */         } 
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/*  94 */     BurrowUtil.switchToSlot((this.mode.getValue() == Mode.OBBY) ? BurrowUtil.findHotbarBlock(BlockObsidian.class) : ((this.mode.getValue() == Mode.ECHEST) ? BurrowUtil.findHotbarBlock(BlockEnderChest.class) : BurrowUtil.findHotbarBlock(BlockChest.class)));
/*  95 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.41999998688698D, mc.field_71439_g.field_70161_v, true));
/*  96 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.7531999805211997D, mc.field_71439_g.field_70161_v, true));
/*  97 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 1.00133597911214D, mc.field_71439_g.field_70161_v, true));
/*  98 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 1.16610926093821D, mc.field_71439_g.field_70161_v, true));
/*  99 */     BurrowUtil.placeBlock(this.originalPos, EnumHand.MAIN_HAND, ((Boolean)this.rotate.getValue()).booleanValue(), true, false);
/* 100 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + ((Integer)this.offset.getValue()).intValue(), mc.field_71439_g.field_70161_v, false));
/* 101 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
/* 102 */     mc.field_71439_g.func_70095_a(false);
/* 103 */     BurrowUtil.switchToSlot(this.oldSlot);
/* 104 */     toggle();
/*     */   }
/*     */   
/*     */   private boolean intersectsWithEntity(BlockPos pos) {
/* 108 */     for (Entity entity : mc.field_71441_e.field_72996_f) {
/* 109 */       if (entity.equals(mc.field_71439_g)) {
/*     */         continue;
/*     */       }
/* 112 */       if (entity instanceof net.minecraft.entity.item.EntityItem) {
/*     */         continue;
/*     */       }
/* 115 */       if ((new AxisAlignedBB(pos)).func_72326_a(entity.func_174813_aQ())) {
/* 116 */         return true;
/*     */       }
/*     */     } 
/* 119 */     return false;
/*     */   }
/*     */   
/*     */   public enum Mode
/*     */   {
/* 124 */     OBBY,
/* 125 */     ECHEST,
/* 126 */     EABypass;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\combat\Burrow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */