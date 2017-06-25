package com.luxplat.foundation.service;

import com.luxplat.core.query.support.IPageList;
import com.luxplat.core.query.support.IQueryObject;
import com.luxplat.foundation.domain.GoodsType;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract interface IGoodsTypeService
{
  public abstract boolean save(GoodsType paramGoodsType);

  public abstract GoodsType getObjById(Long paramLong);

  public abstract boolean delete(Long paramLong);

  public abstract boolean batchDelete(List<Serializable> paramList);

  public abstract IPageList list(IQueryObject paramIQueryObject);

  public abstract boolean update(GoodsType paramGoodsType);

  public abstract List<GoodsType> query(String paramString, Map paramMap, int paramInt1, int paramInt2);
}



