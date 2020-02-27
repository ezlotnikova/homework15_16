package com.gmail.ezlotnikova.springboot.helper.generator.impl;

import java.util.Random;

import com.gmail.ezlotnikova.service.model.ItemDTO;
import com.gmail.ezlotnikova.springboot.helper.generator.ItemDTOGenerator;
import org.springframework.stereotype.Component;

@Component
public class ItemDTOGeneratorImpl implements ItemDTOGenerator {

    @Override
    public ItemDTO generate(int number) {
        Random random = new Random();
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setName("Item" + number);
        itemDTO.setDescription("Description" + number);
        itemDTO.setPrice(Math.abs(random.nextInt()));
        return itemDTO;
    }

}