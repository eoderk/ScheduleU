package com.evanknight.scheduleu.util;

import com.evanknight.scheduleu.entities.SUObject;

import java.util.ArrayList;
import java.util.TreeMap;

public interface Validator<T extends Validator> {

    T insert();

    T update();

    T delete();

    boolean getValidation();
    TreeMap<String, String> invalidAttributes();
}
