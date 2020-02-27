package com.gmail.ezlotnikova.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gmail.ezlotnikova.repository.ItemShopRelationRepository;
import com.gmail.ezlotnikova.repository.model.ItemShopRelation;
import org.springframework.stereotype.Repository;

@Repository
public class ItemShopRelationRepositoryImpl implements ItemShopRelationRepository {

    @Override
    public ItemShopRelation add(Connection connection, ItemShopRelation itemShopRelation) throws SQLException {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO item_shop_relation (item_id, shop_id) VALUES (?,?)")
        ) {
            statement.setLong(1, itemShopRelation.getItemId());
            statement.setLong(2, itemShopRelation.getShopId());
            statement.execute();
            return itemShopRelation;
        }
    }

    @Override
    public void deleteById(Connection connection, Long id) throws SQLException {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "DELETE FROM item_shop_relation WHERE item_id = ?")
        ) {
            statement.setLong(1, id);
           statement.execute();
        }
    }

    @Override
    public List<Long> getAllItemId(Connection connection) throws SQLException {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT item_id  FROM item_shop_relation")
        ) {
            List<Long> itemIdList = new ArrayList<>();
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Long itemId = rs.getLong("item_id");
                    itemIdList.add(itemId);
                }
                return itemIdList;
            }
        }
    }

}