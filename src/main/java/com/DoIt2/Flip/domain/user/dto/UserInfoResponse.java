package com.DoIt2.Flip.domain.user.dto;

import com.DoIt2.Flip.domain.user.enums.Role;
import com.DoIt2.Flip.domain.user.enums.Theme;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoResponse {

    private String name;
    private String email;
    private Role role;
    private Theme theme;
}
