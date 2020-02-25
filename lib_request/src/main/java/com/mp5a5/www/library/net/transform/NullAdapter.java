package com.mp5a5.www.library.net.transform;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * @author ：mp5a5 on 2018/12/28 14：40
 * @describe 对返回null的处理
 * @email：wwb199055@126.com
 */
public class NullAdapter extends TypeAdapter<String> {

    @Override
    public void write(JsonWriter out, String value) throws IOException {
        if (null == value) {
            out.nullValue();
            return;
        }
        out.value(value);
    }

    @Override
    public String read(JsonReader in) throws IOException {
        if (JsonToken.NULL == in.peek()) {
            in.nextNull();
            return "";
        }
        return in.nextString();
    }
}
