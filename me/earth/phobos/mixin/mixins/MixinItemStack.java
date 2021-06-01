/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import me.earth.phobos.features.modules.player.TrueDurability;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import org.spongepowered.asm.mixin.Dynamic;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Shadow;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ 
/*    */ @Mixin({ItemStack.class})
/*    */ public abstract class MixinItemStack {
/*    */   @Shadow
/*    */   private int field_77991_e;
/*    */   
/*    */   @Inject(method = {"<init>(Lnet/minecraft/item/Item;IILnet/minecraft/nbt/NBTTagCompound;)V"}, at = {@At("RETURN")})
/*    */   @Dynamic
/*    */   private void initHook(Item item, int idkWhatDisIsIPastedThis, int dura, NBTTagCompound compound, CallbackInfo info) {
/* 22 */     this.field_77991_e = checkDurability(ItemStack.class.cast(this), this.field_77991_e, dura);
/*    */   }
/*    */   
/*    */   @Inject(method = {"<init>(Lnet/minecraft/nbt/NBTTagCompound;)V"}, at = {@At("RETURN")})
/*    */   private void initHook2(NBTTagCompound compound, CallbackInfo info) {
/* 27 */     this.field_77991_e = checkDurability(ItemStack.class.cast(this), this.field_77991_e, compound.func_74765_d("Damage"));
/*    */   }
/*    */   
/*    */   private int checkDurability(ItemStack item, int damage, int dura) {
/* 31 */     int trueDura = damage;
/* 32 */     if (TrueDurability.getInstance().isOn() && dura < 0) {
/* 33 */       trueDura = dura;
/*    */     }
/* 35 */     return trueDura;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\mixins\MixinItemStack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */