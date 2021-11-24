package com.example.christiansoeappproject.repository;

import java.util.List;

public interface ICrudRepository<T> {

    void create(T t);

    T readById(String id);

    List<T> readAll();

    void update(T t);

    void delete(String id);
}
