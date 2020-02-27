package com.gmail.ezlotnikova.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.gmail.ezlotnikova.repository.ItemRepository;
import com.gmail.ezlotnikova.repository.model.Item;
import org.springframework.stereotype.Repository;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    @Override
    public Item add(Connection connection, Item item) throws SQLException {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO item(name, description) VALUES (?,?)",
                        Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, item.getName());
            statement.setString(2, item.getDescription());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Adding item failed, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    item.setId(generatedKeys.getLong(1));
                    addItemDetails(connection, item);
                } else {
                    throw new SQLException("Adding item failed, no ID obtained.");
                }
            }
            return item;
        }
    }

    @Override
    public void deleteById(Connection connection, Long id) throws SQLException {
        deleteItemDetailsById(connection, id);
        deleteItemById(connection, id);
    }

    private void addItemDetails(Connection connection, Item item) throws SQLException {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO item_details(item_id, price) VALUES (?,?)")
        ) {
            statement.setLong(1, item.getId());
            statement.setInt(2, item.getPrice());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Adding details failed, no rows affected");
            }
        }
    }

    private void deleteItemDetailsById(Connection connection, Long id) throws SQLException {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "DELETE FROM item_details WHERE item_id = ?")
        ) {
            statement.setLong(1, (Long) id);
            statement.execute();
        }
    }

    private void deleteItemById(Connection connection, Long id) throws SQLException {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "DELETE FROM item WHERE id = ?")
        ) {
            statement.setLong(1, (Long) id);
            statement.execute();
        }
    }

}