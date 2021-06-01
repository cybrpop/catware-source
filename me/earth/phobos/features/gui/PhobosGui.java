/*     */ package me.earth.phobos.features.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.features.gui.components.Component;
/*     */ import me.earth.phobos.features.gui.components.items.Item;
/*     */ import me.earth.phobos.features.gui.components.items.buttons.Button;
/*     */ import me.earth.phobos.features.gui.components.items.buttons.ModuleButton;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ public class PhobosGui extends GuiScreen {
/*     */   private static PhobosGui phobosGui;
/*  16 */   private final ArrayList<Component> components = new ArrayList<>();
/*     */ 
/*     */   
/*     */   public PhobosGui() {
/*  20 */     setInstance();
/*  21 */     load();
/*     */   }
/*     */   
/*     */   public static PhobosGui getInstance() {
/*  25 */     if (INSTANCE == null) {
/*  26 */       INSTANCE = new PhobosGui();
/*     */     }
/*  28 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  32 */     INSTANCE = this;
/*     */   }
/*     */   
/*     */   public static PhobosGui getClickGui() {
/*  36 */     return getInstance();
/*     */   }
/*     */   
/*     */   private void load() {
/*  40 */     int x = -84;
/*  41 */     for (Module.Category category : Phobos.moduleManager.getCategories()) {
/*  42 */       x += 90; this.components.add(new Component(category.getName(), x, 4, true)
/*     */           {
/*     */             public void setupItems()
/*     */             {
/*  46 */               Phobos.moduleManager.getModulesByCategory(category).forEach(module -> {
/*     */                     if (!module.hidden) {
/*     */                       addButton((Button)new ModuleButton(module));
/*     */                     }
/*     */                   });
/*     */             }
/*     */           });
/*     */     } 
/*  54 */     this.components.forEach(components -> components.getItems().sort(()));
/*     */   }
/*     */   
/*     */   public void updateModule(Module module) {
/*  58 */     for (Component component : this.components) {
/*  59 */       for (Item item : component.getItems()) {
/*  60 */         if (!(item instanceof ModuleButton))
/*  61 */           continue;  ModuleButton button = (ModuleButton)item;
/*  62 */         Module mod = button.getModule();
/*  63 */         if (module == null || !module.equals(mod))
/*  64 */           continue;  button.initSettings();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
/*  71 */     checkMouseWheel();
/*  72 */     func_146276_q_();
/*  73 */     this.components.forEach(components -> components.drawScreen(mouseX, mouseY, partialTicks));
/*     */   }
/*     */   
/*     */   public void func_73864_a(int mouseX, int mouseY, int clickedButton) {
/*  77 */     this.components.forEach(components -> components.mouseClicked(mouseX, mouseY, clickedButton));
/*     */   }
/*     */   
/*     */   public void func_146286_b(int mouseX, int mouseY, int releaseButton) {
/*  81 */     this.components.forEach(components -> components.mouseReleased(mouseX, mouseY, releaseButton));
/*     */   }
/*     */   
/*     */   public boolean func_73868_f() {
/*  85 */     return false;
/*     */   }
/*     */   
/*     */   public final ArrayList<Component> getComponents() {
/*  89 */     return this.components;
/*     */   }
/*     */   
/*     */   public void checkMouseWheel() {
/*  93 */     int dWheel = Mouse.getDWheel();
/*  94 */     if (dWheel < 0) {
/*  95 */       this.components.forEach(component -> component.setY(component.getY() - 10));
/*  96 */     } else if (dWheel > 0) {
/*  97 */       this.components.forEach(component -> component.setY(component.getY() + 10));
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getTextOffset() {
/* 102 */     return -6;
/*     */   }
/*     */   
/*     */   public Component getComponentByName(String name) {
/* 106 */     for (Component component : this.components) {
/* 107 */       if (!component.getName().equalsIgnoreCase(name))
/* 108 */         continue;  return component;
/*     */     } 
/* 110 */     return null;
/*     */   }
/*     */   
/*     */   public void func_73869_a(char typedChar, int keyCode) throws IOException {
/* 114 */     super.func_73869_a(typedChar, keyCode);
/* 115 */     this.components.forEach(component -> component.onKeyTyped(typedChar, keyCode));
/*     */   }
/*     */ 
/*     */   
/* 119 */   private static PhobosGui INSTANCE = new PhobosGui();
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\gui\PhobosGui.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */