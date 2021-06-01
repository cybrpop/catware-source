/*    */ package me.earth.phobos.features.modules.movement;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.HoleUtil;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Anchor
/*    */   extends Module
/*    */ {
/* 17 */   private final Setting<Boolean> guarantee = register(new Setting("Guarantee Hole", Boolean.valueOf(true)));
/* 18 */   private final Setting<Integer> activateHeight = register(new Setting("Activate Height", Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(5)));
/*    */   private BlockPos playerPos;
/*    */   
/*    */   public Anchor() {
/* 22 */     super("Anchor", "Pulls you to a safe hole", Module.Category.MOVEMENT, true, false, false);
/*    */   }
/*    */   
/*    */   public void onUpdate() {
/* 26 */     if (mc.field_71439_g == null) {
/*    */       return;
/*    */     }
/*    */     
/* 30 */     if (mc.field_71439_g.field_70163_u < 0.0D) {
/*    */       return;
/*    */     }
/*    */     
/* 34 */     double blockX = Math.floor(mc.field_71439_g.field_70165_t);
/* 35 */     double blockZ = Math.floor(mc.field_71439_g.field_70161_v);
/*    */     
/* 37 */     double offsetX = Math.abs(mc.field_71439_g.field_70165_t - blockX);
/* 38 */     double offsetZ = Math.abs(mc.field_71439_g.field_70161_v - blockZ);
/*    */     
/* 40 */     if (((Boolean)this.guarantee.getValue()).booleanValue() && (offsetX < 0.30000001192092896D || offsetX > 0.699999988079071D || offsetZ < 0.30000001192092896D || offsetZ > 0.699999988079071D)) {
/*    */       return;
/*    */     }
/*    */     
/* 44 */     this.playerPos = new BlockPos(blockX, mc.field_71439_g.field_70163_u, blockZ);
/*    */     
/* 46 */     if (mc.field_71441_e.func_180495_p(this.playerPos).func_177230_c() != Blocks.field_150350_a) {
/*    */       return;
/*    */     }
/*    */     
/* 50 */     BlockPos currentBlock = this.playerPos.func_177977_b();
/* 51 */     for (int i = 0; i < ((Integer)this.activateHeight.getValue()).intValue(); i++) {
/* 52 */       currentBlock = currentBlock.func_177977_b();
/* 53 */       if (mc.field_71441_e.func_180495_p(currentBlock).func_177230_c() != Blocks.field_150350_a) {
/* 54 */         HashMap<HoleUtil.BlockOffset, HoleUtil.BlockSafety> sides = HoleUtil.getUnsafeSides(currentBlock.func_177984_a());
/* 55 */         sides.entrySet().removeIf(entry -> (entry.getValue() == HoleUtil.BlockSafety.RESISTANT));
/* 56 */         if (sides.size() == 0) {
/* 57 */           mc.field_71439_g.field_70159_w = 0.0D;
/* 58 */           mc.field_71439_g.field_70179_y = 0.0D;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\movement\Anchor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */