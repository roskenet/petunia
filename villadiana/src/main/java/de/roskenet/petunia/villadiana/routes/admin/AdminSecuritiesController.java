package de.roskenet.petunia.villadiana.routes.admin;

import de.roskenet.petunia.dto.CreateSecurityRequest;
import de.roskenet.petunia.dto.SecurityDto;
import de.roskenet.petunia.dto.UpdateSecurityRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/admin/securities")
public class AdminSecuritiesController {

    private final RestClient engineClient;

    public AdminSecuritiesController(
            @Value("${services.engine.base-url:http://localhost:8081}") String engineBaseUrl
    ) {
        this.engineClient = RestClient.create(engineBaseUrl);
    }

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public List<SecurityDto> getSecurities() {
        try {
            SecurityDto[] securities = engineClient.get()
                    .uri("/api/securities")
                    .retrieve()
                    .body(SecurityDto[].class);
            return securities == null ? List.of() : Arrays.asList(securities);
        } catch (RestClientResponseException ex) {
            throw toStatusException(ex);
        }
    }

    @GetMapping("/{symbol}")
    @PreAuthorize("hasRole('admin')")
    public SecurityDto getSecurity(@PathVariable String symbol) {
        try {
            return engineClient.get()
                    .uri("/api/securities/{symbol}", symbol)
                    .retrieve()
                    .body(SecurityDto.class);
        } catch (RestClientResponseException ex) {
            throw toStatusException(ex);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<SecurityDto> createSecurity(@RequestBody CreateSecurityRequest request) {
        try {
            SecurityDto created = engineClient.post()
                    .uri("/api/securities")
                    .body(request)
                    .retrieve()
                    .body(SecurityDto.class);
            return ResponseEntity.status(201).body(created);
        } catch (RestClientResponseException ex) {
            throw toStatusException(ex);
        }
    }

    @PutMapping("/{symbol}")
    @PreAuthorize("hasRole('admin')")
    public SecurityDto updateSecurity(
            @PathVariable String symbol,
            @RequestBody UpdateSecurityRequest request
    ) {
        try {
            return engineClient.put()
                    .uri("/api/securities/{symbol}", symbol)
                    .body(request)
                    .retrieve()
                    .body(SecurityDto.class);
        } catch (RestClientResponseException ex) {
            throw toStatusException(ex);
        }
    }

    @DeleteMapping("/{symbol}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Void> deleteSecurity(@PathVariable String symbol) {
        try {
            engineClient.delete()
                    .uri("/api/securities/{symbol}", symbol)
                    .retrieve()
                    .toBodilessEntity();
            return ResponseEntity.noContent().build();
        } catch (RestClientResponseException ex) {
            throw toStatusException(ex);
        }
    }

    private ResponseStatusException toStatusException(RestClientResponseException ex) {
        String message = ex.getResponseBodyAsString();
        if (message.isBlank()) {
            message = ex.getMessage();
        }
        return new ResponseStatusException(ex.getStatusCode(), message, ex);
    }
}

