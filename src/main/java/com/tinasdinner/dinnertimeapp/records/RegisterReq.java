package com.tinasdinner.dinnertimeapp.records;

import com.tinasdinner.dinnertimeapp.auth.Role;

public record RegisterReq(
        String username,
        String email,
        String password,
        Role role) {
}
