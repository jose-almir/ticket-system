package ticketsystem.agent;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewAgentForm {
    @NotNull
    @NotBlank
    @Size(max = 80)
    private String firstName;

    @NotNull
    @NotBlank
    @Size(max = 200)
    private String lastName;

    @NotNull
    @NotBlank
    @Email
    @Size(max = 120)
    private String email;

    @NotNull
    @NotBlank
    @Size(max = 25)
    private String phone;

    @NotBlank
    @Size(min = 6, message = "min. 6 caracteres")
    private String password;
}
