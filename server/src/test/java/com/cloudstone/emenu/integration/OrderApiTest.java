package com.cloudstone.emenu.integration;

/**
 * Created by charliez on 4/27/14.
 */

import com.cloudstone.emenu.EmenuContext;
import com.cloudstone.emenu.data.Dish;
import com.cloudstone.emenu.data.Table;
import com.cloudstone.emenu.logic.TableLogic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public final class OrderApiTest extends TestBase {

    @Autowired
    private TableLogic tableLogic;

    private Table openTable(String name) {
        Table table = new Table();
        table.setName(name);
        table = tableLogic.add(new EmenuContext(), table);
        tableLogic.occupy(new EmenuContext(), table, 1);
        return table;
    }

    @Test
    public void createOrder() throws Exception {
        Table table = openTable("test_table");
        Dish dish = dish("test_dish");

        // No dishes -- bad request
        mockMvc.perform(post("/api/orders")
                .content("{" +
                            "\"name\":\"order1\"," +
                            "\"tableId\":" + Integer.toString(table.getId()) +
                        "}"))
                .andExpect(status().isBadRequest());

        // Wrong table id -- bad request
        mockMvc.perform(post("/api/orders")
                .content("{" +
                        "\"name\":\"order1\"," +
                        "\"tableId\":12349876" +
                        ",\"dishes\":[{\"id\":" + Integer.toString(dish.getId()) + "}]" +
                        "}"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/orders")
                .content("{" +
                        "\"name\":\"order1\"," +
                        "\"tableId\":" + Integer.toString(table.getId()) +
                        ",\"dishes\":[{\"id\":" + Integer.toString(dish.getId()) + "}]" +
                        "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(isA(int.class)));
    }
}
