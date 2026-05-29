package com.sinquinto.serviclick.Auth.Infrastructure;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GooglePendingUserResponse {
    String email;
    String name;
    String lastName;
    boolean requiresRoleSelection;
}
