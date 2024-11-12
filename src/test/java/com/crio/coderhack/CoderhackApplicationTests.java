package com.crio.coderhack;

import com.crio.coderhack.controller.UserController;
import com.crio.coderhack.entity.Badges;
import com.crio.coderhack.entity.User;
import com.crio.coderhack.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CoderhackApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    UserRepository userRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void integrationTestAddUser() throws Exception {
        //Add User
        User user = getUsers().get(0);
        String request = """
                {
                    "UserID": "1",
                    "Username":"User1"
                }""";
        when(userRepository.existsById(user.getUserId())).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);

        this.mockMvc.perform(MockMvcRequestBuilders.post(UserController.USER_API_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.UserID").value(user.getUserId()))
                .andExpect(jsonPath("$.UserName").value(user.getUserName()))
                .andExpect(jsonPath("$.Score").value(user.getScore()))
                .andExpect(jsonPath("$.Badges").isEmpty())
                .andReturn();
    }

    @Test
    void integrationTestGetUserAndAllUsers() throws Exception {
        //Get User By Id
        User user = getUsers().get(1);

        when(userRepository.findById("1")).thenReturn(Optional.ofNullable(user));

        this.mockMvc.perform(MockMvcRequestBuilders.get(UserController.USER_API_ENDPOINT + "/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.UserID").value(user.getUserId()))
                .andExpect(jsonPath("$.UserName").value(user.getUserName()))
                .andExpect(jsonPath("$.Score").value(user.getScore()))
                .andExpect(jsonPath("$.Badges").value(containsInAnyOrder(user.getBadges().toArray())));

        //Get All Users
        when(userRepository.findAll()).thenReturn(getUsers());
        List<User> listOfUsers = getUsers();
        this.mockMvc.perform(MockMvcRequestBuilders.get(UserController.USER_API_ENDPOINT)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].UserName").value("User2"))
                .andExpect(jsonPath("$[1].UserName").value("User4"))
                .andExpect(jsonPath("$[2].UserName").value("User3"))
                .andExpect(jsonPath("$[3].UserName").value("User1"));
    }

    @Test
    void integrationTestUpdateUserScore() throws Exception {
        User user = getUsers().get(3);
        User updatedUser = new User(user.getUserId(), user.getUserName(), 90, Badges.getBadge(90));

        String request = """
                {
                    "Score":"90"
                }""";

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        this.mockMvc.perform(MockMvcRequestBuilders.put(UserController.USER_API_ENDPOINT + "/" + user.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.UserID").value(updatedUser.getUserId()))
                .andExpect(jsonPath("$.UserName").value(updatedUser.getUserName()))
                .andExpect(jsonPath("$.Score").value(updatedUser.getScore()))
                .andExpect(jsonPath("$.Badges").value(containsInAnyOrder(updatedUser.getBadges().toArray())));
    }

    @Test
    void integrationTestDeleteUser() throws Exception {
        //Delete User By Id
        when(userRepository.existsById("1")).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.delete(UserController.USER_API_ENDPOINT + "/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }


    private List<User> getUsers() {
        List<User> userList = new ArrayList<>();
        userList.add(new User("1", "User1", 0, new HashSet<>()));
        userList.add(new User("2", "User2", 100, Badges.getBadge(100)));
        userList.add(new User("3", "User3", 10, Badges.getBadge(10)));
        userList.add(new User("4", "User4", 50, Badges.getBadge(50)));
        return userList;
    }

}
