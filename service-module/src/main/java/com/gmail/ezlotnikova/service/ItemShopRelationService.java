package com.gmail.ezlotnikova.service;

import java.util.List;

import com.gmail.ezlotnikova.service.model.ItemShopRelationDTO;

public interface ItemShopRelationService extends GeneralService<ItemShopRelationDTO> {

    List<Long> getAllItemId();

}