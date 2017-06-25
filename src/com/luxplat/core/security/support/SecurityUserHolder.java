 package com.luxplat.core.security.support;
 
 import com.luxplat.core.tools.CommUtil;
 import com.luxplat.foundation.domain.User;
 import javax.servlet.http.Cookie;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpSession;
 import org.springframework.security.Authentication;
 import org.springframework.security.context.SecurityContext;
 import org.springframework.security.context.SecurityContextHolder;
 import org.springframework.web.context.request.RequestContextHolder;
 import org.springframework.web.context.request.ServletRequestAttributes;
 
 public class SecurityUserHolder
 {
   public static User getCurrentUser()
   {
     if (SecurityContextHolder.getContext().getAuthentication() != null)
     {
       if ((SecurityContextHolder.getContext().getAuthentication()
         .getPrincipal() instanceof User))
       {
         return (User)SecurityContextHolder.getContext()
           .getAuthentication().getPrincipal();
       }
     }
     User user = null;
     if (RequestContextHolder.getRequestAttributes() != null) {
       HttpServletRequest request = ((ServletRequestAttributes)
         RequestContextHolder.getRequestAttributes()).getRequest();
       user = request.getSession(false).getAttribute("user") != null ? 
         (User)request
         .getSession(false).getAttribute("user") : null;
 
       Cookie[] cookies = request.getCookies();
       String id = "";
       if (cookies != null) {
         for (Cookie cookie : cookies)
         {
           if (cookie.getName()
             .equals("luxplat_user_session")) {
             id = CommUtil.null2String(cookie.getValue());
           }
         }
       }
       if (id.equals("")) {
         user = null;
       }
     }
 
     return user;
   }
 }
