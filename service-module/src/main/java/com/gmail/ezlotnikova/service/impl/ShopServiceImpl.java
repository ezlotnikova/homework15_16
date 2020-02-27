package com.gmail.ezlotnikova.service.impl;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;

import com.gmail.ezlotnikova.repository.ConnectionRepository;
import com.gmail.ezlotnikova.repository.ShopRepository;
import com.gmail.ezlotnikova.repository.model.Shop;
import com.gmail.ezlotnikova.service.ShopService;
import com.gmail.ezlotnikova.service.model.ShopDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ShopServiceImpl implements ShopService {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final ConnectionRepository connectionRepository;
    private final ShopRepository shopRepository;

    public ShopServiceImpl(
            ConnectionRepository connectionRepository,
            ShopRepository shopRepository) {
        this.connectionRepository = connectionRepository;
        this.shopRepository = shopRepository;
    }

    @Override
    public ShopDTO add(ShopDTO shopDTO) {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Shop shop = convertShopDTOToShop(shopDTO);
                Shop addedShop = shopRepository.add(connection, shop);
                ShopDTO addedShopDTO = convertShopToShopDTO(addedShop);
                connection.commit();
                return addedShopDTO;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return shopDTO;
    }

    @Override
    public void deleteById(Long id) {
        throw new UnsupportedOperationException("Delete by Id operation is not supported for Shop object");
    }

    private ShopDTO convertShopToShopDTO(Shop shop) {
        ShopDTO shopDTO = new ShopDTO();
        Long id = shop.getId();
        shopDTO.setId(id);
        String name = shop.getName();
        shopDTO.setName(name);
        String location = shop.getLocation();
        shopDTO.setLocation(location);
        return shopDTO;
    }

    private Shop convertShopDTOToShop(ShopDTO shopDTO) {
        Shop shop = new Shop();
        String name = shopDTO.getName();
        shop.setName(name);
        String location = shopDTO.getLocation();
        shop.setLocation(location);
        return shop;
    }

}