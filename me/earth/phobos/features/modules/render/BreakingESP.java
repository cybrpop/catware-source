/*    */ package me.earth.phobos.features.modules.render;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import me.earth.phobos.event.events.BlockBreakingEvent;
/*    */ import me.earth.phobos.event.events.Render3DEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class BreakingESP
/*    */   extends Module
/*    */ {
/* 14 */   private final Map<BlockPos, Integer> breakingProgressMap = new HashMap<>();
/*    */   
/*    */   public BreakingESP() {
/* 17 */     super("BreakingESP", "Shows block breaking progress", Module.Category.RENDER, true, false, false);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onBlockBreak(BlockBreakingEvent event) {
/* 22 */     this.breakingProgressMap.put(event.pos, Integer.valueOf(event.breakStage));
/*    */   }
/*    */ 
/*    */   
/*    */   public void onRender3D(Render3DEvent event) {}
/*    */   
/*    */   public enum Mode
/*    */   {
/* 30 */     BAR,
/* 31 */     ALPHA,
/* 32 */     WIDTH;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\render\BreakingESP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */