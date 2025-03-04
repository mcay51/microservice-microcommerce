package tr.com.mcay.orderservice.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tr.com.mcay.orderservice.repository.OrderRepository;

@Component
public class OrderServiceHealthIndicator implements HealthIndicator {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public OrderServiceHealthIndicator(OrderRepository orderRepository, RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public Health health() {
        try {
            // Check if the repository is accessible
            long orderCount = orderRepository.count();
            
            // Check if dependent services are accessible
            boolean productServiceAccessible = checkProductServiceHealth();
            
            if (!productServiceAccessible) {
                return Health.down()
                        .withDetail("orderCount", orderCount)
                        .withDetail("message", "Product service is not accessible")
                        .build();
            }
            
            return Health.up()
                    .withDetail("orderCount", orderCount)
                    .withDetail("productServiceAccessible", productServiceAccessible)
                    .withDetail("message", "Order service is running normally")
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("error", e.getMessage())
                    .withDetail("message", "Order service is not functioning properly")
                    .build();
        }
    }
    
    private boolean checkProductServiceHealth() {
        try {
            // Simple ping to product service
            // In a real environment, you might want to use a circuit breaker here
            restTemplate.getForObject("http://product-service:8081/health-check/liveness", String.class);
            return true;
        } catch (Exception e) {
            // Log the exception but don't fail the health check completely
            return false;
        }
    }
} 