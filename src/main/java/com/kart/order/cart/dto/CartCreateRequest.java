package com.kart.order.cart.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CartCreateRequest(
        @NotNull(message = "Cart status can not be null")
        @NotBlank(message = "Cart can not be blank or empty")
        @Pattern(
                regexp = "ACTIVE|CHECKED_OUT|ABANDONED",
                message = "Cart status can be ACTIVE or CHECKED_OUT or ABANDONED"
        )
        String status
) {
}
