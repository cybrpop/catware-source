/*    */ package me.earth.phobos.features.modules.movement;
/*    */ 
/*    */ import me.earth.phobos.event.events.StepEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketPlayer;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class Step
/*    */   extends Module {
/*    */   private static Step instance;
/* 14 */   public Setting<Boolean> vanilla = register(new Setting("Vanilla", Boolean.valueOf(false)));
/* 15 */   public Setting<Integer> stepHeight = register(new Setting("Height", Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(2)));
/* 16 */   public Setting<Boolean> turnOff = register(new Setting("Disable", Boolean.valueOf(false)));
/*    */   
/*    */   public Step() {
/* 19 */     super("Step", "Allows you to step up blocks", Module.Category.MOVEMENT, true, false, false);
/* 20 */     instance = this;
/*    */   }
/*    */   
/*    */   public static Step getInstance() {
/* 24 */     if (instance == null) {
/* 25 */       instance = new Step();
/*    */     }
/* 27 */     return instance;
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onStep(StepEvent event) {
/* 32 */     if (mc.field_71439_g.field_70122_E && !mc.field_71439_g.func_70055_a(Material.field_151586_h) && !mc.field_71439_g.func_70055_a(Material.field_151587_i) && mc.field_71439_g.field_70124_G && mc.field_71439_g.field_70143_R == 0.0F && !mc.field_71474_y.field_74314_A.field_74513_e && !mc.field_71439_g.func_70617_f_()) {
/* 33 */       event.setHeight(((Integer)this.stepHeight.getValue()).intValue());
/* 34 */       double rheight = (mc.field_71439_g.func_174813_aQ()).field_72338_b - mc.field_71439_g.field_70163_u;
/* 35 */       if (rheight >= 0.625D) {
/* 36 */         if (!((Boolean)this.vanilla.getValue()).booleanValue()) {
/* 37 */           ncpStep(rheight);
/*    */         }
/* 39 */         if (((Boolean)this.turnOff.getValue()).booleanValue()) {
/* 40 */           disable();
/*    */         }
/*    */       } 
/*    */     } else {
/* 44 */       event.setHeight(0.6F);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void ncpStep(double height) {
/* 56 */     double posX = mc.field_71439_g.field_70165_t;
/* 57 */     double posZ = mc.field_71439_g.field_70161_v;
/* 58 */     double y = mc.field_71439_g.field_70163_u;
/* 59 */     if (height >= 1.1D) {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 80 */       if (height < 1.6D) {
/*    */         double[] offset;
/* 82 */         for (double off : offset = new double[] { 0.42D, 0.33D, 0.24D, 0.083D, -0.078D }) {
/* 83 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(posX, y += off, posZ, false));
/*    */         }
/* 85 */       } else if (height < 2.1D) {
/*    */         double[] heights;
/* 87 */         for (double off : heights = new double[] { 0.425D, 0.821D, 0.699D, 0.599D, 1.022D, 1.372D, 1.652D, 1.869D }) {
/* 88 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(posX, y + off, posZ, false));
/*    */         }
/*    */       } else {
/*    */         double[] heights;
/* 92 */         for (double off : heights = new double[] { 0.425D, 0.821D, 0.699D, 0.599D, 1.022D, 1.372D, 1.652D, 1.869D, 2.019D, 1.907D })
/* 93 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(posX, y + off, posZ, false)); 
/*    */       } 
/*    */       return;
/*    */     } 
/*    */     double first = 0.42D;
/*    */     double d1 = 0.75D;
/*    */     if (height != 1.0D) {
/*    */       first *= height;
/*    */       d1 *= height;
/*    */       if (first > 0.425D)
/*    */         first = 0.425D; 
/*    */       if (d1 > 0.78D)
/*    */         d1 = 0.78D; 
/*    */       if (d1 < 0.49D)
/*    */         d1 = 0.49D; 
/*    */     } 
/*    */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(posX, y + first, posZ, false));
/*    */     if (y + d1 < y + height)
/*    */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(posX, y + d1, posZ, false)); 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\movement\Step.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */