package com.cloudstone.emenu.store.sqlitedb;


import static org.junit.Assert.*;
import com.cloudstone.emenu.EmenuContext;
import com.cloudstone.emenu.data.Dish;
import com.cloudstone.emenu.storage.sqlitedb.DishDb;
import com.cloudstone.emenu.storage.sqlitedb.util.SqliteDataSource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.List;

/**
 * Created by nicholaszhao on 4/19/14.
 */
public class DishDbTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private DishDb dishDb;

    @Before
    public void createDb() throws Exception {
        SqliteDataSource dataSource = new SqliteDataSource();
        File dbFile = tempFolder.newFile();
        dataSource.setDbFile(dbFile);
        dishDb = new DishDb();
        dishDb.setDataSource(dataSource);
    }

    @Test
    public void basicOperationTest() throws Exception {
        EmenuContext context = new EmenuContext();
        Dish dish = new Dish();
        dish.setName("name");
        dish.setPinyin("name");
        dish.setPrice(5);
        dish.setDesc("desc");
        dishDb.add(context, dish);
        List<Dish> dishes = dishDb.getAll(context);
        assertEquals(1, dishes.size());

        Dish updatedDish = dishes.get(0);
        dishDb.delete(context, updatedDish.getId());
        dishes = dishDb.getAll(context);
        assertEquals(1, dishes.size());
        updatedDish = dishes.get(0);
        assertTrue(updatedDish.isDeleted());
        assertEquals("name", updatedDish.getName());
        assertEquals(5, updatedDish.getPrice(), 0);
        assertEquals("desc", updatedDish.getDesc());


        updatedDish.setDeleted(false);
        dishDb.update(context, updatedDish);
        dishes = dishDb.getAll(context);
        assertEquals(1, dishes.size());
        updatedDish = dishes.get(0);
        assertEquals("name", updatedDish.getName());
        assertEquals(5, updatedDish.getPrice(), 0);
        assertEquals("desc", updatedDish.getDesc());
        assertFalse(updatedDish.isDeleted());

    }

}
