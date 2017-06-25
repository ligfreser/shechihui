package com.luxplat.foundation.service;

import com.luxplat.core.query.support.IPageList;
import com.luxplat.core.query.support.IQueryObject;
import com.luxplat.foundation.domain.ZTCGoldLog;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract interface IZTCGoldLogService
{
  public abstract boolean save(ZTCGoldLog paramZTCGoldLog);

  public abstract ZTCGoldLog getObjById(Long paramLong);

  public abstract boolean delete(Long paramLong);

  public abstract boolean batchDelete(List<Serializable> paramList);

  public abstract IPageList list(IQueryObject paramIQueryObject);

  public abstract boolean update(ZTCGoldLog paramZTCGoldLog);

  public abstract List<ZTCGoldLog> query(String paramString, Map paramMap, int paramInt1, int paramInt2);
}



