/*    */ package me.earth.phobos.features.modules.client;
/*    */ 
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.passive.EntityOcelot;
/*    */ import net.minecraft.nbt.NBTBase;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.nbt.NBTTagInt;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraftforge.client.event.RenderPlayerEvent;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class ShoulderEntity
/*    */   extends Module {
/* 16 */   private static final ResourceLocation BLACK_OCELOT_TEXTURES = new ResourceLocation("textures/entity/cat/black.png");
/*    */   
/*    */   public ShoulderEntity() {
/* 19 */     super("ShoulderEntity", "Test", Module.Category.CLIENT, true, true, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 24 */     mc.field_71441_e.func_73027_a(-101, (Entity)new EntityOcelot((World)mc.field_71441_e));
/* 25 */     NBTTagCompound tag = new NBTTagCompound();
/* 26 */     tag.func_74782_a("id", (NBTBase)new NBTTagInt(-101));
/* 27 */     mc.field_71439_g.func_192027_g(tag);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 32 */     mc.field_71441_e.func_73028_b(-101);
/*    */   }
/*    */ 
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onRenderPlayer(RenderPlayerEvent.Post event) {}
/*    */   
/*    */   public float interpolate(float yaw1, float yaw2, float percent) {
/* 40 */     float rotation = (yaw1 + (yaw2 - yaw1) * percent) % 360.0F;
/* 41 */     if (rotation < 0.0F) {
/* 42 */       rotation += 360.0F;
/*    */     }
/* 44 */     return rotation;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\client\ShoulderEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */