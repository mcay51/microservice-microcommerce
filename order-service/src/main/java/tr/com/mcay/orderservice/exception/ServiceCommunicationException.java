package tr.com.mcay.orderservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class ServiceCommunicationException extends RuntimeException {
    
    public ServiceCommunicationException(String message) {
        super(message);
    }
    
    public ServiceCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ServiceCommunicationException(String serviceName, String operation) {
        super(String.format("Failed to %s with %s service", operation, serviceName));
    }
    
    public ServiceCommunicationException(String serviceName, String operation, Throwable cause) {
        super(String.format("Failed to %s with %s service", operation, serviceName), cause);
    }
} 