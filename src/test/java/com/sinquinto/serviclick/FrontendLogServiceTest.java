package com.sinquinto.serviclick;

import com.sinquinto.serviclick.Log.Application.FrontendLogService;
import com.sinquinto.serviclick.Log.Domain.FrontendLog;
import com.sinquinto.serviclick.Log.Infrastructure.FrontendLogRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FrontendLogServiceTest {

    @Mock
    private FrontendLogRepository repository;

    @InjectMocks
    private FrontendLogService service;

    @Test
    @DisplayName("saveLogs debe delegar al repositorio")
    void saveLogs_shouldDelegate() {
        List<FrontendLog> logs = List.of(new FrontendLog());
        when(repository.saveAll(anyList())).thenReturn(logs);

        service.saveLogs(logs);

        verify(repository).saveAll(logs);
    }

    @Test
    @DisplayName("getLogs debe retornar todos los logs del repositorio")
    void getLogs_shouldReturnAll() {
        List<FrontendLog> logs = List.of(new FrontendLog());
        when(repository.findAll()).thenReturn(logs);

        List<FrontendLog> result = service.getLogs();

        assertEquals(1, result.size());
        verify(repository).findAll();
    }
}