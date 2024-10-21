package org.tomato.gowithtomato.dao.daoInterface.base;

import org.tomato.gowithtomato.util.ConnectionManager;

public interface BaseDAO {
    ConnectionManager connectionManager = ConnectionManager.getInstance();
}
