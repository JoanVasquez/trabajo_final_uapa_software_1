package com.uapa.software;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uapa.software.entities.Bill;
import com.uapa.software.entities.BillDetail;
import com.uapa.software.entities.Customer;
import com.uapa.software.entities.Interaction;
import com.uapa.software.entities.Preference;
import com.uapa.software.entities.Product;
import com.uapa.software.entities.User;
import com.uapa.software.utils.HibernateUtil;

public class HibernateUtilTest {

    private HibernateUtil.ConfigurationFactory mockFactory;
    private Configuration mockConfiguration;

    @BeforeEach
    void setUp() {
        // Set the environment to "test"
        System.setProperty("env", "test");

        // Mock ConfigurationFactory and Configuration
        mockFactory = mock(HibernateUtil.ConfigurationFactory.class);
        mockConfiguration = mock(Configuration.class);

        // Mock SessionFactory
        SessionFactory mockSessionFactory = mock(SessionFactory.class);

        // Define behavior for mockFactory and mockConfiguration
        when(mockFactory.create()).thenReturn(mockConfiguration);
        when(mockConfiguration.setProperties(any(Properties.class))).thenReturn(mockConfiguration);
        when(mockConfiguration.addAnnotatedClass(any(Class.class))).thenReturn(mockConfiguration);

        // Mock buildSessionFactory to return the mocked SessionFactory
        when(mockConfiguration.buildSessionFactory()).thenReturn(mockSessionFactory);

        // Set the mocked factory in HibernateUtil
        HibernateUtil.setConfigurationFactory(mockFactory);
    }

    @AfterEach
    void tearDown() {
        HibernateUtil.resetSessionFactory();
    }

    @Test
    void testGetSessionFactory() {
        // Invoke the method
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        // Verify interactions with the mocked configuration
        assertNotNull(sessionFactory);
        verify(mockFactory).create();
        verify(mockConfiguration).setProperties(any(Properties.class));
        verify(mockConfiguration).addAnnotatedClass(Customer.class);
        verify(mockConfiguration).addAnnotatedClass(Interaction.class);
        verify(mockConfiguration).addAnnotatedClass(Preference.class);
        verify(mockConfiguration).addAnnotatedClass(Product.class);
        verify(mockConfiguration).addAnnotatedClass(Bill.class);
        verify(mockConfiguration).addAnnotatedClass(BillDetail.class);
        verify(mockConfiguration).addAnnotatedClass(User.class);
    }

    @Test
    void testSessionFactoryReuse() {
        // Get SessionFactory multiple times
        SessionFactory sessionFactory1 = HibernateUtil.getSessionFactory();
        SessionFactory sessionFactory2 = HibernateUtil.getSessionFactory();

        // Verify that the same instance is returned
        assertSame(sessionFactory1, sessionFactory2);
    }

    @Test
    void testShutdown() {
        // Get SessionFactory and then shut it down
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        HibernateUtil.shutdown();

        // Ensure the SessionFactory is closed
        assertNotNull(sessionFactory);
        assertFalse(sessionFactory.isOpen());
    }
    
    @Test
    void testGetSessionFactoryExceptionHandling() {
        // Mock buildSessionFactory to throw an exception
        when(mockConfiguration.buildSessionFactory())
                .thenThrow(new RuntimeException("Mock exception for testing"));

        // Verify that the RuntimeException is thrown with the correct message
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                HibernateUtil::getSessionFactory,
                "Expected getSessionFactory() to throw a RuntimeException"
        );

        assertTrue(
                exception.getMessage().contains("Error creating the SessionFactory"),
                "Exception message should indicate an error creating the SessionFactory"
        );
    }

}
