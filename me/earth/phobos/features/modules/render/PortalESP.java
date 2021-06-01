/*    */ package me.earth.phobos.features.modules.render;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.util.ArrayList;
/*    */ import me.earth.phobos.event.events.Render3DEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.RenderUtil;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ 
/*    */ 
/*    */ public class PortalESP
/*    */   extends Module
/*    */ {
/* 18 */   private final ArrayList<BlockPos> blockPosArrayList = new ArrayList<>();
/* 19 */   private final Setting<Integer> distance = register(new Setting("Distance", Integer.valueOf(60), Integer.valueOf(10), Integer.valueOf(100)));
/* 20 */   private final Setting<Boolean> box = register(new Setting("Box", Boolean.valueOf(false)));
/* 21 */   private final Setting<Integer> boxAlpha = register(new Setting("BoxAlpha", Integer.valueOf(125), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.box.getValue()).booleanValue()));
/* 22 */   private final Setting<Boolean> outline = register(new Setting("Outline", Boolean.valueOf(true)));
/* 23 */   private final Setting<Float> lineWidth = register(new Setting("LineWidth", Float.valueOf(1.0F), Float.valueOf(0.1F), Float.valueOf(5.0F), v -> ((Boolean)this.outline.getValue()).booleanValue()));
/*    */   private int cooldownTicks;
/*    */   
/*    */   public PortalESP() {
/* 27 */     super("PortalESP", "Draws portals", Module.Category.RENDER, true, false, false);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onTickEvent(TickEvent.ClientTickEvent event) {
/* 32 */     if (mc.field_71441_e == null) {
/*    */       return;
/*    */     }
/* 35 */     if (this.cooldownTicks < 1) {
/* 36 */       this.blockPosArrayList.clear();
/* 37 */       compileDL();
/* 38 */       this.cooldownTicks = 80;
/*    */     } 
/* 40 */     this.cooldownTicks--;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onRender3D(Render3DEvent event) {
/* 45 */     if (mc.field_71441_e == null) {
/*    */       return;
/*    */     }
/* 48 */     for (BlockPos pos : this.blockPosArrayList) {
/* 49 */       RenderUtil.drawBoxESP(pos, new Color(204, 0, 153, 255), false, new Color(204, 0, 153, 255), ((Float)this.lineWidth.getValue()).floatValue(), ((Boolean)this.outline.getValue()).booleanValue(), ((Boolean)this.box.getValue()).booleanValue(), ((Integer)this.boxAlpha.getValue()).intValue(), false);
/*    */     }
/*    */   }
/*    */   
/*    */   private void compileDL() {
/* 54 */     if (mc.field_71441_e == null || mc.field_71439_g == null) {
/*    */       return;
/*    */     }
/* 57 */     for (int x = (int)mc.field_71439_g.field_70165_t - ((Integer)this.distance.getValue()).intValue(); x <= (int)mc.field_71439_g.field_70165_t + ((Integer)this.distance.getValue()).intValue(); x++) {
/* 58 */       for (int y = (int)mc.field_71439_g.field_70163_u - ((Integer)this.distance.getValue()).intValue(); y <= (int)mc.field_71439_g.field_70163_u + ((Integer)this.distance.getValue()).intValue(); y++) {
/* 59 */         int z = (int)Math.max(mc.field_71439_g.field_70161_v - ((Integer)this.distance.getValue()).intValue(), 0.0D);
/* 60 */         while (z <= Math.min(mc.field_71439_g.field_70161_v + ((Integer)this.distance.getValue()).intValue(), 255.0D)) {
/* 61 */           BlockPos pos = new BlockPos(x, y, z);
/* 62 */           Block block = mc.field_71441_e.func_180495_p(pos).func_177230_c();
/* 63 */           if (block instanceof net.minecraft.block.BlockPortal) {
/* 64 */             this.blockPosArrayList.add(pos);
/*    */           }
/* 66 */           z++;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\render\PortalESP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */