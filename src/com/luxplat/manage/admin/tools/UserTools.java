package com.luxplat.manage.admin.tools;
 
 import com.luxplat.core.tools.CommUtil;
 import com.luxplat.foundation.domain.User;
 import com.luxplat.foundation.service.IUserService;
 import java.util.ArrayList;
 import java.util.List;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.security.concurrent.SessionRegistry;
 import org.springframework.stereotype.Component;
 
 @Component
 public class UserTools
 {
 
   @Autowired
   private SessionRegistry sessionRegistry;
 
   @Autowired
   private IUserService userSerivce;
 
   public List<User> query_user()
   {
     List users = new ArrayList();
     Object[] objs = this.sessionRegistry.getAllPrincipals();
     for (int i = 0; i < objs.length; i++) {
       User user = this.userSerivce.getObjByProperty("userName", 
         CommUtil.null2String(objs[i]));
 
       users.add(user);
     }
 
     return users;
   }
 
   public boolean userOnLine(String userName)
   {
     boolean ret = false;
     List users = query_user();
     for (int i=0 ; i<users.size() ; i++) {
    	 User tmp_user = (User)users.get(i) ;
         if ((tmp_user != null) && (tmp_user.getUsername().equals(userName.trim()))) {
             ret = true;
           }
     }
     return ret;
   }
 }



