/*     */ package me.earth.phobos.features.modules.combat;
/*     */ 
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.BlockUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.Util;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketAnimation;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
/*     */ import net.minecraft.network.play.client.CPacketUseEntity;
/*     */ import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
/*     */ import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
/*     */ import net.minecraft.network.play.server.SPacketSpawnMob;
/*     */ import net.minecraft.network.play.server.SPacketSpawnObject;
/*     */ import net.minecraft.network.play.server.SPacketSpawnPainting;
/*     */ import net.minecraft.network.play.server.SPacketSpawnPlayer;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.util.math.Vec3i;
/*     */ import net.minecraftforge.fml.common.eventhandler.EventPriority;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class GodModule extends Module {
/*  32 */   public Setting<Integer> rotations = register(new Setting("Spoofs", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(20)));
/*  33 */   public Setting<Boolean> rotate = register(new Setting("Rotate", Boolean.valueOf(false)));
/*  34 */   public Setting<Boolean> render = register(new Setting("Render", Boolean.valueOf(false)));
/*  35 */   public Setting<Boolean> antiIllegal = register(new Setting("AntiIllegal", Boolean.valueOf(true)));
/*  36 */   public Setting<Boolean> checkPos = register(new Setting("CheckPos", Boolean.valueOf(true)));
/*  37 */   public Setting<Boolean> oneDot15 = register(new Setting("1.15", Boolean.valueOf(false)));
/*  38 */   public Setting<Boolean> entitycheck = register(new Setting("EntityCheck", Boolean.valueOf(false)));
/*  39 */   public Setting<Integer> attacks = register(new Setting("Attacks", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(10)));
/*  40 */   public Setting<Integer> delay = register(new Setting("Delay", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(50)));
/*  41 */   private float yaw = 0.0F;
/*  42 */   private float pitch = 0.0F;
/*     */   private boolean rotating;
/*     */   private int rotationPacketsSpoofed;
/*  45 */   private int highestID = -100000;
/*     */   
/*     */   public GodModule() {
/*  48 */     super("GodModule", "Wow", Module.Category.COMBAT, true, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onToggle() {
/*  53 */     resetFields();
/*  54 */     if (mc.field_71441_e != null) {
/*  55 */       updateEntityID();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  61 */     if (((Boolean)this.render.getValue()).booleanValue()) {
/*  62 */       for (Entity entity : mc.field_71441_e.field_72996_f) {
/*  63 */         if (!(entity instanceof net.minecraft.entity.item.EntityEnderCrystal))
/*  64 */           continue;  entity.func_96094_a(String.valueOf(entity.field_145783_c));
/*  65 */         entity.func_174805_g(true);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLogout() {
/*  72 */     resetFields();
/*     */   }
/*     */   
/*     */   @SubscribeEvent(priority = EventPriority.HIGHEST)
/*     */   public void onSendPacket(PacketEvent.Send event) {
/*  77 */     if (event.getStage() == 0 && event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
/*  78 */       CPacketPlayerTryUseItemOnBlock packet = (CPacketPlayerTryUseItemOnBlock)event.getPacket();
/*  79 */       if (mc.field_71439_g.func_184586_b(packet.field_187027_c).func_77973_b() instanceof net.minecraft.item.ItemEndCrystal) {
/*  80 */         if ((((Boolean)this.checkPos.getValue()).booleanValue() && !BlockUtil.canPlaceCrystal(packet.field_179725_b, ((Boolean)this.entitycheck.getValue()).booleanValue(), ((Boolean)this.oneDot15.getValue()).booleanValue())) || checkPlayers()) {
/*     */           return;
/*     */         }
/*  83 */         updateEntityID();
/*  84 */         for (int i = 1; i < ((Integer)this.attacks.getValue()).intValue(); i++) {
/*  85 */           attackID(packet.field_179725_b, this.highestID + i);
/*     */         }
/*     */       } 
/*     */     } 
/*  89 */     if (event.getStage() == 0 && this.rotating && ((Boolean)this.rotate.getValue()).booleanValue() && event.getPacket() instanceof CPacketPlayer) {
/*  90 */       CPacketPlayer packet = (CPacketPlayer)event.getPacket();
/*  91 */       packet.field_149476_e = this.yaw;
/*  92 */       packet.field_149473_f = this.pitch;
/*  93 */       this.rotationPacketsSpoofed++;
/*  94 */       if (this.rotationPacketsSpoofed >= ((Integer)this.rotations.getValue()).intValue()) {
/*  95 */         this.rotating = false;
/*  96 */         this.rotationPacketsSpoofed = 0;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void attackID(BlockPos pos, int id) {
/* 102 */     Entity entity = mc.field_71441_e.func_73045_a(id);
/* 103 */     if (entity == null || entity instanceof net.minecraft.entity.item.EntityEnderCrystal) {
/* 104 */       AttackThread attackThread = new AttackThread(id, pos, ((Integer)this.delay.getValue()).intValue(), this);
/* 105 */       attackThread.start();
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketReceive(PacketEvent.Receive event) {
/* 111 */     if (event.getPacket() instanceof SPacketSpawnObject) {
/* 112 */       checkID(((SPacketSpawnObject)event.getPacket()).func_149001_c());
/* 113 */     } else if (event.getPacket() instanceof SPacketSpawnExperienceOrb) {
/* 114 */       checkID(((SPacketSpawnExperienceOrb)event.getPacket()).func_148985_c());
/* 115 */     } else if (event.getPacket() instanceof SPacketSpawnPlayer) {
/* 116 */       checkID(((SPacketSpawnPlayer)event.getPacket()).func_148943_d());
/* 117 */     } else if (event.getPacket() instanceof SPacketSpawnGlobalEntity) {
/* 118 */       checkID(((SPacketSpawnGlobalEntity)event.getPacket()).func_149052_c());
/* 119 */     } else if (event.getPacket() instanceof SPacketSpawnPainting) {
/* 120 */       checkID(((SPacketSpawnPainting)event.getPacket()).func_148965_c());
/* 121 */     } else if (event.getPacket() instanceof SPacketSpawnMob) {
/* 122 */       checkID(((SPacketSpawnMob)event.getPacket()).func_149024_d());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void checkID(int id) {
/* 127 */     if (id > this.highestID) {
/* 128 */       this.highestID = id;
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateEntityID() {
/* 133 */     for (Entity entity : mc.field_71441_e.field_72996_f) {
/* 134 */       if (entity.func_145782_y() <= this.highestID)
/* 135 */         continue;  this.highestID = entity.func_145782_y();
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean checkPlayers() {
/* 140 */     if (((Boolean)this.antiIllegal.getValue()).booleanValue()) {
/* 141 */       for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/* 142 */         if (!checkItem(player.func_184614_ca()) && !checkItem(player.func_184592_cb()))
/*     */           continue; 
/* 144 */         return false;
/*     */       } 
/*     */     }
/* 147 */     return true;
/*     */   }
/*     */   
/*     */   private boolean checkItem(ItemStack stack) {
/* 151 */     return (stack.func_77973_b() instanceof net.minecraft.item.ItemBow || stack.func_77973_b() instanceof net.minecraft.item.ItemExpBottle || stack.func_77973_b() == Items.field_151007_F);
/*     */   }
/*     */   
/*     */   public void rotateTo(BlockPos pos) {
/* 155 */     float[] angle = MathUtil.calcAngle(mc.field_71439_g.func_174824_e(mc.func_184121_ak()), new Vec3d((Vec3i)pos));
/* 156 */     this.yaw = angle[0];
/* 157 */     this.pitch = angle[1];
/* 158 */     this.rotating = true;
/*     */   }
/*     */   
/*     */   private void resetFields() {
/* 162 */     this.rotating = false;
/* 163 */     this.highestID = -1000000;
/*     */   }
/*     */   
/*     */   public static class AttackThread
/*     */     extends Thread {
/*     */     private final BlockPos pos;
/*     */     private final int id;
/*     */     private final int delay;
/*     */     private final GodModule godModule;
/*     */     
/*     */     public AttackThread(int idIn, BlockPos posIn, int delayIn, GodModule godModuleIn) {
/* 174 */       this.id = idIn;
/* 175 */       this.pos = posIn;
/* 176 */       this.delay = delayIn;
/* 177 */       this.godModule = godModuleIn;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       try {
/* 183 */         wait(this.delay);
/* 184 */         CPacketUseEntity attack = new CPacketUseEntity();
/* 185 */         attack.field_149567_a = this.id;
/* 186 */         attack.field_149566_b = CPacketUseEntity.Action.ATTACK;
/* 187 */         this.godModule.rotateTo(this.pos.func_177984_a());
/* 188 */         Util.mc.field_71439_g.field_71174_a.func_147297_a((Packet)attack);
/* 189 */         Util.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
/* 190 */       } catch (InterruptedException e) {
/* 191 */         e.printStackTrace();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\combat\GodModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */