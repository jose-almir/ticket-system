package ticketsystem.agent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
    @Query(value = "SELECT * FROM agent WHERE CONCAT(first_name,' ',last_name) LIKE %:fullName%", nativeQuery = true)
    List<Agent> findByFullName(String fullName);

    boolean existsAgentByEmail(String email);
}
