package com.uapa.software.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uapa.software.entities.User;
import com.uapa.software.repositories.GenericRepository;
import com.uapa.software.repositories.UserRepository;

public class UserService extends GenericService<User> {

    private final UserRepository userRepository;
    private final CognitoService cognitoService;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(CognitoService cognitoService, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.cognitoService = cognitoService;
    }

    @Override
	protected GenericRepository<User> getRepository() {
        return userRepository;
    }

    public String login(User entity) {
        String token = cognitoService.authenticate(entity.getUsername(), entity.getPassword());
        if (token != null) {
            logger.info("User logged in: {}", entity.getUsername());
            return token;
        }
        return null;
    }

    public String saveAndLogin(User entity) {
        cognitoService.registerUser(entity.getUsername(), entity.getPassword());
        String token = cognitoService.authenticate(entity.getUsername(), entity.getPassword());

        if (token != null) {
            logger.info("User logged in: {}", entity.getUsername());
            userRepository.save(entity);
            logger.info("User saved to database: {}", entity.getUsername());
            return token;
        }
        return null;
    }
}
