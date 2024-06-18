package ru.molchmd.minibank.middle.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateUserRequest {
    private final Long userId;
    private final String userName;

    public CreateUserRequest(@JsonProperty("userId") Long userId,
                             @JsonProperty("userName") String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return "CreateUserRequest{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                '}';
    }
}
