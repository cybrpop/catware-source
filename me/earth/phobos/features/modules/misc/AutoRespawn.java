/*    */ package me.earth.phobos.features.modules.misc;
/*    */ 
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraftforge.client.event.GuiOpenEvent;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class AutoRespawn
/*    */   extends Module
/*    */ {
/* 12 */   public Setting<Boolean> antiDeathScreen = register(new Setting("AntiDeathScreen", Boolean.valueOf(true)));
/* 13 */   public Setting<Boolean> deathCoords = register(new Setting("DeathCoords", Boolean.valueOf(false)));
/* 14 */   public Setting<Boolean> respawn = register(new Setting("Respawn", Boolean.valueOf(true)));
/*    */   
/*    */   public AutoRespawn() {
/* 17 */     super("AutoRespawn", "Respawns you when you die.", Module.Category.MISC, true, false, false);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onDisplayDeathScreen(GuiOpenEvent event) {
/* 22 */     if (event.getGui() instanceof net.minecraft.client.gui.GuiGameOver) {
/* 23 */       if (((Boolean)this.deathCoords.getValue()).booleanValue() && event.getGui() instanceof net.minecraft.client.gui.GuiGameOver) {
/* 24 */         Command.sendMessage(String.format("You died at x %d y %d z %d", new Object[] { Integer.valueOf((int)mc.field_71439_g.field_70165_t), Integer.valueOf((int)mc.field_71439_g.field_70163_u), Integer.valueOf((int)mc.field_71439_g.field_70161_v) }));
/*    */       }
/* 26 */       if ((((Boolean)this.respawn.getValue()).booleanValue() && mc.field_71439_g.func_110143_aJ() <= 0.0F) || (((Boolean)this.antiDeathScreen.getValue()).booleanValue() && mc.field_71439_g.func_110143_aJ() > 0.0F)) {
/* 27 */         event.setCanceled(true);
/* 28 */         mc.field_71439_g.func_71004_bE();
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\misc\AutoRespawn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */