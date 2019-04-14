package com.mixram.telegram.bot;

import com.mixram.telegram.bot.services.domain.Data3DPlastic;
import com.mixram.telegram.bot.services.domain.enums.Shop3D;
import com.mixram.telegram.bot.utils.htmlparser.ParseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author mixram on 2019-04-14.
 * @since ...
 */
@Component
public class TestCacheComponent {

    private final Random random;
    private final TestCacheService service;

    @Autowired
    public TestCacheComponent(TestCacheService service) {
        this.service = service;
        this.random = new Random();
    }


    @Scheduled(cron = "0/5 * * ? * *")
    public void storePlasticToRedisManually() {
        List<ParseData> list = new ArrayList<>(1);
        list.add(ParseData.builder()
                          .pageTitle(String.valueOf(random.nextInt()))
                          .productName("222")
                          .productOldPrice(new BigDecimal("20.45"))
                          .productSalePrice(new BigDecimal("100.24"))
                          .build());
        Data3DPlastic plastic = Data3DPlastic.builder()
                                             .data(list)
                                             .build();

        //        service.storePlasticToRedisManually(plastic, Shop3D.SHOP_3DPLAST.toString());
        //        service.storePlasticToRedisManually2(plastic, Shop3D.SHOP_3DPLAST);
        service.storePlasticToRedisManuallyEvict3(Shop3D.SHOP_3DPLAST.toString());
        service.storePlasticToRedisManuallyPut3(Shop3D.SHOP_3DPLAST.toString());
        //        service.storePlasticToRedisManually4(Shop3D.SHOP_3DPLAST);
    }

    @Scheduled(cron = "0/2 * * ? * *")
    public void getPlasticFromRedisManually() {
        //        Data3DPlastic plastic = service.getPlasticFromRedisManually(Shop3D.SHOP_3DPLAST.toString());
        //        Data3DPlastic plastic = service.getPlasticFromRedisManually2(Shop3D.SHOP_3DPLAST);
        Data3DPlastic plastic = service.getPlasticFromRedisManually3(Shop3D.SHOP_3DPLAST.toString());
        //        Data3DPlastic plastic = service.getPlasticFromRedisManually4(Shop3D.SHOP_3DPLAST);

        System.out.println("PLASTIC: " + plastic);
    }
}
