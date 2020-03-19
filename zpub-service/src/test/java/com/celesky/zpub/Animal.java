package com.celesky.zpub;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * @desc:
 * @author: panqiong
 * @date: 2020-01-04
 */
public class Animal<T> {

    public T getAnimal(String json){

        T animal = JSON.parseObject(json,new TypeReference<T>(){});
        return animal;
    }



}
