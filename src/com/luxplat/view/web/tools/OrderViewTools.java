 package com.luxplat.view.web.tools;
 
 import com.luxplat.core.security.support.SecurityUserHolder;
 import com.luxplat.foundation.domain.Store;
 import com.luxplat.foundation.domain.User;
 import com.luxplat.foundation.service.IOrderFormService;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Component;
 
 @Component
 public class OrderViewTools
 {
 
   @Autowired
   private IOrderFormService orderFormService;
 
   public int query_user_order(String order_status)
   {
     Map params = new HashMap();
     int status = -1;
     if (order_status.equals("order_submit")) {
       status = 10;
     }
     if (order_status.equals("order_pay")) {
       status = 20;
     }
     if (order_status.equals("order_shipping")) {
       status = 30;
     }
     if (order_status.equals("order_receive")) {
       status = 40;
     }
     if (order_status.equals("order_finish")) {
       status = 60;
     }
     if (order_status.equals("order_cancel")) {
       status = 0;
     }
     params.put("status", Integer.valueOf(status));
     params.put("user_id", SecurityUserHolder.getCurrentUser().getId());
     List ofs = this.orderFormService
       .query(
       "select obj from OrderForm obj where obj.order_status=:status and obj.user.id=:user_id", 
       params, -1, -1);
     return ofs.size();
   }
 
   public int query_store_order(String order_status) {
     if (SecurityUserHolder.getCurrentUser().getStore() != null) {
       Map params = new HashMap();
       int status = -1;
       if (order_status.equals("order_submit")) {
         status = 10;
       }
       if (order_status.equals("order_pay")) {
         status = 20;
       }
       if (order_status.equals("order_shipping")) {
         status = 30;
       }
       if (order_status.equals("order_receive")) {
         status = 40;
       }
       if (order_status.equals("order_finish")) {
         status = 60;
       }
       if (order_status.equals("order_cancel")) {
         status = 0;
       }
       params.put("status", Integer.valueOf(status));
       params.put("store_id", SecurityUserHolder.getCurrentUser()
         .getStore().getId());
       List ofs = this.orderFormService
         .query(
         "select obj from OrderForm obj where obj.order_status=:status and obj.store.id=:store_id", 
         params, -1, -1);
       return ofs.size();
     }
     return 0;
   }
 }
