package com.cloudstone.emenu.integration;

/**
 * Created by charliez on 4/20/14.
 */
import com.cloudstone.emenu.constant.Const;
import com.cloudstone.emenu.data.User;
import com.cloudstone.emenu.util.JsonUtils;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.HttpSession;

import static org.hamcrest.Matchers.isA;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserApiTest extends TestBase {

    @Test
    public void login() throws Exception {
        HttpSession session = mockMvc.perform(post("/api/login")
                .param("name", "admin").param("password", "admin"))
                .andExpect(status().isOk())
                .andReturn().getRequest().getSession();

        String username = randomUser();
        // No session, bad request.
        mockMvc.perform(post("/api/users")
                .content(
                    "{\"name\":\"" + username + "\"," +
                    "\"password\":\"test_admin\"," +
                    "\"type\":1," +
                    "\"restaurantId\":" + restaurant("test").getId() + "}"))
                .andExpect(status().isUnauthorized());

        // Create admin
        User admin = JsonUtils.fromJson(
                mockMvc.perform(post("/api/users")
                        .content("{\"name\":\"" + username + "\"," +
                                "\"password\":\"test_admin\"," +
                                "\"type\":1," +
                                "\"restaurantId\":" + restaurant("test").getId() + "}")
                        .session((MockHttpSession) session))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(isA(int.class)))
                    .andReturn().getResponse().getContentAsString(), User.class);

        assertEquals(username, admin.getName());
        assertEquals(1, admin.getType());

        mockMvc.perform(post("/api/login")
                .param("name", "test_admin1").param("password", "randomStuff"))
                .andExpect(status().isUnauthorized());

        // Login as admin.
        session = mockMvc.perform(post("/api/login")
                .param("name", "test_admin1").param("password", "test_admin"))
                .andExpect(status().isOk())
                .andReturn().getRequest().getSession();

        username = randomUser();

        // Create waiter
        MvcResult res = mockMvc.perform(post("/api/users")
                .content(
                        "{\"name\":\"" + username + "\"," +
                        "\"password\":\"waiter\",\"type\":0}"
                )
                .session((MockHttpSession) session))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(isA(int.class)))
                .andReturn();

        User waiter = JsonUtils.fromJson(res.getResponse().getContentAsString(), User.class);
        assertEquals(username, waiter.getName());

        session = mockMvc.perform(post("/api/login")
                .param("name", username).param("password", "waiter"))
                .andExpect(status().isOk())
                .andReturn().getRequest().getSession();

        User user = (User) session.getAttribute("loginUser");
        assertEquals(username, user.getName());
        assertEquals(Const.UserType.USER, user.getType());
    }
}