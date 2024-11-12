package com.crio.coderhack.exchanges;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    @JsonProperty("UserID")
    private String userId;
    @JsonProperty("UserName")
    private String userName;
    @JsonProperty("Score")
    private Integer score;
    @JsonProperty("Badges")
    private HashSet<String> badges;

}
