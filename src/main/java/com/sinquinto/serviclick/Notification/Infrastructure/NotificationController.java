package com.sinquinto.serviclick.Notification.Infrastructure;

import com.sinquinto.serviclick.Notification.Application.NotificationService;
import com.sinquinto.serviclick.Notification.Domain.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService service;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> findByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.findByUserId(userId));
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<Notification>> findUnreadByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.findUnreadByUserId(userId));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Notification> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(service.markAsRead(id));
    }

    @PatchMapping("/user/{userId}/read-all")
    public ResponseEntity<Void> markAllAsRead(@PathVariable Long userId) {
        service.markAllAsRead(userId);
        return ResponseEntity.noContent().build();
    }
}