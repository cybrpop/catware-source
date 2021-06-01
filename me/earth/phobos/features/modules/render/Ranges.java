/*    */ package me.earth.phobos.features.modules.render;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.util.ArrayList;
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.event.events.Render3DEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.EntityUtil;
/*    */ import me.earth.phobos.util.RenderUtil;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.math.RayTraceResult;
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ 
/*    */ public class Ranges
/*    */   extends Module
/*    */ {
/* 22 */   private final Setting<Boolean> hitSpheres = register(new Setting("HitSpheres", Boolean.valueOf(false)));
/* 23 */   private final Setting<Boolean> circle = register(new Setting("Circle", Boolean.valueOf(true)));
/* 24 */   private final Setting<Boolean> ownSphere = register(new Setting("OwnSphere", Boolean.valueOf(false), v -> ((Boolean)this.hitSpheres.getValue()).booleanValue()));
/* 25 */   private final Setting<Boolean> raytrace = register(new Setting("RayTrace", Boolean.valueOf(false), v -> ((Boolean)this.circle.getValue()).booleanValue()));
/* 26 */   private final Setting<Float> lineWidth = register(new Setting("LineWidth", Float.valueOf(1.5F), Float.valueOf(0.1F), Float.valueOf(5.0F)));
/* 27 */   private final Setting<Double> radius = register(new Setting("Radius", Double.valueOf(4.5D), Double.valueOf(0.1D), Double.valueOf(8.0D)));
/*    */   
/*    */   public Ranges() {
/* 30 */     super("Ranges", "Draws a circle around the player.", Module.Category.RENDER, false, false, false);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onUpdate() {}
/*    */ 
/*    */   
/*    */   public void onRender3D(Render3DEvent event) {
/* 39 */     if (((Boolean)this.circle.getValue()).booleanValue()) {
/* 40 */       GlStateManager.func_179094_E();
/* 41 */       GlStateManager.func_179147_l();
/* 42 */       GlStateManager.func_179090_x();
/* 43 */       GlStateManager.func_179126_j();
/* 44 */       GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
/* 45 */       RenderManager renderManager = mc.func_175598_ae();
/* 46 */       float hue = (float)(System.currentTimeMillis() % 7200L) / 7200.0F;
/* 47 */       Color color = new Color(Color.HSBtoRGB(hue, 1.0F, 1.0F));
/* 48 */       ArrayList<Vec3d> hVectors = new ArrayList<>();
/* 49 */       double x = mc.field_71439_g.field_70142_S + (mc.field_71439_g.field_70165_t - mc.field_71439_g.field_70142_S) * event.getPartialTicks() - renderManager.field_78725_b;
/* 50 */       double y = mc.field_71439_g.field_70137_T + (mc.field_71439_g.field_70163_u - mc.field_71439_g.field_70137_T) * event.getPartialTicks() - renderManager.field_78726_c;
/* 51 */       double z = mc.field_71439_g.field_70136_U + (mc.field_71439_g.field_70161_v - mc.field_71439_g.field_70136_U) * event.getPartialTicks() - renderManager.field_78723_d;
/* 52 */       GL11.glLineWidth(((Float)this.lineWidth.getValue()).floatValue());
/* 53 */       GL11.glBegin(1);
/* 54 */       for (int i = 0; i <= 360; i++) {
/* 55 */         Vec3d vec = new Vec3d(x + Math.sin(i * Math.PI / 180.0D) * ((Double)this.radius.getValue()).doubleValue(), y + 0.1D, z + Math.cos(i * Math.PI / 180.0D) * ((Double)this.radius.getValue()).doubleValue());
/* 56 */         RayTraceResult result = mc.field_71441_e.func_147447_a(new Vec3d(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + mc.field_71439_g.func_70047_e(), mc.field_71439_g.field_70161_v), vec, false, false, true);
/* 57 */         if (result != null && ((Boolean)this.raytrace.getValue()).booleanValue()) {
/* 58 */           Phobos.LOGGER.info("raytrace was not null");
/* 59 */           hVectors.add(result.field_72307_f);
/*    */         } else {
/*    */           
/* 62 */           hVectors.add(vec);
/*    */         } 
/* 64 */       }  for (int j = 0; j < hVectors.size() - 1; j++) {
/* 65 */         GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
/* 66 */         GL11.glVertex3d(((Vec3d)hVectors.get(j)).field_72450_a, ((Vec3d)hVectors.get(j)).field_72448_b, ((Vec3d)hVectors.get(j)).field_72449_c);
/* 67 */         GL11.glVertex3d(((Vec3d)hVectors.get(j + 1)).field_72450_a, ((Vec3d)hVectors.get(j + 1)).field_72448_b, ((Vec3d)hVectors.get(j + 1)).field_72449_c);
/* 68 */         color = new Color(Color.HSBtoRGB(hue += 0.0027777778F, 1.0F, 1.0F));
/*    */       } 
/* 70 */       GL11.glEnd();
/* 71 */       GlStateManager.func_179117_G();
/* 72 */       GlStateManager.func_179097_i();
/* 73 */       GlStateManager.func_179098_w();
/* 74 */       GlStateManager.func_179084_k();
/* 75 */       GlStateManager.func_179121_F();
/*    */     } 
/* 77 */     if (((Boolean)this.hitSpheres.getValue()).booleanValue())
/* 78 */       for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/* 79 */         if (player == null || (player.equals(mc.field_71439_g) && !((Boolean)this.ownSphere.getValue()).booleanValue()))
/*    */           continue; 
/* 81 */         Vec3d interpolated = EntityUtil.interpolateEntity((Entity)player, event.getPartialTicks());
/* 82 */         if (Phobos.friendManager.isFriend(player.func_70005_c_())) {
/* 83 */           GL11.glColor4f(0.15F, 0.15F, 1.0F, 1.0F);
/* 84 */         } else if (mc.field_71439_g.func_70032_d((Entity)player) >= 64.0F) {
/* 85 */           GL11.glColor4f(0.0F, 1.0F, 0.0F, 1.0F);
/*    */         } else {
/* 87 */           GL11.glColor4f(1.0F, mc.field_71439_g.func_70032_d((Entity)player) / 150.0F, 0.0F, 1.0F);
/*    */         } 
/* 89 */         RenderUtil.drawSphere(interpolated.field_72450_a, interpolated.field_72448_b, interpolated.field_72449_c, ((Double)this.radius.getValue()).floatValue(), 20, 15);
/*    */       }  
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\render\Ranges.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */