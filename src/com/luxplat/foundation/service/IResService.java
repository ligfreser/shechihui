package com.luxplat.foundation.service;

import com.luxplat.core.query.support.IPageList;
import com.luxplat.core.query.support.IQueryObject;
import com.luxplat.foundation.domain.Res;
import java.util.List;
import java.util.Map;

public abstract interface IResService
{
  public abstract boolean save(Res paramRes);

  public abstract boolean delete(Long paramLong);

  public abstract boolean update(Res paramRes);

  public abstract Res getObjById(Long paramLong);

  public abstract List<Res> query(String paramString, Map paramMap, int paramInt1, int paramInt2);

  public abstract IPageList list(IQueryObject paramIQueryObject);
}



