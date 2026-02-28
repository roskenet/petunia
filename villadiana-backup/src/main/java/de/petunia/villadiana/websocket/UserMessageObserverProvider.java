package de.petunia.villadiana.websocket;

import nakadi.BusinessEventMapped;
import nakadi.StreamObserver;
import nakadi.StreamObserverProvider;
import nakadi.TypeLiteral;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class UserMessageObserverProvider implements StreamObserverProvider<BusinessEventMapped<UserMessage>> {

    private final SimpMessagingTemplate template;

    public UserMessageObserverProvider(SimpMessagingTemplate template) {
        this.template = template;
    }


    @Override
    public StreamObserver createStreamObserver() {
        return new UserMessageObserver(template);
    }

    @Override
    public TypeLiteral typeLiteral() {
        return new TypeLiteral<BusinessEventMapped<UserMessage>>() {};
    }
}
