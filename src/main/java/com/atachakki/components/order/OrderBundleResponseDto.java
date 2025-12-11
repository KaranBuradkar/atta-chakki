package com.atachakki.components.order;

import com.atachakki.components.due.or.refund.DueOrRefundResponseDto;
import org.springframework.data.domain.Page;

public record OrderBundleResponseDto(
        Page<OrderResponseDto> orderPageResponseDto,
        Page<DueOrRefundResponseDto> dueOrRefundPageResponseDto
) {
}
