package com.tinasdinner.dinnertimeapp.records;

import com.tinasdinner.dinnertimeapp.auth.Role;

import java.util.List;

public record UserViewRecord(Integer id,String username,String email, String password, Role role, List Authorities) {
}
