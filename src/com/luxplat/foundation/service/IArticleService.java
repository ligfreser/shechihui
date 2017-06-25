package com.luxplat.foundation.service;

import com.luxplat.core.query.support.IPageList;
import com.luxplat.core.query.support.IQueryObject;
import com.luxplat.foundation.domain.Article;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract interface IArticleService
{
  public abstract boolean save(Article paramArticle);

  public abstract Article getObjById(Long paramLong);

  public abstract boolean delete(Long paramLong);

  public abstract boolean batchDelete(List<Serializable> paramList);

  public abstract IPageList list(IQueryObject paramIQueryObject);

  public abstract boolean update(Article paramArticle);

  public abstract List<Article> query(String paramString, Map paramMap, int paramInt1, int paramInt2);

  public abstract Article getObjByProperty(String paramString, Object paramObject);
}



