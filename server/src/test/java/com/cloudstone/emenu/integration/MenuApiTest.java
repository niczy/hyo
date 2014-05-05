package com.cloudstone.emenu.integration;

/**
 * Created by charliez on 4/26/14.
 */

import com.cloudstone.emenu.EmenuContext;
import com.cloudstone.emenu.data.Chapter;
import com.cloudstone.emenu.data.Dish;
import com.cloudstone.emenu.data.Menu;
import com.cloudstone.emenu.data.Restaurant;
import com.cloudstone.emenu.logic.MenuLogic;
import com.cloudstone.emenu.logic.RestaurantLogic;
import com.cloudstone.emenu.util.JsonUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
public class MenuApiTest extends TestBase {

    // ------------------------------------
    //    Tests
    // ------------------------------------

    @Test
    public void createMenu() throws Exception {
        mockMvc.perform(post("/api/menus")
                    .content("{\"name\":\"test_menu1\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(isA(int.class)));
    }

    @Test
    public void createChapter() throws Exception {
        Menu menu = menu("test_menu");
        mockMvc.perform(post("/api/chapters")
                    .content("{\"name\":\"test_chapter1\"," +
                            "\"menuId\":" + menu.getId() + "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(isA(int.class)));

        String chapter2Str = mockMvc.perform(post("/api/chapters")
                    .content("{\"name\":\"test_chapter2\"," +
                             "\"menuId\":" + menu.getId() + "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(isA(int.class)))
                .andReturn().getResponse().getContentAsString();

        Chapter chapter2 = JsonUtils.fromJson(chapter2Str, Chapter.class);

        mockMvc.perform(get("/api/chapters")
                    .param("menuId", Integer.toString(menu.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        mockMvc.perform(get("/api/chapters")
                .param("menuId", Integer.toString(menu.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        // Delete
        mockMvc.perform(delete("/api/chapters/" + chapter2.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/chapters")
                .param("menuId", Integer.toString(menu.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        // Add chapter2 again.
        mockMvc.perform(post("/api/chapters")
                .content("{\"name\":\"test_chapter2\"," +
                        "\"menuId\":" + menu.getId() + "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(isA(int.class)));

        mockMvc.perform(get("/api/chapters")
                .param("menuId", Integer.toString(menu.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Ignore
    public void createPage() throws Exception {
        Chapter chapter = chapter("test_chapter");
        mockMvc.perform(post("/api/pages")
                    .content("{\"name\":\"test_page1\"}")
                    .param("chapterId", Integer.toString(chapter.getId())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(isA(int.class)));
    }

    @Test
    public void dishOperation() throws Exception {
        // Create dish
        mockMvc.perform(post("/api/dishes").accept(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"test_dish\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(isA(int.class)));

        // Update dish
        Dish dish = dish("random_name");
        String id = Integer.toString(dish.getId());
        dish.setName("new_name");
        String dishStr = JsonUtils.toJson(dish);
        mockMvc.perform(put("/api/dishes/" + id)
                    .accept(MediaType.APPLICATION_JSON).content(
                        dishStr))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("new_name"));

        // Delete dish
        mockMvc.perform(delete("/api/dishes/" + Integer.toString(dish.getId())))
                .andExpect(status().isOk());
    }

    @Test
    public void bindDishToMenu() throws Exception {
        Dish d1 = dish("random_name");
    }

    @Test
    public void listDishes() throws Exception {
        Dish d1 = dish("dish1");
        Dish d2 = dish("dish2");
        Dish d3 = dish("dish3");
        mockMvc.perform(get("/api/dishes"))
                .andExpect(status().isOk());
    }
}
