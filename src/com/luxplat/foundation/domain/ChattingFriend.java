package com.luxplat.foundation.domain;
 
 import com.luxplat.core.domain.IdEntity;
 import javax.persistence.Entity;
 import javax.persistence.FetchType;
 import javax.persistence.ManyToOne;
 import javax.persistence.Table;
 import org.hibernate.annotations.Cache;
 import org.hibernate.annotations.CacheConcurrencyStrategy;
 
 @Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
 @Entity
 @Table(name="luxplat_chattingfriend")
 public class ChattingFriend extends IdEntity
 {
 
   @ManyToOne(fetch=FetchType.LAZY)
   private User user;
 
   @ManyToOne(fetch=FetchType.LAZY)
   private User friendUser;
 
   public User getUser()
   {
     return this.user;
   }
 
   public void setUser(User user) {
     this.user = user;
   }
 
   public User getFriendUser() {
     return this.friendUser;
   }
 
   public void setFriendUser(User friendUser) {
     this.friendUser = friendUser;
   }
 }



