/*      */ package me.earth.phobos.util;
/*      */ import java.awt.Color;
/*      */ import java.nio.FloatBuffer;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.Objects;
/*      */ import me.earth.phobos.Phobos;
/*      */ import net.minecraft.block.material.Material;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.gui.ScaledResolution;
/*      */ import net.minecraft.client.model.ModelBiped;
/*      */ import net.minecraft.client.renderer.BufferBuilder;
/*      */ import net.minecraft.client.renderer.GlStateManager;
/*      */ import net.minecraft.client.renderer.OpenGlHelper;
/*      */ import net.minecraft.client.renderer.RenderGlobal;
/*      */ import net.minecraft.client.renderer.Tessellator;
/*      */ import net.minecraft.client.renderer.culling.Frustum;
/*      */ import net.minecraft.client.renderer.culling.ICamera;
/*      */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*      */ import net.minecraft.client.shader.Framebuffer;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.util.EnumFacing;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import net.minecraft.util.math.AxisAlignedBB;
/*      */ import net.minecraft.util.math.BlockPos;
/*      */ import net.minecraft.util.math.Vec2f;
/*      */ import net.minecraft.util.math.Vec3d;
/*      */ import net.minecraft.world.World;
/*      */ import org.lwjgl.BufferUtils;
/*      */ import org.lwjgl.opengl.EXTFramebufferObject;
/*      */ import org.lwjgl.opengl.GL11;
/*      */ import org.lwjgl.util.glu.Disk;
/*      */ import org.lwjgl.util.glu.Sphere;
/*      */ 
/*      */ public class RenderUtil implements Util {
/*   40 */   private static final Frustum frustrum = new Frustum();
/*   41 */   private static final FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
/*   42 */   private static final IntBuffer viewport = BufferUtils.createIntBuffer(16);
/*   43 */   private static final FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
/*   44 */   private static final FloatBuffer projection = BufferUtils.createFloatBuffer(16);
/*   45 */   public static RenderItem itemRender = mc.func_175599_af();
/*   46 */   public static ICamera camera = (ICamera)new Frustum();
/*   47 */   private static boolean depth = GL11.glIsEnabled(2896);
/*   48 */   private static boolean texture = GL11.glIsEnabled(3042);
/*   49 */   private static boolean clean = GL11.glIsEnabled(3553);
/*   50 */   private static boolean bind = GL11.glIsEnabled(2929);
/*   51 */   private static boolean override = GL11.glIsEnabled(2848);
/*      */   
/*      */   public static void updateModelViewProjectionMatrix() {
/*   54 */     GL11.glGetFloat(2982, modelView);
/*   55 */     GL11.glGetFloat(2983, projection);
/*   56 */     GL11.glGetInteger(2978, viewport);
/*   57 */     ScaledResolution res = new ScaledResolution(Minecraft.func_71410_x());
/*   58 */     GLUProjection.getInstance().updateMatrices(viewport, modelView, projection, (res.func_78326_a() / (Minecraft.func_71410_x()).field_71443_c), (res.func_78328_b() / (Minecraft.func_71410_x()).field_71440_d));
/*      */   }
/*      */   
/*      */   public static void drawRectangleCorrectly(int x, int y, int w, int h, int color) {
/*   62 */     GL11.glLineWidth(1.0F);
/*   63 */     Gui.func_73734_a(x, y, x + w, y + h, color);
/*      */   }
/*      */   
/*      */   public static void drawWaypointImage(BlockPos pos, GLUProjection.Projection projection, Color color, String name, boolean rectangle, Color rectangleColor) {
/*   67 */     GlStateManager.func_179094_E();
/*   68 */     GlStateManager.func_179137_b(projection.getX(), projection.getY(), 0.0D);
/*   69 */     String text = name + ": " + Math.round(mc.field_71439_g.func_70011_f(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p())) + "M";
/*   70 */     float textWidth = Phobos.textManager.getStringWidth(text);
/*   71 */     Phobos.textManager.drawString(text, -(textWidth / 2.0F), -(Phobos.textManager.getFontHeight() / 2.0F) + Phobos.textManager.getFontHeight() / 2.0F, color.getRGB(), false);
/*   72 */     GlStateManager.func_179137_b(-projection.getX(), -projection.getY(), 0.0D);
/*   73 */     GlStateManager.func_179121_F();
/*      */   }
/*      */   
/*      */   public static AxisAlignedBB interpolateAxis(AxisAlignedBB bb) {
/*   77 */     return new AxisAlignedBB(bb.field_72340_a - (mc.func_175598_ae()).field_78730_l, bb.field_72338_b - (mc.func_175598_ae()).field_78731_m, bb.field_72339_c - (mc.func_175598_ae()).field_78728_n, bb.field_72336_d - (mc.func_175598_ae()).field_78730_l, bb.field_72337_e - (mc.func_175598_ae()).field_78731_m, bb.field_72334_f - (mc.func_175598_ae()).field_78728_n);
/*      */   }
/*      */   
/*      */   public static void drawTexturedRect(int x, int y, int textureX, int textureY, int width, int height, int zLevel) {
/*   81 */     Tessellator tessellator = Tessellator.func_178181_a();
/*   82 */     BufferBuilder BufferBuilder2 = tessellator.func_178180_c();
/*   83 */     BufferBuilder2.func_181668_a(7, DefaultVertexFormats.field_181707_g);
/*   84 */     BufferBuilder2.func_181662_b((x + 0), (y + height), zLevel).func_187315_a(((textureX + 0) * 0.00390625F), ((textureY + height) * 0.00390625F)).func_181675_d();
/*   85 */     BufferBuilder2.func_181662_b((x + width), (y + height), zLevel).func_187315_a(((textureX + width) * 0.00390625F), ((textureY + height) * 0.00390625F)).func_181675_d();
/*   86 */     BufferBuilder2.func_181662_b((x + width), (y + 0), zLevel).func_187315_a(((textureX + width) * 0.00390625F), ((textureY + 0) * 0.00390625F)).func_181675_d();
/*   87 */     BufferBuilder2.func_181662_b((x + 0), (y + 0), zLevel).func_187315_a(((textureX + 0) * 0.00390625F), ((textureY + 0) * 0.00390625F)).func_181675_d();
/*   88 */     tessellator.func_78381_a();
/*      */   }
/*      */   
/*      */   public static void drawOpenGradientBox(BlockPos pos, Color startColor, Color endColor, double height) {
/*   92 */     for (EnumFacing face : EnumFacing.values()) {
/*   93 */       if (face != EnumFacing.UP)
/*   94 */         drawGradientPlane(pos, face, startColor, endColor, height); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void drawClosedGradientBox(BlockPos pos, Color startColor, Color endColor, double height) {
/*   99 */     for (EnumFacing face : EnumFacing.values()) {
/*  100 */       drawGradientPlane(pos, face, startColor, endColor, height);
/*      */     }
/*      */   }
/*      */   
/*      */   public static void drawTricolorGradientBox(BlockPos pos, Color startColor, Color midColor, Color endColor) {
/*  105 */     for (EnumFacing face : EnumFacing.values()) {
/*  106 */       if (face != EnumFacing.UP)
/*  107 */         drawGradientPlane(pos, face, startColor, midColor, true, false); 
/*      */     } 
/*  109 */     for (EnumFacing face : EnumFacing.values()) {
/*  110 */       if (face != EnumFacing.DOWN)
/*  111 */         drawGradientPlane(pos, face, midColor, endColor, true, true); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void drawGradientPlane(BlockPos pos, EnumFacing face, Color startColor, Color endColor, boolean half, boolean top) {
/*  116 */     Tessellator tessellator = Tessellator.func_178181_a();
/*  117 */     BufferBuilder builder = tessellator.func_178180_c();
/*  118 */     IBlockState iblockstate = mc.field_71441_e.func_180495_p(pos);
/*  119 */     Vec3d interp = EntityUtil.interpolateEntity((Entity)mc.field_71439_g, mc.func_184121_ak());
/*  120 */     AxisAlignedBB bb = iblockstate.func_185918_c((World)mc.field_71441_e, pos).func_186662_g(0.0020000000949949026D).func_72317_d(-interp.field_72450_a, -interp.field_72448_b, -interp.field_72449_c);
/*  121 */     float red = startColor.getRed() / 255.0F;
/*  122 */     float green = startColor.getGreen() / 255.0F;
/*  123 */     float blue = startColor.getBlue() / 255.0F;
/*  124 */     float alpha = startColor.getAlpha() / 255.0F;
/*  125 */     float red1 = endColor.getRed() / 255.0F;
/*  126 */     float green1 = endColor.getGreen() / 255.0F;
/*  127 */     float blue1 = endColor.getBlue() / 255.0F;
/*  128 */     float alpha1 = endColor.getAlpha() / 255.0F;
/*  129 */     double x1 = 0.0D;
/*  130 */     double y1 = 0.0D;
/*  131 */     double z1 = 0.0D;
/*  132 */     double x2 = 0.0D;
/*  133 */     double y2 = 0.0D;
/*  134 */     double z2 = 0.0D;
/*  135 */     if (face == EnumFacing.DOWN) {
/*  136 */       x1 = bb.field_72340_a;
/*  137 */       x2 = bb.field_72336_d;
/*  138 */       y1 = bb.field_72338_b + (top ? 0.5D : 0.0D);
/*  139 */       y2 = bb.field_72338_b + (top ? 0.5D : 0.0D);
/*  140 */       z1 = bb.field_72339_c;
/*  141 */       z2 = bb.field_72334_f;
/*  142 */     } else if (face == EnumFacing.UP) {
/*  143 */       x1 = bb.field_72340_a;
/*  144 */       x2 = bb.field_72336_d;
/*  145 */       y1 = bb.field_72337_e / (half ? 2 : true);
/*  146 */       y2 = bb.field_72337_e / (half ? 2 : true);
/*  147 */       z1 = bb.field_72339_c;
/*  148 */       z2 = bb.field_72334_f;
/*  149 */     } else if (face == EnumFacing.EAST) {
/*  150 */       x1 = bb.field_72336_d;
/*  151 */       x2 = bb.field_72336_d;
/*  152 */       y1 = bb.field_72338_b + (top ? 0.5D : 0.0D);
/*  153 */       y2 = bb.field_72337_e / (half ? 2 : true);
/*  154 */       z1 = bb.field_72339_c;
/*  155 */       z2 = bb.field_72334_f;
/*  156 */     } else if (face == EnumFacing.WEST) {
/*  157 */       x1 = bb.field_72340_a;
/*  158 */       x2 = bb.field_72340_a;
/*  159 */       y1 = bb.field_72338_b + (top ? 0.5D : 0.0D);
/*  160 */       y2 = bb.field_72337_e / (half ? 2 : true);
/*  161 */       z1 = bb.field_72339_c;
/*  162 */       z2 = bb.field_72334_f;
/*  163 */     } else if (face == EnumFacing.SOUTH) {
/*  164 */       x1 = bb.field_72340_a;
/*  165 */       x2 = bb.field_72336_d;
/*  166 */       y1 = bb.field_72338_b + (top ? 0.5D : 0.0D);
/*  167 */       y2 = bb.field_72337_e / (half ? 2 : true);
/*  168 */       z1 = bb.field_72334_f;
/*  169 */       z2 = bb.field_72334_f;
/*  170 */     } else if (face == EnumFacing.NORTH) {
/*  171 */       x1 = bb.field_72340_a;
/*  172 */       x2 = bb.field_72336_d;
/*  173 */       y1 = bb.field_72338_b + (top ? 0.5D : 0.0D);
/*  174 */       y2 = bb.field_72337_e / (half ? 2 : true);
/*  175 */       z1 = bb.field_72339_c;
/*  176 */       z2 = bb.field_72339_c;
/*      */     } 
/*  178 */     GlStateManager.func_179094_E();
/*  179 */     GlStateManager.func_179097_i();
/*  180 */     GlStateManager.func_179090_x();
/*  181 */     GlStateManager.func_179147_l();
/*  182 */     GlStateManager.func_179118_c();
/*  183 */     GlStateManager.func_179132_a(false);
/*  184 */     builder.func_181668_a(5, DefaultVertexFormats.field_181706_f);
/*  185 */     if (face == EnumFacing.EAST || face == EnumFacing.WEST || face == EnumFacing.NORTH || face == EnumFacing.SOUTH) {
/*  186 */       builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  187 */       builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  188 */       builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  189 */       builder.func_181662_b(x1, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  190 */       builder.func_181662_b(x1, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  191 */       builder.func_181662_b(x1, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  192 */       builder.func_181662_b(x1, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  193 */       builder.func_181662_b(x1, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  194 */       builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  195 */       builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  196 */       builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  197 */       builder.func_181662_b(x2, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  198 */       builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  199 */       builder.func_181662_b(x2, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  200 */       builder.func_181662_b(x2, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  201 */       builder.func_181662_b(x2, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  202 */       builder.func_181662_b(x1, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  203 */       builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  204 */       builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  205 */       builder.func_181662_b(x2, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  206 */       builder.func_181662_b(x1, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  207 */       builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  208 */       builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  209 */       builder.func_181662_b(x1, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  210 */       builder.func_181662_b(x1, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  211 */       builder.func_181662_b(x1, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  212 */       builder.func_181662_b(x2, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  213 */       builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  214 */       builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  215 */       builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  216 */     } else if (face == EnumFacing.UP) {
/*  217 */       builder.func_181662_b(x1, y1, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  218 */       builder.func_181662_b(x1, y1, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  219 */       builder.func_181662_b(x1, y1, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  220 */       builder.func_181662_b(x1, y1, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  221 */       builder.func_181662_b(x1, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  222 */       builder.func_181662_b(x1, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  223 */       builder.func_181662_b(x1, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  224 */       builder.func_181662_b(x1, y1, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  225 */       builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  226 */       builder.func_181662_b(x2, y1, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  227 */       builder.func_181662_b(x2, y1, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  228 */       builder.func_181662_b(x2, y1, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  229 */       builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  230 */       builder.func_181662_b(x2, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  231 */       builder.func_181662_b(x2, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  232 */       builder.func_181662_b(x2, y1, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  233 */       builder.func_181662_b(x1, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  234 */       builder.func_181662_b(x1, y1, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  235 */       builder.func_181662_b(x1, y1, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  236 */       builder.func_181662_b(x2, y1, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  237 */       builder.func_181662_b(x1, y1, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  238 */       builder.func_181662_b(x2, y1, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  239 */       builder.func_181662_b(x2, y1, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  240 */       builder.func_181662_b(x1, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  241 */       builder.func_181662_b(x1, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  242 */       builder.func_181662_b(x1, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  243 */       builder.func_181662_b(x2, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  244 */       builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  245 */       builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  246 */       builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  247 */     } else if (face == EnumFacing.DOWN) {
/*  248 */       builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  249 */       builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  250 */       builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  251 */       builder.func_181662_b(x1, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  252 */       builder.func_181662_b(x1, y2, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  253 */       builder.func_181662_b(x1, y2, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  254 */       builder.func_181662_b(x1, y2, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  255 */       builder.func_181662_b(x1, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  256 */       builder.func_181662_b(x2, y2, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  257 */       builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  258 */       builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  259 */       builder.func_181662_b(x2, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  260 */       builder.func_181662_b(x2, y2, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  261 */       builder.func_181662_b(x2, y2, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  262 */       builder.func_181662_b(x2, y2, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  263 */       builder.func_181662_b(x2, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  264 */       builder.func_181662_b(x1, y2, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  265 */       builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  266 */       builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  267 */       builder.func_181662_b(x2, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  268 */       builder.func_181662_b(x1, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  269 */       builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  270 */       builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  271 */       builder.func_181662_b(x1, y2, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  272 */       builder.func_181662_b(x1, y2, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  273 */       builder.func_181662_b(x1, y2, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  274 */       builder.func_181662_b(x2, y2, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  275 */       builder.func_181662_b(x2, y2, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  276 */       builder.func_181662_b(x2, y2, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  277 */       builder.func_181662_b(x2, y2, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*      */     } 
/*  279 */     tessellator.func_78381_a();
/*  280 */     GlStateManager.func_179132_a(true);
/*  281 */     GlStateManager.func_179084_k();
/*  282 */     GlStateManager.func_179141_d();
/*  283 */     GlStateManager.func_179098_w();
/*  284 */     GlStateManager.func_179126_j();
/*  285 */     GlStateManager.func_179121_F();
/*      */   }
/*      */   
/*      */   public static void drawGradientPlane(BlockPos pos, EnumFacing face, Color startColor, Color endColor, double height) {
/*  289 */     Tessellator tessellator = Tessellator.func_178181_a();
/*  290 */     BufferBuilder builder = tessellator.func_178180_c();
/*  291 */     IBlockState iblockstate = mc.field_71441_e.func_180495_p(pos);
/*  292 */     Vec3d interp = EntityUtil.interpolateEntity((Entity)mc.field_71439_g, mc.func_184121_ak());
/*  293 */     AxisAlignedBB bb = iblockstate.func_185918_c((World)mc.field_71441_e, pos).func_186662_g(0.0020000000949949026D).func_72317_d(-interp.field_72450_a, -interp.field_72448_b, -interp.field_72449_c).func_72321_a(0.0D, height, 0.0D);
/*  294 */     float red = startColor.getRed() / 255.0F;
/*  295 */     float green = startColor.getGreen() / 255.0F;
/*  296 */     float blue = startColor.getBlue() / 255.0F;
/*  297 */     float alpha = startColor.getAlpha() / 255.0F;
/*  298 */     float red1 = endColor.getRed() / 255.0F;
/*  299 */     float green1 = endColor.getGreen() / 255.0F;
/*  300 */     float blue1 = endColor.getBlue() / 255.0F;
/*  301 */     float alpha1 = endColor.getAlpha() / 255.0F;
/*  302 */     double x1 = 0.0D;
/*  303 */     double y1 = 0.0D;
/*  304 */     double z1 = 0.0D;
/*  305 */     double x2 = 0.0D;
/*  306 */     double y2 = 0.0D;
/*  307 */     double z2 = 0.0D;
/*  308 */     if (face == EnumFacing.DOWN) {
/*  309 */       x1 = bb.field_72340_a;
/*  310 */       x2 = bb.field_72336_d;
/*  311 */       y1 = bb.field_72338_b;
/*  312 */       y2 = bb.field_72338_b;
/*  313 */       z1 = bb.field_72339_c;
/*  314 */       z2 = bb.field_72334_f;
/*  315 */     } else if (face == EnumFacing.UP) {
/*  316 */       x1 = bb.field_72340_a;
/*  317 */       x2 = bb.field_72336_d;
/*  318 */       y1 = bb.field_72337_e;
/*  319 */       y2 = bb.field_72337_e;
/*  320 */       z1 = bb.field_72339_c;
/*  321 */       z2 = bb.field_72334_f;
/*  322 */     } else if (face == EnumFacing.EAST) {
/*  323 */       x1 = bb.field_72336_d;
/*  324 */       x2 = bb.field_72336_d;
/*  325 */       y1 = bb.field_72338_b;
/*  326 */       y2 = bb.field_72337_e;
/*  327 */       z1 = bb.field_72339_c;
/*  328 */       z2 = bb.field_72334_f;
/*  329 */     } else if (face == EnumFacing.WEST) {
/*  330 */       x1 = bb.field_72340_a;
/*  331 */       x2 = bb.field_72340_a;
/*  332 */       y1 = bb.field_72338_b;
/*  333 */       y2 = bb.field_72337_e;
/*  334 */       z1 = bb.field_72339_c;
/*  335 */       z2 = bb.field_72334_f;
/*  336 */     } else if (face == EnumFacing.SOUTH) {
/*  337 */       x1 = bb.field_72340_a;
/*  338 */       x2 = bb.field_72336_d;
/*  339 */       y1 = bb.field_72338_b;
/*  340 */       y2 = bb.field_72337_e;
/*  341 */       z1 = bb.field_72334_f;
/*  342 */       z2 = bb.field_72334_f;
/*  343 */     } else if (face == EnumFacing.NORTH) {
/*  344 */       x1 = bb.field_72340_a;
/*  345 */       x2 = bb.field_72336_d;
/*  346 */       y1 = bb.field_72338_b;
/*  347 */       y2 = bb.field_72337_e;
/*  348 */       z1 = bb.field_72339_c;
/*  349 */       z2 = bb.field_72339_c;
/*      */     } 
/*  351 */     GlStateManager.func_179094_E();
/*  352 */     GlStateManager.func_179097_i();
/*  353 */     GlStateManager.func_179090_x();
/*  354 */     GlStateManager.func_179147_l();
/*  355 */     GlStateManager.func_179118_c();
/*  356 */     GlStateManager.func_179132_a(false);
/*  357 */     builder.func_181668_a(5, DefaultVertexFormats.field_181706_f);
/*  358 */     if (face == EnumFacing.EAST || face == EnumFacing.WEST || face == EnumFacing.NORTH || face == EnumFacing.SOUTH) {
/*  359 */       builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  360 */       builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  361 */       builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  362 */       builder.func_181662_b(x1, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  363 */       builder.func_181662_b(x1, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  364 */       builder.func_181662_b(x1, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  365 */       builder.func_181662_b(x1, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  366 */       builder.func_181662_b(x1, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  367 */       builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  368 */       builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  369 */       builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  370 */       builder.func_181662_b(x2, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  371 */       builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  372 */       builder.func_181662_b(x2, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  373 */       builder.func_181662_b(x2, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  374 */       builder.func_181662_b(x2, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  375 */       builder.func_181662_b(x1, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  376 */       builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  377 */       builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  378 */       builder.func_181662_b(x2, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  379 */       builder.func_181662_b(x1, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  380 */       builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  381 */       builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  382 */       builder.func_181662_b(x1, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  383 */       builder.func_181662_b(x1, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  384 */       builder.func_181662_b(x1, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  385 */       builder.func_181662_b(x2, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  386 */       builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  387 */       builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  388 */       builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  389 */     } else if (face == EnumFacing.UP) {
/*  390 */       builder.func_181662_b(x1, y1, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  391 */       builder.func_181662_b(x1, y1, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  392 */       builder.func_181662_b(x1, y1, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  393 */       builder.func_181662_b(x1, y1, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  394 */       builder.func_181662_b(x1, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  395 */       builder.func_181662_b(x1, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  396 */       builder.func_181662_b(x1, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  397 */       builder.func_181662_b(x1, y1, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  398 */       builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  399 */       builder.func_181662_b(x2, y1, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  400 */       builder.func_181662_b(x2, y1, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  401 */       builder.func_181662_b(x2, y1, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  402 */       builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  403 */       builder.func_181662_b(x2, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  404 */       builder.func_181662_b(x2, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  405 */       builder.func_181662_b(x2, y1, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  406 */       builder.func_181662_b(x1, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  407 */       builder.func_181662_b(x1, y1, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  408 */       builder.func_181662_b(x1, y1, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  409 */       builder.func_181662_b(x2, y1, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  410 */       builder.func_181662_b(x1, y1, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  411 */       builder.func_181662_b(x2, y1, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  412 */       builder.func_181662_b(x2, y1, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  413 */       builder.func_181662_b(x1, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  414 */       builder.func_181662_b(x1, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  415 */       builder.func_181662_b(x1, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  416 */       builder.func_181662_b(x2, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  417 */       builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  418 */       builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  419 */       builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  420 */     } else if (face == EnumFacing.DOWN) {
/*  421 */       builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  422 */       builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  423 */       builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  424 */       builder.func_181662_b(x1, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  425 */       builder.func_181662_b(x1, y2, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  426 */       builder.func_181662_b(x1, y2, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  427 */       builder.func_181662_b(x1, y2, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  428 */       builder.func_181662_b(x1, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  429 */       builder.func_181662_b(x2, y2, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  430 */       builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  431 */       builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  432 */       builder.func_181662_b(x2, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  433 */       builder.func_181662_b(x2, y2, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  434 */       builder.func_181662_b(x2, y2, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  435 */       builder.func_181662_b(x2, y2, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  436 */       builder.func_181662_b(x2, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  437 */       builder.func_181662_b(x1, y2, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  438 */       builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  439 */       builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  440 */       builder.func_181662_b(x2, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  441 */       builder.func_181662_b(x1, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  442 */       builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  443 */       builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  444 */       builder.func_181662_b(x1, y2, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  445 */       builder.func_181662_b(x1, y2, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  446 */       builder.func_181662_b(x1, y2, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  447 */       builder.func_181662_b(x2, y2, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  448 */       builder.func_181662_b(x2, y2, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  449 */       builder.func_181662_b(x2, y2, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  450 */       builder.func_181662_b(x2, y2, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
/*      */     } 
/*  452 */     tessellator.func_78381_a();
/*  453 */     GlStateManager.func_179132_a(true);
/*  454 */     GlStateManager.func_179084_k();
/*  455 */     GlStateManager.func_179141_d();
/*  456 */     GlStateManager.func_179098_w();
/*  457 */     GlStateManager.func_179126_j();
/*  458 */     GlStateManager.func_179121_F();
/*      */   }
/*      */   
/*      */   public static void drawGradientRect(int x, int y, int w, int h, int startColor, int endColor) {
/*  462 */     float f = (startColor >> 24 & 0xFF) / 255.0F;
/*  463 */     float f1 = (startColor >> 16 & 0xFF) / 255.0F;
/*  464 */     float f2 = (startColor >> 8 & 0xFF) / 255.0F;
/*  465 */     float f3 = (startColor & 0xFF) / 255.0F;
/*  466 */     float f4 = (endColor >> 24 & 0xFF) / 255.0F;
/*  467 */     float f5 = (endColor >> 16 & 0xFF) / 255.0F;
/*  468 */     float f6 = (endColor >> 8 & 0xFF) / 255.0F;
/*  469 */     float f7 = (endColor & 0xFF) / 255.0F;
/*  470 */     GlStateManager.func_179090_x();
/*  471 */     GlStateManager.func_179147_l();
/*  472 */     GlStateManager.func_179118_c();
/*  473 */     GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
/*  474 */     GlStateManager.func_179103_j(7425);
/*  475 */     Tessellator tessellator = Tessellator.func_178181_a();
/*  476 */     BufferBuilder vertexbuffer = tessellator.func_178180_c();
/*  477 */     vertexbuffer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
/*  478 */     vertexbuffer.func_181662_b(x + w, y, 0.0D).func_181666_a(f1, f2, f3, f).func_181675_d();
/*  479 */     vertexbuffer.func_181662_b(x, y, 0.0D).func_181666_a(f1, f2, f3, f).func_181675_d();
/*  480 */     vertexbuffer.func_181662_b(x, y + h, 0.0D).func_181666_a(f5, f6, f7, f4).func_181675_d();
/*  481 */     vertexbuffer.func_181662_b(x + w, y + h, 0.0D).func_181666_a(f5, f6, f7, f4).func_181675_d();
/*  482 */     tessellator.func_78381_a();
/*  483 */     GlStateManager.func_179103_j(7424);
/*  484 */     GlStateManager.func_179084_k();
/*  485 */     GlStateManager.func_179141_d();
/*  486 */     GlStateManager.func_179098_w();
/*      */   }
/*      */   
/*      */   public static void drawGradientBlockOutline(BlockPos pos, Color startColor, Color endColor, float linewidth, double height) {
/*  490 */     IBlockState iblockstate = mc.field_71441_e.func_180495_p(pos);
/*  491 */     Vec3d interp = EntityUtil.interpolateEntity((Entity)mc.field_71439_g, mc.func_184121_ak());
/*  492 */     drawGradientBlockOutline(iblockstate.func_185918_c((World)mc.field_71441_e, pos).func_186662_g(0.0020000000949949026D).func_72317_d(-interp.field_72450_a, -interp.field_72448_b, -interp.field_72449_c).func_72321_a(0.0D, height, 0.0D), startColor, endColor, linewidth);
/*      */   }
/*      */   
/*      */   public static void drawProperGradientBlockOutline(BlockPos pos, Color startColor, Color midColor, Color endColor, float linewidth) {
/*  496 */     IBlockState iblockstate = mc.field_71441_e.func_180495_p(pos);
/*  497 */     Vec3d interp = EntityUtil.interpolateEntity((Entity)mc.field_71439_g, mc.func_184121_ak());
/*  498 */     drawProperGradientBlockOutline(iblockstate.func_185918_c((World)mc.field_71441_e, pos).func_186662_g(0.0020000000949949026D).func_72317_d(-interp.field_72450_a, -interp.field_72448_b, -interp.field_72449_c), startColor, midColor, endColor, linewidth);
/*      */   }
/*      */   
/*      */   public static void drawProperGradientBlockOutline(AxisAlignedBB bb, Color startColor, Color midColor, Color endColor, float linewidth) {
/*  502 */     float red = endColor.getRed() / 255.0F;
/*  503 */     float green = endColor.getGreen() / 255.0F;
/*  504 */     float blue = endColor.getBlue() / 255.0F;
/*  505 */     float alpha = endColor.getAlpha() / 255.0F;
/*  506 */     float red1 = midColor.getRed() / 255.0F;
/*  507 */     float green1 = midColor.getGreen() / 255.0F;
/*  508 */     float blue1 = midColor.getBlue() / 255.0F;
/*  509 */     float alpha1 = midColor.getAlpha() / 255.0F;
/*  510 */     float red2 = startColor.getRed() / 255.0F;
/*  511 */     float green2 = startColor.getGreen() / 255.0F;
/*  512 */     float blue2 = startColor.getBlue() / 255.0F;
/*  513 */     float alpha2 = startColor.getAlpha() / 255.0F;
/*  514 */     double dif = (bb.field_72337_e - bb.field_72338_b) / 2.0D;
/*  515 */     GlStateManager.func_179094_E();
/*  516 */     GlStateManager.func_179147_l();
/*  517 */     GlStateManager.func_179097_i();
/*  518 */     GlStateManager.func_179120_a(770, 771, 0, 1);
/*  519 */     GlStateManager.func_179090_x();
/*  520 */     GlStateManager.func_179132_a(false);
/*  521 */     GL11.glEnable(2848);
/*  522 */     GL11.glHint(3154, 4354);
/*  523 */     GL11.glLineWidth(linewidth);
/*  524 */     GL11.glBegin(1);
/*  525 */     GL11.glColor4d(red, green, blue, alpha);
/*  526 */     GL11.glVertex3d(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c);
/*  527 */     GL11.glVertex3d(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c);
/*  528 */     GL11.glVertex3d(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c);
/*  529 */     GL11.glVertex3d(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f);
/*  530 */     GL11.glVertex3d(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f);
/*  531 */     GL11.glVertex3d(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f);
/*  532 */     GL11.glVertex3d(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f);
/*  533 */     GL11.glVertex3d(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c);
/*  534 */     GL11.glVertex3d(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c);
/*  535 */     GL11.glColor4d(red1, green1, blue1, alpha1);
/*  536 */     GL11.glVertex3d(bb.field_72340_a, bb.field_72338_b + dif, bb.field_72339_c);
/*  537 */     GL11.glVertex3d(bb.field_72340_a, bb.field_72338_b + dif, bb.field_72339_c);
/*  538 */     GL11.glColor4f(red2, green2, blue2, alpha2);
/*  539 */     GL11.glVertex3d(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c);
/*  540 */     GL11.glColor4d(red, green, blue, alpha);
/*  541 */     GL11.glVertex3d(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f);
/*  542 */     GL11.glColor4d(red1, green1, blue1, alpha1);
/*  543 */     GL11.glVertex3d(bb.field_72340_a, bb.field_72338_b + dif, bb.field_72334_f);
/*  544 */     GL11.glVertex3d(bb.field_72340_a, bb.field_72338_b + dif, bb.field_72334_f);
/*  545 */     GL11.glColor4d(red2, green2, blue2, alpha2);
/*  546 */     GL11.glVertex3d(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f);
/*  547 */     GL11.glColor4d(red, green, blue, alpha);
/*  548 */     GL11.glVertex3d(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f);
/*  549 */     GL11.glColor4d(red1, green1, blue1, alpha1);
/*  550 */     GL11.glVertex3d(bb.field_72336_d, bb.field_72338_b + dif, bb.field_72334_f);
/*  551 */     GL11.glVertex3d(bb.field_72336_d, bb.field_72338_b + dif, bb.field_72334_f);
/*  552 */     GL11.glColor4d(red2, green2, blue2, alpha2);
/*  553 */     GL11.glVertex3d(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f);
/*  554 */     GL11.glColor4d(red, green, blue, alpha);
/*  555 */     GL11.glVertex3d(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c);
/*  556 */     GL11.glColor4d(red1, green1, blue1, alpha1);
/*  557 */     GL11.glVertex3d(bb.field_72336_d, bb.field_72338_b + dif, bb.field_72339_c);
/*  558 */     GL11.glVertex3d(bb.field_72336_d, bb.field_72338_b + dif, bb.field_72339_c);
/*  559 */     GL11.glColor4d(red2, green2, blue2, alpha2);
/*  560 */     GL11.glVertex3d(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c);
/*  561 */     GL11.glColor4d(red2, green2, blue2, alpha2);
/*  562 */     GL11.glVertex3d(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c);
/*  563 */     GL11.glVertex3d(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c);
/*  564 */     GL11.glVertex3d(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c);
/*  565 */     GL11.glVertex3d(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f);
/*  566 */     GL11.glVertex3d(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f);
/*  567 */     GL11.glVertex3d(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f);
/*  568 */     GL11.glVertex3d(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f);
/*  569 */     GL11.glVertex3d(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c);
/*  570 */     GL11.glVertex3d(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c);
/*  571 */     GL11.glEnd();
/*  572 */     GL11.glDisable(2848);
/*  573 */     GlStateManager.func_179132_a(true);
/*  574 */     GlStateManager.func_179126_j();
/*  575 */     GlStateManager.func_179098_w();
/*  576 */     GlStateManager.func_179084_k();
/*  577 */     GlStateManager.func_179121_F();
/*      */   }
/*      */   
/*      */   public static void drawGradientBlockOutline(AxisAlignedBB bb, Color startColor, Color endColor, float linewidth) {
/*  581 */     float red = startColor.getRed() / 255.0F;
/*  582 */     float green = startColor.getGreen() / 255.0F;
/*  583 */     float blue = startColor.getBlue() / 255.0F;
/*  584 */     float alpha = startColor.getAlpha() / 255.0F;
/*  585 */     float red1 = endColor.getRed() / 255.0F;
/*  586 */     float green1 = endColor.getGreen() / 255.0F;
/*  587 */     float blue1 = endColor.getBlue() / 255.0F;
/*  588 */     float alpha1 = endColor.getAlpha() / 255.0F;
/*  589 */     GlStateManager.func_179094_E();
/*  590 */     GlStateManager.func_179147_l();
/*  591 */     GlStateManager.func_179097_i();
/*  592 */     GlStateManager.func_179120_a(770, 771, 0, 1);
/*  593 */     GlStateManager.func_179090_x();
/*  594 */     GlStateManager.func_179132_a(false);
/*  595 */     GL11.glEnable(2848);
/*  596 */     GL11.glHint(3154, 4354);
/*  597 */     GL11.glLineWidth(linewidth);
/*  598 */     Tessellator tessellator = Tessellator.func_178181_a();
/*  599 */     BufferBuilder bufferbuilder = tessellator.func_178180_c();
/*  600 */     bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181706_f);
/*  601 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  602 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  603 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  604 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  605 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  606 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  607 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  608 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  609 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  610 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  611 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  612 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  613 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  614 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  615 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  616 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  617 */     tessellator.func_78381_a();
/*  618 */     GL11.glDisable(2848);
/*  619 */     GlStateManager.func_179132_a(true);
/*  620 */     GlStateManager.func_179126_j();
/*  621 */     GlStateManager.func_179098_w();
/*  622 */     GlStateManager.func_179084_k();
/*  623 */     GlStateManager.func_179121_F();
/*      */   }
/*      */   
/*      */   public static void drawGradientFilledBox(BlockPos pos, Color startColor, Color endColor) {
/*  627 */     IBlockState iblockstate = mc.field_71441_e.func_180495_p(pos);
/*  628 */     Vec3d interp = EntityUtil.interpolateEntity((Entity)mc.field_71439_g, mc.func_184121_ak());
/*  629 */     drawGradientFilledBox(iblockstate.func_185918_c((World)mc.field_71441_e, pos).func_186662_g(0.0020000000949949026D).func_72317_d(-interp.field_72450_a, -interp.field_72448_b, -interp.field_72449_c), startColor, endColor);
/*      */   }
/*      */   
/*      */   public static void drawGradientFilledBox(AxisAlignedBB bb, Color startColor, Color endColor) {
/*  633 */     GlStateManager.func_179094_E();
/*  634 */     GlStateManager.func_179147_l();
/*  635 */     GlStateManager.func_179097_i();
/*  636 */     GlStateManager.func_179120_a(770, 771, 0, 1);
/*  637 */     GlStateManager.func_179090_x();
/*  638 */     GlStateManager.func_179132_a(false);
/*  639 */     float alpha = endColor.getAlpha() / 255.0F;
/*  640 */     float red = endColor.getRed() / 255.0F;
/*  641 */     float green = endColor.getGreen() / 255.0F;
/*  642 */     float blue = endColor.getBlue() / 255.0F;
/*  643 */     float alpha1 = startColor.getAlpha() / 255.0F;
/*  644 */     float red1 = startColor.getRed() / 255.0F;
/*  645 */     float green1 = startColor.getGreen() / 255.0F;
/*  646 */     float blue1 = startColor.getBlue() / 255.0F;
/*  647 */     Tessellator tessellator = Tessellator.func_178181_a();
/*  648 */     BufferBuilder bufferbuilder = tessellator.func_178180_c();
/*  649 */     bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181706_f);
/*  650 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  651 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  652 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  653 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  654 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  655 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  656 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  657 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  658 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  659 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  660 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  661 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  662 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  663 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  664 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  665 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  666 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  667 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  668 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  669 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  670 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  671 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  672 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  673 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  674 */     tessellator.func_78381_a();
/*  675 */     GlStateManager.func_179132_a(true);
/*  676 */     GlStateManager.func_179126_j();
/*  677 */     GlStateManager.func_179098_w();
/*  678 */     GlStateManager.func_179084_k();
/*  679 */     GlStateManager.func_179121_F();
/*      */   }
/*      */   
/*      */   public static void drawGradientRect(float x, float y, float w, float h, int startColor, int endColor) {
/*  683 */     float f = (startColor >> 24 & 0xFF) / 255.0F;
/*  684 */     float f1 = (startColor >> 16 & 0xFF) / 255.0F;
/*  685 */     float f2 = (startColor >> 8 & 0xFF) / 255.0F;
/*  686 */     float f3 = (startColor & 0xFF) / 255.0F;
/*  687 */     float f4 = (endColor >> 24 & 0xFF) / 255.0F;
/*  688 */     float f5 = (endColor >> 16 & 0xFF) / 255.0F;
/*  689 */     float f6 = (endColor >> 8 & 0xFF) / 255.0F;
/*  690 */     float f7 = (endColor & 0xFF) / 255.0F;
/*  691 */     GlStateManager.func_179090_x();
/*  692 */     GlStateManager.func_179147_l();
/*  693 */     GlStateManager.func_179118_c();
/*  694 */     GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
/*  695 */     GlStateManager.func_179103_j(7425);
/*  696 */     Tessellator tessellator = Tessellator.func_178181_a();
/*  697 */     BufferBuilder vertexbuffer = tessellator.func_178180_c();
/*  698 */     vertexbuffer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
/*  699 */     vertexbuffer.func_181662_b(x + w, y, 0.0D).func_181666_a(f1, f2, f3, f).func_181675_d();
/*  700 */     vertexbuffer.func_181662_b(x, y, 0.0D).func_181666_a(f1, f2, f3, f).func_181675_d();
/*  701 */     vertexbuffer.func_181662_b(x, y + h, 0.0D).func_181666_a(f5, f6, f7, f4).func_181675_d();
/*  702 */     vertexbuffer.func_181662_b(x + w, y + h, 0.0D).func_181666_a(f5, f6, f7, f4).func_181675_d();
/*  703 */     tessellator.func_78381_a();
/*  704 */     GlStateManager.func_179103_j(7424);
/*  705 */     GlStateManager.func_179084_k();
/*  706 */     GlStateManager.func_179141_d();
/*  707 */     GlStateManager.func_179098_w();
/*      */   }
/*      */   
/*      */   public static void drawFilledCircle(double x, double y, double z, Color color, double radius) {
/*  711 */     Tessellator tessellator = Tessellator.func_178181_a();
/*  712 */     BufferBuilder builder = tessellator.func_178180_c();
/*  713 */     builder.func_181668_a(5, DefaultVertexFormats.field_181706_f);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void drawGradientBoxTest(BlockPos pos, Color startColor, Color endColor) {}
/*      */   
/*      */   public static void blockESP(BlockPos b, Color c, double length, double length2) {
/*  720 */     blockEsp(b, c, length, length2);
/*      */   }
/*      */   
/*      */   public static void drawBoxESP(BlockPos pos, Color color, boolean secondC, Color secondColor, float lineWidth, boolean outline, boolean box, int boxAlpha, boolean air) {
/*  724 */     if (box) {
/*  725 */       drawBox(pos, new Color(color.getRed(), color.getGreen(), color.getBlue(), boxAlpha));
/*      */     }
/*  727 */     if (outline) {
/*  728 */       drawBlockOutline(pos, secondC ? secondColor : color, lineWidth, air);
/*      */     }
/*      */   }
/*      */   
/*      */   public static void drawBoxESP(BlockPos pos, Color color, boolean secondC, Color secondColor, float lineWidth, boolean outline, boolean box, int boxAlpha, boolean air, double height, boolean gradientBox, boolean gradientOutline, boolean invertGradientBox, boolean invertGradientOutline, int gradientAlpha) {
/*  733 */     if (box) {
/*  734 */       drawBox(pos, new Color(color.getRed(), color.getGreen(), color.getBlue(), boxAlpha), height, gradientBox, invertGradientBox, gradientAlpha);
/*      */     }
/*  736 */     if (outline) {
/*  737 */       drawBlockOutline(pos, secondC ? secondColor : color, lineWidth, air, height, gradientOutline, invertGradientOutline, gradientAlpha);
/*      */     }
/*      */   }
/*      */   
/*      */   public static void glScissor(float x, float y, float x1, float y1, ScaledResolution sr) {
/*  742 */     GL11.glScissor((int)(x * sr.func_78325_e()), (int)(mc.field_71440_d - y1 * sr.func_78325_e()), (int)((x1 - x) * sr.func_78325_e()), (int)((y1 - y) * sr.func_78325_e()));
/*      */   }
/*      */   
/*      */   public static void drawLine(float x, float y, float x1, float y1, float thickness, int hex) {
/*  746 */     float red = (hex >> 16 & 0xFF) / 255.0F;
/*  747 */     float green = (hex >> 8 & 0xFF) / 255.0F;
/*  748 */     float blue = (hex & 0xFF) / 255.0F;
/*  749 */     float alpha = (hex >> 24 & 0xFF) / 255.0F;
/*  750 */     GlStateManager.func_179094_E();
/*  751 */     GlStateManager.func_179090_x();
/*  752 */     GlStateManager.func_179147_l();
/*  753 */     GlStateManager.func_179118_c();
/*  754 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/*  755 */     GlStateManager.func_179103_j(7425);
/*  756 */     GL11.glLineWidth(thickness);
/*  757 */     GL11.glEnable(2848);
/*  758 */     GL11.glHint(3154, 4354);
/*  759 */     Tessellator tessellator = Tessellator.func_178181_a();
/*  760 */     BufferBuilder bufferbuilder = tessellator.func_178180_c();
/*  761 */     bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181706_f);
/*  762 */     bufferbuilder.func_181662_b(x, y, 0.0D).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  763 */     bufferbuilder.func_181662_b(x1, y1, 0.0D).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  764 */     tessellator.func_78381_a();
/*  765 */     GlStateManager.func_179103_j(7424);
/*  766 */     GL11.glDisable(2848);
/*  767 */     GlStateManager.func_179084_k();
/*  768 */     GlStateManager.func_179141_d();
/*  769 */     GlStateManager.func_179098_w();
/*  770 */     GlStateManager.func_179121_F();
/*      */   }
/*      */   
/*      */   public static void drawBox(BlockPos pos, Color color) {
/*  774 */     AxisAlignedBB bb = new AxisAlignedBB(pos.func_177958_n() - (mc.func_175598_ae()).field_78730_l, pos.func_177956_o() - (mc.func_175598_ae()).field_78731_m, pos.func_177952_p() - (mc.func_175598_ae()).field_78728_n, (pos.func_177958_n() + 1) - (mc.func_175598_ae()).field_78730_l, (pos.func_177956_o() + 1) - (mc.func_175598_ae()).field_78731_m, (pos.func_177952_p() + 1) - (mc.func_175598_ae()).field_78728_n);
/*  775 */     camera.func_78547_a(((Entity)Objects.requireNonNull((T)mc.func_175606_aa())).field_70165_t, (mc.func_175606_aa()).field_70163_u, (mc.func_175606_aa()).field_70161_v);
/*  776 */     if (camera.func_78546_a(new AxisAlignedBB(bb.field_72340_a + (mc.func_175598_ae()).field_78730_l, bb.field_72338_b + (mc.func_175598_ae()).field_78731_m, bb.field_72339_c + (mc.func_175598_ae()).field_78728_n, bb.field_72336_d + (mc.func_175598_ae()).field_78730_l, bb.field_72337_e + (mc.func_175598_ae()).field_78731_m, bb.field_72334_f + (mc.func_175598_ae()).field_78728_n))) {
/*  777 */       GlStateManager.func_179094_E();
/*  778 */       GlStateManager.func_179147_l();
/*  779 */       GlStateManager.func_179097_i();
/*  780 */       GlStateManager.func_179120_a(770, 771, 0, 1);
/*  781 */       GlStateManager.func_179090_x();
/*  782 */       GlStateManager.func_179132_a(false);
/*  783 */       GL11.glEnable(2848);
/*  784 */       GL11.glHint(3154, 4354);
/*  785 */       RenderGlobal.func_189696_b(bb, color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
/*  786 */       GL11.glDisable(2848);
/*  787 */       GlStateManager.func_179132_a(true);
/*  788 */       GlStateManager.func_179126_j();
/*  789 */       GlStateManager.func_179098_w();
/*  790 */       GlStateManager.func_179084_k();
/*  791 */       GlStateManager.func_179121_F();
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void drawBetterGradientBox(BlockPos pos, Color startColor, Color endColor) {
/*  796 */     float red = startColor.getRed() / 255.0F;
/*  797 */     float green = startColor.getGreen() / 255.0F;
/*  798 */     float blue = startColor.getBlue() / 255.0F;
/*  799 */     float alpha = startColor.getAlpha() / 255.0F;
/*  800 */     float red1 = endColor.getRed() / 255.0F;
/*  801 */     float green1 = endColor.getGreen() / 255.0F;
/*  802 */     float blue1 = endColor.getBlue() / 255.0F;
/*  803 */     float alpha1 = endColor.getAlpha() / 255.0F;
/*  804 */     AxisAlignedBB bb = new AxisAlignedBB(pos.func_177958_n() - (mc.func_175598_ae()).field_78730_l, pos.func_177956_o() - (mc.func_175598_ae()).field_78731_m, pos.func_177952_p() - (mc.func_175598_ae()).field_78728_n, (pos.func_177958_n() + 1) - (mc.func_175598_ae()).field_78730_l, (pos.func_177956_o() + 1) - (mc.func_175598_ae()).field_78731_m, (pos.func_177952_p() + 1) - (mc.func_175598_ae()).field_78728_n);
/*  805 */     double offset = (bb.field_72337_e - bb.field_72338_b) / 2.0D;
/*  806 */     Tessellator tessellator = Tessellator.func_178181_a();
/*  807 */     BufferBuilder builder = tessellator.func_178180_c();
/*  808 */     GlStateManager.func_179094_E();
/*  809 */     GlStateManager.func_179147_l();
/*  810 */     GlStateManager.func_179097_i();
/*  811 */     GlStateManager.func_179120_a(770, 771, 0, 1);
/*  812 */     GlStateManager.func_179090_x();
/*  813 */     GlStateManager.func_179132_a(false);
/*  814 */     GL11.glEnable(2848);
/*  815 */     GL11.glHint(3154, 4354);
/*  816 */     builder.func_181668_a(5, DefaultVertexFormats.field_181706_f);
/*  817 */     builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  818 */     builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  819 */     builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  820 */     builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  821 */     builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  822 */     builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  823 */     builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  824 */     builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  825 */     builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  826 */     builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  827 */     builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  828 */     builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  829 */     builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  830 */     builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  831 */     builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  832 */     builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  833 */     builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  834 */     builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  835 */     builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  836 */     builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  837 */     builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  838 */     builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  839 */     builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  840 */     builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  841 */     builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  842 */     builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  843 */     builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  844 */     builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  845 */     builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  846 */     builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*      */   }
/*      */   
/*      */   public static void drawBetterGradientBox(BlockPos pos, Color startColor, Color midColor, Color endColor) {
/*  850 */     float red = startColor.getRed() / 255.0F;
/*  851 */     float green = startColor.getGreen() / 255.0F;
/*  852 */     float blue = startColor.getBlue() / 255.0F;
/*  853 */     float alpha = startColor.getAlpha() / 255.0F;
/*  854 */     float red1 = endColor.getRed() / 255.0F;
/*  855 */     float green1 = endColor.getGreen() / 255.0F;
/*  856 */     float blue1 = endColor.getBlue() / 255.0F;
/*  857 */     float alpha1 = endColor.getAlpha() / 255.0F;
/*  858 */     float red2 = midColor.getRed() / 255.0F;
/*  859 */     float green2 = midColor.getGreen() / 255.0F;
/*  860 */     float blue2 = midColor.getBlue() / 255.0F;
/*  861 */     float alpha2 = midColor.getAlpha() / 255.0F;
/*  862 */     AxisAlignedBB bb = new AxisAlignedBB(pos.func_177958_n() - (mc.func_175598_ae()).field_78730_l, pos.func_177956_o() - (mc.func_175598_ae()).field_78731_m, pos.func_177952_p() - (mc.func_175598_ae()).field_78728_n, (pos.func_177958_n() + 1) - (mc.func_175598_ae()).field_78730_l, (pos.func_177956_o() + 1) - (mc.func_175598_ae()).field_78731_m, (pos.func_177952_p() + 1) - (mc.func_175598_ae()).field_78728_n);
/*  863 */     double offset = (bb.field_72337_e - bb.field_72338_b) / 2.0D;
/*  864 */     Tessellator tessellator = Tessellator.func_178181_a();
/*  865 */     BufferBuilder builder = tessellator.func_178180_c();
/*  866 */     GlStateManager.func_179094_E();
/*  867 */     GlStateManager.func_179147_l();
/*  868 */     GlStateManager.func_179097_i();
/*  869 */     GlStateManager.func_179120_a(770, 771, 0, 1);
/*  870 */     GlStateManager.func_179090_x();
/*  871 */     GlStateManager.func_179132_a(false);
/*  872 */     GL11.glEnable(2848);
/*  873 */     GL11.glHint(3154, 4354);
/*  874 */     builder.func_181668_a(5, DefaultVertexFormats.field_181706_f);
/*  875 */     builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  876 */     builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  877 */     builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  878 */     builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  879 */     builder.func_181662_b(bb.field_72340_a, bb.field_72338_b + offset, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
/*  880 */     builder.func_181662_b(bb.field_72340_a, bb.field_72338_b + offset, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
/*  881 */     builder.func_181662_b(bb.field_72340_a, bb.field_72338_b + offset, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
/*  882 */     builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  883 */     builder.func_181662_b(bb.field_72336_d, bb.field_72338_b + offset, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
/*  884 */     builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  885 */     builder.func_181662_b(bb.field_72340_a, bb.field_72338_b + offset, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
/*  886 */     builder.func_181662_b(bb.field_72340_a, bb.field_72338_b + offset, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
/*  887 */     builder.func_181662_b(bb.field_72340_a, bb.field_72338_b + offset, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
/*  888 */     builder.func_181662_b(bb.field_72340_a, bb.field_72338_b + offset, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
/*  889 */     builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  890 */     builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  891 */     builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  892 */     builder.func_181662_b(bb.field_72340_a, bb.field_72338_b + offset, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
/*  893 */     builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  894 */     builder.func_181662_b(bb.field_72336_d, bb.field_72338_b + offset, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
/*  895 */     tessellator.func_78381_a();
/*  896 */     GL11.glDisable(2848);
/*  897 */     GlStateManager.func_179132_a(true);
/*  898 */     GlStateManager.func_179126_j();
/*  899 */     GlStateManager.func_179098_w();
/*  900 */     GlStateManager.func_179084_k();
/*  901 */     GlStateManager.func_179121_F();
/*      */   }
/*      */   
/*      */   public static void drawEvenBetterGradientBox(BlockPos pos, Color startColor, Color midColor, Color endColor) {
/*  905 */     float red = startColor.getRed() / 255.0F;
/*  906 */     float green = startColor.getGreen() / 255.0F;
/*  907 */     float blue = startColor.getBlue() / 255.0F;
/*  908 */     float alpha = startColor.getAlpha() / 255.0F;
/*  909 */     float red1 = endColor.getRed() / 255.0F;
/*  910 */     float green1 = endColor.getGreen() / 255.0F;
/*  911 */     float blue1 = endColor.getBlue() / 255.0F;
/*  912 */     float alpha1 = endColor.getAlpha() / 255.0F;
/*  913 */     float red2 = midColor.getRed() / 255.0F;
/*  914 */     float green2 = midColor.getGreen() / 255.0F;
/*  915 */     float blue2 = midColor.getBlue() / 255.0F;
/*  916 */     float alpha2 = midColor.getAlpha() / 255.0F;
/*  917 */     AxisAlignedBB bb = new AxisAlignedBB(pos.func_177958_n() - (mc.func_175598_ae()).field_78730_l, pos.func_177956_o() - (mc.func_175598_ae()).field_78731_m, pos.func_177952_p() - (mc.func_175598_ae()).field_78728_n, (pos.func_177958_n() + 1) - (mc.func_175598_ae()).field_78730_l, (pos.func_177956_o() + 1) - (mc.func_175598_ae()).field_78731_m, (pos.func_177952_p() + 1) - (mc.func_175598_ae()).field_78728_n);
/*  918 */     double offset = (bb.field_72337_e - bb.field_72338_b) / 2.0D;
/*  919 */     Tessellator tessellator = Tessellator.func_178181_a();
/*  920 */     BufferBuilder builder = tessellator.func_178180_c();
/*  921 */     GlStateManager.func_179094_E();
/*  922 */     GlStateManager.func_179147_l();
/*  923 */     GlStateManager.func_179097_i();
/*  924 */     GlStateManager.func_179120_a(770, 771, 0, 1);
/*  925 */     GlStateManager.func_179090_x();
/*  926 */     GlStateManager.func_179132_a(false);
/*  927 */     GL11.glEnable(2848);
/*  928 */     GL11.glHint(3154, 4354);
/*  929 */     builder.func_181668_a(5, DefaultVertexFormats.field_181706_f);
/*  930 */     builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  931 */     builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  932 */     builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  933 */     builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  934 */     builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  935 */     builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  936 */     builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  937 */     builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  938 */     builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  939 */     builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  940 */     builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  941 */     builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  942 */     builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  943 */     builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  944 */     builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  945 */     builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  946 */     builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  947 */     builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  948 */     builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  949 */     builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  950 */     builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  951 */     builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  952 */     builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
/*  953 */     builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  954 */     builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  955 */     builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  956 */     builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  957 */     builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  958 */     builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  959 */     builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  960 */     tessellator.func_78381_a();
/*  961 */     GL11.glDisable(2848);
/*  962 */     GlStateManager.func_179132_a(true);
/*  963 */     GlStateManager.func_179126_j();
/*  964 */     GlStateManager.func_179098_w();
/*  965 */     GlStateManager.func_179084_k();
/*  966 */     GlStateManager.func_179121_F();
/*      */   }
/*      */   
/*      */   public static void drawBox(BlockPos pos, Color color, double height, boolean gradient, boolean invert, int alpha) {
/*  970 */     if (gradient) {
/*  971 */       Color endColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
/*  972 */       drawOpenGradientBox(pos, invert ? endColor : color, invert ? color : endColor, height);
/*      */       return;
/*      */     } 
/*  975 */     AxisAlignedBB bb = new AxisAlignedBB(pos.func_177958_n() - (mc.func_175598_ae()).field_78730_l, pos.func_177956_o() - (mc.func_175598_ae()).field_78731_m, pos.func_177952_p() - (mc.func_175598_ae()).field_78728_n, (pos.func_177958_n() + 1) - (mc.func_175598_ae()).field_78730_l, (pos.func_177956_o() + 1) - (mc.func_175598_ae()).field_78731_m + height, (pos.func_177952_p() + 1) - (mc.func_175598_ae()).field_78728_n);
/*  976 */     camera.func_78547_a(((Entity)Objects.requireNonNull((T)mc.func_175606_aa())).field_70165_t, (mc.func_175606_aa()).field_70163_u, (mc.func_175606_aa()).field_70161_v);
/*  977 */     if (camera.func_78546_a(new AxisAlignedBB(bb.field_72340_a + (mc.func_175598_ae()).field_78730_l, bb.field_72338_b + (mc.func_175598_ae()).field_78731_m, bb.field_72339_c + (mc.func_175598_ae()).field_78728_n, bb.field_72336_d + (mc.func_175598_ae()).field_78730_l, bb.field_72337_e + (mc.func_175598_ae()).field_78731_m, bb.field_72334_f + (mc.func_175598_ae()).field_78728_n))) {
/*  978 */       GlStateManager.func_179094_E();
/*  979 */       GlStateManager.func_179147_l();
/*  980 */       GlStateManager.func_179097_i();
/*  981 */       GlStateManager.func_179120_a(770, 771, 0, 1);
/*  982 */       GlStateManager.func_179090_x();
/*  983 */       GlStateManager.func_179132_a(false);
/*  984 */       GL11.glEnable(2848);
/*  985 */       GL11.glHint(3154, 4354);
/*  986 */       RenderGlobal.func_189696_b(bb, color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
/*  987 */       GL11.glDisable(2848);
/*  988 */       GlStateManager.func_179132_a(true);
/*  989 */       GlStateManager.func_179126_j();
/*  990 */       GlStateManager.func_179098_w();
/*  991 */       GlStateManager.func_179084_k();
/*  992 */       GlStateManager.func_179121_F();
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void drawBlockOutline(BlockPos pos, Color color, float linewidth, boolean air) {
/*  997 */     IBlockState iblockstate = mc.field_71441_e.func_180495_p(pos);
/*  998 */     if ((air || iblockstate.func_185904_a() != Material.field_151579_a) && mc.field_71441_e.func_175723_af().func_177746_a(pos)) {
/*  999 */       Vec3d interp = EntityUtil.interpolateEntity((Entity)mc.field_71439_g, mc.func_184121_ak());
/* 1000 */       drawBlockOutline(iblockstate.func_185918_c((World)mc.field_71441_e, pos).func_186662_g(0.0020000000949949026D).func_72317_d(-interp.field_72450_a, -interp.field_72448_b, -interp.field_72449_c), color, linewidth);
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void drawBlockOutline(BlockPos pos, Color color, float linewidth, boolean air, double height, boolean gradient, boolean invert, int alpha) {
/* 1005 */     if (gradient) {
/* 1006 */       Color endColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
/* 1007 */       drawGradientBlockOutline(pos, invert ? endColor : color, invert ? color : endColor, linewidth, height);
/*      */       return;
/*      */     } 
/* 1010 */     IBlockState iblockstate = mc.field_71441_e.func_180495_p(pos);
/* 1011 */     if ((air || iblockstate.func_185904_a() != Material.field_151579_a) && mc.field_71441_e.func_175723_af().func_177746_a(pos)) {
/* 1012 */       AxisAlignedBB blockAxis = new AxisAlignedBB(pos.func_177958_n() - (mc.func_175598_ae()).field_78730_l, pos.func_177956_o() - (mc.func_175598_ae()).field_78731_m, pos.func_177952_p() - (mc.func_175598_ae()).field_78728_n, (pos.func_177958_n() + 1) - (mc.func_175598_ae()).field_78730_l, (pos.func_177956_o() + 1) - (mc.func_175598_ae()).field_78731_m + height, (pos.func_177952_p() + 1) - (mc.func_175598_ae()).field_78728_n);
/* 1013 */       drawBlockOutline(blockAxis.func_186662_g(0.0020000000949949026D), color, linewidth);
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void drawBlockOutline(AxisAlignedBB bb, Color color, float linewidth) {
/* 1018 */     float red = color.getRed() / 255.0F;
/* 1019 */     float green = color.getGreen() / 255.0F;
/* 1020 */     float blue = color.getBlue() / 255.0F;
/* 1021 */     float alpha = color.getAlpha() / 255.0F;
/* 1022 */     GlStateManager.func_179094_E();
/* 1023 */     GlStateManager.func_179147_l();
/* 1024 */     GlStateManager.func_179097_i();
/* 1025 */     GlStateManager.func_179120_a(770, 771, 0, 1);
/* 1026 */     GlStateManager.func_179090_x();
/* 1027 */     GlStateManager.func_179132_a(false);
/* 1028 */     GL11.glEnable(2848);
/* 1029 */     GL11.glHint(3154, 4354);
/* 1030 */     GL11.glLineWidth(linewidth);
/* 1031 */     Tessellator tessellator = Tessellator.func_178181_a();
/* 1032 */     BufferBuilder bufferbuilder = tessellator.func_178180_c();
/* 1033 */     bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181706_f);
/* 1034 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1035 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1036 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1037 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1038 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1039 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1040 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1041 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1042 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1043 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1044 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1045 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1046 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1047 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1048 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1049 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1050 */     tessellator.func_78381_a();
/* 1051 */     GL11.glDisable(2848);
/* 1052 */     GlStateManager.func_179132_a(true);
/* 1053 */     GlStateManager.func_179126_j();
/* 1054 */     GlStateManager.func_179098_w();
/* 1055 */     GlStateManager.func_179084_k();
/* 1056 */     GlStateManager.func_179121_F();
/*      */   }
/*      */   
/*      */   public static void drawBoxESP(BlockPos pos, Color color, float lineWidth, boolean outline, boolean box, int boxAlpha) {
/* 1060 */     AxisAlignedBB bb = new AxisAlignedBB(pos.func_177958_n() - (mc.func_175598_ae()).field_78730_l, pos.func_177956_o() - (mc.func_175598_ae()).field_78731_m, pos.func_177952_p() - (mc.func_175598_ae()).field_78728_n, (pos.func_177958_n() + 1) - (mc.func_175598_ae()).field_78730_l, (pos.func_177956_o() + 1) - (mc.func_175598_ae()).field_78731_m, (pos.func_177952_p() + 1) - (mc.func_175598_ae()).field_78728_n);
/* 1061 */     camera.func_78547_a(((Entity)Objects.requireNonNull((T)mc.func_175606_aa())).field_70165_t, (mc.func_175606_aa()).field_70163_u, (mc.func_175606_aa()).field_70161_v);
/* 1062 */     if (camera.func_78546_a(new AxisAlignedBB(bb.field_72340_a + (mc.func_175598_ae()).field_78730_l, bb.field_72338_b + (mc.func_175598_ae()).field_78731_m, bb.field_72339_c + (mc.func_175598_ae()).field_78728_n, bb.field_72336_d + (mc.func_175598_ae()).field_78730_l, bb.field_72337_e + (mc.func_175598_ae()).field_78731_m, bb.field_72334_f + (mc.func_175598_ae()).field_78728_n))) {
/* 1063 */       GlStateManager.func_179094_E();
/* 1064 */       GlStateManager.func_179147_l();
/* 1065 */       GlStateManager.func_179097_i();
/* 1066 */       GlStateManager.func_179120_a(770, 771, 0, 1);
/* 1067 */       GlStateManager.func_179090_x();
/* 1068 */       GlStateManager.func_179132_a(false);
/* 1069 */       GL11.glEnable(2848);
/* 1070 */       GL11.glHint(3154, 4354);
/* 1071 */       GL11.glLineWidth(lineWidth);
/* 1072 */       double dist = mc.field_71439_g.func_70011_f((pos.func_177958_n() + 0.5F), (pos.func_177956_o() + 0.5F), (pos.func_177952_p() + 0.5F)) * 0.75D;
/* 1073 */       if (box) {
/* 1074 */         RenderGlobal.func_189696_b(bb, color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, boxAlpha / 255.0F);
/*      */       }
/* 1076 */       if (outline) {
/* 1077 */         RenderGlobal.func_189694_a(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, bb.field_72336_d, bb.field_72337_e, bb.field_72334_f, color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
/*      */       }
/* 1079 */       GL11.glDisable(2848);
/* 1080 */       GlStateManager.func_179132_a(true);
/* 1081 */       GlStateManager.func_179126_j();
/* 1082 */       GlStateManager.func_179098_w();
/* 1083 */       GlStateManager.func_179084_k();
/* 1084 */       GlStateManager.func_179121_F();
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void drawText(BlockPos pos, String text) {
/* 1089 */     if (pos == null || text == null) {
/*      */       return;
/*      */     }
/* 1092 */     GlStateManager.func_179094_E();
/* 1093 */     glBillboardDistanceScaled(pos.func_177958_n() + 0.5F, pos.func_177956_o() + 0.5F, pos.func_177952_p() + 0.5F, (EntityPlayer)mc.field_71439_g, 1.0F);
/* 1094 */     GlStateManager.func_179097_i();
/* 1095 */     GlStateManager.func_179137_b(-(Phobos.textManager.getStringWidth(text) / 2.0D), 0.0D, 0.0D);
/* 1096 */     Phobos.textManager.drawStringWithShadow(text, 0.0F, 0.0F, -5592406);
/* 1097 */     GlStateManager.func_179121_F();
/*      */   }
/*      */   
/*      */   public static void drawOutlinedBlockESP(BlockPos pos, Color color, float linewidth) {
/* 1101 */     IBlockState iblockstate = mc.field_71441_e.func_180495_p(pos);
/* 1102 */     Vec3d interp = EntityUtil.interpolateEntity((Entity)mc.field_71439_g, mc.func_184121_ak());
/* 1103 */     drawBoundingBox(iblockstate.func_185918_c((World)mc.field_71441_e, pos).func_186662_g(0.0020000000949949026D).func_72317_d(-interp.field_72450_a, -interp.field_72448_b, -interp.field_72449_c), linewidth, ColorUtil.toRGBA(color));
/*      */   }
/*      */   
/*      */   public static void blockEsp(BlockPos blockPos, Color c, double length, double length2) {
/* 1107 */     double x = blockPos.func_177958_n() - mc.field_175616_W.field_78725_b;
/* 1108 */     double y = blockPos.func_177956_o() - mc.field_175616_W.field_78726_c;
/* 1109 */     double z = blockPos.func_177952_p() - mc.field_175616_W.field_78723_d;
/* 1110 */     GL11.glPushMatrix();
/* 1111 */     GL11.glBlendFunc(770, 771);
/* 1112 */     GL11.glEnable(3042);
/* 1113 */     GL11.glLineWidth(2.0F);
/* 1114 */     GL11.glDisable(3553);
/* 1115 */     GL11.glDisable(2929);
/* 1116 */     GL11.glDepthMask(false);
/* 1117 */     GL11.glColor4d((c.getRed() / 255.0F), (c.getGreen() / 255.0F), (c.getBlue() / 255.0F), 0.25D);
/* 1118 */     drawColorBox(new AxisAlignedBB(x, y, z, x + length2, y + 1.0D, z + length), 0.0F, 0.0F, 0.0F, 0.0F);
/* 1119 */     GL11.glColor4d(0.0D, 0.0D, 0.0D, 0.5D);
/* 1120 */     drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + length2, y + 1.0D, z + length));
/* 1121 */     GL11.glLineWidth(2.0F);
/* 1122 */     GL11.glEnable(3553);
/* 1123 */     GL11.glEnable(2929);
/* 1124 */     GL11.glDepthMask(true);
/* 1125 */     GL11.glDisable(3042);
/* 1126 */     GL11.glPopMatrix();
/* 1127 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/*      */   }
/*      */   
/*      */   public static void drawRect(float x, float y, float w, float h, int color) {
/* 1131 */     float alpha = (color >> 24 & 0xFF) / 255.0F;
/* 1132 */     float red = (color >> 16 & 0xFF) / 255.0F;
/* 1133 */     float green = (color >> 8 & 0xFF) / 255.0F;
/* 1134 */     float blue = (color & 0xFF) / 255.0F;
/* 1135 */     Tessellator tessellator = Tessellator.func_178181_a();
/* 1136 */     BufferBuilder bufferbuilder = tessellator.func_178180_c();
/* 1137 */     GlStateManager.func_179147_l();
/* 1138 */     GlStateManager.func_179090_x();
/* 1139 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/* 1140 */     bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181706_f);
/* 1141 */     bufferbuilder.func_181662_b(x, h, 0.0D).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1142 */     bufferbuilder.func_181662_b(w, h, 0.0D).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1143 */     bufferbuilder.func_181662_b(w, y, 0.0D).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1144 */     bufferbuilder.func_181662_b(x, y, 0.0D).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1145 */     tessellator.func_78381_a();
/* 1146 */     GlStateManager.func_179098_w();
/* 1147 */     GlStateManager.func_179084_k();
/*      */   }
/*      */   
/*      */   public static void drawColorBox(AxisAlignedBB axisalignedbb, float red, float green, float blue, float alpha) {
/* 1151 */     Tessellator ts = Tessellator.func_178181_a();
/* 1152 */     BufferBuilder vb = ts.func_178180_c();
/* 1153 */     vb.func_181668_a(7, DefaultVertexFormats.field_181707_g);
/* 1154 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1155 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1156 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1157 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1158 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1159 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1160 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1161 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1162 */     ts.func_78381_a();
/* 1163 */     vb.func_181668_a(7, DefaultVertexFormats.field_181707_g);
/* 1164 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1165 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1166 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1167 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1168 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1169 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1170 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1171 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1172 */     ts.func_78381_a();
/* 1173 */     vb.func_181668_a(7, DefaultVertexFormats.field_181707_g);
/* 1174 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1175 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1176 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1177 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1178 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1179 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1180 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1181 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1182 */     ts.func_78381_a();
/* 1183 */     vb.func_181668_a(7, DefaultVertexFormats.field_181707_g);
/* 1184 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1185 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1186 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1187 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1188 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1189 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1190 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1191 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1192 */     ts.func_78381_a();
/* 1193 */     vb.func_181668_a(7, DefaultVertexFormats.field_181707_g);
/* 1194 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1195 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1196 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1197 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1198 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1199 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1200 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1201 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1202 */     ts.func_78381_a();
/* 1203 */     vb.func_181668_a(7, DefaultVertexFormats.field_181707_g);
/* 1204 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1205 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1206 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1207 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1208 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1209 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1210 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1211 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1212 */     ts.func_78381_a();
/*      */   }
/*      */   
/*      */   public static void drawSelectionBoundingBox(AxisAlignedBB boundingBox) {
/* 1216 */     Tessellator tessellator = Tessellator.func_178181_a();
/* 1217 */     BufferBuilder vertexbuffer = tessellator.func_178180_c();
/* 1218 */     vertexbuffer.func_181668_a(3, DefaultVertexFormats.field_181705_e);
/* 1219 */     vertexbuffer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72338_b, boundingBox.field_72339_c).func_181675_d();
/* 1220 */     vertexbuffer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72338_b, boundingBox.field_72339_c).func_181675_d();
/* 1221 */     vertexbuffer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72338_b, boundingBox.field_72334_f).func_181675_d();
/* 1222 */     vertexbuffer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72338_b, boundingBox.field_72334_f).func_181675_d();
/* 1223 */     vertexbuffer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72338_b, boundingBox.field_72339_c).func_181675_d();
/* 1224 */     tessellator.func_78381_a();
/* 1225 */     vertexbuffer.func_181668_a(3, DefaultVertexFormats.field_181705_e);
/* 1226 */     vertexbuffer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72337_e, boundingBox.field_72339_c).func_181675_d();
/* 1227 */     vertexbuffer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72337_e, boundingBox.field_72339_c).func_181675_d();
/* 1228 */     vertexbuffer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72337_e, boundingBox.field_72334_f).func_181675_d();
/* 1229 */     vertexbuffer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72337_e, boundingBox.field_72334_f).func_181675_d();
/* 1230 */     vertexbuffer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72337_e, boundingBox.field_72339_c).func_181675_d();
/* 1231 */     tessellator.func_78381_a();
/* 1232 */     vertexbuffer.func_181668_a(1, DefaultVertexFormats.field_181705_e);
/* 1233 */     vertexbuffer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72338_b, boundingBox.field_72339_c).func_181675_d();
/* 1234 */     vertexbuffer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72337_e, boundingBox.field_72339_c).func_181675_d();
/* 1235 */     vertexbuffer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72338_b, boundingBox.field_72339_c).func_181675_d();
/* 1236 */     vertexbuffer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72337_e, boundingBox.field_72339_c).func_181675_d();
/* 1237 */     vertexbuffer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72338_b, boundingBox.field_72334_f).func_181675_d();
/* 1238 */     vertexbuffer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72337_e, boundingBox.field_72334_f).func_181675_d();
/* 1239 */     vertexbuffer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72338_b, boundingBox.field_72334_f).func_181675_d();
/* 1240 */     vertexbuffer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72337_e, boundingBox.field_72334_f).func_181675_d();
/* 1241 */     tessellator.func_78381_a();
/*      */   }
/*      */   
/*      */   public static void glrendermethod() {
/* 1245 */     GL11.glEnable(3042);
/* 1246 */     GL11.glBlendFunc(770, 771);
/* 1247 */     GL11.glEnable(2848);
/* 1248 */     GL11.glLineWidth(2.0F);
/* 1249 */     GL11.glDisable(3553);
/* 1250 */     GL11.glEnable(2884);
/* 1251 */     GL11.glDisable(2929);
/* 1252 */     double viewerPosX = (mc.func_175598_ae()).field_78730_l;
/* 1253 */     double viewerPosY = (mc.func_175598_ae()).field_78731_m;
/* 1254 */     double viewerPosZ = (mc.func_175598_ae()).field_78728_n;
/* 1255 */     GL11.glPushMatrix();
/* 1256 */     GL11.glTranslated(-viewerPosX, -viewerPosY, -viewerPosZ);
/*      */   }
/*      */   
/*      */   public static void glStart(float n, float n2, float n3, float n4) {
/* 1260 */     glrendermethod();
/* 1261 */     GL11.glColor4f(n, n2, n3, n4);
/*      */   }
/*      */   
/*      */   public static void glEnd() {
/* 1265 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 1266 */     GL11.glPopMatrix();
/* 1267 */     GL11.glEnable(2929);
/* 1268 */     GL11.glEnable(3553);
/* 1269 */     GL11.glDisable(3042);
/* 1270 */     GL11.glDisable(2848);
/*      */   }
/*      */   
/*      */   public static AxisAlignedBB getBoundingBox(BlockPos blockPos) {
/* 1274 */     return mc.field_71441_e.func_180495_p(blockPos).func_185900_c((IBlockAccess)mc.field_71441_e, blockPos).func_186670_a(blockPos);
/*      */   }
/*      */   
/*      */   public static void drawOutlinedBox(AxisAlignedBB axisAlignedBB) {
/* 1278 */     GL11.glBegin(1);
/* 1279 */     GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
/* 1280 */     GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
/* 1281 */     GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
/* 1282 */     GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
/* 1283 */     GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
/* 1284 */     GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
/* 1285 */     GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
/* 1286 */     GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
/* 1287 */     GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
/* 1288 */     GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
/* 1289 */     GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
/* 1290 */     GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
/* 1291 */     GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
/* 1292 */     GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
/* 1293 */     GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
/* 1294 */     GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
/* 1295 */     GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
/* 1296 */     GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
/* 1297 */     GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
/* 1298 */     GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
/* 1299 */     GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
/* 1300 */     GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
/* 1301 */     GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
/* 1302 */     GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
/* 1303 */     GL11.glEnd();
/*      */   }
/*      */   
/*      */   public static void drawFilledBoxESPN(BlockPos pos, Color color) {
/* 1307 */     AxisAlignedBB bb = new AxisAlignedBB(pos.func_177958_n() - (mc.func_175598_ae()).field_78730_l, pos.func_177956_o() - (mc.func_175598_ae()).field_78731_m, pos.func_177952_p() - (mc.func_175598_ae()).field_78728_n, (pos.func_177958_n() + 1) - (mc.func_175598_ae()).field_78730_l, (pos.func_177956_o() + 1) - (mc.func_175598_ae()).field_78731_m, (pos.func_177952_p() + 1) - (mc.func_175598_ae()).field_78728_n);
/* 1308 */     int rgba = ColorUtil.toRGBA(color);
/* 1309 */     drawFilledBox(bb, rgba);
/*      */   }
/*      */   
/*      */   public static void drawFilledBox(AxisAlignedBB bb, int color) {
/* 1313 */     GlStateManager.func_179094_E();
/* 1314 */     GlStateManager.func_179147_l();
/* 1315 */     GlStateManager.func_179097_i();
/* 1316 */     GlStateManager.func_179120_a(770, 771, 0, 1);
/* 1317 */     GlStateManager.func_179090_x();
/* 1318 */     GlStateManager.func_179132_a(false);
/* 1319 */     float alpha = (color >> 24 & 0xFF) / 255.0F;
/* 1320 */     float red = (color >> 16 & 0xFF) / 255.0F;
/* 1321 */     float green = (color >> 8 & 0xFF) / 255.0F;
/* 1322 */     float blue = (color & 0xFF) / 255.0F;
/* 1323 */     Tessellator tessellator = Tessellator.func_178181_a();
/* 1324 */     BufferBuilder bufferbuilder = tessellator.func_178180_c();
/* 1325 */     bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181706_f);
/* 1326 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1327 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1328 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1329 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1330 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1331 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1332 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1333 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1334 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1335 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1336 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1337 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1338 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1339 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1340 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1341 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1342 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1343 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1344 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1345 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1346 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1347 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1348 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1349 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1350 */     tessellator.func_78381_a();
/* 1351 */     GlStateManager.func_179132_a(true);
/* 1352 */     GlStateManager.func_179126_j();
/* 1353 */     GlStateManager.func_179098_w();
/* 1354 */     GlStateManager.func_179084_k();
/* 1355 */     GlStateManager.func_179121_F();
/*      */   }
/*      */   
/*      */   public static void drawBoundingBox(AxisAlignedBB bb, float width, int color) {
/* 1359 */     GlStateManager.func_179094_E();
/* 1360 */     GlStateManager.func_179147_l();
/* 1361 */     GlStateManager.func_179097_i();
/* 1362 */     GlStateManager.func_179120_a(770, 771, 0, 1);
/* 1363 */     GlStateManager.func_179090_x();
/* 1364 */     GlStateManager.func_179132_a(false);
/* 1365 */     GL11.glEnable(2848);
/* 1366 */     GL11.glHint(3154, 4354);
/* 1367 */     GL11.glLineWidth(width);
/* 1368 */     float alpha = (color >> 24 & 0xFF) / 255.0F;
/* 1369 */     float red = (color >> 16 & 0xFF) / 255.0F;
/* 1370 */     float green = (color >> 8 & 0xFF) / 255.0F;
/* 1371 */     float blue = (color & 0xFF) / 255.0F;
/* 1372 */     Tessellator tessellator = Tessellator.func_178181_a();
/* 1373 */     BufferBuilder bufferbuilder = tessellator.func_178180_c();
/* 1374 */     bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181706_f);
/* 1375 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1376 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1377 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1378 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1379 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1380 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1381 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1382 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1383 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1384 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1385 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1386 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1387 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1388 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1389 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1390 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1391 */     tessellator.func_78381_a();
/* 1392 */     GL11.glDisable(2848);
/* 1393 */     GlStateManager.func_179132_a(true);
/* 1394 */     GlStateManager.func_179126_j();
/* 1395 */     GlStateManager.func_179098_w();
/* 1396 */     GlStateManager.func_179084_k();
/* 1397 */     GlStateManager.func_179121_F();
/*      */   }
/*      */   
/*      */   public static void glBillboard(float x, float y, float z) {
/* 1401 */     float scale = 0.02666667F;
/* 1402 */     GlStateManager.func_179137_b(x - (mc.func_175598_ae()).field_78725_b, y - (mc.func_175598_ae()).field_78726_c, z - (mc.func_175598_ae()).field_78723_d);
/* 1403 */     GlStateManager.func_187432_a(0.0F, 1.0F, 0.0F);
/* 1404 */     GlStateManager.func_179114_b(-mc.field_71439_g.field_70177_z, 0.0F, 1.0F, 0.0F);
/* 1405 */     GlStateManager.func_179114_b(mc.field_71439_g.field_70125_A, (mc.field_71474_y.field_74320_O == 2) ? -1.0F : 1.0F, 0.0F, 0.0F);
/* 1406 */     GlStateManager.func_179152_a(-scale, -scale, scale);
/*      */   }
/*      */   
/*      */   public static void glBillboardDistanceScaled(float x, float y, float z, EntityPlayer player, float scale) {
/* 1410 */     glBillboard(x, y, z);
/* 1411 */     int distance = (int)player.func_70011_f(x, y, z);
/* 1412 */     float scaleDistance = distance / 2.0F / (2.0F + 2.0F - scale);
/* 1413 */     if (scaleDistance < 1.0F) {
/* 1414 */       scaleDistance = 1.0F;
/*      */     }
/* 1416 */     GlStateManager.func_179152_a(scaleDistance, scaleDistance, scaleDistance);
/*      */   }
/*      */   
/*      */   public static void drawColoredBoundingBox(AxisAlignedBB bb, float width, float red, float green, float blue, float alpha) {
/* 1420 */     GlStateManager.func_179094_E();
/* 1421 */     GlStateManager.func_179147_l();
/* 1422 */     GlStateManager.func_179097_i();
/* 1423 */     GlStateManager.func_179120_a(770, 771, 0, 1);
/* 1424 */     GlStateManager.func_179090_x();
/* 1425 */     GlStateManager.func_179132_a(false);
/* 1426 */     GL11.glEnable(2848);
/* 1427 */     GL11.glHint(3154, 4354);
/* 1428 */     GL11.glLineWidth(width);
/* 1429 */     Tessellator tessellator = Tessellator.func_178181_a();
/* 1430 */     BufferBuilder bufferbuilder = tessellator.func_178180_c();
/* 1431 */     bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181706_f);
/* 1432 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, 0.0F).func_181675_d();
/* 1433 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1434 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1435 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1436 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1437 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1438 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1439 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1440 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1441 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1442 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1443 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, 0.0F).func_181675_d();
/* 1444 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1445 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, 0.0F).func_181675_d();
/* 1446 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1447 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, 0.0F).func_181675_d();
/* 1448 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1449 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, 0.0F).func_181675_d();
/* 1450 */     tessellator.func_78381_a();
/* 1451 */     GL11.glDisable(2848);
/* 1452 */     GlStateManager.func_179132_a(true);
/* 1453 */     GlStateManager.func_179126_j();
/* 1454 */     GlStateManager.func_179098_w();
/* 1455 */     GlStateManager.func_179084_k();
/* 1456 */     GlStateManager.func_179121_F();
/*      */   }
/*      */   
/*      */   public static void drawSphere(double x, double y, double z, float size, int slices, int stacks) {
/* 1460 */     Sphere s = new Sphere();
/* 1461 */     GL11.glPushMatrix();
/* 1462 */     GL11.glBlendFunc(770, 771);
/* 1463 */     GL11.glEnable(3042);
/* 1464 */     GL11.glLineWidth(1.2F);
/* 1465 */     GL11.glDisable(3553);
/* 1466 */     GL11.glDisable(2929);
/* 1467 */     GL11.glDepthMask(false);
/* 1468 */     s.setDrawStyle(100013);
/* 1469 */     GL11.glTranslated(x - mc.field_175616_W.field_78725_b, y - mc.field_175616_W.field_78726_c, z - mc.field_175616_W.field_78723_d);
/* 1470 */     s.draw(size, slices, stacks);
/* 1471 */     GL11.glLineWidth(2.0F);
/* 1472 */     GL11.glEnable(3553);
/* 1473 */     GL11.glEnable(2929);
/* 1474 */     GL11.glDepthMask(true);
/* 1475 */     GL11.glDisable(3042);
/* 1476 */     GL11.glPopMatrix();
/*      */   }
/*      */   
/*      */   public static void drawBar(GLUProjection.Projection projection, float width, float height, float totalWidth, Color startColor, Color outlineColor) {
/* 1480 */     if (projection.getType() == GLUProjection.Projection.Type.INSIDE) {
/* 1481 */       GlStateManager.func_179094_E();
/* 1482 */       GlStateManager.func_179137_b(projection.getX(), projection.getY(), 0.0D);
/* 1483 */       drawOutlineRect(-(totalWidth / 2.0F), -(height / 2.0F), totalWidth, height, outlineColor.getRGB());
/* 1484 */       drawRect(-(totalWidth / 2.0F), -(height / 2.0F), width, height, startColor.getRGB());
/* 1485 */       GlStateManager.func_179137_b(-projection.getX(), -projection.getY(), 0.0D);
/* 1486 */       GlStateManager.func_179121_F();
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void drawProjectedText(GLUProjection.Projection projection, float addX, float addY, String text, Color color, boolean shadow) {
/* 1491 */     if (projection.getType() == GLUProjection.Projection.Type.INSIDE) {
/* 1492 */       GlStateManager.func_179094_E();
/* 1493 */       GlStateManager.func_179137_b(projection.getX(), projection.getY(), 0.0D);
/* 1494 */       Phobos.textManager.drawString(text, -(Phobos.textManager.getStringWidth(text) / 2.0F) + addX, addY, color.getRGB(), shadow);
/* 1495 */       GlStateManager.func_179137_b(-projection.getX(), -projection.getY(), 0.0D);
/* 1496 */       GlStateManager.func_179121_F();
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void drawChungusESP(GLUProjection.Projection projection, float width, float height, ResourceLocation location) {
/* 1501 */     if (projection.getType() == GLUProjection.Projection.Type.INSIDE) {
/* 1502 */       GlStateManager.func_179094_E();
/* 1503 */       GlStateManager.func_179137_b(projection.getX(), projection.getY(), 0.0D);
/* 1504 */       mc.func_110434_K().func_110577_a(location);
/* 1505 */       GlStateManager.func_179098_w();
/* 1506 */       GlStateManager.func_179084_k();
/* 1507 */       mc.func_110434_K().func_110577_a(location);
/* 1508 */       drawCompleteImage(0.0F, 0.0F, width, height);
/* 1509 */       mc.func_110434_K().func_147645_c(location);
/* 1510 */       GlStateManager.func_179147_l();
/* 1511 */       GlStateManager.func_179090_x();
/* 1512 */       GlStateManager.func_179137_b(-projection.getX(), -projection.getY(), 0.0D);
/* 1513 */       GlStateManager.func_179121_F();
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void drawCompleteImage(float posX, float posY, float width, float height) {
/* 1518 */     GL11.glPushMatrix();
/* 1519 */     GL11.glTranslatef(posX, posY, 0.0F);
/* 1520 */     GL11.glBegin(7);
/* 1521 */     GL11.glTexCoord2f(0.0F, 0.0F);
/* 1522 */     GL11.glVertex3f(0.0F, 0.0F, 0.0F);
/* 1523 */     GL11.glTexCoord2f(0.0F, 1.0F);
/* 1524 */     GL11.glVertex3f(0.0F, height, 0.0F);
/* 1525 */     GL11.glTexCoord2f(1.0F, 1.0F);
/* 1526 */     GL11.glVertex3f(width, height, 0.0F);
/* 1527 */     GL11.glTexCoord2f(1.0F, 0.0F);
/* 1528 */     GL11.glVertex3f(width, 0.0F, 0.0F);
/* 1529 */     GL11.glEnd();
/* 1530 */     GL11.glPopMatrix();
/*      */   }
/*      */   
/*      */   public static void drawOutlineRect(float x, float y, float w, float h, int color) {
/* 1534 */     float alpha = (color >> 24 & 0xFF) / 255.0F;
/* 1535 */     float red = (color >> 16 & 0xFF) / 255.0F;
/* 1536 */     float green = (color >> 8 & 0xFF) / 255.0F;
/* 1537 */     float blue = (color & 0xFF) / 255.0F;
/* 1538 */     Tessellator tessellator = Tessellator.func_178181_a();
/* 1539 */     BufferBuilder bufferbuilder = tessellator.func_178180_c();
/* 1540 */     GlStateManager.func_179147_l();
/* 1541 */     GlStateManager.func_179090_x();
/* 1542 */     GlStateManager.func_187441_d(1.0F);
/* 1543 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/* 1544 */     bufferbuilder.func_181668_a(2, DefaultVertexFormats.field_181706_f);
/* 1545 */     bufferbuilder.func_181662_b(x, h, 0.0D).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1546 */     bufferbuilder.func_181662_b(w, h, 0.0D).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1547 */     bufferbuilder.func_181662_b(w, y, 0.0D).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1548 */     bufferbuilder.func_181662_b(x, y, 0.0D).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1549 */     tessellator.func_78381_a();
/* 1550 */     GlStateManager.func_179098_w();
/* 1551 */     GlStateManager.func_179084_k();
/*      */   }
/*      */   
/*      */   public static void draw3DRect(float x, float y, float w, float h, Color startColor, Color endColor, float lineWidth) {
/* 1555 */     float alpha = startColor.getAlpha() / 255.0F;
/* 1556 */     float red = startColor.getRed() / 255.0F;
/* 1557 */     float green = startColor.getGreen() / 255.0F;
/* 1558 */     float blue = startColor.getBlue() / 255.0F;
/* 1559 */     float alpha1 = endColor.getAlpha() / 255.0F;
/* 1560 */     float red1 = endColor.getRed() / 255.0F;
/* 1561 */     float green1 = endColor.getGreen() / 255.0F;
/* 1562 */     float blue1 = endColor.getBlue() / 255.0F;
/* 1563 */     Tessellator tessellator = Tessellator.func_178181_a();
/* 1564 */     BufferBuilder bufferbuilder = tessellator.func_178180_c();
/* 1565 */     GlStateManager.func_179147_l();
/* 1566 */     GlStateManager.func_179090_x();
/* 1567 */     GlStateManager.func_187441_d(lineWidth);
/* 1568 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/* 1569 */     bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181706_f);
/* 1570 */     bufferbuilder.func_181662_b(x, h, 0.0D).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1571 */     bufferbuilder.func_181662_b(w, h, 0.0D).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1572 */     bufferbuilder.func_181662_b(w, y, 0.0D).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1573 */     bufferbuilder.func_181662_b(x, y, 0.0D).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1574 */     tessellator.func_78381_a();
/* 1575 */     GlStateManager.func_179098_w();
/* 1576 */     GlStateManager.func_179084_k();
/*      */   }
/*      */   
/*      */   public static void drawClock(float x, float y, float radius, int slices, int loops, float lineWidth, boolean fill, Color color) {
/* 1580 */     Disk disk = new Disk();
/* 1581 */     Date date = new Date();
/* 1582 */     int hourAngle = 180 + -(Calendar.getInstance().get(10) * 30 + Calendar.getInstance().get(12) / 2);
/* 1583 */     int minuteAngle = 180 + -(Calendar.getInstance().get(12) * 6 + Calendar.getInstance().get(13) / 10);
/* 1584 */     int secondAngle = 180 + -(Calendar.getInstance().get(13) * 6);
/* 1585 */     int totalMinutesTime = Calendar.getInstance().get(12);
/* 1586 */     int totalHoursTime = Calendar.getInstance().get(10);
/* 1587 */     if (fill) {
/* 1588 */       GL11.glPushMatrix();
/* 1589 */       GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
/* 1590 */       GL11.glBlendFunc(770, 771);
/* 1591 */       GL11.glEnable(3042);
/* 1592 */       GL11.glLineWidth(lineWidth);
/* 1593 */       GL11.glDisable(3553);
/* 1594 */       disk.setOrientation(100020);
/* 1595 */       disk.setDrawStyle(100012);
/* 1596 */       GL11.glTranslated(x, y, 0.0D);
/* 1597 */       disk.draw(0.0F, radius, slices, loops);
/* 1598 */       GL11.glEnable(3553);
/* 1599 */       GL11.glDisable(3042);
/* 1600 */       GL11.glPopMatrix();
/*      */     } else {
/* 1602 */       GL11.glPushMatrix();
/* 1603 */       GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
/* 1604 */       GL11.glEnable(3042);
/* 1605 */       GL11.glLineWidth(lineWidth);
/* 1606 */       GL11.glDisable(3553);
/* 1607 */       GL11.glBegin(3);
/* 1608 */       ArrayList<Vec2f> hVectors = new ArrayList<>();
/* 1609 */       float hue = (float)(System.currentTimeMillis() % 7200L) / 7200.0F;
/* 1610 */       for (int i = 0; i <= 360; i++) {
/* 1611 */         Vec2f vec = new Vec2f(x + (float)Math.sin(i * Math.PI / 180.0D) * radius, y + (float)Math.cos(i * Math.PI / 180.0D) * radius);
/* 1612 */         hVectors.add(vec);
/*      */       } 
/* 1614 */       Color color1 = new Color(Color.HSBtoRGB(hue, 1.0F, 1.0F));
/* 1615 */       for (int j = 0; j < hVectors.size() - 1; j++) {
/* 1616 */         GL11.glColor4f(color1.getRed() / 255.0F, color1.getGreen() / 255.0F, color1.getBlue() / 255.0F, color1.getAlpha() / 255.0F);
/* 1617 */         GL11.glVertex3d(((Vec2f)hVectors.get(j)).field_189982_i, ((Vec2f)hVectors.get(j)).field_189983_j, 0.0D);
/* 1618 */         GL11.glVertex3d(((Vec2f)hVectors.get(j + 1)).field_189982_i, ((Vec2f)hVectors.get(j + 1)).field_189983_j, 0.0D);
/* 1619 */         color1 = new Color(Color.HSBtoRGB(hue += 0.0027777778F, 1.0F, 1.0F));
/*      */       } 
/* 1621 */       GL11.glEnd();
/* 1622 */       GL11.glEnable(3553);
/* 1623 */       GL11.glDisable(3042);
/* 1624 */       GL11.glPopMatrix();
/*      */     } 
/* 1626 */     drawLine(x, y, x + (float)Math.sin(hourAngle * Math.PI / 180.0D) * radius / 2.0F, y + (float)Math.cos(hourAngle * Math.PI / 180.0D) * radius / 2.0F, 1.0F, Color.WHITE.getRGB());
/* 1627 */     drawLine(x, y, x + (float)Math.sin(minuteAngle * Math.PI / 180.0D) * (radius - radius / 10.0F), y + (float)Math.cos(minuteAngle * Math.PI / 180.0D) * (radius - radius / 10.0F), 1.0F, Color.WHITE.getRGB());
/* 1628 */     drawLine(x, y, x + (float)Math.sin(secondAngle * Math.PI / 180.0D) * (radius - radius / 10.0F), y + (float)Math.cos(secondAngle * Math.PI / 180.0D) * (radius - radius / 10.0F), 1.0F, Color.RED.getRGB());
/*      */   }
/*      */   
/*      */   public static void GLPre(float lineWidth) {
/* 1632 */     depth = GL11.glIsEnabled(2896);
/* 1633 */     texture = GL11.glIsEnabled(3042);
/* 1634 */     clean = GL11.glIsEnabled(3553);
/* 1635 */     bind = GL11.glIsEnabled(2929);
/* 1636 */     override = GL11.glIsEnabled(2848);
/* 1637 */     GLPre(depth, texture, clean, bind, override, lineWidth);
/*      */   }
/*      */   
/*      */   public static void GlPost() {
/* 1641 */     GLPost(depth, texture, clean, bind, override);
/*      */   }
/*      */   
/*      */   private static void GLPre(boolean depth, boolean texture, boolean clean, boolean bind, boolean override, float lineWidth) {
/* 1645 */     if (depth) {
/* 1646 */       GL11.glDisable(2896);
/*      */     }
/* 1648 */     if (!texture) {
/* 1649 */       GL11.glEnable(3042);
/*      */     }
/* 1651 */     GL11.glLineWidth(lineWidth);
/* 1652 */     if (clean) {
/* 1653 */       GL11.glDisable(3553);
/*      */     }
/* 1655 */     if (bind) {
/* 1656 */       GL11.glDisable(2929);
/*      */     }
/* 1658 */     if (!override) {
/* 1659 */       GL11.glEnable(2848);
/*      */     }
/* 1661 */     GlStateManager.func_187401_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
/* 1662 */     GL11.glHint(3154, 4354);
/* 1663 */     GlStateManager.func_179132_a(false);
/*      */   }
/*      */   
/*      */   public static float[][] getBipedRotations(ModelBiped biped) {
/* 1667 */     float[][] rotations = new float[5][];
/* 1668 */     float[] headRotation = { biped.field_78116_c.field_78795_f, biped.field_78116_c.field_78796_g, biped.field_78116_c.field_78808_h };
/* 1669 */     rotations[0] = headRotation;
/* 1670 */     float[] rightArmRotation = { biped.field_178723_h.field_78795_f, biped.field_178723_h.field_78796_g, biped.field_178723_h.field_78808_h };
/* 1671 */     rotations[1] = rightArmRotation;
/* 1672 */     float[] leftArmRotation = { biped.field_178724_i.field_78795_f, biped.field_178724_i.field_78796_g, biped.field_178724_i.field_78808_h };
/* 1673 */     rotations[2] = leftArmRotation;
/* 1674 */     float[] rightLegRotation = { biped.field_178721_j.field_78795_f, biped.field_178721_j.field_78796_g, biped.field_178721_j.field_78808_h };
/* 1675 */     rotations[3] = rightLegRotation;
/* 1676 */     float[] leftLegRotation = { biped.field_178722_k.field_78795_f, biped.field_178722_k.field_78796_g, biped.field_178722_k.field_78808_h };
/* 1677 */     rotations[4] = leftLegRotation;
/* 1678 */     return rotations;
/*      */   }
/*      */   
/*      */   private static void GLPost(boolean depth, boolean texture, boolean clean, boolean bind, boolean override) {
/* 1682 */     GlStateManager.func_179132_a(true);
/* 1683 */     if (!override) {
/* 1684 */       GL11.glDisable(2848);
/*      */     }
/* 1686 */     if (bind) {
/* 1687 */       GL11.glEnable(2929);
/*      */     }
/* 1689 */     if (clean) {
/* 1690 */       GL11.glEnable(3553);
/*      */     }
/* 1692 */     if (!texture) {
/* 1693 */       GL11.glDisable(3042);
/*      */     }
/* 1695 */     if (depth) {
/* 1696 */       GL11.glEnable(2896);
/*      */     }
/*      */   }
/*      */   
/*      */   public static void drawArc(float cx, float cy, float r, float start_angle, float end_angle, int num_segments) {
/* 1701 */     GL11.glBegin(4);
/* 1702 */     int i = (int)(num_segments / 360.0F / start_angle) + 1;
/* 1703 */     while (i <= num_segments / 360.0F / end_angle) {
/* 1704 */       double previousangle = 6.283185307179586D * (i - 1) / num_segments;
/* 1705 */       double angle = 6.283185307179586D * i / num_segments;
/* 1706 */       GL11.glVertex2d(cx, cy);
/* 1707 */       GL11.glVertex2d(cx + Math.cos(angle) * r, cy + Math.sin(angle) * r);
/* 1708 */       GL11.glVertex2d(cx + Math.cos(previousangle) * r, cy + Math.sin(previousangle) * r);
/* 1709 */       i++;
/*      */     } 
/* 1711 */     glEnd();
/*      */   }
/*      */   
/*      */   public static void drawArcOutline(float cx, float cy, float r, float start_angle, float end_angle, int num_segments) {
/* 1715 */     GL11.glBegin(2);
/* 1716 */     int i = (int)(num_segments / 360.0F / start_angle) + 1;
/* 1717 */     while (i <= num_segments / 360.0F / end_angle) {
/* 1718 */       double angle = 6.283185307179586D * i / num_segments;
/* 1719 */       GL11.glVertex2d(cx + Math.cos(angle) * r, cy + Math.sin(angle) * r);
/* 1720 */       i++;
/*      */     } 
/* 1722 */     glEnd();
/*      */   }
/*      */   
/*      */   public static void drawCircleOutline(float x, float y, float radius) {
/* 1726 */     drawCircleOutline(x, y, radius, 0, 360, 40);
/*      */   }
/*      */   
/*      */   public static void drawCircleOutline(float x, float y, float radius, int start, int end, int segments) {
/* 1730 */     drawArcOutline(x, y, radius, start, end, segments);
/*      */   }
/*      */   
/*      */   public static void drawCircle(float x, float y, float radius) {
/* 1734 */     drawCircle(x, y, radius, 0, 360, 64);
/*      */   }
/*      */   
/*      */   public static void drawCircle(float x, float y, float radius, int start, int end, int segments) {
/* 1738 */     drawArc(x, y, radius, start, end, segments);
/*      */   }
/*      */   
/*      */   public static void drawOutlinedRoundedRectangle(int x, int y, int width, int height, float radius, float dR, float dG, float dB, float dA, float outlineWidth) {
/* 1742 */     drawRoundedRectangle(x, y, width, height, radius);
/* 1743 */     GL11.glColor4f(dR, dG, dB, dA);
/* 1744 */     drawRoundedRectangle(x + outlineWidth, y + outlineWidth, width - outlineWidth * 2.0F, height - outlineWidth * 2.0F, radius);
/*      */   }
/*      */   
/*      */   public static void drawRectangle(float x, float y, float width, float height) {
/* 1748 */     GL11.glEnable(3042);
/* 1749 */     GL11.glBlendFunc(770, 771);
/* 1750 */     GL11.glBegin(2);
/* 1751 */     GL11.glVertex2d(width, 0.0D);
/* 1752 */     GL11.glVertex2d(0.0D, 0.0D);
/* 1753 */     GL11.glVertex2d(0.0D, height);
/* 1754 */     GL11.glVertex2d(width, height);
/* 1755 */     glEnd();
/*      */   }
/*      */   
/*      */   public static void drawRectangleXY(float x, float y, float width, float height) {
/* 1759 */     GL11.glEnable(3042);
/* 1760 */     GL11.glBlendFunc(770, 771);
/* 1761 */     GL11.glBegin(2);
/* 1762 */     GL11.glVertex2d((x + width), y);
/* 1763 */     GL11.glVertex2d(x, y);
/* 1764 */     GL11.glVertex2d(x, (y + height));
/* 1765 */     GL11.glVertex2d((x + width), (y + height));
/* 1766 */     glEnd();
/*      */   }
/*      */   
/*      */   public static void drawFilledRectangle(float x, float y, float width, float height) {
/* 1770 */     GL11.glEnable(3042);
/* 1771 */     GL11.glBlendFunc(770, 771);
/* 1772 */     GL11.glBegin(7);
/* 1773 */     GL11.glVertex2d((x + width), y);
/* 1774 */     GL11.glVertex2d(x, y);
/* 1775 */     GL11.glVertex2d(x, (y + height));
/* 1776 */     GL11.glVertex2d((x + width), (y + height));
/* 1777 */     glEnd();
/*      */   }
/*      */   
/*      */   public static Vec3d to2D(double x, double y, double z) {
/* 1781 */     GL11.glGetFloat(2982, modelView);
/* 1782 */     GL11.glGetFloat(2983, projection);
/* 1783 */     GL11.glGetInteger(2978, viewport);
/* 1784 */     boolean result = GLU.gluProject((float)x, (float)y, (float)z, modelView, projection, viewport, screenCoords);
/* 1785 */     if (result) {
/* 1786 */       return new Vec3d(screenCoords.get(0), (Display.getHeight() - screenCoords.get(1)), screenCoords.get(2));
/*      */     }
/* 1788 */     return null;
/*      */   }
/*      */   
/*      */   public static void drawTracerPointer(float x, float y, float size, float widthDiv, float heightDiv, boolean outline, float outlineWidth, int color) {
/* 1792 */     boolean blend = GL11.glIsEnabled(3042);
/* 1793 */     float alpha = (color >> 24 & 0xFF) / 255.0F;
/* 1794 */     GL11.glEnable(3042);
/* 1795 */     GL11.glDisable(3553);
/* 1796 */     GL11.glBlendFunc(770, 771);
/* 1797 */     GL11.glEnable(2848);
/* 1798 */     GL11.glPushMatrix();
/* 1799 */     hexColor(color);
/* 1800 */     GL11.glBegin(7);
/* 1801 */     GL11.glVertex2d(x, y);
/* 1802 */     GL11.glVertex2d((x - size / widthDiv), (y + size));
/* 1803 */     GL11.glVertex2d(x, (y + size / heightDiv));
/* 1804 */     GL11.glVertex2d((x + size / widthDiv), (y + size));
/* 1805 */     GL11.glVertex2d(x, y);
/* 1806 */     GL11.glEnd();
/* 1807 */     if (outline) {
/* 1808 */       GL11.glLineWidth(outlineWidth);
/* 1809 */       GL11.glColor4f(0.0F, 0.0F, 0.0F, alpha);
/* 1810 */       GL11.glBegin(2);
/* 1811 */       GL11.glVertex2d(x, y);
/* 1812 */       GL11.glVertex2d((x - size / widthDiv), (y + size));
/* 1813 */       GL11.glVertex2d(x, (y + size / heightDiv));
/* 1814 */       GL11.glVertex2d((x + size / widthDiv), (y + size));
/* 1815 */       GL11.glVertex2d(x, y);
/* 1816 */       GL11.glEnd();
/*      */     } 
/* 1818 */     GL11.glPopMatrix();
/* 1819 */     GL11.glEnable(3553);
/* 1820 */     if (!blend) {
/* 1821 */       GL11.glDisable(3042);
/*      */     }
/* 1823 */     GL11.glDisable(2848);
/*      */   }
/*      */   
/*      */   public static int getRainbow(int speed, int offset, float s, float b) {
/* 1827 */     float hue = (float)((System.currentTimeMillis() + offset) % speed);
/* 1828 */     return Color.getHSBColor(hue /= speed, s, b).getRGB();
/*      */   }
/*      */   
/*      */   public static void hexColor(int hexColor) {
/* 1832 */     float red = (hexColor >> 16 & 0xFF) / 255.0F;
/* 1833 */     float green = (hexColor >> 8 & 0xFF) / 255.0F;
/* 1834 */     float blue = (hexColor & 0xFF) / 255.0F;
/* 1835 */     float alpha = (hexColor >> 24 & 0xFF) / 255.0F;
/* 1836 */     GL11.glColor4f(red, green, blue, alpha);
/*      */   }
/*      */   
/*      */   public static boolean isInViewFrustrum(Entity entity) {
/* 1840 */     return (isInViewFrustrum(entity.func_174813_aQ()) || entity.field_70158_ak);
/*      */   }
/*      */   
/*      */   public static boolean isInViewFrustrum(AxisAlignedBB bb) {
/* 1844 */     Entity current = Minecraft.func_71410_x().func_175606_aa();
/* 1845 */     frustrum.func_78547_a(current.field_70165_t, current.field_70163_u, current.field_70161_v);
/* 1846 */     return frustrum.func_78546_a(bb);
/*      */   }
/*      */   
/*      */   public static void drawRoundedRectangle(float x, float y, float width, float height, float radius) {
/* 1850 */     GL11.glEnable(3042);
/* 1851 */     drawArc(x + width - radius, y + height - radius, radius, 0.0F, 90.0F, 16);
/* 1852 */     drawArc(x + radius, y + height - radius, radius, 90.0F, 180.0F, 16);
/* 1853 */     drawArc(x + radius, y + radius, radius, 180.0F, 270.0F, 16);
/* 1854 */     drawArc(x + width - radius, y + radius, radius, 270.0F, 360.0F, 16);
/* 1855 */     GL11.glBegin(4);
/* 1856 */     GL11.glVertex2d((x + width - radius), y);
/* 1857 */     GL11.glVertex2d((x + radius), y);
/* 1858 */     GL11.glVertex2d((x + width - radius), (y + radius));
/* 1859 */     GL11.glVertex2d((x + width - radius), (y + radius));
/* 1860 */     GL11.glVertex2d((x + radius), y);
/* 1861 */     GL11.glVertex2d((x + radius), (y + radius));
/* 1862 */     GL11.glVertex2d((x + width), (y + radius));
/* 1863 */     GL11.glVertex2d(x, (y + radius));
/* 1864 */     GL11.glVertex2d(x, (y + height - radius));
/* 1865 */     GL11.glVertex2d((x + width), (y + radius));
/* 1866 */     GL11.glVertex2d(x, (y + height - radius));
/* 1867 */     GL11.glVertex2d((x + width), (y + height - radius));
/* 1868 */     GL11.glVertex2d((x + width - radius), (y + height - radius));
/* 1869 */     GL11.glVertex2d((x + radius), (y + height - radius));
/* 1870 */     GL11.glVertex2d((x + width - radius), (y + height));
/* 1871 */     GL11.glVertex2d((x + width - radius), (y + height));
/* 1872 */     GL11.glVertex2d((x + radius), (y + height - radius));
/* 1873 */     GL11.glVertex2d((x + radius), (y + height));
/* 1874 */     glEnd();
/*      */   }
/*      */   
/*      */   public static void renderOne(float lineWidth) {
/* 1878 */     checkSetupFBO();
/* 1879 */     GL11.glPushAttrib(1048575);
/* 1880 */     GL11.glDisable(3008);
/* 1881 */     GL11.glDisable(3553);
/* 1882 */     GL11.glDisable(2896);
/* 1883 */     GL11.glEnable(3042);
/* 1884 */     GL11.glBlendFunc(770, 771);
/* 1885 */     GL11.glLineWidth(lineWidth);
/* 1886 */     GL11.glEnable(2848);
/* 1887 */     GL11.glEnable(2960);
/* 1888 */     GL11.glClear(1024);
/* 1889 */     GL11.glClearStencil(15);
/* 1890 */     GL11.glStencilFunc(512, 1, 15);
/* 1891 */     GL11.glStencilOp(7681, 7681, 7681);
/* 1892 */     GL11.glPolygonMode(1032, 6913);
/*      */   }
/*      */   
/*      */   public static void renderTwo() {
/* 1896 */     GL11.glStencilFunc(512, 0, 15);
/* 1897 */     GL11.glStencilOp(7681, 7681, 7681);
/* 1898 */     GL11.glPolygonMode(1032, 6914);
/*      */   }
/*      */   
/*      */   public static void renderThree() {
/* 1902 */     GL11.glStencilFunc(514, 1, 15);
/* 1903 */     GL11.glStencilOp(7680, 7680, 7680);
/* 1904 */     GL11.glPolygonMode(1032, 6913);
/*      */   }
/*      */   
/*      */   public static void renderFour(Color color) {
/* 1908 */     setColor(color);
/* 1909 */     GL11.glDepthMask(false);
/* 1910 */     GL11.glDisable(2929);
/* 1911 */     GL11.glEnable(10754);
/* 1912 */     GL11.glPolygonOffset(1.0F, -2000000.0F);
/* 1913 */     OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, 240.0F, 240.0F);
/*      */   }
/*      */   
/*      */   public static void renderFive() {
/* 1917 */     GL11.glPolygonOffset(1.0F, 2000000.0F);
/* 1918 */     GL11.glDisable(10754);
/* 1919 */     GL11.glEnable(2929);
/* 1920 */     GL11.glDepthMask(true);
/* 1921 */     GL11.glDisable(2960);
/* 1922 */     GL11.glDisable(2848);
/* 1923 */     GL11.glHint(3154, 4352);
/* 1924 */     GL11.glEnable(3042);
/* 1925 */     GL11.glEnable(2896);
/* 1926 */     GL11.glEnable(3553);
/* 1927 */     GL11.glEnable(3008);
/* 1928 */     GL11.glPopAttrib();
/*      */   }
/*      */   
/*      */   public static void setColor(Color color) {
/* 1932 */     GL11.glColor4d(color.getRed() / 255.0D, color.getGreen() / 255.0D, color.getBlue() / 255.0D, color.getAlpha() / 255.0D);
/*      */   }
/*      */   
/*      */   public static void checkSetupFBO() {
/* 1936 */     Framebuffer fbo = mc.field_147124_at;
/* 1937 */     if (fbo != null && fbo.field_147624_h > -1) {
/* 1938 */       setupFBO(fbo);
/* 1939 */       fbo.field_147624_h = -1;
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void setupFBO(Framebuffer fbo) {
/* 1944 */     EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.field_147624_h);
/* 1945 */     int stencilDepthBufferID = EXTFramebufferObject.glGenRenderbuffersEXT();
/* 1946 */     EXTFramebufferObject.glBindRenderbufferEXT(36161, stencilDepthBufferID);
/* 1947 */     EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, mc.field_71443_c, mc.field_71440_d);
/* 1948 */     EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencilDepthBufferID);
/* 1949 */     EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencilDepthBufferID);
/*      */   }
/*      */   
/*      */   public static class RenderTesselator
/*      */     extends Tessellator {
/* 1954 */     public static RenderTesselator INSTANCE = new RenderTesselator();
/*      */     
/*      */     public RenderTesselator() {
/* 1957 */       super(2097152);
/*      */     }
/*      */     
/*      */     public static void prepare(int mode) {
/* 1961 */       prepareGL();
/* 1962 */       begin(mode);
/*      */     }
/*      */     
/*      */     public static void prepareGL() {
/* 1966 */       GL11.glBlendFunc(770, 771);
/* 1967 */       GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
/* 1968 */       GlStateManager.func_187441_d(1.5F);
/* 1969 */       GlStateManager.func_179090_x();
/* 1970 */       GlStateManager.func_179132_a(false);
/* 1971 */       GlStateManager.func_179147_l();
/* 1972 */       GlStateManager.func_179097_i();
/* 1973 */       GlStateManager.func_179140_f();
/* 1974 */       GlStateManager.func_179129_p();
/* 1975 */       GlStateManager.func_179141_d();
/* 1976 */       GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
/*      */     }
/*      */     
/*      */     public static void begin(int mode) {
/* 1980 */       INSTANCE.func_178180_c().func_181668_a(mode, DefaultVertexFormats.field_181706_f);
/*      */     }
/*      */     
/*      */     public static void release() {
/* 1984 */       render();
/* 1985 */       releaseGL();
/*      */     }
/*      */     
/*      */     public static void render() {
/* 1989 */       INSTANCE.func_78381_a();
/*      */     }
/*      */     
/*      */     public static void releaseGL() {
/* 1993 */       GlStateManager.func_179089_o();
/* 1994 */       GlStateManager.func_179132_a(true);
/* 1995 */       GlStateManager.func_179098_w();
/* 1996 */       GlStateManager.func_179147_l();
/* 1997 */       GlStateManager.func_179126_j();
/*      */     }
/*      */     
/*      */     public static void drawBox(BlockPos blockPos, int argb, int sides) {
/* 2001 */       int a = argb >>> 24 & 0xFF;
/* 2002 */       int r = argb >>> 16 & 0xFF;
/* 2003 */       int g = argb >>> 8 & 0xFF;
/* 2004 */       int b = argb & 0xFF;
/* 2005 */       drawBox(blockPos, r, g, b, a, sides);
/*      */     }
/*      */     
/*      */     public static void drawBox(float x, float y, float z, int argb, int sides) {
/* 2009 */       int a = argb >>> 24 & 0xFF;
/* 2010 */       int r = argb >>> 16 & 0xFF;
/* 2011 */       int g = argb >>> 8 & 0xFF;
/* 2012 */       int b = argb & 0xFF;
/* 2013 */       drawBox(INSTANCE.func_178180_c(), x, y, z, 1.0F, 1.0F, 1.0F, r, g, b, a, sides);
/*      */     }
/*      */     
/*      */     public static void drawBox(BlockPos blockPos, int r, int g, int b, int a, int sides) {
/* 2017 */       drawBox(INSTANCE.func_178180_c(), blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p(), 1.0F, 1.0F, 1.0F, r, g, b, a, sides);
/*      */     }
/*      */     
/*      */     public static BufferBuilder getBufferBuilder() {
/* 2021 */       return INSTANCE.func_178180_c();
/*      */     }
/*      */     
/*      */     public static void drawBox(BufferBuilder buffer, float x, float y, float z, float w, float h, float d, int r, int g, int b, int a, int sides) {
/* 2025 */       if ((sides & 0x1) != 0) {
/* 2026 */         buffer.func_181662_b((x + w), y, z).func_181669_b(r, g, b, a).func_181675_d();
/* 2027 */         buffer.func_181662_b((x + w), y, (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/* 2028 */         buffer.func_181662_b(x, y, (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/* 2029 */         buffer.func_181662_b(x, y, z).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/* 2031 */       if ((sides & 0x2) != 0) {
/* 2032 */         buffer.func_181662_b((x + w), (y + h), z).func_181669_b(r, g, b, a).func_181675_d();
/* 2033 */         buffer.func_181662_b(x, (y + h), z).func_181669_b(r, g, b, a).func_181675_d();
/* 2034 */         buffer.func_181662_b(x, (y + h), (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/* 2035 */         buffer.func_181662_b((x + w), (y + h), (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/* 2037 */       if ((sides & 0x4) != 0) {
/* 2038 */         buffer.func_181662_b((x + w), y, z).func_181669_b(r, g, b, a).func_181675_d();
/* 2039 */         buffer.func_181662_b(x, y, z).func_181669_b(r, g, b, a).func_181675_d();
/* 2040 */         buffer.func_181662_b(x, (y + h), z).func_181669_b(r, g, b, a).func_181675_d();
/* 2041 */         buffer.func_181662_b((x + w), (y + h), z).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/* 2043 */       if ((sides & 0x8) != 0) {
/* 2044 */         buffer.func_181662_b(x, y, (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/* 2045 */         buffer.func_181662_b((x + w), y, (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/* 2046 */         buffer.func_181662_b((x + w), (y + h), (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/* 2047 */         buffer.func_181662_b(x, (y + h), (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/* 2049 */       if ((sides & 0x10) != 0) {
/* 2050 */         buffer.func_181662_b(x, y, z).func_181669_b(r, g, b, a).func_181675_d();
/* 2051 */         buffer.func_181662_b(x, y, (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/* 2052 */         buffer.func_181662_b(x, (y + h), (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/* 2053 */         buffer.func_181662_b(x, (y + h), z).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/* 2055 */       if ((sides & 0x20) != 0) {
/* 2056 */         buffer.func_181662_b((x + w), y, (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/* 2057 */         buffer.func_181662_b((x + w), y, z).func_181669_b(r, g, b, a).func_181675_d();
/* 2058 */         buffer.func_181662_b((x + w), (y + h), z).func_181669_b(r, g, b, a).func_181675_d();
/* 2059 */         buffer.func_181662_b((x + w), (y + h), (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/*      */     }
/*      */     
/*      */     public static void drawLines(BufferBuilder buffer, float x, float y, float z, float w, float h, float d, int r, int g, int b, int a, int sides) {
/* 2064 */       if ((sides & 0x11) != 0) {
/* 2065 */         buffer.func_181662_b(x, y, z).func_181669_b(r, g, b, a).func_181675_d();
/* 2066 */         buffer.func_181662_b(x, y, (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/* 2068 */       if ((sides & 0x12) != 0) {
/* 2069 */         buffer.func_181662_b(x, (y + h), z).func_181669_b(r, g, b, a).func_181675_d();
/* 2070 */         buffer.func_181662_b(x, (y + h), (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/* 2072 */       if ((sides & 0x21) != 0) {
/* 2073 */         buffer.func_181662_b((x + w), y, z).func_181669_b(r, g, b, a).func_181675_d();
/* 2074 */         buffer.func_181662_b((x + w), y, (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/* 2076 */       if ((sides & 0x22) != 0) {
/* 2077 */         buffer.func_181662_b((x + w), (y + h), z).func_181669_b(r, g, b, a).func_181675_d();
/* 2078 */         buffer.func_181662_b((x + w), (y + h), (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/* 2080 */       if ((sides & 0x5) != 0) {
/* 2081 */         buffer.func_181662_b(x, y, z).func_181669_b(r, g, b, a).func_181675_d();
/* 2082 */         buffer.func_181662_b((x + w), y, z).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/* 2084 */       if ((sides & 0x6) != 0) {
/* 2085 */         buffer.func_181662_b(x, (y + h), z).func_181669_b(r, g, b, a).func_181675_d();
/* 2086 */         buffer.func_181662_b((x + w), (y + h), z).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/* 2088 */       if ((sides & 0x9) != 0) {
/* 2089 */         buffer.func_181662_b(x, y, (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/* 2090 */         buffer.func_181662_b((x + w), y, (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/* 2092 */       if ((sides & 0xA) != 0) {
/* 2093 */         buffer.func_181662_b(x, (y + h), (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/* 2094 */         buffer.func_181662_b((x + w), (y + h), (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/* 2096 */       if ((sides & 0x14) != 0) {
/* 2097 */         buffer.func_181662_b(x, y, z).func_181669_b(r, g, b, a).func_181675_d();
/* 2098 */         buffer.func_181662_b(x, (y + h), z).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/* 2100 */       if ((sides & 0x24) != 0) {
/* 2101 */         buffer.func_181662_b((x + w), y, z).func_181669_b(r, g, b, a).func_181675_d();
/* 2102 */         buffer.func_181662_b((x + w), (y + h), z).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/* 2104 */       if ((sides & 0x18) != 0) {
/* 2105 */         buffer.func_181662_b(x, y, (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/* 2106 */         buffer.func_181662_b(x, (y + h), (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/* 2108 */       if ((sides & 0x28) != 0) {
/* 2109 */         buffer.func_181662_b((x + w), y, (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/* 2110 */         buffer.func_181662_b((x + w), (y + h), (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/*      */     }
/*      */     
/*      */     public static void drawBoundingBox(AxisAlignedBB bb, float width, float red, float green, float blue, float alpha) {
/* 2115 */       GlStateManager.func_179094_E();
/* 2116 */       GlStateManager.func_179147_l();
/* 2117 */       GlStateManager.func_179097_i();
/* 2118 */       GlStateManager.func_179120_a(770, 771, 0, 1);
/* 2119 */       GlStateManager.func_179090_x();
/* 2120 */       GlStateManager.func_179132_a(false);
/* 2121 */       GL11.glEnable(2848);
/* 2122 */       GL11.glHint(3154, 4354);
/* 2123 */       GL11.glLineWidth(width);
/* 2124 */       Tessellator tessellator = Tessellator.func_178181_a();
/* 2125 */       BufferBuilder bufferbuilder = tessellator.func_178180_c();
/* 2126 */       bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181706_f);
/* 2127 */       bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 2128 */       bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 2129 */       bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 2130 */       bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 2131 */       bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 2132 */       bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 2133 */       bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 2134 */       bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 2135 */       bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 2136 */       bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 2137 */       bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 2138 */       bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 2139 */       bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 2140 */       bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 2141 */       bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 2142 */       bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 2143 */       tessellator.func_78381_a();
/* 2144 */       GL11.glDisable(2848);
/* 2145 */       GlStateManager.func_179132_a(true);
/* 2146 */       GlStateManager.func_179126_j();
/* 2147 */       GlStateManager.func_179098_w();
/* 2148 */       GlStateManager.func_179084_k();
/* 2149 */       GlStateManager.func_179121_F();
/*      */     }
/*      */     
/*      */     public static void drawFullBox(AxisAlignedBB bb, BlockPos blockPos, float width, int argb, int alpha2) {
/* 2153 */       int a = argb >>> 24 & 0xFF;
/* 2154 */       int r = argb >>> 16 & 0xFF;
/* 2155 */       int g = argb >>> 8 & 0xFF;
/* 2156 */       int b = argb & 0xFF;
/* 2157 */       drawFullBox(bb, blockPos, width, r, g, b, a, alpha2);
/*      */     }
/*      */     
/*      */     public static void drawFullBox(AxisAlignedBB bb, BlockPos blockPos, float width, int red, int green, int blue, int alpha, int alpha2) {
/* 2161 */       prepare(7);
/* 2162 */       drawBox(blockPos, red, green, blue, alpha, 63);
/* 2163 */       release();
/* 2164 */       drawBoundingBox(bb, width, red, green, blue, alpha2);
/*      */     }
/*      */     
/*      */     public static void drawHalfBox(BlockPos blockPos, int argb, int sides) {
/* 2168 */       int a = argb >>> 24 & 0xFF;
/* 2169 */       int r = argb >>> 16 & 0xFF;
/* 2170 */       int g = argb >>> 8 & 0xFF;
/* 2171 */       int b = argb & 0xFF;
/* 2172 */       drawHalfBox(blockPos, r, g, b, a, sides);
/*      */     }
/*      */     
/*      */     public static void drawHalfBox(BlockPos blockPos, int r, int g, int b, int a, int sides) {
/* 2176 */       drawBox(INSTANCE.func_178180_c(), blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p(), 1.0F, 0.5F, 1.0F, r, g, b, a, sides);
/*      */     }
/*      */   }
/*      */   
/*      */   public static final class GeometryMasks {
/* 2181 */     public static final HashMap<EnumFacing, Integer> FACEMAP = new HashMap<>();
/*      */     
/*      */     static {
/* 2184 */       FACEMAP.put(EnumFacing.DOWN, Integer.valueOf(1));
/* 2185 */       FACEMAP.put(EnumFacing.WEST, Integer.valueOf(16));
/* 2186 */       FACEMAP.put(EnumFacing.NORTH, Integer.valueOf(4));
/* 2187 */       FACEMAP.put(EnumFacing.SOUTH, Integer.valueOf(8));
/* 2188 */       FACEMAP.put(EnumFacing.EAST, Integer.valueOf(32));
/* 2189 */       FACEMAP.put(EnumFacing.UP, Integer.valueOf(2));
/*      */     }
/*      */     
/*      */     public static final class Line {
/*      */       public static final int DOWN_WEST = 17;
/*      */       public static final int UP_WEST = 18;
/*      */       public static final int DOWN_EAST = 33;
/*      */       public static final int UP_EAST = 34;
/*      */       public static final int DOWN_NORTH = 5;
/*      */       public static final int UP_NORTH = 6;
/*      */       public static final int DOWN_SOUTH = 9;
/*      */       public static final int UP_SOUTH = 10;
/*      */       public static final int NORTH_WEST = 20;
/*      */       public static final int NORTH_EAST = 36;
/*      */       public static final int SOUTH_WEST = 24;
/*      */       public static final int SOUTH_EAST = 40;
/*      */       public static final int ALL = 63;
/*      */     }
/*      */     
/*      */     public static final class Quad {
/*      */       public static final int DOWN = 1;
/*      */       public static final int UP = 2;
/*      */       public static final int NORTH = 4;
/*      */       public static final int SOUTH = 8;
/*      */       public static final int WEST = 16;
/*      */       public static final int EAST = 32;
/*      */       public static final int ALL = 63;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobo\\util\RenderUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */