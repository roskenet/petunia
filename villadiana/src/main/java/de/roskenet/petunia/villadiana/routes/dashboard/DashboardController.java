package de.roskenet.petunia.villadiana.routes.dashboard;

import de.roskenet.petunia.dto.OrderResponse;
import de.roskenet.petunia.dto.PlaceOrderRequest;
import de.roskenet.petunia.villadiana.dto.Asset;
import de.roskenet.petunia.villadiana.dto.PlayerAccount;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final RestClient engineClient;

    public DashboardController(
            @Value("${services.engine.base-url}") String engineBaseUrl
    ) {
        this.engineClient = RestClient.create(engineBaseUrl);
    }

    @GetMapping("/account/{id}")
    @PreAuthorize("hasAnyRole('admin', 'trader')")
    public PlayerAccount getMyAccount(@PathVariable UUID id) {
        try {
            return engineClient.get()
                    .uri("/api/accounts/{id}", id)
                    .retrieve()
                    .body(PlayerAccount.class);
        } catch (RestClientResponseException ex) {
            throw toStatusException(ex);
        }
    }

    @GetMapping("/account/{id}/assets")
    @PreAuthorize("hasAnyRole('admin', 'trader')")
    public List<Asset> getMyAssets(@PathVariable UUID id) {
        try {
            Asset[] assets = engineClient.get()
                    .uri("/api/accounts/{id}/assets", id)
                    .retrieve()
                    .body(Asset[].class);
            return assets == null ? List.of() : Arrays.asList(assets);
        } catch (RestClientResponseException ex) {
            throw toStatusException(ex);
        }
    }

    @PostMapping("/orders")
    @PreAuthorize("hasAnyRole('admin', 'trader')")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody PlaceOrderRequest request) {
        try {
            OrderResponse response = engineClient.post()
                    .uri("/api/orders")
                    .body(request)
                    .retrieve()
                    .body(OrderResponse.class);
            return ResponseEntity.status(201).body(response);
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
