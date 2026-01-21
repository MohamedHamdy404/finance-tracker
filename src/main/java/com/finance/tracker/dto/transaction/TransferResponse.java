package com.finance.tracker.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO for transfer response (contains both linked transactions)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferResponse {

    private UUID transferGroupId;
    private TransactionResponse outgoingTransaction;
    private TransactionResponse incomingTransaction;
}
