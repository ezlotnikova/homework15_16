package com.gmail.ezlotnikova.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.gmail.ezlotnikova.repository.ShopRepository;
import com.gmail.ezlotnikova.repository.model.Shop;
import org.springframework.stereotype.Repository;

@Repository
public class ShopRepositoryImpl implements ShopRepository {

    @Override
    public Shop add(Connection connection, Shop shop) throws SQLException {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO shop(name, location) VALUES (?,?)",
                        Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, shop.getName());
            statement.setString(2, shop.getLocation());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Adding shop failed, no rows affected");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    shop.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Adding shop failed, no ID obtained");
                }
            }
            return shop;
        }
    }

    @Override
    public void deleteById(Connection connection, Long id) {
        throw new UnsupportedOperationException("Delete by Id operation is not supported for Shop object");
    }

}