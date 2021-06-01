/*    */ package me.earth.phobos.features.modules.player;
/*    */ 
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.InventoryUtil;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.ItemEnderPearl;
/*    */ import net.minecraft.util.EnumHand;
/*    */ import net.minecraft.util.math.RayTraceResult;
/*    */ import net.minecraft.world.World;
/*    */ import org.lwjgl.input.Mouse;
/*    */ 
/*    */ public class MCP
/*    */   extends Module {
/* 17 */   private final Setting<Mode> mode = register(new Setting("Mode", Mode.MIDDLECLICK));
/* 18 */   private final Setting<Boolean> stopRotation = register(new Setting("Rotation", Boolean.valueOf(true)));
/* 19 */   private final Setting<Boolean> antiFriend = register(new Setting("AntiFriend", Boolean.valueOf(true)));
/* 20 */   private final Setting<Integer> rotation = register(new Setting("Delay", Integer.valueOf(10), Integer.valueOf(0), Integer.valueOf(100), v -> ((Boolean)this.stopRotation.getValue()).booleanValue()));
/*    */   private boolean clicked = false;
/*    */   
/*    */   public MCP() {
/* 24 */     super("MCP", "Throws a pearl", Module.Category.PLAYER, false, false, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 29 */     if (!fullNullCheck() && this.mode.getValue() == Mode.TOGGLE) {
/* 30 */       throwPearl();
/* 31 */       disable();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onTick() {
/* 37 */     if (this.mode.getValue() == Mode.MIDDLECLICK) {
/* 38 */       if (Mouse.isButtonDown(2)) {
/* 39 */         if (!this.clicked) {
/* 40 */           throwPearl();
/*    */         }
/* 42 */         this.clicked = true;
/*    */       } else {
/* 44 */         this.clicked = false;
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   private void throwPearl() {
/*    */     Entity entity;
/*    */     RayTraceResult result;
/* 53 */     if (((Boolean)this.antiFriend.getValue()).booleanValue() && (result = mc.field_71476_x) != null && result.field_72313_a == RayTraceResult.Type.ENTITY && entity = result.field_72308_g instanceof EntityPlayer) {
/*    */       return;
/*    */     }
/* 56 */     int pearlSlot = InventoryUtil.findHotbarBlock(ItemEnderPearl.class);
/* 57 */     boolean offhand = (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151079_bi), bl = offhand;
/* 58 */     if (pearlSlot != -1 || offhand) {
/* 59 */       int oldslot = mc.field_71439_g.field_71071_by.field_70461_c;
/* 60 */       if (!offhand) {
/* 61 */         InventoryUtil.switchToHotbarSlot(pearlSlot, false);
/*    */       }
/* 63 */       mc.field_71442_b.func_187101_a((EntityPlayer)mc.field_71439_g, (World)mc.field_71441_e, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
/* 64 */       if (!offhand)
/* 65 */         InventoryUtil.switchToHotbarSlot(oldslot, false); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public enum Mode
/*    */   {
/* 71 */     TOGGLE,
/* 72 */     MIDDLECLICK;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\player\MCP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */