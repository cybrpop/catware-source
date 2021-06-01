package me.earth.phobos.mixin.mixins.accessors;

import net.minecraft.inventory.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({Container.class})
public interface IContainer {
  @Accessor("transactionID")
  void setTransactionID(short paramShort);
}


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\mixins\accessors\IContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */