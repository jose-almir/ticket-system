package ticketsystem.chart;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ticketsystem.ticket.TicketRepository;

@Controller
@RequestMapping("/charts")
public class ChartController {
    @Autowired
    private TicketRepository ticketRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping
    public String showCharts(Model template) throws JsonProcessingException {
        template.addAttribute("ticketStatusCounts", objectMapper.writeValueAsString(ticketRepository.groupByStatus()));
        template.addAttribute("ticketPriorityCounts", objectMapper.writeValueAsString(ticketRepository.groupByPriorityLevel()));
        template.addAttribute("ticketsByYear", objectMapper.writeValueAsString(ticketRepository.groupByYear()));
        template.addAttribute("ticketsByAssignment", ticketRepository.groupByAssignment());
        return "charts/all";
    }
}
