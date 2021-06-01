/*    */ package me.earth.phobos.features.modules.player;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.Timer;
/*    */ 
/*    */ public class TimerSpeed
/*    */   extends Module {
/* 10 */   public Setting<Boolean> autoOff = register(new Setting("AutoOff", Boolean.valueOf(false)));
/* 11 */   public Setting<Integer> timeLimit = register(new Setting("Limit", Integer.valueOf(250), Integer.valueOf(1), Integer.valueOf(2500), v -> ((Boolean)this.autoOff.getValue()).booleanValue()));
/* 12 */   public Setting<TimerMode> mode = register(new Setting("Mode", TimerMode.NORMAL));
/* 13 */   public Setting<Float> timerSpeed = register(new Setting("Speed", Float.valueOf(4.0F), Float.valueOf(0.1F), Float.valueOf(20.0F)));
/* 14 */   public Setting<Float> fastSpeed = register(new Setting("Fast", Float.valueOf(10.0F), Float.valueOf(0.1F), Float.valueOf(100.0F), v -> (this.mode.getValue() == TimerMode.SWITCH), "Fast Speed for switch."));
/* 15 */   public Setting<Integer> fastTime = register(new Setting("FastTime", Integer.valueOf(20), Integer.valueOf(1), Integer.valueOf(500), v -> (this.mode.getValue() == TimerMode.SWITCH), "How long you want to go fast.(ms * 10)"));
/* 16 */   public Setting<Integer> slowTime = register(new Setting("SlowTime", Integer.valueOf(20), Integer.valueOf(1), Integer.valueOf(500), v -> (this.mode.getValue() == TimerMode.SWITCH), "Recover from too fast.(ms * 10)"));
/* 17 */   public Setting<Boolean> startFast = register(new Setting("StartFast", Boolean.valueOf(false), v -> (this.mode.getValue() == TimerMode.SWITCH)));
/* 18 */   public float speed = 1.0F;
/* 19 */   private final Timer timer = new Timer();
/* 20 */   private final Timer turnOffTimer = new Timer();
/*    */   private boolean fast = false;
/*    */   
/*    */   public TimerSpeed() {
/* 24 */     super("Timer", "Will speed up the game.", Module.Category.PLAYER, false, false, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 29 */     this.turnOffTimer.reset();
/* 30 */     this.speed = ((Float)this.timerSpeed.getValue()).floatValue();
/* 31 */     if (!((Boolean)this.startFast.getValue()).booleanValue()) {
/* 32 */       this.timer.reset();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 38 */     if (((Boolean)this.autoOff.getValue()).booleanValue() && this.turnOffTimer.passedMs(((Integer)this.timeLimit.getValue()).intValue())) {
/* 39 */       disable();
/*    */       return;
/*    */     } 
/* 42 */     if (this.mode.getValue() == TimerMode.NORMAL) {
/* 43 */       this.speed = ((Float)this.timerSpeed.getValue()).floatValue();
/*    */       return;
/*    */     } 
/* 46 */     if (!this.fast && this.timer.passedDms(((Integer)this.slowTime.getValue()).intValue())) {
/* 47 */       this.fast = true;
/* 48 */       this.speed = ((Float)this.fastSpeed.getValue()).floatValue();
/* 49 */       this.timer.reset();
/*    */     } 
/* 51 */     if (this.fast && this.timer.passedDms(((Integer)this.fastTime.getValue()).intValue())) {
/* 52 */       this.fast = false;
/* 53 */       this.speed = ((Float)this.timerSpeed.getValue()).floatValue();
/* 54 */       this.timer.reset();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 60 */     this.speed = 1.0F;
/* 61 */     Phobos.timerManager.reset();
/* 62 */     this.fast = false;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDisplayInfo() {
/* 67 */     return this.timerSpeed.getValueAsString();
/*    */   }
/*    */   
/*    */   public enum TimerMode {
/* 71 */     NORMAL,
/* 72 */     SWITCH;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\player\TimerSpeed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */