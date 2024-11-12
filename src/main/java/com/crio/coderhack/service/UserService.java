package com.crio.coderhack.service;

import com.crio.coderhack.exchanges.PostUserRequest;
import com.crio.coderhack.exchanges.UpdateUserRequest;
import com.crio.coderhack.exchanges.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse addUser(PostUserRequest postUserRequest);
    List<UserResponse> getAllUsers();
    UserResponse getUser(String userId);
    UserResponse updateScore(UpdateUserRequest updateUserRequest);
    void deleteUser(String userId);
}
