/*    */ package me.earth.phobos.manager;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ import me.earth.phobos.features.Feature;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.PlayerUtil;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ 
/*    */ public class FriendManager
/*    */   extends Feature
/*    */ {
/* 14 */   private final Map<String, UUID> friends = new HashMap<>();
/*    */   
/*    */   public FriendManager() {
/* 17 */     super("Friends");
/*    */   }
/*    */   
/*    */   public boolean isFriend(String name) {
/* 21 */     return (this.friends.get(name) != null);
/*    */   }
/*    */   
/*    */   public boolean isFriend(EntityPlayer player) {
/* 25 */     return isFriend(player.func_70005_c_());
/*    */   }
/*    */   
/*    */   public void addFriend(String name) {
/* 29 */     Friend friend = getFriendByName(name);
/* 30 */     if (friend != null) {
/* 31 */       this.friends.put(friend.getUsername(), friend.getUuid());
/*    */     }
/*    */   }
/*    */   
/*    */   public void removeFriend(String name) {
/* 36 */     this.friends.remove(name);
/*    */   }
/*    */   
/*    */   public void onLoad() {
/* 40 */     this.friends.clear();
/* 41 */     clearSettings();
/*    */   }
/*    */   
/*    */   public void saveFriends() {
/* 45 */     clearSettings();
/* 46 */     for (Map.Entry<String, UUID> entry : this.friends.entrySet()) {
/* 47 */       register(new Setting(((UUID)entry.getValue()).toString(), entry.getKey()));
/*    */     }
/*    */   }
/*    */   
/*    */   public Map<String, UUID> getFriends() {
/* 52 */     return this.friends;
/*    */   }
/*    */   
/*    */   public Friend getFriendByName(String input) {
/* 56 */     UUID uuid = PlayerUtil.getUUIDFromName(input);
/* 57 */     if (uuid != null) {
/* 58 */       return new Friend(input, uuid);
/*    */     }
/* 60 */     return null;
/*    */   }
/*    */   
/*    */   public void addFriend(Friend friend) {
/* 64 */     this.friends.put(friend.getUsername(), friend.getUuid());
/*    */   }
/*    */   
/*    */   public static class Friend {
/*    */     private final String username;
/*    */     private final UUID uuid;
/*    */     
/*    */     public Friend(String username, UUID uuid) {
/* 72 */       this.username = username;
/* 73 */       this.uuid = uuid;
/*    */     }
/*    */     
/*    */     public String getUsername() {
/* 77 */       return this.username;
/*    */     }
/*    */     
/*    */     public UUID getUuid() {
/* 81 */       return this.uuid;
/*    */     }
/*    */     
/*    */     public boolean equals(Object other) {
/* 85 */       return (other instanceof Friend && ((Friend)other).getUsername().equals(this.username) && ((Friend)other).getUuid().equals(this.uuid));
/*    */     }
/*    */     
/*    */     public int hashCode() {
/* 89 */       return this.username.hashCode() + this.uuid.hashCode();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\manager\FriendManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */