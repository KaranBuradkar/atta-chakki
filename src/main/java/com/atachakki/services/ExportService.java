package com.atachakki.services;

import com.atachakki.components.customer.CustomerResponseDto;
import com.atachakki.components.order.OrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class ExportService {

    public ByteArrayInputStream exportCustomersToCsv(Page<CustomerResponseDto> customers) {

        StringBuilder sb = new StringBuilder();
        sb.append("ID,Name,Email,Debt,CreatedAt\n");

        for (CustomerResponseDto c : customers) {
            sb.append(c.id()).append(",");
            sb.append(safe(c.name())).append(",");
            sb.append(safe(c.email())).append(",");
            sb.append("FAILED").append(",");
            sb.append(c.createdAt()).append("\n");
        }

        return new ByteArrayInputStream(sb.toString().getBytes());
    }

    private String safe(String s) {
        return (s == null ? "NA" : s.replace(",", " "));
    }

    public ByteArrayInputStream exportOrdersToCsv(
            CustomerResponseDto c,
            Page<OrderResponseDto> orders
    ) {
        StringBuilder sb = new StringBuilder();
        sb.append("ID,Name,Email,Debt,CreatedAt\n");
        sb.append(c.id()).append(",");
        sb.append(safe(c.name())).append(",");
        sb.append(safe(c.email())).append(",");
        sb.append("FAILED").append(",");
        sb.append(c.createdAt()).append("\n\n");

        sb.append("ID,OrderDate,Name,quantity,quantityType,totalAmount," +
                "paymentStatus,addedByName,updatedByName,CreatedAt, UpdatedAt\n");

        for (OrderResponseDto o : orders) {
            sb.append(o.id()).append(",");
            sb.append(o.orderDate().toString()).append(",");
            sb.append(safe(o.orderItemName())).append(",");
            sb.append(safe(o.quantity().toString())).append(",");
            sb.append(safe(o.quantityType().toString())).append(",");
            sb.append(safe(o.totalAmount().toString())).append(",");
            sb.append(safe(o.paymentStatus().toString())).append(",");
            sb.append(safe(o.addedByName())).append(",");
            sb.append(safe(o.updatedByName())).append(",");
            sb.append(o.createdAt().toString()).append(",");
            sb.append(o.updatedAt()).append("\n");
        }

        return new ByteArrayInputStream(sb.toString().getBytes());
    }

    public ByteArrayInputStream exportCustomersToPdf(Page<CustomerResponseDto> customers) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        return new ByteArrayInputStream(out.toByteArray());
    }

}
