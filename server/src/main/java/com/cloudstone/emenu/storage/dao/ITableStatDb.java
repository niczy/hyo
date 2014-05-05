
package com.cloudstone.emenu.storage.dao;

import com.cloudstone.emenu.EmenuContext;
import com.cloudstone.emenu.data.TableStat;

public interface ITableStatDb extends IDb {
    public void add(EmenuContext context, TableStat stat);

    public TableStat get(EmenuContext context, long day);
}
