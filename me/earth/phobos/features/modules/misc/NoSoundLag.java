/*    */ package me.earth.phobos.features.modules.misc;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Set;
/*    */ import me.earth.phobos.event.events.PacketEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.modules.combat.AutoCrystal;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.MathUtil;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.init.SoundEvents;
/*    */ import net.minecraft.network.play.server.SPacketSoundEffect;
/*    */ import net.minecraft.util.SoundCategory;
/*    */ import net.minecraft.util.SoundEvent;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NoSoundLag
/*    */   extends Module
/*    */ {
/* 27 */   private static final Set<SoundEvent> BLACKLIST = Sets.newHashSet((Object[])new SoundEvent[] { SoundEvents.field_187719_p, SoundEvents.field_191258_p, SoundEvents.field_187716_o, SoundEvents.field_187725_r, SoundEvents.field_187722_q, SoundEvents.field_187713_n, SoundEvents.field_187728_s });
/*    */   
/*    */   private static NoSoundLag instance;
/* 30 */   public Setting<Boolean> crystals = register(new Setting("Crystals", Boolean.valueOf(true)));
/* 31 */   public Setting<Boolean> armor = register(new Setting("Armor", Boolean.valueOf(true)));
/* 32 */   public Setting<Float> soundRange = register(new Setting("SoundRange", Float.valueOf(12.0F), Float.valueOf(0.0F), Float.valueOf(12.0F)));
/*    */   
/*    */   public NoSoundLag() {
/* 35 */     super("NoSoundLag", "Prevents Lag through sound spam.", Module.Category.MISC, true, false, false);
/* 36 */     instance = this;
/*    */   }
/*    */   
/*    */   public static NoSoundLag getInstance() {
/* 40 */     if (instance == null) {
/* 41 */       instance = new NoSoundLag();
/*    */     }
/* 43 */     return instance;
/*    */   }
/*    */   
/*    */   public static void removeEntities(SPacketSoundEffect packet, float range) {
/* 47 */     BlockPos pos = new BlockPos(packet.func_149207_d(), packet.func_149211_e(), packet.func_149210_f());
/* 48 */     ArrayList<Entity> toRemove = new ArrayList<>();
/* 49 */     for (Entity entity : mc.field_71441_e.field_72996_f) {
/* 50 */       if (!(entity instanceof net.minecraft.entity.item.EntityEnderCrystal) || entity.func_174818_b(pos) > MathUtil.square(range))
/*    */         continue; 
/* 52 */       toRemove.add(entity);
/*    */     } 
/* 54 */     for (Entity entity : toRemove) {
/* 55 */       entity.func_70106_y();
/*    */     }
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onPacketReceived(PacketEvent.Receive event) {
/* 61 */     if (event != null && event.getPacket() != null && mc.field_71439_g != null && mc.field_71441_e != null && event.getPacket() instanceof SPacketSoundEffect) {
/* 62 */       SPacketSoundEffect packet = (SPacketSoundEffect)event.getPacket();
/* 63 */       if (((Boolean)this.crystals.getValue()).booleanValue() && packet.func_186977_b() == SoundCategory.BLOCKS && packet.func_186978_a() == SoundEvents.field_187539_bB && (AutoCrystal.getInstance().isOff() || (!((Boolean)(AutoCrystal.getInstance()).sound.getValue()).booleanValue() && (AutoCrystal.getInstance()).threadMode.getValue() != AutoCrystal.ThreadMode.SOUND))) {
/* 64 */         removeEntities(packet, ((Float)this.soundRange.getValue()).floatValue());
/*    */       }
/* 66 */       if (BLACKLIST.contains(packet.func_186978_a()) && ((Boolean)this.armor.getValue()).booleanValue())
/* 67 */         event.setCanceled(true); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\misc\NoSoundLag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */