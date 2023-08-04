package ticketsystem.ticket;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ticketsystem.agent.Agent;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@ToString
public class Ticket {
    public enum Priority {LOW, MEDIUM, HIGH, URGENT}

    public enum Status {OPEN, RESOLVED, CLOSED}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 160, nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT NOT NULL")
    private String description;

    @Column(nullable = false)
    private Priority priorityLevel;

    @Column(nullable = false)
    private Status status;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "fk_assigned")
    private Agent assigned;

    @ManyToOne
    @JoinColumn(name = "fk_submitter", nullable = false)
    private Agent submitter;

    public Ticket(String subject, String description, Priority priorityLevel, Status status, Agent submitter) {
        this.subject = subject;
        this.description = description;
        this.priorityLevel = priorityLevel;
        this.status = status;
        this.submitter = submitter;
    }
}
