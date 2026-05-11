package com.sinquinto.serviclick.User.Infrastructure.Repository;

import com.sinquinto.serviclick.User.Infrastructure.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringUserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
}
