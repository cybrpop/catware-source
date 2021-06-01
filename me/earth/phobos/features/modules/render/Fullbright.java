/*    */ package me.earth.phobos.features.modules.render;
/*    */ 
/*    */ import me.earth.phobos.event.events.PacketEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraft.init.MobEffects;
/*    */ import net.minecraft.network.play.server.SPacketEntityEffect;
/*    */ import net.minecraft.potion.PotionEffect;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class Fullbright
/*    */   extends Module {
/* 13 */   public Setting<Mode> mode = register(new Setting("Mode", Mode.GAMMA));
/* 14 */   public Setting<Boolean> effects = register(new Setting("Effects", Boolean.valueOf(false)));
/* 15 */   private float previousSetting = 1.0F;
/*    */   
/*    */   public Fullbright() {
/* 18 */     super("Fullbright", "Makes your game brighter.", Module.Category.RENDER, true, false, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 23 */     this.previousSetting = mc.field_71474_y.field_74333_Y;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 28 */     if (this.mode.getValue() == Mode.GAMMA) {
/* 29 */       mc.field_71474_y.field_74333_Y = 1000.0F;
/*    */     }
/* 31 */     if (this.mode.getValue() == Mode.POTION) {
/* 32 */       mc.field_71439_g.func_70690_d(new PotionEffect(MobEffects.field_76439_r, 5210));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 38 */     if (this.mode.getValue() == Mode.POTION) {
/* 39 */       mc.field_71439_g.func_184589_d(MobEffects.field_76439_r);
/*    */     }
/* 41 */     mc.field_71474_y.field_74333_Y = this.previousSetting;
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onPacketReceive(PacketEvent.Receive event) {
/* 46 */     if (event.getStage() == 0 && event.getPacket() instanceof SPacketEntityEffect && ((Boolean)this.effects.getValue()).booleanValue()) {
/* 47 */       SPacketEntityEffect packet = (SPacketEntityEffect)event.getPacket();
/* 48 */       if (mc.field_71439_g != null && packet.func_149426_d() == mc.field_71439_g.func_145782_y() && (packet.func_149427_e() == 9 || packet.func_149427_e() == 15))
/* 49 */         event.setCanceled(true); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public enum Mode
/*    */   {
/* 55 */     GAMMA,
/* 56 */     POTION;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\render\Fullbright.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */