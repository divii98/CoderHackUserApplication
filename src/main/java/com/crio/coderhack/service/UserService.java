package com.crio.coderhack.service;

import com.crio.coderhack.exchanges.PostUserRequest;
import com.crio.coderhack.exchanges.UpdateUserRequest;
import com.crio.coderhack.exchanges.UserResponse;

import java.util.List;

public interface UserService {
    public UserResponse addUser(PostUserRequest postUserRequest);
    public List<UserResponse> getAllUsers();
    public UserResponse getUser(String userId);
    public UserResponse updateScore(UpdateUserRequest updateUserRequest);
    public void deleteUser(String userId);
}
