package com.sinquinto.serviclick.Exception.Domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class ResourceNotFoundException extends RuntimeException{
    String message;
}
