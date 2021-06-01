/*    */ package me.earth.phobos.features.modules.movement;
/*    */ 
/*    */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.util.BlockUtil;
/*    */ import me.earth.phobos.util.EntityUtil;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketPlayer;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class HoleTP
/*    */   extends Module {
/* 15 */   private static HoleTP INSTANCE = new HoleTP();
/* 16 */   private final double[] oneblockPositions = new double[] { 0.42D, 0.75D };
/*    */   private int packets;
/*    */   private boolean jumped = false;
/*    */   
/*    */   public HoleTP() {
/* 21 */     super("HoleTP", "Teleports you in a hole.", Module.Category.MOVEMENT, true, true, false);
/* 22 */     setInstance();
/*    */   }
/*    */   
/*    */   public static HoleTP getInstance() {
/* 26 */     if (INSTANCE == null) {
/* 27 */       INSTANCE = new HoleTP();
/*    */     }
/* 29 */     return INSTANCE;
/*    */   }
/*    */   
/*    */   private void setInstance() {
/* 33 */     INSTANCE = this;
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/* 38 */     if (event.getStage() == 1 && (Speed.getInstance().isOff() || (Speed.getInstance()).mode.getValue() == Speed.Mode.INSTANT) && Strafe.getInstance().isOff() && LagBlock.getInstance().isOff()) {
/* 39 */       if (!mc.field_71439_g.field_70122_E) {
/* 40 */         if (mc.field_71474_y.field_74314_A.func_151470_d()) {
/* 41 */           this.jumped = true;
/*    */         }
/*    */       } else {
/* 44 */         this.jumped = false;
/*    */       } 
/* 46 */       if (!this.jumped && mc.field_71439_g.field_70143_R < 0.5D && BlockUtil.isInHole() && mc.field_71439_g.field_70163_u - BlockUtil.getNearestBlockBelow() <= 1.125D && mc.field_71439_g.field_70163_u - BlockUtil.getNearestBlockBelow() <= 0.95D && !EntityUtil.isOnLiquid() && !EntityUtil.isInLiquid()) {
/* 47 */         if (!mc.field_71439_g.field_70122_E) {
/* 48 */           this.packets++;
/*    */         }
/* 50 */         if (!mc.field_71439_g.field_70122_E && !mc.field_71439_g.func_70055_a(Material.field_151586_h) && !mc.field_71439_g.func_70055_a(Material.field_151587_i) && !mc.field_71474_y.field_74314_A.func_151470_d() && !mc.field_71439_g.func_70617_f_() && this.packets > 0) {
/* 51 */           BlockPos blockPos = new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v);
/* 52 */           for (double position : this.oneblockPositions) {
/* 53 */             mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position((blockPos.func_177958_n() + 0.5F), mc.field_71439_g.field_70163_u - position, (blockPos.func_177952_p() + 0.5F), true));
/*    */           }
/* 55 */           mc.field_71439_g.func_70107_b((blockPos.func_177958_n() + 0.5F), BlockUtil.getNearestBlockBelow() + 0.1D, (blockPos.func_177952_p() + 0.5F));
/* 56 */           this.packets = 0;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\movement\HoleTP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */