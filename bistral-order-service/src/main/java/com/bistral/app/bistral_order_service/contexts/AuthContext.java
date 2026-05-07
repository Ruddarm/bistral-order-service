package com.bistral.app.bistral_order_service.contexts;

import lombok.*;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@ToString
public class AuthContext {

    private UUID userId;
    private UUID bistroId;
    private UUID branchId;
    private UUID roleId;
    private Set<String> permissions;
}
