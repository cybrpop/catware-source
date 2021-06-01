/*     */ package me.earth.phobos.features.modules.render;
/*     */ 
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.EntityUtil2;
/*     */ import me.earth.phobos.util.VectorUtils;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.culling.Frustum;
/*     */ import net.minecraft.client.renderer.culling.ICamera;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraftforge.client.event.RenderGameOverlayEvent;
/*     */ import net.minecraftforge.client.event.RenderPlayerEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.EventPriority;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ImageESP
/*     */   extends Module
/*     */ {
/*  41 */   public Setting<Boolean> noRenderPlayers = register(new Setting("No Render Players", Boolean.valueOf(false)));
/*     */   private ResourceLocation waifu;
/*     */   private ICamera camera;
/*     */   
/*     */   public ImageESP() {
/*  46 */     super("ImageESP", "just a nice module kek", Module.Category.RENDER, true, true, false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  64 */     this.camera = (ICamera)new Frustum();
/*     */   } private <T> BufferedImage getImage(T source, ThrowingFunction<T, BufferedImage> readFunction) { try { return readFunction.apply(source); }
/*     */     catch (IOException ex)
/*     */     { ex.printStackTrace(); return null; }
/*  68 */      } @SubscribeEvent(priority = EventPriority.LOWEST) public void onRenderGameOverlayEvent(RenderGameOverlayEvent.Text event) { if (this.waifu == null) {
/*     */       return;
/*     */     }
/*  71 */     double d3 = mc.field_71439_g.field_70142_S + (mc.field_71439_g.field_70165_t - mc.field_71439_g.field_70142_S) * event.getPartialTicks();
/*  72 */     double d4 = mc.field_71439_g.field_70137_T + (mc.field_71439_g.field_70163_u - mc.field_71439_g.field_70137_T) * event.getPartialTicks();
/*  73 */     double d5 = mc.field_71439_g.field_70136_U + (mc.field_71439_g.field_70161_v - mc.field_71439_g.field_70136_U) * event.getPartialTicks();
/*     */     
/*  75 */     this.camera.func_78547_a(d3, d4, d5);
/*  76 */     List<EntityPlayer> players = new ArrayList<>(mc.field_71441_e.field_73010_i);
/*  77 */     players.sort(Comparator.<EntityPlayer, Comparable>comparing(entityPlayer -> Float.valueOf(mc.field_71439_g.func_70032_d((Entity)entityPlayer))).reversed());
/*  78 */     for (EntityPlayer player : players) {
/*  79 */       if (player != mc.func_175606_aa() && player.func_70089_S() && this.camera.func_78546_a(player.func_174813_aQ())) {
/*  80 */         EntityPlayer entityPlayer = player;
/*  81 */         Vec3d bottomVec = EntityUtil2.getInterpolatedPos((Entity)entityPlayer, event.getPartialTicks());
/*  82 */         Vec3d topVec = bottomVec.func_178787_e(new Vec3d(0.0D, (player.func_184177_bl()).field_72337_e - player.field_70163_u, 0.0D));
/*  83 */         VectorUtils.ScreenPos top = VectorUtils._toScreen(topVec.field_72450_a, topVec.field_72448_b, topVec.field_72449_c);
/*  84 */         VectorUtils.ScreenPos bot = VectorUtils._toScreen(bottomVec.field_72450_a, bottomVec.field_72448_b, bottomVec.field_72449_c);
/*  85 */         if (!top.isVisible && !bot.isVisible) {
/*     */           continue;
/*     */         }
/*     */         
/*  89 */         int width = bot.y - top.y, height = width;
/*  90 */         int x = (int)(top.x - width / 1.8D);
/*  91 */         int y = top.y;
/*  92 */         mc.field_71446_o.func_110577_a(this.waifu);
/*  93 */         GlStateManager.func_179124_c(255.0F, 255.0F, 255.0F);
/*  94 */         Gui.func_152125_a(x, y, 0.0F, 0.0F, width, height, width, height, width, height);
/*     */       } 
/*     */     }  }
/*     */    private boolean shouldDraw(EntityLivingBase entity) {
/*     */     return (!entity.equals(mc.field_71439_g) && entity.func_110143_aJ() > 0.0F && EntityUtil2.isPlayer((Entity)entity));
/*     */   } @SubscribeEvent
/*     */   public void onRenderPlayer(RenderPlayerEvent.Pre event) {
/* 101 */     if (((Boolean)this.noRenderPlayers.getValue()).booleanValue() && !event.getEntity().equals(mc.field_71439_g)) {
/* 102 */       event.setCanceled(true);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private InputStream getFile(String string) {
/* 113 */     return ImageESP.class.getResourceAsStream(string);
/*     */   }
/*     */   
/*     */   private enum CachedImage {
/* 117 */     CAT("/images/cat.png"),
/* 118 */     STEVEN("/images/stevengg.png"),
/* 119 */     JUICE("/images/juicee.png");
/*     */     
/*     */     String name;
/*     */     
/*     */     CachedImage(String name) {
/* 124 */       this.name = name;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 128 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface ThrowingFunction<T, R> {
/*     */     R apply(T param1T) throws IOException;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\render\ImageESP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */