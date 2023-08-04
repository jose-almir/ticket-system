package ticketsystem.chart;

import lombok.Data;
import ticketsystem.ticket.Ticket;

public record TicketStatusCount(Ticket.Status status, Long count) {
}
