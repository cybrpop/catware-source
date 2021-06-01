/*     */ package me.earth.phobos.features.modules.client;
/*     */ import java.awt.Color;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.net.Socket;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.Render2DEvent;
/*     */ import me.earth.phobos.event.events.Render3DEvent;
/*     */ import me.earth.phobos.features.Feature;
/*     */ import me.earth.phobos.features.command.Command;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Bind;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.manager.WaypointManager;
/*     */ import me.earth.phobos.util.RenderUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import me.earth.phobos.util.Util;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.init.SoundEvents;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.SoundCategory;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.RayTraceResult;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ public class IRC extends Module {
/*  37 */   public static final Random avRandomizer = new Random();
/*  38 */   private static final ResourceLocation SHULKER_GUI_TEXTURE = null;
/*     */   public static IRC INSTANCE;
/*     */   public static IRCHandler handler;
/*     */   public static List<String> phobosUsers;
/*  42 */   public Setting<String> ip = register(new Setting("IP", "206.189.218.150"));
/*  43 */   public Setting<Boolean> waypoints = register(new Setting("Waypoints", Boolean.valueOf(false)));
/*  44 */   public Setting<Boolean> ding = register(new Setting("Ding", Boolean.valueOf(false), v -> ((Boolean)this.waypoints.getValue()).booleanValue()));
/*  45 */   public Setting<Integer> red = register(new Setting("Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.waypoints.getValue()).booleanValue()));
/*  46 */   public Setting<Integer> green = register(new Setting("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.waypoints.getValue()).booleanValue()));
/*  47 */   public Setting<Integer> blue = register(new Setting("Blue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.waypoints.getValue()).booleanValue()));
/*  48 */   public Setting<Integer> alpha = register(new Setting("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.waypoints.getValue()).booleanValue()));
/*  49 */   public Setting<Boolean> inventories = register(new Setting("Inventories", Boolean.valueOf(false)));
/*  50 */   public Setting<Boolean> render = register(new Setting("Render", Boolean.valueOf(true), v -> ((Boolean)this.inventories.getValue()).booleanValue()));
/*  51 */   public Setting<Boolean> own = register(new Setting("OwnShulker", Boolean.valueOf(true), v -> ((Boolean)this.inventories.getValue()).booleanValue()));
/*  52 */   public Setting<Integer> cooldown = register(new Setting("ShowForS", Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(5), v -> ((Boolean)this.inventories.getValue()).booleanValue()));
/*  53 */   public Setting<Boolean> offsets = register(new Setting("Offsets", Boolean.valueOf(false)));
/*  54 */   private final Setting<Integer> yPerPlayer = register(new Setting("Y/Player", Integer.valueOf(18), v -> ((Boolean)this.offsets.getValue()).booleanValue()));
/*  55 */   private final Setting<Integer> xOffset = register(new Setting("XOffset", Integer.valueOf(4), v -> ((Boolean)this.offsets.getValue()).booleanValue()));
/*  56 */   private final Setting<Integer> yOffset = register(new Setting("YOffset", Integer.valueOf(2), v -> ((Boolean)this.offsets.getValue()).booleanValue()));
/*  57 */   private final Setting<Integer> trOffset = register(new Setting("TROffset", Integer.valueOf(2), v -> ((Boolean)this.offsets.getValue()).booleanValue()));
/*  58 */   public Setting<Integer> invH = register(new Setting("InvH", Integer.valueOf(3), v -> ((Boolean)this.inventories.getValue()).booleanValue()));
/*  59 */   public Setting<Bind> pingBind = register(new Setting("Ping", new Bind(-1)));
/*     */   public boolean status = false;
/*  61 */   public Timer updateTimer = new Timer();
/*  62 */   public Timer downTimer = new Timer();
/*     */   
/*     */   public BlockPos waypointTarget;
/*  65 */   private int textRadarY = 0;
/*     */   private boolean down = false;
/*     */   private boolean pressed = false;
/*     */   
/*     */   public IRC() {
/*  70 */     super("just don't", "Phobos chat server", Module.Category.CLIENT, true, true, true);
/*  71 */     INSTANCE = this;
/*     */   }
/*     */   
/*     */   public static void updateInventory() throws IOException {
/*  75 */     handler.outputStream.writeUTF("updateinventory");
/*  76 */     handler.outputStream.writeUTF(mc.field_71439_g.func_70005_c_());
/*  77 */     writeByteArray(serializeInventory(), handler.outputStream);
/*     */   }
/*     */   
/*     */   public static void updateInventories() {
/*  81 */     for (String player : phobosUsers) {
/*     */       try {
/*  83 */         send("inventory", player);
/*  84 */       } catch (IOException e) {
/*  85 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void updateWaypoint(BlockPos pos, String server, String dimension, Color color) throws IOException {
/*  91 */     send("waypoint", server + ":" + dimension + ":" + pos.func_177958_n() + ":" + pos.func_177956_o() + ":" + pos.func_177952_p(), color.getRed() + ":" + color.getGreen() + ":" + color.getBlue() + ":" + color.getAlpha());
/*     */   }
/*     */   
/*     */   public static void removeWaypoint() throws IOException {
/*  95 */     handler.outputStream.writeUTF("removewaypoint");
/*  96 */     handler.outputStream.writeUTF(mc.field_71439_g.func_70005_c_());
/*  97 */     handler.outputStream.flush();
/*     */   }
/*     */   
/*     */   public static void send(String command, String data, String data1) throws IOException {
/* 101 */     handler.outputStream.writeUTF(command);
/* 102 */     handler.outputStream.writeUTF(mc.field_71439_g.func_70005_c_());
/* 103 */     handler.outputStream.writeUTF(data);
/* 104 */     handler.outputStream.writeUTF(data1);
/* 105 */     handler.outputStream.flush();
/*     */   }
/*     */   
/*     */   public static void send(String command, String data) throws IOException {
/* 109 */     handler.outputStream.writeUTF(command);
/* 110 */     handler.outputStream.writeUTF(mc.field_71439_g.func_70005_c_());
/* 111 */     handler.outputStream.writeUTF(data);
/* 112 */     handler.outputStream.flush();
/*     */   }
/*     */   
/*     */   private static byte[] readByteArrayLWithLength(DataInputStream reader) throws IOException {
/* 116 */     int length = reader.readInt();
/* 117 */     if (length > 0) {
/* 118 */       byte[] cifrato = new byte[length];
/* 119 */       reader.readFully(cifrato, 0, cifrato.length);
/* 120 */       return cifrato;
/*     */     } 
/* 122 */     return null;
/*     */   }
/*     */   
/*     */   public static void writeByteArray(byte[] data, DataOutputStream writer) throws IOException {
/* 126 */     writer.writeInt(data.length);
/* 127 */     writer.write(data);
/* 128 */     writer.flush();
/*     */   }
/*     */   
/*     */   public static List<ItemStack> deserializeInventory(byte[] inventory) throws IOException, ClassNotFoundException {
/* 132 */     ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(inventory));
/* 133 */     ArrayList<ItemStack> inventoryList = (ArrayList<ItemStack>)stream.readObject();
/* 134 */     return inventoryList;
/*     */   }
/*     */   
/*     */   public static byte[] serializeInventory() throws IOException {
/* 138 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 139 */     ObjectOutputStream oos = new ObjectOutputStream(bos);
/* 140 */     oos.writeObject(new ArrayList((Collection<?>)mc.field_71439_g.field_71071_by.field_70462_a));
/* 141 */     return bos.toByteArray();
/*     */   }
/*     */   
/*     */   public static void say(String message) throws IOException {
/* 145 */     handler.outputStream.writeUTF("message");
/* 146 */     handler.outputStream.writeUTF(mc.field_71439_g.func_70005_c_());
/* 147 */     handler.outputStream.writeUTF(message);
/* 148 */     handler.outputStream.flush();
/*     */   }
/*     */   
/*     */   public static void cockt(int id) throws IOException {
/* 152 */     handler.outputStream.writeUTF("cockt");
/* 153 */     handler.outputStream.writeInt(id);
/* 154 */     handler.outputStream.flush();
/*     */   }
/*     */   
/*     */   public static String getDimension(int dim) {
/* 158 */     switch (dim) {
/*     */       case 0:
/* 160 */         return "Overworld";
/*     */       
/*     */       case -1:
/* 163 */         return "Nether";
/*     */       
/*     */       case 1:
/* 166 */         return "End";
/*     */     } 
/*     */     
/* 169 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/* 176 */     if (handler != null && handler.isAlive() && !handler.isInterrupted()) {
/* 177 */       this.status = !handler.socket.isClosed();
/*     */     } else {
/* 179 */       this.status = false;
/*     */     } 
/* 181 */     if (this.updateTimer.passedMs(5000L) && handler != null && handler.isAlive() && !handler.socket.isClosed()) {
/*     */       try {
/* 183 */         handler.outputStream.writeUTF("update");
/* 184 */         handler.outputStream.writeUTF(mc.field_71439_g.func_70005_c_());
/* 185 */         handler.outputStream.flush();
/* 186 */       } catch (Exception e) {
/* 187 */         e.printStackTrace();
/*     */       } 
/* 189 */       this.updateTimer.reset();
/*     */     } 
/* 191 */     if (!mc.func_71356_B() && !(mc.field_71462_r instanceof me.earth.phobos.features.gui.PhobosGui) && handler != null && !handler.socket.isClosed() && this.status) {
/* 192 */       if (this.down) {
/* 193 */         if (this.downTimer.passedMs(2000L)) {
/*     */           try {
/* 195 */             removeWaypoint();
/* 196 */           } catch (IOException e2) {
/* 197 */             e2.printStackTrace();
/*     */           } 
/* 199 */           this.down = false;
/* 200 */           this.downTimer.reset();
/*     */         } 
/* 202 */         if (!Keyboard.isKeyDown(((Bind)this.pingBind.getValue()).getKey())) {
/*     */           try {
/* 204 */             updateWaypoint(this.waypointTarget, mc.field_71422_O.field_78845_b, String.valueOf(mc.field_71439_g.field_71093_bK), new Color(((Integer)this.red.getValue()).intValue(), ((Integer)this.green.getValue()).intValue(), ((Integer)this.blue.getValue()).intValue(), ((Integer)this.alpha.getValue()).intValue()));
/* 205 */           } catch (IOException e2) {
/* 206 */             e2.printStackTrace();
/*     */           } 
/*     */         }
/*     */       } 
/* 210 */       if (Keyboard.isKeyDown(((Bind)this.pingBind.getValue()).getKey())) {
/* 211 */         if (!this.pressed) {
/* 212 */           this.down = true;
/* 213 */           this.pressed = true;
/*     */         } 
/*     */       } else {
/* 216 */         this.down = false;
/* 217 */         this.pressed = false;
/* 218 */         this.downTimer.reset();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender3D(Render3DEvent event) {
/* 225 */     if (Feature.fullNullCheck() || mc.func_71356_B()) {
/*     */       return;
/*     */     }
/* 228 */     RayTraceResult result = mc.field_71439_g.func_174822_a(2000.0D, event.getPartialTicks());
/* 229 */     if (result != null) {
/* 230 */       this.waypointTarget = new BlockPos(result.field_72307_f);
/*     */     }
/* 232 */     if (((Boolean)this.waypoints.getValue()).booleanValue()) {
/* 233 */       for (WaypointManager.Waypoint waypoint : Phobos.waypointManager.waypoints.values()) {
/* 234 */         if (mc.field_71439_g.field_71093_bK != waypoint.dimension || 
/* 235 */           !mc.field_71422_O.field_78845_b.equals(waypoint.server)) {
/*     */           continue;
/*     */         }
/* 238 */         waypoint.renderBox();
/* 239 */         waypoint.render();
/* 240 */         GlStateManager.func_179126_j();
/* 241 */         GlStateManager.func_179132_a(true);
/* 242 */         GlStateManager.func_179145_e();
/* 243 */         GlStateManager.func_179084_k();
/* 244 */         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 245 */         RenderHelper.func_74518_a();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onRender2D(Render2DEvent event) {
/* 253 */     if (fullNullCheck()) {
/*     */       return;
/*     */     }
/* 256 */     if (((Boolean)this.inventories.getValue()).booleanValue()) {
/* 257 */       int x = -4 + ((Integer)this.xOffset.getValue()).intValue();
/* 258 */       int y = 10 + ((Integer)this.yOffset.getValue()).intValue();
/* 259 */       this.textRadarY = 0;
/* 260 */       for (String player : phobosUsers) {
/* 261 */         if (Phobos.inventoryManager.inventories.get(player) != null) {
/*     */           continue;
/*     */         }
/* 264 */         List<ItemStack> stacks = (List<ItemStack>)Phobos.inventoryManager.inventories.get(player);
/* 265 */         renderShulkerToolTip(stacks, x, y, player);
/* 266 */         y += ((Integer)this.yPerPlayer.getValue()).intValue() + 60;
/* 267 */         this.textRadarY = y - 10 - ((Integer)this.yOffset.getValue()).intValue() + ((Integer)this.trOffset.getValue()).intValue();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void connect() throws IOException {
/* 273 */     if (!INSTANCE.status) {
/* 274 */       Socket socket = new Socket((String)this.ip.getValue(), 1488);
/* 275 */       (handler = new IRCHandler(socket)).start();
/* 276 */       handler.outputStream.writeUTF("update");
/* 277 */       handler.outputStream.writeUTF(mc.field_71439_g.func_70005_c_());
/* 278 */       handler.outputStream.flush();
/* 279 */       INSTANCE.status = true;
/* 280 */       Command.sendMessage("Â§aIRC connected successfully!");
/*     */     } else {
/* 282 */       Command.sendMessage("Â§cIRC is already connected!");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void disconnect() throws IOException {
/* 287 */     if (INSTANCE.status) {
/* 288 */       handler.socket.close();
/* 289 */       if (!handler.isInterrupted()) {
/* 290 */         handler.interrupt();
/*     */       }
/*     */     } else {
/* 293 */       Command.sendMessage("Â§cIRC is not connected!");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void friendAll() throws IOException {
/* 298 */     handler.outputStream.writeUTF("friendall");
/* 299 */     handler.outputStream.flush();
/*     */   }
/*     */   
/*     */   public void list() throws IOException {
/* 303 */     handler.outputStream.writeUTF("list");
/* 304 */     handler.outputStream.flush();
/*     */   }
/*     */   
/*     */   public void renderShulkerToolTip(List<ItemStack> stacks, int x, int y, String name) {
/* 308 */     GlStateManager.func_179098_w();
/* 309 */     GlStateManager.func_179140_f();
/* 310 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 311 */     GlStateManager.func_179147_l();
/* 312 */     GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
/* 313 */     mc.func_110434_K().func_110577_a(SHULKER_GUI_TEXTURE);
/* 314 */     RenderUtil.drawTexturedRect(x, y, 0, 0, 176, 16, 500);
/* 315 */     RenderUtil.drawTexturedRect(x, y + 16, 0, 16, 176, 54 + ((Integer)this.invH.getValue()).intValue(), 500);
/* 316 */     RenderUtil.drawTexturedRect(x, y + 16 + 54, 0, 160, 176, 8, 500);
/* 317 */     GlStateManager.func_179097_i();
/* 318 */     Color color = new Color(0, 0, 0, 255);
/* 319 */     this.renderer.drawStringWithShadow(name, (x + 8), (y + 6), ColorUtil.toRGBA(color));
/* 320 */     GlStateManager.func_179126_j();
/* 321 */     RenderHelper.func_74520_c();
/* 322 */     GlStateManager.func_179091_B();
/* 323 */     GlStateManager.func_179142_g();
/* 324 */     GlStateManager.func_179145_e();
/* 325 */     for (int i = 0; i < stacks.size(); i++) {
/* 326 */       int iX = x + i % 9 * 18 + 8;
/* 327 */       int iY = y + i / 9 * 18 + 18;
/* 328 */       ItemStack itemStack = stacks.get(i);
/* 329 */       (mc.func_175599_af()).field_77023_b = 501.0F;
/* 330 */       RenderUtil.itemRender.func_180450_b(itemStack, iX, iY);
/* 331 */       RenderUtil.itemRender.func_180453_a(mc.field_71466_p, itemStack, iX, iY, null);
/* 332 */       (mc.func_175599_af()).field_77023_b = 0.0F;
/*     */     } 
/* 334 */     GlStateManager.func_179140_f();
/* 335 */     GlStateManager.func_179084_k();
/* 336 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   private static class IRCHandler
/*     */     extends Thread {
/*     */     public Socket socket;
/*     */     public DataInputStream inputStream;
/*     */     public DataOutputStream outputStream;
/*     */     
/*     */     public IRCHandler(Socket socket) {
/* 346 */       super(Util.mc.field_71439_g.func_70005_c_());
/* 347 */       this.socket = socket;
/*     */       try {
/* 349 */         this.inputStream = new DataInputStream(socket.getInputStream());
/* 350 */         this.outputStream = new DataOutputStream(socket.getOutputStream());
/* 351 */       } catch (IOException e) {
/* 352 */         e.printStackTrace();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 358 */       Command.sendMessage("Â§aSocket thread starting!");
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/*     */         try {
/* 364 */           String input = this.inputStream.readUTF();
/* 365 */           if (input.equalsIgnoreCase("message")) {
/* 366 */             String name = this.inputStream.readUTF();
/* 367 */             String message = this.inputStream.readUTF();
/* 368 */             Command.sendMessage("Â§c[IRC] Â§r<" + name + ">: " + message);
/*     */           } 
/* 370 */           if (input.equalsIgnoreCase("list")) {
/* 371 */             String f = this.inputStream.readUTF();
/*     */             
/* 373 */             String[] split = f.split("%%%"), friends = split;
/* 374 */             for (String friend : split) {
/* 375 */               Command.sendMessage("Â§b" + friend.replace("_&_", " ID: "));
/*     */             }
/* 377 */           } else if (input.equalsIgnoreCase("friendall")) {
/* 378 */             String f = this.inputStream.readUTF();
/*     */             
/* 380 */             String[] split2 = f.split("%%%"), friends = split2;
/* 381 */             for (String friend : split2) {
/* 382 */               if (!friend.equals(Util.mc.field_71439_g.func_70005_c_())) {
/* 383 */                 Phobos.friendManager.addFriend(friend);
/* 384 */                 Command.sendMessage("Â§b" + friend + " has been friended");
/*     */               } 
/*     */             } 
/* 387 */           } else if (input.equalsIgnoreCase("waypoint")) {
/* 388 */             String name = this.inputStream.readUTF();
/* 389 */             String[] inputs = this.inputStream.readUTF().split(":");
/* 390 */             String[] colors = this.inputStream.readUTF().split(":");
/* 391 */             String server = inputs[0];
/* 392 */             String dimension = inputs[1];
/* 393 */             Color color = new Color(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2]), Integer.parseInt(colors[3]));
/* 394 */             Phobos.waypointManager.waypoints.put(name, new WaypointManager.Waypoint(name, server, Integer.parseInt(dimension), Integer.parseInt(inputs[2]), Integer.parseInt(inputs[3]), Integer.parseInt(inputs[4]), color));
/* 395 */             Command.sendMessage("Â§c[IRC] Â§r" + name + " has set a waypoint at Â§c(" + Integer.parseInt(inputs[2]) + "," + Integer.parseInt(inputs[3]) + "," + Integer.parseInt(inputs[4]) + ")Â§r on the server Â§c" + server + "Â§r in the dimension Â§c" + IRC.getDimension(Integer.parseInt(dimension)));
/* 396 */             if (((Boolean)IRC.INSTANCE.ding.getValue()).booleanValue()) {
/* 397 */               Util.mc.field_71441_e.func_184134_a(Util.mc.field_71439_g.field_70165_t, Util.mc.field_71439_g.field_70163_u, Util.mc.field_71439_g.field_70161_v, SoundEvents.field_187604_bf, SoundCategory.PLAYERS, 1.0F, 0.7F, false);
/*     */             }
/* 399 */           } else if (input.equalsIgnoreCase("removewaypoint")) {
/* 400 */             String name = this.inputStream.readUTF();
/* 401 */             Phobos.waypointManager.waypoints.remove(name);
/* 402 */             Command.sendMessage("Â§c[IRC] Â§r" + name + " has removed their waypoint");
/* 403 */             if (((Boolean)IRC.INSTANCE.ding.getValue()).booleanValue()) {
/* 404 */               Util.mc.field_71441_e.func_184134_a(Util.mc.field_71439_g.field_70165_t, Util.mc.field_71439_g.field_70163_u, Util.mc.field_71439_g.field_70161_v, SoundEvents.field_187604_bf, SoundCategory.PLAYERS, 1.0F, -0.7F, false);
/*     */             }
/* 406 */           } else if (input.equalsIgnoreCase("inventory")) {
/* 407 */             String name = this.inputStream.readUTF();
/* 408 */             byte[] inventory = IRC.readByteArrayLWithLength(this.inputStream);
/* 409 */             for (String player : IRC.phobosUsers) {
/* 410 */               if (player.equalsIgnoreCase(name)) {
/* 411 */                 Phobos.inventoryManager.inventories.put(player, IRC.deserializeInventory(inventory));
/*     */               }
/*     */             } 
/* 414 */           } else if (input.equalsIgnoreCase("users")) {
/* 415 */             byte[] inputBytes = IRC.readByteArrayLWithLength(this.inputStream);
/* 416 */             ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(inputBytes));
/* 417 */             List<String> players = (List<String>)stream.readObject();
/* 418 */             Command.sendMessage("Â§c[IRC]Â§r Active Users:");
/* 419 */             for (String name2 : players) {
/* 420 */               Command.sendMessage(name2);
/* 421 */               if (!IRC.phobosUsers.contains(name2)) {
/* 422 */                 IRC.phobosUsers.add(name2);
/*     */               }
/*     */             } 
/*     */           } 
/* 426 */           IRC.INSTANCE.status = !this.socket.isClosed();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         }
/* 435 */         catch (IOException|ClassNotFoundException e) {
/* 436 */           e.printStackTrace();
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\client\IRC.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */