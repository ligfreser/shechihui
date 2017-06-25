package com.luxplat.foundation.service;

import com.luxplat.core.query.support.IPageList;
import com.luxplat.core.query.support.IQueryObject;
import com.luxplat.foundation.domain.Store;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract interface IStoreService
{
  public abstract boolean save(Store paramStore);

  public abstract Store getObjById(Long paramLong);

  public abstract boolean delete(Long paramLong);

  public abstract boolean batchDelete(List<Serializable> paramList);

  public abstract IPageList list(IQueryObject paramIQueryObject);

  public abstract boolean update(Store paramStore);

  public abstract List<Store> query(String paramString, Map paramMap, int paramInt1, int paramInt2);

  public abstract Store getObjByProperty(String paramString, Object paramObject);
}



