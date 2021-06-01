/*    */ package me.earth.phobos.manager;
/*    */ 
/*    */ import java.text.DecimalFormat;
/*    */ import java.util.Arrays;
/*    */ import java.util.Objects;
/*    */ import me.earth.phobos.features.Feature;
/*    */ import me.earth.phobos.features.modules.client.Managers;
/*    */ import me.earth.phobos.util.Timer;
/*    */ import net.minecraft.client.network.NetHandlerPlayClient;
/*    */ 
/*    */ public class ServerManager
/*    */   extends Feature {
/* 13 */   private final float[] tpsCounts = new float[10];
/* 14 */   private final DecimalFormat format = new DecimalFormat("##.00#");
/* 15 */   private final Timer timer = new Timer();
/* 16 */   private float TPS = 20.0F;
/* 17 */   private long lastUpdate = -1L;
/* 18 */   private String serverBrand = "";
/*    */   
/*    */   public void onPacketReceived() {
/* 21 */     this.timer.reset();
/*    */   }
/*    */   
/*    */   public boolean isServerNotResponding() {
/* 25 */     return this.timer.passedMs(((Integer)(Managers.getInstance()).respondTime.getValue()).intValue());
/*    */   }
/*    */   
/*    */   public long serverRespondingTime() {
/* 29 */     return this.timer.getPassedTimeMs();
/*    */   }
/*    */ 
/*    */   
/*    */   public void update() {
/* 34 */     long currentTime = System.currentTimeMillis();
/* 35 */     if (this.lastUpdate == -1L) {
/* 36 */       this.lastUpdate = currentTime;
/*    */       return;
/*    */     } 
/* 39 */     long timeDiff = currentTime - this.lastUpdate;
/* 40 */     float tickTime = (float)timeDiff / 20.0F;
/* 41 */     if (tickTime == 0.0F)
/* 42 */       tickTime = 50.0F; 
/*    */     float tps;
/* 44 */     if ((tps = 1000.0F / tickTime) > 20.0F) {
/* 45 */       tps = 20.0F;
/*    */     }
/* 47 */     System.arraycopy(this.tpsCounts, 0, this.tpsCounts, 1, this.tpsCounts.length - 1);
/* 48 */     this.tpsCounts[0] = tps;
/* 49 */     double total = 0.0D;
/* 50 */     for (float f : this.tpsCounts) {
/* 51 */       total += f;
/*    */     }
/* 53 */     if ((total /= this.tpsCounts.length) > 20.0D) {
/* 54 */       total = 20.0D;
/*    */     }
/* 56 */     this.TPS = Float.parseFloat(this.format.format(total));
/* 57 */     this.lastUpdate = currentTime;
/*    */   }
/*    */ 
/*    */   
/*    */   public void reset() {
/* 62 */     Arrays.fill(this.tpsCounts, 20.0F);
/* 63 */     this.TPS = 20.0F;
/*    */   }
/*    */   
/*    */   public float getTpsFactor() {
/* 67 */     return 20.0F / this.TPS;
/*    */   }
/*    */   
/*    */   public float getTPS() {
/* 71 */     return this.TPS;
/*    */   }
/*    */   
/*    */   public String getServerBrand() {
/* 75 */     return this.serverBrand;
/*    */   }
/*    */   
/*    */   public void setServerBrand(String brand) {
/* 79 */     this.serverBrand = brand;
/*    */   }
/*    */   
/*    */   public int getPing() {
/* 83 */     if (fullNullCheck()) {
/* 84 */       return 0;
/*    */     }
/*    */     try {
/* 87 */       return ((NetHandlerPlayClient)Objects.<NetHandlerPlayClient>requireNonNull(mc.func_147114_u())).func_175102_a(mc.func_147114_u().func_175105_e().getId()).func_178853_c();
/* 88 */     } catch (Exception e) {
/* 89 */       return 0;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\manager\ServerManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */