/*    */ package me.earth.phobos.util;
/*    */ 
/*    */ import java.lang.reflect.AccessibleObject;
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Member;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class ReflectionUtil {
/*    */   public static <F, T extends F> void copyOf(F from, T to, boolean ignoreFinal) throws NoSuchFieldException, IllegalAccessException {
/* 10 */     Objects.requireNonNull(from);
/* 11 */     Objects.requireNonNull(to);
/* 12 */     Class<?> clazz = from.getClass();
/* 13 */     for (Field field : clazz.getDeclaredFields()) {
/* 14 */       makePublic(field);
/* 15 */       if (!isStatic(field) && (!ignoreFinal || !isFinal(field))) {
/* 16 */         makeMutable(field);
/* 17 */         field.set(to, field.get(from));
/*    */       } 
/*    */     } 
/*    */   }
/*    */   public static <F, T extends F> void copyOf(F from, T to) throws NoSuchFieldException, IllegalAccessException {
/* 22 */     copyOf(from, to, false);
/*    */   }
/*    */   
/*    */   public static boolean isStatic(Member instance) {
/* 26 */     return ((instance.getModifiers() & 0x8) != 0);
/*    */   }
/*    */   
/*    */   public static boolean isFinal(Member instance) {
/* 30 */     return ((instance.getModifiers() & 0x10) != 0);
/*    */   }
/*    */   
/*    */   public static void makeAccessible(AccessibleObject instance, boolean accessible) {
/* 34 */     Objects.requireNonNull(instance);
/* 35 */     instance.setAccessible(accessible);
/*    */   }
/*    */   
/*    */   public static void makePublic(AccessibleObject instance) {
/* 39 */     makeAccessible(instance, true);
/*    */   }
/*    */   
/*    */   public static void makePrivate(AccessibleObject instance) {
/* 43 */     makeAccessible(instance, false);
/*    */   }
/*    */   
/*    */   public static void makeMutable(Member instance) throws NoSuchFieldException, IllegalAccessException {
/* 47 */     Objects.requireNonNull(instance);
/* 48 */     Field modifiers = Field.class.getDeclaredField("modifiers");
/* 49 */     makePublic(modifiers);
/* 50 */     modifiers.setInt(instance, instance.getModifiers() & 0xFFFFFFEF);
/*    */   }
/*    */   
/*    */   public static void makeImmutable(Member instance) throws NoSuchFieldException, IllegalAccessException {
/* 54 */     Objects.requireNonNull(instance);
/* 55 */     Field modifiers = Field.class.getDeclaredField("modifiers");
/* 56 */     makePublic(modifiers);
/* 57 */     modifiers.setInt(instance, instance.getModifiers() & 0x10);
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobo\\util\ReflectionUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */