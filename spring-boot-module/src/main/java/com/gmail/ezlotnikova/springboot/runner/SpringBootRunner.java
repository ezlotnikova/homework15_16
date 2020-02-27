package com.gmail.ezlotnikova.springboot.runner;

import java.lang.invoke.MethodHandles;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.gmail.ezlotnikova.service.ItemService;
import com.gmail.ezlotnikova.service.ItemShopRelationService;
import com.gmail.ezlotnikova.service.ShopService;
import com.gmail.ezlotnikova.service.model.ItemDTO;
import com.gmail.ezlotnikova.service.model.ItemShopRelationDTO;
import com.gmail.ezlotnikova.service.model.ShopDTO;
import com.gmail.ezlotnikova.springboot.helper.generator.ItemDTOGenerator;
import com.gmail.ezlotnikova.springboot.helper.generator.ShopDTOGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import static com.gmail.ezlotnikova.springboot.constant.Constant.ITEM_AMOUNT;
import static com.gmail.ezlotnikova.springboot.constant.Constant.ITEM_SHOP_RELATION_AMOUNT;
import static com.gmail.ezlotnikova.springboot.constant.Constant.SHOP_AMOUNT;

@Component
public class SpringBootRunner implements ApplicationRunner {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final ItemDTOGenerator itemDTOGenerator;
    private final ShopDTOGenerator shopDTOGenerator;
    private final ItemService itemService;
    private final ShopService shopService;
    private final ItemShopRelationService itemShopRelationService;

    public SpringBootRunner(
            ItemDTOGenerator itemDTOGenerator,
            ShopDTOGenerator shopDTOGenerator,
            ItemService itemService,
            ShopService shopService,
            ItemShopRelationService itemShopRelationService) {
        this.itemDTOGenerator = itemDTOGenerator;
        this.shopDTOGenerator = shopDTOGenerator;
        this.itemService = itemService;
        this.shopService = shopService;
        this.itemShopRelationService = itemShopRelationService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Long> itemIdList = populateItemTable();
        List<Long> shopIdList = populateShopTable();
        populateItemShopRelationTable(itemIdList, shopIdList);
        deleteItemsRelatedToShops();
    }

    private List<Long> populateItemTable() {
        List<Long> itemIdList = new ArrayList<>();
        for (int i = 1; i <= ITEM_AMOUNT; i++) {
            ItemDTO itemDTO = itemDTOGenerator.generate(i);
            try {
                ItemDTO addedItemDTO = itemService.add(itemDTO);
                Long itemId = addedItemDTO.getId();
                itemIdList.add(itemId);
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return itemIdList;
    }

    private List<Long> populateShopTable() {
        List<Long> shopIdList = new ArrayList<>();
        for (int i = 1; i <= SHOP_AMOUNT; i++) {
            ShopDTO shopDTO = shopDTOGenerator.generate(i);
            try {
                ShopDTO addedShopDTO = shopService.add(shopDTO);
                Long shopId = addedShopDTO.getId();
                shopIdList.add(shopId);
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return shopIdList;
    }

    private void populateItemShopRelationTable(List<Long> itemIdList, List<Long> shopIdList) {
        Random random = new Random();
        for (int i = 0; i < ITEM_SHOP_RELATION_AMOUNT; ) {
            ItemShopRelationDTO itemShopRelationDTO = new ItemShopRelationDTO();
            Long itemId = itemIdList.get(random.nextInt(itemIdList.size()));
            itemShopRelationDTO.setItemId(itemId);
            Long shopId = shopIdList.get(random.nextInt(shopIdList.size()));
            itemShopRelationDTO.setShopId(shopId);
            try {
                itemShopRelationService.add(itemShopRelationDTO);
                i++;
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private void deleteItemsRelatedToShops() {
        List<Long> itemIdList = itemShopRelationService.getAllItemId();
        for (Long itemId : itemIdList) {
            itemShopRelationService.deleteById(itemId);
            itemService.deleteById(itemId);
        }
    }

}