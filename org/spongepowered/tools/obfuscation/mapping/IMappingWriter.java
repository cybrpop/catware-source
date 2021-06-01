package org.spongepowered.tools.obfuscation.mapping;

import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.tools.obfuscation.ObfuscationType;

public interface IMappingWriter {
  void write(String paramString, ObfuscationType paramObfuscationType, IMappingConsumer.MappingSet<MappingField> paramMappingSet, IMappingConsumer.MappingSet<MappingMethod> paramMappingSet1);
}


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\org\spongepowered\tools\obfuscation\mapping\IMappingWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */