package tr.com.mcay.stockservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.CompositeHealth;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health-check")
@Tag(name = "Health Check Controller", description = "API for custom health checks")
public class HealthCheckController {

    private final HealthEndpoint healthEndpoint;

    @Autowired
    public HealthCheckController(HealthEndpoint healthEndpoint) {
        this.healthEndpoint = healthEndpoint;
    }

    @Operation(summary = "Get detailed health status", description = "Returns detailed health information about the service")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service is healthy",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "503", description = "Service is unhealthy",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getHealthStatus() {
        HealthComponent health = healthEndpoint.health();
        Map<String, Object> healthDetails = new HashMap<>();
        
        healthDetails.put("status", health.getStatus().getCode());
        
        if (health instanceof CompositeHealth) {
            healthDetails.put("components", ((CompositeHealth) health).getComponents());
        }
        
        healthDetails.put("timestamp", System.currentTimeMillis());
        healthDetails.put("service", "stock-service");
        
        HttpStatus httpStatus = Status.UP.equals(health.getStatus()) ? 
                HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
        
        return new ResponseEntity<>(healthDetails, httpStatus);
    }

    @Operation(summary = "Get readiness status", description = "Checks if the service is ready to accept traffic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service is ready",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "503", description = "Service is not ready",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/readiness")
    public ResponseEntity<Health> getReadiness() {
        try {
            // Add specific readiness logic here if needed
            return ResponseEntity.ok(Health.up()
                    .withDetail("message", "Stock service is ready to accept traffic")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Health.down()
                            .withDetail("error", e.getMessage())
                            .withDetail("message", "Stock service is not ready")
                            .build());
        }
    }

    @Operation(summary = "Get liveness status", description = "Checks if the service is running")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service is alive",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "503", description = "Service is not alive",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/liveness")
    public ResponseEntity<Health> getLiveness() {
        try {
            // Simple check to verify the application is running
            return ResponseEntity.ok(Health.up()
                    .withDetail("message", "Stock service is alive")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Health.down()
                            .withDetail("error", e.getMessage())
                            .withDetail("message", "Stock service is not alive")
                            .build());
        }
    }
} 