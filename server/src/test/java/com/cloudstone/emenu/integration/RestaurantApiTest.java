package com.cloudstone.emenu.integration;

/**
 * Created by charliez on 4/26/14.
 */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class RestaurantApiTest extends TestBase {

    @Test
    public void createRestaurant() throws Exception {
        mockMvc.perform(post("/api/restaurants")
                .content("{\"name\":\"test_menu1\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(isA(int.class)))
                .andExpect(jsonPath("$.restaurantId").value(isA(int.class)));
    }
}
