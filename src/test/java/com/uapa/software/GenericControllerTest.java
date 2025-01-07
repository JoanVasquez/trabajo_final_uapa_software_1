package com.uapa.software;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.uapa.software.controllers.GenericController;
import com.uapa.software.entities.User;
import com.uapa.software.services.GenericService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class GenericControllerTest {

    private GenericController<User> genericController;
    private GenericService<User> mockGenericService;

    private User validUser;

    @BeforeEach
    void setUp() {
        mockGenericService = mock(GenericService.class);

        // Use an anonymous subclass of GenericController for testing
        genericController = new GenericController<>(mockGenericService) {
        };

        validUser = new User();
        validUser.setId(1);
        validUser.setUsername("testuser");
        validUser.setPassword("password123");
        validUser.setRol("USER");
    }

    @Test
    void testSave_Success() {
        when(mockGenericService.save(validUser)).thenReturn(validUser);

        User savedUser = genericController.save(validUser);

        assertNotNull(savedUser);
        assertEquals("testuser", savedUser.getUsername());
        verify(mockGenericService, times(1)).save(validUser);
    }

    @Test
    void testSave_Failure() {
        when(mockGenericService.save(validUser)).thenThrow(new RuntimeException("Save failed"));

        Exception exception = assertThrows(RuntimeException.class, () -> genericController.save(validUser));
        assertEquals("Save failed", exception.getMessage());
        verify(mockGenericService, times(1)).save(validUser);
    }

    @Test
    void testUpdate_Success() {
        when(mockGenericService.update(validUser)).thenReturn(true);

        boolean isUpdated = genericController.update(validUser);

        assertTrue(isUpdated);
        verify(mockGenericService, times(1)).update(validUser);
    }

    @Test
    void testUpdate_Failure() {
        when(mockGenericService.update(validUser)).thenReturn(false);

        boolean isUpdated = genericController.update(validUser);

        assertFalse(isUpdated);
        verify(mockGenericService, times(1)).update(validUser);
    }

    @Test
    void testDelete_Success() {
        when(mockGenericService.delete(validUser)).thenReturn(true);

        boolean isDeleted = genericController.delete(validUser);

        assertTrue(isDeleted);
        verify(mockGenericService, times(1)).delete(validUser);
    }

    @Test
    void testDelete_Failure() {
        when(mockGenericService.delete(validUser)).thenReturn(false);

        boolean isDeleted = genericController.delete(validUser);

        assertFalse(isDeleted);
        verify(mockGenericService, times(1)).delete(validUser);
    }

    @Test
    void testGetById_Success() {
        when(mockGenericService.getById("User", 1)).thenReturn(validUser);

        User user = genericController.getById("User", 1);

        assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals("testuser", user.getUsername());
        verify(mockGenericService, times(1)).getById("User", 1);
    }

    @Test
    void testGetById_NotFound() {
        when(mockGenericService.getById("User", 1)).thenReturn(null);

        User user = genericController.getById("User", 1);

        assertNull(user);
        verify(mockGenericService, times(1)).getById("User", 1);
    }

    @Test
    void testList_Success() {
        List<User> users = Arrays.asList(validUser, new User());
        when(mockGenericService.list("User")).thenReturn(users);

        List<User> userList = genericController.list("User");

        assertNotNull(userList);
        assertEquals(2, userList.size());
        verify(mockGenericService, times(1)).list("User");
    }

    @Test
    void testList_Empty() {
        when(mockGenericService.list("User")).thenReturn(Arrays.asList());

        List<User> userList = genericController.list("User");

        assertNotNull(userList);
        assertTrue(userList.isEmpty());
        verify(mockGenericService, times(1)).list("User");
    }
}
