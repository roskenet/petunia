package de.petunia.villadiana.websocket;

import lombok.extern.slf4j.Slf4j;
import nakadi.BusinessEventMapped;
import nakadi.NakadiException;
import nakadi.Problem;
import nakadi.StreamBatch;
import nakadi.StreamBatchRecord;
import nakadi.StreamCursorContext;
import nakadi.StreamObserver;
import nakadi.StreamOffsetObserver;
import org.slf4j.MDC;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;
import java.util.Optional;

@Slf4j
public class UserMessageObserver implements StreamObserver<BusinessEventMapped<UserMessage>> {

    private final SimpMessagingTemplate template;

    public UserMessageObserver(SimpMessagingTemplate template) {
       this.template = template;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onNext(StreamBatchRecord<BusinessEventMapped<UserMessage>> record) {

        try {
            final StreamOffsetObserver offsetObserver = record.streamOffsetObserver();
            final StreamBatch<BusinessEventMapped<UserMessage>> batch = record.streamBatch();
            final StreamCursorContext context = record.streamCursorContext();

            MDC.put("cursor_context", context.toString());
            try {
                if (!batch.isEmpty()) {
                    final List<BusinessEventMapped<UserMessage>> events = batch.events();

                    for (BusinessEventMapped event : events) {
                        UserMessage message = (UserMessage) event.data();
                        log.info("LoggingStreamObserver message received: {}", message);
                        template.convertAndSendToUser(
                                message.getSubject(),
                                "/queue/petunias", message.getMessage());
                    }
                    offsetObserver.onNext(record.streamCursorContext());
                }
            } finally {
                MDC.remove("cursor_context");
            }
        } catch (NakadiException e) {
            throw e;
        } catch (Exception e) {
            throw new NakadiException(Problem.localProblem(e.getMessage(), ""), e);
        }
    }

    @Override
    public Optional<Long> requestBackPressure() {
        return Optional.empty();
    }

    @Override
    public Optional<Integer> requestBuffer() {
        return Optional.empty();
    }
}
