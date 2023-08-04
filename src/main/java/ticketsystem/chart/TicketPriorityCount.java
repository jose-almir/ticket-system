package ticketsystem.chart;

import ticketsystem.ticket.Ticket;

public record TicketPriorityCount(Ticket.Priority priority, Long count) {
}
