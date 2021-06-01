/*    */ package me.earth.phobos.features.modules.render;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import me.earth.phobos.event.events.Render3DEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.modules.client.Colors;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.ColorUtil;
/*    */ import me.earth.phobos.util.MathUtil;
/*    */ import me.earth.phobos.util.RenderUtil;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.item.EntityItemFrame;
/*    */ import net.minecraft.tileentity.TileEntity;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StorageESP
/*    */   extends Module
/*    */ {
/* 23 */   private final Setting<Float> range = register(new Setting("Range", Float.valueOf(50.0F), Float.valueOf(1.0F), Float.valueOf(300.0F)));
/* 24 */   private final Setting<Boolean> colorSync = register(new Setting("Sync", Boolean.valueOf(false)));
/* 25 */   private final Setting<Boolean> chest = register(new Setting("Chest", Boolean.valueOf(true)));
/* 26 */   private final Setting<Boolean> dispenser = register(new Setting("Dispenser", Boolean.valueOf(false)));
/* 27 */   private final Setting<Boolean> shulker = register(new Setting("Shulker", Boolean.valueOf(true)));
/* 28 */   private final Setting<Boolean> echest = register(new Setting("Ender Chest", Boolean.valueOf(true)));
/* 29 */   private final Setting<Boolean> furnace = register(new Setting("Furnace", Boolean.valueOf(false)));
/* 30 */   private final Setting<Boolean> hopper = register(new Setting("Hopper", Boolean.valueOf(false)));
/* 31 */   private final Setting<Boolean> cart = register(new Setting("Minecart", Boolean.valueOf(false)));
/* 32 */   private final Setting<Boolean> frame = register(new Setting("Item Frame", Boolean.valueOf(false)));
/* 33 */   private final Setting<Boolean> box = register(new Setting("Box", Boolean.valueOf(false)));
/* 34 */   private final Setting<Integer> boxAlpha = register(new Setting("BoxAlpha", Integer.valueOf(125), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.box.getValue()).booleanValue()));
/* 35 */   private final Setting<Boolean> outline = register(new Setting("Outline", Boolean.valueOf(true)));
/* 36 */   private final Setting<Float> lineWidth = register(new Setting("LineWidth", Float.valueOf(1.0F), Float.valueOf(0.1F), Float.valueOf(5.0F), v -> ((Boolean)this.outline.getValue()).booleanValue()));
/*    */   
/*    */   public StorageESP() {
/* 39 */     super("StorageESP", "Highlights Containers.", Module.Category.RENDER, false, false, false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void onRender3D(Render3DEvent event) {
/* 46 */     HashMap<BlockPos, Integer> positions = new HashMap<>();
/* 47 */     for (TileEntity tileEntity : mc.field_71441_e.field_147482_g) {
/* 48 */       int color; BlockPos pos; if (((!(tileEntity instanceof net.minecraft.tileentity.TileEntityChest) || !((Boolean)this.chest.getValue()).booleanValue()) && (!(tileEntity instanceof net.minecraft.tileentity.TileEntityDispenser) || !((Boolean)this.dispenser.getValue()).booleanValue()) && (!(tileEntity instanceof net.minecraft.tileentity.TileEntityShulkerBox) || !((Boolean)this.shulker.getValue()).booleanValue()) && (!(tileEntity instanceof net.minecraft.tileentity.TileEntityEnderChest) || !((Boolean)this.echest.getValue()).booleanValue()) && (!(tileEntity instanceof net.minecraft.tileentity.TileEntityFurnace) || !((Boolean)this.furnace.getValue()).booleanValue()) && (!(tileEntity instanceof net.minecraft.tileentity.TileEntityHopper) || !((Boolean)this.hopper.getValue()).booleanValue())) || mc.field_71439_g.func_174818_b(pos = tileEntity.func_174877_v()) > MathUtil.square(((Float)this.range.getValue()).floatValue()) || (color = getTileEntityColor(tileEntity)) == -1)
/*    */         continue; 
/* 50 */       positions.put(pos, Integer.valueOf(color));
/*    */     } 
/* 52 */     for (Entity entity : mc.field_71441_e.field_72996_f) {
/* 53 */       int color; BlockPos pos; if (((!(entity instanceof EntityItemFrame) || !((Boolean)this.frame.getValue()).booleanValue()) && (!(entity instanceof net.minecraft.entity.item.EntityMinecartChest) || !((Boolean)this.cart.getValue()).booleanValue())) || mc.field_71439_g.func_174818_b(pos = entity.func_180425_c()) > MathUtil.square(((Float)this.range.getValue()).floatValue()) || (color = getEntityColor(entity)) == -1)
/*    */         continue; 
/* 55 */       positions.put(pos, Integer.valueOf(color));
/*    */     } 
/* 57 */     for (Map.Entry<BlockPos, Integer> entry : positions.entrySet()) {
/* 58 */       BlockPos blockPos = (BlockPos)entry.getKey();
/* 59 */       int color = ((Integer)entry.getValue()).intValue();
/* 60 */       RenderUtil.drawBoxESP(blockPos, ((Boolean)this.colorSync.getValue()).booleanValue() ? Colors.INSTANCE.getCurrentColor() : new Color(color), false, new Color(color), ((Float)this.lineWidth.getValue()).floatValue(), ((Boolean)this.outline.getValue()).booleanValue(), ((Boolean)this.box.getValue()).booleanValue(), ((Integer)this.boxAlpha.getValue()).intValue(), false);
/*    */     } 
/*    */   }
/*    */   
/*    */   private int getTileEntityColor(TileEntity tileEntity) {
/* 65 */     if (tileEntity instanceof net.minecraft.tileentity.TileEntityChest) {
/* 66 */       return ColorUtil.Colors.BLUE;
/*    */     }
/* 68 */     if (tileEntity instanceof net.minecraft.tileentity.TileEntityShulkerBox) {
/* 69 */       return ColorUtil.Colors.RED;
/*    */     }
/* 71 */     if (tileEntity instanceof net.minecraft.tileentity.TileEntityEnderChest) {
/* 72 */       return ColorUtil.Colors.PURPLE;
/*    */     }
/* 74 */     if (tileEntity instanceof net.minecraft.tileentity.TileEntityFurnace) {
/* 75 */       return ColorUtil.Colors.GRAY;
/*    */     }
/* 77 */     if (tileEntity instanceof net.minecraft.tileentity.TileEntityHopper) {
/* 78 */       return ColorUtil.Colors.DARK_RED;
/*    */     }
/* 80 */     if (tileEntity instanceof net.minecraft.tileentity.TileEntityDispenser) {
/* 81 */       return ColorUtil.Colors.ORANGE;
/*    */     }
/* 83 */     return -1;
/*    */   }
/*    */   
/*    */   private int getEntityColor(Entity entity) {
/* 87 */     if (entity instanceof net.minecraft.entity.item.EntityMinecartChest) {
/* 88 */       return ColorUtil.Colors.ORANGE;
/*    */     }
/* 90 */     if (entity instanceof EntityItemFrame && ((EntityItemFrame)entity).func_82335_i().func_77973_b() instanceof net.minecraft.item.ItemShulkerBox) {
/* 91 */       return ColorUtil.Colors.YELLOW;
/*    */     }
/* 93 */     if (entity instanceof EntityItemFrame && !(((EntityItemFrame)entity).func_82335_i().func_77973_b() instanceof net.minecraft.item.ItemShulkerBox)) {
/* 94 */       return ColorUtil.Colors.ORANGE;
/*    */     }
/* 96 */     return -1;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\render\StorageESP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */