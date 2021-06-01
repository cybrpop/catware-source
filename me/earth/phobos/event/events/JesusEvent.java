/*    */ package me.earth.phobos.event.events;
/*    */ 
/*    */ import me.earth.phobos.event.EventStage;
/*    */ import net.minecraft.util.math.AxisAlignedBB;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraftforge.fml.common.eventhandler.Cancelable;
/*    */ 
/*    */ @Cancelable
/*    */ public class JesusEvent
/*    */   extends EventStage {
/*    */   private BlockPos pos;
/*    */   private AxisAlignedBB boundingBox;
/*    */   
/*    */   public JesusEvent(int stage, BlockPos pos) {
/* 15 */     super(stage);
/* 16 */     this.pos = pos;
/*    */   }
/*    */   
/*    */   public BlockPos getPos() {
/* 20 */     return this.pos;
/*    */   }
/*    */   
/*    */   public void setPos(BlockPos pos) {
/* 24 */     this.pos = pos;
/*    */   }
/*    */   
/*    */   public AxisAlignedBB getBoundingBox() {
/* 28 */     return this.boundingBox;
/*    */   }
/*    */   
/*    */   public void setBoundingBox(AxisAlignedBB boundingBox) {
/* 32 */     this.boundingBox = boundingBox;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\event\events\JesusEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */