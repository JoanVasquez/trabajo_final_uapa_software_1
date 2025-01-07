package com.uapa.software.controllers;

import java.util.List;

import com.uapa.software.services.GenericService;
import com.uapa.software.services.ICRUD;

public abstract class GenericController<T> {

    private ICRUD genericService;

    public GenericController(GenericService<T> genericService) {
        this.genericService = genericService;
    }

    @SuppressWarnings("unchecked")
    public T save(T value) {
        return (T) genericService.save(value);
    }

    public boolean update(T value) {
        return genericService.update(value);
    }

    public boolean delete(T value) {
        return genericService.delete(value);
    }

    public T getById(String className, int id) {
        return (T) genericService.getById(className, id);
    }

    public List<T> list(String className) {
        return genericService.list(className);
    }
}
