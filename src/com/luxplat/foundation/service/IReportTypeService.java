package com.luxplat.foundation.service;

import com.luxplat.core.query.support.IPageList;
import com.luxplat.core.query.support.IQueryObject;
import com.luxplat.foundation.domain.ReportType;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract interface IReportTypeService
{
  public abstract boolean save(ReportType paramReportType);

  public abstract ReportType getObjById(Long paramLong);

  public abstract boolean delete(Long paramLong);

  public abstract boolean batchDelete(List<Serializable> paramList);

  public abstract IPageList list(IQueryObject paramIQueryObject);

  public abstract boolean update(ReportType paramReportType);

  public abstract List<ReportType> query(String paramString, Map paramMap, int paramInt1, int paramInt2);
}



