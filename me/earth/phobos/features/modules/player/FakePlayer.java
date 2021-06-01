/*     */ package me.earth.phobos.features.modules.player;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.client.ClickGui;
/*     */ import me.earth.phobos.features.modules.client.ServerModule;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import net.minecraft.client.entity.EntityOtherPlayerMP;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketChatMessage;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class FakePlayer
/*     */   extends Module {
/*  22 */   public static final String[][] phobosInfo = new String[][] { { "8af022c8-b926-41a0-8b79-2b544ff00fcf", "3arthqu4ke", "3", "0" }, { "0aa3b04f-786a-49c8-bea9-025ee0dd1e85", "zb0b", "-3", "0" }, { "19bf3f1f-fe06-4c86-bea5-3dad5df89714", "3vt", "0", "-3" }, { "e47d6571-99c2-415b-955e-c4bc7b55941b", "Phobos_eu", "0", "3" }, { "b01f9bc1-cb7c-429a-b178-93d771f00926", "bakpotatisen", "6", "0" }, { "b232930c-c28a-4e10-8c90-f152235a65c5", "948", "-6", "0" }, { "ace08461-3db3-4579-98d3-390a67d5645b", "Browswer", "0", "-6" }, { "5bead5b0-3bab-460d-af1d-7929950f40c2", "fsck", "0", "6" }, { "78ee2bd6-64c4-45f0-96e5-0b6747ba7382", "Fit", "0", "9" }, { "78ee2bd6-64c4-45f0-96e5-0b6747ba7382", "deathcurz0", "0", "-9" } };
/*  23 */   private static final String[] fitInfo = new String[] { "fdee323e-7f0c-4c15-8d1c-0f277442342a", "Fit" };
/*  24 */   private static FakePlayer INSTANCE = new FakePlayer();
/*  25 */   private final List<EntityOtherPlayerMP> fakeEntities = new ArrayList<>();
/*  26 */   public Setting<Boolean> multi = register(new Setting("Multi", Boolean.valueOf(false)));
/*  27 */   public List<Integer> fakePlayerIdList = new ArrayList<>();
/*  28 */   private final Setting<Boolean> copyInv = register(new Setting("CopyInv", Boolean.valueOf(true)));
/*  29 */   private final Setting<Integer> players = register(new Setting("Players", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(9), v -> ((Boolean)this.multi.getValue()).booleanValue(), "Amount of other players."));
/*     */   
/*     */   public FakePlayer() {
/*  32 */     super("FakePlayer", "Spawns in a fake player", Module.Category.PLAYER, true, false, false);
/*  33 */     setInstance();
/*     */   }
/*     */   
/*     */   public static FakePlayer getInstance() {
/*  37 */     if (INSTANCE == null) {
/*  38 */       INSTANCE = new FakePlayer();
/*     */     }
/*  40 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  44 */     INSTANCE = this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLoad() {
/*  49 */     disable();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  54 */     if (fullNullCheck()) {
/*  55 */       disable();
/*     */       return;
/*     */     } 
/*  58 */     if (ServerModule.getInstance().isConnected()) {
/*  59 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Serverprefix" + (String)(ClickGui.getInstance()).prefix.getValue()));
/*  60 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Server" + (String)(ClickGui.getInstance()).prefix.getValue() + "module FakePlayer set Enabled true"));
/*     */     } 
/*  62 */     this.fakePlayerIdList = new ArrayList<>();
/*  63 */     if (((Boolean)this.multi.getValue()).booleanValue()) {
/*  64 */       int amount = 0;
/*  65 */       int entityId = -101;
/*  66 */       for (String[] data : phobosInfo) {
/*  67 */         addFakePlayer(data[0], data[1], entityId, Integer.parseInt(data[2]), Integer.parseInt(data[3]));
/*  68 */         if (++amount >= ((Integer)this.players.getValue()).intValue()) {
/*     */           return;
/*     */         }
/*  71 */         entityId -= amount;
/*     */       } 
/*     */     } else {
/*  74 */       addFakePlayer(fitInfo[0], fitInfo[1], -100, 0, 0);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  80 */     if (fullNullCheck()) {
/*     */       return;
/*     */     }
/*  83 */     if (ServerModule.getInstance().isConnected()) {
/*  84 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Serverprefix" + (String)(ClickGui.getInstance()).prefix.getValue()));
/*  85 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Server" + (String)(ClickGui.getInstance()).prefix.getValue() + "module FakePlayer set Enabled false"));
/*     */     } 
/*  87 */     for (Iterator<Integer> iterator = this.fakePlayerIdList.iterator(); iterator.hasNext(); ) { int id = ((Integer)iterator.next()).intValue();
/*  88 */       mc.field_71441_e.func_73028_b(id); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLogout() {
/*  94 */     if (isOn()) {
/*  95 */       disable();
/*     */     }
/*     */   }
/*     */   
/*     */   private void addFakePlayer(String uuid, String name, int entityId, int offsetX, int offsetZ) {
/* 100 */     GameProfile profile = new GameProfile(UUID.fromString(uuid), name);
/* 101 */     EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP((World)mc.field_71441_e, profile);
/* 102 */     fakePlayer.func_82149_j((Entity)mc.field_71439_g);
/* 103 */     fakePlayer.field_70165_t += offsetX;
/* 104 */     fakePlayer.field_70161_v += offsetZ;
/* 105 */     if (((Boolean)this.copyInv.getValue()).booleanValue()) {
/* 106 */       for (PotionEffect potionEffect : Phobos.potionManager.getOwnPotions()) {
/* 107 */         fakePlayer.func_70690_d(potionEffect);
/*     */       }
/* 109 */       fakePlayer.field_71071_by.func_70455_b(mc.field_71439_g.field_71071_by);
/*     */     } 
/* 111 */     fakePlayer.func_70606_j(mc.field_71439_g.func_110143_aJ() + mc.field_71439_g.func_110139_bj());
/* 112 */     this.fakeEntities.add(fakePlayer);
/* 113 */     mc.field_71441_e.func_73027_a(entityId, (Entity)fakePlayer);
/* 114 */     this.fakePlayerIdList.add(Integer.valueOf(entityId));
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\player\FakePlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */