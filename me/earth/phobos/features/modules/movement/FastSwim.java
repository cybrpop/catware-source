/*    */ package me.earth.phobos.features.modules.movement;
/*    */ 
/*    */ import me.earth.phobos.event.events.MoveEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class FastSwim
/*    */   extends Module {
/* 10 */   public Setting<Double> waterHorizontal = register(new Setting("WaterHorizontal", Double.valueOf(3.0D), Double.valueOf(1.0D), Double.valueOf(20.0D)));
/* 11 */   public Setting<Double> waterVertical = register(new Setting("WaterVertical", Double.valueOf(3.0D), Double.valueOf(1.0D), Double.valueOf(20.0D)));
/* 12 */   public Setting<Double> lavaHorizontal = register(new Setting("LavaHorizontal", Double.valueOf(4.0D), Double.valueOf(1.0D), Double.valueOf(20.0D)));
/* 13 */   public Setting<Double> lavaVertical = register(new Setting("LavaVertical", Double.valueOf(4.0D), Double.valueOf(1.0D), Double.valueOf(20.0D)));
/*    */   
/*    */   public FastSwim() {
/* 16 */     super("FastSwim", "Swim fast", Module.Category.MOVEMENT, true, false, false);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onMove(MoveEvent event) {
/* 21 */     if (mc.field_71439_g.func_180799_ab() && !mc.field_71439_g.field_70122_E) {
/* 22 */       event.setX(event.getX() * ((Double)this.lavaHorizontal.getValue()).doubleValue());
/* 23 */       event.setZ(event.getZ() * ((Double)this.lavaHorizontal.getValue()).doubleValue());
/* 24 */       event.setY(event.getY() * ((Double)this.lavaVertical.getValue()).doubleValue());
/* 25 */     } else if (mc.field_71439_g.func_70090_H() && !mc.field_71439_g.field_70122_E) {
/* 26 */       event.setX(event.getX() * ((Double)this.waterHorizontal.getValue()).doubleValue());
/* 27 */       event.setZ(event.getZ() * ((Double)this.waterHorizontal.getValue()).doubleValue());
/* 28 */       event.setY(event.getY() * ((Double)this.waterVertical.getValue()).doubleValue());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\movement\FastSwim.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */