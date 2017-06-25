 package com.luxplat.app.view.action;
 
 import com.luxplat.core.domain.virtual.SysMap;
import com.luxplat.core.mv.JModelAndView;
import com.luxplat.core.query.support.IPageList;
import com.luxplat.core.security.support.SecurityUserHolder;
import com.luxplat.core.tools.CommUtil;
import com.luxplat.foundation.domain.Area;
import com.luxplat.foundation.domain.Consult;
import com.luxplat.foundation.domain.Goods;
import com.luxplat.foundation.domain.GoodsBrand;
import com.luxplat.foundation.domain.GoodsClass;
import com.luxplat.foundation.domain.GoodsSpecProperty;
import com.luxplat.foundation.domain.GoodsSpecification;
import com.luxplat.foundation.domain.GoodsTypeProperty;
import com.luxplat.foundation.domain.Group;
import com.luxplat.foundation.domain.GroupGoods;
import com.luxplat.foundation.domain.Store;
import com.luxplat.foundation.domain.StoreClass;
import com.luxplat.foundation.domain.StorePoint;
import com.luxplat.foundation.domain.SysConfig;
import com.luxplat.foundation.domain.Transport;
import com.luxplat.foundation.domain.User;
import com.luxplat.foundation.domain.UserGoodsClass;
import com.luxplat.foundation.domain.query.ConsultQueryObject;
import com.luxplat.foundation.domain.query.EvaluateQueryObject;
import com.luxplat.foundation.domain.query.GoodsCartQueryObject;
import com.luxplat.foundation.domain.query.GoodsQueryObject;
import com.luxplat.foundation.service.IAreaService;
import com.luxplat.foundation.service.IConsultService;
import com.luxplat.foundation.service.IEvaluateService;
import com.luxplat.foundation.service.IGoodsBrandService;
import com.luxplat.foundation.service.IGoodsCartService;
import com.luxplat.foundation.service.IGoodsClassService;
import com.luxplat.foundation.service.IGoodsService;
import com.luxplat.foundation.service.IGoodsSpecPropertyService;
import com.luxplat.foundation.service.IGoodsTypePropertyService;
import com.luxplat.foundation.service.IOrderFormService;
import com.luxplat.foundation.service.IStoreClassService;
import com.luxplat.foundation.service.IStoreService;
import com.luxplat.foundation.service.ISysConfigService;
import com.luxplat.foundation.service.IUserConfigService;
import com.luxplat.foundation.service.IUserGoodsClassService;
import com.luxplat.foundation.service.IUserService;
import com.luxplat.manage.admin.tools.UserTools;
import com.luxplat.manage.seller.Tools.TransportTools;
import com.luxplat.view.web.tools.AreaViewTools;
import com.luxplat.view.web.tools.GoodsViewTools;
import com.luxplat.view.web.tools.IpAddress;
import com.luxplat.view.web.tools.StoreViewTools;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
 
 @Controller
 public class AppGoodsViewAction
 {
 
   @Autowired
   private ISysConfigService configService;

   @Autowired
   private IUserService userService;
   
   @Autowired
   private IUserConfigService userConfigService;
 
   @Autowired
   private IGoodsService goodsService;
 
   @Autowired
   private IGoodsClassService goodsClassService;
 
   @Autowired
   private IUserGoodsClassService userGoodsClassService;
 
   @Autowired
   private IStoreService storeService;
 
   @Autowired
   private IEvaluateService evaluateService;
 
   @Autowired
   private IOrderFormService orderFormService;
 
   @Autowired
   private IGoodsCartService goodsCartService;
 
   @Autowired
   private IConsultService consultService;
 
   @Autowired
   private IGoodsBrandService brandService;
 
   @Autowired
   private IGoodsSpecPropertyService goodsSpecPropertyService;
 
   @Autowired
   private IGoodsTypePropertyService goodsTypePropertyService;
 
   @Autowired
   private IAreaService areaService;
 
   @Autowired
   private IStoreClassService storeClassService;
 
   @Autowired
   private AreaViewTools areaViewTools;
 
   @Autowired
   private GoodsViewTools goodsViewTools;
 
   @Autowired
   private StoreViewTools storeViewTools;
 
   @Autowired
   private UserTools userTools;
 
   @Autowired
   private TransportTools transportTools;
 
   @RequestMapping({"/app/luxNewGoods.htm"})
   public ModelAndView newGoodsList(HttpServletRequest request, HttpServletResponse response)
   {
       ModelAndView mv = new JModelAndView("/luxNewGoods.html", 
         this.configService.getSysConfig(), 
         this.userConfigService.getUserConfig(), 2, request, 
         response);
       Map params = new HashMap();
       
       params.put("goods_status", Integer.valueOf(0));
       List new_goods_list = this.goodsService
         .query("select obj from Goods obj where obj.goods_status=:goods_status order by obj.addTime desc", 
         params, -1, -1);
       List new_goods = new ArrayList();
       for (int i = 0; i < new_goods_list.size(); i++) {
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
       return mv;
   }
   
   @RequestMapping({"/app/luxRecommend.htm"})
   public ModelAndView recoomendGoodsList(HttpServletRequest request, HttpServletResponse response)
   {
       ModelAndView mv = new JModelAndView("/luxRecommend.html", 
         this.configService.getSysConfig(), 
         this.userConfigService.getUserConfig(), 2, request, 
         response);
       Map params = new HashMap();
       
       params.put("store_recommend", Boolean.valueOf(true));
       params.put("goods_status", Integer.valueOf(0));
       List store_reommend_goods_list = this.goodsService
         .query("select obj from Goods obj where obj.store_recommend=:store_recommend and obj.goods_status=:goods_status order by obj.store_recommend_time desc", 
         params, -1, -1);
       List store_reommend_goods = new ArrayList();
       for (int i = 0; i < store_reommend_goods_list.size(); i++) {
         store_reommend_goods.add((Goods)store_reommend_goods_list.get(i));
       }
       mv.addObject("store_reommend_goods", store_reommend_goods);
       mv.addObject("store_reommend_goods_count", 
         Double.valueOf(Math.ceil(CommUtil.div(Integer.valueOf(store_reommend_goods_list.size()), Integer.valueOf(5)))));
       mv.addObject("goodsViewTools", this.goodsViewTools);
       mv.addObject("storeViewTools", this.storeViewTools);
       if (SecurityUserHolder.getCurrentUser() != null) {
         mv.addObject("user", this.userService.getObjById(
           SecurityUserHolder.getCurrentUser().getId()));
       }
       params.clear();
       return mv;
   }
 
   private Set<Long> genericUserGcIds(UserGoodsClass ugc)
   {
     Set ids = new HashSet();
     ids.add(ugc.getId());
     for (UserGoodsClass child : ugc.getChilds()) {
       Set cids = genericUserGcIds(child);
       for (int j=0 ; j<cids.toArray().length ; j++) {
           ids.add((Long)cids.toArray()[j]);
       }
       ids.add(child.getId());
     }
     return ids;
   }
 
   @RequestMapping({"/app/luxGoodsInfo.htm"})
   public ModelAndView goods_info(HttpServletRequest request, HttpServletResponse response, String id)
   {
     ModelAndView mv = null;
     Goods obj = this.goodsService.getObjById(Long.valueOf(Long.parseLong(id)));
     if (obj.getGoods_status() == 0) {
       String template = "default";
       if ((obj.getGoods_store().getTemplate() != null) && 
         (!obj.getGoods_store().getTemplate().equals(""))) {
         template = obj.getGoods_store().getTemplate();
       }
       mv = new JModelAndView("/luxGoodsInfo.html", 
         this.configService.getSysConfig(), 
         this.userConfigService.getUserConfig(), 2, request, 
         response);
       obj.setGoods_click(obj.getGoods_click() + 1);
       if ((this.configService.getSysConfig().isZtc_status()) && 
         (obj.getZtc_status() == 2)) {
         obj.setZtc_click_num(obj.getZtc_click_num() + 1);
       }
       if ((obj.getGroup() != null) && (obj.getGroup_buy() == 2)) {
         Group group = obj.getGroup();
         if (group.getEndTime().before(new Date())) {
           obj.setGroup(null);
           obj.setGroup_buy(0);
           obj.setGoods_current_price(obj.getStore_price());
         }
       }
 
       this.goodsService.update(obj);
       if (obj.getGoods_store().getStore_status() == 2) {
         mv.addObject("obj", obj);
         mv.addObject("store", obj.getGoods_store());
         Map params = new HashMap();
         params.put("user_id", obj.getGoods_store().getUser().getId());
         params.put("display", Boolean.valueOf(true));
         List ugcs = this.userGoodsClassService
           .query("select obj from UserGoodsClass obj where obj.user.id=:user_id and obj.display=:display and obj.parent.id is null order by obj.sequence asc", 
           params, -1, -1);
         mv.addObject("ugcs", ugcs);
         GoodsQueryObject gqo = new GoodsQueryObject();
         gqo.setPageSize(Integer.valueOf(4));
         gqo.addQuery("obj.goods_store.id", new SysMap("store_id", obj
           .getGoods_store().getId()), "=");
         gqo.addQuery("obj.goods_recommend", new SysMap(
           "goods_recommend", Boolean.valueOf(true)), "=");
         gqo.addQuery("obj.id", new SysMap("id", obj.getId()), "!=");
         gqo.setOrderBy("addTime");
         gqo.setOrderType("desc");
         gqo.addQuery("obj.goods_status", new SysMap("goods_status", Integer.valueOf(0)), 
           "=");
         mv.addObject("goods_recommend_list", this.goodsService
           .list(gqo).getResult());
         params.clear();
         params.put("goods_id", obj.getId());
         params.put("evaluate_type", "buyer");
         List evas = this.evaluateService
           .query("select obj from Evaluate obj where obj.evaluate_goods.id=:goods_id and obj.evaluate_type=:evaluate_type", 
           params, -1, -1);
         mv.addObject("eva_count", Integer.valueOf(evas.size()));
         mv.addObject("goodsViewTools", this.goodsViewTools);
         mv.addObject("storeViewTools", this.storeViewTools);
         mv.addObject("areaViewTools", this.areaViewTools);
         mv.addObject("transportTools", this.transportTools);
 
         List user_viewed_goods = (List)request
           .getSession(false).getAttribute("user_viewed_goods");
         if (user_viewed_goods == null) {
           user_viewed_goods = new ArrayList();
         }
         boolean add = true;
         for (int i=0 ; i < user_viewed_goods.size() ; i++ ) {
        	 Goods tmp_gds = (Goods)user_viewed_goods.get(i) ;
           if (tmp_gds.getId().equals(obj.getId())) {
             add = false;
             break;
           }
         }
         if (add) {
           if (user_viewed_goods.size() >= 4)
             user_viewed_goods.set(1, obj);
           else
             user_viewed_goods.add(obj);
         }
         request.getSession(false).setAttribute("user_viewed_goods", 
           user_viewed_goods);
 
         IpAddress ipAddr = IpAddress.getInstance();
         String current_ip = CommUtil.getIpAddr(request);
         String current_city = ipAddr.IpStringToAddress(current_ip);
         if ((current_city == null) || (current_city.equals(""))) {
           current_city = "全国";
         }
 
         mv.addObject("current_city", current_city);
 
         List areas = this.areaService
           .query("select obj from Area obj where obj.parent.id is null order by obj.sequence asc", 
           null, -1, -1);
         mv.addObject("areas", areas);
         generic_evaluate(obj.getGoods_store(), mv);
       } else {
         mv = new JModelAndView("error.html", 
           this.configService.getSysConfig(), 
           this.userConfigService.getUserConfig(), 2, request, 
           response);
         mv.addObject("op_title", "店铺够开通，拒绝访问");
         mv.addObject("url", CommUtil.getURL(request) + "/app/index.htm");
       }
     } else {
       mv = new JModelAndView("error.html", this.configService.getSysConfig(), 
         this.userConfigService.getUserConfig(), 2, request, 
         response);
       mv.addObject("op_title", "该商品未上架，不允许查看");
       mv.addObject("url", CommUtil.getURL(request) + "/app/index.htm");
     }
     return mv;
   }
 
 
 /*
   private Set<Long> getArrayAreaChildIds(List<Area> areas) {
     Set ids = new HashSet();
     Iterator localIterator2;
     for (Iterator localIterator1 = areas.iterator(); localIterator1.hasNext(); 
       localIterator2.hasNext())
     {
       Area area = (Area)localIterator1.next();
       ids.add(area.getId());
       localIterator2 = area.getChilds().iterator(); 
       Area are = (Area)localIterator2.next();
       Set cids = getAreaChildIds(are);
       for (int j=0 ; j<cids.toArray().length ; j++) {
           ids.add((Long)cids.toArray()[j]);
       }
     }
 
     return ids;
   }
 

 
   private Set<Long> getAreaChildIds(Area area) {
     Set ids = new HashSet();
     ids.add(area.getId());
     Iterator localIterator2;
     for (Iterator localIterator1 = area.getChilds().iterator(); localIterator1.hasNext(); 
       localIterator2.hasNext())
     {
       Area are = (Area)localIterator1.next();
       Set cids = getAreaChildIds(are);
       localIterator2 = cids.iterator(); 
       Long cid = (Long)localIterator2.next();
       ids.add(cid);
     }
 
     return ids;
   }
 
   private List<List<GoodsSpecProperty>> generic_gsp(String gs_ids) {
     List list = new ArrayList();
     String[] gs_id_list = gs_ids.substring(1).split("\\|");
     for (String gd_id_info : gs_id_list) {
       String[] gs_info_list = gd_id_info.split(",");
       GoodsSpecProperty gsp = this.goodsSpecPropertyService
         .getObjById(CommUtil.null2Long(gs_info_list[0]));
       boolean create = true;
       for (List gsp_list : (List[])list.toArray()) {
         for (GoodsSpecProperty gsp_temp : (GoodsSpecProperty[])gsp_list.toArray())
         {
           if (gsp_temp.getSpec().getId()
             .equals(gsp.getSpec().getId())) {
             gsp_list.add(gsp);
             create = false;
             break;
           }
         }
       }
       if (create) {
         List gsps = new ArrayList();
         gsps.add(gsp);
         list.add(gsps);
       }
     }
     return list;
   }
 
   @RequestMapping({"/goods_evaluation.htm"})
   public ModelAndView goods_evaluation(HttpServletRequest request, HttpServletResponse response, String id, String goods_id, String currentPage)
   {
     String template = "default";
     Store store = this.storeService.getObjById(CommUtil.null2Long(id));
     if (store != null) {
       template = store.getTemplate();
     }
     ModelAndView mv = new JModelAndView(
       template + "/goods_evaluation.html", 
       this.configService.getSysConfig(), 
       this.userConfigService.getUserConfig(), 1, request, response);
     EvaluateQueryObject qo = new EvaluateQueryObject(currentPage, mv, 
       "addTime", "desc");
     qo.addQuery("obj.evaluate_goods.id", 
       new SysMap("goods_id", CommUtil.null2Long(goods_id)), "=");
     qo.addQuery("obj.evaluate_type", new SysMap("evaluate_type", "goods"), 
       "=");
     qo.addQuery("obj.evaluate_status", new SysMap("evaluate_status", Integer.valueOf(0)), 
       "=");
     qo.setPageSize(Integer.valueOf(8));
     IPageList pList = this.evaluateService.list(qo);
     CommUtil.saveIPageList2ModelAndView(CommUtil.getURL(request) + 
       "/goods_evaluation.htm", "", "", pList, mv);
     mv.addObject("storeViewTools", this.storeViewTools);
     mv.addObject("store", store);
     Goods goods = this.goodsService
       .getObjById(CommUtil.null2Long(goods_id));
     mv.addObject("goods", goods);
     return mv;
   }
 
   @RequestMapping({"/goods_detail.htm"})
   public ModelAndView goods_detail(HttpServletRequest request, HttpServletResponse response, String id, String goods_id)
   {
     String template = "default";
     Store store = this.storeService.getObjById(CommUtil.null2Long(id));
     if (store != null) {
       template = store.getTemplate();
     }
     ModelAndView mv = new JModelAndView(template + "/goods_detail.html", 
       this.configService.getSysConfig(), 
       this.userConfigService.getUserConfig(), 1, request, response);
     Goods goods = this.goodsService
       .getObjById(CommUtil.null2Long(goods_id));
     mv.addObject("obj", goods);
     generic_evaluate(goods.getGoods_store(), mv);
     this.userTools.query_user();
     return mv;
   }
 
   @RequestMapping({"/goods_order.htm"})
   public ModelAndView goods_order(HttpServletRequest request, HttpServletResponse response, String id, String goods_id, String currentPage)
   {
     String template = "default";
     Store store = this.storeService.getObjById(CommUtil.null2Long(id));
     if (store != null) {
       template = store.getTemplate();
     }
     ModelAndView mv = new JModelAndView(template + "/goods_order.html", 
       this.configService.getSysConfig(), 
       this.userConfigService.getUserConfig(), 1, request, response);
     GoodsCartQueryObject qo = new GoodsCartQueryObject(currentPage, mv, 
       "addTime", "desc");
     qo.addQuery("obj.goods.id", 
       new SysMap("goods_id", CommUtil.null2Long(goods_id)), "=");
     qo.addQuery("obj.of.order_status", new SysMap("order_status", Integer.valueOf(20)), ">=");
     qo.setPageSize(Integer.valueOf(8));
     IPageList pList = this.goodsCartService.list(qo);
     CommUtil.saveIPageList2ModelAndView(CommUtil.getURL(request) + 
       "/goods_order.htm", "", "", pList, mv);
     mv.addObject("storeViewTools", this.storeViewTools);
     return mv;
   }
 
   @RequestMapping({"/goods_consult.htm"})
   public ModelAndView goods_consult(HttpServletRequest request, HttpServletResponse response, String id, String goods_id, String currentPage)
   {
     String template = "default";
     Store store = this.storeService.getObjById(CommUtil.null2Long(id));
     if (store != null) {
       template = store.getTemplate();
     }
     ModelAndView mv = new JModelAndView(template + "/goods_consult.html", 
       this.configService.getSysConfig(), 
       this.userConfigService.getUserConfig(), 1, request, response);
     ConsultQueryObject qo = new ConsultQueryObject(currentPage, mv, 
       "addTime", "desc");
     qo.addQuery("obj.goods.id", 
       new SysMap("goods_id", CommUtil.null2Long(goods_id)), "=");
     IPageList pList = this.consultService.list(qo);
     CommUtil.saveIPageList2ModelAndView(CommUtil.getURL(request) + 
       "/goods_consult.htm", "", "", pList, mv);
     mv.addObject("storeViewTools", this.storeViewTools);
     mv.addObject("goods_id", goods_id);
     return mv;
   }
 
   @RequestMapping({"/goods_consult_save.htm"})
   public ModelAndView goods_consult_save(HttpServletRequest request, HttpServletResponse response, String goods_id, String consult_content, String consult_email, String Anonymous, String consult_code)
   {
     ModelAndView mv = new JModelAndView(
       "user/default/usercenter/success.html", 
       this.configService.getSysConfig(), 
       this.userConfigService.getUserConfig(), 0, request, response);
     String verify_code = CommUtil.null2String(request.getSession(false)
       .getAttribute("consult_code"));
     boolean visit_consult = true;
     if (!this.configService.getSysConfig().isVisitorConsult()) {
       if (SecurityUserHolder.getCurrentUser() == null) {
         visit_consult = false;
       }
       if (CommUtil.null2Boolean(Anonymous)) {
         visit_consult = false;
       }
     }
     if (visit_consult) {
       if (CommUtil.null2String(consult_code).equals(verify_code)) {
         Consult obj = new Consult();
         obj.setAddTime(new Date());
         obj.setConsult_content(consult_content);
         obj.setConsult_email(consult_email);
         if (!CommUtil.null2Boolean(Anonymous)) {
           obj.setConsult_user(SecurityUserHolder.getCurrentUser());
           mv.addObject("op_title", "咨询发布成功");
         }
         obj.setGoods(this.goodsService.getObjById(
           CommUtil.null2Long(goods_id)));
         this.consultService.save(obj);
         request.getSession(false).removeAttribute("consult_code");
       } else {
         mv = new JModelAndView("error.html", 
           this.configService.getSysConfig(), 
           this.userConfigService.getUserConfig(), 1, request, 
           response);
         mv.addObject("op_title", "验证码错误，咨询发布失败");
       }
     } else {
       mv = new JModelAndView("error.html", this.configService.getSysConfig(), 
         this.userConfigService.getUserConfig(), 1, request, 
         response);
       mv.addObject("op_title", "不允许游客咨询");
     }
     mv.addObject("url", CommUtil.getURL(request) + "/goods_" + goods_id + 
       ".htm");
     return mv;
   }
 
   @RequestMapping({"/load_goods_gsp.htm"})
   public void load_goods_gsp(HttpServletRequest request, HttpServletResponse response, String gsp, String id)
   {
     Goods goods = this.goodsService.getObjById(CommUtil.null2Long(id));
     Map map = new HashMap();
     int count = 0;
     double price = 0.0D;
     if ((goods.getGroup() != null) && (goods.getGroup_buy() == 2)) {
       for (GroupGoods gg : goods.getGroup_goods_list())
         if (gg.getGroup().getId().equals(goods.getGroup().getId())) {
           count = gg.getGg_group_count() - gg.getGg_def_count();
           price = CommUtil.null2Double(gg.getGg_price());
         }
     }
     else {
       count = goods.getGoods_inventory();
       price = CommUtil.null2Double(goods.getStore_price());
       if (goods.getInventory_type().equals("spec")) {
         List list = (List)Json.fromJson(ArrayList.class, 
           goods.getGoods_inventory_detail());
         String[] gsp_ids = gsp.split(",");
         for (int i=0 ; i<list.size() ; i++) {
        	 Map temp = (Map)list.get(i) ;
           String[] temp_ids = CommUtil.null2String(temp.get("id"))
             .split("_");
           Arrays.sort(gsp_ids);
           Arrays.sort(temp_ids);
           if (Arrays.equals(gsp_ids, temp_ids)) {
             count = CommUtil.null2Int(temp.get("count"));
             price = CommUtil.null2Double(temp.get("price"));
           }
         }
       }
     }
     map.put("count", Integer.valueOf(count));
     map.put("price", Double.valueOf(price));
     response.setContentType("text/plain");
     response.setHeader("Cache-Control", "no-cache");
     response.setCharacterEncoding("UTF-8");
     try
     {
       PrintWriter writer = response.getWriter();
       writer.print(Json.toJson(map, JsonFormat.compact()));
     }
     catch (IOException e) {
       e.printStackTrace();
     }
   }
 
   @RequestMapping({"/trans_fee.htm"})
   public void trans_fee(HttpServletRequest request, HttpServletResponse response, String city_name, String goods_id)
   {
     Map map = new HashMap();
     Goods goods = this.goodsService
       .getObjById(CommUtil.null2Long(goods_id));
     float mail_fee = 0.0F;
     float express_fee = 0.0F;
     float ems_fee = 0.0F;
     if (goods.getTransport() != null) {
       mail_fee = this.transportTools.cal_goods_trans_fee(
         CommUtil.null2String(goods.getTransport().getId()), "mail", 
         CommUtil.null2String(goods.getGoods_weight()), 
         CommUtil.null2String(goods.getGoods_volume()), city_name);
       express_fee = this.transportTools.cal_goods_trans_fee(
         CommUtil.null2String(goods.getTransport().getId()), 
         "express", CommUtil.null2String(goods.getGoods_weight()), 
         CommUtil.null2String(goods.getGoods_volume()), city_name);
       ems_fee = this.transportTools.cal_goods_trans_fee(
         CommUtil.null2String(goods.getTransport().getId()), "ems", 
         CommUtil.null2String(goods.getGoods_weight()), 
         CommUtil.null2String(goods.getGoods_volume()), city_name);
     }
     map.put("mail_fee", Float.valueOf(mail_fee));
     map.put("express_fee", Float.valueOf(express_fee));
     map.put("ems_fee", Float.valueOf(ems_fee));
     map.put("current_city_info", CommUtil.substring(city_name, 5));
     response.setContentType("text/plain");
     response.setHeader("Cache-Control", "no-cache");
     response.setCharacterEncoding("UTF-8");
     try
     {
       PrintWriter writer = response.getWriter();
       writer.print(Json.toJson(map, JsonFormat.compact()));
     }
     catch (IOException e) {
       e.printStackTrace();
     }
   }
 
   @RequestMapping({"/goods_share.htm"})
   public ModelAndView goods_share(HttpServletRequest request, HttpServletResponse response, String goods_id)
   {
     ModelAndView mv = new JModelAndView("goods_share.html", 
       this.configService.getSysConfig(), 
       this.userConfigService.getUserConfig(), 1, request, response);
     Goods goods = this.goodsService
       .getObjById(CommUtil.null2Long(goods_id));
     mv.addObject("obj", goods);
     return mv;
   }
 
   private Set<Long> genericIds(GoodsClass gc) {
     Set ids = new HashSet();
     ids.add(gc.getId());
     List tmp_gc_childs = gc.getChilds() ;
     for (int i=0 ; i<tmp_gc_childs.size() ; i++) {
    	 GoodsClass child = (GoodsClass)tmp_gc_childs.get(i) ;
       Set cids = genericIds(child);
       for (int j=0 ; j<cids.toArray().length ; j++) {
         ids.add((Long)cids.toArray()[j]);
       }
       ids.add(child.getId());
     }
     return ids;
   } */
 
   private void generic_evaluate(Store store, ModelAndView mv) {
     double description_result = 0.0D;
     double service_result = 0.0D;
     double ship_result = 0.0D;
     if (store.getSc() != null) {
       StoreClass sc = this.storeClassService.getObjById(store.getSc()
         .getId());
       float description_evaluate = CommUtil.null2Float(sc
         .getDescription_evaluate());
       float service_evaluate = CommUtil.null2Float(sc
         .getService_evaluate());
       float ship_evaluate = CommUtil.null2Float(sc.getShip_evaluate());
       if (store.getPoint() != null) {
         float store_description_evaluate = CommUtil.null2Float(store
           .getPoint().getDescription_evaluate());
         float store_service_evaluate = CommUtil.null2Float(store
           .getPoint().getService_evaluate());
         float store_ship_evaluate = CommUtil.null2Float(store
           .getPoint().getShip_evaluate());
 
         description_result = CommUtil.div(Float.valueOf(store_description_evaluate - 
           description_evaluate), Float.valueOf(description_evaluate));
         service_result = CommUtil.div(Float.valueOf(store_service_evaluate - 
           service_evaluate), Float.valueOf(service_evaluate));
         ship_result = CommUtil.div(Float.valueOf(store_ship_evaluate - ship_evaluate), 
           Float.valueOf(ship_evaluate));
       }
     }
     if (description_result > 0.0D) {
       mv.addObject("description_css", "better");
       mv.addObject("description_type", "高于");
       mv.addObject("description_result", 
         CommUtil.null2String(Double.valueOf(CommUtil.mul(Double.valueOf(description_result), Integer.valueOf(100)))) + 
         "%");
     }
     if (description_result == 0.0D) {
       mv.addObject("description_css", "better");
       mv.addObject("description_type", "持平");
       mv.addObject("description_result", "-----");
     }
     if (description_result < 0.0D) {
       mv.addObject("description_css", "lower");
       mv.addObject("description_type", "低于");
       mv.addObject(
         "description_result", 
         CommUtil.null2String(Double.valueOf(CommUtil.mul(Double.valueOf(-description_result), Integer.valueOf(100)))) + 
         "%");
     }
     if (service_result > 0.0D) {
       mv.addObject("service_css", "better");
       mv.addObject("service_type", "高于");
       mv.addObject("service_result", 
         CommUtil.null2String(Double.valueOf(CommUtil.mul(Double.valueOf(service_result), Integer.valueOf(100)))) + 
         "%");
     }
     if (service_result == 0.0D) {
       mv.addObject("service_css", "better");
       mv.addObject("service_type", "持平");
       mv.addObject("service_result", "-----");
     }
     if (service_result < 0.0D) {
       mv.addObject("service_css", "lower");
       mv.addObject("service_type", "低于");
       mv.addObject("service_result", 
         CommUtil.null2String(Double.valueOf(CommUtil.mul(Double.valueOf(-service_result), Integer.valueOf(100)))) + 
         "%");
     }
     if (ship_result > 0.0D) {
       mv.addObject("ship_css", "better");
       mv.addObject("ship_type", "高于");
       mv.addObject("ship_result", 
         CommUtil.null2String(Double.valueOf(CommUtil.mul(Double.valueOf(ship_result), Integer.valueOf(100)))) + "%");
     }
     if (ship_result == 0.0D) {
       mv.addObject("ship_css", "better");
       mv.addObject("ship_type", "持平");
       mv.addObject("ship_result", "-----");
     }
     if (ship_result < 0.0D) {
       mv.addObject("ship_css", "lower");
       mv.addObject("ship_type", "低于");
       mv.addObject("ship_result", 
         CommUtil.null2String(Double.valueOf(CommUtil.mul(Double.valueOf(-ship_result), Integer.valueOf(100)))) + "%");
     }
   }
 }
