/*    */ package me.earth.phobos.util;
/*    */ 
/*    */ import me.earth.phobos.manager.FileManager;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class Wrapper {
/*    */   public static FileManager fileManager;
/*    */   
/*    */   public static Minecraft getMinecraft() {
/* 12 */     return Minecraft.func_71410_x();
/*    */   }
/*    */   public static EntityPlayerSP getPlayer() {
/* 15 */     return (getMinecraft()).field_71439_g;
/*    */   }
/*    */   public static World getWorld() {
/* 18 */     return (World)(getMinecraft()).field_71441_e;
/*    */   }
/*    */ 
/*    */   
/*    */   public static FileManager getFileManager() {
/* 23 */     if (fileManager == null) {
/* 24 */       fileManager = new FileManager();
/*    */     }
/* 26 */     return fileManager;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobo\\util\Wrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */