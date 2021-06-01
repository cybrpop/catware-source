/*     */ package me.earth.phobos.features.modules.misc;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.BlockEvent;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.BlockUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Nuker
/*     */   extends Module
/*     */ {
/*  28 */   private final Timer timer = new Timer();
/*  29 */   public Setting<Boolean> rotate = register(new Setting("Rotate", Boolean.valueOf(false)));
/*  30 */   public Setting<Float> distance = register(new Setting("Range", Float.valueOf(6.0F), Float.valueOf(0.1F), Float.valueOf(10.0F)));
/*  31 */   public Setting<Integer> blockPerTick = register(new Setting("Blocks/Attack", Integer.valueOf(50), Integer.valueOf(1), Integer.valueOf(100)));
/*  32 */   public Setting<Integer> delay = register(new Setting("Delay/Attack", Integer.valueOf(50), Integer.valueOf(1), Integer.valueOf(1000)));
/*  33 */   public Setting<Boolean> nuke = register(new Setting("Nuke", Boolean.valueOf(false)));
/*  34 */   public Setting<Mode> mode = register(new Setting("Mode", Mode.NUKE, v -> ((Boolean)this.nuke.getValue()).booleanValue()));
/*  35 */   public Setting<Boolean> antiRegear = register(new Setting("AntiRegear", Boolean.valueOf(false)));
/*  36 */   public Setting<Boolean> hopperNuker = register(new Setting("HopperAura", Boolean.valueOf(false)));
/*  37 */   private final Setting<Boolean> autoSwitch = register(new Setting("AutoSwitch", Boolean.valueOf(false)));
/*  38 */   private int oldSlot = -1;
/*     */   private boolean isMining = false;
/*     */   private Block selected;
/*     */   
/*     */   public Nuker() {
/*  43 */     super("Nuker", "Mines many blocks", Module.Category.MISC, true, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onToggle() {
/*  48 */     this.selected = null;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClickBlock(BlockEvent event) {
/*     */     Block block;
/*  54 */     if (event.getStage() == 3 && (this.mode.getValue() == Mode.SELECTION || this.mode.getValue() == Mode.NUKE) && (block = mc.field_71441_e.func_180495_p(event.pos).func_177230_c()) != null && block != this.selected) {
/*  55 */       this.selected = block;
/*  56 */       event.setCanceled(true);
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayerPre(UpdateWalkingPlayerEvent event) {
/*  62 */     if (event.getStage() == 0) {
/*  63 */       if (((Boolean)this.nuke.getValue()).booleanValue()) {
/*  64 */         BlockPos pos = null;
/*  65 */         switch ((Mode)this.mode.getValue()) {
/*     */           case SELECTION:
/*     */           case NUKE:
/*  68 */             pos = getClosestBlockSelection();
/*     */             break;
/*     */           
/*     */           case ALL:
/*  72 */             pos = getClosestBlockAll();
/*     */             break;
/*     */         } 
/*     */         
/*  76 */         if (pos != null)
/*  77 */           if (this.mode.getValue() == Mode.SELECTION || this.mode.getValue() == Mode.ALL) {
/*  78 */             if (((Boolean)this.rotate.getValue()).booleanValue()) {
/*  79 */               float[] angle = MathUtil.calcAngle(mc.field_71439_g.func_174824_e(mc.func_184121_ak()), new Vec3d((pos.func_177958_n() + 0.5F), (pos.func_177956_o() + 0.5F), (pos.func_177952_p() + 0.5F)));
/*  80 */               Phobos.rotationManager.setPlayerRotations(angle[0], angle[1]);
/*     */             } 
/*  82 */             if (canBreak(pos)) {
/*  83 */               mc.field_71442_b.func_180512_c(pos, mc.field_71439_g.func_174811_aO());
/*  84 */               mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/*     */             } 
/*     */           } else {
/*  87 */             for (int i = 0; i < ((Integer)this.blockPerTick.getValue()).intValue(); i++) {
/*  88 */               pos = getClosestBlockSelection();
/*  89 */               if (pos != null) {
/*  90 */                 if (((Boolean)this.rotate.getValue()).booleanValue()) {
/*  91 */                   float[] angle = MathUtil.calcAngle(mc.field_71439_g.func_174824_e(mc.func_184121_ak()), new Vec3d((pos.func_177958_n() + 0.5F), (pos.func_177956_o() + 0.5F), (pos.func_177952_p() + 0.5F)));
/*  92 */                   Phobos.rotationManager.setPlayerRotations(angle[0], angle[1]);
/*     */                 } 
/*  94 */                 if (this.timer.passedMs(((Integer)this.delay.getValue()).intValue())) {
/*  95 */                   mc.field_71442_b.func_180512_c(pos, mc.field_71439_g.func_174811_aO());
/*  96 */                   mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/*  97 */                   this.timer.reset();
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           }  
/* 102 */       }  if (((Boolean)this.antiRegear.getValue()).booleanValue()) {
/* 103 */         breakBlocks(BlockUtil.shulkerList);
/*     */       }
/* 105 */       if (((Boolean)this.hopperNuker.getValue()).booleanValue()) {
/* 106 */         ArrayList<Block> blocklist = new ArrayList<>();
/* 107 */         blocklist.add(Blocks.field_150438_bZ);
/* 108 */         breakBlocks(blocklist);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void breakBlocks(List<Block> blocks) {
/* 114 */     BlockPos pos = getNearestBlock(blocks);
/* 115 */     if (pos != null) {
/* 116 */       if (!this.isMining) {
/* 117 */         this.oldSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/* 118 */         this.isMining = true;
/*     */       } 
/* 120 */       if (((Boolean)this.rotate.getValue()).booleanValue()) {
/* 121 */         float[] angle = MathUtil.calcAngle(mc.field_71439_g.func_174824_e(mc.func_184121_ak()), new Vec3d((pos.func_177958_n() + 0.5F), (pos.func_177956_o() + 0.5F), (pos.func_177952_p() + 0.5F)));
/* 122 */         Phobos.rotationManager.setPlayerRotations(angle[0], angle[1]);
/*     */       } 
/* 124 */       if (canBreak(pos)) {
/* 125 */         if (((Boolean)this.autoSwitch.getValue()).booleanValue()) {
/* 126 */           int newSlot = -1;
/* 127 */           for (int i = 0; i < 9; ) {
/* 128 */             ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
/* 129 */             if (stack == ItemStack.field_190927_a || !(stack.func_77973_b() instanceof net.minecraft.item.ItemPickaxe)) { i++; continue; }
/* 130 */              newSlot = i;
/*     */           } 
/*     */           
/* 133 */           if (newSlot != -1) {
/* 134 */             mc.field_71439_g.field_71071_by.field_70461_c = newSlot;
/*     */           }
/*     */         } 
/* 137 */         mc.field_71442_b.func_180512_c(pos, mc.field_71439_g.func_174811_aO());
/* 138 */         mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/*     */       } 
/* 140 */     } else if (((Boolean)this.autoSwitch.getValue()).booleanValue() && this.oldSlot != -1) {
/* 141 */       mc.field_71439_g.field_71071_by.field_70461_c = this.oldSlot;
/* 142 */       this.oldSlot = -1;
/* 143 */       this.isMining = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean canBreak(BlockPos pos) {
/* 148 */     IBlockState blockState = mc.field_71441_e.func_180495_p(pos);
/* 149 */     Block block = blockState.func_177230_c();
/* 150 */     return (block.func_176195_g(blockState, (World)mc.field_71441_e, pos) != -1.0F);
/*     */   }
/*     */   
/*     */   private BlockPos getNearestBlock(List<Block> blocks) {
/* 154 */     double maxDist = MathUtil.square(((Float)this.distance.getValue()).floatValue());
/* 155 */     BlockPos ret = null; double x;
/* 156 */     for (x = maxDist; x >= -maxDist; x--) {
/* 157 */       double y; for (y = maxDist; y >= -maxDist; y--) {
/* 158 */         double z; for (z = maxDist; z >= -maxDist; z--) {
/* 159 */           BlockPos pos = new BlockPos(mc.field_71439_g.field_70165_t + x, mc.field_71439_g.field_70163_u + y, mc.field_71439_g.field_70161_v + z);
/* 160 */           double dist = mc.field_71439_g.func_70092_e(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
/* 161 */           if (dist <= maxDist && blocks.contains(mc.field_71441_e.func_180495_p(pos).func_177230_c()) && canBreak(pos)) {
/*     */             
/* 163 */             maxDist = dist;
/* 164 */             ret = pos;
/*     */           } 
/*     */         } 
/*     */       } 
/* 168 */     }  return ret;
/*     */   }
/*     */   
/*     */   private BlockPos getClosestBlockAll() {
/* 172 */     float maxDist = ((Float)this.distance.getValue()).floatValue();
/* 173 */     BlockPos ret = null;
/* 174 */     for (float x = maxDist; x >= -maxDist; x--) {
/* 175 */       float y; for (y = maxDist; y >= -maxDist; y--) {
/* 176 */         float z; for (z = maxDist; z >= -maxDist; z--) {
/* 177 */           BlockPos pos = new BlockPos(mc.field_71439_g.field_70165_t + x, mc.field_71439_g.field_70163_u + y, mc.field_71439_g.field_70161_v + z);
/* 178 */           double dist = mc.field_71439_g.func_70011_f(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
/* 179 */           if (dist <= maxDist && mc.field_71441_e.func_180495_p(pos).func_177230_c() != Blocks.field_150350_a && !(mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof net.minecraft.block.BlockLiquid) && canBreak(pos) && pos.func_177956_o() >= mc.field_71439_g.field_70163_u) {
/*     */             
/* 181 */             maxDist = (float)dist;
/* 182 */             ret = pos;
/*     */           } 
/*     */         } 
/*     */       } 
/* 186 */     }  return ret;
/*     */   }
/*     */   
/*     */   private BlockPos getClosestBlockSelection() {
/* 190 */     float maxDist = ((Float)this.distance.getValue()).floatValue();
/* 191 */     BlockPos ret = null;
/* 192 */     for (float x = maxDist; x >= -maxDist; x--) {
/* 193 */       float y; for (y = maxDist; y >= -maxDist; y--) {
/* 194 */         float z; for (z = maxDist; z >= -maxDist; z--) {
/* 195 */           BlockPos pos = new BlockPos(mc.field_71439_g.field_70165_t + x, mc.field_71439_g.field_70163_u + y, mc.field_71439_g.field_70161_v + z);
/* 196 */           double dist = mc.field_71439_g.func_70011_f(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
/* 197 */           if (dist <= maxDist && mc.field_71441_e.func_180495_p(pos).func_177230_c() != Blocks.field_150350_a && !(mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof net.minecraft.block.BlockLiquid) && mc.field_71441_e.func_180495_p(pos).func_177230_c() == this.selected && canBreak(pos) && pos.func_177956_o() >= mc.field_71439_g.field_70163_u) {
/*     */             
/* 199 */             maxDist = (float)dist;
/* 200 */             ret = pos;
/*     */           } 
/*     */         } 
/*     */       } 
/* 204 */     }  return ret;
/*     */   }
/*     */   
/*     */   public enum Mode {
/* 208 */     SELECTION,
/* 209 */     ALL,
/* 210 */     NUKE;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\misc\Nuker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */