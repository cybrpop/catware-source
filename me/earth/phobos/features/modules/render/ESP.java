/*     */ package me.earth.phobos.features.modules.render;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import me.earth.phobos.event.events.Render3DEvent;
/*     */ import me.earth.phobos.event.events.RenderEntityModelEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.client.Colors;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.RenderUtil;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderGlobal;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ESP
/*     */   extends Module
/*     */ {
/*  26 */   private static ESP INSTANCE = new ESP();
/*  27 */   private final Setting<Mode> mode = register(new Setting("Mode", Mode.OUTLINE));
/*  28 */   private final Setting<Boolean> colorSync = register(new Setting("Sync", Boolean.valueOf(false)));
/*  29 */   private final Setting<Boolean> players = register(new Setting("Players", Boolean.valueOf(true)));
/*  30 */   private final Setting<Boolean> animals = register(new Setting("Animals", Boolean.valueOf(false)));
/*  31 */   private final Setting<Boolean> mobs = register(new Setting("Mobs", Boolean.valueOf(false)));
/*  32 */   private final Setting<Boolean> items = register(new Setting("Items", Boolean.valueOf(false)));
/*  33 */   private final Setting<Boolean> xporbs = register(new Setting("XpOrbs", Boolean.valueOf(false)));
/*  34 */   private final Setting<Boolean> xpbottles = register(new Setting("XpBottles", Boolean.valueOf(false)));
/*  35 */   private final Setting<Boolean> pearl = register(new Setting("Pearls", Boolean.valueOf(false)));
/*  36 */   private final Setting<Integer> red = register(new Setting("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/*  37 */   private final Setting<Integer> green = register(new Setting("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/*  38 */   private final Setting<Integer> blue = register(new Setting("Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/*  39 */   private final Setting<Integer> boxAlpha = register(new Setting("BoxAlpha", Integer.valueOf(120), Integer.valueOf(0), Integer.valueOf(255)));
/*  40 */   private final Setting<Integer> alpha = register(new Setting("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/*  41 */   private final Setting<Float> lineWidth = register(new Setting("LineWidth", Float.valueOf(2.0F), Float.valueOf(0.1F), Float.valueOf(5.0F)));
/*  42 */   private final Setting<Boolean> colorFriends = register(new Setting("Friends", Boolean.valueOf(true)));
/*  43 */   private final Setting<Boolean> self = register(new Setting("Self", Boolean.valueOf(true)));
/*  44 */   private final Setting<Boolean> onTop = register(new Setting("onTop", Boolean.valueOf(true)));
/*  45 */   private final Setting<Boolean> invisibles = register(new Setting("Invisibles", Boolean.valueOf(false)));
/*     */   
/*     */   public ESP() {
/*  48 */     super("ESP", "Renders a nice ESP.", Module.Category.RENDER, false, false, false);
/*  49 */     setInstance();
/*     */   }
/*     */   
/*     */   public static ESP getInstance() {
/*  53 */     if (INSTANCE == null) {
/*  54 */       INSTANCE = new ESP();
/*     */     }
/*  56 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  60 */     INSTANCE = this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onRender3D(Render3DEvent event) {
/*  68 */     if (((Boolean)this.items.getValue()).booleanValue()) {
/*  69 */       int i = 0;
/*  70 */       for (Entity entity : mc.field_71441_e.field_72996_f) {
/*  71 */         if (!(entity instanceof net.minecraft.entity.item.EntityItem) || mc.field_71439_g.func_70068_e(entity) >= 2500.0D)
/*  72 */           continue;  Vec3d interp = EntityUtil.getInterpolatedRenderPos(entity, mc.func_184121_ak());
/*  73 */         AxisAlignedBB bb = new AxisAlignedBB((entity.func_174813_aQ()).field_72340_a - 0.05D - entity.field_70165_t + interp.field_72450_a, (entity.func_174813_aQ()).field_72338_b - 0.0D - entity.field_70163_u + interp.field_72448_b, (entity.func_174813_aQ()).field_72339_c - 0.05D - entity.field_70161_v + interp.field_72449_c, (entity.func_174813_aQ()).field_72336_d + 0.05D - entity.field_70165_t + interp.field_72450_a, (entity.func_174813_aQ()).field_72337_e + 0.1D - entity.field_70163_u + interp.field_72448_b, (entity.func_174813_aQ()).field_72334_f + 0.05D - entity.field_70161_v + interp.field_72449_c);
/*  74 */         GlStateManager.func_179094_E();
/*  75 */         GlStateManager.func_179147_l();
/*  76 */         GlStateManager.func_179097_i();
/*  77 */         GlStateManager.func_179120_a(770, 771, 0, 1);
/*  78 */         GlStateManager.func_179090_x();
/*  79 */         GlStateManager.func_179132_a(false);
/*  80 */         GL11.glEnable(2848);
/*  81 */         GL11.glHint(3154, 4354);
/*  82 */         GL11.glLineWidth(1.0F);
/*  83 */         RenderGlobal.func_189696_b(bb, ((Boolean)this.colorSync.getValue()).booleanValue() ? (Colors.INSTANCE.getCurrentColor().getRed() / 255.0F) : (((Integer)this.red.getValue()).intValue() / 255.0F), ((Boolean)this.colorSync.getValue()).booleanValue() ? (Colors.INSTANCE.getCurrentColor().getGreen() / 255.0F) : (((Integer)this.green.getValue()).intValue() / 255.0F), ((Boolean)this.colorSync.getValue()).booleanValue() ? (Colors.INSTANCE.getCurrentColor().getBlue() / 255.0F) : (((Integer)this.blue.getValue()).intValue() / 255.0F), ((Boolean)this.colorSync.getValue()).booleanValue() ? Colors.INSTANCE.getCurrentColor().getAlpha() : (((Integer)this.boxAlpha.getValue()).intValue() / 255.0F));
/*  84 */         GL11.glDisable(2848);
/*  85 */         GlStateManager.func_179132_a(true);
/*  86 */         GlStateManager.func_179126_j();
/*  87 */         GlStateManager.func_179098_w();
/*  88 */         GlStateManager.func_179084_k();
/*  89 */         GlStateManager.func_179121_F();
/*  90 */         RenderUtil.drawBlockOutline(bb, ((Boolean)this.colorSync.getValue()).booleanValue() ? Colors.INSTANCE.getCurrentColor() : new Color(((Integer)this.red.getValue()).intValue(), ((Integer)this.green.getValue()).intValue(), ((Integer)this.blue.getValue()).intValue(), ((Integer)this.alpha.getValue()).intValue()), 1.0F);
/*  91 */         if (++i < 50);
/*     */       } 
/*     */     } 
/*     */     
/*  95 */     if (((Boolean)this.xporbs.getValue()).booleanValue()) {
/*  96 */       int i = 0;
/*  97 */       for (Entity entity : mc.field_71441_e.field_72996_f) {
/*  98 */         if (!(entity instanceof net.minecraft.entity.item.EntityXPOrb) || mc.field_71439_g.func_70068_e(entity) >= 2500.0D)
/*  99 */           continue;  Vec3d interp = EntityUtil.getInterpolatedRenderPos(entity, mc.func_184121_ak());
/* 100 */         AxisAlignedBB bb = new AxisAlignedBB((entity.func_174813_aQ()).field_72340_a - 0.05D - entity.field_70165_t + interp.field_72450_a, (entity.func_174813_aQ()).field_72338_b - 0.0D - entity.field_70163_u + interp.field_72448_b, (entity.func_174813_aQ()).field_72339_c - 0.05D - entity.field_70161_v + interp.field_72449_c, (entity.func_174813_aQ()).field_72336_d + 0.05D - entity.field_70165_t + interp.field_72450_a, (entity.func_174813_aQ()).field_72337_e + 0.1D - entity.field_70163_u + interp.field_72448_b, (entity.func_174813_aQ()).field_72334_f + 0.05D - entity.field_70161_v + interp.field_72449_c);
/* 101 */         GlStateManager.func_179094_E();
/* 102 */         GlStateManager.func_179147_l();
/* 103 */         GlStateManager.func_179097_i();
/* 104 */         GlStateManager.func_179120_a(770, 771, 0, 1);
/* 105 */         GlStateManager.func_179090_x();
/* 106 */         GlStateManager.func_179132_a(false);
/* 107 */         GL11.glEnable(2848);
/* 108 */         GL11.glHint(3154, 4354);
/* 109 */         GL11.glLineWidth(1.0F);
/* 110 */         RenderGlobal.func_189696_b(bb, ((Boolean)this.colorSync.getValue()).booleanValue() ? (Colors.INSTANCE.getCurrentColor().getRed() / 255.0F) : (((Integer)this.red.getValue()).intValue() / 255.0F), ((Boolean)this.colorSync.getValue()).booleanValue() ? (Colors.INSTANCE.getCurrentColor().getGreen() / 255.0F) : (((Integer)this.green.getValue()).intValue() / 255.0F), ((Boolean)this.colorSync.getValue()).booleanValue() ? (Colors.INSTANCE.getCurrentColor().getBlue() / 255.0F) : (((Integer)this.blue.getValue()).intValue() / 255.0F), ((Boolean)this.colorSync.getValue()).booleanValue() ? (Colors.INSTANCE.getCurrentColor().getAlpha() / 255.0F) : (((Integer)this.boxAlpha.getValue()).intValue() / 255.0F));
/* 111 */         GL11.glDisable(2848);
/* 112 */         GlStateManager.func_179132_a(true);
/* 113 */         GlStateManager.func_179126_j();
/* 114 */         GlStateManager.func_179098_w();
/* 115 */         GlStateManager.func_179084_k();
/* 116 */         GlStateManager.func_179121_F();
/* 117 */         RenderUtil.drawBlockOutline(bb, ((Boolean)this.colorSync.getValue()).booleanValue() ? Colors.INSTANCE.getCurrentColor() : new Color(((Integer)this.red.getValue()).intValue(), ((Integer)this.green.getValue()).intValue(), ((Integer)this.blue.getValue()).intValue(), ((Integer)this.alpha.getValue()).intValue()), 1.0F);
/* 118 */         if (++i < 50);
/*     */       } 
/*     */     } 
/*     */     
/* 122 */     if (((Boolean)this.pearl.getValue()).booleanValue()) {
/* 123 */       int i = 0;
/* 124 */       for (Entity entity : mc.field_71441_e.field_72996_f) {
/* 125 */         if (!(entity instanceof net.minecraft.entity.item.EntityEnderPearl) || mc.field_71439_g.func_70068_e(entity) >= 2500.0D)
/* 126 */           continue;  Vec3d interp = EntityUtil.getInterpolatedRenderPos(entity, mc.func_184121_ak());
/* 127 */         AxisAlignedBB bb = new AxisAlignedBB((entity.func_174813_aQ()).field_72340_a - 0.05D - entity.field_70165_t + interp.field_72450_a, (entity.func_174813_aQ()).field_72338_b - 0.0D - entity.field_70163_u + interp.field_72448_b, (entity.func_174813_aQ()).field_72339_c - 0.05D - entity.field_70161_v + interp.field_72449_c, (entity.func_174813_aQ()).field_72336_d + 0.05D - entity.field_70165_t + interp.field_72450_a, (entity.func_174813_aQ()).field_72337_e + 0.1D - entity.field_70163_u + interp.field_72448_b, (entity.func_174813_aQ()).field_72334_f + 0.05D - entity.field_70161_v + interp.field_72449_c);
/* 128 */         GlStateManager.func_179094_E();
/* 129 */         GlStateManager.func_179147_l();
/* 130 */         GlStateManager.func_179097_i();
/* 131 */         GlStateManager.func_179120_a(770, 771, 0, 1);
/* 132 */         GlStateManager.func_179090_x();
/* 133 */         GlStateManager.func_179132_a(false);
/* 134 */         GL11.glEnable(2848);
/* 135 */         GL11.glHint(3154, 4354);
/* 136 */         GL11.glLineWidth(1.0F);
/* 137 */         RenderGlobal.func_189696_b(bb, ((Boolean)this.colorSync.getValue()).booleanValue() ? (Colors.INSTANCE.getCurrentColor().getRed() / 255.0F) : (((Integer)this.red.getValue()).intValue() / 255.0F), ((Boolean)this.colorSync.getValue()).booleanValue() ? (Colors.INSTANCE.getCurrentColor().getGreen() / 255.0F) : (((Integer)this.green.getValue()).intValue() / 255.0F), ((Boolean)this.colorSync.getValue()).booleanValue() ? (Colors.INSTANCE.getCurrentColor().getBlue() / 255.0F) : (((Integer)this.blue.getValue()).intValue() / 255.0F), ((Boolean)this.colorSync.getValue()).booleanValue() ? (Colors.INSTANCE.getCurrentColor().getAlpha() / 255.0F) : (((Integer)this.boxAlpha.getValue()).intValue() / 255.0F));
/* 138 */         GL11.glDisable(2848);
/* 139 */         GlStateManager.func_179132_a(true);
/* 140 */         GlStateManager.func_179126_j();
/* 141 */         GlStateManager.func_179098_w();
/* 142 */         GlStateManager.func_179084_k();
/* 143 */         GlStateManager.func_179121_F();
/* 144 */         RenderUtil.drawBlockOutline(bb, ((Boolean)this.colorSync.getValue()).booleanValue() ? Colors.INSTANCE.getCurrentColor() : new Color(((Integer)this.red.getValue()).intValue(), ((Integer)this.green.getValue()).intValue(), ((Integer)this.blue.getValue()).intValue(), ((Integer)this.alpha.getValue()).intValue()), 1.0F);
/* 145 */         if (++i < 50);
/*     */       } 
/*     */     } 
/*     */     
/* 149 */     if (((Boolean)this.xpbottles.getValue()).booleanValue()) {
/* 150 */       int i = 0;
/* 151 */       for (Entity entity : mc.field_71441_e.field_72996_f) {
/* 152 */         if (!(entity instanceof net.minecraft.entity.item.EntityExpBottle) || mc.field_71439_g.func_70068_e(entity) >= 2500.0D)
/* 153 */           continue;  Vec3d interp = EntityUtil.getInterpolatedRenderPos(entity, mc.func_184121_ak());
/* 154 */         AxisAlignedBB bb = new AxisAlignedBB((entity.func_174813_aQ()).field_72340_a - 0.05D - entity.field_70165_t + interp.field_72450_a, (entity.func_174813_aQ()).field_72338_b - 0.0D - entity.field_70163_u + interp.field_72448_b, (entity.func_174813_aQ()).field_72339_c - 0.05D - entity.field_70161_v + interp.field_72449_c, (entity.func_174813_aQ()).field_72336_d + 0.05D - entity.field_70165_t + interp.field_72450_a, (entity.func_174813_aQ()).field_72337_e + 0.1D - entity.field_70163_u + interp.field_72448_b, (entity.func_174813_aQ()).field_72334_f + 0.05D - entity.field_70161_v + interp.field_72449_c);
/* 155 */         GlStateManager.func_179094_E();
/* 156 */         GlStateManager.func_179147_l();
/* 157 */         GlStateManager.func_179097_i();
/* 158 */         GlStateManager.func_179120_a(770, 771, 0, 1);
/* 159 */         GlStateManager.func_179090_x();
/* 160 */         GlStateManager.func_179132_a(false);
/* 161 */         GL11.glEnable(2848);
/* 162 */         GL11.glHint(3154, 4354);
/* 163 */         GL11.glLineWidth(1.0F);
/* 164 */         RenderGlobal.func_189696_b(bb, ((Boolean)this.colorSync.getValue()).booleanValue() ? (Colors.INSTANCE.getCurrentColor().getRed() / 255.0F) : (((Integer)this.red.getValue()).intValue() / 255.0F), ((Boolean)this.colorSync.getValue()).booleanValue() ? (Colors.INSTANCE.getCurrentColor().getGreen() / 255.0F) : (((Integer)this.green.getValue()).intValue() / 255.0F), ((Boolean)this.colorSync.getValue()).booleanValue() ? (Colors.INSTANCE.getCurrentColor().getBlue() / 255.0F) : (((Integer)this.blue.getValue()).intValue() / 255.0F), ((Boolean)this.colorSync.getValue()).booleanValue() ? (Colors.INSTANCE.getCurrentColor().getAlpha() / 255.0F) : (((Integer)this.boxAlpha.getValue()).intValue() / 255.0F));
/* 165 */         GL11.glDisable(2848);
/* 166 */         GlStateManager.func_179132_a(true);
/* 167 */         GlStateManager.func_179126_j();
/* 168 */         GlStateManager.func_179098_w();
/* 169 */         GlStateManager.func_179084_k();
/* 170 */         GlStateManager.func_179121_F();
/* 171 */         RenderUtil.drawBlockOutline(bb, ((Boolean)this.colorSync.getValue()).booleanValue() ? Colors.INSTANCE.getCurrentColor() : new Color(((Integer)this.red.getValue()).intValue(), ((Integer)this.green.getValue()).intValue(), ((Integer)this.blue.getValue()).intValue(), ((Integer)this.alpha.getValue()).intValue()), 1.0F);
/* 172 */         if (++i < 50);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRenderModel(RenderEntityModelEvent event) {
/* 179 */     if (event.getStage() != 0 || event.entity == null || (event.entity.func_82150_aj() && !((Boolean)this.invisibles.getValue()).booleanValue()) || (!((Boolean)this.self.getValue()).booleanValue() && event.entity.equals(mc.field_71439_g)) || (!((Boolean)this.players.getValue()).booleanValue() && event.entity instanceof net.minecraft.entity.player.EntityPlayer) || (!((Boolean)this.animals.getValue()).booleanValue() && EntityUtil.isPassive(event.entity)) || (!((Boolean)this.mobs.getValue()).booleanValue() && !EntityUtil.isPassive(event.entity) && !(event.entity instanceof net.minecraft.entity.player.EntityPlayer))) {
/*     */       return;
/*     */     }
/* 182 */     Color color = ((Boolean)this.colorSync.getValue()).booleanValue() ? Colors.INSTANCE.getCurrentColor() : EntityUtil.getColor(event.entity, ((Integer)this.red.getValue()).intValue(), ((Integer)this.green.getValue()).intValue(), ((Integer)this.blue.getValue()).intValue(), ((Integer)this.alpha.getValue()).intValue(), ((Boolean)this.colorFriends.getValue()).booleanValue());
/* 183 */     boolean fancyGraphics = mc.field_71474_y.field_74347_j;
/* 184 */     mc.field_71474_y.field_74347_j = false;
/* 185 */     float gamma = mc.field_71474_y.field_74333_Y;
/* 186 */     mc.field_71474_y.field_74333_Y = 10000.0F;
/* 187 */     if (((Boolean)this.onTop.getValue()).booleanValue() && (!Chams.getInstance().isEnabled() || !((Boolean)(Chams.getInstance()).colored.getValue()).booleanValue())) {
/* 188 */       event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
/*     */     }
/* 190 */     if (this.mode.getValue() == Mode.OUTLINE) {
/* 191 */       RenderUtil.renderOne(((Float)this.lineWidth.getValue()).floatValue());
/* 192 */       event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
/* 193 */       GlStateManager.func_187441_d(((Float)this.lineWidth.getValue()).floatValue());
/* 194 */       RenderUtil.renderTwo();
/* 195 */       event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
/* 196 */       GlStateManager.func_187441_d(((Float)this.lineWidth.getValue()).floatValue());
/* 197 */       RenderUtil.renderThree();
/* 198 */       RenderUtil.renderFour(color);
/* 199 */       event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
/* 200 */       GlStateManager.func_187441_d(((Float)this.lineWidth.getValue()).floatValue());
/* 201 */       RenderUtil.renderFive();
/*     */     } else {
/* 203 */       GL11.glPushMatrix();
/* 204 */       GL11.glPushAttrib(1048575);
/* 205 */       if (this.mode.getValue() == Mode.WIREFRAME) {
/* 206 */         GL11.glPolygonMode(1032, 6913);
/*     */       } else {
/* 208 */         GL11.glPolygonMode(1028, 6913);
/*     */       } 
/* 210 */       GL11.glDisable(3553);
/* 211 */       GL11.glDisable(2896);
/* 212 */       GL11.glDisable(2929);
/* 213 */       GL11.glEnable(2848);
/* 214 */       GL11.glEnable(3042);
/* 215 */       GlStateManager.func_179112_b(770, 771);
/* 216 */       GlStateManager.func_179131_c(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
/* 217 */       GlStateManager.func_187441_d(((Float)this.lineWidth.getValue()).floatValue());
/* 218 */       event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
/* 219 */       GL11.glPopAttrib();
/* 220 */       GL11.glPopMatrix();
/*     */     } 
/* 222 */     if (!((Boolean)this.onTop.getValue()).booleanValue() && (!Chams.getInstance().isEnabled() || !((Boolean)(Chams.getInstance()).colored.getValue()).booleanValue())) {
/* 223 */       event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
/*     */     }
/*     */     try {
/* 226 */       mc.field_71474_y.field_74347_j = fancyGraphics;
/* 227 */       mc.field_71474_y.field_74333_Y = gamma;
/* 228 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/* 231 */     event.setCanceled(true);
/*     */   }
/*     */   
/*     */   public enum Mode {
/* 235 */     WIREFRAME,
/* 236 */     OUTLINE;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\render\ESP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */