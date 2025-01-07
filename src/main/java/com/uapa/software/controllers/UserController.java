package com.uapa.software.controllers;

import com.uapa.software.entities.User;
import com.uapa.software.repositories.UserRepository;
import com.uapa.software.services.CognitoService;
import com.uapa.software.services.UserService;
import com.uapa.software.utils.CognitoServiceFactory;
import com.uapa.software.utils.HibernateUtil;

public class UserController extends GenericController<User> {

    private static final CognitoService cognitoService = CognitoServiceFactory.createCognitoService();
    private static final UserRepository userRepository = new UserRepository(
            HibernateUtil.getSessionFactory().openSession());
    private static final UserService userService = new UserService(cognitoService, userRepository);

    public UserController() {
        super(userService);
    }

    public String login(User value) {
        if (value.getUsername().equals("") || value.getPassword().equals("")) {
            throw new IllegalArgumentException("Username and password must be filled");
        }
        return userService.login(value);
    }

    public String saveAndLogin(User value) {
        if (value.getUsername().equals("") || value.getPassword().equals("") || value.getRol().equals("")) {
            throw new IllegalArgumentException("Every fields must be filled");
        }

        return userService.saveAndLogin(value);
    }

    @Override
    public User save(User value) {
        if (value.getUsername().equals("") || value.getPassword().equals("") || value.getRol().equals("")) {
            throw new IllegalArgumentException("Every fields must be filled");
        }
        return super.save(value);
    }

    @Override
    public boolean update(User value) {
        if (value.getId().equals("") || value.getUsername().equals("") || value.getPassword().equals("")
                || value.getRol().equals("")) {
            throw new IllegalArgumentException("Every fields must be filled");
        }
        return super.update(value);
    }
}
