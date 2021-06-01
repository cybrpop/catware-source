/*    */ package me.earth.phobos.features.modules.movement;
/*    */ 
/*    */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraft.util.math.AxisAlignedBB;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TPSpeed
/*    */   extends Module
/*    */ {
/* 18 */   private final Setting<Mode> mode = register(new Setting("Mode", Mode.NORMAL));
/* 19 */   private final Setting<Double> speed = register(new Setting("Speed", Double.valueOf(0.25D), Double.valueOf(0.1D), Double.valueOf(10.0D)));
/* 20 */   private final Setting<Double> fallSpeed = register(new Setting("FallSpeed", Double.valueOf(0.25D), Double.valueOf(0.1D), Double.valueOf(10.0D), v -> (this.mode.getValue() == Mode.STEP)));
/* 21 */   private final Setting<Boolean> turnOff = register(new Setting("Off", Boolean.valueOf(false)));
/* 22 */   private final Setting<Integer> tpLimit = register(new Setting("Limit", Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(10), v -> ((Boolean)this.turnOff.getValue()).booleanValue(), "Turn it off."));
/* 23 */   private int tps = 0;
/* 24 */   private final double[] selectedPositions = new double[] { 0.42D, 0.75D, 1.0D };
/*    */   
/*    */   public TPSpeed() {
/* 27 */     super("TpSpeed", "Teleports you.", Module.Category.MOVEMENT, true, false, false);
/*    */   }
/*    */   
/*    */   private static boolean collidesHorizontally(AxisAlignedBB bb) {
/* 31 */     if (mc.field_71441_e.func_184143_b(bb)) {
/* 32 */       Vec3d center = bb.func_189972_c();
/* 33 */       BlockPos blockpos = new BlockPos(center.field_72450_a, bb.field_72338_b, center.field_72449_c);
/* 34 */       return (mc.field_71441_e.func_175665_u(blockpos.func_177976_e()) || mc.field_71441_e.func_175665_u(blockpos.func_177974_f()) || mc.field_71441_e.func_175665_u(blockpos.func_177978_c()) || mc.field_71441_e.func_175665_u(blockpos.func_177968_d()) || mc.field_71441_e.func_175665_u(blockpos));
/*    */     } 
/* 36 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 41 */     this.tps = 0;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onUpdatePlayerWalking(UpdateWalkingPlayerEvent event) {
/*    */     // Byte code:
/*    */     //   0: aload_1
/*    */     //   1: invokevirtual getStage : ()I
/*    */     //   4: ifeq -> 8
/*    */     //   7: return
/*    */     //   8: aload_0
/*    */     //   9: getfield mode : Lme/earth/phobos/features/setting/Setting;
/*    */     //   12: invokevirtual getValue : ()Ljava/lang/Object;
/*    */     //   15: getstatic me/earth/phobos/features/modules/movement/TPSpeed$Mode.NORMAL : Lme/earth/phobos/features/modules/movement/TPSpeed$Mode;
/*    */     //   18: if_acmpne -> 284
/*    */     //   21: aload_0
/*    */     //   22: getfield turnOff : Lme/earth/phobos/features/setting/Setting;
/*    */     //   25: invokevirtual getValue : ()Ljava/lang/Object;
/*    */     //   28: checkcast java/lang/Boolean
/*    */     //   31: invokevirtual booleanValue : ()Z
/*    */     //   34: ifeq -> 62
/*    */     //   37: aload_0
/*    */     //   38: getfield tps : I
/*    */     //   41: aload_0
/*    */     //   42: getfield tpLimit : Lme/earth/phobos/features/setting/Setting;
/*    */     //   45: invokevirtual getValue : ()Ljava/lang/Object;
/*    */     //   48: checkcast java/lang/Integer
/*    */     //   51: invokevirtual intValue : ()I
/*    */     //   54: if_icmplt -> 62
/*    */     //   57: aload_0
/*    */     //   58: invokevirtual disable : ()V
/*    */     //   61: return
/*    */     //   62: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   65: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   68: getfield field_191988_bg : F
/*    */     //   71: fconst_0
/*    */     //   72: fcmpl
/*    */     //   73: ifne -> 102
/*    */     //   76: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   79: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   82: getfield field_70702_br : F
/*    */     //   85: fconst_0
/*    */     //   86: fcmpl
/*    */     //   87: ifeq -> 818
/*    */     //   90: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   93: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   96: getfield field_70122_E : Z
/*    */     //   99: ifeq -> 818
/*    */     //   102: ldc2_w 0.0625
/*    */     //   105: dstore_2
/*    */     //   106: dload_2
/*    */     //   107: aload_0
/*    */     //   108: getfield speed : Lme/earth/phobos/features/setting/Setting;
/*    */     //   111: invokevirtual getValue : ()Ljava/lang/Object;
/*    */     //   114: checkcast java/lang/Double
/*    */     //   117: invokevirtual doubleValue : ()D
/*    */     //   120: dcmpg
/*    */     //   121: ifge -> 204
/*    */     //   124: dload_2
/*    */     //   125: invokestatic directionSpeed : (D)[D
/*    */     //   128: astore #4
/*    */     //   130: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   133: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   136: getfield field_71174_a : Lnet/minecraft/client/network/NetHandlerPlayClient;
/*    */     //   139: new net/minecraft/network/play/client/CPacketPlayer$Position
/*    */     //   142: dup
/*    */     //   143: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   146: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   149: getfield field_70165_t : D
/*    */     //   152: aload #4
/*    */     //   154: iconst_0
/*    */     //   155: daload
/*    */     //   156: dadd
/*    */     //   157: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   160: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   163: getfield field_70163_u : D
/*    */     //   166: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   169: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   172: getfield field_70161_v : D
/*    */     //   175: aload #4
/*    */     //   177: iconst_1
/*    */     //   178: daload
/*    */     //   179: dadd
/*    */     //   180: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   183: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   186: getfield field_70122_E : Z
/*    */     //   189: invokespecial <init> : (DDDZ)V
/*    */     //   192: invokevirtual func_147297_a : (Lnet/minecraft/network/Packet;)V
/*    */     //   195: dload_2
/*    */     //   196: ldc2_w 0.262
/*    */     //   199: dadd
/*    */     //   200: dstore_2
/*    */     //   201: goto -> 106
/*    */     //   204: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   207: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   210: getfield field_71174_a : Lnet/minecraft/client/network/NetHandlerPlayClient;
/*    */     //   213: new net/minecraft/network/play/client/CPacketPlayer$Position
/*    */     //   216: dup
/*    */     //   217: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   220: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   223: getfield field_70165_t : D
/*    */     //   226: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   229: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   232: getfield field_70159_w : D
/*    */     //   235: dadd
/*    */     //   236: dconst_0
/*    */     //   237: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   240: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   243: getfield field_70161_v : D
/*    */     //   246: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   249: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   252: getfield field_70179_y : D
/*    */     //   255: dadd
/*    */     //   256: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   259: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   262: getfield field_70122_E : Z
/*    */     //   265: invokespecial <init> : (DDDZ)V
/*    */     //   268: invokevirtual func_147297_a : (Lnet/minecraft/network/Packet;)V
/*    */     //   271: aload_0
/*    */     //   272: dup
/*    */     //   273: getfield tps : I
/*    */     //   276: iconst_1
/*    */     //   277: iadd
/*    */     //   278: putfield tps : I
/*    */     //   281: goto -> 818
/*    */     //   284: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   287: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   290: getfield field_191988_bg : F
/*    */     //   293: fconst_0
/*    */     //   294: fcmpl
/*    */     //   295: ifne -> 312
/*    */     //   298: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   301: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   304: getfield field_70702_br : F
/*    */     //   307: fconst_0
/*    */     //   308: fcmpl
/*    */     //   309: ifeq -> 818
/*    */     //   312: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   315: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   318: getfield field_70122_E : Z
/*    */     //   321: ifeq -> 818
/*    */     //   324: dconst_0
/*    */     //   325: dstore_2
/*    */     //   326: ldc2_w 0.262
/*    */     //   329: invokestatic directionSpeed : (D)[D
/*    */     //   332: astore #4
/*    */     //   334: ldc2_w 0.0625
/*    */     //   337: dstore #5
/*    */     //   339: dload #5
/*    */     //   341: aload_0
/*    */     //   342: getfield speed : Lme/earth/phobos/features/setting/Setting;
/*    */     //   345: invokevirtual getValue : ()Ljava/lang/Object;
/*    */     //   348: checkcast java/lang/Double
/*    */     //   351: invokevirtual doubleValue : ()D
/*    */     //   354: dcmpg
/*    */     //   355: ifge -> 751
/*    */     //   358: dload #5
/*    */     //   360: invokestatic directionSpeed : (D)[D
/*    */     //   363: astore #7
/*    */     //   365: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   368: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   371: invokevirtual func_174813_aQ : ()Lnet/minecraft/util/math/AxisAlignedBB;
/*    */     //   374: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*    */     //   377: checkcast net/minecraft/util/math/AxisAlignedBB
/*    */     //   380: aload #7
/*    */     //   382: iconst_0
/*    */     //   383: daload
/*    */     //   384: dload_2
/*    */     //   385: aload #7
/*    */     //   387: iconst_1
/*    */     //   388: daload
/*    */     //   389: invokevirtual func_72317_d : (DDD)Lnet/minecraft/util/math/AxisAlignedBB;
/*    */     //   392: astore #8
/*    */     //   394: aload #8
/*    */     //   396: invokestatic collidesHorizontally : (Lnet/minecraft/util/math/AxisAlignedBB;)Z
/*    */     //   399: ifeq -> 544
/*    */     //   402: aload_0
/*    */     //   403: getfield selectedPositions : [D
/*    */     //   406: astore #9
/*    */     //   408: aload #9
/*    */     //   410: arraylength
/*    */     //   411: istore #10
/*    */     //   413: iconst_0
/*    */     //   414: istore #11
/*    */     //   416: iload #11
/*    */     //   418: iload #10
/*    */     //   420: if_icmpge -> 508
/*    */     //   423: aload #9
/*    */     //   425: iload #11
/*    */     //   427: daload
/*    */     //   428: dstore #12
/*    */     //   430: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   433: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   436: getfield field_71174_a : Lnet/minecraft/client/network/NetHandlerPlayClient;
/*    */     //   439: new net/minecraft/network/play/client/CPacketPlayer$Position
/*    */     //   442: dup
/*    */     //   443: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   446: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   449: getfield field_70165_t : D
/*    */     //   452: aload #7
/*    */     //   454: iconst_0
/*    */     //   455: daload
/*    */     //   456: dadd
/*    */     //   457: aload #4
/*    */     //   459: iconst_0
/*    */     //   460: daload
/*    */     //   461: dsub
/*    */     //   462: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   465: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   468: getfield field_70163_u : D
/*    */     //   471: dload_2
/*    */     //   472: dadd
/*    */     //   473: dload #12
/*    */     //   475: dadd
/*    */     //   476: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   479: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   482: getfield field_70161_v : D
/*    */     //   485: aload #7
/*    */     //   487: iconst_1
/*    */     //   488: daload
/*    */     //   489: dadd
/*    */     //   490: aload #4
/*    */     //   492: iconst_1
/*    */     //   493: daload
/*    */     //   494: dsub
/*    */     //   495: iconst_1
/*    */     //   496: invokespecial <init> : (DDDZ)V
/*    */     //   499: invokevirtual func_147297_a : (Lnet/minecraft/network/Packet;)V
/*    */     //   502: iinc #11, 1
/*    */     //   505: goto -> 416
/*    */     //   508: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   511: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   514: invokevirtual func_174813_aQ : ()Lnet/minecraft/util/math/AxisAlignedBB;
/*    */     //   517: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*    */     //   520: checkcast net/minecraft/util/math/AxisAlignedBB
/*    */     //   523: aload #7
/*    */     //   525: iconst_0
/*    */     //   526: daload
/*    */     //   527: dload_2
/*    */     //   528: dconst_1
/*    */     //   529: dadd
/*    */     //   530: dup2
/*    */     //   531: dstore_2
/*    */     //   532: aload #7
/*    */     //   534: iconst_1
/*    */     //   535: daload
/*    */     //   536: invokevirtual func_72317_d : (DDD)Lnet/minecraft/util/math/AxisAlignedBB;
/*    */     //   539: astore #8
/*    */     //   541: goto -> 394
/*    */     //   544: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   547: getfield field_71441_e : Lnet/minecraft/client/multiplayer/WorldClient;
/*    */     //   550: aload #8
/*    */     //   552: ldc2_w 0.0125
/*    */     //   555: dconst_0
/*    */     //   556: ldc2_w 0.0125
/*    */     //   559: invokevirtual func_72314_b : (DDD)Lnet/minecraft/util/math/AxisAlignedBB;
/*    */     //   562: dconst_0
/*    */     //   563: ldc2_w -1.0
/*    */     //   566: dconst_0
/*    */     //   567: invokevirtual func_72317_d : (DDD)Lnet/minecraft/util/math/AxisAlignedBB;
/*    */     //   570: invokevirtual func_72829_c : (Lnet/minecraft/util/math/AxisAlignedBB;)Z
/*    */     //   573: ifne -> 673
/*    */     //   576: dconst_0
/*    */     //   577: dstore #9
/*    */     //   579: dload #9
/*    */     //   581: dconst_1
/*    */     //   582: dcmpg
/*    */     //   583: ifgt -> 669
/*    */     //   586: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   589: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   592: getfield field_71174_a : Lnet/minecraft/client/network/NetHandlerPlayClient;
/*    */     //   595: new net/minecraft/network/play/client/CPacketPlayer$Position
/*    */     //   598: dup
/*    */     //   599: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   602: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   605: getfield field_70165_t : D
/*    */     //   608: aload #7
/*    */     //   610: iconst_0
/*    */     //   611: daload
/*    */     //   612: dadd
/*    */     //   613: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   616: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   619: getfield field_70163_u : D
/*    */     //   622: dload_2
/*    */     //   623: dadd
/*    */     //   624: dload #9
/*    */     //   626: dsub
/*    */     //   627: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   630: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   633: getfield field_70161_v : D
/*    */     //   636: aload #7
/*    */     //   638: iconst_1
/*    */     //   639: daload
/*    */     //   640: dadd
/*    */     //   641: iconst_1
/*    */     //   642: invokespecial <init> : (DDDZ)V
/*    */     //   645: invokevirtual func_147297_a : (Lnet/minecraft/network/Packet;)V
/*    */     //   648: dload #9
/*    */     //   650: aload_0
/*    */     //   651: getfield fallSpeed : Lme/earth/phobos/features/setting/Setting;
/*    */     //   654: invokevirtual getValue : ()Ljava/lang/Object;
/*    */     //   657: checkcast java/lang/Double
/*    */     //   660: invokevirtual doubleValue : ()D
/*    */     //   663: dadd
/*    */     //   664: dstore #9
/*    */     //   666: goto -> 579
/*    */     //   669: dload_2
/*    */     //   670: dconst_1
/*    */     //   671: dsub
/*    */     //   672: dstore_2
/*    */     //   673: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   676: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   679: getfield field_71174_a : Lnet/minecraft/client/network/NetHandlerPlayClient;
/*    */     //   682: new net/minecraft/network/play/client/CPacketPlayer$Position
/*    */     //   685: dup
/*    */     //   686: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   689: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   692: getfield field_70165_t : D
/*    */     //   695: aload #7
/*    */     //   697: iconst_0
/*    */     //   698: daload
/*    */     //   699: dadd
/*    */     //   700: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   703: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   706: getfield field_70163_u : D
/*    */     //   709: dload_2
/*    */     //   710: dadd
/*    */     //   711: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   714: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   717: getfield field_70161_v : D
/*    */     //   720: aload #7
/*    */     //   722: iconst_1
/*    */     //   723: daload
/*    */     //   724: dadd
/*    */     //   725: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   728: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   731: getfield field_70122_E : Z
/*    */     //   734: invokespecial <init> : (DDDZ)V
/*    */     //   737: invokevirtual func_147297_a : (Lnet/minecraft/network/Packet;)V
/*    */     //   740: dload #5
/*    */     //   742: ldc2_w 0.262
/*    */     //   745: dadd
/*    */     //   746: dstore #5
/*    */     //   748: goto -> 339
/*    */     //   751: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   754: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   757: getfield field_71174_a : Lnet/minecraft/client/network/NetHandlerPlayClient;
/*    */     //   760: new net/minecraft/network/play/client/CPacketPlayer$Position
/*    */     //   763: dup
/*    */     //   764: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   767: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   770: getfield field_70165_t : D
/*    */     //   773: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   776: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   779: getfield field_70159_w : D
/*    */     //   782: dadd
/*    */     //   783: dconst_0
/*    */     //   784: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   787: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   790: getfield field_70161_v : D
/*    */     //   793: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   796: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   799: getfield field_70179_y : D
/*    */     //   802: dadd
/*    */     //   803: getstatic me/earth/phobos/features/modules/movement/TPSpeed.mc : Lnet/minecraft/client/Minecraft;
/*    */     //   806: getfield field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
/*    */     //   809: getfield field_70122_E : Z
/*    */     //   812: invokespecial <init> : (DDDZ)V
/*    */     //   815: invokevirtual func_147297_a : (Lnet/minecraft/network/Packet;)V
/*    */     //   818: return
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #46	-> 0
/*    */     //   #47	-> 7
/*    */     //   #49	-> 8
/*    */     //   #50	-> 21
/*    */     //   #51	-> 57
/*    */     //   #52	-> 61
/*    */     //   #54	-> 62
/*    */     //   #55	-> 102
/*    */     //   #56	-> 124
/*    */     //   #57	-> 130
/*    */     //   #55	-> 195
/*    */     //   #59	-> 204
/*    */     //   #60	-> 271
/*    */     //   #62	-> 284
/*    */     //   #63	-> 324
/*    */     //   #64	-> 326
/*    */     //   #65	-> 334
/*    */     //   #66	-> 358
/*    */     //   #67	-> 365
/*    */     //   #68	-> 394
/*    */     //   #69	-> 402
/*    */     //   #70	-> 430
/*    */     //   #69	-> 502
/*    */     //   #72	-> 508
/*    */     //   #74	-> 544
/*    */     //   #75	-> 576
/*    */     //   #76	-> 586
/*    */     //   #75	-> 648
/*    */     //   #78	-> 669
/*    */     //   #80	-> 673
/*    */     //   #65	-> 740
/*    */     //   #82	-> 751
/*    */     //   #84	-> 818
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   130	65	4	dir	[D
/*    */     //   106	98	2	x	D
/*    */     //   430	72	12	position	D
/*    */     //   579	90	9	i	D
/*    */     //   365	375	7	dir	[D
/*    */     //   394	346	8	bb	Lnet/minecraft/util/math/AxisAlignedBB;
/*    */     //   339	412	5	x	D
/*    */     //   326	492	2	pawnY	D
/*    */     //   334	484	4	lastStep	[D
/*    */     //   0	819	0	this	Lme/earth/phobos/features/modules/movement/TPSpeed;
/*    */     //   0	819	1	event	Lme/earth/phobos/event/events/UpdateWalkingPlayerEvent;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public enum Mode
/*    */   {
/* 87 */     NORMAL,
/* 88 */     STEP;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\movement\TPSpeed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */