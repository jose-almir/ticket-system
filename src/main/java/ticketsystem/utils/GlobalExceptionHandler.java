package ticketsystem.utils;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleIntegrityViolation(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Unable to perform action.");
        return "redirect:/agents";
    }
}
