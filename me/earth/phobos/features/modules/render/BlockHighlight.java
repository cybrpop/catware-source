/*    */ package me.earth.phobos.features.modules.render;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import me.earth.phobos.event.events.Render3DEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.modules.client.Colors;
/*    */ import me.earth.phobos.features.modules.client.HUD;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.RenderUtil;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.util.math.RayTraceResult;
/*    */ 
/*    */ public class BlockHighlight
/*    */   extends Module
/*    */ {
/* 16 */   private final Setting<Integer> red = register(new Setting("Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255)));
/* 17 */   private final Setting<Integer> green = register(new Setting("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/* 18 */   private final Setting<Integer> blue = register(new Setting("Blue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255)));
/* 19 */   private final Setting<Integer> alpha = register(new Setting("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/* 20 */   public Setting<Boolean> colorSync = register(new Setting("Sync", Boolean.valueOf(false)));
/* 21 */   public Setting<Boolean> rolling = register(new Setting("Rolling", Boolean.valueOf(false), v -> ((Boolean)this.colorSync.getValue()).booleanValue()));
/* 22 */   public Setting<Boolean> box = register(new Setting("Box", Boolean.valueOf(false)));
/* 23 */   private final Setting<Integer> boxAlpha = register(new Setting("BoxAlpha", Integer.valueOf(125), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.box.getValue()).booleanValue()));
/* 24 */   public Setting<Boolean> outline = register(new Setting("Outline", Boolean.valueOf(true)));
/* 25 */   private final Setting<Float> lineWidth = register(new Setting("LineWidth", Float.valueOf(1.0F), Float.valueOf(0.1F), Float.valueOf(5.0F), v -> ((Boolean)this.outline.getValue()).booleanValue()));
/* 26 */   public Setting<Boolean> customOutline = register(new Setting("CustomLine", Boolean.valueOf(false), v -> ((Boolean)this.outline.getValue()).booleanValue()));
/* 27 */   private final Setting<Integer> cRed = register(new Setting("OL-Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.customOutline.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue())));
/* 28 */   private final Setting<Integer> cGreen = register(new Setting("OL-Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.customOutline.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue())));
/* 29 */   private final Setting<Integer> cBlue = register(new Setting("OL-Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.customOutline.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue())));
/* 30 */   private final Setting<Integer> cAlpha = register(new Setting("OL-Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.customOutline.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue())));
/*    */   
/*    */   public BlockHighlight() {
/* 33 */     super("BlockHighlight", "Highlights the block u look at.", Module.Category.RENDER, false, false, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onRender3D(Render3DEvent event) {
/* 38 */     RayTraceResult ray = mc.field_71476_x;
/* 39 */     if (ray != null && ray.field_72313_a == RayTraceResult.Type.BLOCK) {
/* 40 */       BlockPos blockpos = ray.func_178782_a();
/* 41 */       if (((Boolean)this.rolling.getValue()).booleanValue()) {
/* 42 */         RenderUtil.drawProperGradientBlockOutline(blockpos, new Color(((Integer)(HUD.getInstance()).colorMap.get(Integer.valueOf(0))).intValue()), new Color(((Integer)(HUD.getInstance()).colorMap.get(Integer.valueOf(this.renderer.scaledHeight / 4))).intValue()), new Color(((Integer)(HUD.getInstance()).colorMap.get(Integer.valueOf(this.renderer.scaledHeight / 2))).intValue()), 1.0F);
/*    */       } else {
/* 44 */         RenderUtil.drawBoxESP(blockpos, ((Boolean)this.colorSync.getValue()).booleanValue() ? Colors.INSTANCE.getCurrentColor() : new Color(((Integer)this.red.getValue()).intValue(), ((Integer)this.green.getValue()).intValue(), ((Integer)this.blue.getValue()).intValue(), ((Integer)this.alpha.getValue()).intValue()), ((Boolean)this.customOutline.getValue()).booleanValue(), new Color(((Integer)this.cRed.getValue()).intValue(), ((Integer)this.cGreen.getValue()).intValue(), ((Integer)this.cBlue.getValue()).intValue(), ((Integer)this.cAlpha.getValue()).intValue()), ((Float)this.lineWidth.getValue()).floatValue(), ((Boolean)this.outline.getValue()).booleanValue(), ((Boolean)this.box.getValue()).booleanValue(), ((Integer)this.boxAlpha.getValue()).intValue(), false);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\render\BlockHighlight.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */