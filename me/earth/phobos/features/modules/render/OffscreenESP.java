/*     */ package me.earth.phobos.features.modules.render;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.awt.Color;
/*     */ import java.util.Map;
/*     */ import me.earth.phobos.event.events.Render2DEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.client.Colors;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.RenderUtil;
/*     */ import me.earth.phobos.util.Util;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import org.lwjgl.opengl.Display;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class OffscreenESP
/*     */   extends Module
/*     */ {
/*  24 */   private final Setting<Boolean> colorSync = register(new Setting("Sync", Boolean.valueOf(false)));
/*  25 */   private final Setting<Boolean> invisibles = register(new Setting("Invisibles", Boolean.valueOf(false)));
/*  26 */   private final Setting<Boolean> offscreenOnly = register(new Setting("Offscreen-Only", Boolean.valueOf(true)));
/*  27 */   private final Setting<Boolean> outline = register(new Setting("Outline", Boolean.valueOf(true)));
/*  28 */   private final Setting<Float> outlineWidth = register(new Setting("Outline-Width", Float.valueOf(1.0F), Float.valueOf(0.1F), Float.valueOf(3.0F)));
/*  29 */   private final Setting<Integer> fadeDistance = register(new Setting("Fade-Distance", Integer.valueOf(100), Integer.valueOf(10), Integer.valueOf(200)));
/*  30 */   private final Setting<Integer> radius = register(new Setting("Radius", Integer.valueOf(45), Integer.valueOf(10), Integer.valueOf(200)));
/*  31 */   private final Setting<Float> size = register(new Setting("Size", Float.valueOf(10.0F), Float.valueOf(5.0F), Float.valueOf(25.0F)));
/*  32 */   private final Setting<Integer> red = register(new Setting("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/*  33 */   private final Setting<Integer> green = register(new Setting("Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255)));
/*  34 */   private final Setting<Integer> blue = register(new Setting("Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/*  35 */   private final EntityListener entityListener = new EntityListener();
/*     */   
/*     */   public OffscreenESP() {
/*  38 */     super("ArrowESP", "Shows the direction players are in with cool little triangles :3", Module.Category.RENDER, true, true, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender2D(Render2DEvent event) {
/*  43 */     this.entityListener.render();
/*  44 */     mc.field_71441_e.field_72996_f.forEach(o -> {
/*     */           if (o instanceof EntityPlayer && isValid((EntityPlayer)o)) {
/*     */             EntityPlayer entity = (EntityPlayer)o;
/*     */             Vec3d pos = this.entityListener.getEntityLowerBounds().get(entity);
/*     */             if (pos != null && !isOnScreen(pos) && (!RenderUtil.isInViewFrustrum((Entity)entity) || !((Boolean)this.offscreenOnly.getValue()).booleanValue())) {
/*     */               Color color = ((Boolean)this.colorSync.getValue()).booleanValue() ? new Color(Colors.INSTANCE.getCurrentColor().getRed(), Colors.INSTANCE.getCurrentColor().getGreen(), Colors.INSTANCE.getCurrentColor().getBlue(), (int)MathHelper.func_76131_a(255.0F - 255.0F / ((Integer)this.fadeDistance.getValue()).intValue() * mc.field_71439_g.func_70032_d((Entity)entity), 100.0F, 255.0F)) : EntityUtil.getColor((Entity)entity, ((Integer)this.red.getValue()).intValue(), ((Integer)this.green.getValue()).intValue(), ((Integer)this.blue.getValue()).intValue(), (int)MathHelper.func_76131_a(255.0F - 255.0F / ((Integer)this.fadeDistance.getValue()).intValue() * mc.field_71439_g.func_70032_d((Entity)entity), 100.0F, 255.0F), true);
/*     */               int x = Display.getWidth() / 2 / ((mc.field_71474_y.field_74335_Z == 0) ? 1 : mc.field_71474_y.field_74335_Z);
/*     */               int y = Display.getHeight() / 2 / ((mc.field_71474_y.field_74335_Z == 0) ? 1 : mc.field_71474_y.field_74335_Z);
/*     */               float yaw = getRotations((EntityLivingBase)entity) - mc.field_71439_g.field_70177_z;
/*     */               GL11.glTranslatef(x, y, 0.0F);
/*     */               GL11.glRotatef(yaw, 0.0F, 0.0F, 1.0F);
/*     */               GL11.glTranslatef(-x, -y, 0.0F);
/*     */               RenderUtil.drawTracerPointer(x, (y - ((Integer)this.radius.getValue()).intValue()), ((Float)this.size.getValue()).floatValue(), 2.0F, 1.0F, ((Boolean)this.outline.getValue()).booleanValue(), ((Float)this.outlineWidth.getValue()).floatValue(), color.getRGB());
/*     */               GL11.glTranslatef(x, y, 0.0F);
/*     */               GL11.glRotatef(-yaw, 0.0F, 0.0F, 1.0F);
/*     */               GL11.glTranslatef(-x, -y, 0.0F);
/*     */             } 
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isOnScreen(Vec3d pos) {
/*  70 */     if (pos.field_72450_a <= -1.0D) return false; 
/*  71 */     if (pos.field_72448_b >= 1.0D) return false; 
/*  72 */     int n = (mc.field_71474_y.field_74335_Z == 0) ? 1 : mc.field_71474_y.field_74335_Z;
/*  73 */     if (pos.field_72450_a / n < 0.0D) return false; 
/*  74 */     int n2 = (mc.field_71474_y.field_74335_Z == 0) ? 1 : mc.field_71474_y.field_74335_Z;
/*  75 */     if (pos.field_72450_a / n2 > Display.getWidth()) return false; 
/*  76 */     int n3 = (mc.field_71474_y.field_74335_Z == 0) ? 1 : mc.field_71474_y.field_74335_Z;
/*  77 */     if (pos.field_72448_b / n3 < 0.0D) return false; 
/*  78 */     int n4 = (mc.field_71474_y.field_74335_Z == 0) ? 1 : mc.field_71474_y.field_74335_Z;
/*  79 */     return (pos.field_72448_b / n4 <= Display.getHeight());
/*     */   }
/*     */   
/*     */   private boolean isValid(EntityPlayer entity) {
/*  83 */     return (entity != mc.field_71439_g && (!entity.func_82150_aj() || ((Boolean)this.invisibles.getValue()).booleanValue()) && entity.func_70089_S());
/*     */   }
/*     */   
/*     */   private float getRotations(EntityLivingBase ent) {
/*  87 */     double x = ent.field_70165_t - mc.field_71439_g.field_70165_t;
/*  88 */     double z = ent.field_70161_v - mc.field_71439_g.field_70161_v;
/*  89 */     return (float)-(Math.atan2(x, z) * 57.29577951308232D);
/*     */   }
/*     */   
/*     */   private static class EntityListener {
/*  93 */     private final Map<Entity, Vec3d> entityUpperBounds = Maps.newHashMap();
/*  94 */     private final Map<Entity, Vec3d> entityLowerBounds = Maps.newHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void render() {
/* 100 */       if (!this.entityUpperBounds.isEmpty()) {
/* 101 */         this.entityUpperBounds.clear();
/*     */       }
/* 103 */       if (!this.entityLowerBounds.isEmpty()) {
/* 104 */         this.entityLowerBounds.clear();
/*     */       }
/* 106 */       for (Entity e : Util.mc.field_71441_e.field_72996_f) {
/* 107 */         Vec3d bound = getEntityRenderPosition(e);
/* 108 */         bound.func_178787_e(new Vec3d(0.0D, e.field_70131_O + 0.2D, 0.0D));
/* 109 */         Vec3d upperBounds = RenderUtil.to2D(bound.field_72450_a, bound.field_72448_b, bound.field_72449_c);
/* 110 */         Vec3d lowerBounds = RenderUtil.to2D(bound.field_72450_a, bound.field_72448_b - 2.0D, bound.field_72449_c);
/* 111 */         if (upperBounds == null || lowerBounds == null)
/* 112 */           continue;  this.entityUpperBounds.put(e, upperBounds);
/* 113 */         this.entityLowerBounds.put(e, lowerBounds);
/*     */       } 
/*     */     }
/*     */     
/*     */     private Vec3d getEntityRenderPosition(Entity entity) {
/* 118 */       double partial = Util.mc.field_71428_T.field_194147_b;
/* 119 */       double x = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * partial - (Util.mc.func_175598_ae()).field_78730_l;
/* 120 */       double y = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * partial - (Util.mc.func_175598_ae()).field_78731_m;
/* 121 */       double z = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * partial - (Util.mc.func_175598_ae()).field_78728_n;
/* 122 */       return new Vec3d(x, y, z);
/*     */     }
/*     */     
/*     */     public Map<Entity, Vec3d> getEntityLowerBounds() {
/* 126 */       return this.entityLowerBounds;
/*     */     }
/*     */     
/*     */     private EntityListener() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\render\OffscreenESP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */