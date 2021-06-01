/*     */ package me.earth.phobos.features.modules.render;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.gui.BossInfoClient;
/*     */ import net.minecraft.client.gui.GuiBossOverlay;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.SoundEvents;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.world.BossInfo;
/*     */ import net.minecraft.world.GameType;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.client.event.RenderGameOverlayEvent;
/*     */ import net.minecraftforge.client.event.RenderLivingEvent;
/*     */ import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NoRender
/*     */   extends Module
/*     */ {
/*  37 */   private static NoRender INSTANCE = new NoRender();
/*     */   
/*     */   static {
/*  40 */     INSTANCE = new NoRender();
/*     */   }
/*     */   
/*  43 */   public Setting<Boolean> fire = register(new Setting("Fire", Boolean.valueOf(false), "Removes the portal overlay."));
/*  44 */   public Setting<Boolean> portal = register(new Setting("Portal", Boolean.valueOf(false), "Removes the portal overlay."));
/*  45 */   public Setting<Boolean> pumpkin = register(new Setting("Pumpkin", Boolean.valueOf(false), "Removes the pumpkin overlay."));
/*  46 */   public Setting<Boolean> totemPops = register(new Setting("TotemPop", Boolean.valueOf(false), "Removes the Totem overlay."));
/*  47 */   public Setting<Boolean> items = register(new Setting("Items", Boolean.valueOf(false), "Removes items on the ground."));
/*  48 */   public Setting<Boolean> nausea = register(new Setting("Nausea", Boolean.valueOf(false), "Removes Portal Nausea."));
/*  49 */   public Setting<Boolean> hurtcam = register(new Setting("HurtCam", Boolean.valueOf(false), "Removes shaking after taking damage."));
/*  50 */   public Setting<Fog> fog = register(new Setting("Fog", Fog.NONE, "Removes Fog."));
/*  51 */   public Setting<Boolean> noWeather = register(new Setting("Weather", Boolean.valueOf(false), "AntiWeather"));
/*  52 */   public Setting<Boss> boss = register(new Setting("BossBars", Boss.NONE, "Modifies the bossbars."));
/*  53 */   public Setting<Float> scale = register(new Setting("Scale", Float.valueOf(0.0F), Float.valueOf(0.5F), Float.valueOf(1.0F), v -> (this.boss.getValue() == Boss.MINIMIZE || this.boss.getValue() != Boss.STACK), "Scale of the bars."));
/*  54 */   public Setting<Boolean> bats = register(new Setting("Bats", Boolean.valueOf(false), "Removes bats."));
/*  55 */   public Setting<NoArmor> noArmor = register(new Setting("NoArmor", NoArmor.NONE, "Doesnt Render Armor on players."));
/*  56 */   public Setting<Boolean> glint = register(new Setting("Glint", Boolean.valueOf(false), v -> (this.noArmor.getValue() != NoArmor.NONE)));
/*  57 */   public Setting<Skylight> skylight = register(new Setting("Skylight", Skylight.NONE));
/*  58 */   public Setting<Boolean> barriers = register(new Setting("Barriers", Boolean.valueOf(false), "Barriers"));
/*  59 */   public Setting<Boolean> blocks = register(new Setting("Blocks", Boolean.valueOf(false), "Blocks"));
/*  60 */   public Setting<Boolean> advancements = register(new Setting("Advancements", Boolean.valueOf(false)));
/*  61 */   public Setting<Boolean> pigmen = register(new Setting("Pigmen", Boolean.valueOf(false)));
/*  62 */   public Setting<Boolean> timeChange = register(new Setting("TimeChange", Boolean.valueOf(false)));
/*  63 */   public Setting<Integer> time = register(new Setting("Time", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(23000), v -> ((Boolean)this.timeChange.getValue()).booleanValue()));
/*     */   
/*     */   public NoRender() {
/*  66 */     super("NoRender", "Allows you to stop rendering stuff", Module.Category.RENDER, true, false, false);
/*  67 */     setInstance();
/*     */   }
/*     */   
/*     */   public static NoRender getInstance() {
/*  71 */     if (INSTANCE == null) {
/*  72 */       INSTANCE = new NoRender();
/*     */     }
/*  74 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  78 */     INSTANCE = this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  83 */     if (((Boolean)this.items.getValue()).booleanValue()) {
/*  84 */       mc.field_71441_e.field_72996_f.stream().filter(EntityItem.class::isInstance).map(EntityItem.class::cast).forEach(Entity::func_70106_y);
/*     */     }
/*  86 */     if (((Boolean)this.noWeather.getValue()).booleanValue() && mc.field_71441_e.func_72896_J()) {
/*  87 */       mc.field_71441_e.func_72894_k(0.0F);
/*     */     }
/*  89 */     if (((Boolean)this.timeChange.getValue()).booleanValue()) {
/*  90 */       mc.field_71441_e.func_72877_b(((Integer)this.time.getValue()).intValue());
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketReceive(PacketEvent.Receive event) {
/*  96 */     if ((event.getPacket() instanceof net.minecraft.network.play.server.SPacketTimeUpdate & ((Boolean)this.timeChange.getValue()).booleanValue()) != 0) {
/*  97 */       event.setCanceled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   public void doVoidFogParticles(int posX, int posY, int posZ) {
/* 102 */     int i = 32;
/* 103 */     Random random = new Random();
/* 104 */     ItemStack itemstack = mc.field_71439_g.func_184614_ca();
/* 105 */     boolean flag = (!((Boolean)this.barriers.getValue()).booleanValue() || (mc.field_71442_b.func_178889_l() == GameType.CREATIVE && !itemstack.func_190926_b() && itemstack.func_77973_b() == Item.func_150898_a(Blocks.field_180401_cv)));
/* 106 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/* 107 */     for (int j = 0; j < 667; j++) {
/* 108 */       showBarrierParticles(posX, posY, posZ, 16, random, flag, blockpos$mutableblockpos);
/* 109 */       showBarrierParticles(posX, posY, posZ, 32, random, flag, blockpos$mutableblockpos);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void showBarrierParticles(int x, int y, int z, int offset, Random random, boolean holdingBarrier, BlockPos.MutableBlockPos pos) {
/* 114 */     int i = x + mc.field_71441_e.field_73012_v.nextInt(offset) - mc.field_71441_e.field_73012_v.nextInt(offset);
/* 115 */     int j = y + mc.field_71441_e.field_73012_v.nextInt(offset) - mc.field_71441_e.field_73012_v.nextInt(offset);
/* 116 */     int k = z + mc.field_71441_e.field_73012_v.nextInt(offset) - mc.field_71441_e.field_73012_v.nextInt(offset);
/* 117 */     pos.func_181079_c(i, j, k);
/* 118 */     IBlockState iblockstate = mc.field_71441_e.func_180495_p((BlockPos)pos);
/* 119 */     iblockstate.func_177230_c().func_180655_c(iblockstate, (World)mc.field_71441_e, (BlockPos)pos, random);
/* 120 */     if (!holdingBarrier && iblockstate.func_177230_c() == Blocks.field_180401_cv) {
/* 121 */       mc.field_71441_e.func_175688_a(EnumParticleTypes.BARRIER, (i + 0.5F), (j + 0.5F), (k + 0.5F), 0.0D, 0.0D, 0.0D, new int[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderPre(RenderGameOverlayEvent.Pre event) {
/* 127 */     if (event.getType() == RenderGameOverlayEvent.ElementType.BOSSINFO && this.boss.getValue() != Boss.NONE) {
/* 128 */       event.setCanceled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderPost(RenderGameOverlayEvent.Post event) {
/* 134 */     if (event.getType() == RenderGameOverlayEvent.ElementType.BOSSINFO && this.boss.getValue() != Boss.NONE) {
/* 135 */       if (this.boss.getValue() == Boss.MINIMIZE) {
/* 136 */         Map<UUID, BossInfoClient> map = (mc.field_71456_v.func_184046_j()).field_184060_g;
/* 137 */         if (map == null) {
/*     */           return;
/*     */         }
/* 140 */         ScaledResolution scaledresolution = new ScaledResolution(mc);
/* 141 */         int i = scaledresolution.func_78326_a();
/* 142 */         int j = 12;
/* 143 */         for (Map.Entry<UUID, BossInfoClient> entry : map.entrySet()) {
/* 144 */           BossInfoClient info = entry.getValue();
/* 145 */           String text = info.func_186744_e().func_150254_d();
/* 146 */           int k = (int)(i / ((Float)this.scale.getValue()).floatValue() / 2.0F - 91.0F);
/* 147 */           GL11.glScaled(((Float)this.scale.getValue()).floatValue(), ((Float)this.scale.getValue()).floatValue(), 1.0D);
/* 148 */           if (!event.isCanceled()) {
/* 149 */             GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 150 */             mc.func_110434_K().func_110577_a(GuiBossOverlay.field_184058_a);
/* 151 */             mc.field_71456_v.func_184046_j().func_184052_a(k, j, (BossInfo)info);
/* 152 */             mc.field_71466_p.func_175063_a(text, i / ((Float)this.scale.getValue()).floatValue() / 2.0F - (mc.field_71466_p.func_78256_a(text) / 2), (j - 9), 16777215);
/*     */           } 
/* 154 */           GL11.glScaled(1.0D / ((Float)this.scale.getValue()).floatValue(), 1.0D / ((Float)this.scale.getValue()).floatValue(), 1.0D);
/* 155 */           j += 10 + mc.field_71466_p.field_78288_b;
/*     */         } 
/* 157 */       } else if (this.boss.getValue() == Boss.STACK) {
/* 158 */         Map<UUID, BossInfoClient> map = (mc.field_71456_v.func_184046_j()).field_184060_g;
/* 159 */         HashMap<String, Pair<BossInfoClient, Integer>> to = new HashMap<>();
/* 160 */         for (Map.Entry<UUID, BossInfoClient> entry2 : map.entrySet()) {
/* 161 */           String s = ((BossInfoClient)entry2.getValue()).func_186744_e().func_150254_d();
/* 162 */           if (to.containsKey(s)) {
/* 163 */             Pair<BossInfoClient, Integer> pair = to.get(s);
/* 164 */             pair = new Pair<>(pair.getKey(), Integer.valueOf(((Integer)pair.getValue()).intValue() + 1));
/* 165 */             to.put(s, pair); continue;
/*     */           } 
/* 167 */           Pair<BossInfoClient, Integer> p = new Pair<>(entry2.getValue(), Integer.valueOf(1));
/* 168 */           to.put(s, p);
/*     */         } 
/*     */         
/* 171 */         ScaledResolution scaledresolution2 = new ScaledResolution(mc);
/* 172 */         int l = scaledresolution2.func_78326_a();
/* 173 */         int m = 12;
/* 174 */         for (Map.Entry<String, Pair<BossInfoClient, Integer>> entry3 : to.entrySet()) {
/* 175 */           String text = entry3.getKey();
/* 176 */           BossInfoClient info2 = (BossInfoClient)((Pair)entry3.getValue()).getKey();
/* 177 */           int a = ((Integer)((Pair)entry3.getValue()).getValue()).intValue();
/* 178 */           text = text + " x" + a;
/* 179 */           int k2 = (int)(l / ((Float)this.scale.getValue()).floatValue() / 2.0F - 91.0F);
/* 180 */           GL11.glScaled(((Float)this.scale.getValue()).floatValue(), ((Float)this.scale.getValue()).floatValue(), 1.0D);
/* 181 */           if (!event.isCanceled()) {
/* 182 */             GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 183 */             mc.func_110434_K().func_110577_a(GuiBossOverlay.field_184058_a);
/* 184 */             mc.field_71456_v.func_184046_j().func_184052_a(k2, m, (BossInfo)info2);
/* 185 */             mc.field_71466_p.func_175063_a(text, l / ((Float)this.scale.getValue()).floatValue() / 2.0F - (mc.field_71466_p.func_78256_a(text) / 2), (m - 9), 16777215);
/*     */           } 
/* 187 */           GL11.glScaled(1.0D / ((Float)this.scale.getValue()).floatValue(), 1.0D / ((Float)this.scale.getValue()).floatValue(), 1.0D);
/* 188 */           m += 10 + mc.field_71466_p.field_78288_b;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderLiving(RenderLivingEvent.Pre<?> event) {
/* 196 */     if (((Boolean)this.bats.getValue()).booleanValue() && event.getEntity() instanceof net.minecraft.entity.passive.EntityBat) {
/* 197 */       event.setCanceled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPlaySound(PlaySoundAtEntityEvent event) {
/* 203 */     if ((((Boolean)this.bats.getValue()).booleanValue() && event.getSound().equals(SoundEvents.field_187740_w)) || event.getSound().equals(SoundEvents.field_187742_x) || event.getSound().equals(SoundEvents.field_187743_y) || event.getSound().equals(SoundEvents.field_189108_z) || event.getSound().equals(SoundEvents.field_187744_z)) {
/* 204 */       event.setVolume(0.0F);
/* 205 */       event.setPitch(0.0F);
/* 206 */       event.setCanceled(true);
/*     */     } 
/*     */   }
/*     */   
/*     */   public enum Skylight {
/* 211 */     NONE,
/* 212 */     WORLD,
/* 213 */     ENTITY,
/* 214 */     ALL;
/*     */   }
/*     */   
/*     */   public enum Fog {
/* 218 */     NONE,
/* 219 */     AIR,
/* 220 */     NOFOG;
/*     */   }
/*     */   
/*     */   public enum Boss {
/* 224 */     NONE,
/* 225 */     REMOVE,
/* 226 */     STACK,
/* 227 */     MINIMIZE;
/*     */   }
/*     */   
/*     */   public enum NoArmor {
/* 231 */     NONE,
/* 232 */     ALL,
/* 233 */     HELMET;
/*     */   }
/*     */   
/*     */   public static class Pair<T, S> {
/*     */     private T key;
/*     */     private S value;
/*     */     
/*     */     public Pair(T key, S value) {
/* 241 */       this.key = key;
/* 242 */       this.value = value;
/*     */     }
/*     */     
/*     */     public T getKey() {
/* 246 */       return this.key;
/*     */     }
/*     */     
/*     */     public void setKey(T key) {
/* 250 */       this.key = key;
/*     */     }
/*     */     
/*     */     public S getValue() {
/* 254 */       return this.value;
/*     */     }
/*     */     
/*     */     public void setValue(S value) {
/* 258 */       this.value = value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\render\NoRender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */