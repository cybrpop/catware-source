/*     */ package me.earth.phobos.features.modules.render;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.event.events.RenderEntityModelEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.client.Colors;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityEnderCrystal;
/*     */ import net.minecraft.network.play.server.SPacketDestroyEntities;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class CrystalScale
/*     */   extends Module
/*     */ {
/*     */   public static CrystalScale INSTANCE;
/*  23 */   public Setting<Boolean> animateScale = register(new Setting("AnimateScale", Boolean.valueOf(false)));
/*  24 */   public Setting<Boolean> chams = register(new Setting("Chams", Boolean.valueOf(false)));
/*  25 */   public Setting<Boolean> throughWalls = register(new Setting("ThroughWalls", Boolean.valueOf(true)));
/*  26 */   public Setting<Boolean> wireframeThroughWalls = register(new Setting("WireThroughWalls", Boolean.valueOf(true)));
/*  27 */   public Setting<Boolean> glint = register(new Setting("Glint", Boolean.valueOf(false), v -> ((Boolean)this.chams.getValue()).booleanValue()));
/*  28 */   public Setting<Boolean> wireframe = register(new Setting("Wireframe", Boolean.valueOf(false)));
/*  29 */   public Setting<Float> scale = register(new Setting("Scale", Float.valueOf(1.0F), Float.valueOf(0.1F), Float.valueOf(10.0F)));
/*  30 */   public Setting<Float> lineWidth = register(new Setting("LineWidth", Float.valueOf(1.0F), Float.valueOf(0.1F), Float.valueOf(3.0F)));
/*  31 */   public Setting<Boolean> colorSync = register(new Setting("Sync", Boolean.valueOf(false)));
/*  32 */   public Setting<Boolean> rainbow = register(new Setting("Rainbow", Boolean.valueOf(false)));
/*  33 */   public Setting<Integer> saturation = register(new Setting("Saturation", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(100), v -> ((Boolean)this.rainbow.getValue()).booleanValue()));
/*  34 */   public Setting<Integer> brightness = register(new Setting("Brightness", Integer.valueOf(100), Integer.valueOf(0), Integer.valueOf(100), v -> ((Boolean)this.rainbow.getValue()).booleanValue()));
/*  35 */   public Setting<Integer> speed = register(new Setting("Speed", Integer.valueOf(40), Integer.valueOf(1), Integer.valueOf(100), v -> ((Boolean)this.rainbow.getValue()).booleanValue()));
/*  36 */   public Setting<Boolean> xqz = register(new Setting("XQZ", Boolean.valueOf(false), v -> (!((Boolean)this.rainbow.getValue()).booleanValue() && ((Boolean)this.throughWalls.getValue()).booleanValue())));
/*  37 */   public Setting<Integer> red = register(new Setting("Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> !((Boolean)this.rainbow.getValue()).booleanValue()));
/*  38 */   public Setting<Integer> green = register(new Setting("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> !((Boolean)this.rainbow.getValue()).booleanValue()));
/*  39 */   public Setting<Integer> blue = register(new Setting("Blue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> !((Boolean)this.rainbow.getValue()).booleanValue()));
/*  40 */   public Setting<Integer> alpha = register(new Setting("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/*  41 */   public Setting<Integer> hiddenRed = register(new Setting("Hidden Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.xqz.getValue()).booleanValue() && !((Boolean)this.rainbow.getValue()).booleanValue())));
/*  42 */   public Setting<Integer> hiddenGreen = register(new Setting("Hidden Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.xqz.getValue()).booleanValue() && !((Boolean)this.rainbow.getValue()).booleanValue())));
/*  43 */   public Setting<Integer> hiddenBlue = register(new Setting("Hidden Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.xqz.getValue()).booleanValue() && !((Boolean)this.rainbow.getValue()).booleanValue())));
/*  44 */   public Setting<Integer> hiddenAlpha = register(new Setting("Hidden Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.xqz.getValue()).booleanValue() && !((Boolean)this.rainbow.getValue()).booleanValue())));
/*  45 */   public Map<EntityEnderCrystal, Float> scaleMap = new ConcurrentHashMap<>();
/*     */   
/*     */   public CrystalScale() {
/*  48 */     super("CrystalModifier", "Modifies crystal rendering in different ways", Module.Category.RENDER, true, false, false);
/*  49 */     INSTANCE = this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  54 */     for (Entity crystal : mc.field_71441_e.field_72996_f) {
/*  55 */       if (!(crystal instanceof EntityEnderCrystal))
/*  56 */         continue;  if (!this.scaleMap.containsKey(crystal)) {
/*  57 */         this.scaleMap.put((EntityEnderCrystal)crystal, Float.valueOf(3.125E-4F));
/*     */       } else {
/*  59 */         this.scaleMap.put((EntityEnderCrystal)crystal, Float.valueOf(((Float)this.scaleMap.get(crystal)).floatValue() + 3.125E-4F));
/*     */       } 
/*  61 */       if (((Float)this.scaleMap.get(crystal)).floatValue() < 0.0625F * ((Float)this.scale.getValue()).floatValue())
/*     */         continue; 
/*  63 */       this.scaleMap.remove(crystal);
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onReceivePacket(PacketEvent.Receive event) {
/*  69 */     if (event.getPacket() instanceof SPacketDestroyEntities) {
/*  70 */       SPacketDestroyEntities packet = (SPacketDestroyEntities)event.getPacket();
/*  71 */       for (int id : packet.func_149098_c()) {
/*  72 */         Entity entity = mc.field_71441_e.func_73045_a(id);
/*  73 */         if (entity instanceof EntityEnderCrystal)
/*  74 */           this.scaleMap.remove(entity); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onRenderModel(RenderEntityModelEvent event) {
/*  80 */     if (event.getStage() != 0 || !(event.entity instanceof EntityEnderCrystal) || !((Boolean)this.wireframe.getValue()).booleanValue()) {
/*     */       return;
/*     */     }
/*  83 */     Color color = ((Boolean)this.colorSync.getValue()).booleanValue() ? Colors.INSTANCE.getCurrentColor() : EntityUtil.getColor(event.entity, ((Integer)this.red.getValue()).intValue(), ((Integer)this.green.getValue()).intValue(), ((Integer)this.blue.getValue()).intValue(), ((Integer)this.alpha.getValue()).intValue(), false);
/*  84 */     boolean fancyGraphics = mc.field_71474_y.field_74347_j;
/*  85 */     mc.field_71474_y.field_74347_j = false;
/*  86 */     float gamma = mc.field_71474_y.field_74333_Y;
/*  87 */     mc.field_71474_y.field_74333_Y = 10000.0F;
/*  88 */     GL11.glPushMatrix();
/*  89 */     GL11.glPushAttrib(1048575);
/*  90 */     GL11.glPolygonMode(1032, 6913);
/*  91 */     GL11.glDisable(3553);
/*  92 */     GL11.glDisable(2896);
/*  93 */     if (((Boolean)this.wireframeThroughWalls.getValue()).booleanValue()) {
/*  94 */       GL11.glDisable(2929);
/*     */     }
/*  96 */     GL11.glEnable(2848);
/*  97 */     GL11.glEnable(3042);
/*  98 */     GlStateManager.func_179112_b(770, 771);
/*  99 */     GlStateManager.func_179131_c(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
/* 100 */     GlStateManager.func_187441_d(((Float)this.lineWidth.getValue()).floatValue());
/* 101 */     event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
/* 102 */     GL11.glPopAttrib();
/* 103 */     GL11.glPopMatrix();
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\render\CrystalScale.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */