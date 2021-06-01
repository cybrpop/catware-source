package org.spongepowered.asm.service;

import java.io.IOException;
import org.spongepowered.asm.lib.tree.ClassNode;

public interface IClassBytecodeProvider {
  byte[] getClassBytes(String paramString1, String paramString2) throws IOException;
  
  byte[] getClassBytes(String paramString, boolean paramBoolean) throws ClassNotFoundException, IOException;
  
  ClassNode getClassNode(String paramString) throws ClassNotFoundException, IOException;
}


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\org\spongepowered\asm\service\IClassBytecodeProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */