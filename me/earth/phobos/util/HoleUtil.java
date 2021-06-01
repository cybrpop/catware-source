/*     */ package me.earth.phobos.util;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ 
/*     */ public class HoleUtil
/*     */ {
/*  13 */   private static final Minecraft mc = Minecraft.func_71410_x();
/*     */   
/*     */   public static BlockSafety isBlockSafe(Block block) {
/*  16 */     if (block == Blocks.field_150357_h) {
/*  17 */       return BlockSafety.UNBREAKABLE;
/*     */     }
/*  19 */     if (block == Blocks.field_150343_Z || block == Blocks.field_150477_bB || block == Blocks.field_150467_bQ) {
/*  20 */       return BlockSafety.RESISTANT;
/*     */     }
/*  22 */     return BlockSafety.BREAKABLE;
/*     */   }
/*     */   
/*     */   public static HoleInfo isHole(BlockPos centreBlock, boolean onlyOneWide, boolean ignoreDown) {
/*  26 */     HoleInfo output = new HoleInfo();
/*  27 */     HashMap<BlockOffset, BlockSafety> unsafeSides = getUnsafeSides(centreBlock);
/*     */     
/*  29 */     if (unsafeSides.containsKey(BlockOffset.DOWN) && 
/*  30 */       unsafeSides.remove(BlockOffset.DOWN, BlockSafety.BREAKABLE) && 
/*  31 */       !ignoreDown) {
/*  32 */       output.setSafety(BlockSafety.BREAKABLE);
/*  33 */       return output;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  38 */     int size = unsafeSides.size();
/*     */     
/*  40 */     unsafeSides.entrySet().removeIf(entry -> (entry.getValue() == BlockSafety.RESISTANT));
/*     */ 
/*     */     
/*  43 */     if (unsafeSides.size() != size) {
/*  44 */       output.setSafety(BlockSafety.RESISTANT);
/*     */     }
/*     */     
/*  47 */     size = unsafeSides.size();
/*     */ 
/*     */     
/*  50 */     if (size == 0) {
/*  51 */       output.setType(HoleType.SINGLE);
/*  52 */       output.setCentre(new AxisAlignedBB(centreBlock));
/*  53 */       return output;
/*     */     } 
/*     */     
/*  56 */     if (size == 1 && !onlyOneWide) {
/*  57 */       return isDoubleHole(output, centreBlock, unsafeSides.keySet().stream().findFirst().get());
/*     */     }
/*  59 */     output.setSafety(BlockSafety.BREAKABLE);
/*  60 */     return output;
/*     */   }
/*     */ 
/*     */   
/*     */   private static HoleInfo isDoubleHole(HoleInfo info, BlockPos centreBlock, BlockOffset weakSide) {
/*  65 */     BlockPos unsafePos = weakSide.offset(centreBlock);
/*     */     
/*  67 */     HashMap<BlockOffset, BlockSafety> unsafeSides = getUnsafeSides(unsafePos);
/*     */     
/*  69 */     int size = unsafeSides.size();
/*     */     
/*  71 */     unsafeSides.entrySet().removeIf(entry -> (entry.getValue() == BlockSafety.RESISTANT));
/*     */ 
/*     */     
/*  74 */     if (unsafeSides.size() != size) {
/*  75 */       info.setSafety(BlockSafety.RESISTANT);
/*     */     }
/*     */     
/*  78 */     if (unsafeSides.containsKey(BlockOffset.DOWN)) {
/*  79 */       info.setType(HoleType.CUSTOM);
/*  80 */       unsafeSides.remove(BlockOffset.DOWN);
/*     */     } 
/*     */ 
/*     */     
/*  84 */     if (unsafeSides.size() > 1) {
/*  85 */       info.setType(HoleType.NONE);
/*  86 */       return info;
/*     */     } 
/*     */ 
/*     */     
/*  90 */     double minX = Math.min(centreBlock.func_177958_n(), unsafePos.func_177958_n());
/*  91 */     double maxX = (Math.max(centreBlock.func_177958_n(), unsafePos.func_177958_n()) + 1);
/*  92 */     double minZ = Math.min(centreBlock.func_177952_p(), unsafePos.func_177952_p());
/*  93 */     double maxZ = (Math.max(centreBlock.func_177952_p(), unsafePos.func_177952_p()) + 1);
/*     */     
/*  95 */     info.setCentre(new AxisAlignedBB(minX, centreBlock.func_177956_o(), minZ, maxX, (centreBlock.func_177956_o() + 1), maxZ));
/*     */     
/*  97 */     if (info.getType() != HoleType.CUSTOM) {
/*  98 */       info.setType(HoleType.DOUBLE);
/*     */     }
/* 100 */     return info;
/*     */   }
/*     */   
/*     */   public static HashMap<BlockOffset, BlockSafety> getUnsafeSides(BlockPos pos) {
/* 104 */     HashMap<BlockOffset, BlockSafety> output = new HashMap<>();
/*     */ 
/*     */     
/* 107 */     BlockSafety temp = isBlockSafe(mc.field_71441_e.func_180495_p(BlockOffset.DOWN.offset(pos)).func_177230_c());
/* 108 */     if (temp != BlockSafety.UNBREAKABLE) {
/* 109 */       output.put(BlockOffset.DOWN, temp);
/*     */     }
/* 111 */     temp = isBlockSafe(mc.field_71441_e.func_180495_p(BlockOffset.NORTH.offset(pos)).func_177230_c());
/* 112 */     if (temp != BlockSafety.UNBREAKABLE) {
/* 113 */       output.put(BlockOffset.NORTH, temp);
/*     */     }
/* 115 */     temp = isBlockSafe(mc.field_71441_e.func_180495_p(BlockOffset.SOUTH.offset(pos)).func_177230_c());
/* 116 */     if (temp != BlockSafety.UNBREAKABLE) {
/* 117 */       output.put(BlockOffset.SOUTH, temp);
/*     */     }
/* 119 */     temp = isBlockSafe(mc.field_71441_e.func_180495_p(BlockOffset.EAST.offset(pos)).func_177230_c());
/* 120 */     if (temp != BlockSafety.UNBREAKABLE) {
/* 121 */       output.put(BlockOffset.EAST, temp);
/*     */     }
/* 123 */     temp = isBlockSafe(mc.field_71441_e.func_180495_p(BlockOffset.WEST.offset(pos)).func_177230_c());
/* 124 */     if (temp != BlockSafety.UNBREAKABLE) {
/* 125 */       output.put(BlockOffset.WEST, temp);
/*     */     }
/* 127 */     return output;
/*     */   }
/*     */   
/*     */   public enum BlockSafety {
/* 131 */     UNBREAKABLE,
/* 132 */     RESISTANT,
/* 133 */     BREAKABLE;
/*     */   }
/*     */   
/*     */   public enum HoleType {
/* 137 */     SINGLE,
/* 138 */     DOUBLE,
/* 139 */     CUSTOM,
/* 140 */     NONE;
/*     */   }
/*     */   
/*     */   public static class HoleInfo
/*     */   {
/*     */     private HoleUtil.HoleType type;
/*     */     private HoleUtil.BlockSafety safety;
/*     */     private AxisAlignedBB centre;
/*     */     
/*     */     public HoleInfo() {
/* 150 */       this(HoleUtil.BlockSafety.UNBREAKABLE, HoleUtil.HoleType.NONE);
/*     */     }
/*     */     
/*     */     public HoleInfo(HoleUtil.BlockSafety safety, HoleUtil.HoleType type) {
/* 154 */       this.type = type;
/* 155 */       this.safety = safety;
/*     */     }
/*     */     
/*     */     public void setType(HoleUtil.HoleType type) {
/* 159 */       this.type = type;
/*     */     }
/*     */     
/*     */     public void setSafety(HoleUtil.BlockSafety safety) {
/* 163 */       this.safety = safety;
/*     */     }
/*     */     
/*     */     public void setCentre(AxisAlignedBB centre) {
/* 167 */       this.centre = centre;
/*     */     }
/*     */     
/*     */     public HoleUtil.HoleType getType() {
/* 171 */       return this.type;
/*     */     }
/*     */     
/*     */     public HoleUtil.BlockSafety getSafety() {
/* 175 */       return this.safety;
/*     */     }
/*     */     
/*     */     public AxisAlignedBB getCentre() {
/* 179 */       return this.centre;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum BlockOffset {
/* 184 */     DOWN(0, -1, 0),
/* 185 */     UP(0, 1, 0),
/* 186 */     NORTH(0, 0, -1),
/* 187 */     EAST(1, 0, 0),
/* 188 */     SOUTH(0, 0, 1),
/* 189 */     WEST(-1, 0, 0);
/*     */     
/*     */     private final int x;
/*     */     private final int y;
/*     */     private final int z;
/*     */     
/*     */     BlockOffset(int x, int y, int z) {
/* 196 */       this.x = x;
/* 197 */       this.y = y;
/* 198 */       this.z = z;
/*     */     }
/*     */     
/*     */     public BlockPos offset(BlockPos pos) {
/* 202 */       return pos.func_177982_a(this.x, this.y, this.z);
/*     */     }
/*     */     
/*     */     public BlockPos forward(BlockPos pos, int scale) {
/* 206 */       return pos.func_177982_a(this.x * scale, 0, this.z * scale);
/*     */     }
/*     */     
/*     */     public BlockPos backward(BlockPos pos, int scale) {
/* 210 */       return pos.func_177982_a(-this.x * scale, 0, -this.z * scale);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public BlockPos left(BlockPos pos, int scale) {
/* 216 */       return pos.func_177982_a(this.z * scale, 0, -this.x * scale);
/*     */     }
/*     */     
/*     */     public BlockPos right(BlockPos pos, int scale) {
/* 220 */       return pos.func_177982_a(-this.z * scale, 0, this.x * scale);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobo\\util\HoleUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */