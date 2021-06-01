/*    */ package me.earth.phobos.features.modules.misc;
/*    */ 
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.MathUtil;
/*    */ import me.earth.phobos.util.Timer;
/*    */ import net.minecraft.client.gui.GuiDisconnected;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.multiplayer.GuiConnecting;
/*    */ import net.minecraft.client.multiplayer.ServerData;
/*    */ import net.minecraftforge.client.event.GuiOpenEvent;
/*    */ import net.minecraftforge.event.world.WorldEvent;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AutoReconnect
/*    */   extends Module
/*    */ {
/*    */   private static ServerData serverData;
/* 21 */   private static AutoReconnect INSTANCE = new AutoReconnect();
/*    */ 
/*    */   
/* 24 */   private final Setting<Integer> delay = register(new Setting("Delay", Integer.valueOf(5)));
/*    */   
/*    */   public AutoReconnect() {
/* 27 */     super("AutoReconnect", "Reconnects you if you disconnect.", Module.Category.MISC, true, false, false);
/* 28 */     setInstance();
/*    */   }
/*    */   
/*    */   public static AutoReconnect getInstance() {
/* 32 */     if (INSTANCE == null) {
/* 33 */       INSTANCE = new AutoReconnect();
/*    */     }
/* 35 */     return INSTANCE;
/*    */   }
/*    */   
/*    */   private void setInstance() {
/* 39 */     INSTANCE = this;
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void sendPacket(GuiOpenEvent event) {
/* 44 */     if (event.getGui() instanceof GuiDisconnected) {
/* 45 */       updateLastConnectedServer();
/* 46 */       if (AutoLog.getInstance().isOff()) {
/* 47 */         GuiDisconnected disconnected = (GuiDisconnected)event.getGui();
/* 48 */         event.setGui((GuiScreen)new GuiDisconnectedHook(disconnected));
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onWorldUnload(WorldEvent.Unload event) {
/* 55 */     updateLastConnectedServer();
/*    */   }
/*    */   
/*    */   public void updateLastConnectedServer() {
/* 59 */     ServerData data = mc.func_147104_D();
/* 60 */     if (data != null)
/* 61 */       serverData = data; 
/*    */   }
/*    */   
/*    */   private class GuiDisconnectedHook
/*    */     extends GuiDisconnected
/*    */   {
/*    */     private final Timer timer;
/*    */     
/*    */     public GuiDisconnectedHook(GuiDisconnected disconnected) {
/* 70 */       super(disconnected.field_146307_h, disconnected.field_146306_a, disconnected.field_146304_f);
/* 71 */       this.timer = new Timer();
/* 72 */       this.timer.reset();
/*    */     }
/*    */     
/*    */     public void func_73876_c() {
/* 76 */       if (this.timer.passedS(((Integer)AutoReconnect.this.delay.getValue()).intValue())) {
/* 77 */         this.field_146297_k.func_147108_a((GuiScreen)new GuiConnecting(this.field_146307_h, this.field_146297_k, (AutoReconnect.serverData == null) ? this.field_146297_k.field_71422_O : AutoReconnect.serverData));
/*    */       }
/*    */     }
/*    */     
/*    */     public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
/* 82 */       super.func_73863_a(mouseX, mouseY, partialTicks);
/* 83 */       String s = "Reconnecting in " + MathUtil.round(((((Integer)AutoReconnect.this.delay.getValue()).intValue() * 1000) - this.timer.getPassedTimeMs()) / 1000.0D, 1);
/* 84 */       AutoReconnect.this.renderer.drawString(s, (this.field_146294_l / 2 - AutoReconnect.this.renderer.getStringWidth(s) / 2), (this.field_146295_m - 16), 16777215, true);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\misc\AutoReconnect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */