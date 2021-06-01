/*    */ package me.earth.phobos.features.setting;
/*    */ 
/*    */ import com.google.common.base.Converter;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonPrimitive;
/*    */ 
/*    */ public class EnumConverter
/*    */   extends Converter<Enum, JsonElement> {
/*    */   private final Class<? extends Enum> clazz;
/*    */   
/*    */   public EnumConverter(Class<? extends Enum> clazz) {
/* 12 */     this.clazz = clazz;
/*    */   }
/*    */   
/*    */   public static int currentEnum(Enum clazz) {
/* 16 */     for (int i = 0; i < ((Enum[])clazz.getClass().getEnumConstants()).length; ) {
/* 17 */       Enum e = ((Enum[])clazz.getClass().getEnumConstants())[i];
/* 18 */       if (!e.name().equalsIgnoreCase(clazz.name())) { i++; continue; }
/* 19 */        return i;
/*    */     } 
/* 21 */     return -1;
/*    */   }
/*    */   
/*    */   public static Enum increaseEnum(Enum clazz) {
/* 25 */     int index = currentEnum(clazz);
/* 26 */     for (int i = 0; i < ((Enum[])clazz.getClass().getEnumConstants()).length; ) {
/* 27 */       Enum e = ((Enum[])clazz.getClass().getEnumConstants())[i];
/* 28 */       if (i != index + 1) { i++; continue; }
/* 29 */        return e;
/*    */     } 
/* 31 */     return ((Enum[])clazz.getClass().getEnumConstants())[0];
/*    */   }
/*    */   
/*    */   public static String getProperName(Enum clazz) {
/* 35 */     return Character.toUpperCase(clazz.name().charAt(0)) + clazz.name().toLowerCase().substring(1);
/*    */   }
/*    */   
/*    */   public JsonElement doForward(Enum anEnum) {
/* 39 */     return (JsonElement)new JsonPrimitive(anEnum.toString());
/*    */   }
/*    */   
/*    */   public Enum doBackward(JsonElement jsonElement) {
/*    */     try {
/* 44 */       return Enum.valueOf((Class)this.clazz, jsonElement.getAsString());
/* 45 */     } catch (IllegalArgumentException e) {
/* 46 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\setting\EnumConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */