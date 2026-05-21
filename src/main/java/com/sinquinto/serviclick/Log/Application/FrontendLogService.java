package com.sinquinto.serviclick.Log.Application;

import com.sinquinto.serviclick.Log.Domain.FrontendLog;
import com.sinquinto.serviclick.Log.Infrastructure.FrontendLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FrontendLogService {

    private final FrontendLogRepository repository;

    public void saveLogs(List<FrontendLog> logs) {
        repository.saveAll(logs);
    }

    public List<FrontendLog> getLogs() {
        return repository.findAll();
    }
}