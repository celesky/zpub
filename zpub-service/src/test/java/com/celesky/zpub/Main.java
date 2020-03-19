package com.celesky.zpub;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @desc:
 * @author: panqiong
 * @date: 2020-01-04
 */
public class Main {
    public static void main(String[] args) {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);

        Dog dog = new Dog("laifu","wangwwangwang");
        //String json = JsonUtil.objToStr(dog);
        String json = JSON.toJSONString(dog, SerializerFeature.WriteClassName);
        System.out.println("json = " + json);
        Animal<Dog> dogAnimal = new Animal<>();
        Dog dog2 = dogAnimal.getAnimal(json);

        System.out.println("dog2 = " + dog2);

    }
}
