package org.spongepowered.tools.obfuscation.mapping;

import java.io.File;
import java.io.IOException;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;

public interface IMappingProvider {
  void clear();
  
  boolean isEmpty();
  
  void read(File paramFile) throws IOException;
  
  MappingMethod getMethodMapping(MappingMethod paramMappingMethod);
  
  MappingField getFieldMapping(MappingField paramMappingField);
  
  String getClassMapping(String paramString);
  
  String getPackageMapping(String paramString);
}


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\org\spongepowered\tools\obfuscation\mapping\IMappingProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */