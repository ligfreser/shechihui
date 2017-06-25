 package com.luxplat.view.web.tools;
 
 import com.luxplat.foundation.domain.SpareGoodsClass;
 import com.luxplat.foundation.domain.SpareGoodsFloor;
 import com.luxplat.foundation.service.ISpareGoodsClassService;
 import java.io.PrintStream;
 import java.util.ArrayList;
 import java.util.Iterator;
 import java.util.List;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Component;
 
 @Component
 public class SpareGoodsViewTools
 {
 
   @Autowired
   private ISpareGoodsClassService sgcService;
 
   public List<SpareGoodsClass> query_childclass(SpareGoodsClass sgc)
   {
     List list = new ArrayList();
     Iterator localIterator2;
     for (Iterator localIterator1 = sgc.getChilds().iterator(); localIterator1.hasNext(); 
       localIterator2.hasNext())
     {
       SpareGoodsClass child = (SpareGoodsClass)localIterator1.next();
       list.add(child);
       localIterator2 = child.getChilds().iterator(); 
       SpareGoodsClass c = (SpareGoodsClass)localIterator2.next();
       list.add(c);
       continue; 
     }
 
     return list;
   }
 
   public List<SpareGoodsClass> query_floorClass(SpareGoodsFloor sgf)
   {
     List list = new ArrayList();
     Iterator localIterator2;
     for (Iterator localIterator1 = sgf.getSgc().getChilds().iterator(); localIterator1.hasNext(); 
       localIterator2.hasNext())
     {
       SpareGoodsClass child = (SpareGoodsClass)localIterator1.next();
       if (child.isViewInFloor()) {
         list.add(child);
       }
       localIterator2 = child.getChilds().iterator(); 
       SpareGoodsClass c = (SpareGoodsClass)localIterator2.next();
       if (c.isViewInFloor()) {
         list.add(c);
       }

       continue; 
     }
 
     return list;
   }
 
   public String ClearContent(String inputString)
   {
     String htmlStr = inputString;
     String textStr = "";
     try
     {
       String regEx_script = "<[//s]*?script[^>]*?>[//s//S]*?<[//s]*?///[//s]*?script[//s]*?>";
       String regEx_style = "<[//s]*?style[^>]*?>[//s//S]*?<[//s]*?///[//s]*?style[//s]*?>";
       String regEx_html = "<[^>]+>";
       String regEx_html1 = "<[^>]+";
       Pattern p_script = Pattern.compile(regEx_script, 2);
       Matcher m_script = p_script.matcher(htmlStr);
       htmlStr = m_script.replaceAll("");
 
       Pattern p_style = Pattern.compile(regEx_style, 2);
       Matcher m_style = p_style.matcher(htmlStr);
       htmlStr = m_style.replaceAll("");
 
       Pattern p_html = Pattern.compile(regEx_html, 2);
       Matcher m_html = p_html.matcher(htmlStr);
       htmlStr = m_html.replaceAll("");
 
       Pattern p_html1 = Pattern.compile(regEx_html1, 2);
       Matcher m_html1 = p_html1.matcher(htmlStr);
       htmlStr = m_html1.replaceAll("");
 
       textStr = htmlStr;
     } catch (Exception e) {
       System.err.println("Html2Text: " + e.getMessage());
     }
     return textStr;
   }
 }
