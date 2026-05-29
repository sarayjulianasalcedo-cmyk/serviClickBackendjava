package com.sinquinto.serviclick.Log.Domain;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "frontend_logs")
public class FrontendLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String timestamp;
    private String level;
    private String category;

    @Column(length = 1000)
    private String message;

    @Column(length = 2000)
    private String detail;

    private LocalDateTime receivedAt = LocalDateTime.now();
}