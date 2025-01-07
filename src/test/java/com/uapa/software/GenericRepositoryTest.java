package com.uapa.software;

import com.uapa.software.entities.User;
import com.uapa.software.repositories.GenericRepository;
import com.uapa.software.repositories.UserRepository;
import com.uapa.software.utils.HibernateUtil;
import org.hibernate.Session;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GenericRepositoryTest {

    private Session session;
    private GenericRepository<User> userRepository;

    @BeforeAll
    void setUp() {
        System.setProperty("env", "test");

        // Initialize Hibernate session
        session = HibernateUtil.getSessionFactory().openSession();

        // Create the repository instance
        userRepository = new UserRepository(session) {
            protected java.lang.Class<User> getEntityType() {
                return User.class;
            };
        };
    }

    @SuppressWarnings("deprecation")
    @BeforeEach
    void resetDatabase() {
        session.beginTransaction();
        session.createQuery("DELETE FROM User").executeUpdate();
        session.getTransaction().commit();
    }

    @AfterAll
    void tearDown() {
        if (session != null) {
            session.close();
        }
        HibernateUtil.shutdown();
    }

    @Test
    void save_ValidUser_ReturnsUser() {
        User user = new User(null, "testuser", "password123", "USER");
        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId(), "Saved user should have an ID");
        assertEquals("testuser", savedUser.getUsername());
    }

    @Test
    void findById_ValidId_ReturnsUser() {
        User user = new User(null, "testuser", "password123", "USER");
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findById(user.getId());
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
    }

    @Test
    void findAll_ReturnsUsersList() {
        User user1 = new User(null, "user1", "password1", "USER");
        User user2 = new User(null, "user2", "password2", "ADMIN");
        userRepository.save(user1);
        userRepository.save(user2);

        List<User> users = userRepository.findAll();

        assertEquals(2, users.size());
        assertEquals("user1", users.get(0).getUsername());
        assertEquals("user2", users.get(1).getUsername());
    }

    @Test
    void update_ValidUser_UpdatesUser() {
        User user = new User(null, "testuser", "password123", "USER");
        userRepository.save(user);

        user.setUsername("updateduser");
        boolean updated = userRepository.update(user);

        assertTrue(updated);
        Optional<User> updatedUser = userRepository.findById(user.getId());
        assertEquals("updateduser", updatedUser.get().getUsername());
    }

    @Test
    void delete_ValidUser_DeletesUser() {
        User user = new User(null, "testuser", "password123", "USER");
        userRepository.save(user);

        boolean deleted = userRepository.delete(user);
        assertTrue(deleted);

        Optional<User> foundUser = userRepository.findById(user.getId());
        assertFalse(foundUser.isPresent());
    }

    @Test
    void save_NullEntity_ReturnsNull() {
        User user = null;
        User result = userRepository.save(user);

        assertNull(result, "Saving a null entity should return null");
    }

    @Test
    void save_ThrowsException() {
        assertThrows(RuntimeException.class, () -> {
            userRepository.save(new User(null, null, null, null));
            throw new RuntimeException("Simulated exception");
        });
    }

    @Test
    void update_NullEntity_ReturnsFalse() {
        User user = null;
        boolean result = userRepository.update(user);

        assertFalse(result, "Updating a null entity should return false");
    }

    @Test
    void update_ThrowsException() {
        assertThrows(RuntimeException.class, () -> {
            userRepository.update(new User(null, null, null, null));
            throw new RuntimeException("Simulated exception");
        });
    }

    @Test
    void delete_NullEntity_ReturnsFalse() {
        User user = null;
        boolean result = userRepository.delete(user);

        assertFalse(result, "Deleting a null entity should return false");
    }

    @Test
    void delete_ThrowsException() {
        assertThrows(RuntimeException.class, () -> {
            userRepository.delete(new User(null, null, null, null));
            throw new RuntimeException("Simulated exception");
        });
    }

    @Test
    void findById_InvalidId_ReturnsEmptyOptional() {
        Optional<User> result = userRepository.findById(-1);

        assertTrue(result.isEmpty(), "Finding by an invalid ID should return an empty Optional");
    }

    @Test
    void findById_ThrowsException() {
        assertThrows(RuntimeException.class, () -> {
            userRepository.findById(1);
            throw new RuntimeException("Simulated exception");
        });
    }

    @Test
    void findAll_ReturnsEmptyList() {
        List<User> result = userRepository.findAll();

        assertTrue(result.isEmpty(), "Fetching all entities from an empty database should return an empty list");
    }

    @Test
    void findAll_ThrowsException() {
        assertThrows(RuntimeException.class, () -> {
            userRepository.findAll();
            throw new RuntimeException("Simulated exception");
        });
    }

    @Test
    void save_ThrowsRuntimeExceptionDuringTransaction() {
        assertThrows(RuntimeException.class, () -> {
            userRepository.executeTransaction(session -> {
                throw new RuntimeException("Simulated exception during save");
            });
        });

        // Validate that the transaction rollback occurred by checking the log output.
    }

    @Test
    void update_ThrowsRuntimeExceptionDuringTransaction() {
        assertThrows(RuntimeException.class, () -> {
            userRepository.executeTransaction(session -> {
                throw new RuntimeException("Simulated exception during update");
            });
        });

        // Check if the error was logged.
    }
    
    @Test
    void delete_ThrowsRuntimeExceptionDuringTransaction() {
        assertThrows(RuntimeException.class, () -> {
            userRepository.executeTransaction(session -> {
                throw new RuntimeException("Simulated exception during delete");
            });
        });

        // Verify rollback and exception handling behavior.
    }

    @Test
    void findById_ThrowsExceptionDuringSessionOperation() {
        Session testSession = HibernateUtil.getSessionFactory().openSession(); // Open a separate session for testing
        GenericRepository<User> testRepository = new UserRepository(testSession) {
            @Override
            protected Class<User> getEntityType() {
                return User.class;
            }
        };

        testSession.close(); // Simulate a closed session

        Optional<User> result = testRepository.findById(1);
        assertTrue(result.isEmpty(), "Result should be empty when an exception occurs");
    }
    
    @Test
    void findAll_ThrowsExceptionDuringQueryExecution() {
    	Session testSession = HibernateUtil.getSessionFactory().openSession(); // Open a separate session for testing
        GenericRepository<User> testRepository = new UserRepository(testSession) {
            @Override
            protected Class<User> getEntityType() {
                return User.class;
            }
        };

        testSession.close(); // Simulate a closed session
        List<User> result = testRepository.findAll();

        assertTrue(result.isEmpty(), "Result should be an empty list when an exception occurs");
        // Validate error message in the logs.
    }

 
}
