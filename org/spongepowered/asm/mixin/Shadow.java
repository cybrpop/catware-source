package org.spongepowered.asm.mixin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Shadow {
  String prefix() default "shadow$";
  
  boolean remap() default true;
  
  String[] aliases() default {};
}


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\org\spongepowered\asm\mixin\Shadow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */