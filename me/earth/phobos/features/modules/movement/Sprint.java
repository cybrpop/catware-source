/*    */ package me.earth.phobos.features.modules.movement;
/*    */ 
/*    */ import me.earth.phobos.event.events.MoveEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class Sprint
/*    */   extends Module {
/* 10 */   private static Sprint INSTANCE = new Sprint();
/* 11 */   public Setting<Mode> mode = register(new Setting("Mode", Mode.LEGIT));
/*    */   
/*    */   public Sprint() {
/* 14 */     super("Sprint", "Modifies sprinting", Module.Category.MOVEMENT, false, false, false);
/* 15 */     setInstance();
/*    */   }
/*    */   
/*    */   public static Sprint getInstance() {
/* 19 */     if (INSTANCE == null) {
/* 20 */       INSTANCE = new Sprint();
/*    */     }
/* 22 */     return INSTANCE;
/*    */   }
/*    */   
/*    */   private void setInstance() {
/* 26 */     INSTANCE = this;
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onSprint(MoveEvent event) {
/* 31 */     if (event.getStage() == 1 && this.mode.getValue() == Mode.RAGE && (mc.field_71439_g.field_71158_b.field_192832_b != 0.0F || mc.field_71439_g.field_71158_b.field_78902_a != 0.0F)) {
/* 32 */       event.setCanceled(true);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 38 */     switch ((Mode)this.mode.getValue()) {
/*    */       case RAGE:
/* 40 */         if ((!mc.field_71474_y.field_74351_w.func_151470_d() && !mc.field_71474_y.field_74368_y.func_151470_d() && !mc.field_71474_y.field_74370_x.func_151470_d() && !mc.field_71474_y.field_74366_z.func_151470_d()) || mc.field_71439_g.func_70093_af() || mc.field_71439_g.field_70123_F || mc.field_71439_g.func_71024_bL().func_75116_a() <= 6.0F)
/*    */           break; 
/* 42 */         mc.field_71439_g.func_70031_b(true);
/*    */         break;
/*    */       
/*    */       case LEGIT:
/* 46 */         if (!mc.field_71474_y.field_74351_w.func_151470_d() || mc.field_71439_g.func_70093_af() || mc.field_71439_g.func_184587_cr() || mc.field_71439_g.field_70123_F || mc.field_71439_g.func_71024_bL().func_75116_a() <= 6.0F || mc.field_71462_r != null)
/*    */           break; 
/* 48 */         mc.field_71439_g.func_70031_b(true);
/*    */         break;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 55 */     if (!nullCheck()) {
/* 56 */       mc.field_71439_g.func_70031_b(false);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDisplayInfo() {
/* 62 */     return this.mode.currentEnumName();
/*    */   }
/*    */   
/*    */   public enum Mode {
/* 66 */     LEGIT,
/* 67 */     RAGE;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\movement\Sprint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */