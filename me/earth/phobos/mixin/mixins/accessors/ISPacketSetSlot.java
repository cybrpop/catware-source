package me.earth.phobos.mixin.mixins.accessors;

import net.minecraft.network.play.server.SPacketSetSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({SPacketSetSlot.class})
public interface ISPacketSetSlot {
  @Accessor("windowId")
  int getId();
  
  @Accessor("windowId")
  void setWindowId(int paramInt);
}


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\mixins\accessors\ISPacketSetSlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */