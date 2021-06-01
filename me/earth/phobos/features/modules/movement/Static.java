/*    */ package me.earth.phobos.features.modules.movement;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketPlayer;
/*    */ import net.minecraft.util.math.RayTraceResult;
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ 
/*    */ public class Static
/*    */   extends Module {
/* 13 */   private final Setting<Mode> mode = register(new Setting("Mode", Mode.ROOF));
/* 14 */   private final Setting<Boolean> disabler = register(new Setting("Disable", Boolean.valueOf(true), v -> (this.mode.getValue() == Mode.ROOF)));
/* 15 */   private final Setting<Boolean> ySpeed = register(new Setting("YSpeed", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.STATIC)));
/* 16 */   private final Setting<Float> speed = register(new Setting("Speed", Float.valueOf(0.1F), Float.valueOf(0.0F), Float.valueOf(10.0F), v -> (((Boolean)this.ySpeed.getValue()).booleanValue() && this.mode.getValue() == Mode.STATIC)));
/* 17 */   private final Setting<Float> height = register(new Setting("Height", Float.valueOf(3.0F), Float.valueOf(0.0F), Float.valueOf(256.0F), v -> (this.mode.getValue() == Mode.NOVOID)));
/*    */   
/*    */   public Static() {
/* 20 */     super("Static", "Stops any movement. Glitches you up.", Module.Category.MOVEMENT, false, false, false);
/*    */   }
/*    */   
/*    */   public void onUpdate() {
/*    */     RayTraceResult trace;
/* 25 */     if (fullNullCheck()) {
/*    */       return;
/*    */     }
/* 28 */     switch ((Mode)this.mode.getValue()) {
/*    */       case STATIC:
/* 30 */         mc.field_71439_g.field_71075_bZ.field_75100_b = false;
/* 31 */         mc.field_71439_g.field_70159_w = 0.0D;
/* 32 */         mc.field_71439_g.field_70181_x = 0.0D;
/* 33 */         mc.field_71439_g.field_70179_y = 0.0D;
/* 34 */         if (!((Boolean)this.ySpeed.getValue()).booleanValue())
/* 35 */           break;  mc.field_71439_g.field_70747_aH = ((Float)this.speed.getValue()).floatValue();
/* 36 */         if (mc.field_71474_y.field_74314_A.func_151470_d()) {
/* 37 */           mc.field_71439_g.field_70181_x += ((Float)this.speed.getValue()).floatValue();
/*    */         }
/* 39 */         if (!mc.field_71474_y.field_74311_E.func_151470_d())
/* 40 */           break;  mc.field_71439_g.field_70181_x -= ((Float)this.speed.getValue()).floatValue();
/*    */         break;
/*    */       
/*    */       case ROOF:
/* 44 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, 10000.0D, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E));
/* 45 */         if (!((Boolean)this.disabler.getValue()).booleanValue())
/* 46 */           break;  disable();
/*    */         break;
/*    */       
/*    */       case NOVOID:
/* 50 */         if (mc.field_71439_g.field_70145_X || mc.field_71439_g.field_70163_u > ((Float)this.height.getValue()).floatValue())
/*    */           break; 
/* 52 */         trace = mc.field_71441_e.func_147447_a(mc.field_71439_g.func_174791_d(), new Vec3d(mc.field_71439_g.field_70165_t, 0.0D, mc.field_71439_g.field_70161_v), false, false, false);
/* 53 */         if (trace != null && trace.field_72313_a == RayTraceResult.Type.BLOCK) {
/*    */           return;
/*    */         }
/* 56 */         if (Phobos.moduleManager.isModuleEnabled(Phase.class) || Phobos.moduleManager.isModuleEnabled(Flight.class)) {
/*    */           return;
/*    */         }
/* 59 */         mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/* 60 */         if (mc.field_71439_g.func_184187_bx() == null)
/* 61 */           break;  mc.field_71439_g.func_184187_bx().func_70016_h(0.0D, 0.0D, 0.0D);
/*    */         break;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDisplayInfo() {
/* 68 */     if (this.mode.getValue() == Mode.ROOF) {
/* 69 */       return "Roof";
/*    */     }
/* 71 */     if (this.mode.getValue() == Mode.NOVOID) {
/* 72 */       return "NoVoid";
/*    */     }
/* 74 */     return null;
/*    */   }
/*    */   
/*    */   public enum Mode {
/* 78 */     STATIC,
/* 79 */     ROOF,
/* 80 */     NOVOID;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\movement\Static.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */