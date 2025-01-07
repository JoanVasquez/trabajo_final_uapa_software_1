package com.uapa.software.utils;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;

import com.uapa.software.entities.Bill;
import com.uapa.software.entities.BillDetail;
import com.uapa.software.entities.Customer;
import com.uapa.software.entities.Interaction;
import com.uapa.software.entities.Preference;
import com.uapa.software.entities.Product;
import com.uapa.software.entities.User;

public class HibernateUtil {

	private static SessionFactory sessionFactory;

	// Factory for creating Configuration instances
	private static ConfigurationFactory configurationFactory = Configuration::new;

	private HibernateUtil() {
		// Prevent instantiation
	}

	public static void setConfigurationFactory(ConfigurationFactory factory) {
		configurationFactory = factory;
	}

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			synchronized (HibernateUtil.class) {
				if (sessionFactory == null) {
					try {
						Properties settings = new Properties();

						settings.put(AvailableSettings.DRIVER, EnvConfig.get("DB_DRIVER"));
						settings.put(AvailableSettings.URL, EnvConfig.get("DB_URL"));
						settings.put(AvailableSettings.USER, EnvConfig.get("DB_USER"));
						settings.put(AvailableSettings.PASS, EnvConfig.get("DB_PASSWORD"));
						settings.put(AvailableSettings.DIALECT, EnvConfig.get("DB_DIALECT"));
						settings.put(AvailableSettings.SHOW_SQL, "true");
						settings.put(AvailableSettings.HBM2DDL_AUTO, "create-drop");

						Configuration configuration = configurationFactory.create();
						configuration.setProperties(settings);

						// Add annotated entity classes
						configuration.addAnnotatedClass(Customer.class);
						configuration.addAnnotatedClass(Interaction.class);
						configuration.addAnnotatedClass(Preference.class);
						configuration.addAnnotatedClass(Product.class);
						configuration.addAnnotatedClass(Bill.class);
						configuration.addAnnotatedClass(BillDetail.class);
						configuration.addAnnotatedClass(User.class);

						sessionFactory = configuration.buildSessionFactory();
					} catch (Exception e) {
						e.printStackTrace();
						throw new RuntimeException("Error creating the SessionFactory: " + e.getMessage(), e);
					}
				}
			}
		}
		return sessionFactory;
	}

	public static void shutdown() {
		if (sessionFactory != null) {
			sessionFactory.close();
		}
	}

	public static void resetSessionFactory() {
		sessionFactory = null;
	}

	@FunctionalInterface
	public interface ConfigurationFactory {
		Configuration create();
	}
}
