package org.spongepowered.asm.mixin.transformer.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MixinMerged {
  String mixin();
  
  int priority();
}


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\org\spongepowered\asm\mixin\transformer\meta\MixinMerged.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */