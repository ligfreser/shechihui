package com.luxplat.foundation.service;

import com.luxplat.core.query.support.IPageList;
import com.luxplat.core.query.support.IQueryObject;
import com.luxplat.foundation.domain.AdvertPosition;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract interface IAdvertPositionService
{
  public abstract boolean save(AdvertPosition paramAdvertPosition);

  public abstract AdvertPosition getObjById(Long paramLong);

  public abstract boolean delete(Long paramLong);

  public abstract boolean batchDelete(List<Serializable> paramList);

  public abstract IPageList list(IQueryObject paramIQueryObject);

  public abstract boolean update(AdvertPosition paramAdvertPosition);

  public abstract List<AdvertPosition> query(String paramString, Map paramMap, int paramInt1, int paramInt2);
}



