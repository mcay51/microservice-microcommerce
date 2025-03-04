package tr.com.mcay.productservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tr.com.mcay.productservice.exception.ResourceNotFoundException;
import tr.com.mcay.productservice.exception.BadRequestException;
import tr.com.mcay.productservice.model.Product;
import tr.com.mcay.productservice.repository.ProductRepository;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    public Product createProduct(Product product) {
        if (product.getName() == null || product.getName().isEmpty()) {
            throw new BadRequestException("Product name cannot be empty");
        }
        if (product.getPrice() == null || product.getPrice() <= 0) {
            throw new BadRequestException("Product price must be greater than zero");
        }
        return productRepository.save(product);
    }
   // @CircuitBreaker(name = "default", fallbackMethod = "fallback")
    public Product updateProduct(Product product) {
        if (product.getId() == null) {
            throw new BadRequestException("Product ID cannot be null for update operation");
        }
        // Check if product exists
        productRepository.findById(product.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", product.getId()));
        
        if (product.getName() == null || product.getName().isEmpty()) {
            throw new BadRequestException("Product name cannot be empty");
        }
        if (product.getPrice() == null || product.getPrice() <= 0) {
            throw new BadRequestException("Product price must be greater than zero");
        }
        
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        // Check if product exists before deleting
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        productRepository.delete(product);
    }
    
    public String fallback(Long id, Throwable t) {
        return "Fallback response for product with ID: " + id;
    }
}
