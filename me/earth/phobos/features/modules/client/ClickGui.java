/*    */ package me.earth.phobos.features.modules.client;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.event.events.ClientEvent;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.features.gui.PhobosGui;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.settings.GameSettings;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class ClickGui
/*    */   extends Module {
/* 15 */   private static ClickGui INSTANCE = new ClickGui();
/* 16 */   public Setting<Boolean> colorSync = register(new Setting("Sync", Boolean.valueOf(false)));
/* 17 */   public Setting<Boolean> outline = register(new Setting("Outline", Boolean.valueOf(false)));
/* 18 */   public Setting<Boolean> rainbowRolling = register(new Setting("RollingRainbow", Boolean.valueOf(false), v -> (((Boolean)this.colorSync.getValue()).booleanValue() && ((Boolean)Colors.INSTANCE.rainbow.getValue()).booleanValue() != true)));
/* 19 */   public Setting<String> prefix = register((new Setting("Prefix", ".")).setRenderName(true));
/* 20 */   public Setting<Integer> red = register(new Setting("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/* 21 */   public Setting<Integer> green = register(new Setting("Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255)));
/* 22 */   public Setting<Integer> blue = register(new Setting("Blue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255)));
/* 23 */   public Setting<Integer> hoverAlpha = register(new Setting("Alpha", Integer.valueOf(180), Integer.valueOf(0), Integer.valueOf(255)));
/* 24 */   public Setting<Integer> alpha = register(new Setting("HoverAlpha", Integer.valueOf(240), Integer.valueOf(0), Integer.valueOf(255)));
/* 25 */   public Setting<Boolean> customFov = register(new Setting("CustomFov", Boolean.valueOf(false)));
/* 26 */   public Setting<Float> fov = register(new Setting("Fov", Float.valueOf(150.0F), Float.valueOf(-180.0F), Float.valueOf(180.0F), v -> ((Boolean)this.customFov.getValue()).booleanValue()));
/* 27 */   public Setting<Boolean> openCloseChange = register(new Setting("Open/Close", Boolean.valueOf(false)));
/* 28 */   public Setting<String> open = register((new Setting("Open:", "", v -> ((Boolean)this.openCloseChange.getValue()).booleanValue())).setRenderName(true));
/* 29 */   public Setting<String> close = register((new Setting("Close:", "", v -> ((Boolean)this.openCloseChange.getValue()).booleanValue())).setRenderName(true));
/* 30 */   public Setting<String> moduleButton = register((new Setting("Buttons:", "", v -> !((Boolean)this.openCloseChange.getValue()).booleanValue())).setRenderName(true));
/* 31 */   public Setting<Boolean> devSettings = register(new Setting("DevSettings", Boolean.valueOf(false)));
/* 32 */   public Setting<Integer> topRed = register(new Setting("TopRed", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.devSettings.getValue()).booleanValue()));
/* 33 */   public Setting<Integer> topGreen = register(new Setting("TopGreen", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.devSettings.getValue()).booleanValue()));
/* 34 */   public Setting<Integer> topBlue = register(new Setting("TopBlue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.devSettings.getValue()).booleanValue()));
/* 35 */   public Setting<Integer> topAlpha = register(new Setting("TopAlpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.devSettings.getValue()).booleanValue()));
/*    */   
/*    */   public ClickGui() {
/* 38 */     super("ClickGui", "Opens the ClickGui", Module.Category.CLIENT, true, true, false);
/* 39 */     setInstance();
/*    */   }
/*    */   
/*    */   public static ClickGui getInstance() {
/* 43 */     if (INSTANCE == null) {
/* 44 */       INSTANCE = new ClickGui();
/*    */     }
/* 46 */     return INSTANCE;
/*    */   }
/*    */   
/*    */   private void setInstance() {
/* 50 */     INSTANCE = this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 55 */     if (((Boolean)this.customFov.getValue()).booleanValue()) {
/* 56 */       mc.field_71474_y.func_74304_a(GameSettings.Options.FOV, ((Float)this.fov.getValue()).floatValue());
/*    */     }
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onSettingChange(ClientEvent event) {
/* 62 */     if (event.getStage() == 2 && event.getSetting().getFeature().equals(this)) {
/* 63 */       if (event.getSetting().equals(this.prefix)) {
/* 64 */         Phobos.commandManager.setPrefix((String)this.prefix.getPlannedValue());
/* 65 */         Command.sendMessage("Prefix set to §a" + Phobos.commandManager.getPrefix());
/*    */       } 
/* 67 */       Phobos.colorManager.setColor(((Integer)this.red.getPlannedValue()).intValue(), ((Integer)this.green.getPlannedValue()).intValue(), ((Integer)this.blue.getPlannedValue()).intValue(), ((Integer)this.hoverAlpha.getPlannedValue()).intValue());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 73 */     mc.func_147108_a((GuiScreen)new PhobosGui());
/*    */   }
/*    */ 
/*    */   
/*    */   public void onLoad() {
/* 78 */     if (((Boolean)this.colorSync.getValue()).booleanValue()) {
/* 79 */       Phobos.colorManager.setColor(Colors.INSTANCE.getCurrentColor().getRed(), Colors.INSTANCE.getCurrentColor().getGreen(), Colors.INSTANCE.getCurrentColor().getBlue(), ((Integer)this.hoverAlpha.getValue()).intValue());
/*    */     } else {
/* 81 */       Phobos.colorManager.setColor(((Integer)this.red.getValue()).intValue(), ((Integer)this.green.getValue()).intValue(), ((Integer)this.blue.getValue()).intValue(), ((Integer)this.hoverAlpha.getValue()).intValue());
/*    */     } 
/* 83 */     Phobos.commandManager.setPrefix((String)this.prefix.getValue());
/*    */   }
/*    */ 
/*    */   
/*    */   public void onTick() {
/* 88 */     if (!(mc.field_71462_r instanceof PhobosGui)) {
/* 89 */       disable();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 95 */     if (mc.field_71462_r instanceof PhobosGui)
/* 96 */       mc.func_147108_a(null); 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\client\ClickGui.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */