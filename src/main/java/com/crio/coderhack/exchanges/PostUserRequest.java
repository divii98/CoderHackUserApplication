package com.crio.coderhack.exchanges;

import com.crio.coderhack.constants.Constants;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostUserRequest {
    @JsonProperty("UserID")
    @NotBlank(message = Constants.USER_ID_CANNOT_BE_BLANK)
    private String userId;
    @JsonProperty("Username")
    @NotBlank(message = Constants.USERNAME_CANNOT_BE_BLANK)
    private String userName;
}

