package com.crio.coderhack.service;

import com.crio.coderhack.constants.Constants;
import com.crio.coderhack.entity.Badges;
import com.crio.coderhack.entity.User;
import com.crio.coderhack.exceptions.UserAlreadyExistException;
import com.crio.coderhack.exceptions.UserNotExistException;
import com.crio.coderhack.exchanges.PostUserRequest;
import com.crio.coderhack.exchanges.UpdateUserRequest;
import com.crio.coderhack.exchanges.UserResponse;
import com.crio.coderhack.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public UserResponse addUser(PostUserRequest postUserRequest) {
        if (!userRepository.existsById(postUserRequest.getUserId())) {
            User user = userRepository.save(new User(postUserRequest.getUserId(), postUserRequest.getUserName()));
            return modelMapper.map(user, UserResponse.class);
        }
        throw new UserAlreadyExistException(Constants.EXIST_SAME_WITH_ID);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> allUser = userRepository.findAll();
        allUser.sort(Comparator.comparingInt(User::getScore).reversed());
        return modelMapper.map(allUser, new TypeToken<List<UserResponse>>() {}.getType());
    }

    @Override
    public UserResponse getUser(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty())
            throw new UserNotExistException(Constants.NOT_EXIST_WITH_GIVEN_ID);
        return modelMapper.map(user.get(), UserResponse.class);
    }

    @Override
    public UserResponse updateScore(UpdateUserRequest updateRequest) {
        Optional<User> user = userRepository.findById(updateRequest.getUserId());
        if(user.isEmpty())
            throw new UserNotExistException(Constants.NOT_EXIST_WITH_GIVEN_ID);
        int newScore = updateRequest.getScore();
        if(user.get().getScore() < newScore) {
            user.get().setScore(newScore);
            user.get().getBadges().addAll(Badges.getBadge(newScore));
            return modelMapper.map(userRepository.save(user.get()),UserResponse.class);
        }
        return modelMapper.map(user.get(),UserResponse.class);
    }

    @Override
    public void deleteUser(String userId) {
        if (!userRepository.existsById(userId))
            throw new UserNotExistException(Constants.NOT_EXIST_WITH_GIVEN_ID);
        userRepository.deleteById(userId);
    }
}
