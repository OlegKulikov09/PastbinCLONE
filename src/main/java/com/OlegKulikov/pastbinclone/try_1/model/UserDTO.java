package com.OlegKulikov.pastbinclone.try_1.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    private int id;
    private String login;
    private int sumRateOfUser;
}