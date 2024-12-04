package com.tilon.ojt_back.domain.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginDTO {
    private String empno;
    private String password;
}
