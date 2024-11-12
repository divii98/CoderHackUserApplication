package com.crio.coderhack.exchanges;

import com.crio.coderhack.constants.Constants;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateUserRequest {
    String userId;
    @NotNull(message = Constants.SCORE_CANNOT_BE_NULL)
    @Min(value = Constants.SCORE_MIN_VALUE, message = Constants.SHOULD_BE_GREATER_THAN_0)
    @Max(value = Constants.Score_Max_Value, message = Constants.SHOULD_BE_LESS_THAN_100)
    @JsonProperty("Score")
    Integer score;
}

