package com.gmail.ezlotnikova.springboot.helper.generator.impl;

import java.util.Random;

import com.gmail.ezlotnikova.service.model.ShopDTO;
import com.gmail.ezlotnikova.springboot.helper.generator.ShopDTOGenerator;
import org.springframework.stereotype.Component;

@Component
public class ShopDTOGeneratorImpl implements ShopDTOGenerator {

    @Override
    public ShopDTO generate(int number) {
        Random random = new Random();
        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setName("Shop" + number);
        shopDTO.setLocation("Location" + number);
        return shopDTO;
    }

}