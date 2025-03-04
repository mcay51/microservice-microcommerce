package tr.com.mcay.stockservice.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import tr.com.mcay.stockservice.repository.StockRepository;

@Component
public class StockServiceHealthIndicator implements HealthIndicator {

    private final StockRepository stockRepository;

    @Autowired
    public StockServiceHealthIndicator(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public Health health() {
        try {
            // Check if the repository is accessible
            long stockCount = stockRepository.count();
            
            return Health.up()
                    .withDetail("stockCount", stockCount)
                    .withDetail("message", "Stock service is running normally")
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("error", e.getMessage())
                    .withDetail("message", "Stock service is not functioning properly")
                    .build();
        }
    }
} 