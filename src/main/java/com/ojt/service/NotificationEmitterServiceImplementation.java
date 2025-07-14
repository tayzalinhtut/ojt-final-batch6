package com.ojt.service;

import com.ojt.entity.Notification;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationEmitterServiceImplementation implements NotificationEmitterService {
    private final Map<String, SseEmitter> clients = new ConcurrentHashMap();

    @Override
    public SseEmitter addClient(String clientId) {
        SseEmitter emitter = new SseEmitter(0L);
        clients.put(clientId, emitter);

        emitter.onCompletion(() -> clients.remove(clientId));
        emitter.onTimeout(() -> clients.remove(clientId));
        emitter.onError((e) -> clients.remove(clientId));

        return emitter;
    }

    @Override
    public void broadcastToAdmin(Notification notification) {
        for(Map.Entry<String, SseEmitter> client : clients.entrySet()) {
            String clientId = client.getKey();
            SseEmitter emitter = client.getValue();

            if(clientId.equals("admin")) {
                try {
                    emitter.send(SseEmitter.event()
                            .name("notification")
                            .data(notification));
                } catch(IOException e) {
                    emitter.completeWithError(e);
                }
            }
        }
    }
}
