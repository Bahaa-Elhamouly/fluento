package com.fluento.features.admin.controller;

import com.fluento.common.response.ApiResponse;
import com.fluento.features.admin.dto.AdminUpdateUserRequest;
import com.fluento.features.admin.dto.AdminUserResponse;
import com.fluento.features.admin.service.AdminUserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/users")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public ApiResponse<List<AdminUserResponse>> users() {
        return ApiResponse.success(
                "Users retrieved successfully",
                adminUserService.getUsers()
        );
    }

    @GetMapping("/{userId}")
    public ApiResponse<AdminUserResponse> user(
            @PathVariable UUID userId
    ) {
        return ApiResponse.success(
                "User retrieved successfully",
                adminUserService.getUser(userId)
        );
    }

    @PatchMapping("/{userId}")
    public ApiResponse<AdminUserResponse> updateUser(
            @PathVariable UUID userId,
            @Valid @RequestBody AdminUpdateUserRequest request
    ) {
        return ApiResponse.success(
                "User updated successfully",
                adminUserService.updateUser(userId, request)
        );
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<Void> deleteUser(@PathVariable UUID userId) {
        adminUserService.deleteUser(userId);

        return ApiResponse.success("User deleted successfully");
    }
}
