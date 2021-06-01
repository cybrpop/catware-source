/*    */ package me.earth.phobos.features.setting;
/*    */ 
/*    */ import com.google.common.base.Converter;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonPrimitive;
/*    */ import org.lwjgl.input.Keyboard;
/*    */ 
/*    */ public class Bind {
/*    */   private int key;
/*    */   
/*    */   public Bind(int key) {
/* 12 */     this.key = key;
/*    */   }
/*    */   
/*    */   public static Bind none() {
/* 16 */     return new Bind(-1);
/*    */   }
/*    */   
/*    */   public int getKey() {
/* 20 */     return this.key;
/*    */   }
/*    */   
/*    */   public void setKey(int key) {
/* 24 */     this.key = key;
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 28 */     return (this.key < 0);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 32 */     return isEmpty() ? "None" : ((this.key < 0) ? "None" : capitalise(Keyboard.getKeyName(this.key)));
/*    */   }
/*    */   
/*    */   public boolean isDown() {
/* 36 */     return (!isEmpty() && Keyboard.isKeyDown(getKey()));
/*    */   }
/*    */   
/*    */   private String capitalise(String str) {
/* 40 */     if (str.isEmpty()) {
/* 41 */       return "";
/*    */     }
/* 43 */     return Character.toUpperCase(str.charAt(0)) + ((str.length() != 1) ? str.substring(1).toLowerCase() : "");
/*    */   }
/*    */   
/*    */   public static class BindConverter
/*    */     extends Converter<Bind, JsonElement> {
/*    */     public JsonElement doForward(Bind bind) {
/* 49 */       return (JsonElement)new JsonPrimitive(bind.toString());
/*    */     }
/*    */     
/*    */     public Bind doBackward(JsonElement jsonElement) {
/* 53 */       String s = jsonElement.getAsString();
/* 54 */       if (s.equalsIgnoreCase("None")) {
/* 55 */         return Bind.none();
/*    */       }
/* 57 */       int key = -1;
/*    */       try {
/* 59 */         key = Keyboard.getKeyIndex(s.toUpperCase());
/* 60 */       } catch (Exception exception) {}
/*    */ 
/*    */       
/* 63 */       if (key == 0) {
/* 64 */         return Bind.none();
/*    */       }
/* 66 */       return new Bind(key);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\setting\Bind.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */