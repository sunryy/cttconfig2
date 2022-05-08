package com.ctg.mes.config.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtil {
    static final Type mapType = new TypeToken<Map<String, Object>>() {
    }.getType();

    static final Type strMapType = new TypeToken<Map<String, String>>() {
    }.getType();
    /**
     * Gson无法保证线程安全，所以需要使用ThreadLocal来做
     * https://sites.google.com/site/gson/gson-user-guide
     * Sharing State Across Custom Serializers and Deserializers
     * Sometimes you need to share state across custom serializers/deserializers (see this discussion). You can use the following three strategies to accomplish this:
     * Store shared state in static fields
     * Declare the serializer/deserializer as inner classes of a parent type, and use the instance fields of parent type to store shared state
     * Use Java ThreadLocal
     * 1 and 2 are not thread-safe options, but 3 is.
     */
    private static final ThreadLocal<Gson> gson =
            ThreadLocal.withInitial(() -> new GsonBuilder().disableHtmlEscaping().registerTypeAdapter(mapType, new GsonTypeAdapter()).create());

    //使用另一个对象，防止影响到了写场合
    private static final ThreadLocal<Gson> GSON_WRITE =
            ThreadLocal.withInitial(() -> new GsonBuilder().disableHtmlEscaping().create());

    //用于序列化的适合，基于版本号，过滤一些字段
    private static final ThreadLocal<Gson> GSON_WRITE_EXCLUDE =
            ThreadLocal.withInitial(() -> new GsonBuilder().disableHtmlEscaping().setVersion(1.0).create());

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.get().fromJson(json, classOfT);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return gson.get().fromJson(json, typeOfT);
    }


    public static Map<String, Object> fromJson2Map(String json) {
        return gson.get().fromJson(json, mapType);
    }

    public static Map<String, String> fromJson2StrMap(String json) {
        return gson.get().fromJson(json, strMapType);
    }

    public static <T> T fromJsonWithType(String json, Type typeOfT) {
        return gson.get().fromJson(json, typeOfT);
    }

    public static String toJson(Object object) {
        return GSON_WRITE.get().toJson(object);
    }

    /**
     * 基于{@link com.google.gson.annotations.Since}标签和版本限制一些字段序列化
     */
    public static String toJsonExcludeField(Object object) {
        return GSON_WRITE_EXCLUDE.get().toJson(object);
    }

    public static String toJsonPretty(Object object) {
        return new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create().toJson(object);
    }

    public static String readJsonFile(String filePath) {
        try {
            return ResourceUtils.readResourceUnderClassPath(filePath).trim();
        } catch (Exception e) {
            throw new RuntimeException(filePath + " does not exist or is empty ", e);
        }
    }


    private static final Type type = new TypeToken<Map<String, Map<String, Object>>>() {
    }.getType();

    static Map<String, Map<String, Object>> fromJson2ClassMap(String json) {
        return gson.get().fromJson(json, type);
    }

    /**
     * 用于解
     */
    private static class GsonTypeAdapter extends TypeAdapter<Object> {
        @Override
        public Object read(JsonReader in) throws IOException {
            // 反序列化
            JsonToken token = in.peek();
            switch (token) {
                case BEGIN_ARRAY:
                    List<Object> list = new ArrayList<>();
                    in.beginArray();
                    while (in.hasNext()) {
                        list.add(read(in));
                    }
                    in.endArray();
                    return list;
                case BEGIN_OBJECT:
                    Map<String, Object> map = new HashMap<>();
                    in.beginObject();
                    while (in.hasNext()) {
                        map.put(in.nextName(), read(in));
                    }
                    in.endObject();
                    return map;
                case STRING:
                    return in.nextString();
                case NUMBER:
                    //改写数字的处理逻辑，将数字值分为整型与浮点型。
                    String s = in.nextString();
                    if (s.indexOf('.') >= 0) {
                        return Double.parseDouble(s);
                    } else {
                        return Long.parseLong(s);
                    }
                case BOOLEAN:
                    return in.nextBoolean();
                case NULL:
                    in.nextNull();
                    return null;
                default:
                    throw new IllegalStateException();
            }
        }

        @Override
        public void write(JsonWriter out, Object value) throws IOException {
            // 序列化不处理
        }
    }
}
