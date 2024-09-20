package pt.aubay.resilience_demo.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import pt.aubay.resilience_demo.service.ResilienceExampleService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

import java.util.concurrent.CompletableFuture;

@Log4j2
@RestController
@RequestMapping("/api/v1/resilience")
public class ResilienceExampleController {

    private final ResilienceExampleService resilienceExampleService;

    public ResilienceExampleController(ResilienceExampleService resilienceExampleService) {
        this.resilienceExampleService = resilienceExampleService;
    }


    @GetMapping
    @CircuitBreaker(name = "backend")
    @RateLimiter(name = "backend")
    @Retry(name = "backend")
    public CompletableFuture<String> listingExampleMethod(@RequestParam(name = "delay") boolean delay) {
        log.info("Simulating list...");
        log.info("Delay {}", delay);
        return CompletableFuture.supplyAsync(() -> {
            // Simulate long-running task
            return resilienceExampleService.performOperationWithResilience(delay);
        });
    }

    @PostMapping
    public String writingExampleMethod() {
        log.info("Simulating post...");
        return resilienceExampleService.performOperationWithResilience();
    }

    @PutMapping
    public String updatingExampleMethod() {
        log.info("Simulating update...");
        return resilienceExampleService.performOperationWithResilience();
    }
}
