/*     */ package me.earth.phobos.features.modules.render;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import me.earth.phobos.event.events.ConnectionEvent;
/*     */ import me.earth.phobos.event.events.Render3DEvent;
/*     */ import me.earth.phobos.features.command.Command;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.client.Colors;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.ColorUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.RenderUtil;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class LogoutSpots
/*     */   extends Module
/*     */ {
/*  26 */   private final Setting<Boolean> colorSync = register(new Setting("Sync", Boolean.valueOf(false)));
/*  27 */   private final Setting<Integer> red = register(new Setting("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/*  28 */   private final Setting<Integer> green = register(new Setting("Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255)));
/*  29 */   private final Setting<Integer> blue = register(new Setting("Blue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255)));
/*  30 */   private final Setting<Integer> alpha = register(new Setting("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/*  31 */   private final Setting<Boolean> scaleing = register(new Setting("Scale", Boolean.valueOf(false)));
/*  32 */   private final Setting<Float> scaling = register(new Setting("Size", Float.valueOf(4.0F), Float.valueOf(0.1F), Float.valueOf(20.0F)));
/*  33 */   private final Setting<Float> factor = register(new Setting("Factor", Float.valueOf(0.3F), Float.valueOf(0.1F), Float.valueOf(1.0F), v -> ((Boolean)this.scaleing.getValue()).booleanValue()));
/*  34 */   private final Setting<Boolean> smartScale = register(new Setting("SmartScale", Boolean.valueOf(false), v -> ((Boolean)this.scaleing.getValue()).booleanValue()));
/*  35 */   private final Setting<Boolean> rect = register(new Setting("Rectangle", Boolean.valueOf(true)));
/*  36 */   private final Setting<Boolean> coords = register(new Setting("Coords", Boolean.valueOf(true)));
/*  37 */   private final Setting<Boolean> notification = register(new Setting("Notification", Boolean.valueOf(true)));
/*  38 */   private final List<LogoutPos> spots = new CopyOnWriteArrayList<>();
/*  39 */   public Setting<Float> range = register(new Setting("Range", Float.valueOf(300.0F), Float.valueOf(50.0F), Float.valueOf(500.0F)));
/*  40 */   public Setting<Boolean> message = register(new Setting("Message", Boolean.valueOf(false)));
/*     */   
/*     */   public LogoutSpots() {
/*  43 */     super("LogoutSpots", "Renders LogoutSpots", Module.Category.RENDER, true, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLogout() {
/*  48 */     this.spots.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  53 */     this.spots.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onRender3D(Render3DEvent event) {
/*  61 */     if (!this.spots.isEmpty()) {
/*  62 */       List<LogoutPos> list = this.spots;
/*  63 */       synchronized (list) {
/*  64 */         this.spots.forEach(spot -> {
/*     */               if (spot.getEntity() != null) {
/*     */                 AxisAlignedBB bb = RenderUtil.interpolateAxis(spot.getEntity().func_174813_aQ());
/*     */                 RenderUtil.drawBlockOutline(bb, ((Boolean)this.colorSync.getValue()).booleanValue() ? Colors.INSTANCE.getCurrentColor() : new Color(((Integer)this.red.getValue()).intValue(), ((Integer)this.green.getValue()).intValue(), ((Integer)this.blue.getValue()).intValue(), ((Integer)this.alpha.getValue()).intValue()), 1.0F);
/*     */                 double x = interpolate((spot.getEntity()).field_70142_S, (spot.getEntity()).field_70165_t, event.getPartialTicks()) - (mc.func_175598_ae()).field_78725_b;
/*     */                 double y = interpolate((spot.getEntity()).field_70137_T, (spot.getEntity()).field_70163_u, event.getPartialTicks()) - (mc.func_175598_ae()).field_78726_c;
/*     */                 double z = interpolate((spot.getEntity()).field_70136_U, (spot.getEntity()).field_70161_v, event.getPartialTicks()) - (mc.func_175598_ae()).field_78723_d;
/*     */                 renderNameTag(spot.getName(), x, y, z, event.getPartialTicks(), spot.getX(), spot.getY(), spot.getZ());
/*     */               } 
/*     */             });
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  80 */     if (!fullNullCheck()) {
/*  81 */       this.spots.removeIf(spot -> (mc.field_71439_g.func_70068_e((Entity)spot.getEntity()) >= MathUtil.square(((Float)this.range.getValue()).floatValue())));
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onConnection(ConnectionEvent event) {
/*  87 */     if (event.getStage() == 0) {
/*  88 */       UUID uuid = event.getUuid();
/*  89 */       EntityPlayer entity = mc.field_71441_e.func_152378_a(uuid);
/*  90 */       if (entity != null && ((Boolean)this.message.getValue()).booleanValue()) {
/*  91 */         Command.sendMessage("§a" + entity.func_70005_c_() + " just logged in" + (((Boolean)this.coords.getValue()).booleanValue() ? (" at (" + (int)entity.field_70165_t + ", " + (int)entity.field_70163_u + ", " + (int)entity.field_70161_v + ")!") : "!"), ((Boolean)this.notification.getValue()).booleanValue());
/*     */       }
/*  93 */       this.spots.removeIf(pos -> pos.getName().equalsIgnoreCase(event.getName()));
/*  94 */     } else if (event.getStage() == 1) {
/*  95 */       EntityPlayer entity = event.getEntity();
/*  96 */       UUID uuid = event.getUuid();
/*  97 */       String name = event.getName();
/*  98 */       if (((Boolean)this.message.getValue()).booleanValue()) {
/*  99 */         Command.sendMessage("§c" + event.getName() + " just logged out" + (((Boolean)this.coords.getValue()).booleanValue() ? (" at (" + (int)entity.field_70165_t + ", " + (int)entity.field_70163_u + ", " + (int)entity.field_70161_v + ")!") : "!"), ((Boolean)this.notification.getValue()).booleanValue());
/*     */       }
/* 101 */       if (name != null && entity != null && uuid != null) {
/* 102 */         this.spots.add(new LogoutPos(name, uuid, entity));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void renderNameTag(String name, double x, double yi, double z, float delta, double xPos, double yPos, double zPos) {
/* 108 */     double y = yi + 0.7D;
/* 109 */     Entity camera = mc.func_175606_aa();
/* 110 */     assert camera != null;
/* 111 */     double originalPositionX = camera.field_70165_t;
/* 112 */     double originalPositionY = camera.field_70163_u;
/* 113 */     double originalPositionZ = camera.field_70161_v;
/* 114 */     camera.field_70165_t = interpolate(camera.field_70169_q, camera.field_70165_t, delta);
/* 115 */     camera.field_70163_u = interpolate(camera.field_70167_r, camera.field_70163_u, delta);
/* 116 */     camera.field_70161_v = interpolate(camera.field_70166_s, camera.field_70161_v, delta);
/* 117 */     String displayTag = name + " XYZ: " + (int)xPos + ", " + (int)yPos + ", " + (int)zPos;
/* 118 */     double distance = camera.func_70011_f(x + (mc.func_175598_ae()).field_78730_l, y + (mc.func_175598_ae()).field_78731_m, z + (mc.func_175598_ae()).field_78728_n);
/* 119 */     int width = this.renderer.getStringWidth(displayTag) / 2;
/* 120 */     double scale = (0.0018D + ((Float)this.scaling.getValue()).floatValue() * distance * ((Float)this.factor.getValue()).floatValue()) / 1000.0D;
/* 121 */     if (distance <= 8.0D && ((Boolean)this.smartScale.getValue()).booleanValue()) {
/* 122 */       scale = 0.0245D;
/*     */     }
/* 124 */     if (!((Boolean)this.scaleing.getValue()).booleanValue()) {
/* 125 */       scale = ((Float)this.scaling.getValue()).floatValue() / 100.0D;
/*     */     }
/* 127 */     GlStateManager.func_179094_E();
/* 128 */     RenderHelper.func_74519_b();
/* 129 */     GlStateManager.func_179088_q();
/* 130 */     GlStateManager.func_179136_a(1.0F, -1500000.0F);
/* 131 */     GlStateManager.func_179140_f();
/* 132 */     GlStateManager.func_179109_b((float)x, (float)y + 1.4F, (float)z);
/* 133 */     GlStateManager.func_179114_b(-(mc.func_175598_ae()).field_78735_i, 0.0F, 1.0F, 0.0F);
/* 134 */     GlStateManager.func_179114_b((mc.func_175598_ae()).field_78732_j, (mc.field_71474_y.field_74320_O == 2) ? -1.0F : 1.0F, 0.0F, 0.0F);
/* 135 */     GlStateManager.func_179139_a(-scale, -scale, scale);
/* 136 */     GlStateManager.func_179097_i();
/* 137 */     GlStateManager.func_179147_l();
/* 138 */     GlStateManager.func_179147_l();
/* 139 */     if (((Boolean)this.rect.getValue()).booleanValue()) {
/* 140 */       RenderUtil.drawRect((-width - 2), -(this.renderer.getFontHeight() + 1), width + 2.0F, 1.5F, 1426063360);
/*     */     }
/* 142 */     GlStateManager.func_179084_k();
/* 143 */     this.renderer.drawStringWithShadow(displayTag, -width, -(this.renderer.getFontHeight() - 1), ((Boolean)this.colorSync.getValue()).booleanValue() ? Colors.INSTANCE.getCurrentColorHex() : ColorUtil.toRGBA(new Color(((Integer)this.red.getValue()).intValue(), ((Integer)this.green.getValue()).intValue(), ((Integer)this.blue.getValue()).intValue(), ((Integer)this.alpha.getValue()).intValue())));
/* 144 */     camera.field_70165_t = originalPositionX;
/* 145 */     camera.field_70163_u = originalPositionY;
/* 146 */     camera.field_70161_v = originalPositionZ;
/* 147 */     GlStateManager.func_179126_j();
/* 148 */     GlStateManager.func_179084_k();
/* 149 */     GlStateManager.func_179113_r();
/* 150 */     GlStateManager.func_179136_a(1.0F, 1500000.0F);
/* 151 */     GlStateManager.func_179121_F();
/*     */   }
/*     */   
/*     */   private double interpolate(double previous, double current, float delta) {
/* 155 */     return previous + (current - previous) * delta;
/*     */   }
/*     */   
/*     */   private static class LogoutPos {
/*     */     private final String name;
/*     */     private final UUID uuid;
/*     */     private final EntityPlayer entity;
/*     */     private final double x;
/*     */     private final double y;
/*     */     private final double z;
/*     */     
/*     */     public LogoutPos(String name, UUID uuid, EntityPlayer entity) {
/* 167 */       this.name = name;
/* 168 */       this.uuid = uuid;
/* 169 */       this.entity = entity;
/* 170 */       this.x = entity.field_70165_t;
/* 171 */       this.y = entity.field_70163_u;
/* 172 */       this.z = entity.field_70161_v;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 176 */       return this.name;
/*     */     }
/*     */     
/*     */     public UUID getUuid() {
/* 180 */       return this.uuid;
/*     */     }
/*     */     
/*     */     public EntityPlayer getEntity() {
/* 184 */       return this.entity;
/*     */     }
/*     */     
/*     */     public double getX() {
/* 188 */       return this.x;
/*     */     }
/*     */     
/*     */     public double getY() {
/* 192 */       return this.y;
/*     */     }
/*     */     
/*     */     public double getZ() {
/* 196 */       return this.z;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\render\LogoutSpots.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */