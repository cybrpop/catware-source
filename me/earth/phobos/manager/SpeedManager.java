/*    */ package me.earth.phobos.manager;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import me.earth.phobos.features.Feature;
/*    */ import me.earth.phobos.features.modules.client.Managers;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.math.MathHelper;
/*    */ 
/*    */ public class SpeedManager
/*    */   extends Feature
/*    */ {
/*    */   public static final double LAST_JUMP_INFO_DURATION_DEFAULT = 3.0D;
/*    */   public static boolean didJumpThisTick = false;
/*    */   public static boolean isJumping = false;
/* 17 */   public double firstJumpSpeed = 0.0D;
/* 18 */   public double lastJumpSpeed = 0.0D;
/* 19 */   public double percentJumpSpeedChanged = 0.0D;
/* 20 */   public double jumpSpeedChanged = 0.0D;
/*    */   public boolean didJumpLastTick = false;
/* 22 */   public long jumpInfoStartTime = 0L;
/*    */   public boolean wasFirstJump = true;
/* 24 */   public double speedometerCurrentSpeed = 0.0D;
/* 25 */   public HashMap<EntityPlayer, Double> playerSpeeds = new HashMap<>();
/* 26 */   private final int distancer = 20;
/*    */   
/*    */   public static void setDidJumpThisTick(boolean val) {
/* 29 */     didJumpThisTick = val;
/*    */   }
/*    */   
/*    */   public static void setIsJumping(boolean val) {
/* 33 */     isJumping = val;
/*    */   }
/*    */   
/*    */   public float lastJumpInfoTimeRemaining() {
/* 37 */     return (float)(Minecraft.func_71386_F() - this.jumpInfoStartTime) / 1000.0F;
/*    */   }
/*    */   
/*    */   public void updateValues() {
/* 41 */     double distTraveledLastTickX = mc.field_71439_g.field_70165_t - mc.field_71439_g.field_70169_q;
/* 42 */     double distTraveledLastTickZ = mc.field_71439_g.field_70161_v - mc.field_71439_g.field_70166_s;
/* 43 */     this.speedometerCurrentSpeed = distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ;
/* 44 */     if (didJumpThisTick && (!mc.field_71439_g.field_70122_E || isJumping)) {
/* 45 */       if (didJumpThisTick && !this.didJumpLastTick) {
/* 46 */         this.wasFirstJump = (this.lastJumpSpeed == 0.0D);
/* 47 */         this.percentJumpSpeedChanged = (this.speedometerCurrentSpeed != 0.0D) ? (this.speedometerCurrentSpeed / this.lastJumpSpeed - 1.0D) : -1.0D;
/* 48 */         this.jumpSpeedChanged = this.speedometerCurrentSpeed - this.lastJumpSpeed;
/* 49 */         this.jumpInfoStartTime = Minecraft.func_71386_F();
/* 50 */         this.lastJumpSpeed = this.speedometerCurrentSpeed;
/* 51 */         this.firstJumpSpeed = this.wasFirstJump ? this.lastJumpSpeed : 0.0D;
/*    */       } 
/* 53 */       this.didJumpLastTick = didJumpThisTick;
/*    */     } else {
/* 55 */       this.didJumpLastTick = false;
/* 56 */       this.lastJumpSpeed = 0.0D;
/*    */     } 
/* 58 */     if (((Boolean)(Managers.getInstance()).speed.getValue()).booleanValue()) {
/* 59 */       updatePlayers();
/*    */     }
/*    */   }
/*    */   
/*    */   public void updatePlayers() {
/* 64 */     for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/* 65 */       getClass(); getClass(); if (mc.field_71439_g.func_70068_e((Entity)player) >= (20 * 20))
/*    */         continue; 
/* 67 */       double distTraveledLastTickX = player.field_70165_t - player.field_70169_q;
/* 68 */       double distTraveledLastTickZ = player.field_70161_v - player.field_70166_s;
/* 69 */       double playerSpeed = distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ;
/* 70 */       this.playerSpeeds.put(player, Double.valueOf(playerSpeed));
/*    */     } 
/*    */   }
/*    */   
/*    */   public double getPlayerSpeed(EntityPlayer player) {
/* 75 */     if (this.playerSpeeds.get(player) == null) {
/* 76 */       return 0.0D;
/*    */     }
/* 78 */     return turnIntoKpH(((Double)this.playerSpeeds.get(player)).doubleValue());
/*    */   }
/*    */   
/*    */   public double turnIntoKpH(double input) {
/* 82 */     return MathHelper.func_76133_a(input) * 71.2729367892D;
/*    */   }
/*    */   
/*    */   public double getSpeedKpH() {
/* 86 */     double speedometerkphdouble = turnIntoKpH(this.speedometerCurrentSpeed);
/* 87 */     speedometerkphdouble = Math.round(10.0D * speedometerkphdouble) / 10.0D;
/* 88 */     return speedometerkphdouble;
/*    */   }
/*    */   
/*    */   public double getSpeedMpS() {
/* 92 */     double speedometerMpsdouble = turnIntoKpH(this.speedometerCurrentSpeed) / 3.6D;
/* 93 */     speedometerMpsdouble = Math.round(10.0D * speedometerMpsdouble) / 10.0D;
/* 94 */     return speedometerMpsdouble;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\manager\SpeedManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */