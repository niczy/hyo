package com.cloudstone.emenu.store.mysql;

/**
 * Created by charliez on 5/3/14.
 */

import static org.junit.Assert.assertEquals;

import com.cloudstone.emenu.EmenuContext;
import com.cloudstone.emenu.data.Restaurant;
import com.cloudstone.emenu.storage.dao.RestaurantDAO;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:simpleUnitTestContext.xml"})
public class RestaurantTest {

    @Autowired
    private RestaurantDAO restaurantJDBC;

    @Before
    public void createDb() throws Exception {
    }

    @Test
    public void basicOperationTest() throws Exception {
        EmenuContext context = new EmenuContext();
        Restaurant rnt = new Restaurant();
        rnt.setName("pkduck");
        rnt = restaurantJDBC.add(context, rnt);
        List<Restaurant> rnts = restaurantJDBC.getAll(context);
        assertEquals(1, rnts.size());

        rnt = new Restaurant();
        rnt.setName("pkduck2");
        rnt = restaurantJDBC.add(context, rnt);
        rnts = restaurantJDBC.getAll(context);
        assertEquals(2, rnts.size());
        assertEquals(2, restaurantJDBC.count(context));

        int id2 = rnts.get(1).getId();

        rnt = restaurantJDBC.get(context, id2);
        assertEquals("pkduck2", rnt.getName());

        rnt.setName("pkduck3");
        restaurantJDBC.update(context, rnt);

        rnt = restaurantJDBC.get(context, id2);
        assertEquals("pkduck3", rnt.getName());

        restaurantJDBC.delete(context, id2);
        rnts = restaurantJDBC.getAll(context);
        assertEquals(1, rnts.size());
        assertEquals(1, restaurantJDBC.count(context));
    }

}
