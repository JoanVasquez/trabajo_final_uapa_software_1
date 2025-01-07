package com.uapa.software.services;

import java.util.List;
import java.util.Optional;

import com.uapa.software.repositories.GenericRepository;

public abstract class GenericService<Entity> implements ICRUD<Entity> {

	protected abstract GenericRepository<Entity> getRepository();

	@Override
	public Entity save(Entity entity) {
		return getRepository().save(entity);
	}

	@Override
	public boolean update(Entity entity) {
		return getRepository().update(entity);
	}

	@Override
	public boolean delete(Entity entity) {
		return getRepository().delete(entity);
	}

	@Override
	public Entity getById(String className, int id) {
		Optional<Entity> entity = getRepository().findById(id);
		return entity.orElse(null);
	}

	@Override
	public List<Entity> list(String className) {
		return getRepository().findAll();
	}
}
