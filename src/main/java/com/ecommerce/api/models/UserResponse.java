package com.ecommerce.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private int id;
    private String username;
    private String password;
    private String level;
    private int expectedSuccess;
    private String description;
    private String created_at;
}