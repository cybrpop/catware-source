/*     */ package me.earth.phobos.util;
/*     */ 
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.ActiveRenderInfo;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import org.lwjgl.util.vector.Matrix4f;
/*     */ import org.lwjgl.util.vector.Vector4f;
/*     */ 
/*     */ 
/*     */ public class VectorUtils
/*     */   implements Util
/*     */ {
/*  14 */   static Matrix4f modelMatrix = new Matrix4f();
/*  15 */   static Matrix4f projectionMatrix = new Matrix4f();
/*     */   
/*     */   private static void VecTransformCoordinate(Vector4f vec, Matrix4f matrix) {
/*  18 */     float x = vec.x;
/*  19 */     float y = vec.y;
/*  20 */     float z = vec.z;
/*  21 */     vec.x = x * matrix.m00 + y * matrix.m10 + z * matrix.m20 + matrix.m30;
/*  22 */     vec.y = x * matrix.m01 + y * matrix.m11 + z * matrix.m21 + matrix.m31;
/*  23 */     vec.z = x * matrix.m02 + y * matrix.m12 + z * matrix.m22 + matrix.m32;
/*  24 */     vec.w = x * matrix.m03 + y * matrix.m13 + z * matrix.m23 + matrix.m33;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Plane toScreen(double x, double y, double z) {
/*  31 */     Entity view = mc.func_175606_aa();
/*     */     
/*  33 */     if (view == null) {
/*  34 */       return new Plane(0.0D, 0.0D, false);
/*     */     }
/*     */ 
/*     */     
/*  38 */     Vec3d camPos = ActiveRenderInfo.field_178811_e;
/*  39 */     Vec3d eyePos = ActiveRenderInfo.func_178806_a(view, mc.func_184121_ak());
/*     */     
/*  41 */     float vecX = (float)(camPos.field_72450_a + eyePos.field_72450_a - (float)x);
/*  42 */     float vecY = (float)(camPos.field_72448_b + eyePos.field_72448_b - (float)y);
/*  43 */     float vecZ = (float)(camPos.field_72449_c + eyePos.field_72449_c - (float)z);
/*     */     
/*  45 */     Vector4f pos = new Vector4f(vecX, vecY, vecZ, 1.0F);
/*     */     
/*  47 */     modelMatrix.load(ActiveRenderInfo.field_178812_b
/*     */         
/*  49 */         .asReadOnlyBuffer());
/*  50 */     projectionMatrix.load(ActiveRenderInfo.field_178813_c
/*     */         
/*  52 */         .asReadOnlyBuffer());
/*     */     
/*  54 */     VecTransformCoordinate(pos, modelMatrix);
/*  55 */     VecTransformCoordinate(pos, projectionMatrix);
/*     */     
/*  57 */     if (pos.w > 0.0F) {
/*  58 */       pos.x *= -100000.0F;
/*  59 */       pos.y *= -100000.0F;
/*     */     } else {
/*  61 */       float invert = 1.0F / pos.w;
/*  62 */       pos.x *= invert;
/*  63 */       pos.y *= invert;
/*     */     } 
/*     */     
/*  66 */     ScaledResolution res = new ScaledResolution(mc);
/*  67 */     float halfWidth = res.func_78326_a() / 2.0F;
/*  68 */     float halfHeight = res.func_78328_b() / 2.0F;
/*     */     
/*  70 */     pos.x = halfWidth + 0.5F * pos.x * res.func_78326_a() + 0.5F;
/*  71 */     pos.y = halfHeight - 0.5F * pos.y * res.func_78328_b() + 0.5F;
/*     */     
/*  73 */     boolean bVisible = true;
/*     */     
/*  75 */     if (pos.x < 0.0F || pos.y < 0.0F || pos.x > res.func_78326_a() || pos.y > res.func_78328_b()) {
/*  76 */       bVisible = false;
/*     */     }
/*     */     
/*  79 */     return new Plane(pos.x, pos.y, bVisible);
/*     */   }
/*     */   
/*     */   public static Plane toScreen(Vec3d vec) {
/*  83 */     return toScreen(vec.field_72450_a, vec.field_72448_b, vec.field_72449_c);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static ScreenPos _toScreen(double x, double y, double z) {
/*  88 */     Plane plane = toScreen(x, y, z);
/*  89 */     return new ScreenPos(plane.getX(), plane.getY(), plane.isVisible());
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static ScreenPos _toScreen(Vec3d vec3d) {
/*  94 */     return _toScreen(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static Object vectorAngle(Vec3d vec3d) {
/* 102 */     return null;
/*     */   }
/*     */   
/*     */   public static Vec3d multiplyBy(Vec3d vec1, Vec3d vec2) {
/* 106 */     return new Vec3d(vec1.field_72450_a * vec2.field_72450_a, vec1.field_72448_b * vec2.field_72448_b, vec1.field_72449_c * vec2.field_72449_c);
/*     */   }
/*     */   
/*     */   public static Vec3d copy(Vec3d toCopy) {
/* 110 */     return new Vec3d(toCopy.field_72450_a, toCopy.field_72448_b, toCopy.field_72449_c);
/*     */   }
/*     */   
/*     */   public static double getCrosshairDistance(Vec3d eyes, Vec3d directionVec, Vec3d pos) {
/* 114 */     return pos.func_178788_d(eyes).func_72432_b().func_178788_d(directionVec).func_189985_c();
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static class ScreenPos
/*     */   {
/*     */     public final int x;
/*     */     public final int y;
/*     */     public final boolean isVisible;
/*     */     public final double xD;
/*     */     public final double yD;
/*     */     
/*     */     public ScreenPos(double x, double y, boolean isVisible) {
/* 128 */       this.x = (int)x;
/* 129 */       this.y = (int)y;
/* 130 */       this.xD = x;
/* 131 */       this.yD = y;
/* 132 */       this.isVisible = isVisible;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobo\\util\VectorUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */