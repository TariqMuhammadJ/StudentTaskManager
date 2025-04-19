package com.studytask.dao;

import java.util.List;
import java.util.Optional;
import com.studytask.exceptions.DAOException;

public interface BaseDAO<T> {
    List<T> findAll() throws DAOException;
    Optional<T> findById(int id) throws DAOException;
    void save(T entity) throws DAOException;
    void update(T entity) throws DAOException;
    void delete(int id) throws DAOException;
}