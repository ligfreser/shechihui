package com.luxplat.foundation.service;

import com.luxplat.core.query.support.IPageList;
import com.luxplat.core.query.support.IQueryObject;
import com.luxplat.foundation.domain.GroupGoods;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract interface IGroupGoodsService
{
  public abstract boolean save(GroupGoods paramGroupGoods);

  public abstract GroupGoods getObjById(Long paramLong);

  public abstract boolean delete(Long paramLong);

  public abstract boolean batchDelete(List<Serializable> paramList);

  public abstract IPageList list(IQueryObject paramIQueryObject);

  public abstract boolean update(GroupGoods paramGroupGoods);

  public abstract List<GroupGoods> query(String paramString, Map paramMap, int paramInt1, int paramInt2);
}



