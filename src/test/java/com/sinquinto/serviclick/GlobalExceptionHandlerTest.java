package com.sinquinto.serviclick;

import com.sinquinto.serviclick.Exception.Domain.ResourceDuplicateException;
import com.sinquinto.serviclick.Exception.Domain.ResourceNotFoundException;
import com.sinquinto.serviclick.Exception.Infrastructure.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        webRequest = new ServletWebRequest(new MockHttpServletRequest("GET", "/api/test"));
    }

    @Test
    @DisplayName("handleResourceNotFoundException debe retornar 404 con detalles del error")
    void handleNotFoundException_shouldReturn404() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Recurso no encontrado");

        var response = handler.handleResourceNotFoundException(ex, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Recurso no encontrado", response.getBody().getMessage());
        assertEquals("NOT FOUND", response.getBody().getErrorCode());
    }

    @Test
    @DisplayName("handleResourceDuplicateException debe retornar 409 con detalles del error")
    void handleDuplicateException_shouldReturn409() {
        ResourceDuplicateException ex = new ResourceDuplicateException("Recurso ya existe");

        var response = handler.handleResourceDuplicateException(ex, webRequest);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Recurso ya existe", response.getBody().getMessage());
        assertEquals("DUPLICATE", response.getBody().getErrorCode());
    }
}