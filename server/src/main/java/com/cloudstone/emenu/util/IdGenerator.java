/**
 * @(#)IdGenerator.java, 2013-7-1. 
 *
 */
package com.cloudstone.emenu.util;

import com.cloudstone.emenu.EmenuContext;
import com.cloudstone.emenu.storage.dao.IDb;

/**
 * @author xuhongfeng
 */
public class IdGenerator {

    private int lastId = -1;

    public synchronized int generateId(EmenuContext context, IDb db) {
        if (lastId == -1) {
            lastId = db.getMaxId(context);
        }
        lastId++;
        if (lastId < 1) {
            lastId = 1;
        }
        return lastId;
    }
}
