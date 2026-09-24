package com.example.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class SecurityEndpointsTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    void testHelloEndpointIsPublic() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("hello is Guest"));
    }

    @Test
    void testCustomerAllUnauthenticated() throws Exception {
        mockMvc.perform(get("/customer/all"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    void testCustomerAllWithRoleAdmin() throws Exception {
        mockMvc.perform(get("/customer/all"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Nguyễn Hữu Trung")));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void testCustomerAllWithRoleUserForbidden() throws Exception {
        mockMvc.perform(get("/customer/all"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void testCustomerByIdWithRoleUser() throws Exception {
        mockMvc.perform(get("/customer/001"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("001")))
                .andExpect(content().string(containsString("Nguyễn Hữu Trung")));
    }

    @Test
    void testAddUserEndpointAndAuthenticate() throws Exception {
        String newUserJson = """
                {
                    "name": "testdev",
                    "email": "testdev@gmail.com",
                    "password": "pass123",
                    "roles": "ROLE_USER"
                }
                """;

        mockMvc.perform(post("/user/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Thêm user thành công!"));

        // Authenticate with newly registered user using HTTP Basic
        mockMvc.perform(get("/customer/001")
                        .with(httpBasic("testdev", "pass123")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("001")));
    }

    @Test
    void testInitialAdminUserFromDatabase() throws Exception {
        // trung / 123 seeded by DataInitializer
        mockMvc.perform(get("/customer/all")
                        .with(httpBasic("trung", "123")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Nguyễn Hữu Trung")));
    }

    @Test
    void testInitialNormalUserFromDatabase() throws Exception {
        // user / 123 seeded by DataInitializer
        mockMvc.perform(get("/customer/001")
                        .with(httpBasic("user", "123")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("001")));

        // user / 123 cannot access /customer/all (ROLE_ADMIN only)
        mockMvc.perform(get("/customer/all")
                        .with(httpBasic("user", "123")))
                .andExpect(status().isForbidden());
    }
}
