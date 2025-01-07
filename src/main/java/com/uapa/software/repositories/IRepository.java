package com.uapa.software.repositories;

import java.util.List;
import java.util.Optional;

public interface IRepository<T> {

	/**
	 * Save the given entity.
	 *
	 * @param entity the entity to save
	 * @return the saved entity
	 */
	T save(T entity);

	/**
	 * Update the given entity.
	 *
	 * @param entity the entity to update
	 * @return true if the update was successful, false otherwise
	 */
	boolean update(T entity);

	/**
	 * Delete the given entity.
	 *
	 * @param entity the entity to delete
	 * @return true if the deletion was successful, false otherwise
	 */
	boolean delete(T entity);

	/**
	 * Retrieve an entity by its ID.
	 *
	 * @param id the ID of the entity
	 * @return an Optional containing the entity if found, or empty if not
	 */
	Optional<T> findById(int id);

	/**
	 * Retrieve all entities of the given type.
	 *
	 * @return a list of all entities
	 */
	List<T> findAll();
}
