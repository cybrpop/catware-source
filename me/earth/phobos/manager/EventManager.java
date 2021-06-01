/*     */ package me.earth.phobos.manager;
/*     */ import com.google.common.base.Strings;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.ClientEvent;
/*     */ import me.earth.phobos.event.events.ConnectionEvent;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.event.events.Render2DEvent;
/*     */ import me.earth.phobos.event.events.Render3DEvent;
/*     */ import me.earth.phobos.event.events.TotemPopEvent;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.Feature;
/*     */ import me.earth.phobos.features.command.Command;
/*     */ import me.earth.phobos.features.modules.client.Managers;
/*     */ import me.earth.phobos.features.modules.client.ServerModule;
/*     */ import me.earth.phobos.features.modules.combat.AutoCrystal;
/*     */ import me.earth.phobos.util.GLUProjection;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.GLAllocation;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.network.play.client.CPacketChatMessage;
/*     */ import net.minecraft.network.play.server.SPacketEntityStatus;
/*     */ import net.minecraft.network.play.server.SPacketPlayerListItem;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.client.event.ClientChatEvent;
/*     */ import net.minecraftforge.client.event.RenderGameOverlayEvent;
/*     */ import net.minecraftforge.client.event.RenderWorldLastEvent;
/*     */ import net.minecraftforge.common.MinecraftForge;
/*     */ import net.minecraftforge.event.entity.living.LivingEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.Event;
/*     */ import net.minecraftforge.fml.common.eventhandler.EventPriority;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import net.minecraftforge.fml.common.network.FMLNetworkEvent;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class EventManager extends Feature {
/*  44 */   private final Timer timer = new Timer();
/*  45 */   private final Timer logoutTimer = new Timer();
/*  46 */   private final Timer switchTimer = new Timer();
/*     */   private boolean keyTimeout;
/*  48 */   private final AtomicBoolean tickOngoing = new AtomicBoolean(false);
/*     */   
/*     */   public void init() {
/*  51 */     MinecraftForge.EVENT_BUS.register(this);
/*     */   }
/*     */   
/*     */   public void onUnload() {
/*  55 */     MinecraftForge.EVENT_BUS.unregister(this);
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdate(LivingEvent.LivingUpdateEvent event) {
/*  60 */     if (!fullNullCheck() && (event.getEntity().func_130014_f_()).field_72995_K && event.getEntityLiving().equals(mc.field_71439_g)) {
/*  61 */       Phobos.potionManager.update();
/*  62 */       Phobos.totemPopManager.onUpdate();
/*  63 */       Phobos.inventoryManager.update();
/*  64 */       Phobos.holeManager.update();
/*  65 */       Phobos.safetyManager.onUpdate();
/*  66 */       Phobos.moduleManager.onUpdate();
/*  67 */       Phobos.timerManager.update();
/*  68 */       if (this.timer.passedMs(((Integer)(Managers.getInstance()).moduleListUpdates.getValue()).intValue())) {
/*  69 */         Phobos.moduleManager.sortModules(true);
/*  70 */         Phobos.moduleManager.alphabeticallySortModules();
/*  71 */         this.timer.reset();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onSettingChange(ClientEvent event) {
/*  78 */     if (event.getStage() == 2 && mc.func_147114_u() != null && ServerModule.getInstance().isConnected() && mc.field_71441_e != null) {
/*  79 */       String command = "@Server" + ServerModule.getInstance().getServerPrefix() + "module " + event.getSetting().getFeature().getName() + " set " + event.getSetting().getName() + " " + event.getSetting().getPlannedValue().toString();
/*  80 */       CPacketChatMessage cPacketChatMessage = new CPacketChatMessage(command);
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent(priority = EventPriority.HIGHEST)
/*     */   public void onTickHighest(TickEvent.ClientTickEvent event) {
/*  86 */     if (event.phase == TickEvent.Phase.START) {
/*  87 */       this.tickOngoing.set(true);
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent(priority = EventPriority.LOWEST)
/*     */   public void onTickLowest(TickEvent.ClientTickEvent event) {
/*  93 */     if (event.phase == TickEvent.Phase.END) {
/*  94 */       this.tickOngoing.set(false);
/*  95 */       AutoCrystal.getInstance().postTick();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean ticksOngoing() {
/* 100 */     return this.tickOngoing.get();
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClientConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
/* 105 */     this.logoutTimer.reset();
/* 106 */     Phobos.moduleManager.onLogin();
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
/* 111 */     Phobos.moduleManager.onLogout();
/* 112 */     Phobos.totemPopManager.onLogout();
/* 113 */     Phobos.potionManager.onLogout();
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onTick(TickEvent.ClientTickEvent event) {
/* 118 */     if (fullNullCheck()) {
/*     */       return;
/*     */     }
/* 121 */     Phobos.moduleManager.onTick();
/*     */   }
/*     */   
/*     */   @SubscribeEvent(priority = EventPriority.HIGHEST)
/*     */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/* 126 */     if (fullNullCheck()) {
/*     */       return;
/*     */     }
/* 129 */     if (event.getStage() == 0) {
/* 130 */       Phobos.baritoneManager.onUpdateWalkingPlayer();
/* 131 */       Phobos.speedManager.updateValues();
/* 132 */       Phobos.rotationManager.updateRotations();
/* 133 */       Phobos.positionManager.updatePosition();
/*     */     } 
/* 135 */     if (event.getStage() == 1) {
/* 136 */       Phobos.rotationManager.restoreRotations();
/* 137 */       Phobos.positionManager.restorePosition();
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketSend(PacketEvent.Send event) {
/* 143 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketHeldItemChange) {
/* 144 */       this.switchTimer.reset();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isOnSwitchCoolDown() {
/* 149 */     return !this.switchTimer.passedMs(500L);
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketReceive(PacketEvent.Receive event) {
/* 154 */     if (event.getStage() != 0) {
/*     */       return;
/*     */     }
/* 157 */     Phobos.serverManager.onPacketReceived();
/* 158 */     if (event.getPacket() instanceof SPacketEntityStatus) {
/* 159 */       SPacketEntityStatus packet = (SPacketEntityStatus)event.getPacket();
/* 160 */       if (packet.func_149160_c() == 35 && packet.func_149161_a((World)mc.field_71441_e) instanceof EntityPlayer) {
/* 161 */         EntityPlayer player = (EntityPlayer)packet.func_149161_a((World)mc.field_71441_e);
/* 162 */         MinecraftForge.EVENT_BUS.post((Event)new TotemPopEvent(player));
/* 163 */         Phobos.totemPopManager.onTotemPop(player);
/* 164 */         Phobos.potionManager.onTotemPop(player);
/*     */       } 
/* 166 */     } else if (event.getPacket() instanceof SPacketPlayerListItem && !fullNullCheck() && this.logoutTimer.passedS(1.0D)) {
/* 167 */       SPacketPlayerListItem packet = (SPacketPlayerListItem)event.getPacket();
/* 168 */       if (!SPacketPlayerListItem.Action.ADD_PLAYER.equals(packet.func_179768_b()) && !SPacketPlayerListItem.Action.REMOVE_PLAYER.equals(packet.func_179768_b())) {
/*     */         return;
/*     */       }
/* 171 */       packet.func_179767_a().stream().filter(Objects::nonNull).filter(data -> (!Strings.isNullOrEmpty(data.func_179962_a().getName()) || data.func_179962_a().getId() != null)).forEach(data -> {
/*     */             String name; EntityPlayer entity;
/*     */             UUID id = data.func_179962_a().getId();
/*     */             switch (packet.func_179768_b()) {
/*     */               case ADD_PLAYER:
/*     */                 name = data.func_179962_a().getName();
/*     */                 MinecraftForge.EVENT_BUS.post((Event)new ConnectionEvent(0, id, name));
/*     */                 break;
/*     */               case REMOVE_PLAYER:
/*     */                 entity = mc.field_71441_e.func_152378_a(id);
/*     */                 if (entity != null) {
/*     */                   String logoutName = entity.func_70005_c_();
/*     */                   MinecraftForge.EVENT_BUS.post((Event)new ConnectionEvent(1, entity, id, logoutName));
/*     */                   break;
/*     */                 } 
/*     */                 MinecraftForge.EVENT_BUS.post((Event)new ConnectionEvent(2, id, null));
/*     */                 break;
/*     */             } 
/*     */           });
/* 190 */     } else if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketTimeUpdate) {
/* 191 */       Phobos.serverManager.update();
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onWorldRender(RenderWorldLastEvent event) {
/* 197 */     if (event.isCanceled()) {
/*     */       return;
/*     */     }
/* 200 */     mc.field_71424_I.func_76320_a("phobos");
/* 201 */     GlStateManager.func_179090_x();
/* 202 */     GlStateManager.func_179147_l();
/* 203 */     GlStateManager.func_179118_c();
/* 204 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/* 205 */     GlStateManager.func_179103_j(7425);
/* 206 */     GlStateManager.func_179097_i();
/* 207 */     GlStateManager.func_187441_d(1.0F);
/* 208 */     Render3DEvent render3dEvent = new Render3DEvent(event.getPartialTicks());
/* 209 */     GLUProjection projection = GLUProjection.getInstance();
/* 210 */     IntBuffer viewPort = GLAllocation.func_74527_f(16);
/* 211 */     FloatBuffer modelView = GLAllocation.func_74529_h(16);
/* 212 */     FloatBuffer projectionPort = GLAllocation.func_74529_h(16);
/* 213 */     GL11.glGetFloat(2982, modelView);
/* 214 */     GL11.glGetFloat(2983, projectionPort);
/* 215 */     GL11.glGetInteger(2978, viewPort);
/* 216 */     ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
/* 217 */     projection.updateMatrices(viewPort, modelView, projectionPort, scaledResolution.func_78326_a() / (Minecraft.func_71410_x()).field_71443_c, scaledResolution.func_78328_b() / (Minecraft.func_71410_x()).field_71440_d);
/* 218 */     Phobos.moduleManager.onRender3D(render3dEvent);
/* 219 */     GlStateManager.func_187441_d(1.0F);
/* 220 */     GlStateManager.func_179103_j(7424);
/* 221 */     GlStateManager.func_179084_k();
/* 222 */     GlStateManager.func_179141_d();
/* 223 */     GlStateManager.func_179098_w();
/* 224 */     GlStateManager.func_179126_j();
/* 225 */     GlStateManager.func_179089_o();
/* 226 */     GlStateManager.func_179089_o();
/* 227 */     GlStateManager.func_179132_a(true);
/* 228 */     GlStateManager.func_179098_w();
/* 229 */     GlStateManager.func_179147_l();
/* 230 */     GlStateManager.func_179126_j();
/* 231 */     mc.field_71424_I.func_76319_b();
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void renderHUD(RenderGameOverlayEvent.Post event) {
/* 236 */     if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
/* 237 */       Phobos.textManager.updateResolution();
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent(priority = EventPriority.LOW)
/*     */   public void onRenderGameOverlayEvent(RenderGameOverlayEvent.Text event) {
/* 243 */     if (event.getType().equals(RenderGameOverlayEvent.ElementType.TEXT)) {
/* 244 */       ScaledResolution resolution = new ScaledResolution(mc);
/* 245 */       Render2DEvent render2DEvent = new Render2DEvent(event.getPartialTicks(), resolution);
/* 246 */       Phobos.moduleManager.onRender2D(render2DEvent);
/* 247 */       GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent(priority = EventPriority.HIGHEST)
/*     */   public void onChatSent(ClientChatEvent event) {
/* 253 */     if (event.getMessage().startsWith(Command.getCommandPrefix())) {
/* 254 */       event.setCanceled(true);
/*     */       try {
/* 256 */         mc.field_71456_v.func_146158_b().func_146239_a(event.getMessage());
/* 257 */         if (event.getMessage().length() > 1) {
/* 258 */           Phobos.commandManager.executeCommand(event.getMessage().substring(Command.getCommandPrefix().length() - 1));
/*     */         } else {
/* 260 */           Command.sendMessage("Please enter a command.");
/*     */         } 
/* 262 */       } catch (Exception e) {
/* 263 */         e.printStackTrace();
/* 264 */         Command.sendMessage("§cAn error occurred while running this command. Check the log!");
/*     */       } 
/* 266 */       event.setMessage("");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\manager\EventManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */