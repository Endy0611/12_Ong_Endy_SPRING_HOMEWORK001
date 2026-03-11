package com.example._2_ong_endy_spring_homework001.controller;


import com.example._2_ong_endy_spring_homework001.model.entity.Ticket;
import com.example._2_ong_endy_spring_homework001.model.enums.TicketStatus;
import com.example._2_ong_endy_spring_homework001.model.request.TicketPaymentRequest;
import com.example._2_ong_endy_spring_homework001.model.request.TicketRequest;
import com.example._2_ong_endy_spring_homework001.model.response.ResponseApi;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final ArrayList<Ticket> TICKET_LIST = new ArrayList<>();
    private final AtomicLong ATOMIC_LONG = new AtomicLong(6L);
    public TicketController() {
        TICKET_LIST.add(new Ticket(1L, "Endy", "2026-02-11", "Cambodia", "Phnom Penh", 180.50, true, "BOOKED", "A1"));
        TICKET_LIST.add(new Ticket(2L, "Dine", "2026-03-09", "Cambodia", "Kompot", 200.50, true, "COMPLETED", "A2"));
        TICKET_LIST.add(new Ticket(3L, "Sovannarith", "2026-02-09", "Cambodia", "BattamBang", 199.99, false, "COMPLETED", "A3"));
        TICKET_LIST.add(new Ticket(4L, "Vesna", "2026-02-03", "Cambodia", "Moundoul Kiri", 299.99, false, "COMPLETED", "A3"));
        TICKET_LIST.add(new Ticket(5L, "Kanha", "2026-02-04", "Cambodia", "Kirirom", 92.99, false, "COMPLETED", "A3"));

    }


    @Operation(summary = "Get all tickets")
    @GetMapping
    public ResponseEntity<?> getAllTickets() {
        ResponseApi<List<Ticket>> res = new ResponseApi<>(true, "Ticket retrived successfully", HttpStatus.OK, TICKET_LIST,LocalDateTime.now());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @Operation(summary = "Gat a ticket by ID")
    @GetMapping("/{ticket-id}")
    public ResponseEntity<?> getTicketById(@PathVariable("ticket-id") Long id) {
        Ticket foundTicket = null;
        for (Ticket ticket : TICKET_LIST) {
            if (ticket.getTicketId().equals(id)){
                foundTicket = ticket;
            }
        }
        if (foundTicket != null) {
            ResponseApi<Ticket> res = new ResponseApi<>(true, "Ticket retrived successfully", HttpStatus.OK, foundTicket, LocalDateTime.now());
            return  ResponseEntity.ok(res);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Create a new ticket")
    @PostMapping
    public ResponseEntity<?> createNewTicket(@RequestBody TicketRequest ticketrequest) {
        Ticket ticket = new Ticket(ATOMIC_LONG.getAndIncrement(), ticketrequest.getPassengerName(), ticketrequest.getTravelDate(), ticketrequest.getSourceStation(), ticketrequest.getDestinationStation()
        , ticketrequest.getPrice(), ticketrequest.isPaymentStatus(), ticketrequest.getTicketStatus(), ticketrequest.getSeatNumber());
        TICKET_LIST.add(ticket);
        ResponseApi<Ticket> res = new ResponseApi<>(true, "Ticket retrived successfully", HttpStatus.OK, ticket,LocalDateTime.now());
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }


    @Operation(summary = "Search Tickets by passenger name")
    @GetMapping("/search")
    public ResponseEntity<?> searchByName(@RequestParam String name) {
        List<Ticket> searchName = new ArrayList<>();
        for (Ticket ticket : TICKET_LIST) {
            if (ticket.getPassengerName().equals(name)){
                searchName.add(ticket);

            }
        }

        if(!searchName.isEmpty()){
            ResponseApi<List<Ticket>> res = new ResponseApi<>(true, "Ticket retrived successfully", HttpStatus.OK, searchName,LocalDateTime.now());
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        ResponseApi<List<Ticket>> res = new ResponseApi<>(true, "No tickets found for the given passenger name", HttpStatus.OK,searchName,LocalDateTime.now());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @Operation(summary = "Filter tickets by status and travel date")
    @GetMapping("/filter")
    public ResponseEntity<?> filter(@RequestParam TicketStatus ticketStatus, @RequestParam String travelDate) {
        ArrayList<Ticket> filterTicket = new ArrayList<>();
        for (Ticket ticket : TICKET_LIST) {
            if (ticket.getTicketStatus().equals(ticketStatus.toString()) && ticket.getTravelDate().equals(travelDate)){
                filterTicket.add(ticket);
            }
        }
        ResponseApi<List<Ticket>> res = new ResponseApi<>(true, "Ticket retrived successfully", HttpStatus.OK, filterTicket,LocalDateTime.now());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @Operation(summary = "Update a ticket by ID")
    @PutMapping ("/{ticket-id}")
    public ResponseEntity<?> updateById(@PathVariable("ticket-id") Long id, @RequestBody TicketRequest ticketRequest) {
        List<Ticket> updateticket = new ArrayList<>();
        for (Ticket ticket : TICKET_LIST) {
            if (ticket.getTicketId().equals(id)){
                ticket.setPassengerName(ticketRequest.getPassengerName());
                ticket.setTravelDate(ticketRequest.getTravelDate());
                ticket.setSourceStation(ticketRequest.getSourceStation());
                ticket.setDestinationStation(ticketRequest.getDestinationStation());
                ticket.setPrice(ticketRequest.getPrice());
                ticket.setPaymentStatus(ticketRequest.isPaymentStatus());
                ticket.setTicketStatus(ticketRequest.getTicketStatus());
                ticket.setSeatNumber(ticketRequest.getSeatNumber());
                updateticket.add(ticket);
            }
        }
        if(!updateticket.isEmpty()){
            ResponseApi<List<Ticket>> res = new ResponseApi<>(true, "Ticket updated successfully",HttpStatus.CREATED, updateticket, LocalDateTime.now());
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        ResponseApi<List<Ticket>> res = new ResponseApi<>(false, "No tickets found with the given ID.",HttpStatus.NOT_FOUND, null, LocalDateTime.now());
        return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Delete a ticket using ID")
    @DeleteMapping("/{ticket-id}")
    public ResponseEntity<?> deleteById(@PathVariable("ticket-id") long id) {

        boolean ticketFound = TICKET_LIST.removeIf(t -> t.getTicketId().equals(id));
        if(ticketFound) {
            ResponseApi<Ticket> res = new ResponseApi<>(true, "Ticket deleted successfully", HttpStatus.OK, null,LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }else {
            ResponseApi<Ticket> res = new ResponseApi<>(false, "Ticket retrived successfully", HttpStatus.NOT_FOUND, null,LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
    }

    @Operation(summary = "Create multiple new Tickets")
    @PostMapping ("/bulk")
    public ResponseEntity<?> createMultiTicket(@RequestBody List<TicketRequest> ticketRequests  ) {
        List<Ticket> newTicket = new ArrayList<>();
        for (TicketRequest ticketRequest : ticketRequests) {
            Ticket ticket = new Ticket(
                    ATOMIC_LONG.getAndIncrement(),
                    ticketRequest.getPassengerName(),
                    ticketRequest.getTravelDate(),
                    ticketRequest.getSourceStation(),
                    ticketRequest.getDestinationStation(),
                    ticketRequest.getPrice(),
                    ticketRequest.isPaymentStatus(),
                    ticketRequest.getTicketStatus(),
                    ticketRequest.getSeatNumber());
            newTicket.add(ticket);
        }
        TICKET_LIST.addAll(newTicket);
        ResponseApi<List<Ticket>> res = new ResponseApi<>(true, "Tickets created successfully",HttpStatus.CREATED, newTicket, LocalDateTime.now());
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @Operation(summary = "Update payment status of multiple tickets")
    @PutMapping("/bulk")
    public ResponseEntity<?> updatePaymentStatus(@RequestBody TicketPaymentRequest ticketPaymentRequest) {
        List<Ticket> updatePaymentStatus = new ArrayList<>();

        for (Ticket ticket : TICKET_LIST) {
            if (ticketPaymentRequest.getTicketId().contains(ticket.getTicketId())) {
                ticket.setPaymentStatus(ticketPaymentRequest.isPaymentStatus());
                updatePaymentStatus.add(ticket);
            }
        }
        if(!updatePaymentStatus.isEmpty()) {
            ResponseApi<List<Ticket>> res = new ResponseApi<>(true, "Payment status updated successfully" , HttpStatus.OK, updatePaymentStatus, LocalDateTime.now());
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        ResponseApi<List<Ticket>> res = new ResponseApi<>(false, "No tickets were updated" , HttpStatus.NOT_FOUND, null, LocalDateTime.now());
        return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    }
}
