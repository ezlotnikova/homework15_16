package com.gmail.ezlotnikova.service.impl;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import com.gmail.ezlotnikova.repository.ConnectionRepository;
import com.gmail.ezlotnikova.repository.ItemShopRelationRepository;
import com.gmail.ezlotnikova.repository.model.ItemShopRelation;
import com.gmail.ezlotnikova.service.ItemShopRelationService;
import com.gmail.ezlotnikova.service.model.ItemShopRelationDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ItemShopRelationServiceImpl implements ItemShopRelationService {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final ConnectionRepository connectionRepository;
    private final ItemShopRelationRepository itemShopRelationRepository;

    public ItemShopRelationServiceImpl(
            ConnectionRepository connectionRepository,
            ItemShopRelationRepository itemShopRelationRepository) {
        this.connectionRepository = connectionRepository;
        this.itemShopRelationRepository = itemShopRelationRepository;
    }

    @Override
    public ItemShopRelationDTO add(ItemShopRelationDTO itemShopRelationDTO) throws SQLException {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                ItemShopRelation itemShopRelation = convertDTOToItemShopRelation(itemShopRelationDTO);
                itemShopRelationRepository.add(connection, itemShopRelation);
                connection.commit();
                return itemShopRelationDTO;
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }

    @Override
    public void deleteById(Long id) {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                itemShopRelationRepository.deleteById(connection, id);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public List<Long> getAllItemId() {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<Long> itemIdList = itemShopRelationRepository.getAllItemId(connection);
                connection.commit();
                return itemIdList;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    private ItemShopRelation convertDTOToItemShopRelation(ItemShopRelationDTO itemShopRelationDTO) {
        ItemShopRelation itemShopRelation = new ItemShopRelation();
        Long itemId = itemShopRelationDTO.getItemId();
        itemShopRelation.setItemId(itemId);
        Long shopId = itemShopRelationDTO.getShopId();
        itemShopRelation.setShopId(shopId);
        return itemShopRelation;
    }

}