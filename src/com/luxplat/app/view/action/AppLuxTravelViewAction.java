/**
 * 
 */
package com.luxplat.app.view.action;

import com.luxplat.core.domain.IdEntity;
import com.luxplat.core.mv.JModelAndView;
import com.luxplat.core.security.support.SecurityUserHolder;
import com.luxplat.core.tools.CommUtil;
import com.luxplat.core.tools.Md5Encrypt;
import com.luxplat.foundation.domain.Goods;
import com.luxplat.foundation.domain.GoodsCart;
import com.luxplat.foundation.domain.Group;
import com.luxplat.foundation.domain.Store;
import com.luxplat.foundation.domain.StoreCart;
import com.luxplat.foundation.domain.SysConfig;
import com.luxplat.foundation.domain.User;
import com.luxplat.foundation.service.IAccessoryService;
import com.luxplat.foundation.service.IArticleClassService;
import com.luxplat.foundation.service.IArticleService;
import com.luxplat.foundation.service.IBargainGoodsService;
import com.luxplat.foundation.service.IDeliveryGoodsService;
import com.luxplat.foundation.service.IGoodsBrandService;
import com.luxplat.foundation.service.IGoodsCartService;
import com.luxplat.foundation.service.IGoodsClassService;
import com.luxplat.foundation.service.IGoodsFloorService;
import com.luxplat.foundation.service.IGoodsService;
import com.luxplat.foundation.service.IGroupGoodsService;
import com.luxplat.foundation.service.IGroupService;
import com.luxplat.foundation.service.IMessageService;
import com.luxplat.foundation.service.INavigationService;
import com.luxplat.foundation.service.IPartnerService;
import com.luxplat.foundation.service.IRoleService;
import com.luxplat.foundation.service.IStoreCartService;
import com.luxplat.foundation.service.IStoreService;
import com.luxplat.foundation.service.ISysConfigService;
import com.luxplat.foundation.service.IUserConfigService;
import com.luxplat.foundation.service.IUserService;
import com.luxplat.manage.admin.tools.MsgTools;
import com.luxplat.view.web.tools.GoodsFloorViewTools;
import com.luxplat.view.web.tools.GoodsViewTools;
import com.luxplat.view.web.tools.NavViewTools;
import com.luxplat.view.web.tools.StoreViewTools;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author elanhby
 *
 */
@Controller
public class AppLuxTravelViewAction
{

  @Autowired
  private ISysConfigService configService;

  @Autowired
  private IUserConfigService userConfigService;

  @Autowired
  private IGoodsClassService goodsClassService;

  @Autowired
  private IGoodsBrandService goodsBrandService;

  @Autowired
  private IPartnerService partnerService;

  @Autowired
  private IRoleService roleService;

  @Autowired
  private IUserService userService;

  @Autowired
  private IArticleClassService articleClassService;

  @Autowired
  private IArticleService articleService;

  @Autowired
  private IAccessoryService accessoryService;

  @Autowired
  private IMessageService messageService;

  @Autowired
  private IStoreService storeService;

  @Autowired
  private IGoodsService goodsService;

  @Autowired
  private INavigationService navigationService;

  @Autowired
  private IGroupGoodsService groupGoodsService;

  @Autowired
  private IGroupService groupService;

  @Autowired
  private IGoodsFloorService goodsFloorService;

  @Autowired
  private IBargainGoodsService bargainGoodsService;

  @Autowired
  private IDeliveryGoodsService deliveryGoodsService;

  @Autowired
  private IStoreCartService storeCartService;

  @Autowired
  private IGoodsCartService goodsCartService;

  @Autowired
  private NavViewTools navTools;

  @Autowired
  private GoodsViewTools goodsViewTools;

  @Autowired
  private StoreViewTools storeViewTools;

  @Autowired
  private MsgTools msgTools;

  @Autowired
  private GoodsFloorViewTools gf_tools;



  @RequestMapping({"/app/luxTravel.htm"})
  public ModelAndView luxTravel(HttpServletRequest request, HttpServletResponse response)
  {
    ModelAndView mv = new JModelAndView("luxTravel.html", 
      this.configService.getSysConfig(), 
      this.userConfigService.getUserConfig(), 2, request, response);

	  System.out.println("=========获取到app页面========"+mv.getViewName() + "=======");
	  
    Map params = new HashMap();
   
    params.put("store_recommend", Boolean.valueOf(true));
    params.put("goods_status", Integer.valueOf(0));
    List store_reommend_goods_list = this.goodsService
      .query("select obj from Goods obj where obj.store_recommend=:store_recommend and obj.goods_status=:goods_status order by obj.store_recommend_time desc", 
      params, -1, -1);
    List store_reommend_goods = new ArrayList();
    int max = 3;
    for (int i = 0; i < max; i++) {
      store_reommend_goods.add((Goods)store_reommend_goods_list.get(i));
    }
    mv.addObject("store_reommend_goods", store_reommend_goods);
    mv.addObject("store_reommend_goods_count", 
      Double.valueOf(Math.ceil(CommUtil.div(Integer.valueOf(store_reommend_goods_list.size()), Integer.valueOf(3)))));
    mv.addObject("goodsViewTools", this.goodsViewTools);
    mv.addObject("storeViewTools", this.storeViewTools);
    if (SecurityUserHolder.getCurrentUser() != null) {
      mv.addObject("user", this.userService.getObjById(
        SecurityUserHolder.getCurrentUser().getId()));
    }
    params.clear();
    
    //此处添加对最新上架商品的查询返回，对goods表进行查询，条件为goods_status='0' order by add_time desc 降序，选取前5个。
    params.put("goods_status", Integer.valueOf(0));
    List new_goods_list = this.goodsService
      .query("select obj from Goods obj where obj.goods_status=:goods_status order by obj.addTime desc", 
      params, -1, -1);
    List new_goods = new ArrayList();
    for (int i = 0; i < max; i++) {
 	   new_goods.add((Goods)new_goods_list.get(i));
    }
    mv.addObject("new_goods", new_goods);
    mv.addObject("new_goods_count", 
      Double.valueOf(Math.ceil(CommUtil.div(Integer.valueOf(new_goods_list.size()), Integer.valueOf(3)))));
    mv.addObject("goodsViewTools", this.goodsViewTools);
    mv.addObject("storeViewTools", this.storeViewTools);
    if (SecurityUserHolder.getCurrentUser() != null) {
      mv.addObject("user", this.userService.getObjById(
        SecurityUserHolder.getCurrentUser().getId()));
    }
    params.clear();
    //此处添加对经典箱包分类的查询反馈
    //此处添加对典藏腕表分类的查询反馈
    //此处添加对精品配饰分类的查询反馈
    //奢侈之旅页面暂时挂为空
    return mv;
  }

//  @RequestMapping({"/close.htm"})
//  public ModelAndView close(HttpServletRequest request, HttpServletResponse response)
//  {
//    ModelAndView mv = new JModelAndView("close.html", 
//      this.configService.getSysConfig(), 
//      this.userConfigService.getUserConfig(), 1, request, response);
//    return mv;
//  }
//
//  @RequestMapping({"/404.htm"})
//  public ModelAndView error404(HttpServletRequest request, HttpServletResponse response)
//  {
//    ModelAndView mv = new JModelAndView("404.html", 
//      this.configService.getSysConfig(), 
//      this.userConfigService.getUserConfig(), 1, request, response);
//    String luxplat_view_type = CommUtil.null2String(request.getSession(
//      false).getAttribute("luxplat_view_type"));
//    if ((luxplat_view_type != null) && (!luxplat_view_type.equals("")) && 
//      (luxplat_view_type.equals("weixin"))) {
//      String store_id = CommUtil.null2String(request
//        .getSession(false).getAttribute("store_id"));
//      mv = new JModelAndView("weixin/404.html", 
//        this.configService.getSysConfig(), 
//        this.userConfigService.getUserConfig(), 1, request, 
//        response);
//      mv.addObject("url", CommUtil.getURL(request) + 
//        "/weixin/index.htm?store_id=" + store_id);
//    }
//
//    return mv;
//  }
//
//  @RequestMapping({"/500.htm"})
//  public ModelAndView error500(HttpServletRequest request, HttpServletResponse response)
//  {
//    ModelAndView mv = new JModelAndView("500.html", 
//      this.configService.getSysConfig(), 
//      this.userConfigService.getUserConfig(), 1, request, response);
//    String luxplat_view_type = CommUtil.null2String(request.getSession(
//      false).getAttribute("luxplat_view_type"));
//    if ((luxplat_view_type != null) && (!luxplat_view_type.equals("")) && 
//      (luxplat_view_type.equals("weixin"))) {
//      String store_id = CommUtil.null2String(request
//        .getSession(false).getAttribute("store_id"));
//      mv = new JModelAndView("weixin/500.html", 
//        this.configService.getSysConfig(), 
//        this.userConfigService.getUserConfig(), 1, request, 
//        response);
//      mv.addObject("url", CommUtil.getURL(request) + 
//        "/weixin/index.htm?store_id=" + store_id);
//    }
//
//    return mv;
//  }

//  @RequestMapping({"/goods_class.htm"})
//  public ModelAndView goods_class(HttpServletRequest request, HttpServletResponse response)
//  {
//    ModelAndView mv = new JModelAndView("goods_class.html", 
//      this.configService.getSysConfig(), 
//      this.userConfigService.getUserConfig(), 1, request, response);
//    Map params = new HashMap();
//    params.put("display", Boolean.valueOf(true));
//    List gcs = this.goodsClassService
//      .query("select obj from GoodsClass obj where obj.parent.id is null and obj.display=:display order by obj.sequence asc", 
//      params, -1, -1);
//    mv.addObject("gcs", gcs);
//    return mv;
//  }



}
