package com.cloudstone.emenu.store.sqlitedb;

import com.cloudstone.emenu.storage.sqlitedb.MenuDb;
import com.cloudstone.emenu.storage.sqlitedb.util.SqliteDataSource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * Created by nicholaszhao on 4/19/14.
 */
public class MenuDbTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private MenuDb menuDb;

    @Before
    public void createDb() throws Exception {
        SqliteDataSource dataSource = new SqliteDataSource();
        File dbFile = tempFolder.newFile();
        dataSource.setDbFile(dbFile);
        menuDb = new MenuDb();
        menuDb.setDataSource(dataSource);
    }

}
