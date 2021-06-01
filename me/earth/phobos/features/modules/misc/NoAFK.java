/*    */ package me.earth.phobos.features.modules.misc;
/*    */ 
/*    */ import java.util.Random;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketAnimation;
/*    */ import net.minecraft.util.EnumHand;
/*    */ 
/*    */ public class NoAFK
/*    */   extends Module
/*    */ {
/* 13 */   private final Setting<Boolean> swing = register(new Setting("Swing", Boolean.valueOf(true)));
/* 14 */   private final Setting<Boolean> turn = register(new Setting("Turn", Boolean.valueOf(true)));
/* 15 */   private final Random random = new Random();
/*    */   
/*    */   public NoAFK() {
/* 18 */     super("NoAFK", "Prevents you from getting kicked for afk.", Module.Category.MISC, false, false, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 23 */     if (mc.field_71442_b.func_181040_m()) {
/*    */       return;
/*    */     }
/* 26 */     if (mc.field_71439_g.field_70173_aa % 40 == 0 && ((Boolean)this.swing.getValue()).booleanValue()) {
/* 27 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
/*    */     }
/* 29 */     if (mc.field_71439_g.field_70173_aa % 15 == 0 && ((Boolean)this.turn.getValue()).booleanValue()) {
/* 30 */       mc.field_71439_g.field_70177_z = (this.random.nextInt(360) - 180);
/*    */     }
/* 32 */     if (!((Boolean)this.swing.getValue()).booleanValue() && !((Boolean)this.turn.getValue()).booleanValue() && mc.field_71439_g.field_70173_aa % 80 == 0)
/* 33 */       mc.field_71439_g.func_70664_aZ(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\misc\NoAFK.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */