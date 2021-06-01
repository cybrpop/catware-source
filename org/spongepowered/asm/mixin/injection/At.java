/*    */ package org.spongepowered.asm.mixin.injection;
/*    */ 
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Retention(RetentionPolicy.RUNTIME)
/*    */ public @interface At
/*    */ {
/*    */   String id() default "";
/*    */   
/*    */   String value();
/*    */   
/*    */   String slice() default "";
/*    */   
/*    */   Shift shift() default Shift.NONE;
/*    */   
/*    */   int by() default 0;
/*    */   
/*    */   String[] args() default {};
/*    */   
/*    */   String target() default "";
/*    */   
/*    */   int ordinal() default -1;
/*    */   
/*    */   int opcode() default -1;
/*    */   
/*    */   boolean remap() default true;
/*    */   
/*    */   public enum Shift
/*    */   {
/* 58 */     NONE,
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 63 */     BEFORE,
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 68 */     AFTER,
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 73 */     BY;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\org\spongepowered\asm\mixin\injection\At.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */