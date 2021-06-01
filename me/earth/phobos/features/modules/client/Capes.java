/*    */ package me.earth.phobos.features.modules.client;
/*    */ import java.util.Arrays;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import net.minecraft.client.entity.AbstractClientPlayer;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class Capes extends Module {
/* 11 */   public static final ResourceLocation THREEVT_CAPE = new ResourceLocation("textures/3vt2.png");
/* 12 */   public static final ResourceLocation ZBOB_CAPE = new ResourceLocation("textures/zb0b.png");
/* 13 */   public static final ResourceLocation OHARE_CAPE = new ResourceLocation("textures/ohare.png");
/* 14 */   public static final ResourceLocation SQUID_CAPE = new ResourceLocation("textures/squidcape.png");
/* 15 */   public static Map<String, String[]> UUIDs = (Map)new HashMap<>();
/*    */   private static Capes instance;
/*    */   
/*    */   public Capes() {
/* 19 */     super("Capes", "Renders the client's capes", Module.Category.CLIENT, false, true, false);
/* 20 */     UUIDs.put("Megyn", new String[] { "a5e36d37-5fbe-4481-b5be-1f06baee1f1c", "7de842e8-af08-49ed-9d0c-4071e2a99f00", "8ca55379-c872-4299-987d-d20962badd11", "e6e8bf7e-0b23-4d2e-b2ae-c40c5ff4eecc" });
/* 21 */     UUIDs.put("zb0b", new String[] { "0aa3b04f-786a-49c8-bea9-025ee0dd1e85" });
/* 22 */     UUIDs.put("3vt", new String[] { "19bf3f1f-fe06-4c86-bea5-3dad5df89714", "b0836db9-2472-4ba6-a1b7-92c605f5e80d" });
/* 23 */     UUIDs.put("oHare", new String[] { "453e38dd-f4a9-481f-8ebd-8339e89e5445" });
/* 24 */     UUIDs.put("Squid", new String[] { "811c9272-9793-4fdd-980d-778e8ad2e54c", "09410a87-dfc8-476c-9acb-04bd07126c6e", "2eb88d28-7a26-43ad-81aa-113bd818d977" });
/* 25 */     instance = this;
/*    */   }
/*    */   
/*    */   public static Capes getInstance() {
/* 29 */     if (instance == null) {
/* 30 */       instance = new Capes();
/*    */     }
/* 32 */     return instance;
/*    */   }
/*    */   
/*    */   public static ResourceLocation getCapeResource(AbstractClientPlayer player) {
/* 36 */     for (String name : UUIDs.keySet()) {
/* 37 */       String[] arrayOfString; int i; byte b; for (arrayOfString = UUIDs.get(name), i = arrayOfString.length, b = 0; b < i; ) { String uuid = arrayOfString[b];
/* 38 */         if (name.equalsIgnoreCase("3vt") && player.func_110124_au().toString().equals(uuid)) {
/* 39 */           return THREEVT_CAPE;
/*    */         }
/* 41 */         if (name.equalsIgnoreCase("Megyn") && player.func_110124_au().toString().equals(uuid)) {
/* 42 */           return THREEVT_CAPE;
/*    */         }
/* 44 */         if (!name.equalsIgnoreCase("oHare") || !player.func_110124_au().toString().equals(uuid)) { b++; continue; }
/* 45 */          return OHARE_CAPE; }
/*    */     
/*    */     } 
/* 48 */     return null;
/*    */   }
/*    */   
/*    */   public static boolean hasCape(UUID uuid) {
/* 52 */     Iterator<String> iterator = UUIDs.keySet().iterator();
/* 53 */     if (iterator.hasNext()) {
/* 54 */       String name = iterator.next();
/* 55 */       return Arrays.<Object>asList((Object[])UUIDs.get(name)).contains(uuid.toString());
/*    */     } 
/* 57 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\client\Capes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */