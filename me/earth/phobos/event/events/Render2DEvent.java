/*    */ package me.earth.phobos.event.events;
/*    */ 
/*    */ import me.earth.phobos.event.EventStage;
/*    */ import net.minecraft.client.gui.ScaledResolution;
/*    */ 
/*    */ public class Render2DEvent
/*    */   extends EventStage {
/*    */   public float partialTicks;
/*    */   public ScaledResolution scaledResolution;
/*    */   
/*    */   public Render2DEvent(float partialTicks, ScaledResolution scaledResolution) {
/* 12 */     this.partialTicks = partialTicks;
/* 13 */     this.scaledResolution = scaledResolution;
/*    */   }
/*    */   
/*    */   public void setPartialTicks(float partialTicks) {
/* 17 */     this.partialTicks = partialTicks;
/*    */   }
/*    */   
/*    */   public void setScaledResolution(ScaledResolution scaledResolution) {
/* 21 */     this.scaledResolution = scaledResolution;
/*    */   }
/*    */   
/*    */   public double getScreenWidth() {
/* 25 */     return this.scaledResolution.func_78327_c();
/*    */   }
/*    */   
/*    */   public double getScreenHeight() {
/* 29 */     return this.scaledResolution.func_78324_d();
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\event\events\Render2DEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */