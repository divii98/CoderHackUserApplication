package com.crio.coderhack.controller;

import com.crio.coderhack.constants.Constants;
import com.crio.coderhack.entity.Badges;
import com.crio.coderhack.entity.User;
import com.crio.coderhack.exceptions.UserAlreadyExistException;
import com.crio.coderhack.exceptions.UserNotExistException;
import com.crio.coderhack.exchanges.PostUserRequest;
import com.crio.coderhack.exchanges.UpdateUserRequest;
import com.crio.coderhack.exchanges.UserResponse;
import com.crio.coderhack.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    private final ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddUserApiSuccess() throws Exception {
        PostUserRequest postUserRequest = new PostUserRequest("101", "John Loe");
        UserResponse expectedResponse = modelMapper.map(new User("101", "John Loe"), UserResponse.class);
        when(userService.addUser(postUserRequest)).thenReturn(expectedResponse);
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(UserController.USER_API_ENDPOINT)
                        .content(objectMapper.writeValueAsString(postUserRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        UserResponse actualResponse = objectMapper.readValue(jsonResponse, UserResponse.class);
        assertEquals(actualResponse, expectedResponse);

        verify(userService, times(1)).addUser(postUserRequest);
    }

    @Test
    void testAddUserApiWhenUserAlreadyExistWithSameId() throws Exception {
        PostUserRequest postUserRequest = new PostUserRequest("101", "John Loe");
        UserResponse expectedResponse = modelMapper.map(new User("101", "John Loe"), UserResponse.class);
        when(userService.addUser(postUserRequest)).thenThrow(new UserAlreadyExistException(Constants.EXIST_SAME_WITH_ID));
        this.mockMvc.perform(MockMvcRequestBuilders.post(UserController.USER_API_ENDPOINT)
                        .content(objectMapper.writeValueAsString(postUserRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.Error").value(Constants.EXIST_SAME_WITH_ID))
                .andReturn();

        verify(userService, times(1)).addUser(postUserRequest);
    }

    @Test
    void testAddUserApiWithBlankUserId() throws Exception {
        PostUserRequest postUserRequest = new PostUserRequest("", "John Loe");

        this.mockMvc.perform(MockMvcRequestBuilders.post(UserController.USER_API_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postUserRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.Error").value(Constants.USER_ID_CANNOT_BE_BLANK));
        verify(userService, times(0)).addUser(postUserRequest);
    }

    @Test
    void testAddUserApiWithBlankUserName() throws Exception {
        PostUserRequest postUserRequest = new PostUserRequest("101", "");

        this.mockMvc.perform(MockMvcRequestBuilders.post(UserController.USER_API_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postUserRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.Error").value(Constants.USERNAME_CANNOT_BE_BLANK));
        verify(userService, times(0)).addUser(postUserRequest);
    }

    @Test
    void getAllUsersSuccess() throws Exception {
        User user1 = new User("1", "Name1");
        User user2 = new User("2", "Name2");
        List<UserResponse> getAllUseResponse = List.of(
                modelMapper.map(user1, UserResponse.class),
                modelMapper.map(user2, UserResponse.class));

        when(userService.getAllUsers()).thenReturn(getAllUseResponse);

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(UserController.USER_API_ENDPOINT))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<UserResponse> actualResponse = objectMapper.readValue(jsonResponse, new TypeReference<List<UserResponse>>() {
        });
        assertEquals(actualResponse, getAllUseResponse);

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUserByIdSuccess() throws Exception {
        String userId = "101";
        UserResponse expectedResponse = modelMapper.map(new User("101", "John Loe"), UserResponse.class);

        when(userService.getUser(userId)).thenReturn(expectedResponse);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                        get(UserController.USER_API_ENDPOINT + "/" + userId))
                .andExpect(status().isOk())
                .andReturn();
        UserResponse actualResponse = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponse.class);
        assertEquals(actualResponse, expectedResponse);

        verify(userService, times(1)).getUser(userId);
    }

    @Test
    void getUserByIdRequestWhenUserDoesNotExist() throws Exception {
        String userId = "101";

        when(userService.getUser(userId)).thenThrow(new UserNotExistException(Constants.NOT_EXIST_WITH_GIVEN_ID));

        this.mockMvc.perform(MockMvcRequestBuilders.
                        get(UserController.USER_API_ENDPOINT + "/" + userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.Error").value(Constants.NOT_EXIST_WITH_GIVEN_ID))
                .andReturn();


        verify(userService, times(1)).getUser(userId);
    }

    @Test
    void updateScoresForGivenUserIdSuccess() throws Exception {
        String userId = "101";
        UpdateUserRequest updateUserRequest = new UpdateUserRequest("101",30);
        UserResponse expectedUserResponse = new UserResponse("101","David Ram",30, Badges.getBadge(30));

        when(userService.updateScore(updateUserRequest)).thenReturn(expectedUserResponse);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .put(UserController.USER_API_ENDPOINT+"/"+userId)
                .content(objectMapper.writeValueAsString(updateUserRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        UserResponse actualResponse = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponse.class);
        assertEquals(actualResponse, expectedUserResponse);

        verify(userService,times(1)).updateScore(updateUserRequest);

    }

    @Test
    void updateScoresWhenUserIdDoesNotExist() throws Exception {
        String userId = "101";
        UpdateUserRequest updateUserRequest = new UpdateUserRequest("101",30);

        when(userService.updateScore(updateUserRequest)).thenThrow(new UserNotExistException(Constants.NOT_EXIST_WITH_GIVEN_ID));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put(UserController.USER_API_ENDPOINT+"/"+userId)
                        .content(objectMapper.writeValueAsString(updateUserRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.Error").value(Constants.NOT_EXIST_WITH_GIVEN_ID));

        verify(userService,times(1)).updateScore(updateUserRequest);

    }
    @Test
    void updateScoreWhenScoreValueIsLessThanZero() throws Exception {
        String userId = "101";
        UpdateUserRequest updateUserRequest = new UpdateUserRequest("101",-10);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put(UserController.USER_API_ENDPOINT+"/"+userId)
                        .content(objectMapper.writeValueAsString(updateUserRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.Error").value(Constants.SHOULD_BE_GREATER_THAN_0));

        verify(userService,times(0)).updateScore(updateUserRequest);

    }
    @Test
    void updateScoreWhenScoreValueIsGreaterThanHundred() throws Exception {
        String userId = "101";
        UpdateUserRequest updateUserRequest = new UpdateUserRequest("101",500);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put(UserController.USER_API_ENDPOINT+"/"+userId)
                        .content(objectMapper.writeValueAsString(updateUserRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.Error").value(Constants.SHOULD_BE_LESS_THAN_100));

        verify(userService,times(0)).updateScore(updateUserRequest);
    }

    @Test
    void updateScoreWhenScoreValueIsNull() throws Exception {
        String userId = "101";
        UpdateUserRequest updateUserRequest = new UpdateUserRequest("101",null);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put(UserController.USER_API_ENDPOINT+"/"+userId)
                        .content(objectMapper.writeValueAsString(updateUserRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.Error").value(Constants.SCORE_CANNOT_BE_NULL));

        verify(userService,times(0)).updateScore(updateUserRequest);
    }

    @Test
    void deleteUserSuccessRequest() throws Exception {
        String userId = "101";

        this.mockMvc.perform(MockMvcRequestBuilders.
                        delete(UserController.USER_API_ENDPOINT + "/" + userId))
                .andExpect(status().isNoContent())
                .andReturn();

        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    void deleteUserRequestForUserDoesNotExist() throws Exception {
        String userId = "101";

        doThrow(new UserNotExistException(Constants.NOT_EXIST_WITH_GIVEN_ID)).when(userService).deleteUser(userId);

        this.mockMvc.perform(MockMvcRequestBuilders.
                        delete(UserController.USER_API_ENDPOINT + "/" + userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.Error").value(Constants.NOT_EXIST_WITH_GIVEN_ID))
                .andReturn();

        verify(userService, times(1)).deleteUser(userId);
    }
}