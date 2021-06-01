/*    */ package org.spongepowered.asm.mixin.injection.callback;
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
/*    */ 
/*    */ public class CancellationException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public CancellationException() {}
/*    */   
/*    */   public CancellationException(String message) {
/* 39 */     super(message);
/*    */   }
/*    */   
/*    */   public CancellationException(Throwable cause) {
/* 43 */     super(cause);
/*    */   }
/*    */   
/*    */   public CancellationException(String message, Throwable cause) {
/* 47 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\org\spongepowered\asm\mixin\injection\callback\CancellationException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */