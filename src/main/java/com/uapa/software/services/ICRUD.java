package com.uapa.software.services;

import java.util.List;

public interface ICRUD<T> {
	public T save(T entity);

	public boolean update(T entity);

	public boolean delete(T entity);

	public T getById(String className, int id);

	public List<T> list(String className);
}
