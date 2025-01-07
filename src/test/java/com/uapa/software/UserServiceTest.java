package com.uapa.software;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.uapa.software.entities.User;
import com.uapa.software.repositories.UserRepository;
import com.uapa.software.services.CognitoService;
import com.uapa.software.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserServiceTest {

    @Mock
    private CognitoService cognitoService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize a test user
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");
    }

    @Test
    void testLogin_Success() {
        // Arrange
        String mockToken = "mockToken123";
        when(cognitoService.authenticate(testUser.getUsername(), testUser.getPassword())).thenReturn(mockToken);

        // Act
        String result = userService.login(testUser);

        // Assert
        assertNotNull(result);
        assertEquals(mockToken, result);
        verify(cognitoService, times(1)).authenticate(testUser.getUsername(), testUser.getPassword());
    }

    @Test
    void testLogin_Failure() {
        // Arrange
        when(cognitoService.authenticate(testUser.getUsername(), testUser.getPassword())).thenReturn(null);

        // Act
        String result = userService.login(testUser);

        // Assert
        assertNull(result);
        verify(cognitoService, times(1)).authenticate(testUser.getUsername(), testUser.getPassword());
    }

    @Test
    void testSaveAndLogin_Success() {
        // Arrange
        String mockToken = "mockToken123";
        when(cognitoService.registerUser(testUser.getUsername(), testUser.getPassword())).thenReturn("registered");
        when(cognitoService.authenticate(testUser.getUsername(), testUser.getPassword())).thenReturn(mockToken);
        when(userRepository.save(testUser)).thenReturn(testUser);

        // Act
        String result = userService.saveAndLogin(testUser);

        // Assert
        assertNotNull(result);
        assertEquals(mockToken, result);
        verify(cognitoService, times(1)).registerUser(testUser.getUsername(), testUser.getPassword());
        verify(cognitoService, times(1)).authenticate(testUser.getUsername(), testUser.getPassword());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void testSaveAndLogin_Failure() {
        // Arrange
        when(cognitoService.registerUser(testUser.getUsername(), testUser.getPassword())).thenReturn("registered");
        when(cognitoService.authenticate(testUser.getUsername(), testUser.getPassword())).thenReturn(null);

        // Act
        String result = userService.saveAndLogin(testUser);

        // Assert
        assertNull(result);
        verify(cognitoService, times(1)).registerUser(testUser.getUsername(), testUser.getPassword());
        verify(cognitoService, times(1)).authenticate(testUser.getUsername(), testUser.getPassword());
        verify(userRepository, never()).save(testUser);
    }
}
