package com.example._2_ong_endy_spring_homework001.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketPaymentRequest {
    private List<Long> ticketId;
    private boolean paymentStatus;
}
