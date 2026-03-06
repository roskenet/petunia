package de.roskenet.petunia.villadiana.routes.admin;

import de.roskenet.petunia.dto.CreatePlayerAccountRequest;
import de.roskenet.petunia.dto.PlayerAccountDto;
import de.roskenet.petunia.dto.UpdatePlayerAccountRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/admin/players")
public class AdminPlayersController {

    private final RestClient engineClient;

    public AdminPlayersController(
            @Value("${services.engine.base-url:http://localhost:8081}") String engineBaseUrl
    ) {
        this.engineClient = RestClient.create(engineBaseUrl);
    }

    @GetMapping
    public List<PlayerAccountDto> getPlayers() {
        try {
            PlayerAccountDto[] accounts = engineClient.get()
                    .uri("/api/accounts")
                    .retrieve()
                    .body(PlayerAccountDto[].class);
            return accounts == null ? List.of() : Arrays.asList(accounts);
        } catch (RestClientResponseException ex) {
            throw toStatusException(ex);
        }
    }

    @GetMapping("/{playerName}")
    public PlayerAccountDto getPlayer(@PathVariable String playerName) {
        try {
            return engineClient.get()
                    .uri("/api/accounts/{playerName}", playerName)
                    .retrieve()
                    .body(PlayerAccountDto.class);
        } catch (RestClientResponseException ex) {
            throw toStatusException(ex);
        }
    }

    @PostMapping
    public ResponseEntity<PlayerAccountDto> createPlayer(@RequestBody CreatePlayerAccountRequest request) {
        try {
            PlayerAccountDto created = engineClient.post()
                    .uri("/api/accounts")
                    .body(request)
                    .retrieve()
                    .body(PlayerAccountDto.class);
            return ResponseEntity.status(201).body(created);
        } catch (RestClientResponseException ex) {
            throw toStatusException(ex);
        }
    }

    @PutMapping("/{playerName}")
    public PlayerAccountDto updatePlayer(
            @PathVariable String playerName,
            @RequestBody UpdatePlayerAccountRequest request
    ) {
        try {
            return engineClient.put()
                    .uri("/api/accounts/{playerName}", playerName)
                    .body(request)
                    .retrieve()
                    .body(PlayerAccountDto.class);
        } catch (RestClientResponseException ex) {
            throw toStatusException(ex);
        }
    }

    @DeleteMapping("/{playerName}")
    public ResponseEntity<Void> deletePlayer(@PathVariable String playerName) {
        try {
            engineClient.delete()
                    .uri("/api/accounts/{playerName}", playerName)
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
