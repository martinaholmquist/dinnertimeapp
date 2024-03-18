package com.tinasdinner.dinnertimeapp.records;

public record ChangePasswordReq(String currentPassword,
                                String newPassword,
                                String confirmationPassword) {
}
