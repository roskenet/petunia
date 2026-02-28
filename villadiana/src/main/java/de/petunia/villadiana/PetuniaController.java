package de.petunia.villadiana;

import de.petunia.villadiana.axillaris.AxillarisGateway;
import de.petunia.villadiana.websocket.NotificationDemo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class PetuniaController {

    @Autowired
    private AxillarisGateway axillarisGateway;

//    @Autowired
//    private NotificationDemo notificationDemo;

    @GetMapping("/petunias")
    public List<PetuniaSpecies> getPetuniaSpecies() {
        var authentication= SecurityContextHolder.getContext().getAuthentication();
//        notificationDemo.startSendNotification(authentication);
        return axillarisGateway.getAllPetunias();
    }

    @PostMapping("/petunias")
    public PetuniaSpecies postPetuniaSpecies(@RequestBody PetuniaSpecies species) {
        log.info("Received species: {}", species);
        return species;
    }
}
