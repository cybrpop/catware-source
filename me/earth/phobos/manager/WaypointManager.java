/*    */ package me.earth.phobos.manager;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import me.earth.phobos.features.Feature;
/*    */ import me.earth.phobos.util.RenderUtil;
/*    */ import me.earth.phobos.util.Util;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ 
/*    */ public class WaypointManager
/*    */   extends Feature
/*    */ {
/* 17 */   public static final ResourceLocation WAYPOINT_RESOURCE = new ResourceLocation("textures/waypoint.png");
/* 18 */   public Map<String, Waypoint> waypoints = new HashMap<>();
/*    */   
/*    */   public static class Waypoint {
/*    */     public String owner;
/*    */     public String server;
/*    */     public int dimension;
/*    */     public int x;
/*    */     public int y;
/*    */     public int z;
/*    */     public int red;
/*    */     public int green;
/*    */     public int blue;
/*    */     public int alpha;
/*    */     
/*    */     public Waypoint(String owner, String server, int dimension, int x, int y, int z, Color color) {
/* 33 */       this.owner = owner;
/* 34 */       this.server = server;
/* 35 */       this.dimension = dimension;
/* 36 */       this.x = x;
/* 37 */       this.y = y;
/* 38 */       this.z = z;
/* 39 */       this.red = color.getRed();
/* 40 */       this.green = color.getGreen();
/* 41 */       this.blue = color.getBlue();
/* 42 */       this.alpha = color.getAlpha();
/*    */     }
/*    */     
/*    */     public void renderBox() {
/* 46 */       GL11.glPushMatrix();
/* 47 */       GL11.glDisable(2896);
/* 48 */       GL11.glDisable(2929);
/* 49 */       GL11.glEnable(3042);
/* 50 */       GL11.glBlendFunc(770, 771);
/* 51 */       GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 52 */       RenderUtil.drawBlockOutline(new BlockPos(this.x, this.y, this.z), new Color(this.red, this.green, this.blue, this.alpha), 1.0F, true);
/* 53 */       GlStateManager.func_179098_w();
/* 54 */       GL11.glDisable(3042);
/* 55 */       GL11.glEnable(2896);
/* 56 */       GL11.glEnable(2929);
/* 57 */       GL11.glPopMatrix();
/*    */     }
/*    */     
/*    */     public void render() {
/* 61 */       double posX = this.x - (Util.mc.func_175598_ae()).field_78725_b + 0.5D;
/* 62 */       double posY = this.y - (Util.mc.func_175598_ae()).field_78726_c - 0.5D;
/* 63 */       double posZ = this.z - (Util.mc.func_175598_ae()).field_78723_d + 0.5D;
/* 64 */       float scale = (float)(Util.mc.field_71439_g.func_70011_f(posX + (Util.mc.func_175598_ae()).field_78725_b, posY + (Util.mc.func_175598_ae()).field_78726_c, posZ + (Util.mc.func_175598_ae()).field_78723_d) / 4.0D);
/* 65 */       if (scale < 1.6F) {
/* 66 */         scale = 1.6F;
/*    */       }
/* 68 */       GL11.glPushMatrix();
/* 69 */       GL11.glTranslatef((float)posX, (float)posY + 1.4F, (float)posZ);
/* 70 */       GL11.glNormal3f(0.0F, 1.0F, 0.0F);
/* 71 */       GL11.glRotatef(-(Util.mc.func_175598_ae()).field_78735_i, 0.0F, 1.0F, 0.0F);
/* 72 */       GL11.glRotatef((Util.mc.func_175598_ae()).field_78732_j, 1.0F, 0.0F, 0.0F);
/* 73 */       GL11.glScalef(-(scale /= 25.0F), -scale, scale);
/* 74 */       GL11.glDisable(2896);
/* 75 */       GL11.glDisable(2929);
/* 76 */       GL11.glEnable(3042);
/* 77 */       GL11.glBlendFunc(770, 771);
/* 78 */       int width = Util.mc.field_71466_p.func_78256_a(this.owner) / 2;
/* 79 */       GL11.glPushMatrix();
/* 80 */       GL11.glPopMatrix();
/* 81 */       GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 82 */       GL11.glScalef(0.5F, 0.5F, 0.5F);
/* 83 */       Util.mc.field_71466_p.func_175063_a(this.owner, -width, -(Util.mc.field_71466_p.field_78288_b + 7), (new Color(this.red, this.green, this.blue, this.alpha)).getRGB());
/* 84 */       GL11.glScalef(1.0F, 1.0F, 1.0F);
/* 85 */       GlStateManager.func_179098_w();
/* 86 */       GL11.glDisable(3042);
/* 87 */       GL11.glEnable(2896);
/* 88 */       GL11.glEnable(2929);
/* 89 */       GL11.glPopMatrix();
/* 90 */       GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\manager\WaypointManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */