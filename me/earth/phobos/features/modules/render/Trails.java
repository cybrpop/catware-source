/*    */ package me.earth.phobos.features.modules.render;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import me.earth.phobos.event.events.Render3DEvent;
/*    */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.RenderUtil;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Trails
/*    */   extends Module
/*    */ {
/* 23 */   private final Setting<Float> lineWidth = register(new Setting("LineWidth", Float.valueOf(1.5F), Float.valueOf(0.1F), Float.valueOf(5.0F)));
/* 24 */   private final Setting<Integer> red = register(new Setting("Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255)));
/* 25 */   private final Setting<Integer> green = register(new Setting("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/* 26 */   private final Setting<Integer> blue = register(new Setting("Blue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255)));
/* 27 */   private final Setting<Integer> alpha = register(new Setting("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/* 28 */   private final Map<Entity, List<Vec3d>> renderMap = new HashMap<>();
/*    */   
/*    */   public Trails() {
/* 31 */     super("Trails", "Draws trails on projectiles", Module.Category.RENDER, true, false, false);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/* 36 */     for (Entity entity : mc.field_71441_e.field_72996_f) {
/* 37 */       if (!(entity instanceof net.minecraft.entity.projectile.EntityThrowable) && !(entity instanceof net.minecraft.entity.projectile.EntityArrow))
/* 38 */         continue;  List<Vec3d> vectors = (this.renderMap.get(entity) != null) ? this.renderMap.get(entity) : new ArrayList<>();
/* 39 */       vectors.add(new Vec3d(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v));
/* 40 */       this.renderMap.put(entity, vectors);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onRender3D(Render3DEvent event) {
/* 46 */     for (Entity entity : mc.field_71441_e.field_72996_f) {
/* 47 */       if (!this.renderMap.containsKey(entity))
/* 48 */         continue;  GlStateManager.func_179094_E();
/* 49 */       RenderUtil.GLPre(((Float)this.lineWidth.getValue()).floatValue());
/* 50 */       GlStateManager.func_179147_l();
/* 51 */       GlStateManager.func_179090_x();
/* 52 */       GlStateManager.func_179132_a(false);
/* 53 */       GlStateManager.func_179097_i();
/* 54 */       GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
/* 55 */       GL11.glColor4f(((Integer)this.red.getValue()).intValue() / 255.0F, ((Integer)this.green.getValue()).intValue() / 255.0F, ((Integer)this.blue.getValue()).intValue() / 255.0F, ((Integer)this.alpha.getValue()).intValue() / 255.0F);
/* 56 */       GL11.glLineWidth(((Float)this.lineWidth.getValue()).floatValue());
/* 57 */       GL11.glBegin(1);
/* 58 */       for (int i = 0; i < ((List)this.renderMap.get(entity)).size() - 1; i++) {
/* 59 */         GL11.glVertex3d(((Vec3d)((List)this.renderMap.get(entity)).get(i)).field_72450_a, ((Vec3d)((List)this.renderMap.get(entity)).get(i)).field_72448_b, ((Vec3d)((List)this.renderMap.get(entity)).get(i)).field_72449_c);
/* 60 */         GL11.glVertex3d(((Vec3d)((List)this.renderMap.get(entity)).get(i + 1)).field_72450_a, ((Vec3d)((List)this.renderMap.get(entity)).get(i + 1)).field_72448_b, ((Vec3d)((List)this.renderMap.get(entity)).get(i + 1)).field_72449_c);
/*    */       } 
/* 62 */       GL11.glEnd();
/* 63 */       GlStateManager.func_179117_G();
/* 64 */       GlStateManager.func_179126_j();
/* 65 */       GlStateManager.func_179132_a(true);
/* 66 */       GlStateManager.func_179098_w();
/* 67 */       GlStateManager.func_179084_k();
/* 68 */       RenderUtil.GlPost();
/* 69 */       GlStateManager.func_179121_F();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\render\Trails.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */