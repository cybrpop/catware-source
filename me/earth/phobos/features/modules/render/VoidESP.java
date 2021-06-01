/*    */ package me.earth.phobos.features.modules.render;
/*    */ import java.awt.Color;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CopyOnWriteArrayList;
/*    */ import java.util.stream.Collectors;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.BlockUtil;
/*    */ import me.earth.phobos.util.EntityUtil;
/*    */ import me.earth.phobos.util.RenderUtil;
/*    */ import me.earth.phobos.util.RotationUtil;
/*    */ import me.earth.phobos.util.Timer;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ 
/*    */ public class VoidESP extends Module {
/* 18 */   private final Setting<Float> radius = register(new Setting("Radius", Float.valueOf(8.0F), Float.valueOf(0.0F), Float.valueOf(50.0F)));
/* 19 */   private final Timer timer = new Timer();
/* 20 */   public Setting<Boolean> air = register(new Setting("OnlyAir", Boolean.valueOf(true)));
/* 21 */   public Setting<Boolean> noEnd = register(new Setting("NoEnd", Boolean.valueOf(true)));
/* 22 */   public Setting<Boolean> box = register(new Setting("Box", Boolean.valueOf(true)));
/* 23 */   public Setting<Boolean> outline = register(new Setting("Outline", Boolean.valueOf(true)));
/* 24 */   public Setting<Boolean> colorSync = register(new Setting("Sync", Boolean.valueOf(false)));
/* 25 */   public Setting<Double> height = register(new Setting("Height", Double.valueOf(0.0D), Double.valueOf(-2.0D), Double.valueOf(2.0D)));
/* 26 */   public Setting<Boolean> customOutline = register(new Setting("CustomLine", Boolean.valueOf(false), v -> ((Boolean)this.outline.getValue()).booleanValue()));
/* 27 */   private final Setting<Integer> updates = register(new Setting("Updates", Integer.valueOf(500), Integer.valueOf(0), Integer.valueOf(1000)));
/* 28 */   private final Setting<Integer> voidCap = register(new Setting("VoidCap", Integer.valueOf(500), Integer.valueOf(0), Integer.valueOf(1000)));
/* 29 */   private final Setting<Integer> red = register(new Setting("Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255)));
/* 30 */   private final Setting<Integer> green = register(new Setting("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/* 31 */   private final Setting<Integer> blue = register(new Setting("Blue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255)));
/* 32 */   private final Setting<Integer> alpha = register(new Setting("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/* 33 */   private final Setting<Integer> boxAlpha = register(new Setting("BoxAlpha", Integer.valueOf(125), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.box.getValue()).booleanValue()));
/* 34 */   private final Setting<Float> lineWidth = register(new Setting("LineWidth", Float.valueOf(1.0F), Float.valueOf(0.1F), Float.valueOf(5.0F), v -> ((Boolean)this.outline.getValue()).booleanValue()));
/* 35 */   private final Setting<Integer> cRed = register(new Setting("OL-Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.customOutline.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue())));
/* 36 */   private final Setting<Integer> cGreen = register(new Setting("OL-Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.customOutline.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue())));
/* 37 */   private final Setting<Integer> cBlue = register(new Setting("OL-Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.customOutline.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue())));
/* 38 */   private final Setting<Integer> cAlpha = register(new Setting("OL-Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.customOutline.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue())));
/* 39 */   private List<BlockPos> voidHoles = new CopyOnWriteArrayList<>();
/*    */   
/*    */   public VoidESP() {
/* 42 */     super("VoidEsp", "Esps the void", Module.Category.RENDER, true, false, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onToggle() {
/* 47 */     this.timer.reset();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onLogin() {
/* 52 */     this.timer.reset();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onTick() {
/* 57 */     if (!fullNullCheck() && (!((Boolean)this.noEnd.getValue()).booleanValue() || mc.field_71439_g.field_71093_bK != 1) && this.timer.passedMs(((Integer)this.updates.getValue()).intValue())) {
/* 58 */       this.voidHoles.clear();
/* 59 */       this.voidHoles = findVoidHoles();
/* 60 */       if (this.voidHoles.size() > ((Integer)this.voidCap.getValue()).intValue()) {
/* 61 */         this.voidHoles.clear();
/*    */       }
/* 63 */       this.timer.reset();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onRender3D(Render3DEvent event) {
/* 69 */     if (fullNullCheck() || (((Boolean)this.noEnd.getValue()).booleanValue() && mc.field_71439_g.field_71093_bK == 1)) {
/*    */       return;
/*    */     }
/* 72 */     for (BlockPos pos : this.voidHoles) {
/* 73 */       if (!RotationUtil.isInFov(pos))
/* 74 */         continue;  RenderUtil.drawBoxESP(pos, new Color(((Integer)this.red.getValue()).intValue(), ((Integer)this.green.getValue()).intValue(), ((Integer)this.blue.getValue()).intValue(), ((Integer)this.alpha.getValue()).intValue()), ((Boolean)this.customOutline.getValue()).booleanValue(), new Color(((Integer)this.cRed.getValue()).intValue(), ((Integer)this.cGreen.getValue()).intValue(), ((Integer)this.cBlue.getValue()).intValue(), ((Integer)this.cAlpha.getValue()).intValue()), ((Float)this.lineWidth.getValue()).floatValue(), ((Boolean)this.outline.getValue()).booleanValue(), ((Boolean)this.box.getValue()).booleanValue(), ((Integer)this.boxAlpha.getValue()).intValue(), true, ((Double)this.height.getValue()).doubleValue(), false, false, false, false, 0);
/*    */     } 
/*    */   }
/*    */   
/*    */   private List<BlockPos> findVoidHoles() {
/* 79 */     BlockPos playerPos = EntityUtil.getPlayerPos((EntityPlayer)mc.field_71439_g);
/* 80 */     return (List<BlockPos>)BlockUtil.getDisc(playerPos.func_177982_a(0, -playerPos.func_177956_o(), 0), ((Float)this.radius.getValue()).floatValue()).stream().filter(this::isVoid).collect(Collectors.toList());
/*    */   }
/*    */   
/*    */   private boolean isVoid(BlockPos pos) {
/* 84 */     return ((mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150350_a || (!((Boolean)this.air.getValue()).booleanValue() && mc.field_71441_e.func_180495_p(pos).func_177230_c() != Blocks.field_150357_h)) && pos.func_177956_o() < 1 && pos.func_177956_o() >= 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\render\VoidESP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */