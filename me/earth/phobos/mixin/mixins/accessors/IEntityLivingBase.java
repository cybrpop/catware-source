package me.earth.phobos.mixin.mixins.accessors;

import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({EntityLivingBase.class})
public interface IEntityLivingBase {
  @Invoker("getArmSwingAnimationEnd")
  int getArmSwingAnimationEnd();
}


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\mixins\accessors\IEntityLivingBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */