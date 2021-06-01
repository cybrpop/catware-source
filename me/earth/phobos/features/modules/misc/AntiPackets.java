/*     */ package me.earth.phobos.features.modules.misc;
/*     */ 
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.features.command.Command;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ 
/*     */ public class AntiPackets
/*     */   extends Module
/*     */ {
/*  13 */   private final Setting<Mode> mode = register(new Setting("Packets", Mode.CLIENT));
/*  14 */   private final Setting<Integer> page = register(new Setting("SPackets", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(10), v -> (this.mode.getValue() == Mode.SERVER)));
/*  15 */   private final Setting<Integer> pages = register(new Setting("CPackets", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(4), v -> (this.mode.getValue() == Mode.CLIENT)));
/*  16 */   private final Setting<Boolean> AdvancementInfo = register(new Setting("AdvancementInfo", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 1)));
/*  17 */   private final Setting<Boolean> Animation = register(new Setting("Animation", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 1)));
/*  18 */   private final Setting<Boolean> BlockAction = register(new Setting("BlockAction", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 1)));
/*  19 */   private final Setting<Boolean> BlockBreakAnim = register(new Setting("BlockBreakAnim", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 1)));
/*  20 */   private final Setting<Boolean> BlockChange = register(new Setting("BlockChange", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 1)));
/*  21 */   private final Setting<Boolean> Camera = register(new Setting("Camera", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 1)));
/*  22 */   private final Setting<Boolean> ChangeGameState = register(new Setting("ChangeGameState", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 1)));
/*  23 */   private final Setting<Boolean> Chat = register(new Setting("Chat", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 1)));
/*  24 */   private final Setting<Boolean> ChunkData = register(new Setting("ChunkData", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 2)));
/*  25 */   private final Setting<Boolean> CloseWindow = register(new Setting("CloseWindow", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 2)));
/*  26 */   private final Setting<Boolean> CollectItem = register(new Setting("CollectItem", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 2)));
/*  27 */   private final Setting<Boolean> CombatEvent = register(new Setting("Combatevent", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 2)));
/*  28 */   private final Setting<Boolean> ConfirmTransaction = register(new Setting("ConfirmTransaction", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 2)));
/*  29 */   private final Setting<Boolean> Cooldown = register(new Setting("Cooldown", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 2)));
/*  30 */   private final Setting<Boolean> CustomPayload = register(new Setting("CustomPayload", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 2)));
/*  31 */   private final Setting<Boolean> CustomSound = register(new Setting("CustomSound", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 2)));
/*  32 */   private final Setting<Boolean> DestroyEntities = register(new Setting("DestroyEntities", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 3)));
/*  33 */   private final Setting<Boolean> Disconnect = register(new Setting("Disconnect", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 3)));
/*  34 */   private final Setting<Boolean> DisplayObjective = register(new Setting("DisplayObjective", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 3)));
/*  35 */   private final Setting<Boolean> Effect = register(new Setting("Effect", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 3)));
/*  36 */   private final Setting<Boolean> Entity = register(new Setting("Entity", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 3)));
/*  37 */   private final Setting<Boolean> EntityAttach = register(new Setting("EntityAttach", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 3)));
/*  38 */   private final Setting<Boolean> EntityEffect = register(new Setting("EntityEffect", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 3)));
/*  39 */   private final Setting<Boolean> EntityEquipment = register(new Setting("EntityEquipment", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 3)));
/*  40 */   private final Setting<Boolean> EntityHeadLook = register(new Setting("EntityHeadLook", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 4)));
/*  41 */   private final Setting<Boolean> EntityMetadata = register(new Setting("EntityMetadata", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 4)));
/*  42 */   private final Setting<Boolean> EntityProperties = register(new Setting("EntityProperties", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 4)));
/*  43 */   private final Setting<Boolean> EntityStatus = register(new Setting("EntityStatus", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 4)));
/*  44 */   private final Setting<Boolean> EntityTeleport = register(new Setting("EntityTeleport", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 4)));
/*  45 */   private final Setting<Boolean> EntityVelocity = register(new Setting("EntityVelocity", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 4)));
/*  46 */   private final Setting<Boolean> Explosion = register(new Setting("Explosion", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 4)));
/*  47 */   private final Setting<Boolean> HeldItemChange = register(new Setting("HeldItemChange", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 4)));
/*  48 */   private final Setting<Boolean> JoinGame = register(new Setting("JoinGame", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 5)));
/*  49 */   private final Setting<Boolean> KeepAlive = register(new Setting("KeepAlive", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 5)));
/*  50 */   private final Setting<Boolean> Maps = register(new Setting("Maps", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 5)));
/*  51 */   private final Setting<Boolean> MoveVehicle = register(new Setting("MoveVehicle", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 5)));
/*  52 */   private final Setting<Boolean> MultiBlockChange = register(new Setting("MultiBlockChange", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 5)));
/*  53 */   private final Setting<Boolean> OpenWindow = register(new Setting("OpenWindow", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 5)));
/*  54 */   private final Setting<Boolean> Particles = register(new Setting("Particles", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 5)));
/*  55 */   private final Setting<Boolean> PlaceGhostRecipe = register(new Setting("PlaceGhostRecipe", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 5)));
/*  56 */   private final Setting<Boolean> PlayerAbilities = register(new Setting("PlayerAbilities", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 6)));
/*  57 */   private final Setting<Boolean> PlayerListHeaderFooter = register(new Setting("PlayerListHeaderFooter", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 6)));
/*  58 */   private final Setting<Boolean> PlayerListItem = register(new Setting("PlayerListItem", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 6)));
/*  59 */   private final Setting<Boolean> PlayerPosLook = register(new Setting("PlayerPosLook", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 6)));
/*  60 */   private final Setting<Boolean> RecipeBook = register(new Setting("RecipeBook", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 6)));
/*  61 */   private final Setting<Boolean> RemoveEntityEffect = register(new Setting("RemoveEntityEffect", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 6)));
/*  62 */   private final Setting<Boolean> ResourcePackSend = register(new Setting("ResourcePackSend", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 6)));
/*  63 */   private final Setting<Boolean> Respawn = register(new Setting("Respawn", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 6)));
/*  64 */   private final Setting<Boolean> ScoreboardObjective = register(new Setting("ScoreboardObjective", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 7)));
/*  65 */   private final Setting<Boolean> SelectAdvancementsTab = register(new Setting("SelectAdvancementsTab", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 7)));
/*  66 */   private final Setting<Boolean> ServerDifficulty = register(new Setting("ServerDifficulty", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 7)));
/*  67 */   private final Setting<Boolean> SetExperience = register(new Setting("SetExperience", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 7)));
/*  68 */   private final Setting<Boolean> SetPassengers = register(new Setting("SetPassengers", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 7)));
/*  69 */   private final Setting<Boolean> SetSlot = register(new Setting("SetSlot", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 7)));
/*  70 */   private final Setting<Boolean> SignEditorOpen = register(new Setting("SignEditorOpen", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 7)));
/*  71 */   private final Setting<Boolean> SoundEffect = register(new Setting("SoundEffect", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 7)));
/*  72 */   private final Setting<Boolean> SpawnExperienceOrb = register(new Setting("SpawnExperienceOrb", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 8)));
/*  73 */   private final Setting<Boolean> SpawnGlobalEntity = register(new Setting("SpawnGlobalEntity", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 8)));
/*  74 */   private final Setting<Boolean> SpawnMob = register(new Setting("SpawnMob", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 8)));
/*  75 */   private final Setting<Boolean> SpawnObject = register(new Setting("SpawnObject", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 8)));
/*  76 */   private final Setting<Boolean> SpawnPainting = register(new Setting("SpawnPainting", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 8)));
/*  77 */   private final Setting<Boolean> SpawnPlayer = register(new Setting("SpawnPlayer", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 8)));
/*  78 */   private final Setting<Boolean> SpawnPosition = register(new Setting("SpawnPosition", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 8)));
/*  79 */   private final Setting<Boolean> Statistics = register(new Setting("Statistics", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 8)));
/*  80 */   private final Setting<Boolean> TabComplete = register(new Setting("TabComplete", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 9)));
/*  81 */   private final Setting<Boolean> Teams = register(new Setting("Teams", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 9)));
/*  82 */   private final Setting<Boolean> TimeUpdate = register(new Setting("TimeUpdate", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 9)));
/*  83 */   private final Setting<Boolean> Title = register(new Setting("Title", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 9)));
/*  84 */   private final Setting<Boolean> UnloadChunk = register(new Setting("UnloadChunk", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 9)));
/*  85 */   private final Setting<Boolean> UpdateBossInfo = register(new Setting("UpdateBossInfo", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 9)));
/*  86 */   private final Setting<Boolean> UpdateHealth = register(new Setting("UpdateHealth", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 9)));
/*  87 */   private final Setting<Boolean> UpdateScore = register(new Setting("UpdateScore", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 9)));
/*  88 */   private final Setting<Boolean> UpdateTileEntity = register(new Setting("UpdateTileEntity", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 10)));
/*  89 */   private final Setting<Boolean> UseBed = register(new Setting("UseBed", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 10)));
/*  90 */   private final Setting<Boolean> WindowItems = register(new Setting("WindowItems", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 10)));
/*  91 */   private final Setting<Boolean> WindowProperty = register(new Setting("WindowProperty", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 10)));
/*  92 */   private final Setting<Boolean> WorldBorder = register(new Setting("WorldBorder", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SERVER && ((Integer)this.page.getValue()).intValue() == 10)));
/*  93 */   private final Setting<Boolean> Animations = register(new Setting("Animations", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 1)));
/*  94 */   private final Setting<Boolean> ChatMessage = register(new Setting("ChatMessage", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 1)));
/*  95 */   private final Setting<Boolean> ClickWindow = register(new Setting("ClickWindow", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 1)));
/*  96 */   private final Setting<Boolean> ClientSettings = register(new Setting("ClientSettings", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 1)));
/*  97 */   private final Setting<Boolean> ClientStatus = register(new Setting("ClientStatus", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 1)));
/*  98 */   private final Setting<Boolean> CloseWindows = register(new Setting("CloseWindows", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 1)));
/*  99 */   private final Setting<Boolean> ConfirmTeleport = register(new Setting("ConfirmTeleport", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 1)));
/* 100 */   private final Setting<Boolean> ConfirmTransactions = register(new Setting("ConfirmTransactions", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 1)));
/* 101 */   private final Setting<Boolean> CreativeInventoryAction = register(new Setting("CreativeInventoryAction", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 2)));
/* 102 */   private final Setting<Boolean> CustomPayloads = register(new Setting("CustomPayloads", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 2)));
/* 103 */   private final Setting<Boolean> EnchantItem = register(new Setting("EnchantItem", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 2)));
/* 104 */   private final Setting<Boolean> EntityAction = register(new Setting("EntityAction", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 2)));
/* 105 */   private final Setting<Boolean> HeldItemChanges = register(new Setting("HeldItemChanges", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 2)));
/* 106 */   private final Setting<Boolean> Input = register(new Setting("Input", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 2)));
/* 107 */   private final Setting<Boolean> KeepAlives = register(new Setting("KeepAlives", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 2)));
/* 108 */   private final Setting<Boolean> PlaceRecipe = register(new Setting("PlaceRecipe", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 2)));
/* 109 */   private final Setting<Boolean> Player = register(new Setting("Player", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 3)));
/* 110 */   private final Setting<Boolean> PlayerAbility = register(new Setting("PlayerAbility", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 3)));
/* 111 */   private final Setting<Boolean> PlayerDigging = register(new Setting("PlayerDigging", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.page.getValue()).intValue() == 3)));
/* 112 */   private final Setting<Boolean> PlayerTryUseItem = register(new Setting("PlayerTryUseItem", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 3)));
/* 113 */   private final Setting<Boolean> PlayerTryUseItemOnBlock = register(new Setting("TryUseItemOnBlock", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 3)));
/* 114 */   private final Setting<Boolean> RecipeInfo = register(new Setting("RecipeInfo", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 3)));
/* 115 */   private final Setting<Boolean> ResourcePackStatus = register(new Setting("ResourcePackStatus", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 3)));
/* 116 */   private final Setting<Boolean> SeenAdvancements = register(new Setting("SeenAdvancements", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 3)));
/* 117 */   private final Setting<Boolean> PlayerPackets = register(new Setting("PlayerPackets", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 4)));
/* 118 */   private final Setting<Boolean> Spectate = register(new Setting("Spectate", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 4)));
/* 119 */   private final Setting<Boolean> SteerBoat = register(new Setting("SteerBoat", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 4)));
/* 120 */   private final Setting<Boolean> TabCompletion = register(new Setting("TabCompletion", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 4)));
/* 121 */   private final Setting<Boolean> UpdateSign = register(new Setting("UpdateSign", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 4)));
/* 122 */   private final Setting<Boolean> UseEntity = register(new Setting("UseEntity", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 4)));
/* 123 */   private final Setting<Boolean> VehicleMove = register(new Setting("VehicleMove", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.CLIENT && ((Integer)this.pages.getValue()).intValue() == 4)));
/* 124 */   private int hudAmount = 0;
/*     */   
/*     */   public AntiPackets() {
/* 127 */     super("AntiPackets", "Blocks Packets", Module.Category.MISC, true, false, false);
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketSend(PacketEvent.Send event) {
/* 132 */     if (!isEnabled()) {
/*     */       return;
/*     */     }
/* 135 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketAnimation && ((Boolean)this.Animations.getValue()).booleanValue()) {
/* 136 */       event.setCanceled(true);
/*     */     }
/* 138 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketChatMessage && ((Boolean)this.ChatMessage.getValue()).booleanValue()) {
/* 139 */       event.setCanceled(true);
/*     */     }
/* 141 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketClickWindow && ((Boolean)this.ClickWindow.getValue()).booleanValue()) {
/* 142 */       event.setCanceled(true);
/*     */     }
/* 144 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketClientSettings && ((Boolean)this.ClientSettings.getValue()).booleanValue()) {
/* 145 */       event.setCanceled(true);
/*     */     }
/* 147 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketClientStatus && ((Boolean)this.ClientStatus.getValue()).booleanValue()) {
/* 148 */       event.setCanceled(true);
/*     */     }
/* 150 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketCloseWindow && ((Boolean)this.CloseWindows.getValue()).booleanValue()) {
/* 151 */       event.setCanceled(true);
/*     */     }
/* 153 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketConfirmTeleport && ((Boolean)this.ConfirmTeleport.getValue()).booleanValue()) {
/* 154 */       event.setCanceled(true);
/*     */     }
/* 156 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketConfirmTransaction && ((Boolean)this.ConfirmTransactions.getValue()).booleanValue()) {
/* 157 */       event.setCanceled(true);
/*     */     }
/* 159 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketCreativeInventoryAction && ((Boolean)this.CreativeInventoryAction.getValue()).booleanValue()) {
/* 160 */       event.setCanceled(true);
/*     */     }
/* 162 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketCustomPayload && ((Boolean)this.CustomPayloads.getValue()).booleanValue()) {
/* 163 */       event.setCanceled(true);
/*     */     }
/* 165 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketEnchantItem && ((Boolean)this.EnchantItem.getValue()).booleanValue()) {
/* 166 */       event.setCanceled(true);
/*     */     }
/* 168 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketEntityAction && ((Boolean)this.EntityAction.getValue()).booleanValue()) {
/* 169 */       event.setCanceled(true);
/*     */     }
/* 171 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketHeldItemChange && ((Boolean)this.HeldItemChanges.getValue()).booleanValue()) {
/* 172 */       event.setCanceled(true);
/*     */     }
/* 174 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketInput && ((Boolean)this.Input.getValue()).booleanValue()) {
/* 175 */       event.setCanceled(true);
/*     */     }
/* 177 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketKeepAlive && ((Boolean)this.KeepAlives.getValue()).booleanValue()) {
/* 178 */       event.setCanceled(true);
/*     */     }
/* 180 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlaceRecipe && ((Boolean)this.PlaceRecipe.getValue()).booleanValue()) {
/* 181 */       event.setCanceled(true);
/*     */     }
/* 183 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlayer && ((Boolean)this.Player.getValue()).booleanValue()) {
/* 184 */       event.setCanceled(true);
/*     */     }
/* 186 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlayerAbilities && ((Boolean)this.PlayerAbility.getValue()).booleanValue()) {
/* 187 */       event.setCanceled(true);
/*     */     }
/* 189 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlayerDigging && ((Boolean)this.PlayerDigging.getValue()).booleanValue()) {
/* 190 */       event.setCanceled(true);
/*     */     }
/* 192 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlayerTryUseItem && ((Boolean)this.PlayerTryUseItem.getValue()).booleanValue()) {
/* 193 */       event.setCanceled(true);
/*     */     }
/* 195 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock && ((Boolean)this.PlayerTryUseItemOnBlock.getValue()).booleanValue()) {
/* 196 */       event.setCanceled(true);
/*     */     }
/* 198 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketRecipeInfo && ((Boolean)this.RecipeInfo.getValue()).booleanValue()) {
/* 199 */       event.setCanceled(true);
/*     */     }
/* 201 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketResourcePackStatus && ((Boolean)this.ResourcePackStatus.getValue()).booleanValue()) {
/* 202 */       event.setCanceled(true);
/*     */     }
/* 204 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketSeenAdvancements && ((Boolean)this.SeenAdvancements.getValue()).booleanValue()) {
/* 205 */       event.setCanceled(true);
/*     */     }
/* 207 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketSpectate && ((Boolean)this.Spectate.getValue()).booleanValue()) {
/* 208 */       event.setCanceled(true);
/*     */     }
/* 210 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketSteerBoat && ((Boolean)this.SteerBoat.getValue()).booleanValue()) {
/* 211 */       event.setCanceled(true);
/*     */     }
/* 213 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketTabComplete && ((Boolean)this.TabCompletion.getValue()).booleanValue()) {
/* 214 */       event.setCanceled(true);
/*     */     }
/* 216 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketUpdateSign && ((Boolean)this.UpdateSign.getValue()).booleanValue()) {
/* 217 */       event.setCanceled(true);
/*     */     }
/* 219 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketUseEntity && ((Boolean)this.UseEntity.getValue()).booleanValue()) {
/* 220 */       event.setCanceled(true);
/*     */     }
/* 222 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketVehicleMove && ((Boolean)this.VehicleMove.getValue()).booleanValue()) {
/* 223 */       event.setCanceled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketReceive(PacketEvent.Receive event) {
/* 229 */     if (!isEnabled()) {
/*     */       return;
/*     */     }
/* 232 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketAdvancementInfo && ((Boolean)this.AdvancementInfo.getValue()).booleanValue()) {
/* 233 */       event.setCanceled(true);
/*     */     }
/* 235 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketAnimation && ((Boolean)this.Animation.getValue()).booleanValue()) {
/* 236 */       event.setCanceled(true);
/*     */     }
/* 238 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketBlockAction && ((Boolean)this.BlockAction.getValue()).booleanValue()) {
/* 239 */       event.setCanceled(true);
/*     */     }
/* 241 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketBlockBreakAnim && ((Boolean)this.BlockBreakAnim.getValue()).booleanValue()) {
/* 242 */       event.setCanceled(true);
/*     */     }
/* 244 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketBlockChange && ((Boolean)this.BlockChange.getValue()).booleanValue()) {
/* 245 */       event.setCanceled(true);
/*     */     }
/* 247 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketCamera && ((Boolean)this.Camera.getValue()).booleanValue()) {
/* 248 */       event.setCanceled(true);
/*     */     }
/* 250 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketChangeGameState && ((Boolean)this.ChangeGameState.getValue()).booleanValue()) {
/* 251 */       event.setCanceled(true);
/*     */     }
/* 253 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketChat && ((Boolean)this.Chat.getValue()).booleanValue()) {
/* 254 */       event.setCanceled(true);
/*     */     }
/* 256 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketChunkData && ((Boolean)this.ChunkData.getValue()).booleanValue()) {
/* 257 */       event.setCanceled(true);
/*     */     }
/* 259 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketCloseWindow && ((Boolean)this.CloseWindow.getValue()).booleanValue()) {
/* 260 */       event.setCanceled(true);
/*     */     }
/* 262 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketCollectItem && ((Boolean)this.CollectItem.getValue()).booleanValue()) {
/* 263 */       event.setCanceled(true);
/*     */     }
/* 265 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketCombatEvent && ((Boolean)this.CombatEvent.getValue()).booleanValue()) {
/* 266 */       event.setCanceled(true);
/*     */     }
/* 268 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketConfirmTransaction && ((Boolean)this.ConfirmTransaction.getValue()).booleanValue()) {
/* 269 */       event.setCanceled(true);
/*     */     }
/* 271 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketCooldown && ((Boolean)this.Cooldown.getValue()).booleanValue()) {
/* 272 */       event.setCanceled(true);
/*     */     }
/* 274 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketCustomPayload && ((Boolean)this.CustomPayload.getValue()).booleanValue()) {
/* 275 */       event.setCanceled(true);
/*     */     }
/* 277 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketCustomSound && ((Boolean)this.CustomSound.getValue()).booleanValue()) {
/* 278 */       event.setCanceled(true);
/*     */     }
/* 280 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketDestroyEntities && ((Boolean)this.DestroyEntities.getValue()).booleanValue()) {
/* 281 */       event.setCanceled(true);
/*     */     }
/* 283 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketDisconnect && ((Boolean)this.Disconnect.getValue()).booleanValue()) {
/* 284 */       event.setCanceled(true);
/*     */     }
/* 286 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketChunkData && ((Boolean)this.ChunkData.getValue()).booleanValue()) {
/* 287 */       event.setCanceled(true);
/*     */     }
/* 289 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketCloseWindow && ((Boolean)this.CloseWindow.getValue()).booleanValue()) {
/* 290 */       event.setCanceled(true);
/*     */     }
/* 292 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketCollectItem && ((Boolean)this.CollectItem.getValue()).booleanValue()) {
/* 293 */       event.setCanceled(true);
/*     */     }
/* 295 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketDisplayObjective && ((Boolean)this.DisplayObjective.getValue()).booleanValue()) {
/* 296 */       event.setCanceled(true);
/*     */     }
/* 298 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketEffect && ((Boolean)this.Effect.getValue()).booleanValue()) {
/* 299 */       event.setCanceled(true);
/*     */     }
/* 301 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketEntity && ((Boolean)this.Entity.getValue()).booleanValue()) {
/* 302 */       event.setCanceled(true);
/*     */     }
/* 304 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketEntityAttach && ((Boolean)this.EntityAttach.getValue()).booleanValue()) {
/* 305 */       event.setCanceled(true);
/*     */     }
/* 307 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketEntityEffect && ((Boolean)this.EntityEffect.getValue()).booleanValue()) {
/* 308 */       event.setCanceled(true);
/*     */     }
/* 310 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketEntityEquipment && ((Boolean)this.EntityEquipment.getValue()).booleanValue()) {
/* 311 */       event.setCanceled(true);
/*     */     }
/* 313 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketEntityHeadLook && ((Boolean)this.EntityHeadLook.getValue()).booleanValue()) {
/* 314 */       event.setCanceled(true);
/*     */     }
/* 316 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketEntityMetadata && ((Boolean)this.EntityMetadata.getValue()).booleanValue()) {
/* 317 */       event.setCanceled(true);
/*     */     }
/* 319 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketEntityProperties && ((Boolean)this.EntityProperties.getValue()).booleanValue()) {
/* 320 */       event.setCanceled(true);
/*     */     }
/* 322 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketEntityStatus && ((Boolean)this.EntityStatus.getValue()).booleanValue()) {
/* 323 */       event.setCanceled(true);
/*     */     }
/* 325 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketEntityTeleport && ((Boolean)this.EntityTeleport.getValue()).booleanValue()) {
/* 326 */       event.setCanceled(true);
/*     */     }
/* 328 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketEntityVelocity && ((Boolean)this.EntityVelocity.getValue()).booleanValue()) {
/* 329 */       event.setCanceled(true);
/*     */     }
/* 331 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketExplosion && ((Boolean)this.Explosion.getValue()).booleanValue()) {
/* 332 */       event.setCanceled(true);
/*     */     }
/* 334 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketHeldItemChange && ((Boolean)this.HeldItemChange.getValue()).booleanValue()) {
/* 335 */       event.setCanceled(true);
/*     */     }
/* 337 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketJoinGame && ((Boolean)this.JoinGame.getValue()).booleanValue()) {
/* 338 */       event.setCanceled(true);
/*     */     }
/* 340 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketKeepAlive && ((Boolean)this.KeepAlive.getValue()).booleanValue()) {
/* 341 */       event.setCanceled(true);
/*     */     }
/* 343 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketMaps && ((Boolean)this.Maps.getValue()).booleanValue()) {
/* 344 */       event.setCanceled(true);
/*     */     }
/* 346 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketMoveVehicle && ((Boolean)this.MoveVehicle.getValue()).booleanValue()) {
/* 347 */       event.setCanceled(true);
/*     */     }
/* 349 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketMultiBlockChange && ((Boolean)this.MultiBlockChange.getValue()).booleanValue()) {
/* 350 */       event.setCanceled(true);
/*     */     }
/* 352 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketOpenWindow && ((Boolean)this.OpenWindow.getValue()).booleanValue()) {
/* 353 */       event.setCanceled(true);
/*     */     }
/* 355 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketParticles && ((Boolean)this.Particles.getValue()).booleanValue()) {
/* 356 */       event.setCanceled(true);
/*     */     }
/* 358 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketPlaceGhostRecipe && ((Boolean)this.PlaceGhostRecipe.getValue()).booleanValue()) {
/* 359 */       event.setCanceled(true);
/*     */     }
/* 361 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketPlayerAbilities && ((Boolean)this.PlayerAbilities.getValue()).booleanValue()) {
/* 362 */       event.setCanceled(true);
/*     */     }
/* 364 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketPlayerListHeaderFooter && ((Boolean)this.PlayerListHeaderFooter.getValue()).booleanValue()) {
/* 365 */       event.setCanceled(true);
/*     */     }
/* 367 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketPlayerListItem && ((Boolean)this.PlayerListItem.getValue()).booleanValue()) {
/* 368 */       event.setCanceled(true);
/*     */     }
/* 370 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketPlayerPosLook && ((Boolean)this.PlayerPosLook.getValue()).booleanValue()) {
/* 371 */       event.setCanceled(true);
/*     */     }
/* 373 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketRecipeBook && ((Boolean)this.RecipeBook.getValue()).booleanValue()) {
/* 374 */       event.setCanceled(true);
/*     */     }
/* 376 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketRemoveEntityEffect && ((Boolean)this.RemoveEntityEffect.getValue()).booleanValue()) {
/* 377 */       event.setCanceled(true);
/*     */     }
/* 379 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketResourcePackSend && ((Boolean)this.ResourcePackSend.getValue()).booleanValue()) {
/* 380 */       event.setCanceled(true);
/*     */     }
/* 382 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketRespawn && ((Boolean)this.Respawn.getValue()).booleanValue()) {
/* 383 */       event.setCanceled(true);
/*     */     }
/* 385 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketScoreboardObjective && ((Boolean)this.ScoreboardObjective.getValue()).booleanValue()) {
/* 386 */       event.setCanceled(true);
/*     */     }
/* 388 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketSelectAdvancementsTab && ((Boolean)this.SelectAdvancementsTab.getValue()).booleanValue()) {
/* 389 */       event.setCanceled(true);
/*     */     }
/* 391 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketServerDifficulty && ((Boolean)this.ServerDifficulty.getValue()).booleanValue()) {
/* 392 */       event.setCanceled(true);
/*     */     }
/* 394 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketSetExperience && ((Boolean)this.SetExperience.getValue()).booleanValue()) {
/* 395 */       event.setCanceled(true);
/*     */     }
/* 397 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketSetPassengers && ((Boolean)this.SetPassengers.getValue()).booleanValue()) {
/* 398 */       event.setCanceled(true);
/*     */     }
/* 400 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketSetSlot && ((Boolean)this.SetSlot.getValue()).booleanValue()) {
/* 401 */       event.setCanceled(true);
/*     */     }
/* 403 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketSignEditorOpen && ((Boolean)this.SignEditorOpen.getValue()).booleanValue()) {
/* 404 */       event.setCanceled(true);
/*     */     }
/* 406 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketSoundEffect && ((Boolean)this.SoundEffect.getValue()).booleanValue()) {
/* 407 */       event.setCanceled(true);
/*     */     }
/* 409 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketSpawnExperienceOrb && ((Boolean)this.SpawnExperienceOrb.getValue()).booleanValue()) {
/* 410 */       event.setCanceled(true);
/*     */     }
/* 412 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketSpawnGlobalEntity && ((Boolean)this.SpawnGlobalEntity.getValue()).booleanValue()) {
/* 413 */       event.setCanceled(true);
/*     */     }
/* 415 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketSpawnMob && ((Boolean)this.SpawnMob.getValue()).booleanValue()) {
/* 416 */       event.setCanceled(true);
/*     */     }
/* 418 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketSpawnObject && ((Boolean)this.SpawnObject.getValue()).booleanValue()) {
/* 419 */       event.setCanceled(true);
/*     */     }
/* 421 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketSpawnPainting && ((Boolean)this.SpawnPainting.getValue()).booleanValue()) {
/* 422 */       event.setCanceled(true);
/*     */     }
/* 424 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketSpawnPlayer && ((Boolean)this.SpawnPlayer.getValue()).booleanValue()) {
/* 425 */       event.setCanceled(true);
/*     */     }
/* 427 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketSpawnPosition && ((Boolean)this.SpawnPosition.getValue()).booleanValue()) {
/* 428 */       event.setCanceled(true);
/*     */     }
/* 430 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketStatistics && ((Boolean)this.Statistics.getValue()).booleanValue()) {
/* 431 */       event.setCanceled(true);
/*     */     }
/* 433 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketTabComplete && ((Boolean)this.TabComplete.getValue()).booleanValue()) {
/* 434 */       event.setCanceled(true);
/*     */     }
/* 436 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketTeams && ((Boolean)this.Teams.getValue()).booleanValue()) {
/* 437 */       event.setCanceled(true);
/*     */     }
/* 439 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketTimeUpdate && ((Boolean)this.TimeUpdate.getValue()).booleanValue()) {
/* 440 */       event.setCanceled(true);
/*     */     }
/* 442 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketTitle && ((Boolean)this.Title.getValue()).booleanValue()) {
/* 443 */       event.setCanceled(true);
/*     */     }
/* 445 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketUnloadChunk && ((Boolean)this.UnloadChunk.getValue()).booleanValue()) {
/* 446 */       event.setCanceled(true);
/*     */     }
/* 448 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketUpdateBossInfo && ((Boolean)this.UpdateBossInfo.getValue()).booleanValue()) {
/* 449 */       event.setCanceled(true);
/*     */     }
/* 451 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketUpdateHealth && ((Boolean)this.UpdateHealth.getValue()).booleanValue()) {
/* 452 */       event.setCanceled(true);
/*     */     }
/* 454 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketUpdateScore && ((Boolean)this.UpdateScore.getValue()).booleanValue()) {
/* 455 */       event.setCanceled(true);
/*     */     }
/* 457 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketUpdateTileEntity && ((Boolean)this.UpdateTileEntity.getValue()).booleanValue()) {
/* 458 */       event.setCanceled(true);
/*     */     }
/* 460 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketUseBed && ((Boolean)this.UseBed.getValue()).booleanValue()) {
/* 461 */       event.setCanceled(true);
/*     */     }
/* 463 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketWindowItems && ((Boolean)this.WindowItems.getValue()).booleanValue()) {
/* 464 */       event.setCanceled(true);
/*     */     }
/* 466 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketWindowProperty && ((Boolean)this.WindowProperty.getValue()).booleanValue()) {
/* 467 */       event.setCanceled(true);
/*     */     }
/* 469 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketWorldBorder && ((Boolean)this.WorldBorder.getValue()).booleanValue()) {
/* 470 */       event.setCanceled(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/* 476 */     String standart = "§aAntiPackets On!§f Cancelled Packets: ";
/* 477 */     StringBuilder text = new StringBuilder(standart);
/* 478 */     if (!this.settings.isEmpty()) {
/* 479 */       for (Setting setting : this.settings) {
/* 480 */         if (!(setting.getValue() instanceof Boolean) || !((Boolean)setting.getValue()).booleanValue() || setting.getName().equalsIgnoreCase("Enabled") || setting.getName().equalsIgnoreCase("drawn"))
/*     */           continue; 
/* 482 */         String name = setting.getName();
/* 483 */         text.append(name).append(", ");
/*     */       } 
/*     */     }
/* 486 */     if (text.toString().equals(standart)) {
/* 487 */       Command.sendMessage("§aAntiPackets On!§f Currently not cancelling any Packets.");
/*     */     } else {
/* 489 */       String output = removeLastChar(removeLastChar(text.toString()));
/* 490 */       Command.sendMessage(output);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/* 496 */     int amount = 0;
/* 497 */     if (!this.settings.isEmpty()) {
/* 498 */       for (Setting setting : this.settings) {
/* 499 */         if (!(setting.getValue() instanceof Boolean) || !((Boolean)setting.getValue()).booleanValue() || setting.getName().equalsIgnoreCase("Enabled") || setting.getName().equalsIgnoreCase("drawn"))
/*     */           continue; 
/* 501 */         amount++;
/*     */       } 
/*     */     }
/* 504 */     this.hudAmount = amount;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/* 509 */     if (this.hudAmount == 0) {
/* 510 */       return "";
/*     */     }
/* 512 */     return this.hudAmount + "";
/*     */   }
/*     */   
/*     */   public String removeLastChar(String str) {
/* 516 */     if (str != null && str.length() > 0) {
/* 517 */       str = str.substring(0, str.length() - 1);
/*     */     }
/* 519 */     return str;
/*     */   }
/*     */   
/*     */   public enum Mode {
/* 523 */     CLIENT,
/* 524 */     SERVER;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\misc\AntiPackets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */