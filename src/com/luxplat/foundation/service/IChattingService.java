package com.luxplat.foundation.service;

import com.luxplat.core.query.support.IPageList;
import com.luxplat.core.query.support.IQueryObject;
import com.luxplat.foundation.domain.Chatting;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract interface IChattingService
{
  public abstract boolean save(Chatting paramChatting);

  public abstract Chatting getObjById(Long paramLong);

  public abstract boolean delete(Long paramLong);

  public abstract boolean batchDelete(List<Serializable> paramList);

  public abstract IPageList list(IQueryObject paramIQueryObject);

  public abstract boolean update(Chatting paramChatting);

  public abstract List<Chatting> query(String paramString, Map paramMap, int paramInt1, int paramInt2);
}



