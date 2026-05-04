package org.example.ordersservice.dtos.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.ordersservice.dtos.user.UserOutputDto;

@Getter
@Setter
@Builder
public class LoginResponseDto {
    private String token;
    private UserOutputDto userOutputDto;
}
