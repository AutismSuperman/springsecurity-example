package com.fulinlin.util;

import org.springframework.ui.ModelMap;

/**
 * @program: spring-seruirty
 * @author: Fulin
 * @create: 2019-05-27 11:01
 **/
public class GenerateModelMap {

    public static ModelMap generateMap(Integer value, String message) {
        ModelMap modelMap = new ModelMap();
        modelMap.put("code", value);
        modelMap.put("message", message);
        return modelMap;
    }
}
