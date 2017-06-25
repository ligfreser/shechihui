package com.luxplat.foundation.service;

import com.luxplat.core.query.support.IPageList;
import com.luxplat.core.query.support.IQueryObject;
import com.luxplat.foundation.domain.CouponInfo;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract interface ICouponInfoService
{
  public abstract boolean save(CouponInfo paramCouponInfo);

  public abstract CouponInfo getObjById(Long paramLong);

  public abstract boolean delete(Long paramLong);

  public abstract boolean batchDelete(List<Serializable> paramList);

  public abstract IPageList list(IQueryObject paramIQueryObject);

  public abstract boolean update(CouponInfo paramCouponInfo);

  public abstract List<CouponInfo> query(String paramString, Map paramMap, int paramInt1, int paramInt2);
}



