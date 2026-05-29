package com.sinquinto.serviclick.Log.Infrastructure;

import com.sinquinto.serviclick.Log.Domain.FrontendLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FrontendLogRepository extends JpaRepository<FrontendLog, Long> {
}
