package com.gmail.ezlotnikova.service;

import java.sql.SQLException;

public interface GeneralService<T> {

    T add(T t) throws SQLException;

    void deleteById(Long id);

}