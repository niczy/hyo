package com.cloudstone.emenu.integration;

import com.cloudstone.emenu.EmenuContext;
import com.cloudstone.emenu.constant.Const;
import com.cloudstone.emenu.data.*;
import com.cloudstone.emenu.logic.MenuLogic;
import com.cloudstone.emenu.logic.RestaurantLogic;
import com.cloudstone.emenu.logic.UserLogic;
import com.cloudstone.emenu.storage.dao.IUserDb;
import com.cloudstone.emenu.util.JsonUtils;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

/**
 * Created by charliez on 4/26/14.
 */

@ContextConfiguration(locations = {"classpath:integrationTestContext.xml", "classpath:commonContext.xml"})
@WebAppConfiguration
public class TestBase {

    @Autowired
    private IUserDb userDb;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Autowired
    protected UserLogic userLogic;

    @Autowired
    protected MenuLogic menuLogic;

    @Autowired
    protected RestaurantLogic restLogic;

    // ------------------------------------
    //    Utilities
    // ------------------------------------
    protected Restaurant restaurant(String name) {
        Restaurant rest = new Restaurant();
        rest.setName(name);
        return restLogic.add(new EmenuContext(), rest);
    }

    protected Menu menu(String name) throws Exception {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setRestaurantId(restaurant("restaurant").getId());
        menu = menuLogic.addMenu(new EmenuContext(), menu);
        System.out.println("created menu " + JsonUtils.toJson(menu));
        return menu;
    }

    protected Chapter chapter(String name) throws Exception {
        Chapter chapter = new Chapter();
        chapter.setName(name);
        chapter.setMenuId(menu("menu").getId());
        return menuLogic.addChapter(new EmenuContext(), chapter);
    }

    protected Dish dish(String name) throws Exception {
        Dish dish = new Dish();
        dish.setName(name);
        return menuLogic.addDish(new EmenuContext(), dish);
    }

    protected String randomUser() {
        String username = null;
        do {
            username = UUID.randomUUID().toString();
        }
        while (userDb.getByName(new EmenuContext(), username) != null);
        return username;
    }

    protected User createWaiter() {
        User user = new User();
        user.setName(randomUser());
        user.setPassword("password");
        user.setType(Const.UserType.USER);
        user = userLogic.add(new EmenuContext(), user);
        return user;
    }
}
