package com.gmail.ezlotnikova.service.impl;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;

import com.gmail.ezlotnikova.repository.ConnectionRepository;
import com.gmail.ezlotnikova.repository.ItemRepository;
import com.gmail.ezlotnikova.repository.model.Item;
import com.gmail.ezlotnikova.service.ItemService;
import com.gmail.ezlotnikova.service.model.ItemDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final ConnectionRepository connectionRepository;
    private final ItemRepository itemRepository;

    public ItemServiceImpl(
            ConnectionRepository connectionRepository,
            ItemRepository itemRepository) {
        this.connectionRepository = connectionRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemDTO add(ItemDTO itemDTO) {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Item item = convertItemDTOToItem(itemDTO);
                Item addedItem = itemRepository.add(connection, item);
                ItemDTO addedItemDTO = convertItemToItemDTO(addedItem);
                connection.commit();
                return addedItemDTO;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return itemDTO;
    }

    @Override
    public void deleteById(Long id) {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                itemRepository.deleteById(connection, id);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private ItemDTO convertItemToItemDTO(Item item) {
        ItemDTO itemDTO = new ItemDTO();
        Long id = item.getId();
        itemDTO.setId(id);
        String name = item.getName();
        itemDTO.setName(name);
        String description = item.getDescription();
        itemDTO.setDescription(description);
        int price = item.getPrice();
        itemDTO.setPrice(price);
        return itemDTO;
    }

    private Item convertItemDTOToItem(ItemDTO itemDTO) {
        Item item = new Item();
        String name = itemDTO.getName();
        item.setName(name);
        String description = itemDTO.getDescription();
        item.setDescription(description);
        int price = itemDTO.getPrice();
        item.setPrice(price);
        return item;
    }

}