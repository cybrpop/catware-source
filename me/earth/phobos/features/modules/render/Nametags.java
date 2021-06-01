/*     */ package me.earth.phobos.features.modules.render;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.Objects;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.Render3DEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.client.Colors;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.DamageUtil;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.RotationUtil;
/*     */ import net.minecraft.client.network.NetHandlerPlayClient;
/*     */ import net.minecraft.client.renderer.BufferBuilder;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.enchantment.Enchantment;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.util.text.TextFormatting;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ 
/*     */ public class Nametags
/*     */   extends Module
/*     */ {
/*  32 */   private static Nametags INSTANCE = new Nametags();
/*  33 */   private final Setting<Boolean> health = register(new Setting("Health", Boolean.valueOf(true)));
/*  34 */   private final Setting<Boolean> armor = register(new Setting("Armor", Boolean.valueOf(true)));
/*  35 */   private final Setting<Float> scaling = register(new Setting("Size", Float.valueOf(0.3F), Float.valueOf(0.1F), Float.valueOf(20.0F)));
/*  36 */   private final Setting<Boolean> invisibles = register(new Setting("Invisibles", Boolean.valueOf(false)));
/*  37 */   private final Setting<Boolean> ping = register(new Setting("Ping", Boolean.valueOf(true)));
/*  38 */   private final Setting<Boolean> totemPops = register(new Setting("TotemPops", Boolean.valueOf(true)));
/*  39 */   private final Setting<Boolean> gamemode = register(new Setting("Gamemode", Boolean.valueOf(false)));
/*  40 */   private final Setting<Boolean> entityID = register(new Setting("ID", Boolean.valueOf(false)));
/*  41 */   private final Setting<Boolean> rect = register(new Setting("Rectangle", Boolean.valueOf(true)));
/*  42 */   private final Setting<Boolean> outline = register(new Setting("Outline", Boolean.valueOf(false), v -> ((Boolean)this.rect.getValue()).booleanValue()));
/*  43 */   private final Setting<Boolean> colorSync = register(new Setting("Sync", Boolean.valueOf(false), v -> ((Boolean)this.outline.getValue()).booleanValue()));
/*  44 */   private final Setting<Integer> redSetting = register(new Setting("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.outline.getValue()).booleanValue()));
/*  45 */   private final Setting<Integer> greenSetting = register(new Setting("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.outline.getValue()).booleanValue()));
/*  46 */   private final Setting<Integer> blueSetting = register(new Setting("Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.outline.getValue()).booleanValue()));
/*  47 */   private final Setting<Integer> alphaSetting = register(new Setting("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.outline.getValue()).booleanValue()));
/*  48 */   private final Setting<Float> lineWidth = register(new Setting("LineWidth", Float.valueOf(1.5F), Float.valueOf(0.1F), Float.valueOf(5.0F), v -> ((Boolean)this.outline.getValue()).booleanValue()));
/*  49 */   private final Setting<Boolean> sneak = register(new Setting("SneakColor", Boolean.valueOf(false)));
/*  50 */   private final Setting<Boolean> heldStackName = register(new Setting("StackName", Boolean.valueOf(false)));
/*  51 */   private final Setting<Boolean> whiter = register(new Setting("White", Boolean.valueOf(false)));
/*  52 */   private final Setting<Boolean> onlyFov = register(new Setting("OnlyFov", Boolean.valueOf(false)));
/*  53 */   private final Setting<Boolean> scaleing = register(new Setting("Scale", Boolean.valueOf(false)));
/*  54 */   private final Setting<Float> factor = register(new Setting("Factor", Float.valueOf(0.3F), Float.valueOf(0.1F), Float.valueOf(1.0F), v -> ((Boolean)this.scaleing.getValue()).booleanValue()));
/*  55 */   private final Setting<Boolean> smartScale = register(new Setting("SmartScale", Boolean.valueOf(false), v -> ((Boolean)this.scaleing.getValue()).booleanValue()));
/*     */   
/*     */   public Nametags() {
/*  58 */     super("Nametags", "Better Nametags", Module.Category.RENDER, false, false, false);
/*  59 */     setInstance();
/*     */   }
/*     */   
/*     */   public static Nametags getInstance() {
/*  63 */     if (INSTANCE == null) {
/*  64 */       INSTANCE = new Nametags();
/*     */     }
/*  66 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  70 */     INSTANCE = this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender3D(Render3DEvent event) {
/*  75 */     if (!fullNullCheck()) {
/*  76 */       for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/*  77 */         if (player == null || player.equals(mc.field_71439_g) || !player.func_70089_S() || (player.func_82150_aj() && !((Boolean)this.invisibles.getValue()).booleanValue()) || (((Boolean)this.onlyFov.getValue()).booleanValue() && !RotationUtil.isInFov((Entity)player)))
/*     */           continue; 
/*  79 */         double x = interpolate(player.field_70142_S, player.field_70165_t, event.getPartialTicks()) - (mc.func_175598_ae()).field_78725_b;
/*  80 */         double y = interpolate(player.field_70137_T, player.field_70163_u, event.getPartialTicks()) - (mc.func_175598_ae()).field_78726_c;
/*  81 */         double z = interpolate(player.field_70136_U, player.field_70161_v, event.getPartialTicks()) - (mc.func_175598_ae()).field_78723_d;
/*  82 */         renderNameTag(player, x, y, z, event.getPartialTicks());
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public void drawRect(float x, float y, float w, float h, int color) {
/*  88 */     float alpha = (color >> 24 & 0xFF) / 255.0F;
/*  89 */     float red = (color >> 16 & 0xFF) / 255.0F;
/*  90 */     float green = (color >> 8 & 0xFF) / 255.0F;
/*  91 */     float blue = (color & 0xFF) / 255.0F;
/*  92 */     Tessellator tessellator = Tessellator.func_178181_a();
/*  93 */     BufferBuilder bufferbuilder = tessellator.func_178180_c();
/*  94 */     GlStateManager.func_179147_l();
/*  95 */     GlStateManager.func_179090_x();
/*  96 */     GlStateManager.func_187441_d(((Float)this.lineWidth.getValue()).floatValue());
/*  97 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/*  98 */     bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181706_f);
/*  99 */     bufferbuilder.func_181662_b(x, h, 0.0D).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 100 */     bufferbuilder.func_181662_b(w, h, 0.0D).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 101 */     bufferbuilder.func_181662_b(w, y, 0.0D).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 102 */     bufferbuilder.func_181662_b(x, y, 0.0D).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 103 */     tessellator.func_78381_a();
/* 104 */     GlStateManager.func_179098_w();
/* 105 */     GlStateManager.func_179084_k();
/*     */   }
/*     */   
/*     */   public void drawOutlineRect(float x, float y, float w, float h, int color) {
/* 109 */     float alpha = (color >> 24 & 0xFF) / 255.0F;
/* 110 */     float red = (color >> 16 & 0xFF) / 255.0F;
/* 111 */     float green = (color >> 8 & 0xFF) / 255.0F;
/* 112 */     float blue = (color & 0xFF) / 255.0F;
/* 113 */     Tessellator tessellator = Tessellator.func_178181_a();
/* 114 */     BufferBuilder bufferbuilder = tessellator.func_178180_c();
/* 115 */     GlStateManager.func_179147_l();
/* 116 */     GlStateManager.func_179090_x();
/* 117 */     GlStateManager.func_187441_d(((Float)this.lineWidth.getValue()).floatValue());
/* 118 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/* 119 */     bufferbuilder.func_181668_a(2, DefaultVertexFormats.field_181706_f);
/* 120 */     bufferbuilder.func_181662_b(x, h, 0.0D).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 121 */     bufferbuilder.func_181662_b(w, h, 0.0D).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 122 */     bufferbuilder.func_181662_b(w, y, 0.0D).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 123 */     bufferbuilder.func_181662_b(x, y, 0.0D).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 124 */     tessellator.func_78381_a();
/* 125 */     GlStateManager.func_179098_w();
/* 126 */     GlStateManager.func_179084_k();
/*     */   }
/*     */   
/*     */   private void renderNameTag(EntityPlayer player, double x, double y, double z, float delta) {
/* 130 */     double tempY = y;
/* 131 */     tempY += player.func_70093_af() ? 0.5D : 0.7D;
/* 132 */     Entity camera = mc.func_175606_aa();
/* 133 */     assert camera != null;
/* 134 */     double originalPositionX = camera.field_70165_t;
/* 135 */     double originalPositionY = camera.field_70163_u;
/* 136 */     double originalPositionZ = camera.field_70161_v;
/* 137 */     camera.field_70165_t = interpolate(camera.field_70169_q, camera.field_70165_t, delta);
/* 138 */     camera.field_70163_u = interpolate(camera.field_70167_r, camera.field_70163_u, delta);
/* 139 */     camera.field_70161_v = interpolate(camera.field_70166_s, camera.field_70161_v, delta);
/* 140 */     String displayTag = getDisplayTag(player);
/* 141 */     double distance = camera.func_70011_f(x + (mc.func_175598_ae()).field_78730_l, y + (mc.func_175598_ae()).field_78731_m, z + (mc.func_175598_ae()).field_78728_n);
/* 142 */     int width = this.renderer.getStringWidth(displayTag) / 2;
/* 143 */     double scale = (0.0018D + ((Float)this.scaling.getValue()).floatValue() * distance * ((Float)this.factor.getValue()).floatValue()) / 1000.0D;
/* 144 */     if (distance <= 8.0D && ((Boolean)this.smartScale.getValue()).booleanValue()) {
/* 145 */       scale = 0.0245D;
/*     */     }
/* 147 */     if (!((Boolean)this.scaleing.getValue()).booleanValue()) {
/* 148 */       scale = ((Float)this.scaling.getValue()).floatValue() / 100.0D;
/*     */     }
/* 150 */     GlStateManager.func_179094_E();
/* 151 */     RenderHelper.func_74519_b();
/* 152 */     GlStateManager.func_179088_q();
/* 153 */     GlStateManager.func_179136_a(1.0F, -1500000.0F);
/* 154 */     GlStateManager.func_179140_f();
/* 155 */     GlStateManager.func_179109_b((float)x, (float)tempY + 1.4F, (float)z);
/* 156 */     GlStateManager.func_179114_b(-(mc.func_175598_ae()).field_78735_i, 0.0F, 1.0F, 0.0F);
/* 157 */     GlStateManager.func_179114_b((mc.func_175598_ae()).field_78732_j, (mc.field_71474_y.field_74320_O == 2) ? -1.0F : 1.0F, 0.0F, 0.0F);
/* 158 */     GlStateManager.func_179139_a(-scale, -scale, scale);
/* 159 */     GlStateManager.func_179097_i();
/* 160 */     GlStateManager.func_179147_l();
/* 161 */     GlStateManager.func_179147_l();
/* 162 */     if (((Boolean)this.rect.getValue()).booleanValue()) {
/* 163 */       drawRect((-width - 2), -(this.renderer.getFontHeight() + 1), width + 2.0F, 1.5F, 1426063360);
/* 164 */       if (((Boolean)this.outline.getValue()).booleanValue()) {
/* 165 */         int color = ((Boolean)this.colorSync.getValue()).booleanValue() ? Colors.INSTANCE.getCurrentColorHex() : (new Color(((Integer)this.redSetting.getValue()).intValue(), ((Integer)this.greenSetting.getValue()).intValue(), ((Integer)this.blueSetting.getValue()).intValue(), ((Integer)this.alphaSetting.getValue()).intValue())).getRGB();
/* 166 */         drawOutlineRect((-width - 2), -(mc.field_71466_p.field_78288_b + 1), width + 2.0F, 1.5F, color);
/*     */       } 
/*     */     } 
/* 169 */     GlStateManager.func_179084_k();
/* 170 */     ItemStack renderMainHand = player.func_184614_ca().func_77946_l();
/* 171 */     if (renderMainHand.func_77962_s() && (renderMainHand.func_77973_b() instanceof net.minecraft.item.ItemTool || renderMainHand.func_77973_b() instanceof net.minecraft.item.ItemArmor)) {
/* 172 */       renderMainHand.field_77994_a = 1;
/*     */     }
/* 174 */     if (((Boolean)this.heldStackName.getValue()).booleanValue() && !renderMainHand.field_190928_g && renderMainHand.func_77973_b() != Items.field_190931_a) {
/* 175 */       String stackName = renderMainHand.func_82833_r();
/* 176 */       int stackNameWidth = this.renderer.getStringWidth(stackName) / 2;
/* 177 */       GL11.glPushMatrix();
/* 178 */       GL11.glScalef(0.75F, 0.75F, 0.0F);
/* 179 */       this.renderer.drawStringWithShadow(stackName, -stackNameWidth, -(getBiggestArmorTag(player) + 20.0F), -1);
/* 180 */       GL11.glScalef(1.5F, 1.5F, 1.0F);
/* 181 */       GL11.glPopMatrix();
/*     */     } 
/* 183 */     if (((Boolean)this.armor.getValue()).booleanValue()) {
/* 184 */       GlStateManager.func_179094_E();
/* 185 */       int xOffset = -8;
/* 186 */       for (ItemStack stack : player.field_71071_by.field_70460_b) {
/* 187 */         if (stack == null)
/* 188 */           continue;  xOffset -= 8;
/*     */       } 
/* 190 */       xOffset -= 8;
/* 191 */       ItemStack renderOffhand = player.func_184592_cb().func_77946_l();
/* 192 */       if (renderOffhand.func_77962_s() && (renderOffhand.func_77973_b() instanceof net.minecraft.item.ItemTool || renderOffhand.func_77973_b() instanceof net.minecraft.item.ItemArmor)) {
/* 193 */         renderOffhand.field_77994_a = 1;
/*     */       }
/* 195 */       renderItemStack(renderOffhand, xOffset, -26);
/* 196 */       xOffset += 16;
/* 197 */       for (ItemStack stack : player.field_71071_by.field_70460_b) {
/* 198 */         if (stack == null)
/* 199 */           continue;  ItemStack armourStack = stack.func_77946_l();
/* 200 */         if (armourStack.func_77962_s() && (armourStack.func_77973_b() instanceof net.minecraft.item.ItemTool || armourStack.func_77973_b() instanceof net.minecraft.item.ItemArmor)) {
/* 201 */           armourStack.field_77994_a = 1;
/*     */         }
/* 203 */         renderItemStack(armourStack, xOffset, -26);
/* 204 */         xOffset += 16;
/*     */       } 
/* 206 */       renderItemStack(renderMainHand, xOffset, -26);
/* 207 */       GlStateManager.func_179121_F();
/*     */     } 
/* 209 */     this.renderer.drawStringWithShadow(displayTag, -width, -(this.renderer.getFontHeight() - 1), getDisplayColour(player));
/* 210 */     camera.field_70165_t = originalPositionX;
/* 211 */     camera.field_70163_u = originalPositionY;
/* 212 */     camera.field_70161_v = originalPositionZ;
/* 213 */     GlStateManager.func_179126_j();
/* 214 */     GlStateManager.func_179084_k();
/* 215 */     GlStateManager.func_179113_r();
/* 216 */     GlStateManager.func_179136_a(1.0F, 1500000.0F);
/* 217 */     GlStateManager.func_179121_F();
/*     */   }
/*     */   
/*     */   private void renderItemStack(ItemStack stack, int x, int y) {
/* 221 */     GlStateManager.func_179094_E();
/* 222 */     GlStateManager.func_179132_a(true);
/* 223 */     GlStateManager.func_179086_m(256);
/* 224 */     RenderHelper.func_74519_b();
/* 225 */     (mc.func_175599_af()).field_77023_b = -150.0F;
/* 226 */     GlStateManager.func_179118_c();
/* 227 */     GlStateManager.func_179126_j();
/* 228 */     GlStateManager.func_179129_p();
/* 229 */     mc.func_175599_af().func_180450_b(stack, x, y);
/* 230 */     mc.func_175599_af().func_175030_a(mc.field_71466_p, stack, x, y);
/* 231 */     (mc.func_175599_af()).field_77023_b = 0.0F;
/* 232 */     RenderHelper.func_74518_a();
/* 233 */     GlStateManager.func_179089_o();
/* 234 */     GlStateManager.func_179141_d();
/* 235 */     GlStateManager.func_179152_a(0.5F, 0.5F, 0.5F);
/* 236 */     GlStateManager.func_179097_i();
/* 237 */     renderEnchantmentText(stack, x, y);
/* 238 */     GlStateManager.func_179126_j();
/* 239 */     GlStateManager.func_179152_a(2.0F, 2.0F, 2.0F);
/* 240 */     GlStateManager.func_179121_F();
/*     */   }
/*     */   
/*     */   private void renderEnchantmentText(ItemStack stack, int x, int y) {
/* 244 */     int enchantmentY = y - 8;
/* 245 */     if (stack.func_77973_b() == Items.field_151153_ao && stack.func_77962_s()) {
/* 246 */       this.renderer.drawStringWithShadow("god", (x * 2), enchantmentY, -3977919);
/* 247 */       enchantmentY -= 8;
/*     */     } 
/* 249 */     NBTTagList enchants = stack.func_77986_q();
/* 250 */     for (int index = 0; index < enchants.func_74745_c(); index++) {
/* 251 */       short id = enchants.func_150305_b(index).func_74765_d("id");
/* 252 */       short level = enchants.func_150305_b(index).func_74765_d("lvl");
/* 253 */       Enchantment enc = Enchantment.func_185262_c(id);
/* 254 */       if (enc != null) {
/* 255 */         String encName = enc.func_190936_d() ? (TextFormatting.RED + enc.func_77316_c(level).substring(11).substring(0, 1).toLowerCase()) : enc.func_77316_c(level).substring(0, 1).toLowerCase();
/* 256 */         encName = encName + level;
/* 257 */         this.renderer.drawStringWithShadow(encName, (x * 2), enchantmentY, -1);
/* 258 */         enchantmentY -= 8;
/*     */       } 
/* 260 */     }  if (DamageUtil.hasDurability(stack)) {
/* 261 */       int percent = DamageUtil.getRoundedDamage(stack);
/* 262 */       String color = (percent >= 60) ? "§a" : ((percent >= 25) ? "§e" : "§c");
/* 263 */       this.renderer.drawStringWithShadow(color + percent + "%", (x * 2), enchantmentY, -1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private float getBiggestArmorTag(EntityPlayer player) {
/* 271 */     float enchantmentY = 0.0F;
/* 272 */     boolean arm = false;
/* 273 */     for (ItemStack stack : player.field_71071_by.field_70460_b) {
/* 274 */       float encY = 0.0F;
/* 275 */       if (stack != null) {
/* 276 */         NBTTagList enchants = stack.func_77986_q();
/* 277 */         for (int index = 0; index < enchants.func_74745_c(); index++) {
/* 278 */           short id = enchants.func_150305_b(index).func_74765_d("id");
/* 279 */           Enchantment enc = Enchantment.func_185262_c(id);
/* 280 */           if (enc != null) {
/* 281 */             encY += 8.0F;
/* 282 */             arm = true;
/*     */           } 
/*     */         } 
/* 285 */       }  if (encY <= enchantmentY)
/* 286 */         continue;  enchantmentY = encY;
/*     */     } 
/* 288 */     ItemStack renderMainHand = player.func_184614_ca().func_77946_l();
/* 289 */     if (renderMainHand.func_77962_s()) {
/* 290 */       float encY = 0.0F;
/* 291 */       NBTTagList enchants = renderMainHand.func_77986_q();
/* 292 */       for (int index2 = 0; index2 < enchants.func_74745_c(); index2++) {
/* 293 */         short id = enchants.func_150305_b(index2).func_74765_d("id");
/* 294 */         Enchantment enc2 = Enchantment.func_185262_c(id);
/* 295 */         if (enc2 != null) {
/* 296 */           encY += 8.0F;
/* 297 */           arm = true;
/*     */         } 
/* 299 */       }  if (encY > enchantmentY)
/* 300 */         enchantmentY = encY; 
/*     */     } 
/*     */     ItemStack renderOffHand;
/* 303 */     if ((renderOffHand = player.func_184592_cb().func_77946_l()).func_77962_s()) {
/* 304 */       float encY = 0.0F;
/* 305 */       NBTTagList enchants = renderOffHand.func_77986_q();
/* 306 */       for (int index = 0; index < enchants.func_74745_c(); index++) {
/* 307 */         short id = enchants.func_150305_b(index).func_74765_d("id");
/* 308 */         Enchantment enc = Enchantment.func_185262_c(id);
/* 309 */         if (enc != null) {
/* 310 */           encY += 8.0F;
/* 311 */           arm = true;
/*     */         } 
/* 313 */       }  if (encY > enchantmentY) {
/* 314 */         enchantmentY = encY;
/*     */       }
/*     */     } 
/* 317 */     return (arm ? false : 20) + enchantmentY;
/*     */   }
/*     */   
/*     */   private String getDisplayTag(EntityPlayer player) {
/* 321 */     String name = player.func_145748_c_().func_150254_d();
/* 322 */     if (name.contains(mc.func_110432_I().func_111285_a())) {
/* 323 */       name = "You";
/*     */     }
/* 325 */     if (!((Boolean)this.health.getValue()).booleanValue()) {
/* 326 */       return name;
/*     */     }
/* 328 */     float health = EntityUtil.getHealth((Entity)player);
/* 329 */     String color = (health > 18.0F) ? "§a" : ((health > 16.0F) ? "§2" : ((health > 12.0F) ? "§e" : ((health > 8.0F) ? "§6" : ((health > 5.0F) ? "§c" : "§4"))));
/* 330 */     String pingStr = "";
/* 331 */     if (((Boolean)this.ping.getValue()).booleanValue()) {
/*     */       try {
/* 333 */         int responseTime = ((NetHandlerPlayClient)Objects.<NetHandlerPlayClient>requireNonNull(mc.func_147114_u())).func_175102_a(player.func_110124_au()).func_178853_c();
/* 334 */         pingStr = pingStr + responseTime + "ms ";
/* 335 */       } catch (Exception exception) {}
/*     */     }
/*     */ 
/*     */     
/* 339 */     String popStr = " ";
/* 340 */     if (((Boolean)this.totemPops.getValue()).booleanValue()) {
/* 341 */       popStr = popStr + Phobos.totemPopManager.getTotemPopString(player);
/*     */     }
/* 343 */     String idString = "";
/* 344 */     if (((Boolean)this.entityID.getValue()).booleanValue()) {
/* 345 */       idString = idString + "ID: " + player.func_145782_y() + " ";
/*     */     }
/* 347 */     String gameModeStr = "";
/* 348 */     if (((Boolean)this.gamemode.getValue()).booleanValue()) {
/* 349 */       gameModeStr = player.func_184812_l_() ? (gameModeStr + "[C] ") : ((player.func_175149_v() || player.func_82150_aj()) ? (gameModeStr + "[I] ") : (gameModeStr + "[S] "));
/*     */     }
/* 351 */     name = (Math.floor(health) == health) ? (name + color + " " + ((health > 0.0F) ? (String)Integer.valueOf((int)Math.floor(health)) : "dead")) : (name + color + " " + ((health > 0.0F) ? (String)Integer.valueOf((int)health) : "dead"));
/* 352 */     return pingStr + idString + gameModeStr + name + popStr;
/*     */   }
/*     */   
/*     */   private int getDisplayColour(EntityPlayer player) {
/* 356 */     int colour = -5592406;
/* 357 */     if (((Boolean)this.whiter.getValue()).booleanValue()) {
/* 358 */       colour = -1;
/*     */     }
/* 360 */     if (Phobos.friendManager.isFriend(player)) {
/* 361 */       return -11157267;
/*     */     }
/* 363 */     if (player.func_82150_aj()) {
/* 364 */       colour = -1113785;
/* 365 */     } else if (player.func_70093_af() && ((Boolean)this.sneak.getValue()).booleanValue()) {
/* 366 */       colour = -6481515;
/*     */     } 
/* 368 */     return colour;
/*     */   }
/*     */   
/*     */   private double interpolate(double previous, double current, float delta) {
/* 372 */     return previous + (current - previous) * delta;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\render\Nametags.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */