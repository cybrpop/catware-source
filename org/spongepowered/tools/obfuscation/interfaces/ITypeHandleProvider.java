package org.spongepowered.tools.obfuscation.interfaces;

import javax.lang.model.type.TypeMirror;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;

public interface ITypeHandleProvider {
  TypeHandle getTypeHandle(String paramString);
  
  TypeHandle getSimulatedHandle(String paramString, TypeMirror paramTypeMirror);
}


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\org\spongepowered\tools\obfuscation\interfaces\ITypeHandleProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */