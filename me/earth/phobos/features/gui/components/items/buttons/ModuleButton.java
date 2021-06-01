/*     */ package me.earth.phobos.features.gui.components.items.buttons;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.features.gui.PhobosGui;
/*     */ import me.earth.phobos.features.gui.components.items.Item;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.client.ClickGui;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import net.minecraft.client.audio.ISound;
/*     */ import net.minecraft.client.audio.PositionedSoundRecord;
/*     */ import net.minecraft.init.SoundEvents;
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
/*     */ public class ModuleButton
/*     */   extends Button
/*     */ {
/*     */   private final Module module;
/*  28 */   private List<Item> items = new ArrayList<>();
/*     */   private boolean subOpen;
/*     */   
/*     */   public ModuleButton(Module module) {
/*  32 */     super(module.getName());
/*  33 */     this.module = module;
/*  34 */     initSettings();
/*     */   }
/*     */   
/*     */   public void initSettings() {
/*  38 */     ArrayList<Item> newItems = new ArrayList<>();
/*  39 */     if (!this.module.getSettings().isEmpty()) {
/*  40 */       for (Setting setting : this.module.getSettings()) {
/*  41 */         if (setting.getValue() instanceof Boolean && !setting.getName().equals("Enabled")) {
/*  42 */           newItems.add(new BooleanButton(setting));
/*     */         }
/*  44 */         if (setting.getValue() instanceof me.earth.phobos.features.setting.Bind && !this.module.getName().equalsIgnoreCase("Hud")) {
/*  45 */           newItems.add(new BindButton(setting));
/*     */         }
/*  47 */         if (setting.getValue() instanceof String || setting.getValue() instanceof Character) {
/*  48 */           newItems.add(new StringButton(setting));
/*     */         }
/*  50 */         if (setting.isNumberSetting()) {
/*  51 */           if (setting.hasRestriction()) {
/*  52 */             newItems.add(new Slider(setting));
/*     */             continue;
/*     */           } 
/*  55 */           newItems.add(new UnlimitedSlider(setting));
/*     */         } 
/*  57 */         if (!setting.isEnumSetting())
/*  58 */           continue;  newItems.add(new EnumButton(setting));
/*     */       } 
/*     */     }
/*  61 */     this.items = newItems;
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/*  66 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*  67 */     if (!this.items.isEmpty()) {
/*  68 */       ClickGui gui = (ClickGui)Phobos.moduleManager.getModuleByClass(ClickGui.class);
/*  69 */       Phobos.textManager.drawStringWithShadow(((Boolean)gui.openCloseChange.getValue()).booleanValue() ? (this.subOpen ? (String)gui.close.getValue() : (String)gui.open.getValue()) : (String)gui.moduleButton.getValue(), this.x - 1.5F + this.width - 7.4F, this.y - 2.0F - PhobosGui.getClickGui().getTextOffset(), -1);
/*  70 */       if (this.subOpen) {
/*  71 */         float height = 1.0F;
/*  72 */         for (Item item : this.items) {
/*  73 */           if (!item.isHidden()) {
/*  74 */             item.setLocation(this.x + 1.0F, this.y + (height += 15.0F));
/*  75 */             item.setHeight(15);
/*  76 */             item.setWidth(this.width - 9);
/*  77 */             item.drawScreen(mouseX, mouseY, partialTicks);
/*     */           } 
/*  79 */           item.update();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
/*  87 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*  88 */     if (!this.items.isEmpty()) {
/*  89 */       if (mouseButton == 1 && isHovering(mouseX, mouseY)) {
/*  90 */         this.subOpen = !this.subOpen;
/*  91 */         mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_184371_a(SoundEvents.field_187909_gi, 1.0F));
/*     */       } 
/*  93 */       if (this.subOpen) {
/*  94 */         for (Item item : this.items) {
/*  95 */           if (item.isHidden())
/*  96 */             continue;  item.mouseClicked(mouseX, mouseY, mouseButton);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onKeyTyped(char typedChar, int keyCode) {
/* 104 */     super.onKeyTyped(typedChar, keyCode);
/* 105 */     if (!this.items.isEmpty() && this.subOpen) {
/* 106 */       for (Item item : this.items) {
/* 107 */         if (item.isHidden())
/* 108 */           continue;  item.onKeyTyped(typedChar, keyCode);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 115 */     if (this.subOpen) {
/* 116 */       int height = 14;
/* 117 */       for (Item item : this.items) {
/* 118 */         if (item.isHidden())
/* 119 */           continue;  height += item.getHeight() + 1;
/*     */       } 
/* 121 */       return height + 2;
/*     */     } 
/* 123 */     return 14;
/*     */   }
/*     */   
/*     */   public Module getModule() {
/* 127 */     return this.module;
/*     */   }
/*     */ 
/*     */   
/*     */   public void toggle() {
/* 132 */     this.module.toggle();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getState() {
/* 137 */     return this.module.isEnabled();
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\gui\components\items\buttons\ModuleButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */