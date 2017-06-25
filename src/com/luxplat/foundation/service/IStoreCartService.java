package com.luxplat.foundation.service;

import com.luxplat.core.query.support.IPageList;
import com.luxplat.core.query.support.IQueryObject;
import com.luxplat.foundation.domain.StoreCart;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract interface IStoreCartService
{
  public abstract boolean save(StoreCart paramStoreCart);

  public abstract StoreCart getObjById(Long paramLong);

  public abstract boolean delete(Long paramLong);

  public abstract boolean batchDelete(List<Serializable> paramList);

  public abstract IPageList list(IQueryObject paramIQueryObject);

  public abstract boolean update(StoreCart paramStoreCart);

  public abstract List<StoreCart> query(String paramString, Map paramMap, int paramInt1, int paramInt2);
}



