package ticketsystem.agent;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ticketsystem.ticket.TicketRepository;
import ticketsystem.utils.UsernameGenerator;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

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

    @PostMapping("/{id}/picture")
    public String agentUploadPicture(@PathVariable Long id, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        Optional<Agent> agent = agentRepository.findById(id);
        if (agent.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", String.format("Agent ID %d doesn't exist.", id));
            return "redirect:/agents";
        }

        Agent agentDetails = agent.get();

        try {
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Error uploading file. File is empty.");
                return "redirect:/agents";
            }

            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String newFileName = UUID.randomUUID().toString() + "." + extension;


            Path rootLocation = Paths.get("upload-dir");
            Path destinationFile = rootLocation.resolve(
                            Paths.get(Objects.requireNonNull(newFileName)))
                    .normalize().toAbsolutePath();

            if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
                redirectAttributes.addFlashAttribute("error", "Cannot store file outside current directory.");
                return "redirect:/agents";
            }

            if(agentDetails.getProfilePicture() != null) {
                Path oldFile = rootLocation.resolve(agentDetails.getProfilePicture());
                Files.deleteIfExists(oldFile);
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            agentDetails.setProfilePicture(newFileName);
            agentRepository.save(agentDetails);

        } catch (IOException e) {
            System.out.println(e);
            redirectAttributes.addFlashAttribute("error", "Error uploading file. Please try again.");
            return "redirect:/agents";
        }

        return "redirect:/agents/" + id + "/details";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename, RedirectAttributes redirectAttributes) {
        Path rootLocation = Paths.get("upload-dir");
        Path file = rootLocation.resolve(filename);
        Resource resource = null;
        try {
            resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"").body(resource);
            } else {
                throw new RuntimeException("Could not read the file!");
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}
