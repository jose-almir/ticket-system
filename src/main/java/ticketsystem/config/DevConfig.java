package ticketsystem.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ticketsystem.utils.PopulateService;

@Configuration
@Profile({"dev", "test"})
public class DevConfig {
    @Autowired
    private PopulateService populateService;

    @PostConstruct
    public void init() {
        populateService.populate();
    }
}
