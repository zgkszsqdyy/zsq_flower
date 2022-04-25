package com.hr.dao;

import com.hr.entity.EASYBUY_ORDER;
import org.junit.Test;

import java.util.ArrayList;

public class MingxiDaoTest {
    @Test
    public void se(){
//        EASYBUY_ORDER order = EASYBUY_ORDERDao.selectById(1);
//        System.out.println(order);
        ArrayList<EASYBUY_ORDER> easybuy_orders = EASYBUY_DdanDao.selectById(1);
        System.out.println(easybuy_orders);
    }
}