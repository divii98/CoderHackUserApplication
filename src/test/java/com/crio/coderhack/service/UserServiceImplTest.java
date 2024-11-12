package com.crio.coderhack.service;

import com.crio.coderhack.entity.Badges;
import com.crio.coderhack.entity.User;
import com.crio.coderhack.exceptions.UserAlreadyExistException;
import com.crio.coderhack.exceptions.UserNotExistException;
import com.crio.coderhack.exchanges.PostUserRequest;
import com.crio.coderhack.exchanges.UpdateUserRequest;
import com.crio.coderhack.exchanges.UserResponse;
import com.crio.coderhack.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplTest {
    @MockBean
    UserRepository userRepository;

    @Autowired
    UserService userService;

    private static final ModelMapper mapper = new ModelMapper();


    @Test
    void addUserWithCorrectDetails() {
        PostUserRequest psr = new PostUserRequest("123", "Ran Odin");
        User user = new User("123", "Ran Odin");
        UserResponse ur = new UserResponse("123", "Ran Odin", 0, new HashSet<>());

        when(userRepository.existsById("123")).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);

        UserResponse actualResult = userService.addUser(psr);
        assertEquals(actualResult, ur);

        verify(userRepository, times(1)).existsById(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void addUserWithUserIdWhichAlreadyExist() {
        PostUserRequest psr = new PostUserRequest("123", "Ran Odin");

        when(userRepository.existsById("123")).thenReturn(true);

        assertThrows(UserAlreadyExistException.class, () -> userService.addUser(psr));

        verify(userRepository, times(1)).existsById("123");
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void getAllUsers() {
        User user1 = new User("123", "Dan Molly", 52, Badges.getBadge(52));
        User user2 =  new User("456", "Max Han", 95, Badges.getBadge(95));
        User user3 = new User("999", "Tan bol", 12, Badges.getBadge(12));
        List<User> allUserList = new ArrayList<>(List.of(user1, user2, user3));

        when(userRepository.findAll()).thenReturn(allUserList);

        List<UserResponse> actualResult = userService.getAllUsers();
        List<UserResponse> expectedResult = mapper.map(allUserList, new TypeToken<List<UserResponse>>() {}.getType());
        expectedResult.sort(Comparator.comparingInt(UserResponse::getScore).reversed());

        assertEquals(actualResult, expectedResult);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserByIdSuccess() {
        String userId = "123";
        User user1 = new User(userId, "Dan Molly", 52, Badges.getBadge(52));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));

        UserResponse actualResult = userService.getUser(userId);

        assertEquals(actualResult,mapper.map(user1, UserResponse.class));

        verify(userRepository,times(1)).findById(anyString());
    }

    @Test
    void getUserForIdWhichDoesNotExist() {
        String userId = "123";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotExistException.class,() -> userService.getUser(userId));

        verify(userRepository,times(1)).findById(anyString());
    }
    @Test
    void updateScoreWhenScoreIsMoreThanExistingScore() {
        UserResponse expectedResponse = new UserResponse("123","Van Bes",60,Badges.getBadge(60));
        UpdateUserRequest uur = new UpdateUserRequest("123",60);
        User user = new User("123","Van Bes",20,new HashSet<>());
        User updatedUser = new User("123","Van Bes",60,Badges.getBadge(60));

        when(userRepository.findById("123")).thenReturn(Optional.of(user));
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        UserResponse actualResponse = userService.updateScore(uur);
        assertEquals(actualResponse,expectedResponse);

        verify(userRepository,times(1)).findById("123");
        verify(userRepository,times(1)).save(any(User.class));
    }

    @Test
    void updateScoreWhenScoreIsLessThanExistingScore() {
        UserResponse expectedResponse = new UserResponse("123","Van Bes",100,Badges.getBadge(100));
        UpdateUserRequest uur = new UpdateUserRequest("123",60);
        User user = new User("123","Van Bes",100,Badges.getBadge(100));

        when(userRepository.findById("123")).thenReturn(Optional.of(user));

        UserResponse actualResponse = userService.updateScore(uur);
        assertEquals(actualResponse,expectedResponse);

        verify(userRepository,times(1)).findById("123");
        verify(userRepository,times(0)).save(any(User.class));
    }

    @Test
    void updateScoreWhenUserDoesNotExist() {
        UpdateUserRequest uur = new UpdateUserRequest("123",60);

        when(userRepository.findById("123")).thenReturn(Optional.empty());

        assertThrows(UserNotExistException.class, ()-> userService.updateScore(uur));

        verify(userRepository,times(1)).findById("123");
        verify(userRepository,times(0)).save(any(User.class));
    }

    @Test
    void deleteUserSuccess() {
        String userId = "546";

        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteUser(userId);

        verify(userRepository,times(1)).existsById(userId);
        verify(userRepository,times(1)).deleteById(userId);
    }

    @Test
    void deleteUserRequestWhenUserDoesNotExist() {
        String userId = "546";

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(UserNotExistException.class,()->userService.deleteUser(userId));

        verify(userRepository,times(1)).existsById(userId);
        verify(userRepository,times(0)).deleteById(userId);
    }
}