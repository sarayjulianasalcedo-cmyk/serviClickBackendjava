package com.sinquinto.serviclick.User.Infrastructure.Mapper;

import com.sinquinto.serviclick.User.Domain.User;
import com.sinquinto.serviclick.User.Infrastructure.DTO.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDTOMapper {
    UserDTO userToUserDTO(User user);
    User UserDTOToUser(UserDTO userDTO);
}
