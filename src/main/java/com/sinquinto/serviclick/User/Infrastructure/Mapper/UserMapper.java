package com.sinquinto.serviclick.User.Infrastructure.Mapper;

import com.sinquinto.serviclick.User.Domain.User;
import com.sinquinto.serviclick.User.Infrastructure.Entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User userEntityToUser(UserEntity userEntity);
    UserEntity userToUserEntity(User user);
}
