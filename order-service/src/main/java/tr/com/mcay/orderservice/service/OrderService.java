package tr.com.mcay.orderservice.service;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.web.client.RestTemplate;
import tr.com.mcay.orderservice.exception.ResourceNotFoundException;
import tr.com.mcay.orderservice.exception.BadRequestException;
import tr.com.mcay.orderservice.exception.ServiceCommunicationException;
import tr.com.mcay.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.com.mcay.orderservice.model.Orders;
import tr.com.mcay.orderservice.dto.StockRequest;

import java.util.List;

@Service
public class OrderService {
    private final RestTemplate restTemplate;
    private final OrderRepository orderRepository;
    @Autowired
    public OrderService(OrderRepository orderRepository,RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
    }

    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    public Orders getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
    }

    public Orders createOrder(Orders order) {
        if (order.getProductId() == null) {
            throw new BadRequestException("Product ID cannot be null");
        }
        if (order.getQuantity() <= 0) {
            throw new BadRequestException("Order quantity must be greater than zero");
        }
        return orderRepository.save(order);
    }

    public Orders updateOrder(Orders order) {
        if (order.getId() == null) {
            throw new BadRequestException("Order ID cannot be null for update operation");
        }
        // Check if order exists
        orderRepository.findById(order.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", order.getId()));
        
        if (order.getProductId() == null) {
            throw new BadRequestException("Product ID cannot be null");
        }
        if (order.getQuantity() <= 0) {
            throw new BadRequestException("Order quantity must be greater than zero");
        }
        
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        // Check if order exists before deleting
        Orders order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        orderRepository.delete(order);
    }
    
    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackProductService")
    @RateLimiter(name = "getProductDetailsRateLimiter")
    @Retry(name = "getProductDetailsRetry",fallbackMethod ="fallbackProductServiceRetry" )
    public String getProductDetails(Long productId) {
        try {
            if (productId == null || productId <= 0) {
                throw new BadRequestException("Product ID must be a positive number");
            }
            return restTemplate.getForObject("http://product-service:8081/products/" + productId, String.class);
        } catch (Exception e) {
            throw new ServiceCommunicationException("product-service", "get product details", e);
        }
    }

    @Retry(name = "getProductDetailsRetry",fallbackMethod ="fallbackProductServiceRetry" )
    public String getProductDetailsRetry(Long productId) {
        try {
            System.out.println("Trying to fetch product details...");
            if (productId == null || productId <= 0) {
                throw new BadRequestException("Product ID must be a positive number");
            }
            return restTemplate.getForObject("http://product-service:8081/products/" + productId, String.class);
        } catch (Exception e) {
            throw new ServiceCommunicationException("product-service", "get product details with retry", e);
        }
    }
    
    public String fallbackProductService(Long productId, Throwable throwable) {
        return "Product service is currently unavailable. Please try again later. "+throwable.getMessage();
    }
    
    public String fallbackProductServiceRetry(Long productId, Throwable throwable) {
        return "Product service is currently unavailable. Please try again later. "+throwable.getMessage();
    }

    @Bulkhead(name = "orderServiceBulkhead", fallbackMethod = "orderFallback")
    public String createStock(Long productId, int quantity) {
        try {
            if (productId == null || productId <= 0) {
                throw new BadRequestException("Product ID must be a positive number");
            }
            if (quantity <= 0) {
                throw new BadRequestException("Quantity must be greater than zero");
            }
            return restTemplate.postForObject("http://stock-service:8083/stocks", new StockRequest(productId, quantity), String.class);
        } catch (Exception e) {
            throw new ServiceCommunicationException("stock-service", "create stock", e);
        }
    }
    
    public String orderFallback(Long productId, int quantity, Throwable throwable) {
        System.err.println("Order service is overloaded or failed: " + throwable.getMessage());
        return "Order service is currently overloaded. Please try again later.";
    }
}
