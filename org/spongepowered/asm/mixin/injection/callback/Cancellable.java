package org.spongepowered.asm.mixin.injection.callback;

public interface Cancellable {
  boolean isCancellable();
  
  boolean isCancelled();
  
  void cancel() throws CancellationException;
}


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\org\spongepowered\asm\mixin\injection\callback\Cancellable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */