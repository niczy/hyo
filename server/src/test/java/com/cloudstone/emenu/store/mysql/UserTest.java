package com.cloudstone.emenu.store.mysql;

import com.cloudstone.emenu.EmenuContext;
import com.cloudstone.emenu.data.User;
import com.cloudstone.emenu.storage.dao.IUserDb;
import com.cloudstone.emenu.storage.dao.RestaurantDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by charliez on 5/3/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:simpleUnitTestContext.xml"})
public class UserTest {

    @Autowired
    private IUserDb userJDBC;

    @Test
    public void basicOperationTest() throws Exception {
        EmenuContext context = new EmenuContext();
        context.setRestaurantId(1);
        User user = new User();
        user.setName("Test");
        user.setRealName("Real");
        user.setPassword("password");
        user.setRestaurantId(1);
        userJDBC.add(context, user);
        List<User> users = userJDBC.getAll(context);
        assertEquals(1, users.size());

        User updatedUser = users.get(0);
        userJDBC.delete(context, updatedUser.getId());
        users = userJDBC.getAll(context);
        assertEquals(0, users.size());

        updatedUser.setDeleted(false);
        updatedUser.setName("Test1");
        userJDBC.update(context, updatedUser);
        users = userJDBC.getAll(context);
        assertEquals(1, users.size());
        updatedUser = users.get(0);
        assertEquals("Test1", updatedUser.getName());
        assertEquals("Real", updatedUser.getRealName());
        assertEquals("password", updatedUser.getPassword());
        assertFalse(updatedUser.isDeleted());
    }
}
