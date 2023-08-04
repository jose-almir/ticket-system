package ticketsystem.agent;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ticketsystem.ticket.TicketRepository;
import ticketsystem.utils.UsernameGenerator;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

@Controller
@RequestMapping("/agents")
public class AgentController {

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @GetMapping
    public String agentList(
            @RequestParam(value = "q", required = false) String searchTerm,
            Model template,
            @ModelAttribute("error") String error,
            @ModelAttribute("info") String info
    ) {
        boolean hasValidSearchTerm = searchTerm != null && !searchTerm.isBlank();
        template.addAttribute("agentList", hasValidSearchTerm ? agentRepository.findByFullName(searchTerm) : agentRepository.findAll());
        template.addAttribute("error", error);
        template.addAttribute("info", info);
        template.addAttribute("q", searchTerm);
        return "agent/list-agents";
    }

    @GetMapping("/new")
    public String agentNew(Model template) {
        template.addAttribute("form", new NewAgentForm());
        return "agent/new-agent";
    }

    @PostMapping("/save")
    public String agentSave(@Valid @ModelAttribute("form") NewAgentForm form, BindingResult bindingResult, Model template) {
        if (bindingResult.hasErrors()) {
            return "agent/new-agent";
        }

        if (agentRepository.existsAgentByEmail(form.getEmail())) {
            template.addAttribute("error", "Email already exists.");
            return "agent/new-agent";
        }

        Agent newAgent = new Agent();
        newAgent.setFirstName(form.getFirstName());
        newAgent.setLastName(form.getLastName());
        newAgent.setEmail(form.getEmail());
        newAgent.setPhone(form.getPhone());
        newAgent.setUsername(UsernameGenerator.generateUsername(form.getFirstName(), form.getEmail()));

        agentRepository.save(newAgent);

        return "redirect:/agents";
    }


    @GetMapping("/{id}/details")
    public String agentDetails(@PathVariable Long id, Model template, RedirectAttributes attributes) {
        Optional<Agent> agentDetails = agentRepository.findById(id);

        if (agentDetails.isEmpty()) {
            attributes.addFlashAttribute("error", String.format("Agent ID %d doesn't exist.", id));
            return "redirect:/agents";
        }

        template.addAttribute("agentDetails", agentDetails.get());
        template.addAttribute("agentSubmittedTickets", ticketRepository.findTicketBySubmitter(agentDetails.get()));
        return "agent/detail-agent";
    }

    @GetMapping("/{id}/edit")
    public String agentEdit(@PathVariable Long id, Model template, RedirectAttributes attributes) {
        Optional<Agent> agent = agentRepository.findById(id);
        if (agent.isEmpty()) {
            attributes.addFlashAttribute("error", String.format("Agent ID %d doesn't exist.", id));
            return "redirect:/agents";
        }

        template.addAttribute("agentDetails", agent.get());
        return "agent/edit-agent";
    }

    @PostMapping("/{id}/delete")
    public String agentDelete(@PathVariable Long id, RedirectAttributes attributes) {
        agentRepository.deleteById(id);
        attributes.addFlashAttribute("info", String.format("Agent ID %d removed.", id));
        return "redirect:/agents";
    }
}
