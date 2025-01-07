package com.uapa.software;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.uapa.software.repositories.GenericRepository;
import com.uapa.software.services.GenericService;

class GenericServiceTest {

    // Mock repository
    @Mock
    private GenericRepository<Object> mockRepository;

    // Test implementation of GenericService
    private GenericService<Object> service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Anonymous class implementation for testing
        service = new GenericService<>() {
            @Override
            protected GenericRepository<Object> getRepository() {
                return mockRepository;
            }
        };
    }

    @Test
    void testSave() {
        Object entity = new Object();
        when(mockRepository.save(entity)).thenReturn(entity);

        Object result = service.save(entity);

        assertNotNull(result);
        assertEquals(entity, result);
        verify(mockRepository, times(1)).save(entity);
    }

    @Test
    void testUpdate() {
        Object entity = new Object();
        when(mockRepository.update(entity)).thenReturn(true);

        boolean result = service.update(entity);

        assertTrue(result);
        verify(mockRepository, times(1)).update(entity);
    }

    @Test
    void testDelete() {
        Object entity = new Object();
        when(mockRepository.delete(entity)).thenReturn(true);

        boolean result = service.delete(entity);

        assertTrue(result);
        verify(mockRepository, times(1)).delete(entity);
    }

    @Test
    void testGetById() {
        Object entity = new Object();
        when(mockRepository.findById(1)).thenReturn(Optional.of(entity));

        Object result = service.getById("Object", 1);

        assertNotNull(result);
        assertEquals(entity, result);
        verify(mockRepository, times(1)).findById(1);
    }

    @Test
    void testGetById_NotFound() {
        when(mockRepository.findById(1)).thenReturn(Optional.empty());

        Object result = service.getById("Object", 1);

        assertNull(result);
        verify(mockRepository, times(1)).findById(1);
    }

    @Test
    void testList() {
        List<Object> entities = Arrays.asList(new Object(), new Object());
        when(mockRepository.findAll()).thenReturn(entities);

        List<Object> result = service.list("Object");

        assertNotNull(result);
        assertEquals(entities.size(), result.size());
        assertEquals(entities, result);
        verify(mockRepository, times(1)).findAll();
    }
}
