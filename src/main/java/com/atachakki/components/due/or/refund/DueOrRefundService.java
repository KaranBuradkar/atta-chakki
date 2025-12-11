package com.atachakki.components.due.or.refund;

import com.atachakki.security.authorizationValidation.IsAdminOrShopOwnerOrShopkeeper;
import org.springframework.stereotype.Service;

@Service
public interface DueOrRefundService {

    @IsAdminOrShopOwnerOrShopkeeper
    DueOrRefundResponseDto create(Long shopId, Long customerId, DueOrRefundRequestDto requestDto);
}
