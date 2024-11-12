package com.crio.coderhack.controller;

import com.crio.coderhack.exchanges.PostUserRequest;
import com.crio.coderhack.exchanges.UpdateUserRequest;
import com.crio.coderhack.exchanges.UserResponse;
import com.crio.coderhack.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
public class UserController {
    @Autowired
    UserService userService;

    public static final String USER_API_ENDPOINT = "/users";


    @PostMapping(value = USER_API_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> addUsers(@Valid @RequestBody PostUserRequest postUserRequest) {
        log.info("Add user request sent with data: " + postUserRequest);
        return ResponseEntity.ok(userService.addUser(postUserRequest));
    }

    @GetMapping(value = USER_API_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping(value = USER_API_ENDPOINT + "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> getUserById(@PathVariable String userId) {
        log.info("Get request called for Id: " + userId);
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PutMapping(value = USER_API_ENDPOINT + "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> updateScoresForGivenUserId(@PathVariable String userId, @Valid @RequestBody UpdateUserRequest updateRequest) {
        updateRequest.setUserId(userId);
        log.info("Update request called with data: " + updateRequest);
        return ResponseEntity.ok(userService.updateScore(updateRequest));
    }

    @DeleteMapping(value = USER_API_ENDPOINT + "/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        log.info("Delete request called for Id: " + userId);
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }


}
