package com.luxplat.manage.buyer.action;
 
 import com.luxplat.core.annotation.SecurityMapping;
 import com.luxplat.core.domain.virtual.SysMap;
 import com.luxplat.core.mv.JModelAndView;
 import com.luxplat.core.query.support.IPageList;
 import com.luxplat.core.security.support.SecurityUserHolder;
 import com.luxplat.core.tools.CommUtil;
 import com.luxplat.foundation.domain.User;
 import com.luxplat.foundation.domain.query.CouponInfoQueryObject;
 import com.luxplat.foundation.service.ICouponInfoService;
 import com.luxplat.foundation.service.ISysConfigService;
 import com.luxplat.foundation.service.IUserConfigService;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.servlet.ModelAndView;
 
 @Controller
 public class CouponBuyerAction
 {
 
   @Autowired
   private ISysConfigService configService;
 
   @Autowired
   private IUserConfigService userConfigService;
 
   @Autowired
   private ICouponInfoService couponInfoService;
 
   @SecurityMapping(title="买家优惠券列表", value="/buyer/coupon.htm*", rtype="buyer", rname="用户中心", rcode="user_center", rgroup="用户中心", display = false, rsequence = 0)
   @RequestMapping({"/buyer/coupon.htm"})
   public ModelAndView coupon(HttpServletRequest request, HttpServletResponse response, String reply, String currentPage)
   {
     ModelAndView mv = new JModelAndView(
       "user/default/usercenter/buyer_coupon.html", this.configService
       .getSysConfig(), 
       this.userConfigService.getUserConfig(), 0, request, response);
     CouponInfoQueryObject qo = new CouponInfoQueryObject(currentPage, mv, 
       "addTime", "desc");
     qo.addQuery("obj.user.id", new SysMap("user_id", 
       SecurityUserHolder.getCurrentUser().getId()), "=");
     IPageList pList = this.couponInfoService.list(qo);
     CommUtil.saveIPageList2ModelAndView("", "", "", pList, mv);
     return mv;
   }
 }



