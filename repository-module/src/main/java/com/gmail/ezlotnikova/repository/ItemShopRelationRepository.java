package com.gmail.ezlotnikova.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.gmail.ezlotnikova.repository.model.ItemShopRelation;

public interface ItemShopRelationRepository extends GeneralRepository<ItemShopRelation> {

    List<Long> getAllItemId(Connection connection) throws SQLException;

}