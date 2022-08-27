package com.maz;

import com.alibaba.druid.pool.ha.selector.StickyDataSourceHolder;
import com.maz.reggie.service.SetmealService;
import com.maz.reggie.service.impl.SetmealServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ReggieTakeOutApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void test1(){
        String fileName = "evevev.jpg";
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        System.out.println(suffix);
    }
/*    @Test
    public void test2(){
        List<Long> ids = new ArrayList<>();
        ids.add(1562672518626541570L);
        System.out.println(ids);

        for (Long id : ids){
            SetmealService setmealService = new SetmealServiceImpl();
            setmealService.changeSealStatus(0,ids);
        }

    }*/

}
