package com.tilon.ojt_back.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUpdateDTO {
    private int AdminId;
  private String currentPassword;
  private String newPassword;    

}

