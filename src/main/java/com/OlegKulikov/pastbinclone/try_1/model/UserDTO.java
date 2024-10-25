package com.OlegKulikov.pastbinclone.try_1.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserDTO {
    private UUID id;
    private String login;
    private int sumRateOfUser;
}