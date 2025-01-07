package com.uapa.software.repositories;

import org.hibernate.Session;

import com.uapa.software.entities.User;

public class UserRepository extends GenericRepository<User> {
    public UserRepository(Session session) {
        super(session);
    }

    @Override
    protected Class<User> getEntityType() {
        return User.class;
    }
}
