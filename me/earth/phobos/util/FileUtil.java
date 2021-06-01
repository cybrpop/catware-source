/*    */ package me.earth.phobos.util;
/*    */ import java.io.IOException;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.StandardOpenOption;
/*    */ import java.util.Collections;
/*    */ 
/*    */ public class FileUtil {
/*    */   public static boolean appendTextFile(String data, String file) {
/*    */     try {
/* 12 */       Path path = Paths.get(file, new String[0]);
/* 13 */       Files.write(path, Collections.singletonList(data), StandardCharsets.UTF_8, new OpenOption[] { Files.exists(path, new java.nio.file.LinkOption[0]) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE });
/* 14 */     } catch (IOException e) {
/* 15 */       System.out.println("WARNING: Unable to write file: " + file);
/* 16 */       return false;
/*    */     } 
/* 18 */     return true;
/*    */   }
/*    */   
/*    */   public static List<String> readTextFileAllLines(String file) {
/*    */     try {
/* 23 */       Path path = Paths.get(file, new String[0]);
/* 24 */       return Files.readAllLines(path, StandardCharsets.UTF_8);
/* 25 */     } catch (IOException e) {
/* 26 */       System.out.println("WARNING: Unable to read file, creating new file: " + file);
/* 27 */       appendTextFile("", file);
/* 28 */       return Collections.emptyList();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobo\\util\FileUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */