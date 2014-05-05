package com.cloudstone.emenu.controllers;

/**
 * Created by charliez on 4/20/14.
 */
import com.cloudstone.emenu.EmenuContext;
import com.cloudstone.emenu.data.User;
import com.cloudstone.emenu.logic.UserLogic;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:unitTestContext.xml", "classpath:commonContext.xml"})
@WebAppConfiguration
public class UserApiControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private UserLogic userLogicMock;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        Mockito.reset(userLogicMock);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void login() throws Exception {
        when(userLogicMock.login(any(EmenuContext.class), eq("adminTest"), eq("passwordTest")))
                .thenReturn(new User());

        mockMvc.perform(post("/api/login")
                .param("name", "adminTest").param("password", "passwordTest"))
                .andExpect(status().isOk());
        verify(userLogicMock, times(1))
                .login(any(EmenuContext.class), eq("adminTest"), eq("passwordTest"));

        when(userLogicMock.login(any(EmenuContext.class), eq("adminTest"), eq("wrongPassword")))
                .thenReturn(null);

        mockMvc.perform(post("/api/login")
                .param("name", "adminTest").param("password", "wrongPassword"))
                .andExpect(status().isUnauthorized());

        verify(userLogicMock, times(1))
                .login(any(EmenuContext.class), eq("adminTest"), eq("wrongPassword"));
        verifyNoMoreInteractions(userLogicMock);
    }

    @Test
    public void findAllPublicUserNames() throws Exception {
        when(userLogicMock.listUserNames(any(EmenuContext.class))).thenReturn(Arrays.asList("first", "second"));

        mockMvc.perform(get("/api/public/user-names"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]", is("first")))
                .andExpect(jsonPath("$[1]", is("second")));

        verify(userLogicMock, times(1)).listUserNames(any(EmenuContext.class));
        verifyNoMoreInteractions(userLogicMock);
    }
}