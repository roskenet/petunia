package de.roskenet.petunia.villadiana.routes.admin;

import de.roskenet.petunia.villadiana.dto.CreatePlayerAccountRequest;
import de.roskenet.petunia.villadiana.dto.PlayerAccount;
import de.roskenet.petunia.villadiana.dto.UpdatePlayerAccountRequest;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/players")
public class AdminPlayersController {

    private final RestClient engineClient;




    public AdminPlayersController(
            @Value("${services.engine.base-url}") String engineBaseUrl
    ) {
        this.engineClient = RestClient.create(engineBaseUrl);
    }

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public List<PlayerAccount> getPlayers() {
        try {
            PlayerAccount[] accounts = engineClient.get()
                    .uri("/api/accounts")
                    .retrieve()
                    .body(PlayerAccount[].class);
            return accounts == null ? List.of() : Arrays.asList(accounts);
        } catch (RestClientResponseException ex) {
            throw toStatusException(ex);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin', 'trader')")
    public PlayerAccount getPlayer(@PathVariable UUID id) {
        try {
            return engineClient.get()
                    .uri("/api/accounts/{id}", id)
                    .retrieve()
                    .body(PlayerAccount.class);
        } catch (RestClientResponseException ex) {
            throw toStatusException(ex);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<PlayerAccount> createPlayer(@RequestBody CreatePlayerAccountRequest request) {
        try {
            PlayerAccount created = engineClient.post()
                    .uri("/api/accounts")
                    .body(request)
                    .retrieve()
                    .body(PlayerAccount.class);
            return ResponseEntity.status(201).body(created);
        } catch (RestClientResponseException ex) {
            throw toStatusException(ex);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public PlayerAccount updatePlayer(
            @PathVariable UUID id,
            @RequestBody UpdatePlayerAccountRequest request
    ) {
        try {
            return engineClient.put()
                    .uri("/api/accounts/{id}", id)
                    .body(request)
                    .retrieve()
                    .body(PlayerAccount.class);
        } catch (RestClientResponseException ex) {
            throw toStatusException(ex);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Void> deletePlayer(@PathVariable UUID id) {
        try {
            engineClient.delete()
                    .uri("/api/accounts/{id}", id)
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
