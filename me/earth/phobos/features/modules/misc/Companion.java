/*    */ package me.earth.phobos.features.modules.misc;
/*    */ 
/*    */ import com.mojang.text2speech.Narrator;
/*    */ import me.earth.phobos.event.events.DeathEvent;
/*    */ import me.earth.phobos.event.events.TotemPopEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class Companion
/*    */   extends Module {
/* 12 */   public Setting<String> totemPopMessage = register(new Setting("PopMessage", "<player> watch out you're popping!"));
/* 13 */   public Setting<String> deathMessages = register(new Setting("DeathMessage", "<player> you retard you just fucking died!"));
/* 14 */   private final Narrator narrator = Narrator.getNarrator();
/*    */   
/*    */   public Companion() {
/* 17 */     super("Companion", "The best module", Module.Category.MISC, true, false, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 22 */     this.narrator.say("Hello and welcome to Catware");
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 27 */     this.narrator.clear();
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onTotemPop(TotemPopEvent event) {
/* 32 */     if (event.getEntity() == mc.field_71439_g) {
/* 33 */       this.narrator.say(((String)this.totemPopMessage.getValue()).replaceAll("<player>", mc.field_71439_g.func_70005_c_()));
/*    */     }
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onDeath(DeathEvent event) {
/* 39 */     if (event.player == mc.field_71439_g)
/* 40 */       this.narrator.say(((String)this.deathMessages.getValue()).replaceAll("<player>", mc.field_71439_g.func_70005_c_())); 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\misc\Companion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */