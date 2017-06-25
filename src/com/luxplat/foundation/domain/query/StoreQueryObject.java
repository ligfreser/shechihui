package com.luxplat.foundation.domain.query;
 
 import com.luxplat.core.query.QueryObject;
 import org.springframework.web.servlet.ModelAndView;
 
 public class StoreQueryObject extends QueryObject
 {
   public StoreQueryObject(String currentPage, ModelAndView mv, String orderBy, String orderType)
   {
     super(currentPage, mv, orderBy, orderType);
   }
 
   public StoreQueryObject()
   {
   }
 }



