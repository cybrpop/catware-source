/*    */ package me.earth.phobos.features.modules.misc;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraft.client.network.NetworkPlayerInfo;
/*    */ import net.minecraft.scoreboard.ScorePlayerTeam;
/*    */ import net.minecraft.scoreboard.Team;
/*    */ 
/*    */ public class ExtraTab
/*    */   extends Module {
/* 12 */   private static ExtraTab INSTANCE = new ExtraTab();
/* 13 */   public Setting<Integer> size = register(new Setting("Size", Integer.valueOf(250), Integer.valueOf(1), Integer.valueOf(1000)));
/*    */   
/*    */   public ExtraTab() {
/* 16 */     super("ExtraTab", "Extends Tab.", Module.Category.MISC, false, false, false);
/* 17 */     setInstance();
/*    */   }
/*    */ 
/*    */   
/*    */   public static String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn) {
/* 22 */     String name = (networkPlayerInfoIn.func_178854_k() != null) ? networkPlayerInfoIn.func_178854_k().func_150254_d() : ScorePlayerTeam.func_96667_a((Team)networkPlayerInfoIn.func_178850_i(), networkPlayerInfoIn.func_178845_a().getName()), string = name;
/* 23 */     if (Phobos.friendManager.isFriend(name)) {
/* 24 */       return "§b" + name;
/*    */     }
/* 26 */     return name;
/*    */   }
/*    */   
/*    */   public static ExtraTab getINSTANCE() {
/* 30 */     if (INSTANCE == null) {
/* 31 */       INSTANCE = new ExtraTab();
/*    */     }
/* 33 */     return INSTANCE;
/*    */   }
/*    */   
/*    */   private void setInstance() {
/* 37 */     INSTANCE = this;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\misc\ExtraTab.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */