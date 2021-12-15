package com.example.christiansoeappproject.repository;

import java.util.List;

interface ICrudRepository<T> {

    void create(T t);

    void readAll();

    void update(T t);

    void delete(String id);
}
