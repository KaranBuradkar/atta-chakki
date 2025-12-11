package com.atachakki.components.payment;

import com.atachakki.components.due.or.refund.DueOrRefundRequestDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PaymentBundleRequestDto {

    @NotNull @NotEmpty
    @JsonProperty(value = "orderIds")  private List<Long> orderIds;
    @NotNull
    @JsonProperty(value = "payment") @Valid private PaymentRequestDto paymentRequestDto;
    @JsonProperty(value = "dueOrRefund") @Valid private DueOrRefundRequestDto dueOrRefundRequestDto;

    public PaymentBundleRequestDto() {}

    public List<Long> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<Long> orderIds) {
        this.orderIds = orderIds;
    }

    public PaymentRequestDto getPaymentRequestDto() {
        return paymentRequestDto;
    }

    public void setPaymentRequestDto(PaymentRequestDto paymentRequestDto) {
        this.paymentRequestDto = paymentRequestDto;
    }

    public DueOrRefundRequestDto getDueOrRefundRequestDto() {
        return dueOrRefundRequestDto;
    }

    public void setDueOrRefundRequestDto(DueOrRefundRequestDto dueOrRefundRequestDto) {
        this.dueOrRefundRequestDto = dueOrRefundRequestDto;
    }
}
