package com.luxplat.foundation.service;

import com.luxplat.core.query.support.IPageList;
import com.luxplat.core.query.support.IQueryObject;
import com.luxplat.foundation.domain.Report;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract interface IReportService
{
  public abstract boolean save(Report paramReport);

  public abstract Report getObjById(Long paramLong);

  public abstract boolean delete(Long paramLong);

  public abstract boolean batchDelete(List<Serializable> paramList);

  public abstract IPageList list(IQueryObject paramIQueryObject);

  public abstract boolean update(Report paramReport);

  public abstract List<Report> query(String paramString, Map paramMap, int paramInt1, int paramInt2);
}



