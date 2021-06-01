/*    */ package me.earth.phobos.mixin;
/*    */ 
/*    */ import java.util.Map;
/*    */ import me.earth.phobos.Phobos;
/*    */ import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
/*    */ import org.spongepowered.asm.launch.MixinBootstrap;
/*    */ import org.spongepowered.asm.mixin.MixinEnvironment;
/*    */ import org.spongepowered.asm.mixin.Mixins;
/*    */ 
/*    */ public class PhobosMixinLoader
/*    */   implements IFMLLoadingPlugin {
/*    */   private static boolean isObfuscatedEnvironment = false;
/*    */   
/*    */   public PhobosMixinLoader() {
/* 15 */     Phobos.LOGGER.info("Catware mixins initialized");
/* 16 */     MixinBootstrap.init();
/* 17 */     Mixins.addConfiguration("mixins.phobos.json");
/* 18 */     MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
/* 19 */     Phobos.LOGGER.info(MixinEnvironment.getDefaultEnvironment().getObfuscationContext());
/*    */   }
/*    */   
/*    */   public String[] getASMTransformerClass() {
/* 23 */     return new String[0];
/*    */   }
/*    */   
/*    */   public String getModContainerClass() {
/* 27 */     return null;
/*    */   }
/*    */   
/*    */   public String getSetupClass() {
/* 31 */     return null;
/*    */   }
/*    */   
/*    */   public void injectData(Map<String, Object> data) {
/* 35 */     isObfuscatedEnvironment = ((Boolean)data.get("runtimeDeobfuscationEnabled")).booleanValue();
/*    */   }
/*    */   
/*    */   public String getAccessTransformerClass() {
/* 39 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\PhobosMixinLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */