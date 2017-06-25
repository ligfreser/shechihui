 package com.luxplat.manage.timer;
 
 import com.luxplat.core.tools.CommUtil;
 import com.luxplat.foundation.domain.BargainGoods;
 import com.luxplat.foundation.domain.Goods;
 import com.luxplat.foundation.domain.GoodsCart;
 import com.luxplat.foundation.domain.Message;
 import com.luxplat.foundation.domain.Store;
 import com.luxplat.foundation.domain.StoreCart;
 import com.luxplat.foundation.domain.SysConfig;
 import com.luxplat.foundation.domain.Template;
 import com.luxplat.foundation.domain.ZTCGoldLog;
 import com.luxplat.foundation.service.IBargainGoodsService;
 import com.luxplat.foundation.service.IGoodsCartService;
 import com.luxplat.foundation.service.IGoodsService;
 import com.luxplat.foundation.service.IMessageService;
 import com.luxplat.foundation.service.IStoreCartService;
 import com.luxplat.foundation.service.IStoreService;
 import com.luxplat.foundation.service.ISysConfigService;
 import com.luxplat.foundation.service.ITemplateService;
 import com.luxplat.foundation.service.IUserService;
 import com.luxplat.foundation.service.IZTCGoldLogService;
 import com.luxplat.lucene.LuceneThread;
 import com.luxplat.lucene.LuceneVo;
 import java.io.File;
 import java.util.ArrayList;
 import java.util.Calendar;
 import java.util.Date;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Component;
 
 @Component("shop_job")
 public class JobManageAction
 {
 
   @Autowired
   private IGoodsService goodsService;
 
   @Autowired
   private IZTCGoldLogService ztcGoldLogService;
 
   @Autowired
   private IStoreService storeService;
 
   @Autowired
   private ITemplateService templateService;
 
   @Autowired
   private IMessageService messageService;
 
   @Autowired
   private IUserService userService;
 
   @Autowired
   private ISysConfigService configService;
 
   @Autowired
   private IBargainGoodsService bargainGoodsService;
 
   @Autowired
   private IStoreCartService storeCartService;
 
   @Autowired
   private IGoodsCartService goodsCartService;
 
   public void execute()
   {
     Map params = new HashMap();
     params.put("ztc_status", Integer.valueOf(2));
     List goods_audit_list = this.goodsService.query(
       "select obj from Goods obj where obj.ztc_status=:ztc_status", 
       params, -1, -1);
     for (int i=0 ; i<goods_audit_list.size() ; i++) {
    	 Goods goods = (Goods) goods_audit_list.get(i) ;	
         if (goods.getZtc_begin_time().before(new Date())) {
             goods.setZtc_dredge_price(goods.getZtc_price());
             goods.setZtc_status(3);
             this.goodsService.update(goods);
           }
     }
     params.clear();
     params.put("ztc_status", Integer.valueOf(3));
     goods_audit_list = this.goodsService.query(
       "select obj from Goods obj where obj.ztc_status=:ztc_status", 
       params, -1, -1);

     for (int i=0 ; i<goods_audit_list.size() ; i++) {
    	 Goods tmp_goods = (Goods) goods_audit_list.get(i) ;	
         if (tmp_goods.getZtc_gold() > tmp_goods.getZtc_price()) {
      	   tmp_goods.setZtc_gold(tmp_goods.getZtc_gold() - tmp_goods.getZtc_price());
      	   tmp_goods.setZtc_dredge_price(tmp_goods.getZtc_price());
           this.goodsService.update(tmp_goods);
           ZTCGoldLog tmp_log = new ZTCGoldLog();
           tmp_log.setAddTime(new Date());
           tmp_log.setZgl_content("直通车消耗金币");
           tmp_log.setZgl_gold(tmp_goods.getZtc_price());
           tmp_log.setZgl_goods(tmp_goods);
           tmp_log.setZgl_type(1);
           this.ztcGoldLogService.save(tmp_log);
         } else {
      	   tmp_goods.setZtc_status(0);
      	   tmp_goods.setZtc_dredge_price(0);
      	   tmp_goods.setZtc_pay_status(0);
      	   tmp_goods.setZtc_apply_time(null);
           this.goodsService.update(tmp_goods);
         }
     }
 
     List stores = this.storeService.query(
       "select obj from Store obj where obj.validity is not null", 
       null, -1, -1);

     for (int i=0 ; i<stores.size() ; i++) {
    	 Store tmp_store = (Store) stores.get(i) ;
         if (tmp_store.getValidity().before(new Date())) {
      	   tmp_store.setStore_status(4);
           this.storeService.update(tmp_store);
           Template template = this.templateService.getObjByProperty(
             "mark", "msg_toseller_store_auto_closed_notify");
           if ((template != null) && (template.isOpen())) {
          	 Message msg = new Message();
             msg.setAddTime(new Date());
             msg.setContent(template.getContent());
             msg.setFromUser(this.userService.getObjByProperty(
               "userName", "admin"));
             msg.setStatus(0);
             msg.setTitle(template.getTitle());
             msg.setToUser(tmp_store.getUser());
             msg.setType(0);
             this.messageService.save(msg);
           }
         }
     }
 
     params.clear();
     params.put("goods_status", Integer.valueOf(0));
     List goods_list = this.goodsService
       .query(
       "select obj from Goods obj where obj.goods_status=:goods_status", 
       params, -1, -1);
     List goods_vo_list = new ArrayList();
     
     for (int i=0 ; i<goods_list.size() ; i++) {
    	 Goods tmp_goods = (Goods) goods_list.get(i) ;	 
         LuceneVo vo = new LuceneVo();
         vo.setVo_id(tmp_goods.getId());
         vo.setVo_title(tmp_goods.getGoods_name());
         vo.setVo_content(tmp_goods.getGoods_details());
         vo.setVo_type("goods");
         vo.setVo_store_price(CommUtil.null2Double(tmp_goods.getStore_price()));
         vo.setVo_add_time(tmp_goods.getAddTime().getTime());
         vo.setVo_goods_salenum(tmp_goods.getGoods_salenum());
         goods_vo_list.add(vo);
     }
     String goods_lucene_path = System.getProperty("user.dir") + 
       File.separator + "luence" + File.separator + "goods";
     File file = new File(goods_lucene_path);
     if (!file.exists()) {
       CommUtil.createFolder(goods_lucene_path);
     }
     LuceneThread goods_thread = new LuceneThread(goods_lucene_path, 
       goods_vo_list);
     goods_thread.run();
     SysConfig config = this.configService.getSysConfig();
     config.setLucene_update(new Date());
     this.configService.update(config);
 
     params.clear();
     Calendar cal = Calendar.getInstance();
     cal.add(6, -1);
     params.put("bg_time", CommUtil.formatDate(CommUtil.formatShortDate(cal
       .getTime())));
     List bgs = this.bargainGoodsService.query(
       "select obj from BargainGoods obj where obj.bg_time=:bg_time", 
       params, -1, -1);
     
     for (int i=0 ; i<bgs.size() ;i++) {
    	 BargainGoods tmp_bg = (BargainGoods) bgs.get(i) ;
    	 tmp_bg.setBg_status(-2);
         this.bargainGoodsService.update(tmp_bg);
         Goods goods = tmp_bg.getBg_goods();
         goods.setBargain_status(0);
         goods.setGoods_current_price(goods.getStore_price());
         this.goodsService.update(goods);
     }
 
     params.clear();
     params.put("bg_time", CommUtil.formatDate(
       CommUtil.formatShortDate(new Date())));
     params.put("bg_status", Integer.valueOf(1));
     bgs = this.bargainGoodsService
       .query(
       "select obj from BargainGoods obj where obj.bg_time=:bg_time and obj.bg_status=:bg_status", 
       params, -1, -1);
     Goods goods;
     for (int i=0 ; i<bgs.size() ;i++) {
    	 BargainGoods tmp_bg = (BargainGoods) bgs.get(i) ;
         goods = tmp_bg.getBg_goods();
         goods.setBargain_status(2);
         goods.setGoods_current_price(tmp_bg.getBg_price());
         this.goodsService.update(goods);
     }
 
     params.clear();
     cal = Calendar.getInstance();
     cal.add(6, -1);
     params.put("addTime", cal.getTime());
     params.put("sc_status", Integer.valueOf(0));
     List cart_list = this.storeCartService
       .query(
       "select obj from StoreCart obj where obj.user.id is null and obj.addTime<=:addTime and obj.sc_status=:sc_status", 
       params, -1, -1);
     for (int i=0 ; i<cart_list.size() ; i++) {
    	 StoreCart tmp_sc = (StoreCart) cart_list.get(i) ;
         for (GoodsCart gc : tmp_sc.getGcs()) {
             gc.getGsps().clear();
             this.goodsCartService.delete(gc.getId());
           }
           this.storeCartService.delete(tmp_sc.getId());
     }
 
     params.clear();
     cal = Calendar.getInstance();
     cal.add(6, -7);
     params.put("addTime", cal.getTime());
     params.put("sc_status", Integer.valueOf(0));
     cart_list = this.storeCartService
       .query(
       "select obj from StoreCart obj where obj.user.id is not null and obj.addTime<=:addTime and obj.sc_status=:sc_status", 
       params, -1, -1);
     for (int i=0 ; i<cart_list.size() ; i++) {
    	 StoreCart tmp_sc = (StoreCart) cart_list.get(i) ;
         for (GoodsCart gc : tmp_sc.getGcs()) {
             gc.getGsps().clear();
             this.goodsCartService.delete(gc.getId());
           }
           this.storeCartService.delete(tmp_sc.getId());
     }
   }
 }

