/*    */ package me.earth.phobos.features.modules.movement;
/*    */ 
/*    */ import me.earth.phobos.event.events.MoveEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class SafeWalk
/*    */   extends Module {
/*    */   public SafeWalk() {
/* 12 */     super("SafeWalk", "Walks safe", Module.Category.MOVEMENT, true, false, false);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onMove(MoveEvent event) {
/* 17 */     if (event.getStage() == 0) {
/* 18 */       double x = event.getX();
/* 19 */       double y = event.getY();
/* 20 */       double z = event.getZ();
/* 21 */       if (mc.field_71439_g.field_70122_E) {
/* 22 */         double increment = 0.05D;
/* 23 */         while (x != 0.0D && isOffsetBBEmpty(x, -1.0D, 0.0D)) {
/* 24 */           if (x < increment && x >= -increment) {
/* 25 */             x = 0.0D;
/*    */             continue;
/*    */           } 
/* 28 */           if (x > 0.0D) {
/* 29 */             x -= increment;
/*    */             continue;
/*    */           } 
/* 32 */           x += increment;
/*    */         } 
/* 34 */         while (z != 0.0D && isOffsetBBEmpty(0.0D, -1.0D, z)) {
/* 35 */           if (z < increment && z >= -increment) {
/* 36 */             z = 0.0D;
/*    */             continue;
/*    */           } 
/* 39 */           if (z > 0.0D) {
/* 40 */             z -= increment;
/*    */             continue;
/*    */           } 
/* 43 */           z += increment;
/*    */         } 
/* 45 */         while (x != 0.0D && z != 0.0D && isOffsetBBEmpty(x, -1.0D, z)) {
/* 46 */           x = (x < increment && x >= -increment) ? 0.0D : ((x > 0.0D) ? (x -= increment) : (x += increment));
/* 47 */           if (z < increment && z >= -increment) {
/* 48 */             z = 0.0D;
/*    */             continue;
/*    */           } 
/* 51 */           if (z > 0.0D) {
/* 52 */             z -= increment;
/*    */             continue;
/*    */           } 
/* 55 */           z += increment;
/*    */         } 
/*    */       } 
/* 58 */       event.setX(x);
/* 59 */       event.setY(y);
/* 60 */       event.setZ(z);
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean isOffsetBBEmpty(double offsetX, double offsetY, double offsetZ) {
/* 65 */     EntityPlayerSP playerSP = mc.field_71439_g;
/* 66 */     return mc.field_71441_e.func_184144_a((Entity)playerSP, playerSP.func_174813_aQ().func_72317_d(offsetX, offsetY, offsetZ)).isEmpty();
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\movement\SafeWalk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */