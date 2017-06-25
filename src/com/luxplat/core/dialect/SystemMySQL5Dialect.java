 package com.luxplat.core.dialect;
 
 import org.hibernate.Hibernate;
import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.NullableType;
 
 public class SystemMySQL5Dialect extends MySQL5Dialect
 {
   public SystemMySQL5Dialect()
   {
	 super();
     registerHibernateType(-1, Hibernate.TEXT.getName());
     registerFunction("getGoodsClassChildIDList",new SQLFunctionTemplate(Hibernate.STRING,"getGoodsClassChildIDList(?1)"));
   }
 }

