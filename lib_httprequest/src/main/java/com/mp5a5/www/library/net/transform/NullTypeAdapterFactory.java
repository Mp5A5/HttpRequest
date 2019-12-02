package com.mp5a5.www.library.net.transform;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * @author ：mp5a5 on 2018/12/28 14：37
 * @describe
 * @email：wwb199055@126.com
 */
public class NullTypeAdapterFactory<T> implements TypeAdapterFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<T> rawType= (Class<T>) type.getRawType();
        if (rawType!=String.class) {
            return null;
        }
        return (TypeAdapter<T>) new NullAdapter();
    }
}
