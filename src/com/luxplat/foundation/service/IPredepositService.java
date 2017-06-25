package com.luxplat.foundation.service;

import com.luxplat.core.query.support.IPageList;
import com.luxplat.core.query.support.IQueryObject;
import com.luxplat.foundation.domain.Predeposit;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract interface IPredepositService
{
  public abstract boolean save(Predeposit paramPredeposit);

  public abstract Predeposit getObjById(Long paramLong);

  public abstract boolean delete(Long paramLong);

  public abstract boolean batchDelete(List<Serializable> paramList);

  public abstract IPageList list(IQueryObject paramIQueryObject);

  public abstract boolean update(Predeposit paramPredeposit);

  public abstract List<Predeposit> query(String paramString, Map paramMap, int paramInt1, int paramInt2);
}



