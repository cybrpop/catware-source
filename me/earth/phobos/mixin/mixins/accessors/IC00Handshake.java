package me.earth.phobos.mixin.mixins.accessors;

import net.minecraft.network.handshake.client.C00Handshake;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({C00Handshake.class})
public interface IC00Handshake {
  @Accessor("ip")
  String getIp();
  
  @Accessor("ip")
  void setIp(String paramString);
}


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\mixins\accessors\IC00Handshake.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */