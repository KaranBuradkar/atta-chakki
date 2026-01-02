package com.atachakki.components.customer;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class CustomerRequestDto {

    @NotBlank(message = "Name is required")
    @JsonProperty("customer_name") private String name;

    @NotNull(message = "Debt is required")
    @DecimalMin(value = "0.0", message = "Debt must be positive")
    @JsonProperty("customer_debt") private BigDecimal debt;

    @Email(message = "Invalid email format")
    @JsonProperty("customer_email") private String email;

    @JsonProperty("customer_specification") private String specification;

    public CustomerRequestDto() {}

    public CustomerRequestDto(
            String name,
            BigDecimal debt,
            String email,
            String specification
    ) {
        this.name = name;
        this.debt = debt;
        this.email = email;
        this.specification = specification;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getDebt() { return debt; }
    public void setDebt(BigDecimal debt) { this.debt = debt; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSpecification() { return specification; }
    public void setSpecification(String specification) { this.specification = specification; }
}
