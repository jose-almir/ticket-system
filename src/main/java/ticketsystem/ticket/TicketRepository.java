package ticketsystem.ticket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ticketsystem.agent.Agent;
import ticketsystem.chart.TicketByAssignment;
import ticketsystem.chart.TicketPriorityCount;
import ticketsystem.chart.TicketStatusCount;
import ticketsystem.chart.TicketsByYear;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findTicketBySubmitter(Agent submitter);

    @Query("SELECT new ticketsystem.chart.TicketStatusCount(t.status, COUNT(t)) FROM Ticket t GROUP BY t.status")
    List<TicketStatusCount> groupByStatus();

    @Query("SELECT new ticketsystem.chart.TicketPriorityCount(t.priorityLevel, COUNT(t)) FROM Ticket t GROUP BY t.priorityLevel")
    List<TicketPriorityCount> groupByPriorityLevel();

    @Query("SELECT new ticketsystem.chart.TicketsByYear(YEAR(t.createdAt), COUNT(t))  FROM Ticket t GROUP BY YEAR(t.createdAt) ORDER BY YEAR(t.createdAt)")
    List<TicketsByYear> groupByYear();

    @Query("""
            SELECT new ticketsystem.chart.TicketByAssignment(sc.status, SUM(sc.count))
            FROM (SELECT CASE WHEN t.assigned IS NULL THEN 'Assigned' ELSE 'No assigned' END AS status, COUNT(*) AS count
                  FROM Ticket t
                  GROUP BY t.assigned
            ) as sc
            GROUP BY sc.status
            """)
    List<TicketByAssignment> groupByAssignment();
}
