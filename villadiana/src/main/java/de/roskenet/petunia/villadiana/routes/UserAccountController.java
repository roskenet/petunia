package de.roskenet.petunia.villadiana.routes;

import de.roskenet.petunia.dto.AssetDto;
import de.roskenet.petunia.dto.OrderResponse;
import de.roskenet.petunia.dto.PlaceOrderRequest;
import de.roskenet.petunia.dto.PlayerAccountDto;
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
@RequestMapping("/api/me")
public class UserAccountController {

    private final RestClient engineClient;

    public UserAccountController(
            @Value("${services.engine.base-url}") String engineBaseUrl
    ) {
        this.engineClient = RestClient.create(engineBaseUrl);
    }

    @GetMapping("/account/{id}")
    @PreAuthorize("hasAnyRole('admin', 'trader')")
    public PlayerAccountDto getMyAccount(@PathVariable UUID id) {
        // In a real app, we should verify that 'id' belongs to the authenticated user.
        // For now, we assume the frontend sends the correct 'sub' from UserInfo.
        try {
            return engineClient.get()
                    .uri("/api/accounts/{id}", id)
                    .retrieve()
                    .body(PlayerAccountDto.class);
        } catch (RestClientResponseException ex) {
            throw toStatusException(ex);
        }
    }

    @GetMapping("/account/{id}/assets")
    @PreAuthorize("hasAnyRole('admin', 'trader')")
    public List<AssetDto> getMyAssets(@PathVariable UUID id) {
        try {
            AssetDto[] assets = engineClient.get()
                    .uri("/api/accounts/{id}/assets", id)
                    .retrieve()
                    .body(AssetDto[].class);
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
