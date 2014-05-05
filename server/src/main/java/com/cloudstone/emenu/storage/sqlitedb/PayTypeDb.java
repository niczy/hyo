/**
 * @(#)PayTypeDb.java, Aug 1, 2013. 
 *
 */
package com.cloudstone.emenu.storage.sqlitedb;

import java.util.ArrayList;
import java.util.List;

import com.cloudstone.emenu.storage.dao.IPayTypeDb;
import org.springframework.stereotype.Repository;

import com.cloudstone.emenu.EmenuContext;
import com.cloudstone.emenu.data.PayType;

/**
 * @author xuhongfeng
 */
@Repository
public class PayTypeDb extends IdNameDb<PayType> implements IPayTypeDb {
    private static final String TABLE_NAME = "payType";

    private static final String[] DEFAULT_PAY_TYPE = {
            "Cash", "Credit/Debit Card"
    };
    private List<PayType> payTypes = new ArrayList();

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected void init(EmenuContext context) {
        super.init(context);
        int id = 1;
        for (String name : DEFAULT_PAY_TYPE) {
            PayType type = new PayType();
            type.setName(name);
            type.setId(id++);
            payTypes.add(type);
        }

    }

    @Override
    protected PayType newObject() {
        return new PayType();
    }

    @Override
    public List<PayType> getAllPayType(EmenuContext context) {
        return payTypes;
    }

    @Override
    public void addPayType(EmenuContext context, PayType payType) {
        add(context, payType);
    }

}
