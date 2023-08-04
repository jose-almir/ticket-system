package ticketsystem.agent;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@ToString
public class Agent {
    public enum Role {ADMINISTRATOR, SUPPORT}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 80, nullable = false)
    private String firstName;

    @Column(length = 200, nullable = false)
    private String lastName;

    @Column(length = 120, unique = true, nullable = false)
    private String email;

    @Column(length = 25, unique = true, nullable = false)
    private String phone;

    @Column(length = 16, unique = true, nullable = false)
    private String username;

    private String password;

    private String profilePicture;

    @Column(nullable = false)
    private Role role = Role.SUPPORT;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Agent(String firstName, String lastName, String email, String phone, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.username = username;
    }
}
