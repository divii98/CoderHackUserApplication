package com.crio.coderhack.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;

@Document(collection = "coderHackUsers")
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class User {
    @Id
    private String userId;
    private String userName;
    private Integer score;
    private HashSet<String> badges;


    public User(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
        this.score = 0;
        this.badges = new HashSet<>();
    }
}
