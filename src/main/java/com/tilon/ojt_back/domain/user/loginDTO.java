package com.tilon.ojt_back.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class loginDTO {
    private String empno;
    private String password;
}
