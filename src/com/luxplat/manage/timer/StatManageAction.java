 package com.luxplat.manage.timer;
 
 import com.luxplat.core.security.support.SecurityUserHolder;
 import com.luxplat.core.tools.CommUtil;
 import com.luxplat.foundation.domain.Activity;
 import com.luxplat.foundation.domain.ActivityGoods;
 import com.luxplat.foundation.domain.DeliveryGoods;
 import com.luxplat.foundation.domain.Evaluate;
 import com.luxplat.foundation.domain.Goods;
 import com.luxplat.foundation.domain.Group;
 import com.luxplat.foundation.domain.GroupGoods;
 import com.luxplat.foundation.domain.MobileVerifyCode;
 import com.luxplat.foundation.domain.OrderForm;
 import com.luxplat.foundation.domain.OrderFormLog;
 import com.luxplat.foundation.domain.Payment;
 import com.luxplat.foundation.domain.PredepositLog;
 import com.luxplat.foundation.domain.Store;
 import com.luxplat.foundation.domain.StoreClass;
 import com.luxplat.foundation.domain.StorePoint;
 import com.luxplat.foundation.domain.StoreStat;
 import com.luxplat.foundation.domain.SysConfig;
 import com.luxplat.foundation.domain.User;
 import com.luxplat.foundation.service.IActivityGoodsService;
 import com.luxplat.foundation.service.IActivityService;
 import com.luxplat.foundation.service.IDeliveryGoodsService;
 import com.luxplat.foundation.service.IEvaluateService;
 import com.luxplat.foundation.service.IGoodsService;
 import com.luxplat.foundation.service.IGroupGoodsService;
 import com.luxplat.foundation.service.IGroupService;
 import com.luxplat.foundation.service.IMobileVerifyCodeService;
 import com.luxplat.foundation.service.IOrderFormLogService;
 import com.luxplat.foundation.service.IOrderFormService;
 import com.luxplat.foundation.service.IPaymentService;
 import com.luxplat.foundation.service.IPredepositLogService;
 import com.luxplat.foundation.service.IStoreClassService;
 import com.luxplat.foundation.service.IStorePointService;
 import com.luxplat.foundation.service.IStoreService;
 import com.luxplat.foundation.service.IStoreStatService;
 import com.luxplat.foundation.service.ISysConfigService;
 import com.luxplat.foundation.service.ITemplateService;
 import com.luxplat.foundation.service.IUserService;
 import com.luxplat.manage.admin.tools.MsgTools;
 import com.luxplat.manage.admin.tools.StatTools;
 import java.io.File;
 import java.io.FileOutputStream;
 import java.io.OutputStreamWriter;
 import java.io.PrintWriter;
 import java.io.StringWriter;
 import java.math.BigDecimal;
 import java.text.DecimalFormat;
 import java.util.Calendar;
 import java.util.Date;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;
 import java.util.Properties;
 import org.apache.velocity.VelocityContext;
 import org.apache.velocity.app.Velocity;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Component;
 
 @Component("shop_stat")
 public class StatManageAction
 {
 
   @Autowired
   private IStoreStatService storeStatService;
 
   @Autowired
   private StatTools statTools;
 
   @Autowired
   private IMobileVerifyCodeService mobileverifycodeService;
 
   @Autowired
   private IStoreService storeService;
 
   @Autowired
   private IEvaluateService evaluateService;
 
   @Autowired
   private IStorePointService storePointService;
 
   @Autowired
   private IGroupService groupService;
 
   @Autowired
   private IOrderFormService orderFormService;
 
   @Autowired
   private IOrderFormLogService orderFormLogService;
 
   @Autowired
   private IPaymentService paymentService;
 
   @Autowired
   private IPredepositLogService predepositLogService;
 
   @Autowired
   private ISysConfigService configService;
 
   @Autowired
   private IUserService userService;
 
   @Autowired
   private ITemplateService templateService;
 
   @Autowired
   private IActivityService activityService;
 
   @Autowired
   private IGoodsService goodsService;
 
   @Autowired
   private IDeliveryGoodsService deliveryGoodsService;
 
   @Autowired
   private IStoreClassService storeClassService;
 
   @Autowired
   private IActivityGoodsService activityGoodsService;
 
   @Autowired
   private IGroupGoodsService groupGoodsService;
 
   @Autowired
   private MsgTools msgTools;
 
   private void execute()
     throws Exception
   {
     List stats = this.storeStatService.query(
       "select obj from StoreStat obj", null, -1, -1);
     StoreStat stat = null;
     if (stats.size() > 0)
       stat = (StoreStat)stats.get(0);
     else {
       stat = new StoreStat();
     }
     stat.setAddTime(new Date());
     Calendar cal = Calendar.getInstance();
     cal.add(12, 30);
     stat.setNext_time(cal.getTime());
     stat.setWeek_complaint(this.statTools.query_complaint(-7));
     stat.setWeek_goods(this.statTools.query_goods(-7));
     stat.setWeek_order(this.statTools.query_order(-7));
     stat.setWeek_report(this.statTools.query_report(-7));
     stat.setWeek_store(this.statTools.query_store(-7));
     stat.setWeek_user(this.statTools.query_user(-7));
     stat.setAll_goods(this.statTools.query_all_goods());
     stat.setAll_store(this.statTools.query_all_store());
     stat.setAll_user(this.statTools.query_all_user());
     stat.setStore_update(this.statTools.query_update_store());
     stat.setOrder_amount(BigDecimal.valueOf(this.statTools
       .query_all_amount()));
     if (stats.size() > 0)
       this.storeStatService.update(stat);
     else {
       this.storeStatService.save(stat);
     }
     cal.setTime(new Date());
     cal.add(12, -15);
     Map params = new HashMap();
     params.put("time", cal.getTime());
     List mvcs = this.mobileverifycodeService
       .query(
       "select obj from MobileVerifyCode obj where obj.addTime<=:time", 
       params, -1, -1);
     for (int i = 0 ; i<mvcs.size() ; i++) {
    	 MobileVerifyCode mvc = (MobileVerifyCode) mvcs.get(i) ;
         this.mobileverifycodeService.delete(mvc.getId());
     }
 
     List stores = this.storeService.query(
       "select obj from Store obj", null, -1, -1);
     List evas;
     double service_evaluate;
     double ship_evaluate;
     DecimalFormat df;
     for (int i=0 ; i<stores.size() ; i++) {
    	 Store store = (Store) stores.get(i) ;
       params.clear();
       params.put("store_id", store.getId());
       evas = this.evaluateService
         .query(
         "select obj from Evaluate obj where obj.of.store.id=:store_id", 
         params, -1, -1);
       double store_evaluate1 = 0.0D;
       double store_evaluate1_total = 0.0D;
       double description_evaluate = 0.0D;
       double description_evaluate_total = 0.0D;
       service_evaluate = 0.0D;
       double service_evaluate_total = 0.0D;
       ship_evaluate = 0.0D;
       double ship_evaluate_total = 0.0D;
       df = new DecimalFormat("0.0");
       for (int i1=0 ; i1<evas.size() ; i1++) {
    		Evaluate eva1 = (Evaluate) evas.get(i1) ;
            store_evaluate1_total = store_evaluate1_total + 
                    eva1.getEvaluate_buyer_val();
          
	          description_evaluate_total = description_evaluate_total + 
	            CommUtil.null2Double(eva1.getDescription_evaluate());
	  
	          service_evaluate_total = service_evaluate_total + 
	            CommUtil.null2Double(eva1.getService_evaluate());
	  
	          ship_evaluate_total = ship_evaluate_total + 
	            CommUtil.null2Double(eva1.getShip_evaluate());
       }
       store_evaluate1 = CommUtil.null2Double(df
         .format(store_evaluate1_total / evas.size()));
       description_evaluate = CommUtil.null2Double(df
         .format(description_evaluate_total / evas.size()));
       service_evaluate = CommUtil.null2Double(df
         .format(service_evaluate_total / evas.size()));
       ship_evaluate = CommUtil.null2Double(df.format(ship_evaluate_total / 
         evas.size()));
       double description_evaluate_halfyear = 0.0D;
       double service_evaluate_halfyear = 0.0D;
       double ship_evaluate_halfyear = 0.0D;
       int description_evaluate_halfyear_count5 = 0;
       int description_evaluate_halfyear_count4 = 0;
       int description_evaluate_halfyear_count3 = 0;
       int description_evaluate_halfyear_count2 = 0;
       int description_evaluate_halfyear_count1 = 0;
       int service_evaluate_halfyear_count5 = 0;
       int service_evaluate_halfyear_count4 = 0;
       int service_evaluate_halfyear_count3 = 0;
       int service_evaluate_halfyear_count2 = 0;
       int service_evaluate_halfyear_count1 = 0;
       int ship_evaluate_halfyear_count5 = 0;
       int ship_evaluate_halfyear_count4 = 0;
       int ship_evaluate_halfyear_count3 = 0;
       int ship_evaluate_halfyear_count2 = 0;
       int ship_evaluate_halfyear_count1 = 0;
       Calendar cal1 = Calendar.getInstance();
       cal1.add(2, -6);
       params.clear();
       params.put("store_id", store.getId());
       params.put("addTime", cal1.getTime());
       evas = this.evaluateService
         .query(
         "select obj from Evaluate obj where obj.of.store.id=:store_id and obj.addTime>=:addTime", 
         params, -1, -1);
       for (int j=0 ; j<evas.size() ; j++) {
    		Evaluate eva = (Evaluate) evas.get(j) ;
         description_evaluate_halfyear = description_evaluate_halfyear + 
           CommUtil.null2Double(eva.getDescription_evaluate());
 
         service_evaluate_halfyear = service_evaluate_halfyear + 
           CommUtil.null2Double(eva.getService_evaluate());
 
         ship_evaluate_halfyear = ship_evaluate_halfyear + 
           CommUtil.null2Double(eva.getService_evaluate());
         if (CommUtil.null2Double(eva.getDescription_evaluate()) >= 4.0D) {
           description_evaluate_halfyear_count5++;
         }
         if ((CommUtil.null2Double(eva.getDescription_evaluate()) >= 3.0D) && 
           (CommUtil.null2Double(eva.getDescription_evaluate()) < 4.0D)) {
           description_evaluate_halfyear_count4++;
         }
         if ((CommUtil.null2Double(eva.getDescription_evaluate()) >= 2.0D) && 
           (CommUtil.null2Double(eva.getDescription_evaluate()) < 3.0D)) {
           description_evaluate_halfyear_count3++;
         }
         if ((CommUtil.null2Double(eva.getDescription_evaluate()) >= 1.0D) && 
           (CommUtil.null2Double(eva.getDescription_evaluate()) < 2.0D)) {
           description_evaluate_halfyear_count2++;
         }
         if ((CommUtil.null2Double(eva.getDescription_evaluate()) >= 0.0D) && 
           (CommUtil.null2Double(eva.getDescription_evaluate()) < 1.0D)) {
           description_evaluate_halfyear_count1++;
         }
         if (CommUtil.null2Double(eva.getService_evaluate()) >= 4.0D) {
           service_evaluate_halfyear_count5++;
         }
         if ((CommUtil.null2Double(eva.getService_evaluate()) >= 3.0D) && 
           (CommUtil.null2Double(eva.getService_evaluate()) < 4.0D)) {
           service_evaluate_halfyear_count4++;
         }
         if ((CommUtil.null2Double(eva.getService_evaluate()) >= 2.0D) && 
           (CommUtil.null2Double(eva.getService_evaluate()) < 3.0D)) {
           service_evaluate_halfyear_count3++;
         }
         if ((CommUtil.null2Double(eva.getService_evaluate()) >= 1.0D) && 
           (CommUtil.null2Double(eva.getService_evaluate()) < 2.0D)) {
           service_evaluate_halfyear_count2++;
         }
         if ((CommUtil.null2Double(eva.getService_evaluate()) >= 0.0D) && 
           (CommUtil.null2Double(eva.getService_evaluate()) < 1.0D)) {
           service_evaluate_halfyear_count1++;
         }
         if (CommUtil.null2Double(eva.getShip_evaluate()) >= 4.0D) {
           ship_evaluate_halfyear_count5++;
         }
         if ((CommUtil.null2Double(eva.getShip_evaluate()) >= 3.0D) && 
           (CommUtil.null2Double(eva.getShip_evaluate()) < 4.0D)) {
           ship_evaluate_halfyear_count4++;
         }
         if ((CommUtil.null2Double(eva.getShip_evaluate()) >= 2.0D) && 
           (CommUtil.null2Double(eva.getShip_evaluate()) < 3.0D)) {
           ship_evaluate_halfyear_count3++;
         }
         if ((CommUtil.null2Double(eva.getShip_evaluate()) >= 1.0D) && 
           (CommUtil.null2Double(eva.getShip_evaluate()) < 2.0D)) {
           ship_evaluate_halfyear_count2++;
         }
         if ((CommUtil.null2Double(eva.getShip_evaluate()) >= 0.0D) && 
           (CommUtil.null2Double(eva.getShip_evaluate()) < 1.0D)) {
           ship_evaluate_halfyear_count1++;
         }
       }
       if (evas.size() > 0)
       {
         description_evaluate_halfyear = description_evaluate_halfyear / 
           evas.size();
 
         service_evaluate_halfyear = service_evaluate_halfyear / 
           evas.size();
         ship_evaluate_halfyear /= evas.size();
       }
       params.clear();
       params.put("store_id", store.getId());
       List sps = this.storePointService
         .query(
         "select obj from StorePoint obj where obj.store.id=:store_id", 
         params, -1, -1);
       StorePoint point = null;
       if (sps.size() > 0)
         point = (StorePoint)sps.get(0);
       else {
         point = new StorePoint();
       }
       point.setStatTime(new Date());
       point.setStore(store);
       point.setDescription_evaluate(
         BigDecimal.valueOf(description_evaluate));
       point.setService_evaluate(BigDecimal.valueOf(service_evaluate));
       point.setShip_evaluate(BigDecimal.valueOf(ship_evaluate));
       point.setStore_evaluate1(BigDecimal.valueOf(store_evaluate1));
       point.setDescription_evaluate_halfyear(
         BigDecimal.valueOf(description_evaluate_halfyear));
       point
         .setDescription_evaluate_halfyear_count1(description_evaluate_halfyear_count1);
       point
         .setDescription_evaluate_halfyear_count2(description_evaluate_halfyear_count2);
       point
         .setDescription_evaluate_halfyear_count3(description_evaluate_halfyear_count3);
       point
         .setDescription_evaluate_halfyear_count4(description_evaluate_halfyear_count4);
       point
         .setDescription_evaluate_halfyear_count5(description_evaluate_halfyear_count5);
       point.setService_evaluate_halfyear(
         BigDecimal.valueOf(service_evaluate_halfyear));
       point
         .setService_evaluate_halfyear_count1(service_evaluate_halfyear_count1);
       point
         .setService_evaluate_halfyear_count2(service_evaluate_halfyear_count2);
       point
         .setService_evaluate_halfyear_count3(service_evaluate_halfyear_count3);
       point
         .setService_evaluate_halfyear_count4(service_evaluate_halfyear_count4);
       point
         .setService_evaluate_halfyear_count5(service_evaluate_halfyear_count5);
       point.setShip_evaluate_halfyear(BigDecimal.valueOf(ship_evaluate));
       point
         .setShip_evaluate_halfyear_count1(ship_evaluate_halfyear_count1);
       point
         .setShip_evaluate_halfyear_count2(ship_evaluate_halfyear_count2);
       point
         .setShip_evaluate_halfyear_count3(ship_evaluate_halfyear_count3);
       point
         .setShip_evaluate_halfyear_count4(ship_evaluate_halfyear_count4);
       point
         .setShip_evaluate_halfyear_count5(ship_evaluate_halfyear_count5);
       if (sps.size() > 0)
         this.storePointService.update(point);
       else {
         this.storePointService.save(point);
       }
     }
 
     List scs = this.storeClassService.query(
       "select obj from StoreClass obj", null, -1, -1);
     double description_evaluate;
     service_evaluate = 0.00D;
     for (int j=0 ; j<scs.size() ; j++) {
    	 StoreClass sc = (StoreClass) scs.get(j) ;
         description_evaluate = 0.0D;
         service_evaluate = 0.0D;
         ship_evaluate = 0.0D;
         params.clear();
         params.put("sc_id", sc.getId());
         List sp_list = this.storePointService
           .query(
           "select obj from StorePoint obj where obj.store.sc.id=:sc_id", 
           params, -1, -1);
         for (int i=0 ;i<sp_list.size() ; i++) {
        	 StorePoint sp = (StorePoint) sp_list.get(i) ;
             description_evaluate = CommUtil.add(Double.valueOf(description_evaluate), sp
                     .getDescription_evaluate());
                   service_evaluate = CommUtil.add(Double.valueOf(service_evaluate), sp
                     .getService_evaluate());
                   ship_evaluate = CommUtil.add(Double.valueOf(ship_evaluate), sp
                     .getShip_evaluate());
         }
         sc.setDescription_evaluate(BigDecimal.valueOf(CommUtil.div(
           Double.valueOf(description_evaluate), Integer.valueOf(sp_list.size()))));
         sc.setService_evaluate(BigDecimal.valueOf(CommUtil.div(
           Double.valueOf(service_evaluate), Integer.valueOf(sp_list.size()))));
         sc.setShip_evaluate(BigDecimal.valueOf(CommUtil.div(Double.valueOf(ship_evaluate), 
           Integer.valueOf(sp_list.size()))));
         this.storeClassService.update(sc);
     }
 
     List groups = this.groupService.query(
       "select obj from Group obj order by obj.addTime", null, -1, -1);
     Goods goods;
     for (int i=0 ; i<groups.size() ; i++) {
    	 Group group = (Group) groups.get(i);
       if ((group.getBeginTime().before(new Date())) && 
                 (group.getEndTime().after(new Date()))) {
         group.setStatus(0);
         this.groupService.update(group);
       }
       if (group.getEndTime().before(new Date())) {
         group.setStatus(-2);
         this.groupService.update(group);
         for (Iterator<GroupGoods> service_evaluate1 = group.getGg_list().iterator(); service_evaluate1.hasNext(); ) { 
        	 GroupGoods gg = (GroupGoods)service_evaluate1.next();
           gg.setGg_status(-2);
           this.groupGoodsService.update(gg);
           goods = gg.getGg_goods();
           goods.setGroup_buy(0);
           goods.setGoods_current_price(goods.getStore_price());
           this.goodsService.update(goods);
         }
       }
     }
 
     params.clear();
     params.put("ac_end_time", new Date());
     params.put("ac_status", Integer.valueOf(1));
     List acts = this.activityService
       .query(
       "select obj from Activity obj where obj.ac_end_time<=:ac_end_time and obj.ac_status=:ac_status", 
       params, -1, -1);
     
     for (Iterator tmp_act = acts.iterator(); tmp_act.hasNext();)
     {
       Activity act = (Activity)tmp_act.next();
       act.setAc_status(0);
       this.activityService.update(act);
       Iterator tmp_goods_list = act.getAgs().iterator(); 
       ActivityGoods ac = (ActivityGoods) tmp_goods_list.next();
       ac.setAg_status(-2);
       this.activityGoodsService.update(ac);
       Goods goods1 = ac.getAg_goods();
       goods1.setActivity_status(0);
       goods1.setGoods_current_price(goods1.getStore_price());
       this.goodsService.update(goods1);
     }
 
     int auto_order_notice = this.configService.getSysConfig()
       .getAuto_order_notice();
     cal = Calendar.getInstance();
     params.clear();
     cal.add(6, -auto_order_notice);
     params.put("shipTime", cal.getTime());
     params.put("auto_confirm_email", Boolean.valueOf(true));
     params.put("auto_confirm_sms", Boolean.valueOf(true));
     List notice_ofs = this.orderFormService
       .query(
       "select obj from OrderForm obj where obj.shipTime<=:shipTime and (obj.auto_confirm_email=:auto_confirm_email or obj.auto_confirm_sms=:auto_confirm_sms)", 
       params, -1, -1);
     for (int i=0 ; i<notice_ofs.size() ;i++) {
    		OrderForm of = (OrderForm) notice_ofs.get(i) ; 
       if (!of.isAuto_confirm_email()) {
         boolean email = send_email(of, 
           "email_tobuyer_order_will_confirm_notify");
         if (email) {
           of.setAuto_confirm_email(true);
           this.orderFormService.update(of);
         }
       }
       if (!of.isAuto_confirm_sms()) {
         boolean sms = send_sms(of, of.getUser().getMobile(), 
           "sms_tobuyer_order_will_confirm_notify");
         if (sms) {
           of.setAuto_confirm_sms(true);
           this.orderFormService.update(of);
         }
       }
     }
 
     int auto_order_confirm = this.configService.getSysConfig()
       .getAuto_order_confirm();
     cal = Calendar.getInstance();
     params.clear();
     cal.add(6, -auto_order_confirm);
     params.put("shipTime", cal.getTime());
     List confirm_ofs = this.orderFormService.query(
       "select obj from OrderForm obj where obj.shipTime<=:shipTime", 
       params, -1, -1);
     OrderFormLog ofl;
     PredepositLog log;
     for (int i=0 ; i<confirm_ofs.size() ;i++) {
    		OrderForm of = (OrderForm) confirm_ofs.get(i) ; 
       of.setOrder_status(40);
       boolean ret = this.orderFormService.update(of);
       if (ret) {
         ofl = new OrderFormLog();
         ofl.setAddTime(new Date());
         ofl.setLog_info("确认收货");
         ofl.setLog_user(SecurityUserHolder.getCurrentUser());
         ofl.setOf(of);
         this.orderFormLogService.save(ofl);
         if (this.configService.getSysConfig().isEmailEnable()) {
           send_email(of, 
             "email_toseller_order_receive_ok_notify");
         }
         if (this.configService.getSysConfig().isSmsEnbale()) {
           send_sms(of, of.getStore().getUser().getMobile(), 
             "sms_toseller_order_receive_ok_notify");
         }
         if (of.getPayment().getMark().equals("balance")) {
           User seller = this.userService.getObjById(of.getStore()
             .getUser().getId());
           if (this.configService.getSysConfig().getBalance_fenrun() == 1)
           {
             params.clear();
             params.put("type", "admin");
             params.put("mark", "alipay");
             List payments = this.paymentService
               .query(
               "select obj from Payment obj where obj.type=:type and obj.mark=:mark", 
               params, -1, -1);
             Payment shop_payment = new Payment();
             if (payments.size() > 0) {
               shop_payment = (Payment)payments.get(0);
             }
 
             double shop_availableBalance = CommUtil.null2Double(of
               .getTotalPrice()) * 
               CommUtil.null2Double(shop_payment
               .getBalance_divide_rate());
             User admin = this.userService.getObjByProperty(
               "userName", "admin");
             admin.setAvailableBalance(BigDecimal.valueOf(
               CommUtil.add(admin.getAvailableBalance(), 
               Double.valueOf(shop_availableBalance))));
             this.userService.update(admin);
             PredepositLog log1 = new PredepositLog();
             log1.setAddTime(new Date());
             log1.setPd_log_user(seller);
             log1.setPd_op_type("分润");
             log1.setPd_log_amount(
               BigDecimal.valueOf(shop_availableBalance));
             log1.setPd_log_info("自动确认收货平台分润获得预存款，订单" + 
               of.getOrder_id());
             log1.setPd_type("可用预存款");
             this.predepositLogService.save(log1);
 
             double seller_availableBalance = 
               CommUtil.null2Double(of.getTotalPrice()) - 
               shop_availableBalance;
             seller.setAvailableBalance(BigDecimal.valueOf(
               CommUtil.add(seller.getAvailableBalance(), 
               Double.valueOf(seller_availableBalance))));
             this.userService.update(seller);
             PredepositLog log11 = new PredepositLog();
             log11.setAddTime(new Date());
             log11.setPd_log_user(seller);
             log11.setPd_op_type("增加");
             log11.setPd_log_amount(
               BigDecimal.valueOf(seller_availableBalance));
             log11.setPd_log_info("自动确认收货增加预存款，订单号" + 
               of.getOrder_id());
             log11.setPd_type("可用预存款");
             this.predepositLogService.save(log11);
 
             User buyer = of.getUser();
             buyer.setFreezeBlance(BigDecimal.valueOf(
               CommUtil.subtract(buyer.getFreezeBlance(), of
               .getTotalPrice())));
             this.userService.update(buyer);
           }
           else {
             seller.setAvailableBalance(BigDecimal.valueOf(
               CommUtil.add(seller.getAvailableBalance(), of
               .getTotalPrice())));
             this.userService.update(seller);
             log = new PredepositLog();
             log.setAddTime(new Date());
             log.setPd_log_user(seller);
             log.setPd_op_type("增加");
             log.setPd_log_amount(of.getTotalPrice());
             log.setPd_log_info("自动确认收货增加预存款,订单号" + of.getOrder_id());
 
             log.setPd_type("可用预存款");
             this.predepositLogService.save(log);
 
             User buyer = of.getUser();
             buyer.setFreezeBlance(BigDecimal.valueOf(
               CommUtil.subtract(buyer.getFreezeBlance(), of
               .getTotalPrice())));
             this.userService.update(buyer);
           }
         }
       }
     }
 
     int auto_order_evaluate = this.configService.getSysConfig()
       .getAuto_order_evaluate();
     cal = Calendar.getInstance();
     params.clear();
     cal.add(6, -auto_order_evaluate);
     params.put("auto_order_evaluate", cal.getTime());
     params.put("order_status_40", Integer.valueOf(40));
     params.put("order_status_47", Integer.valueOf(47));
     params.put("order_status_48", Integer.valueOf(48));
     params.put("order_status_49", Integer.valueOf(49));
     params.put("order_status_50", Integer.valueOf(50));
     params.put("order_status_60", Integer.valueOf(60));
     List confirm_evaluate_ofs = this.orderFormService
       .query(
       "select obj from OrderForm obj where obj.return_shipTime<=:return_shipTime and obj.order_status>=:order_status_40 and obj.order_status!=:order_status_47 and obj.order_status!=:order_status_48 and obj.order_status!=:order_status_49 and obj.order_status!=:order_status_50 and obj.order_status!=:order_status_60", 
       params, -1, -1);
     for (int i=0 ; i<confirm_evaluate_ofs.size() ;i++) {
		OrderForm order = (OrderForm) confirm_evaluate_ofs.get(i) ; 
       order.setOrder_status(65);
       this.orderFormService.update(order);
     }
 
     int auto_order_return = this.configService.getSysConfig()
       .getAuto_order_return();
     cal = Calendar.getInstance();
     params.clear();
     cal.add(6, -auto_order_return);
     params.put("return_shipTime", cal.getTime());
     params.put("order_status", Integer.valueOf(46));
     List confirm_return_ofs = this.orderFormService
       .query(
       "select obj from OrderForm obj where obj.return_shipTime<=:return_shipTime and obj.order_status=:order_status", 
       params, -1, -1);
     for (int i=0 ; i<confirm_return_ofs.size() ;i++) {
		OrderForm order = (OrderForm) confirm_return_ofs.get(i) ; 
	       order.setOrder_status(49);
	       this.orderFormService.update(order);
     }
 
     params.clear();
     params.put("delivery_end_time", new Date());
     List dgs = this.deliveryGoodsService
       .query(
       "select obj from DeliveryGoods obj where obj.d_goods.goods_store.delivery_end_time<:delivery_end_time", 
       params, -1, -1);
     for (int i=0 ; i<dgs.size() ; i++) {
    	 DeliveryGoods dg  = (DeliveryGoods) dgs.get(i) ;
         dg.setD_status(-2);
         this.deliveryGoodsService.update(dg);
         Goods goods1 = dg.getD_goods();
         goods1.setDelivery_status(0);
         this.goodsService.update(goods1);
     }
 
     params.clear();
     params.put("combin_end_time", new Date());
     stores = this.storeService
       .query(
       "select obj from Store obj where obj.combin_end_time<=:combin_end_time", 
       params, -1, -1);
     for (Iterator tmp_stores = stores.iterator(); tmp_stores.hasNext();)
     {
       Store store = (Store)tmp_stores.next();
       Iterator tmp_gds_list = store.getGoods_list().iterator(); 
       Goods goods1 = (Goods)tmp_gds_list.next();
       if (goods1.getCombin_status() != 0) {
         goods1.setCombin_begin_time(null);
         goods1.setCombin_end_time(null);
         goods1.setCombin_price(null);
         goods1.setCombin_status(0);
         goods1.getCombin_goods().clear();
         this.goodsService.update(goods1);
       }
 
     }
 
     List goods_list = this.evaluateService.query_goods(
       "select distinct obj.evaluate_goods from Evaluate obj ", null, 
       -1, -1);
     description_evaluate = 0.00D;
     for (int i=0 ; i<goods_list.size() ; i++) {
    	 Goods tmp_good = (Goods) goods_list.get(i) ;	  
       description_evaluate = 0.0D;
       params.clear();
       params.put("evaluate_goods_id", tmp_good.getId());
       List eva_list = this.evaluateService
         .query(
         "select obj from Evaluate obj where obj.evaluate_goods.id=:evaluate_goods_id", 
         params, -1, -1);
       for (int i1=0 ; i1<eva_list.size() ; i1++) {
    		Evaluate tmp_eva = (Evaluate) eva_list.get(i1) ;
            description_evaluate = CommUtil.add(tmp_eva.getDescription_evaluate(), Double.valueOf(description_evaluate));
       }
 
       description_evaluate = CommUtil.div(Double.valueOf(description_evaluate), 
         Integer.valueOf(eva_list
         .size()));
       tmp_good.setDescription_evaluate(
         BigDecimal.valueOf(description_evaluate));
       this.goodsService.update(tmp_good);
     }
 
     params.clear();
     params.put("weixin_status", Integer.valueOf(1));
     List store_list = this.storeService
       .query(
       "select obj from Store obj where obj.weixin_status=:weixin_status", 
       params, -1, -1);
     for (int i=0 ; i<store_list.size() ; i++) {
    	 Store tmp_store = (Store) store_list.get(i) ;
       if (tmp_store.getWeixin_end_time().before(new Date())) {
    	   tmp_store.setWeixin_status(2);
         this.storeService.update(tmp_store);
       }
     }
   }
 
   private boolean send_email(OrderForm order, String mark) throws Exception
   {
     com.luxplat.foundation.domain.Template template = this.templateService.getObjByProperty("mark", mark);
     if (template.isOpen()) {
       String email = order.getStore().getUser().getEmail();
       String subject = template.getTitle();
       String path = System.getProperty("luxplat.root") + "vm" + 
         File.separator;
       PrintWriter pwrite = new PrintWriter(new OutputStreamWriter(
         new FileOutputStream(path + "msg.vm", false), "UTF-8"));
       pwrite.print(template.getContent());
       pwrite.flush();
       pwrite.close();
 
       Properties p = new Properties();
       p.setProperty("file.resource.loader.path", 
         System.getProperty("luxplat.root") + 
         "vm" + File.separator);
       p.setProperty("input.encoding", "UTF-8");
       p.setProperty("output.encoding", "UTF-8");
       Velocity.init(p);
       org.apache.velocity.Template blank = Velocity.getTemplate("msg.vm", 
         "UTF-8");
       VelocityContext context = new VelocityContext();
       context.put("buyer", order.getUser());
       context.put("seller", order.getStore().getUser());
       context.put("config", this.configService.getSysConfig());
       context.put("send_time", CommUtil.formatLongDate(new Date()));
       context.put("webPath", this.configService.getSysConfig()
         .getAddress());
       context.put("order", order);
       StringWriter writer = new StringWriter();
       blank.merge(context, writer);
 
       String content = writer.toString();
       boolean ret = this.msgTools.sendEmail(email, subject, content);
       return ret;
     }
     return false;
   }
 
   private boolean send_sms(OrderForm order, String mobile, String mark) throws Exception
   {
     com.luxplat.foundation.domain.Template template = this.templateService.getObjByProperty("mark", mark);
     if (template.isOpen()) {
       String path = System.getProperty("luxplat.root") + "vm" + 
         File.separator;
       PrintWriter pwrite = new PrintWriter(new OutputStreamWriter(
         new FileOutputStream(path + "msg.vm", false), "UTF-8"));
       pwrite.print(template.getContent());
       pwrite.flush();
       pwrite.close();
 
       Properties p = new Properties();
       p.setProperty("file.resource.loader.path", 
         System.getProperty("luxplat.root") + 
         "vm" + File.separator);
       p.setProperty("input.encoding", "UTF-8");
       p.setProperty("output.encoding", "UTF-8");
       Velocity.init(p);
       org.apache.velocity.Template blank = Velocity.getTemplate("msg.vm", 
         "UTF-8");
       VelocityContext context = new VelocityContext();
       context.put("buyer", order.getUser());
       context.put("seller", order.getStore().getUser());
       context.put("config", this.configService.getSysConfig());
       context.put("send_time", CommUtil.formatLongDate(new Date()));
       context.put("webPath", this.configService.getSysConfig()
         .getAddress());
       context.put("order", order);
       StringWriter writer = new StringWriter();
       blank.merge(context, writer);
 
       String content = writer.toString();
       boolean ret = this.msgTools.sendSMS(mobile, content);
       return ret;
     }
     return false;
   }
 }
