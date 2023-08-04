package ticketsystem.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ticketsystem.agent.Agent;
import ticketsystem.agent.AgentRepository;
import ticketsystem.ticket.Ticket;
import ticketsystem.ticket.TicketRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Profile({"dev", "test"})
public class PopulateService {
    private final Random random = new Random();

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private TicketRepository ticketRepository;

    public void populate() {
        List<Agent> agents = new ArrayList<>();
        List<Ticket> tickets = new ArrayList<>();

        agents.add(new Agent("John", "Smith", "john.doe@example.com", "+1234567890", "johndoe"));
        agents.add(new Agent("Jane", "Johnson", "jane.smith@example.com", "+9876543210", "janesmith"));
        agents.add(new Agent("Michael", "Brown", "michael.brown@example.com", "+1122334455", "mbrown"));
        agents.add(new Agent("Emily", "Wilson", "emily.wilson@example.com", "+9988776655", "ewilson"));
        agents.add(new Agent("Robert", "Jones", "robert.jones@example.com", "+1231231231", "rjones"));
        agents.add(new Agent("Lisa", "Martinez", "lisa.martinez@example.com", "+9988774466", "lmartinez"));
        agents.add(new Agent("William", "Lee", "william.lee@example.com", "+1122334433", "wlee"));
        agents.add(new Agent("Samantha", "Davis", "samantha.davis@example.com", "+5566778899", "sdavis"));
        agents.add(new Agent("Daniel", "Taylor", "daniel.taylor@example.com", "+9988778877", "dtaylor"));
        agents.add(new Agent("Laura", "Miller", "laura.miller@example.com", "+2233445566", "lmiller"));
        agents.add(new Agent("James", "Wilson", "james.wilson@example.com", "+8877665544", "jwilson"));
        agents.add(new Agent("Sophia", "Anderson", "sophia.anderson@example.com", "+8899776655", "sanderson"));

        agentRepository.saveAll(agents);


        List<String> ticketTitles = List.of("Login issue", "Performance problem", "Data not updating", "Feature request", "Application crash");
        List<String> ticketDescriptions = List.of(
                "User cannot log in to the system",
                "Application is running slow",
                "Data is not getting updated in real-time",
                "Customer requests a new feature",
                "Application crashes when performing action X"
        );
        List<Ticket.Priority> ticketPriorities = List.of(
                Ticket.Priority.HIGH, Ticket.Priority.MEDIUM, Ticket.Priority.LOW, Ticket.Priority.URGENT
        );
        List<Ticket.Status> ticketStatuses = List.of(Ticket.Status.OPEN, Ticket.Status.RESOLVED, Ticket.Status.CLOSED);

        for (int i = 1; i <= 50; i++) {
            String title = ticketTitles.get(random.nextInt(ticketTitles.size()));
            String description = ticketDescriptions.get(random.nextInt(ticketDescriptions.size()));
            Ticket.Priority priority = ticketPriorities.get(random.nextInt(ticketPriorities.size()));
            Ticket.Status status = ticketStatuses.get(random.nextInt(ticketStatuses.size()));
            Agent submitterAgent = agents.get(random.nextInt(agents.size()));

            Ticket ticket = new Ticket(title, description, priority, status, submitterAgent);


            if (status == Ticket.Status.RESOLVED || status == Ticket.Status.OPEN) {
                Agent assignedAgent;

                do {
                    assignedAgent = agents.get(random.nextInt(agents.size()));
                } while (ticket.getSubmitter().equals(assignedAgent));

                ticket.setAssigned(assignedAgent);
            }

            tickets.add(ticket);
        }

        ticketRepository.saveAll(tickets);

        for(Ticket ticket : tickets) {
            ticket.setCreatedAt(ticket.getCreatedAt().minusYears(random.nextLong(7)));
        }
        ticketRepository.saveAll(tickets);
    }
}
