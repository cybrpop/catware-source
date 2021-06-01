/*      */ package me.earth.phobos.features.modules.combat;
/*      */ import com.mojang.authlib.GameProfile;
/*      */ import java.awt.Color;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Queue;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentLinkedQueue;
/*      */ import java.util.concurrent.ScheduledExecutorService;
/*      */ import java.util.concurrent.atomic.AtomicBoolean;
/*      */ import me.earth.phobos.Phobos;
/*      */ import me.earth.phobos.event.events.ClientEvent;
/*      */ import me.earth.phobos.event.events.PacketEvent;
/*      */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*      */ import me.earth.phobos.features.command.Command;
/*      */ import me.earth.phobos.features.modules.Module;
/*      */ import me.earth.phobos.features.modules.client.Colors;
/*      */ import me.earth.phobos.features.setting.Bind;
/*      */ import me.earth.phobos.features.setting.Setting;
/*      */ import me.earth.phobos.util.BlockUtil;
/*      */ import me.earth.phobos.util.DamageUtil;
/*      */ import me.earth.phobos.util.EntityUtil;
/*      */ import me.earth.phobos.util.MathUtil;
/*      */ import me.earth.phobos.util.RenderUtil;
/*      */ import me.earth.phobos.util.Timer;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.client.entity.EntityOtherPlayerMP;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.item.EntityEnderCrystal;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.init.SoundEvents;
/*      */ import net.minecraft.item.ItemEndCrystal;
/*      */ import net.minecraft.network.Packet;
/*      */ import net.minecraft.network.play.client.CPacketPlayer;
/*      */ import net.minecraft.network.play.client.CPacketUseEntity;
/*      */ import net.minecraft.network.play.server.SPacketDestroyEntities;
/*      */ import net.minecraft.network.play.server.SPacketEntityStatus;
/*      */ import net.minecraft.network.play.server.SPacketExplosion;
/*      */ import net.minecraft.network.play.server.SPacketSoundEffect;
/*      */ import net.minecraft.network.play.server.SPacketSpawnObject;
/*      */ import net.minecraft.util.EnumFacing;
/*      */ import net.minecraft.util.EnumHand;
/*      */ import net.minecraft.util.SoundCategory;
/*      */ import net.minecraft.util.math.AxisAlignedBB;
/*      */ import net.minecraft.util.math.BlockPos;
/*      */ import net.minecraft.util.math.RayTraceResult;
/*      */ import net.minecraft.util.math.Vec3d;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*      */ import org.lwjgl.input.Keyboard;
/*      */ import org.lwjgl.input.Mouse;
/*      */ 
/*      */ public class AutoCrystal extends Module {
/*   58 */   public static EntityPlayer target = null;
/*   59 */   public static Set<BlockPos> lowDmgPos = (Set<BlockPos>)new ConcurrentSet();
/*   60 */   public static Set<BlockPos> placedPos = new HashSet<>();
/*   61 */   public static Set<BlockPos> brokenPos = new HashSet<>();
/*      */   private static AutoCrystal instance;
/*   63 */   public final Timer threadTimer = new Timer();
/*   64 */   private final Setting<Settings> setting = register(new Setting("Settings", Settings.PLACE));
/*   65 */   public final Setting<Boolean> attackOppositeHand = register(new Setting("OppositeHand", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.DEV)));
/*   66 */   public final Setting<Boolean> removeAfterAttack = register(new Setting("AttackRemove", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.DEV)));
/*   67 */   public final Setting<Boolean> antiBlock = register(new Setting("AntiFeetPlace", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.DEV)));
/*   68 */   private final Setting<Integer> switchCooldown = register(new Setting("Cooldown", Integer.valueOf(500), Integer.valueOf(0), Integer.valueOf(1000), v -> (this.setting.getValue() == Settings.MISC)));
/*   69 */   private final Setting<Integer> eventMode = register(new Setting("Updates", Integer.valueOf(3), Integer.valueOf(1), Integer.valueOf(3), v -> (this.setting.getValue() == Settings.DEV)));
/*   70 */   private final Timer switchTimer = new Timer();
/*   71 */   private final Timer manualTimer = new Timer();
/*   72 */   private final Timer breakTimer = new Timer();
/*   73 */   private final Timer placeTimer = new Timer();
/*   74 */   private final Timer syncTimer = new Timer();
/*   75 */   private final Timer predictTimer = new Timer();
/*   76 */   private final Timer renderTimer = new Timer();
/*   77 */   private final AtomicBoolean shouldInterrupt = new AtomicBoolean(false);
/*   78 */   private final Timer syncroTimer = new Timer();
/*   79 */   private final Map<EntityPlayer, Timer> totemPops = new ConcurrentHashMap<>();
/*   80 */   private final Queue<CPacketUseEntity> packetUseEntities = new LinkedList<>();
/*   81 */   private final AtomicBoolean threadOngoing = new AtomicBoolean(false);
/*   82 */   public Setting<Raytrace> raytrace = register(new Setting("Raytrace", Raytrace.NONE, v -> (this.setting.getValue() == Settings.MISC)));
/*   83 */   public Setting<Boolean> place = register(new Setting("Place", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.PLACE)));
/*   84 */   public Setting<Integer> placeDelay = register(new Setting("PlaceDelay", Integer.valueOf(25), Integer.valueOf(0), Integer.valueOf(500), v -> (this.setting.getValue() == Settings.PLACE && ((Boolean)this.place.getValue()).booleanValue())));
/*   85 */   public Setting<Float> placeRange = register(new Setting("PlaceRange", Float.valueOf(6.0F), Float.valueOf(0.0F), Float.valueOf(10.0F), v -> (this.setting.getValue() == Settings.PLACE && ((Boolean)this.place.getValue()).booleanValue())));
/*   86 */   public Setting<Float> minDamage = register(new Setting("MinDamage", Float.valueOf(7.0F), Float.valueOf(0.1F), Float.valueOf(20.0F), v -> (this.setting.getValue() == Settings.PLACE && ((Boolean)this.place.getValue()).booleanValue())));
/*   87 */   public Setting<Float> maxSelfPlace = register(new Setting("MaxSelfPlace", Float.valueOf(10.0F), Float.valueOf(0.1F), Float.valueOf(36.0F), v -> (this.setting.getValue() == Settings.PLACE && ((Boolean)this.place.getValue()).booleanValue())));
/*   88 */   public Setting<Integer> wasteAmount = register(new Setting("WasteAmount", Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(5), v -> (this.setting.getValue() == Settings.PLACE && ((Boolean)this.place.getValue()).booleanValue())));
/*   89 */   public Setting<Boolean> wasteMinDmgCount = register(new Setting("CountMinDmg", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.PLACE && ((Boolean)this.place.getValue()).booleanValue())));
/*   90 */   public Setting<Float> facePlace = register(new Setting("FacePlace", Float.valueOf(8.0F), Float.valueOf(0.1F), Float.valueOf(20.0F), v -> (this.setting.getValue() == Settings.PLACE && ((Boolean)this.place.getValue()).booleanValue())));
/*   91 */   public Setting<Float> placetrace = register(new Setting("Placetrace", Float.valueOf(4.5F), Float.valueOf(0.0F), Float.valueOf(10.0F), v -> (this.setting.getValue() == Settings.PLACE && ((Boolean)this.place.getValue()).booleanValue() && this.raytrace.getValue() != Raytrace.NONE && this.raytrace.getValue() != Raytrace.BREAK)));
/*   92 */   public Setting<Boolean> antiSurround = register(new Setting("AntiSurround", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.PLACE && ((Boolean)this.place.getValue()).booleanValue())));
/*   93 */   public Setting<Boolean> limitFacePlace = register(new Setting("LimitFacePlace", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.PLACE && ((Boolean)this.place.getValue()).booleanValue())));
/*   94 */   public Setting<Boolean> oneDot15 = register(new Setting("1.15", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.PLACE && ((Boolean)this.place.getValue()).booleanValue())));
/*   95 */   public Setting<Boolean> doublePop = register(new Setting("AntiTotem", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.PLACE && ((Boolean)this.place.getValue()).booleanValue())));
/*   96 */   public Setting<Double> popHealth = register(new Setting("PopHealth", Double.valueOf(1.0D), Double.valueOf(0.0D), Double.valueOf(3.0D), v -> (this.setting.getValue() == Settings.PLACE && ((Boolean)this.place.getValue()).booleanValue() && ((Boolean)this.doublePop.getValue()).booleanValue())));
/*   97 */   public Setting<Float> popDamage = register(new Setting("PopDamage", Float.valueOf(4.0F), Float.valueOf(0.0F), Float.valueOf(6.0F), v -> (this.setting.getValue() == Settings.PLACE && ((Boolean)this.place.getValue()).booleanValue() && ((Boolean)this.doublePop.getValue()).booleanValue())));
/*   98 */   public Setting<Integer> popTime = register(new Setting("PopTime", Integer.valueOf(500), Integer.valueOf(0), Integer.valueOf(1000), v -> (this.setting.getValue() == Settings.PLACE && ((Boolean)this.place.getValue()).booleanValue() && ((Boolean)this.doublePop.getValue()).booleanValue())));
/*   99 */   public Setting<Boolean> explode = register(new Setting("Break", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.BREAK)));
/*  100 */   public Setting<Switch> switchMode = register(new Setting("Attack", Switch.BREAKSLOT, v -> (this.setting.getValue() == Settings.BREAK && ((Boolean)this.explode.getValue()).booleanValue())));
/*  101 */   public Setting<Integer> breakDelay = register(new Setting("BreakDelay", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(500), v -> (this.setting.getValue() == Settings.BREAK && ((Boolean)this.explode.getValue()).booleanValue())));
/*  102 */   public Setting<Float> breakRange = register(new Setting("BreakRange", Float.valueOf(6.0F), Float.valueOf(0.0F), Float.valueOf(10.0F), v -> (this.setting.getValue() == Settings.BREAK && ((Boolean)this.explode.getValue()).booleanValue())));
/*  103 */   public Setting<Integer> packets = register(new Setting("Packets", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(6), v -> (this.setting.getValue() == Settings.BREAK && ((Boolean)this.explode.getValue()).booleanValue())));
/*  104 */   public Setting<Float> maxSelfBreak = register(new Setting("MaxSelfBreak", Float.valueOf(10.0F), Float.valueOf(0.1F), Float.valueOf(36.0F), v -> (this.setting.getValue() == Settings.BREAK && ((Boolean)this.explode.getValue()).booleanValue())));
/*  105 */   public Setting<Float> breaktrace = register(new Setting("Breaktrace", Float.valueOf(4.5F), Float.valueOf(0.0F), Float.valueOf(10.0F), v -> (this.setting.getValue() == Settings.BREAK && ((Boolean)this.explode.getValue()).booleanValue() && this.raytrace.getValue() != Raytrace.NONE && this.raytrace.getValue() != Raytrace.PLACE)));
/*  106 */   public Setting<Boolean> manual = register(new Setting("Manual", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.BREAK)));
/*  107 */   public Setting<Boolean> manualMinDmg = register(new Setting("ManMinDmg", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.BREAK && ((Boolean)this.manual.getValue()).booleanValue())));
/*  108 */   public Setting<Integer> manualBreak = register(new Setting("ManualDelay", Integer.valueOf(500), Integer.valueOf(0), Integer.valueOf(500), v -> (this.setting.getValue() == Settings.BREAK && ((Boolean)this.manual.getValue()).booleanValue())));
/*  109 */   public Setting<Boolean> sync = register(new Setting("Sync", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.BREAK && (((Boolean)this.explode.getValue()).booleanValue() || ((Boolean)this.manual.getValue()).booleanValue()))));
/*  110 */   public Setting<Boolean> instant = register(new Setting("Predict", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.BREAK && ((Boolean)this.explode.getValue()).booleanValue() && ((Boolean)this.place.getValue()).booleanValue())));
/*  111 */   public Setting<PredictTimer> instantTimer = register(new Setting("PredictTimer", PredictTimer.NONE, v -> (this.setting.getValue() == Settings.BREAK && ((Boolean)this.explode.getValue()).booleanValue() && ((Boolean)this.place.getValue()).booleanValue() && ((Boolean)this.instant.getValue()).booleanValue())));
/*  112 */   public Setting<Boolean> resetBreakTimer = register(new Setting("ResetBreakTimer", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.BREAK && ((Boolean)this.explode.getValue()).booleanValue() && ((Boolean)this.place.getValue()).booleanValue() && ((Boolean)this.instant.getValue()).booleanValue())));
/*  113 */   public Setting<Integer> predictDelay = register(new Setting("PredictDelay", Integer.valueOf(12), Integer.valueOf(0), Integer.valueOf(500), v -> (this.setting.getValue() == Settings.BREAK && ((Boolean)this.explode.getValue()).booleanValue() && ((Boolean)this.place.getValue()).booleanValue() && ((Boolean)this.instant.getValue()).booleanValue() && this.instantTimer.getValue() == PredictTimer.PREDICT)));
/*  114 */   public Setting<Boolean> predictCalc = register(new Setting("PredictCalc", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.BREAK && ((Boolean)this.explode.getValue()).booleanValue() && ((Boolean)this.place.getValue()).booleanValue() && ((Boolean)this.instant.getValue()).booleanValue())));
/*  115 */   public Setting<Boolean> superSafe = register(new Setting("SuperSafe", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.BREAK && ((Boolean)this.explode.getValue()).booleanValue() && ((Boolean)this.place.getValue()).booleanValue() && ((Boolean)this.instant.getValue()).booleanValue())));
/*  116 */   public Setting<Boolean> antiCommit = register(new Setting("AntiOverCommit", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.BREAK && ((Boolean)this.explode.getValue()).booleanValue() && ((Boolean)this.place.getValue()).booleanValue() && ((Boolean)this.instant.getValue()).booleanValue())));
/*  117 */   public Setting<Boolean> render = register(new Setting("Render", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.RENDER)));
/*  118 */   private final Setting<Integer> red = register(new Setting("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (this.setting.getValue() == Settings.RENDER && ((Boolean)this.render.getValue()).booleanValue())));
/*  119 */   private final Setting<Integer> green = register(new Setting("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (this.setting.getValue() == Settings.RENDER && ((Boolean)this.render.getValue()).booleanValue())));
/*  120 */   private final Setting<Integer> blue = register(new Setting("Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (this.setting.getValue() == Settings.RENDER && ((Boolean)this.render.getValue()).booleanValue())));
/*  121 */   private final Setting<Integer> alpha = register(new Setting("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (this.setting.getValue() == Settings.RENDER && ((Boolean)this.render.getValue()).booleanValue())));
/*  122 */   public Setting<Boolean> colorSync = register(new Setting("CSync", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.RENDER)));
/*  123 */   public Setting<Boolean> box = register(new Setting("Box", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.RENDER && ((Boolean)this.render.getValue()).booleanValue())));
/*  124 */   private final Setting<Integer> boxAlpha = register(new Setting("BoxAlpha", Integer.valueOf(125), Integer.valueOf(0), Integer.valueOf(255), v -> (this.setting.getValue() == Settings.RENDER && ((Boolean)this.render.getValue()).booleanValue() && ((Boolean)this.box.getValue()).booleanValue())));
/*  125 */   public Setting<Boolean> outline = register(new Setting("Outline", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.RENDER && ((Boolean)this.render.getValue()).booleanValue())));
/*  126 */   private final Setting<Float> lineWidth = register(new Setting("LineWidth", Float.valueOf(1.5F), Float.valueOf(0.1F), Float.valueOf(5.0F), v -> (this.setting.getValue() == Settings.RENDER && ((Boolean)this.render.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue())));
/*  127 */   public Setting<Boolean> text = register(new Setting("Text", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.RENDER && ((Boolean)this.render.getValue()).booleanValue())));
/*  128 */   public Setting<Boolean> customOutline = register(new Setting("CustomLine", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.RENDER && ((Boolean)this.render.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue())));
/*  129 */   private final Setting<Integer> cRed = register(new Setting("OL-Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (this.setting.getValue() == Settings.RENDER && ((Boolean)this.render.getValue()).booleanValue() && ((Boolean)this.customOutline.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue())));
/*  130 */   private final Setting<Integer> cGreen = register(new Setting("OL-Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (this.setting.getValue() == Settings.RENDER && ((Boolean)this.render.getValue()).booleanValue() && ((Boolean)this.customOutline.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue())));
/*  131 */   private final Setting<Integer> cBlue = register(new Setting("OL-Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (this.setting.getValue() == Settings.RENDER && ((Boolean)this.render.getValue()).booleanValue() && ((Boolean)this.customOutline.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue())));
/*  132 */   private final Setting<Integer> cAlpha = register(new Setting("OL-Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (this.setting.getValue() == Settings.RENDER && ((Boolean)this.render.getValue()).booleanValue() && ((Boolean)this.customOutline.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue())));
/*  133 */   public Setting<Boolean> holdFacePlace = register(new Setting("HoldFacePlace", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.MISC)));
/*  134 */   public Setting<Boolean> holdFaceBreak = register(new Setting("HoldSlowBreak", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.MISC && ((Boolean)this.holdFacePlace.getValue()).booleanValue())));
/*  135 */   public Setting<Boolean> slowFaceBreak = register(new Setting("SlowFaceBreak", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.MISC)));
/*  136 */   public Setting<Boolean> actualSlowBreak = register(new Setting("ActuallySlow", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.MISC)));
/*  137 */   public Setting<Integer> facePlaceSpeed = register(new Setting("FaceSpeed", Integer.valueOf(500), Integer.valueOf(0), Integer.valueOf(500), v -> (this.setting.getValue() == Settings.MISC)));
/*  138 */   public Setting<Boolean> antiNaked = register(new Setting("AntiNaked", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.MISC)));
/*  139 */   public Setting<Float> range = register(new Setting("Range", Float.valueOf(12.0F), Float.valueOf(0.1F), Float.valueOf(20.0F), v -> (this.setting.getValue() == Settings.MISC)));
/*  140 */   public Setting<Target> targetMode = register(new Setting("Target", Target.CLOSEST, v -> (this.setting.getValue() == Settings.MISC)));
/*  141 */   public Setting<Integer> minArmor = register(new Setting("MinArmor", Integer.valueOf(5), Integer.valueOf(0), Integer.valueOf(125), v -> (this.setting.getValue() == Settings.MISC)));
/*  142 */   public Setting<AutoSwitch> autoSwitch = register(new Setting("Switch", AutoSwitch.TOGGLE, v -> (this.setting.getValue() == Settings.MISC)));
/*  143 */   public Setting<Bind> switchBind = register(new Setting("SwitchBind", new Bind(-1), v -> (this.setting.getValue() == Settings.MISC && this.autoSwitch.getValue() == AutoSwitch.TOGGLE)));
/*  144 */   public Setting<Boolean> offhandSwitch = register(new Setting("Offhand", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.MISC && this.autoSwitch.getValue() != AutoSwitch.NONE)));
/*  145 */   public Setting<Boolean> switchBack = register(new Setting("Switchback", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.MISC && this.autoSwitch.getValue() != AutoSwitch.NONE && ((Boolean)this.offhandSwitch.getValue()).booleanValue())));
/*  146 */   public Setting<Boolean> lethalSwitch = register(new Setting("LethalSwitch", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.MISC && this.autoSwitch.getValue() != AutoSwitch.NONE)));
/*  147 */   public Setting<Boolean> mineSwitch = register(new Setting("MineSwitch", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.MISC && this.autoSwitch.getValue() != AutoSwitch.NONE)));
/*  148 */   public Setting<Rotate> rotate = register(new Setting("Rotate", Rotate.OFF, v -> (this.setting.getValue() == Settings.MISC)));
/*  149 */   public Setting<Boolean> suicide = register(new Setting("Suicide", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.MISC)));
/*  150 */   public Setting<Boolean> webAttack = register(new Setting("WebAttack", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.MISC && this.targetMode.getValue() != Target.DAMAGE)));
/*  151 */   public Setting<Boolean> fullCalc = register(new Setting("ExtraCalc", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.MISC)));
/*  152 */   public Setting<Boolean> sound = register(new Setting("Sound", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.MISC)));
/*  153 */   public Setting<Float> soundRange = register(new Setting("SoundRange", Float.valueOf(12.0F), Float.valueOf(0.0F), Float.valueOf(12.0F), v -> (this.setting.getValue() == Settings.MISC)));
/*  154 */   public Setting<Float> soundPlayer = register(new Setting("SoundPlayer", Float.valueOf(6.0F), Float.valueOf(0.0F), Float.valueOf(12.0F), v -> (this.setting.getValue() == Settings.MISC)));
/*  155 */   public Setting<Boolean> soundConfirm = register(new Setting("SoundConfirm", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.MISC)));
/*  156 */   public Setting<Boolean> extraSelfCalc = register(new Setting("MinSelfDmg", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.MISC)));
/*  157 */   public Setting<AntiFriendPop> antiFriendPop = register(new Setting("FriendPop", AntiFriendPop.NONE, v -> (this.setting.getValue() == Settings.MISC)));
/*  158 */   public Setting<Boolean> noCount = register(new Setting("AntiCount", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.MISC && (this.antiFriendPop.getValue() == AntiFriendPop.ALL || this.antiFriendPop.getValue() == AntiFriendPop.BREAK))));
/*  159 */   public Setting<Boolean> calcEvenIfNoDamage = register(new Setting("BigFriendCalc", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.MISC && (this.antiFriendPop.getValue() == AntiFriendPop.ALL || this.antiFriendPop.getValue() == AntiFriendPop.BREAK) && this.targetMode.getValue() != Target.DAMAGE)));
/*  160 */   public Setting<Boolean> predictFriendDmg = register(new Setting("PredictFriend", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.MISC && (this.antiFriendPop.getValue() == AntiFriendPop.ALL || this.antiFriendPop.getValue() == AntiFriendPop.BREAK) && ((Boolean)this.instant.getValue()).booleanValue())));
/*  161 */   public Setting<Float> minMinDmg = register(new Setting("MinMinDmg", Float.valueOf(0.0F), Float.valueOf(0.0F), Float.valueOf(3.0F), v -> (this.setting.getValue() == Settings.DEV && ((Boolean)this.place.getValue()).booleanValue())));
/*  162 */   public Setting<Boolean> breakSwing = register(new Setting("BreakSwing", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.DEV)));
/*  163 */   public Setting<Boolean> placeSwing = register(new Setting("PlaceSwing", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.DEV)));
/*  164 */   public Setting<Boolean> exactHand = register(new Setting("ExactHand", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.DEV && ((Boolean)this.placeSwing.getValue()).booleanValue())));
/*  165 */   public Setting<Boolean> justRender = register(new Setting("JustRender", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.DEV)));
/*  166 */   public Setting<Boolean> fakeSwing = register(new Setting("FakeSwing", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.DEV && ((Boolean)this.justRender.getValue()).booleanValue())));
/*  167 */   public Setting<Logic> logic = register(new Setting("Logic", Logic.BREAKPLACE, v -> (this.setting.getValue() == Settings.DEV)));
/*  168 */   public Setting<DamageSync> damageSync = register(new Setting("DamageSync", DamageSync.NONE, v -> (this.setting.getValue() == Settings.DEV)));
/*  169 */   public Setting<Integer> damageSyncTime = register(new Setting("SyncDelay", Integer.valueOf(500), Integer.valueOf(0), Integer.valueOf(500), v -> (this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE)));
/*  170 */   public Setting<Float> dropOff = register(new Setting("DropOff", Float.valueOf(5.0F), Float.valueOf(0.0F), Float.valueOf(10.0F), v -> (this.setting.getValue() == Settings.DEV && this.damageSync.getValue() == DamageSync.BREAK)));
/*  171 */   public Setting<Integer> confirm = register(new Setting("Confirm", Integer.valueOf(250), Integer.valueOf(0), Integer.valueOf(1000), v -> (this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE)));
/*  172 */   public Setting<Boolean> syncedFeetPlace = register(new Setting("FeetSync", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE)));
/*  173 */   public Setting<Boolean> fullSync = register(new Setting("FullSync", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE && ((Boolean)this.syncedFeetPlace.getValue()).booleanValue())));
/*  174 */   public Setting<Boolean> syncCount = register(new Setting("SyncCount", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE && ((Boolean)this.syncedFeetPlace.getValue()).booleanValue())));
/*  175 */   public Setting<Boolean> hyperSync = register(new Setting("HyperSync", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE && ((Boolean)this.syncedFeetPlace.getValue()).booleanValue())));
/*  176 */   public Setting<Boolean> gigaSync = register(new Setting("GigaSync", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE && ((Boolean)this.syncedFeetPlace.getValue()).booleanValue())));
/*  177 */   public Setting<Boolean> syncySync = register(new Setting("SyncySync", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE && ((Boolean)this.syncedFeetPlace.getValue()).booleanValue())));
/*  178 */   public Setting<Boolean> enormousSync = register(new Setting("EnormousSync", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE && ((Boolean)this.syncedFeetPlace.getValue()).booleanValue())));
/*  179 */   public Setting<Boolean> holySync = register(new Setting("UnbelievableSync", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE && ((Boolean)this.syncedFeetPlace.getValue()).booleanValue())));
/*  180 */   public Setting<Boolean> rotateFirst = register(new Setting("FirstRotation", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.DEV && this.rotate.getValue() != Rotate.OFF && ((Integer)this.eventMode.getValue()).intValue() == 2)));
/*  181 */   public Setting<ThreadMode> threadMode = register(new Setting("Thread", ThreadMode.NONE, v -> (this.setting.getValue() == Settings.DEV)));
/*  182 */   public Setting<Integer> threadDelay = register(new Setting("ThreadDelay", Integer.valueOf(50), Integer.valueOf(1), Integer.valueOf(1000), v -> (this.setting.getValue() == Settings.DEV && this.threadMode.getValue() != ThreadMode.NONE)));
/*  183 */   public Setting<Boolean> syncThreadBool = register(new Setting("ThreadSync", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.DEV && this.threadMode.getValue() != ThreadMode.NONE)));
/*  184 */   public Setting<Integer> syncThreads = register(new Setting("SyncThreads", Integer.valueOf(1000), Integer.valueOf(1), Integer.valueOf(10000), v -> (this.setting.getValue() == Settings.DEV && this.threadMode.getValue() != ThreadMode.NONE && ((Boolean)this.syncThreadBool.getValue()).booleanValue())));
/*  185 */   public Setting<Boolean> predictPos = register(new Setting("PredictPos", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.DEV)));
/*  186 */   public Setting<Boolean> renderExtrapolation = register(new Setting("RenderExtrapolation", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.DEV && ((Boolean)this.predictPos.getValue()).booleanValue())));
/*  187 */   public Setting<Integer> predictTicks = register(new Setting("ExtrapolationTicks", Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(20), v -> (this.setting.getValue() == Settings.DEV && ((Boolean)this.predictPos.getValue()).booleanValue())));
/*  188 */   public Setting<Integer> rotations = register(new Setting("Spoofs", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(20), v -> (this.setting.getValue() == Settings.DEV)));
/*  189 */   public Setting<Boolean> predictRotate = register(new Setting("PredictRotate", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.DEV)));
/*  190 */   public Setting<Float> predictOffset = register(new Setting("PredictOffset", Float.valueOf(0.0F), Float.valueOf(0.0F), Float.valueOf(4.0F), v -> (this.setting.getValue() == Settings.DEV)));
/*  191 */   public Setting<Boolean> brownZombie = register(new Setting("BrownZombieMode", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.MISC)));
/*  192 */   public Setting<Boolean> doublePopOnDamage = register(new Setting("DamagePop", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.PLACE && ((Boolean)this.place.getValue()).booleanValue() && ((Boolean)this.doublePop.getValue()).booleanValue() && this.targetMode.getValue() == Target.DAMAGE)));
/*      */   public boolean rotating = false;
/*  194 */   private Queue<Entity> attackList = new ConcurrentLinkedQueue<>();
/*  195 */   private Map<Entity, Float> crystalMap = new HashMap<>();
/*  196 */   private Entity efficientTarget = null;
/*  197 */   private double currentDamage = 0.0D;
/*  198 */   private double renderDamage = 0.0D;
/*  199 */   private double lastDamage = 0.0D;
/*      */   private boolean didRotation = false;
/*      */   private boolean switching = false;
/*  202 */   private BlockPos placePos = null;
/*  203 */   private BlockPos renderPos = null;
/*      */   private boolean mainHand = false;
/*      */   private boolean offHand = false;
/*  206 */   private int crystalCount = 0;
/*  207 */   private int minDmgCount = 0;
/*  208 */   private int lastSlot = -1;
/*  209 */   private float yaw = 0.0F;
/*  210 */   private float pitch = 0.0F;
/*  211 */   private BlockPos webPos = null;
/*  212 */   private BlockPos lastPos = null;
/*      */   private boolean posConfirmed = false;
/*      */   private boolean foundDoublePop = false;
/*  215 */   private int rotationPacketsSpoofed = 0;
/*      */   private ScheduledExecutorService executor;
/*      */   private Thread thread;
/*      */   private EntityPlayer currentSyncTarget;
/*      */   private BlockPos syncedPlayerPos;
/*      */   private BlockPos syncedCrystalPos;
/*      */   private PlaceInfo placeInfo;
/*      */   private boolean addTolowDmg;
/*      */   
/*      */   public AutoCrystal() {
/*  225 */     super("AutoCrystal", "Best CA on the market", Module.Category.COMBAT, true, false, false);
/*  226 */     instance = this;
/*      */   }
/*      */   
/*      */   public static AutoCrystal getInstance() {
/*  230 */     if (instance == null) {
/*  231 */       instance = new AutoCrystal();
/*      */     }
/*  233 */     return instance;
/*      */   }
/*      */ 
/*      */   
/*      */   public void onTick() {
/*  238 */     if (this.threadMode.getValue() == ThreadMode.NONE && ((Integer)this.eventMode.getValue()).intValue() == 3) {
/*  239 */       doAutoCrystal();
/*      */     }
/*      */   }
/*      */   
/*      */   @SubscribeEvent
/*      */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/*  245 */     if (event.getStage() == 1) {
/*  246 */       postProcessing();
/*      */     }
/*  248 */     if (event.getStage() != 0) {
/*      */       return;
/*      */     }
/*  251 */     if (((Integer)this.eventMode.getValue()).intValue() == 2) {
/*  252 */       doAutoCrystal();
/*      */     }
/*      */   }
/*      */   
/*      */   public void postTick() {
/*  257 */     if (this.threadMode.getValue() != ThreadMode.NONE) {
/*  258 */       processMultiThreading();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void onUpdate() {
/*  264 */     if (this.threadMode.getValue() == ThreadMode.NONE && ((Integer)this.eventMode.getValue()).intValue() == 1) {
/*  265 */       doAutoCrystal();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void onToggle() {
/*  271 */     brokenPos.clear();
/*  272 */     placedPos.clear();
/*  273 */     this.totemPops.clear();
/*  274 */     this.rotating = false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void onDisable() {
/*  279 */     if (this.thread != null) {
/*  280 */       this.shouldInterrupt.set(true);
/*      */     }
/*  282 */     if (this.executor != null) {
/*  283 */       this.executor.shutdown();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void onEnable() {
/*  289 */     if (this.threadMode.getValue() != ThreadMode.NONE) {
/*  290 */       processMultiThreading();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public String getDisplayInfo() {
/*  296 */     if (this.switching) {
/*  297 */       return "§aSwitch";
/*      */     }
/*  299 */     if (target != null) {
/*  300 */       return target.func_70005_c_();
/*      */     }
/*  302 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   @SubscribeEvent
/*      */   public void onPacketSend(PacketEvent.Send event) {
/*  308 */     if (event.getStage() == 0 && this.rotate.getValue() != Rotate.OFF && this.rotating && ((Integer)this.eventMode.getValue()).intValue() != 2 && event.getPacket() instanceof CPacketPlayer) {
/*  309 */       CPacketPlayer packet2 = (CPacketPlayer)event.getPacket();
/*  310 */       packet2.field_149476_e = this.yaw;
/*  311 */       packet2.field_149473_f = this.pitch;
/*  312 */       this.rotationPacketsSpoofed++;
/*  313 */       if (this.rotationPacketsSpoofed >= ((Integer)this.rotations.getValue()).intValue()) {
/*  314 */         this.rotating = false;
/*  315 */         this.rotationPacketsSpoofed = 0;
/*      */       } 
/*      */     } 
/*  318 */     BlockPos pos = null; CPacketUseEntity packet;
/*  319 */     if (event.getStage() == 0 && event.getPacket() instanceof CPacketUseEntity && (packet = (CPacketUseEntity)event.getPacket()).func_149565_c() == CPacketUseEntity.Action.ATTACK && packet.func_149564_a((World)mc.field_71441_e) instanceof EntityEnderCrystal) {
/*  320 */       pos = packet.func_149564_a((World)mc.field_71441_e).func_180425_c();
/*  321 */       if (((Boolean)this.removeAfterAttack.getValue()).booleanValue()) {
/*  322 */         ((Entity)Objects.<Entity>requireNonNull(packet.func_149564_a((World)mc.field_71441_e))).func_70106_y();
/*  323 */         mc.field_71441_e.func_73028_b(packet.field_149567_a);
/*      */       } 
/*      */     } 
/*  326 */     if (event.getStage() == 0 && event.getPacket() instanceof CPacketUseEntity && (packet = (CPacketUseEntity)event.getPacket()).func_149565_c() == CPacketUseEntity.Action.ATTACK && packet.func_149564_a((World)mc.field_71441_e) instanceof EntityEnderCrystal) {
/*  327 */       EntityEnderCrystal crystal = (EntityEnderCrystal)packet.func_149564_a((World)mc.field_71441_e);
/*  328 */       if (((Boolean)this.antiBlock.getValue()).booleanValue() && EntityUtil.isCrystalAtFeet(crystal, ((Float)this.range.getValue()).floatValue()) && pos != null) {
/*  329 */         rotateToPos(pos);
/*  330 */         BlockUtil.placeCrystalOnBlock(this.placePos, this.offHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, ((Boolean)this.placeSwing.getValue()).booleanValue(), ((Boolean)this.exactHand.getValue()).booleanValue());
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   @SubscribeEvent(priority = EventPriority.HIGH, receiveCanceled = true)
/*      */   public void onPacketReceive(PacketEvent.Receive event) {
/*  338 */     if (fullNullCheck()) {
/*      */       return;
/*      */     }
/*  341 */     if (!((Boolean)this.justRender.getValue()).booleanValue() && this.switchTimer.passedMs(((Integer)this.switchCooldown.getValue()).intValue()) && ((Boolean)this.explode.getValue()).booleanValue() && ((Boolean)this.instant.getValue()).booleanValue() && event.getPacket() instanceof SPacketSpawnObject && (this.syncedCrystalPos == null || !((Boolean)this.syncedFeetPlace.getValue()).booleanValue() || this.damageSync.getValue() == DamageSync.NONE)) {
/*      */       
/*  343 */       SPacketSpawnObject packet2 = (SPacketSpawnObject)event.getPacket(); BlockPos pos;
/*  344 */       if (packet2.func_148993_l() == 51 && mc.field_71439_g.func_174818_b(pos = new BlockPos(packet2.func_186880_c(), packet2.func_186882_d(), packet2.func_186881_e())) + ((Float)this.predictOffset.getValue()).floatValue() <= MathUtil.square(((Float)this.breakRange.getValue()).floatValue()) && (this.instantTimer.getValue() == PredictTimer.NONE || (this.instantTimer.getValue() == PredictTimer.BREAK && this.breakTimer.passedMs(((Integer)this.breakDelay.getValue()).intValue())) || (this.instantTimer.getValue() == PredictTimer.PREDICT && this.predictTimer.passedMs(((Integer)this.predictDelay.getValue()).intValue())))) {
/*  345 */         if (predictSlowBreak(pos.func_177977_b())) {
/*      */           return;
/*      */         }
/*  348 */         if (((Boolean)this.predictFriendDmg.getValue()).booleanValue() && (this.antiFriendPop.getValue() == AntiFriendPop.BREAK || this.antiFriendPop.getValue() == AntiFriendPop.ALL) && isRightThread())
/*  349 */           for (EntityPlayer friend : mc.field_71441_e.field_73010_i) {
/*  350 */             if (friend == null || mc.field_71439_g.equals(friend) || friend.func_174818_b(pos) > MathUtil.square(((Float)this.range.getValue()).floatValue() + ((Float)this.placeRange.getValue()).floatValue()) || !Phobos.friendManager.isFriend(friend) || DamageUtil.calculateDamage(pos, (Entity)friend) <= EntityUtil.getHealth((Entity)friend) + 0.5D) {
/*      */               continue;
/*      */             }
/*      */             return;
/*      */           }  
/*  355 */         if (placedPos.contains(pos.func_177977_b())) {
/*      */           float selfDamage;
/*  357 */           if (isRightThread() ? (((Boolean)this.superSafe.getValue()).booleanValue() ? (DamageUtil.canTakeDamage(((Boolean)this.suicide.getValue()).booleanValue()) && ((selfDamage = DamageUtil.calculateDamage(pos, (Entity)mc.field_71439_g)) - 0.5D > EntityUtil.getHealth((Entity)mc.field_71439_g) || selfDamage > ((Float)this.maxSelfBreak.getValue()).floatValue())) : ((Boolean)this.superSafe.getValue()).booleanValue()) : ((Boolean)this.superSafe.getValue()).booleanValue()) {
/*      */             return;
/*      */           }
/*  360 */           attackCrystalPredict(packet2.func_149001_c(), pos);
/*  361 */         } else if (((Boolean)this.predictCalc.getValue()).booleanValue() && isRightThread()) {
/*  362 */           float selfDamage = -1.0F;
/*  363 */           if (DamageUtil.canTakeDamage(((Boolean)this.suicide.getValue()).booleanValue())) {
/*  364 */             selfDamage = DamageUtil.calculateDamage(pos, (Entity)mc.field_71439_g);
/*      */           }
/*  366 */           if (selfDamage + 0.5D < EntityUtil.getHealth((Entity)mc.field_71439_g) && selfDamage <= ((Float)this.maxSelfBreak.getValue()).floatValue()) {
/*  367 */             for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/*      */               float damage;
/*  369 */               if (player.func_174818_b(pos) > MathUtil.square(((Float)this.range.getValue()).floatValue()) || !EntityUtil.isValid((Entity)player, (((Float)this.range.getValue()).floatValue() + ((Float)this.breakRange.getValue()).floatValue())) || (((Boolean)this.antiNaked.getValue()).booleanValue() && DamageUtil.isNaked(player)) || ((damage = DamageUtil.calculateDamage(pos, (Entity)player)) <= selfDamage && (damage <= ((Float)this.minDamage.getValue()).floatValue() || DamageUtil.canTakeDamage(((Boolean)this.suicide.getValue()).booleanValue())) && damage <= EntityUtil.getHealth((Entity)player)))
/*      */                 continue; 
/*  371 */               if (((Boolean)this.predictRotate.getValue()).booleanValue() && ((Integer)this.eventMode.getValue()).intValue() != 2 && (this.rotate.getValue() == Rotate.BREAK || this.rotate.getValue() == Rotate.ALL)) {
/*  372 */                 rotateToPos(pos);
/*      */               }
/*  374 */               attackCrystalPredict(packet2.func_149001_c(), pos);
/*      */             }
/*      */           
/*      */           }
/*      */         } 
/*      */       } 
/*  380 */     } else if (!((Boolean)this.soundConfirm.getValue()).booleanValue() && event.getPacket() instanceof SPacketExplosion) {
/*  381 */       SPacketExplosion packet3 = (SPacketExplosion)event.getPacket();
/*  382 */       BlockPos pos = (new BlockPos(packet3.func_149148_f(), packet3.func_149143_g(), packet3.func_149145_h())).func_177977_b();
/*  383 */       removePos(pos);
/*  384 */     } else if (event.getPacket() instanceof SPacketDestroyEntities) {
/*  385 */       SPacketDestroyEntities packet4 = (SPacketDestroyEntities)event.getPacket();
/*  386 */       for (int id : packet4.func_149098_c()) {
/*  387 */         Entity entity = mc.field_71441_e.func_73045_a(id);
/*  388 */         if (entity instanceof EntityEnderCrystal)
/*  389 */         { brokenPos.remove((new BlockPos(entity.func_174791_d())).func_177977_b());
/*  390 */           placedPos.remove((new BlockPos(entity.func_174791_d())).func_177977_b()); } 
/*      */       } 
/*  392 */     } else if (event.getPacket() instanceof SPacketEntityStatus) {
/*  393 */       SPacketEntityStatus packet5 = (SPacketEntityStatus)event.getPacket();
/*  394 */       if (packet5.func_149160_c() == 35 && packet5.func_149161_a((World)mc.field_71441_e) instanceof EntityPlayer)
/*  395 */         this.totemPops.put((EntityPlayer)packet5.func_149161_a((World)mc.field_71441_e), (new Timer()).reset()); 
/*      */     } else {
/*  397 */       SPacketSoundEffect packet; if (event.getPacket() instanceof SPacketSoundEffect && (packet = (SPacketSoundEffect)event.getPacket()).func_186977_b() == SoundCategory.BLOCKS && packet.func_186978_a() == SoundEvents.field_187539_bB) {
/*  398 */         BlockPos pos = new BlockPos(packet.func_149207_d(), packet.func_149211_e(), packet.func_149210_f());
/*  399 */         if (((Boolean)this.sound.getValue()).booleanValue() || this.threadMode.getValue() == ThreadMode.SOUND) {
/*  400 */           NoSoundLag.removeEntities(packet, ((Float)this.soundRange.getValue()).floatValue());
/*      */         }
/*  402 */         if (((Boolean)this.soundConfirm.getValue()).booleanValue()) {
/*  403 */           removePos(pos);
/*      */         }
/*  405 */         if (this.threadMode.getValue() == ThreadMode.SOUND && isRightThread() && mc.field_71439_g != null && mc.field_71439_g.func_174818_b(pos) < MathUtil.square(((Float)this.soundPlayer.getValue()).floatValue()))
/*  406 */           handlePool(true); 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean predictSlowBreak(BlockPos pos) {
/*  412 */     if (((Boolean)this.antiCommit.getValue()).booleanValue() && lowDmgPos.remove(pos)) {
/*  413 */       return shouldSlowBreak(false);
/*      */     }
/*  415 */     return false;
/*      */   }
/*      */   
/*      */   private boolean isRightThread() {
/*  419 */     return (mc.func_152345_ab() || (!Phobos.eventManager.ticksOngoing() && !this.threadOngoing.get()));
/*      */   }
/*      */   
/*      */   private void attackCrystalPredict(int entityID, BlockPos pos) {
/*  423 */     if (((Boolean)this.predictRotate.getValue()).booleanValue() && (((Integer)this.eventMode.getValue()).intValue() != 2 || this.threadMode.getValue() != ThreadMode.NONE) && (this.rotate.getValue() == Rotate.BREAK || this.rotate.getValue() == Rotate.ALL)) {
/*  424 */       rotateToPos(pos);
/*      */     }
/*  426 */     CPacketUseEntity attackPacket = new CPacketUseEntity();
/*  427 */     attackPacket.field_149567_a = entityID;
/*  428 */     attackPacket.field_149566_b = CPacketUseEntity.Action.ATTACK;
/*  429 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)attackPacket);
/*  430 */     if (((Boolean)this.breakSwing.getValue()).booleanValue()) {
/*  431 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
/*      */     }
/*  433 */     if (((Boolean)this.resetBreakTimer.getValue()).booleanValue()) {
/*  434 */       this.breakTimer.reset();
/*      */     }
/*  436 */     this.predictTimer.reset();
/*      */   }
/*      */   
/*      */   private void removePos(BlockPos pos) {
/*  440 */     if (this.damageSync.getValue() == DamageSync.PLACE) {
/*  441 */       if (placedPos.remove(pos)) {
/*  442 */         this.posConfirmed = true;
/*      */       }
/*  444 */     } else if (this.damageSync.getValue() == DamageSync.BREAK && brokenPos.remove(pos)) {
/*  445 */       this.posConfirmed = true;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void onRender3D(Render3DEvent event) {
/*  451 */     if ((this.offHand || this.mainHand || this.switchMode.getValue() == Switch.CALC) && this.renderPos != null && ((Boolean)this.render.getValue()).booleanValue() && (((Boolean)this.box.getValue()).booleanValue() || ((Boolean)this.text.getValue()).booleanValue() || ((Boolean)this.outline.getValue()).booleanValue())) {
/*  452 */       RenderUtil.drawBoxESP(this.renderPos, ((Boolean)this.colorSync.getValue()).booleanValue() ? Colors.INSTANCE.getCurrentColor() : new Color(((Integer)this.red.getValue()).intValue(), ((Integer)this.green.getValue()).intValue(), ((Integer)this.blue.getValue()).intValue(), ((Integer)this.alpha.getValue()).intValue()), ((Boolean)this.customOutline.getValue()).booleanValue(), ((Boolean)this.colorSync.getValue()).booleanValue() ? Colors.INSTANCE.getCurrentColor() : new Color(((Integer)this.cRed.getValue()).intValue(), ((Integer)this.cGreen.getValue()).intValue(), ((Integer)this.cBlue.getValue()).intValue(), ((Integer)this.cAlpha.getValue()).intValue()), ((Float)this.lineWidth.getValue()).floatValue(), ((Boolean)this.outline.getValue()).booleanValue(), ((Boolean)this.box.getValue()).booleanValue(), ((Integer)this.boxAlpha.getValue()).intValue(), false);
/*  453 */       if (((Boolean)this.text.getValue()).booleanValue()) {
/*  454 */         RenderUtil.drawText(this.renderPos, ((Math.floor(this.renderDamage) == this.renderDamage) ? (String)Integer.valueOf((int)this.renderDamage) : String.format("%.1f", new Object[] { Double.valueOf(this.renderDamage) })) + "");
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   @SubscribeEvent
/*      */   public void onKeyInput(InputEvent.KeyInputEvent event) {
/*  461 */     if (Keyboard.getEventKeyState() && !(mc.field_71462_r instanceof me.earth.phobos.features.gui.PhobosGui) && ((Bind)this.switchBind.getValue()).getKey() == Keyboard.getEventKey()) {
/*  462 */       if (((Boolean)this.switchBack.getValue()).booleanValue() && ((Boolean)this.offhandSwitch.getValue()).booleanValue() && this.offHand) {
/*  463 */         Offhand module = (Offhand)Phobos.moduleManager.getModuleByClass(Offhand.class);
/*  464 */         if (module.isOff()) {
/*  465 */           Command.sendMessage("<" + getDisplayName() + "> §cSwitch failed. Enable the Offhand module.");
/*  466 */         } else if (module.type.getValue() == Offhand.Type.NEW) {
/*  467 */           module.setSwapToTotem(true);
/*  468 */           module.doOffhand();
/*      */         } else {
/*  470 */           module.setMode(Offhand.Mode2.TOTEMS);
/*  471 */           module.doSwitch();
/*      */         } 
/*      */         return;
/*      */       } 
/*  475 */       this.switching = !this.switching;
/*      */     } 
/*      */   }
/*      */   
/*      */   @SubscribeEvent
/*      */   public void onSettingChange(ClientEvent event) {
/*  481 */     if (event.getStage() == 2 && event.getSetting() != null && event.getSetting().getFeature() != null && event.getSetting().getFeature().equals(this) && isEnabled() && (event.getSetting().equals(this.threadDelay) || event.getSetting().equals(this.threadMode))) {
/*  482 */       if (this.executor != null) {
/*  483 */         this.executor.shutdown();
/*      */       }
/*  485 */       if (this.thread != null) {
/*  486 */         this.shouldInterrupt.set(true);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private void postProcessing() {
/*  492 */     if (this.threadMode.getValue() != ThreadMode.NONE || ((Integer)this.eventMode.getValue()).intValue() != 2 || this.rotate.getValue() == Rotate.OFF || !((Boolean)this.rotateFirst.getValue()).booleanValue()) {
/*      */       return;
/*      */     }
/*  495 */     switch ((Logic)this.logic.getValue()) {
/*      */       case OFF:
/*  497 */         postProcessBreak();
/*  498 */         postProcessPlace();
/*      */         break;
/*      */       
/*      */       case PLACE:
/*  502 */         postProcessPlace();
/*  503 */         postProcessBreak();
/*      */         break;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void postProcessBreak() {
/*  509 */     while (!this.packetUseEntities.isEmpty()) {
/*  510 */       CPacketUseEntity packet = this.packetUseEntities.poll();
/*  511 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)packet);
/*  512 */       if (((Boolean)this.breakSwing.getValue()).booleanValue()) {
/*  513 */         mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/*      */       }
/*  515 */       this.breakTimer.reset();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void postProcessPlace() {
/*  520 */     if (this.placeInfo != null) {
/*  521 */       this.placeInfo.runPlace();
/*  522 */       this.placeTimer.reset();
/*  523 */       this.placeInfo = null;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void processMultiThreading() {
/*  528 */     if (isOff()) {
/*      */       return;
/*      */     }
/*  531 */     if (this.threadMode.getValue() == ThreadMode.WHILE) {
/*  532 */       handleWhile();
/*  533 */     } else if (this.threadMode.getValue() != ThreadMode.NONE) {
/*  534 */       handlePool(false);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void handlePool(boolean justDoIt) {
/*  539 */     if (justDoIt || this.executor == null || this.executor.isTerminated() || this.executor.isShutdown() || (this.syncroTimer.passedMs(((Integer)this.syncThreads.getValue()).intValue()) && ((Boolean)this.syncThreadBool.getValue()).booleanValue())) {
/*  540 */       if (this.executor != null) {
/*  541 */         this.executor.shutdown();
/*      */       }
/*  543 */       this.executor = getExecutor();
/*  544 */       this.syncroTimer.reset();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void handleWhile() {
/*  549 */     if (this.thread == null || this.thread.isInterrupted() || !this.thread.isAlive() || (this.syncroTimer.passedMs(((Integer)this.syncThreads.getValue()).intValue()) && ((Boolean)this.syncThreadBool.getValue()).booleanValue())) {
/*  550 */       if (this.thread == null) {
/*  551 */         this.thread = new Thread(RAutoCrystal.getInstance(this));
/*  552 */       } else if (this.syncroTimer.passedMs(((Integer)this.syncThreads.getValue()).intValue()) && !this.shouldInterrupt.get() && ((Boolean)this.syncThreadBool.getValue()).booleanValue()) {
/*  553 */         this.shouldInterrupt.set(true);
/*  554 */         this.syncroTimer.reset();
/*      */         return;
/*      */       } 
/*  557 */       if (this.thread != null && (this.thread.isInterrupted() || !this.thread.isAlive())) {
/*  558 */         this.thread = new Thread(RAutoCrystal.getInstance(this));
/*      */       }
/*  560 */       if (this.thread != null && this.thread.getState() == Thread.State.NEW) {
/*      */         try {
/*  562 */           this.thread.start();
/*  563 */         } catch (Exception e) {
/*  564 */           e.printStackTrace();
/*      */         } 
/*  566 */         this.syncroTimer.reset();
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private ScheduledExecutorService getExecutor() {
/*  572 */     ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
/*  573 */     service.scheduleAtFixedRate(RAutoCrystal.getInstance(this), 0L, ((Integer)this.threadDelay.getValue()).intValue(), TimeUnit.MILLISECONDS);
/*  574 */     return service;
/*      */   }
/*      */   
/*      */   public void doAutoCrystal() {
/*  578 */     if (((Boolean)this.brownZombie.getValue()).booleanValue()) {
/*      */       return;
/*      */     }
/*  581 */     if (check()) {
/*  582 */       switch ((Logic)this.logic.getValue()) {
/*      */         case PLACE:
/*  584 */           placeCrystal();
/*  585 */           breakCrystal();
/*      */           break;
/*      */         
/*      */         case OFF:
/*  589 */           breakCrystal();
/*  590 */           placeCrystal();
/*      */           break;
/*      */       } 
/*      */       
/*  594 */       manualBreaker();
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean check() {
/*  599 */     if (fullNullCheck()) {
/*  600 */       return false;
/*      */     }
/*  602 */     if (this.syncTimer.passedMs(((Integer)this.damageSyncTime.getValue()).intValue())) {
/*  603 */       this.currentSyncTarget = null;
/*  604 */       this.syncedCrystalPos = null;
/*  605 */       this.syncedPlayerPos = null;
/*  606 */     } else if (((Boolean)this.syncySync.getValue()).booleanValue() && this.syncedCrystalPos != null) {
/*  607 */       this.posConfirmed = true;
/*      */     } 
/*  609 */     this.foundDoublePop = false;
/*  610 */     if (this.renderTimer.passedMs(500L)) {
/*  611 */       this.renderPos = null;
/*  612 */       this.renderTimer.reset();
/*      */     } 
/*  614 */     this.mainHand = (mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP);
/*  615 */     this.offHand = (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP);
/*  616 */     this.currentDamage = 0.0D;
/*  617 */     this.placePos = null;
/*  618 */     if (this.lastSlot != mc.field_71439_g.field_71071_by.field_70461_c || AutoTrap.isPlacing || Surround.isPlacing) {
/*  619 */       this.lastSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*  620 */       this.switchTimer.reset();
/*      */     } 
/*  622 */     if (!this.offHand && !this.mainHand) {
/*  623 */       this.placeInfo = null;
/*  624 */       this.packetUseEntities.clear();
/*      */     } 
/*  626 */     if (this.offHand || this.mainHand) {
/*  627 */       this.switching = false;
/*      */     }
/*  629 */     if ((!this.offHand && !this.mainHand && this.switchMode.getValue() == Switch.BREAKSLOT && !this.switching) || !DamageUtil.canBreakWeakness((EntityPlayer)mc.field_71439_g) || !this.switchTimer.passedMs(((Integer)this.switchCooldown.getValue()).intValue())) {
/*  630 */       this.renderPos = null;
/*  631 */       target = null;
/*  632 */       this.rotating = false;
/*  633 */       return false;
/*      */     } 
/*  635 */     if (((Boolean)this.mineSwitch.getValue()).booleanValue() && Mouse.isButtonDown(0) && (this.switching || this.autoSwitch.getValue() == AutoSwitch.ALWAYS) && Mouse.isButtonDown(1) && mc.field_71439_g.func_184614_ca().func_77973_b() instanceof net.minecraft.item.ItemPickaxe) {
/*  636 */       switchItem();
/*      */     }
/*  638 */     mapCrystals();
/*  639 */     if (!this.posConfirmed && this.damageSync.getValue() != DamageSync.NONE && this.syncTimer.passedMs(((Integer)this.confirm.getValue()).intValue())) {
/*  640 */       this.syncTimer.setMs((((Integer)this.damageSyncTime.getValue()).intValue() + 1));
/*      */     }
/*  642 */     return true;
/*      */   }
/*      */   
/*      */   private void mapCrystals() {
/*  646 */     this.efficientTarget = null;
/*  647 */     if (((Integer)this.packets.getValue()).intValue() != 1) {
/*  648 */       this.attackList = new ConcurrentLinkedQueue<>();
/*  649 */       this.crystalMap = new HashMap<>();
/*      */     } 
/*  651 */     this.crystalCount = 0;
/*  652 */     this.minDmgCount = 0;
/*  653 */     Entity maxCrystal = null;
/*  654 */     float maxDamage = 0.5F;
/*  655 */     for (Entity entity : mc.field_71441_e.field_72996_f) {
/*  656 */       if (entity.field_70128_L || !(entity instanceof EntityEnderCrystal) || !isValid(entity))
/*  657 */         continue;  if (((Boolean)this.syncedFeetPlace.getValue()).booleanValue() && entity.func_180425_c().func_177977_b().equals(this.syncedCrystalPos) && this.damageSync.getValue() != DamageSync.NONE) {
/*  658 */         this.minDmgCount++;
/*  659 */         this.crystalCount++;
/*  660 */         if (((Boolean)this.syncCount.getValue()).booleanValue()) {
/*  661 */           this.minDmgCount = ((Integer)this.wasteAmount.getValue()).intValue() + 1;
/*  662 */           this.crystalCount = ((Integer)this.wasteAmount.getValue()).intValue() + 1;
/*      */         } 
/*  664 */         if (!((Boolean)this.hyperSync.getValue()).booleanValue())
/*  665 */           continue;  maxCrystal = null;
/*      */         break;
/*      */       } 
/*  668 */       boolean count = false;
/*  669 */       boolean countMin = false;
/*  670 */       float selfDamage = -1.0F;
/*  671 */       if (DamageUtil.canTakeDamage(((Boolean)this.suicide.getValue()).booleanValue())) {
/*  672 */         selfDamage = DamageUtil.calculateDamage(entity, (Entity)mc.field_71439_g);
/*      */       }
/*  674 */       if (selfDamage + 0.5D < EntityUtil.getHealth((Entity)mc.field_71439_g) && selfDamage <= ((Float)this.maxSelfBreak.getValue()).floatValue()) {
/*  675 */         Entity beforeCrystal = maxCrystal;
/*  676 */         float beforeDamage = maxDamage;
/*  677 */         for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/*      */           
/*  679 */           if (player.func_70068_e(entity) > MathUtil.square(((Float)this.range.getValue()).floatValue()))
/*      */             continue; 
/*  681 */           if (EntityUtil.isValid((Entity)player, (((Float)this.range.getValue()).floatValue() + ((Float)this.breakRange.getValue()).floatValue()))) {
/*  682 */             float f; if ((((Boolean)this.antiNaked.getValue()).booleanValue() && DamageUtil.isNaked(player)) || ((f = DamageUtil.calculateDamage(entity, (Entity)player)) <= selfDamage && (f <= ((Float)this.minDamage.getValue()).floatValue() || DamageUtil.canTakeDamage(((Boolean)this.suicide.getValue()).booleanValue())) && f <= EntityUtil.getHealth((Entity)player)))
/*      */               continue; 
/*  684 */             if (f > maxDamage) {
/*  685 */               maxDamage = f;
/*  686 */               maxCrystal = entity;
/*      */             } 
/*  688 */             if (((Integer)this.packets.getValue()).intValue() == 1) {
/*  689 */               if (f >= ((Float)this.minDamage.getValue()).floatValue() || !((Boolean)this.wasteMinDmgCount.getValue()).booleanValue()) {
/*  690 */                 count = true;
/*      */               }
/*  692 */               countMin = true;
/*      */               continue;
/*      */             } 
/*  695 */             if (this.crystalMap.get(entity) != null && ((Float)this.crystalMap.get(entity)).floatValue() >= f)
/*      */               continue; 
/*  697 */             this.crystalMap.put(entity, Float.valueOf(f)); continue;
/*      */           } 
/*      */           float damage;
/*  700 */           if ((this.antiFriendPop.getValue() != AntiFriendPop.BREAK && this.antiFriendPop.getValue() != AntiFriendPop.ALL) || !Phobos.friendManager.isFriend(player.func_70005_c_()) || (damage = DamageUtil.calculateDamage(entity, (Entity)player)) <= EntityUtil.getHealth((Entity)player) + 0.5D)
/*      */             continue; 
/*  702 */           maxCrystal = beforeCrystal;
/*  703 */           maxDamage = beforeDamage;
/*  704 */           this.crystalMap.remove(entity);
/*  705 */           if (!((Boolean)this.noCount.getValue()).booleanValue())
/*  706 */             break;  count = false;
/*  707 */           countMin = false;
/*      */         } 
/*      */       } 
/*      */       
/*  711 */       if (!countMin)
/*  712 */         continue;  this.minDmgCount++;
/*  713 */       if (!count)
/*  714 */         continue;  this.crystalCount++;
/*      */     } 
/*  716 */     if (this.damageSync.getValue() == DamageSync.BREAK && (maxDamage > this.lastDamage || this.syncTimer.passedMs(((Integer)this.damageSyncTime.getValue()).intValue()) || this.damageSync.getValue() == DamageSync.NONE)) {
/*  717 */       this.lastDamage = maxDamage;
/*      */     }
/*  719 */     if (((Boolean)this.enormousSync.getValue()).booleanValue() && ((Boolean)this.syncedFeetPlace.getValue()).booleanValue() && this.damageSync.getValue() != DamageSync.NONE && this.syncedCrystalPos != null) {
/*  720 */       if (((Boolean)this.syncCount.getValue()).booleanValue()) {
/*  721 */         this.minDmgCount = ((Integer)this.wasteAmount.getValue()).intValue() + 1;
/*  722 */         this.crystalCount = ((Integer)this.wasteAmount.getValue()).intValue() + 1;
/*      */       } 
/*      */       return;
/*      */     } 
/*  726 */     if (((Boolean)this.webAttack.getValue()).booleanValue() && this.webPos != null) {
/*  727 */       if (mc.field_71439_g.func_174818_b(this.webPos.func_177984_a()) > MathUtil.square(((Float)this.breakRange.getValue()).floatValue())) {
/*  728 */         this.webPos = null;
/*      */       } else {
/*  730 */         for (Entity entity : mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(this.webPos.func_177984_a()))) {
/*  731 */           if (!(entity instanceof EntityEnderCrystal))
/*  732 */             continue;  this.attackList.add(entity);
/*  733 */           this.efficientTarget = entity;
/*  734 */           this.webPos = null;
/*  735 */           this.lastDamage = 0.5D;
/*      */           return;
/*      */         } 
/*      */       } 
/*      */     }
/*  740 */     if (shouldSlowBreak(true) && maxDamage < ((Float)this.minDamage.getValue()).floatValue() && (target == null || EntityUtil.getHealth((Entity)target) > ((Float)this.facePlace.getValue()).floatValue() || (!this.breakTimer.passedMs(((Integer)this.facePlaceSpeed.getValue()).intValue()) && ((Boolean)this.slowFaceBreak.getValue()).booleanValue() && Mouse.isButtonDown(0) && ((Boolean)this.holdFacePlace.getValue()).booleanValue() && ((Boolean)this.holdFaceBreak.getValue()).booleanValue()))) {
/*  741 */       this.efficientTarget = null;
/*      */       return;
/*      */     } 
/*  744 */     if (((Integer)this.packets.getValue()).intValue() == 1) {
/*  745 */       this.efficientTarget = maxCrystal;
/*      */     } else {
/*  747 */       this.crystalMap = MathUtil.sortByValue(this.crystalMap, true);
/*  748 */       for (Map.Entry<Entity, Float> entry : this.crystalMap.entrySet()) {
/*  749 */         Entity crystal = (Entity)entry.getKey();
/*  750 */         float damage = ((Float)entry.getValue()).floatValue();
/*  751 */         if (damage >= ((Float)this.minDamage.getValue()).floatValue() || !((Boolean)this.wasteMinDmgCount.getValue()).booleanValue()) {
/*  752 */           this.crystalCount++;
/*      */         }
/*  754 */         this.attackList.add(crystal);
/*  755 */         this.minDmgCount++;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean shouldSlowBreak(boolean withManual) {
/*  761 */     return ((withManual && ((Boolean)this.manual.getValue()).booleanValue() && ((Boolean)this.manualMinDmg.getValue()).booleanValue() && Mouse.isButtonDown(1) && (!Mouse.isButtonDown(0) || !((Boolean)this.holdFacePlace.getValue()).booleanValue())) || (((Boolean)this.holdFacePlace.getValue()).booleanValue() && ((Boolean)this.holdFaceBreak.getValue()).booleanValue() && Mouse.isButtonDown(0) && !this.breakTimer.passedMs(((Integer)this.facePlaceSpeed.getValue()).intValue())) || (((Boolean)this.slowFaceBreak.getValue()).booleanValue() && !this.breakTimer.passedMs(((Integer)this.facePlaceSpeed.getValue()).intValue())));
/*      */   }
/*      */   
/*      */   private void placeCrystal() {
/*  765 */     int crystalLimit = ((Integer)this.wasteAmount.getValue()).intValue();
/*  766 */     if (this.placeTimer.passedMs(((Integer)this.placeDelay.getValue()).intValue()) && ((Boolean)this.place.getValue()).booleanValue() && (this.offHand || this.mainHand || this.switchMode.getValue() == Switch.CALC || (this.switchMode.getValue() == Switch.BREAKSLOT && this.switching))) {
/*  767 */       if ((this.offHand || this.mainHand || (this.switchMode.getValue() != Switch.ALWAYS && !this.switching)) && this.crystalCount >= crystalLimit && (!((Boolean)this.antiSurround.getValue()).booleanValue() || this.lastPos == null || !this.lastPos.equals(this.placePos))) {
/*      */         return;
/*      */       }
/*  770 */       calculateDamage(getTarget((this.targetMode.getValue() == Target.UNSAFE)));
/*  771 */       if (target != null && this.placePos != null) {
/*  772 */         if (!this.offHand && !this.mainHand && this.autoSwitch.getValue() != AutoSwitch.NONE && (this.currentDamage > ((Float)this.minDamage.getValue()).floatValue() || (((Boolean)this.lethalSwitch.getValue()).booleanValue() && EntityUtil.getHealth((Entity)target) <= ((Float)this.facePlace.getValue()).floatValue())) && !switchItem()) {
/*      */           return;
/*      */         }
/*  775 */         if (this.currentDamage < ((Float)this.minDamage.getValue()).floatValue() && ((Boolean)this.limitFacePlace.getValue()).booleanValue()) {
/*  776 */           crystalLimit = 1;
/*      */         }
/*  778 */         if (this.currentDamage >= ((Float)this.minMinDmg.getValue()).floatValue() && (this.offHand || this.mainHand || this.autoSwitch.getValue() != AutoSwitch.NONE) && (this.crystalCount < crystalLimit || (((Boolean)this.antiSurround.getValue()).booleanValue() && this.lastPos != null && this.lastPos.equals(this.placePos))) && (this.currentDamage > ((Float)this.minDamage.getValue()).floatValue() || this.minDmgCount < crystalLimit) && this.currentDamage >= 1.0D && (DamageUtil.isArmorLow(target, ((Integer)this.minArmor.getValue()).intValue()) || EntityUtil.getHealth((Entity)target) <= ((Float)this.facePlace.getValue()).floatValue() || this.currentDamage > ((Float)this.minDamage.getValue()).floatValue() || shouldHoldFacePlace())) {
/*  779 */           float damageOffset = (this.damageSync.getValue() == DamageSync.BREAK) ? (((Float)this.dropOff.getValue()).floatValue() - 5.0F) : 0.0F;
/*  780 */           boolean syncflag = false;
/*  781 */           if (((Boolean)this.syncedFeetPlace.getValue()).booleanValue() && this.placePos.equals(this.lastPos) && isEligableForFeetSync(target, this.placePos) && !this.syncTimer.passedMs(((Integer)this.damageSyncTime.getValue()).intValue()) && target.equals(this.currentSyncTarget) && target.func_180425_c().equals(this.syncedPlayerPos) && this.damageSync.getValue() != DamageSync.NONE) {
/*  782 */             this.syncedCrystalPos = this.placePos;
/*  783 */             this.lastDamage = this.currentDamage;
/*  784 */             if (((Boolean)this.fullSync.getValue()).booleanValue()) {
/*  785 */               this.lastDamage = 100.0D;
/*      */             }
/*  787 */             syncflag = true;
/*      */           } 
/*  789 */           if (syncflag || this.currentDamage - damageOffset > this.lastDamage || this.syncTimer.passedMs(((Integer)this.damageSyncTime.getValue()).intValue()) || this.damageSync.getValue() == DamageSync.NONE) {
/*  790 */             if (!syncflag && this.damageSync.getValue() != DamageSync.BREAK) {
/*  791 */               this.lastDamage = this.currentDamage;
/*      */             }
/*  793 */             this.renderPos = this.placePos;
/*  794 */             this.renderDamage = this.currentDamage;
/*  795 */             if (switchItem()) {
/*  796 */               this.currentSyncTarget = target;
/*  797 */               this.syncedPlayerPos = target.func_180425_c();
/*  798 */               if (this.foundDoublePop) {
/*  799 */                 this.totemPops.put(target, (new Timer()).reset());
/*      */               }
/*  801 */               rotateToPos(this.placePos);
/*  802 */               if (this.addTolowDmg || (((Boolean)this.actualSlowBreak.getValue()).booleanValue() && this.currentDamage < ((Float)this.minDamage.getValue()).floatValue())) {
/*  803 */                 lowDmgPos.add(this.placePos);
/*      */               }
/*  805 */               placedPos.add(this.placePos);
/*  806 */               if (!((Boolean)this.justRender.getValue()).booleanValue()) {
/*  807 */                 if (((Integer)this.eventMode.getValue()).intValue() == 2 && this.threadMode.getValue() == ThreadMode.NONE && ((Boolean)this.rotateFirst.getValue()).booleanValue() && this.rotate.getValue() != Rotate.OFF) {
/*  808 */                   this.placeInfo = new PlaceInfo(this.placePos, this.offHand, ((Boolean)this.placeSwing.getValue()).booleanValue(), ((Boolean)this.exactHand.getValue()).booleanValue());
/*      */                 } else {
/*  810 */                   BlockUtil.placeCrystalOnBlock(this.placePos, this.offHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, ((Boolean)this.placeSwing.getValue()).booleanValue(), ((Boolean)this.exactHand.getValue()).booleanValue());
/*      */                 } 
/*      */               }
/*  813 */               this.lastPos = this.placePos;
/*  814 */               this.placeTimer.reset();
/*  815 */               this.posConfirmed = false;
/*  816 */               if (this.syncTimer.passedMs(((Integer)this.damageSyncTime.getValue()).intValue())) {
/*  817 */                 this.syncedCrystalPos = null;
/*  818 */                 this.syncTimer.reset();
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } else {
/*  824 */         this.renderPos = null;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean shouldHoldFacePlace() {
/*  830 */     this.addTolowDmg = false;
/*  831 */     if (((Boolean)this.holdFacePlace.getValue()).booleanValue() && Mouse.isButtonDown(0)) {
/*  832 */       this.addTolowDmg = true;
/*  833 */       return true;
/*      */     } 
/*  835 */     return false;
/*      */   }
/*      */   
/*      */   private boolean switchItem() {
/*  839 */     if (this.offHand || this.mainHand) {
/*  840 */       return true;
/*      */     }
/*  842 */     switch ((AutoSwitch)this.autoSwitch.getValue()) {
/*      */       case OFF:
/*  844 */         return false;
/*      */       
/*      */       case PLACE:
/*  847 */         if (!this.switching) {
/*  848 */           return false;
/*      */         }
/*      */       
/*      */       case BREAK:
/*  852 */         if (!doSwitch())
/*  853 */           break;  return true;
/*      */     } 
/*      */     
/*  856 */     return false;
/*      */   }
/*      */   
/*      */   private boolean doSwitch() {
/*  860 */     if (((Boolean)this.offhandSwitch.getValue()).booleanValue()) {
/*  861 */       Offhand module = (Offhand)Phobos.moduleManager.getModuleByClass(Offhand.class);
/*  862 */       if (module.isOff()) {
/*  863 */         Command.sendMessage("<" + getDisplayName() + "> §cSwitch failed. Enable the Offhand module.");
/*  864 */         this.switching = false;
/*  865 */         return false;
/*      */       } 
/*  867 */       if (module.type.getValue() == Offhand.Type.NEW) {
/*  868 */         module.setSwapToTotem(false);
/*  869 */         module.setMode(Offhand.Mode.CRYSTALS);
/*  870 */         module.doOffhand();
/*      */       } else {
/*  872 */         module.setMode(Offhand.Mode2.CRYSTALS);
/*  873 */         module.doSwitch();
/*      */       } 
/*  875 */       this.switching = false;
/*  876 */       return true;
/*      */     } 
/*  878 */     if (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP) {
/*  879 */       this.mainHand = false;
/*      */     } else {
/*  881 */       InventoryUtil.switchToHotbarSlot(ItemEndCrystal.class, false);
/*  882 */       this.mainHand = true;
/*      */     } 
/*  884 */     this.switching = false;
/*  885 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void calculateDamage(EntityPlayer targettedPlayer) {
/*  891 */     if (targettedPlayer == null && this.targetMode.getValue() != Target.DAMAGE && !((Boolean)this.fullCalc.getValue()).booleanValue()) {
/*      */       return;
/*      */     }
/*  894 */     float maxDamage = 0.5F;
/*  895 */     EntityPlayer currentTarget = null;
/*  896 */     BlockPos currentPos = null;
/*  897 */     float maxSelfDamage = 0.0F;
/*  898 */     this.foundDoublePop = false;
/*  899 */     BlockPos setToAir = null;
/*  900 */     IBlockState state = null; BlockPos playerPos; Block web;
/*  901 */     if (((Boolean)this.webAttack.getValue()).booleanValue() && targettedPlayer != null && (web = mc.field_71441_e.func_180495_p(playerPos = new BlockPos(targettedPlayer.func_174791_d())).func_177230_c()) == Blocks.field_150321_G) {
/*  902 */       setToAir = playerPos;
/*  903 */       state = mc.field_71441_e.func_180495_p(playerPos);
/*  904 */       mc.field_71441_e.func_175698_g(playerPos);
/*      */     } 
/*      */     
/*  907 */     for (BlockPos pos : BlockUtil.possiblePlacePositions(((Float)this.placeRange.getValue()).floatValue(), ((Boolean)this.antiSurround.getValue()).booleanValue(), ((Boolean)this.oneDot15.getValue()).booleanValue())) {
/*  908 */       if (!BlockUtil.rayTracePlaceCheck(pos, ((this.raytrace.getValue() == Raytrace.PLACE || this.raytrace.getValue() == Raytrace.FULL) && mc.field_71439_g.func_174818_b(pos) > MathUtil.square(((Float)this.placetrace.getValue()).floatValue())), 1.0F))
/*      */         continue; 
/*  910 */       float selfDamage = -1.0F;
/*  911 */       if (DamageUtil.canTakeDamage(((Boolean)this.suicide.getValue()).booleanValue())) {
/*  912 */         selfDamage = DamageUtil.calculateDamage(pos, (Entity)mc.field_71439_g);
/*      */       }
/*  914 */       if (selfDamage + 0.5D >= EntityUtil.getHealth((Entity)mc.field_71439_g) || selfDamage > ((Float)this.maxSelfPlace.getValue()).floatValue())
/*      */         continue; 
/*  916 */       if (targettedPlayer != null) {
/*  917 */         float playerDamage = DamageUtil.calculateDamage(pos, (Entity)targettedPlayer);
/*  918 */         if (((Boolean)this.calcEvenIfNoDamage.getValue()).booleanValue() && (this.antiFriendPop.getValue() == AntiFriendPop.ALL || this.antiFriendPop.getValue() == AntiFriendPop.PLACE)) {
/*  919 */           boolean friendPop = false;
/*  920 */           for (EntityPlayer friend : mc.field_71441_e.field_73010_i) {
/*      */             float friendDamage;
/*  922 */             if (friend == null || mc.field_71439_g.equals(friend) || friend.func_174818_b(pos) > MathUtil.square(((Float)this.range.getValue()).floatValue() + ((Float)this.placeRange.getValue()).floatValue()) || !Phobos.friendManager.isFriend(friend) || (friendDamage = DamageUtil.calculateDamage(pos, (Entity)friend)) <= EntityUtil.getHealth((Entity)friend) + 0.5D)
/*      */               continue; 
/*  924 */             friendPop = true;
/*      */           } 
/*      */           
/*  927 */           if (friendPop)
/*      */             continue; 
/*  929 */         }  if (isDoublePoppable(targettedPlayer, playerDamage) && (currentPos == null || targettedPlayer.func_174818_b(pos) < targettedPlayer.func_174818_b(currentPos))) {
/*  930 */           currentTarget = targettedPlayer;
/*  931 */           maxDamage = playerDamage;
/*  932 */           currentPos = pos;
/*  933 */           this.foundDoublePop = true;
/*      */           continue;
/*      */         } 
/*  936 */         if (this.foundDoublePop || (playerDamage <= maxDamage && (!((Boolean)this.extraSelfCalc.getValue()).booleanValue() || playerDamage < maxDamage || selfDamage >= maxSelfDamage)) || (playerDamage <= selfDamage && (playerDamage <= ((Float)this.minDamage.getValue()).floatValue() || DamageUtil.canTakeDamage(((Boolean)this.suicide.getValue()).booleanValue())) && playerDamage <= EntityUtil.getHealth((Entity)targettedPlayer)))
/*      */           continue; 
/*  938 */         maxDamage = playerDamage;
/*  939 */         currentTarget = targettedPlayer;
/*  940 */         currentPos = pos;
/*  941 */         maxSelfDamage = selfDamage;
/*      */         continue;
/*      */       } 
/*  944 */       float maxDamageBefore = maxDamage;
/*  945 */       EntityPlayer currentTargetBefore = currentTarget;
/*  946 */       BlockPos currentPosBefore = currentPos;
/*  947 */       float maxSelfDamageBefore = maxSelfDamage;
/*  948 */       for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/*      */         
/*  950 */         if (EntityUtil.isValid((Entity)player, (((Float)this.placeRange.getValue()).floatValue() + ((Float)this.range.getValue()).floatValue()))) {
/*  951 */           if (((Boolean)this.antiNaked.getValue()).booleanValue() && DamageUtil.isNaked(player))
/*  952 */             continue;  float playerDamage = DamageUtil.calculateDamage(pos, (Entity)player);
/*  953 */           if (((Boolean)this.doublePopOnDamage.getValue()).booleanValue() && isDoublePoppable(player, playerDamage) && (currentPos == null || player.func_174818_b(pos) < player.func_174818_b(currentPos))) {
/*  954 */             currentTarget = player;
/*  955 */             maxDamage = playerDamage;
/*  956 */             currentPos = pos;
/*  957 */             maxSelfDamage = selfDamage;
/*  958 */             this.foundDoublePop = true;
/*  959 */             if (this.antiFriendPop.getValue() != AntiFriendPop.BREAK && this.antiFriendPop.getValue() != AntiFriendPop.PLACE)
/*      */               continue; 
/*      */             break;
/*      */           } 
/*  963 */           if (this.foundDoublePop || (playerDamage <= maxDamage && (!((Boolean)this.extraSelfCalc.getValue()).booleanValue() || playerDamage < maxDamage || selfDamage >= maxSelfDamage)) || (playerDamage <= selfDamage && (playerDamage <= ((Float)this.minDamage.getValue()).floatValue() || DamageUtil.canTakeDamage(((Boolean)this.suicide.getValue()).booleanValue())) && playerDamage <= EntityUtil.getHealth((Entity)player)))
/*      */             continue; 
/*  965 */           maxDamage = playerDamage;
/*  966 */           currentTarget = player;
/*  967 */           currentPos = pos;
/*  968 */           maxSelfDamage = selfDamage; continue;
/*      */         } 
/*      */         float friendDamage;
/*  971 */         if ((this.antiFriendPop.getValue() != AntiFriendPop.ALL && this.antiFriendPop.getValue() != AntiFriendPop.PLACE) || player == null || player.func_174818_b(pos) > MathUtil.square(((Float)this.range.getValue()).floatValue() + ((Float)this.placeRange.getValue()).floatValue()) || !Phobos.friendManager.isFriend(player) || (friendDamage = DamageUtil.calculateDamage(pos, (Entity)player)) <= EntityUtil.getHealth((Entity)player) + 0.5D)
/*      */           continue; 
/*  973 */         maxDamage = maxDamageBefore;
/*  974 */         currentTarget = currentTargetBefore;
/*  975 */         currentPos = currentPosBefore;
/*  976 */         maxSelfDamage = maxSelfDamageBefore;
/*      */       } 
/*      */     } 
/*      */     
/*  980 */     if (setToAir != null) {
/*  981 */       mc.field_71441_e.func_175656_a(setToAir, state);
/*  982 */       this.webPos = currentPos;
/*      */     } 
/*  984 */     target = currentTarget;
/*  985 */     this.currentDamage = maxDamage;
/*  986 */     this.placePos = currentPos;
/*      */   }
/*      */   private EntityPlayer getTarget(boolean unsafe) {
/*      */     EntityOtherPlayerMP entityOtherPlayerMP;
/*  990 */     if (this.targetMode.getValue() == Target.DAMAGE) {
/*  991 */       return null;
/*      */     }
/*  993 */     EntityPlayer currentTarget = null;
/*  994 */     for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/*  995 */       if (EntityUtil.isntValid((Entity)player, (((Float)this.placeRange.getValue()).floatValue() + ((Float)this.range.getValue()).floatValue())) || (((Boolean)this.antiNaked.getValue()).booleanValue() && DamageUtil.isNaked(player)) || (unsafe && EntityUtil.isSafe((Entity)player)))
/*      */         continue; 
/*  997 */       if (((Integer)this.minArmor.getValue()).intValue() > 0 && DamageUtil.isArmorLow(player, ((Integer)this.minArmor.getValue()).intValue())) {
/*  998 */         currentTarget = player;
/*      */         break;
/*      */       } 
/* 1001 */       if (currentTarget == null) {
/* 1002 */         currentTarget = player;
/*      */         continue;
/*      */       } 
/* 1005 */       if (mc.field_71439_g.func_70068_e((Entity)player) >= mc.field_71439_g.func_70068_e((Entity)currentTarget))
/*      */         continue; 
/* 1007 */       currentTarget = player;
/*      */     } 
/* 1009 */     if (unsafe && currentTarget == null) {
/* 1010 */       return getTarget(false);
/*      */     }
/* 1012 */     if (((Boolean)this.predictPos.getValue()).booleanValue() && currentTarget != null) {
/* 1013 */       GameProfile profile = new GameProfile((currentTarget.func_110124_au() == null) ? UUID.fromString("8af022c8-b926-41a0-8b79-2b544ff00fcf") : currentTarget.func_110124_au(), currentTarget.func_70005_c_());
/* 1014 */       EntityOtherPlayerMP newTarget = new EntityOtherPlayerMP((World)mc.field_71441_e, profile);
/* 1015 */       Vec3d extrapolatePosition = MathUtil.extrapolatePlayerPosition(currentTarget, ((Integer)this.predictTicks.getValue()).intValue());
/* 1016 */       newTarget.func_82149_j((Entity)currentTarget);
/* 1017 */       newTarget.field_70165_t = extrapolatePosition.field_72450_a;
/* 1018 */       newTarget.field_70163_u = extrapolatePosition.field_72448_b;
/* 1019 */       newTarget.field_70161_v = extrapolatePosition.field_72449_c;
/* 1020 */       newTarget.func_70606_j(EntityUtil.getHealth((Entity)currentTarget));
/* 1021 */       newTarget.field_71071_by.func_70455_b(currentTarget.field_71071_by);
/* 1022 */       entityOtherPlayerMP = newTarget;
/*      */     } 
/* 1024 */     return (EntityPlayer)entityOtherPlayerMP;
/*      */   }
/*      */   
/*      */   private void breakCrystal() {
/* 1028 */     if (((Boolean)this.explode.getValue()).booleanValue() && this.breakTimer.passedMs(((Integer)this.breakDelay.getValue()).intValue()) && (this.switchMode.getValue() == Switch.ALWAYS || this.mainHand || this.offHand)) {
/* 1029 */       if (((Integer)this.packets.getValue()).intValue() == 1 && this.efficientTarget != null) {
/* 1030 */         if (((Boolean)this.justRender.getValue()).booleanValue()) {
/* 1031 */           doFakeSwing();
/*      */           return;
/*      */         } 
/* 1034 */         if (((Boolean)this.syncedFeetPlace.getValue()).booleanValue() && ((Boolean)this.gigaSync.getValue()).booleanValue() && this.syncedCrystalPos != null && this.damageSync.getValue() != DamageSync.NONE) {
/*      */           return;
/*      */         }
/* 1037 */         rotateTo(this.efficientTarget);
/* 1038 */         attackEntity(this.efficientTarget);
/* 1039 */         this.breakTimer.reset();
/* 1040 */       } else if (!this.attackList.isEmpty()) {
/* 1041 */         if (((Boolean)this.justRender.getValue()).booleanValue()) {
/* 1042 */           doFakeSwing();
/*      */           return;
/*      */         } 
/* 1045 */         if (((Boolean)this.syncedFeetPlace.getValue()).booleanValue() && ((Boolean)this.gigaSync.getValue()).booleanValue() && this.syncedCrystalPos != null && this.damageSync.getValue() != DamageSync.NONE) {
/*      */           return;
/*      */         }
/* 1048 */         for (int i = 0; i < ((Integer)this.packets.getValue()).intValue(); i++) {
/* 1049 */           Entity entity = this.attackList.poll();
/* 1050 */           if (entity != null) {
/* 1051 */             rotateTo(entity);
/* 1052 */             attackEntity(entity);
/*      */           } 
/* 1054 */         }  this.breakTimer.reset();
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private void attackEntity(Entity entity) {
/* 1060 */     if (entity != null) {
/* 1061 */       if (((Integer)this.eventMode.getValue()).intValue() == 2 && this.threadMode.getValue() == ThreadMode.NONE && ((Boolean)this.rotateFirst.getValue()).booleanValue() && this.rotate.getValue() != Rotate.OFF) {
/* 1062 */         this.packetUseEntities.add(new CPacketUseEntity(entity));
/*      */       } else {
/* 1064 */         EntityUtil.attackEntity(entity, ((Boolean)this.sync.getValue()).booleanValue(), ((Boolean)this.breakSwing.getValue()).booleanValue());
/* 1065 */         brokenPos.add((new BlockPos(entity.func_174791_d())).func_177977_b());
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private void doFakeSwing() {
/* 1071 */     if (((Boolean)this.fakeSwing.getValue()).booleanValue()) {
/* 1072 */       EntityUtil.swingArmNoPacket(EnumHand.MAIN_HAND, (EntityLivingBase)mc.field_71439_g);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void manualBreaker() {
/* 1078 */     if (this.rotate.getValue() != Rotate.OFF && ((Integer)this.eventMode.getValue()).intValue() != 2 && this.rotating)
/* 1079 */       if (this.didRotation) {
/* 1080 */         mc.field_71439_g.field_70125_A = (float)(mc.field_71439_g.field_70125_A + 4.0E-4D);
/* 1081 */         this.didRotation = false;
/*      */       } else {
/* 1083 */         mc.field_71439_g.field_70125_A = (float)(mc.field_71439_g.field_70125_A - 4.0E-4D);
/* 1084 */         this.didRotation = true;
/*      */       }  
/*      */     RayTraceResult result;
/* 1087 */     if ((this.offHand || this.mainHand) && ((Boolean)this.manual.getValue()).booleanValue() && this.manualTimer.passedMs(((Integer)this.manualBreak.getValue()).intValue()) && Mouse.isButtonDown(1) && mc.field_71439_g.func_184592_cb().func_77973_b() != Items.field_151153_ao && mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() != Items.field_151153_ao && mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() != Items.field_151031_f && mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() != Items.field_151062_by && (result = mc.field_71476_x) != null) {
/* 1088 */       Entity entity; BlockPos mousePos; switch (result.field_72313_a) {
/*      */         case OFF:
/* 1090 */           entity = result.field_72308_g;
/* 1091 */           if (!(entity instanceof EntityEnderCrystal))
/* 1092 */             break;  EntityUtil.attackEntity(entity, ((Boolean)this.sync.getValue()).booleanValue(), ((Boolean)this.breakSwing.getValue()).booleanValue());
/* 1093 */           this.manualTimer.reset();
/*      */           break;
/*      */         
/*      */         case PLACE:
/* 1097 */           mousePos = mc.field_71476_x.func_178782_a().func_177984_a();
/* 1098 */           for (Entity target : mc.field_71441_e.func_72839_b(null, new AxisAlignedBB(mousePos))) {
/* 1099 */             if (!(target instanceof EntityEnderCrystal))
/* 1100 */               continue;  EntityUtil.attackEntity(target, ((Boolean)this.sync.getValue()).booleanValue(), ((Boolean)this.breakSwing.getValue()).booleanValue());
/* 1101 */             this.manualTimer.reset();
/*      */           } 
/*      */           break;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void rotateTo(Entity entity) {
/*      */     float[] angle;
/* 1110 */     switch ((Rotate)this.rotate.getValue()) {
/*      */       case OFF:
/* 1112 */         this.rotating = false;
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case BREAK:
/*      */       case ALL:
/* 1119 */         angle = MathUtil.calcAngle(mc.field_71439_g.func_174824_e(mc.func_184121_ak()), entity.func_174791_d());
/* 1120 */         if (((Integer)this.eventMode.getValue()).intValue() == 2 && this.threadMode.getValue() == ThreadMode.NONE) {
/* 1121 */           Phobos.rotationManager.setPlayerRotations(angle[0], angle[1]);
/*      */           break;
/*      */         } 
/* 1124 */         this.yaw = angle[0];
/* 1125 */         this.pitch = angle[1];
/* 1126 */         this.rotating = true;
/*      */         break;
/*      */     } 
/*      */   }
/*      */   private void rotateToPos(BlockPos pos) {
/*      */     float[] angle;
/* 1132 */     switch ((Rotate)this.rotate.getValue()) {
/*      */       case OFF:
/* 1134 */         this.rotating = false;
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case PLACE:
/*      */       case ALL:
/* 1141 */         angle = MathUtil.calcAngle(mc.field_71439_g.func_174824_e(mc.func_184121_ak()), new Vec3d((pos.func_177958_n() + 0.5F), (pos.func_177956_o() - 0.5F), (pos.func_177952_p() + 0.5F)));
/* 1142 */         if (((Integer)this.eventMode.getValue()).intValue() == 2 && this.threadMode.getValue() == ThreadMode.NONE) {
/* 1143 */           Phobos.rotationManager.setPlayerRotations(angle[0], angle[1]);
/*      */           break;
/*      */         } 
/* 1146 */         this.yaw = angle[0];
/* 1147 */         this.pitch = angle[1];
/* 1148 */         this.rotating = true;
/*      */         break;
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean isDoublePoppable(EntityPlayer player, float damage) {
/*      */     float health;
/* 1155 */     if (((Boolean)this.doublePop.getValue()).booleanValue() && (health = EntityUtil.getHealth((Entity)player)) <= ((Double)this.popHealth.getValue()).doubleValue() && damage > health + 0.5D && damage <= ((Float)this.popDamage.getValue()).floatValue()) {
/* 1156 */       Timer timer = this.totemPops.get(player);
/* 1157 */       return (timer == null || timer.passedMs(((Integer)this.popTime.getValue()).intValue()));
/*      */     } 
/* 1159 */     return false;
/*      */   }
/*      */   
/*      */   private boolean isValid(Entity entity) {
/* 1163 */     return (entity != null && mc.field_71439_g.func_70068_e(entity) <= MathUtil.square(((Float)this.breakRange.getValue()).floatValue()) && (this.raytrace.getValue() == Raytrace.NONE || this.raytrace.getValue() == Raytrace.PLACE || mc.field_71439_g.func_70685_l(entity) || (!mc.field_71439_g.func_70685_l(entity) && mc.field_71439_g.func_70068_e(entity) <= MathUtil.square(((Float)this.breaktrace.getValue()).floatValue()))));
/*      */   }
/*      */   
/*      */   private boolean isEligableForFeetSync(EntityPlayer player, BlockPos pos) {
/* 1167 */     if (((Boolean)this.holySync.getValue()).booleanValue()) {
/* 1168 */       BlockPos playerPos = new BlockPos(player.func_174791_d()); EnumFacing[] arrayOfEnumFacing; int i; byte b;
/* 1169 */       for (arrayOfEnumFacing = EnumFacing.values(), i = arrayOfEnumFacing.length, b = 0; b < i; ) { EnumFacing facing = arrayOfEnumFacing[b];
/*      */         BlockPos holyPos;
/* 1171 */         if (facing == EnumFacing.DOWN || facing == EnumFacing.UP || !pos.equals(holyPos = playerPos.func_177977_b().func_177972_a(facing))) {
/*      */           b++; continue;
/* 1173 */         }  return true; }
/*      */       
/* 1175 */       return false;
/*      */     } 
/* 1177 */     return true;
/*      */   }
/*      */   
/*      */   public enum PredictTimer {
/* 1181 */     NONE,
/* 1182 */     BREAK,
/* 1183 */     PREDICT;
/*      */   }
/*      */   
/*      */   public enum AntiFriendPop
/*      */   {
/* 1188 */     NONE,
/* 1189 */     PLACE,
/* 1190 */     BREAK,
/* 1191 */     ALL;
/*      */   }
/*      */   
/*      */   public enum ThreadMode
/*      */   {
/* 1196 */     NONE,
/* 1197 */     POOL,
/* 1198 */     SOUND,
/* 1199 */     WHILE;
/*      */   }
/*      */   
/*      */   public enum AutoSwitch
/*      */   {
/* 1204 */     NONE,
/* 1205 */     TOGGLE,
/* 1206 */     ALWAYS;
/*      */   }
/*      */   
/*      */   public enum Raytrace
/*      */   {
/* 1211 */     NONE,
/* 1212 */     PLACE,
/* 1213 */     BREAK,
/* 1214 */     FULL;
/*      */   }
/*      */   
/*      */   public enum Switch
/*      */   {
/* 1219 */     ALWAYS,
/* 1220 */     BREAKSLOT,
/* 1221 */     CALC;
/*      */   }
/*      */   
/*      */   public enum Logic
/*      */   {
/* 1226 */     BREAKPLACE,
/* 1227 */     PLACEBREAK;
/*      */   }
/*      */   
/*      */   public enum Target
/*      */   {
/* 1232 */     CLOSEST,
/* 1233 */     UNSAFE,
/* 1234 */     DAMAGE;
/*      */   }
/*      */   
/*      */   public enum Rotate
/*      */   {
/* 1239 */     OFF,
/* 1240 */     PLACE,
/* 1241 */     BREAK,
/* 1242 */     ALL;
/*      */   }
/*      */   
/*      */   public enum DamageSync
/*      */   {
/* 1247 */     NONE,
/* 1248 */     PLACE,
/* 1249 */     BREAK;
/*      */   }
/*      */   
/*      */   public enum Settings
/*      */   {
/* 1254 */     PLACE,
/* 1255 */     BREAK,
/* 1256 */     RENDER,
/* 1257 */     MISC,
/* 1258 */     DEV;
/*      */   }
/*      */   
/*      */   public static class PlaceInfo
/*      */   {
/*      */     private final BlockPos pos;
/*      */     private final boolean offhand;
/*      */     private final boolean placeSwing;
/*      */     private final boolean exactHand;
/*      */     
/*      */     public PlaceInfo(BlockPos pos, boolean offhand, boolean placeSwing, boolean exactHand) {
/* 1269 */       this.pos = pos;
/* 1270 */       this.offhand = offhand;
/* 1271 */       this.placeSwing = placeSwing;
/* 1272 */       this.exactHand = exactHand;
/*      */     }
/*      */     
/*      */     public void runPlace() {
/* 1276 */       BlockUtil.placeCrystalOnBlock(this.pos, this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, this.placeSwing, this.exactHand);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class RAutoCrystal
/*      */     implements Runnable
/*      */   {
/*      */     private static RAutoCrystal instance;
/*      */     
/*      */     private AutoCrystal autoCrystal;
/*      */     
/*      */     public static RAutoCrystal getInstance(AutoCrystal autoCrystal) {
/* 1289 */       if (instance == null) {
/* 1290 */         instance = new RAutoCrystal();
/* 1291 */         instance.autoCrystal = autoCrystal;
/*      */       } 
/* 1293 */       return instance;
/*      */     }
/*      */ 
/*      */     
/*      */     public void run() {
/* 1298 */       if (this.autoCrystal.threadMode.getValue() == AutoCrystal.ThreadMode.WHILE) {
/* 1299 */         while (this.autoCrystal.isOn() && this.autoCrystal.threadMode.getValue() == AutoCrystal.ThreadMode.WHILE) {
/* 1300 */           while (Phobos.eventManager.ticksOngoing());
/*      */           
/* 1302 */           if (this.autoCrystal.shouldInterrupt.get()) {
/* 1303 */             this.autoCrystal.shouldInterrupt.set(false);
/* 1304 */             this.autoCrystal.syncroTimer.reset();
/* 1305 */             this.autoCrystal.thread.interrupt();
/*      */             break;
/*      */           } 
/* 1308 */           this.autoCrystal.threadOngoing.set(true);
/* 1309 */           Phobos.safetyManager.doSafetyCheck();
/* 1310 */           this.autoCrystal.doAutoCrystal();
/* 1311 */           this.autoCrystal.threadOngoing.set(false);
/*      */           try {
/* 1313 */             Thread.sleep(((Integer)this.autoCrystal.threadDelay.getValue()).intValue());
/* 1314 */           } catch (InterruptedException e) {
/* 1315 */             this.autoCrystal.thread.interrupt();
/* 1316 */             e.printStackTrace();
/*      */           } 
/*      */         } 
/* 1319 */       } else if (this.autoCrystal.threadMode.getValue() != AutoCrystal.ThreadMode.NONE && this.autoCrystal.isOn()) {
/* 1320 */         while (Phobos.eventManager.ticksOngoing());
/*      */         
/* 1322 */         this.autoCrystal.threadOngoing.set(true);
/* 1323 */         Phobos.safetyManager.doSafetyCheck();
/* 1324 */         this.autoCrystal.doAutoCrystal();
/* 1325 */         this.autoCrystal.threadOngoing.set(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\combat\AutoCrystal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */