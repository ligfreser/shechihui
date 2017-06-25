package com.luxplat.foundation.domain.query;
 
 import com.luxplat.core.query.QueryObject;
 import org.springframework.web.servlet.ModelAndView;
 
 public class SpareGoodsFloorQueryObject extends QueryObject
 {
   public SpareGoodsFloorQueryObject(String currentPage, ModelAndView mv, String orderBy, String orderType)
   {
     super(currentPage, mv, orderBy, orderType);
   }
 
   public SpareGoodsFloorQueryObject()
   {
   }
 }



