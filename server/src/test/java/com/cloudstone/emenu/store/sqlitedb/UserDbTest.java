package com.cloudstone.emenu.store.sqlitedb;


import com.cloudstone.emenu.EmenuContext;
import com.cloudstone.emenu.data.User;
import com.cloudstone.emenu.storage.sqlitedb.UserDb;
import com.cloudstone.emenu.storage.sqlitedb.util.SqliteDataSource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by nicholaszhao on 4/19/14.
 */
public class UserDbTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private UserDb userDb;

    @Before
    public void createDb() throws Exception {
        SqliteDataSource dataSource = new SqliteDataSource();
        File dbFile = tempFolder.newFile();
        dataSource.setDbFile(dbFile);
        userDb = new UserDb();
        userDb.setDataSource(dataSource);
    }

    @Test
    public void basicOperationTest() throws Exception {
        EmenuContext context = new EmenuContext();
        User user = new User();
        user.setName("Test");
        user.setRealName("Real");
        user.setPassword("password");
        userDb.add(context, user);
        List<User> users = userDb.getAll(context);
        assertEquals(1, users.size());

        User updatedUser = users.get(0);
        userDb.delete(context, updatedUser.getId());
        users = userDb.getAll(context);
        assertEquals(1, users.size());
        updatedUser = users.get(0);
        assertTrue(updatedUser.isDeleted());
        assertEquals("Test", updatedUser.getName());
        assertEquals("Real", updatedUser.getRealName());
        assertEquals("password", user.getPassword());


        updatedUser.setDeleted(false);
        userDb.update(context, updatedUser);
        users = userDb.getAll(context);
        assertEquals(1, users.size());
        updatedUser = users.get(0);
        assertEquals("Test", updatedUser.getName());
        assertEquals("Real", updatedUser.getRealName());
        assertEquals("password", updatedUser.getPassword());
        assertFalse(updatedUser.isDeleted());
    }

}
