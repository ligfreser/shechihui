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
 @Table(name="luxplat_homepage_goodsclass")
 public class HomePageGoodsClass extends IdEntity
 {
 
   @ManyToOne(fetch=FetchType.LAZY)
   private User user;
 
   @ManyToOne(fetch=FetchType.LAZY)
   private GoodsClass gc;
 
   public GoodsClass getGc()
   {
     return this.gc;
   }
 
   public void setGc(GoodsClass gc) {
     this.gc = gc;
   }
 
   public User getUser() {
     return this.user;
   }
 
   public void setUser(User user) {
     this.user = user;
   }
 }



