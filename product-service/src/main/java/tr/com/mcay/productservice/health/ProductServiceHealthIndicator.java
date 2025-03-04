package tr.com.mcay.productservice.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import tr.com.mcay.productservice.repository.ProductRepository;

@Component
public class ProductServiceHealthIndicator implements HealthIndicator {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceHealthIndicator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Health health() {
        try {
            // Check if the repository is accessible
            long productCount = productRepository.count();
            
            return Health.up()
                    .withDetail("productCount", productCount)
                    .withDetail("message", "Product service is running normally")
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("error", e.getMessage())
                    .withDetail("message", "Product service is not functioning properly")
                    .build();
        }
    }
} 