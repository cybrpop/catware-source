/*     */ package me.earth.phobos.features.modules;
/*     */ 
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.ClientEvent;
/*     */ import me.earth.phobos.event.events.Render2DEvent;
/*     */ import me.earth.phobos.event.events.Render3DEvent;
/*     */ import me.earth.phobos.features.Feature;
/*     */ import me.earth.phobos.features.command.Command;
/*     */ import me.earth.phobos.features.modules.client.HUD;
/*     */ import me.earth.phobos.features.setting.Bind;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.Util;
/*     */ import net.minecraftforge.common.MinecraftForge;
/*     */ import net.minecraftforge.fml.common.eventhandler.Event;
/*     */ 
/*     */ public class Module
/*     */   extends Feature
/*     */ {
/*     */   private final String description;
/*     */   private final Category category;
/*  24 */   public Setting<Boolean> enabled = register(new Setting("Enabled", Boolean.valueOf(false)));
/*  25 */   public Setting<Boolean> drawn = register(new Setting("Drawn", Boolean.valueOf(true)));
/*  26 */   public Setting<Bind> bind = register(new Setting("Bind", new Bind(-1)));
/*     */   public Setting<String> displayName;
/*     */   public boolean hasListener;
/*     */   public boolean alwaysListening;
/*     */   public boolean hidden;
/*  31 */   public float arrayListOffset = 0.0F;
/*  32 */   public float arrayListVOffset = 0.0F;
/*     */   public float offset;
/*     */   public float vOffset;
/*     */   public boolean sliding;
/*     */   public Animation animation;
/*     */   
/*     */   public Module(String name, String description, Category category, boolean hasListener, boolean hidden, boolean alwaysListening) {
/*  39 */     super(name);
/*  40 */     this.displayName = register(new Setting("DisplayName", name));
/*  41 */     this.description = description;
/*  42 */     this.category = category;
/*  43 */     this.hasListener = hasListener;
/*  44 */     this.hidden = hidden;
/*  45 */     this.alwaysListening = alwaysListening;
/*  46 */     this.animation = new Animation(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {}
/*     */ 
/*     */   
/*     */   public void onDisable() {}
/*     */ 
/*     */   
/*     */   public void onToggle() {}
/*     */ 
/*     */   
/*     */   public void onLoad() {}
/*     */ 
/*     */   
/*     */   public void onTick() {}
/*     */ 
/*     */   
/*     */   public void onLogin() {}
/*     */ 
/*     */   
/*     */   public void onLogout() {}
/*     */ 
/*     */   
/*     */   public void onUpdate() {}
/*     */ 
/*     */   
/*     */   public void onRender2D(Render2DEvent event) {}
/*     */ 
/*     */   
/*     */   public void onRender3D(Render3DEvent event) {}
/*     */ 
/*     */   
/*     */   public void onUnload() {}
/*     */   
/*     */   public String getDisplayInfo() {
/*  83 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isOn() {
/*  87 */     return ((Boolean)this.enabled.getValue()).booleanValue();
/*     */   }
/*     */   
/*     */   public boolean isOff() {
/*  91 */     return !((Boolean)this.enabled.getValue()).booleanValue();
/*     */   }
/*     */   
/*     */   public void setEnabled(boolean enabled) {
/*  95 */     if (enabled) {
/*  96 */       enable();
/*     */     } else {
/*  98 */       disable();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void enable() {
/* 103 */     this.enabled.setValue(Boolean.valueOf(true));
/* 104 */     onToggle();
/* 105 */     onEnable();
/* 106 */     if (isOn() && this.hasListener && !this.alwaysListening) {
/* 107 */       MinecraftForge.EVENT_BUS.register(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public void disable() {
/* 112 */     if (this.hasListener && !this.alwaysListening) {
/* 113 */       MinecraftForge.EVENT_BUS.unregister(this);
/*     */     }
/* 115 */     this.enabled.setValue(Boolean.valueOf(false));
/* 116 */     onToggle();
/* 117 */     onDisable();
/*     */   }
/*     */   
/*     */   public void toggle() {
/* 121 */     ClientEvent event = new ClientEvent(!isEnabled() ? 1 : 0, this);
/* 122 */     MinecraftForge.EVENT_BUS.post((Event)event);
/* 123 */     if (!event.isCanceled()) {
/* 124 */       setEnabled(!isEnabled());
/*     */     }
/*     */   }
/*     */   
/*     */   public String getDisplayName() {
/* 129 */     return (String)this.displayName.getValue();
/*     */   }
/*     */   
/*     */   public void setDisplayName(String name) {
/* 133 */     Module module = Phobos.moduleManager.getModuleByDisplayName(name);
/* 134 */     Module originalModule = Phobos.moduleManager.getModuleByName(name);
/* 135 */     if (module == null && originalModule == null) {
/* 136 */       Command.sendMessage(getDisplayName() + ", Original name: " + getName() + ", has been renamed to: " + name);
/* 137 */       this.displayName.setValue(name);
/*     */       return;
/*     */     } 
/* 140 */     Command.sendMessage("§cA module of this name already exists.");
/*     */   }
/*     */   
/*     */   public String getDescription() {
/* 144 */     return this.description;
/*     */   }
/*     */   
/*     */   public boolean isSliding() {
/* 148 */     return this.sliding;
/*     */   }
/*     */   
/*     */   public boolean isDrawn() {
/* 152 */     return ((Boolean)this.drawn.getValue()).booleanValue();
/*     */   }
/*     */   
/*     */   public void setDrawn(boolean drawn) {
/* 156 */     this.drawn.setValue(Boolean.valueOf(drawn));
/*     */   }
/*     */   
/*     */   public Category getCategory() {
/* 160 */     return this.category;
/*     */   }
/*     */   
/*     */   public String getInfo() {
/* 164 */     return null;
/*     */   }
/*     */   
/*     */   public Bind getBind() {
/* 168 */     return (Bind)this.bind.getValue();
/*     */   }
/*     */   
/*     */   public void setBind(int key) {
/* 172 */     this.bind.setValue(new Bind(key));
/*     */   }
/*     */   
/*     */   public boolean listening() {
/* 176 */     return ((this.hasListener && isOn()) || this.alwaysListening);
/*     */   }
/*     */   
/*     */   public String getFullArrayString() {
/* 180 */     return getDisplayName() + "§8" + ((getDisplayInfo() != null) ? (" [§r" + getDisplayInfo() + "§8]") : "");
/*     */   }
/*     */   
/*     */   public enum Category {
/* 184 */     COMBAT("Combat"),
/* 185 */     MISC("Misc"),
/* 186 */     RENDER("Render"),
/* 187 */     MOVEMENT("Movement"),
/* 188 */     PLAYER("Player"),
/* 189 */     CLIENT("Client");
/*     */     
/*     */     private final String name;
/*     */     
/*     */     Category(String name) {
/* 194 */       this.name = name;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 198 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   public class Animation
/*     */     extends Thread {
/*     */     public Module module;
/*     */     public float offset;
/*     */     public float vOffset;
/*     */     public String lastText;
/*     */     public boolean shouldMetaSlide;
/*     */     ScheduledExecutorService service;
/*     */     
/*     */     public Animation(Module module) {
/* 212 */       super("Animation");
/* 213 */       this.service = Executors.newSingleThreadScheduledExecutor();
/* 214 */       this.module = module;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 219 */       String text = this.module.getDisplayName() + "§7" + ((this.module.getDisplayInfo() != null) ? (" [§f" + this.module.getDisplayInfo() + "§7]") : "");
/* 220 */       this.module.offset = Module.this.renderer.getStringWidth(text) / ((Integer)(HUD.getInstance()).animationHorizontalTime.getValue()).floatValue();
/* 221 */       this.module.vOffset = Module.this.renderer.getFontHeight() / ((Integer)(HUD.getInstance()).animationVerticalTime.getValue()).floatValue();
/* 222 */       if (this.module.isEnabled() && ((Integer)(HUD.getInstance()).animationHorizontalTime.getValue()).intValue() != 1) {
/* 223 */         if (this.module.arrayListOffset > this.module.offset && Util.mc.field_71441_e != null) {
/* 224 */           this.module.arrayListOffset -= this.module.offset;
/* 225 */           this.module.sliding = true;
/*     */         } 
/* 227 */       } else if (this.module.isDisabled() && ((Integer)(HUD.getInstance()).animationHorizontalTime.getValue()).intValue() != 1) {
/* 228 */         if (this.module.arrayListOffset < Module.this.renderer.getStringWidth(text) && Util.mc.field_71441_e != null) {
/* 229 */           this.module.arrayListOffset += this.module.offset;
/* 230 */           this.module.sliding = true;
/*     */         } else {
/* 232 */           this.module.sliding = false;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 239 */       System.out.println("Starting animation thread for " + this.module.getName());
/* 240 */       this.service.scheduleAtFixedRate(this, 0L, 1L, TimeUnit.MILLISECONDS);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\Module.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */