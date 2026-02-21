package de.petunia.villadiana.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationDemo {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationDemo(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void startSendNotification(Authentication authentication) {
        log.info("Start sending notifications for " + authentication.getName());

        var principal = (DefaultOidcUser) authentication.getPrincipal();

        var message = "Hello " + principal.getGivenName() + " " + principal.getFamilyName() + "!";

        Runnable theRunnable = () -> {
            for (int i = 0; i < 10; i++) {
                log.info("Sending notification " + i + " for " + authentication.getName());

                messagingTemplate.convertAndSend("/topic/petunias", "Allgemeine Nachricht!");

                messagingTemplate.convertAndSendToUser(
                        authentication.getName(),
                        "/queue/petunias", message);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Thread thread = new Thread(theRunnable);
        thread.start();

    }
}
