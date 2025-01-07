package com.uapa.software;

import static org.junit.jupiter.api.Assertions.*;

import com.uapa.software.controllers.UserController;
import com.uapa.software.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserControllerTest {

    private UserController userController;

    private User validUser;

    @BeforeEach
    void setUp() {
        System.setProperty("env", "test");
        // Initialize UserController which will use H2 via HibernateUtil
        userController = new UserController();

        // Set up valid user
        validUser = new User();
        validUser.setUsername("testuser");
        validUser.setPassword("password123");
        validUser.setRol("USER");
    }

    @Test
    void testLogin_Success() {
        // Save the user and test login
        userController.save(validUser);

        String token = userController.login(validUser);

        assertNotNull(token);
        assertEquals("mock-jwt-token", token); // Replace "mockToken" with the expected token if needed
    }

    @Test
    void testLogin_ThrowsException_WhenUsernameOrPasswordIsEmpty() {
        validUser.setUsername("");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userController.login(validUser));

        assertEquals("Username and password must be filled", exception.getMessage());
    }

    @Test
    void testSaveAndLogin_Success() {
        String token = userController.saveAndLogin(validUser);

        assertNotNull(token);
        assertEquals("mock-jwt-token", token); // Replace "mockToken" with the expected token if needed
    }

    @Test
    void testSave_Success() {
        User savedUser = userController.save(validUser);

        assertNotNull(savedUser);
        assertEquals("testuser", savedUser.getUsername());
    }

    @Test
    void testUpdate_Success() {
        validUser.setId(1);
        validUser.setUsername("updateduser");

        boolean isUpdated = userController.update(validUser);

        assertTrue(isUpdated);
    }

    @Test
    void testLogin_ThrowsException_WhenUsernameIsEmpty() {
        validUser.setUsername("");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userController.login(validUser));
        assertEquals("Username and password must be filled", exception.getMessage());
    }

    @Test
    void testLogin_ThrowsException_WhenPasswordIsEmpty() {
        validUser.setPassword("");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userController.login(validUser));
        assertEquals("Username and password must be filled", exception.getMessage());
    }

    @Test
    void testSaveAndLogin_ThrowsException_WhenUsernameIsEmpty() {
        validUser.setUsername("");

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> userController.saveAndLogin(validUser));
        assertEquals("Every fields must be filled", exception.getMessage());
    }

    @Test
    void testSaveAndLogin_ThrowsException_WhenPasswordIsEmpty() {
        validUser.setPassword("");

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> userController.saveAndLogin(validUser));
        assertEquals("Every fields must be filled", exception.getMessage());
    }

    @Test
    void testSaveAndLogin_ThrowsException_WhenRoleIsEmpty() {
        validUser.setRol("");

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> userController.saveAndLogin(validUser));
        assertEquals("Every fields must be filled", exception.getMessage());
    }

    @Test
    void testSave_ThrowsException_WhenUsernameIsEmpty() {
        validUser.setUsername("");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userController.save(validUser));
        assertEquals("Every fields must be filled", exception.getMessage());
    }

    @Test
    void testSave_ThrowsException_WhenPasswordIsEmpty() {
        validUser.setPassword("");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userController.save(validUser));
        assertEquals("Every fields must be filled", exception.getMessage());
    }

    @Test
    void testSave_ThrowsException_WhenRoleIsEmpty() {
        validUser.setRol("");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userController.save(validUser));
        assertEquals("Every fields must be filled", exception.getMessage());
    }

}
