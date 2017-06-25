package com.luxplat.foundation.dao;

import com.luxplat.core.base.GenericDAO;
import com.luxplat.foundation.domain.Message;
import org.springframework.stereotype.Repository;

@Repository("messageDAO")
public class MessageDAO extends GenericDAO<Message>
{
}



