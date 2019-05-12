package com.tedu.web.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtil {
    private static ObjectMapper mapper;
    private static JsonInclude.Include DEFAULT_PROPERTY_INCLUSION = JsonInclude.Include.NON_DEFAULT;
    private static boolean IS_ENABLE_INDENT_OUTPUT = false;
    private static String CSV_DEFAULT_COLUMN_SEPARATOR = ",";
    static {
        try {
            initMapper();
            configPropertyInclusion();
            configIndentOutput();
            configCommon();
        } catch (Exception e) {
            log.error("jackson config error", e);
        }
    }

    private static void initMapper() {
        mapper = new ObjectMapper();
    }

    private static void configCommon() {
        config(mapper);
    }

    private static void configPropertyInclusion() {
        mapper.setSerializationInclusion(DEFAULT_PROPERTY_INCLUSION);
    }

    private static void configIndentOutput() {
        mapper.configure(SerializationFeature.INDENT_OUTPUT, IS_ENABLE_INDENT_OUTPUT);
    }

    private static void config(ObjectMapper objectMapper) {
        objectMapper.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        objectMapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        objectMapper.enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.enable(JsonParser.Feature.ALLOW_COMMENTS);
        objectMapper.disable(JsonGenerator.Feature.ESCAPE_NON_ASCII);
        objectMapper.enable(JsonGenerator.Feature.IGNORE_UNKNOWN);
        objectMapper.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        objectMapper.registerModule(new ParameterNamesModule());
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new GuavaModule());
    }
    public static void setSerializationInclusion(JsonInclude.Include inclusion) {
        DEFAULT_PROPERTY_INCLUSION = inclusion;
        configPropertyInclusion();
    }

    public static void setIndentOutput(boolean isEnable) {
        IS_ENABLE_INDENT_OUTPUT = isEnable;
        configIndentOutput();
    }

    public static <V> V from(URL url, Class<V> c) {
        try {
            return mapper.readValue(url, c);
        } catch (IOException e) {
            log.error("jackson from error, url: {}, type: {}", url.getPath(), c, e);
            return null;
        }
    }

    public static <V> V from(InputStream inputStream, Class<V> c) {
        try {
            return mapper.readValue(inputStream, c);
        } catch (IOException e) {
            log.error("jackson from error, type: {}", c, e);
            return null;
        }
    }

    public static <V> V from(File file, Class<V> c) {
        try {
            return mapper.readValue(file, c);
        } catch (IOException e) {
            log.error("jackson from error, file path: {}, type: {}", file.getPath(), c, e);
            return null;
        }
    }

    public static <V> V from(Object jsonObj, Class<V> c) {
        try {
            return mapper.readValue(jsonObj.toString(), c);
        } catch (IOException e) {
            log.error("jackson from error, json: {}, type: {}", jsonObj.toString(), c, e);
            return null;
        }
    }

    public static <V> V from(String json, Class<V> c) {
        try {
            return mapper.readValue(json, c);
        } catch (IOException e) {
            log.error("jackson from error, json: {}, type: {}", json, c, e);
            return null;
        }
    }

    public static <V> V from(URL url, TypeReference<V> type) {
        try {
            return mapper.readValue(url, type);
        } catch (IOException e) {
            log.error("jackson from error, url: {}, type: {}", url.getPath(), type, e);
            return null;
        }
    }

    public static <V> V from(InputStream inputStream, TypeReference<V> type) {
        try {
            return mapper.readValue(inputStream, type);
        } catch (IOException e) {
            log.error("jackson from error, type: {}", type, e);
            return null;
        }
    }

    public static <V> V from(File file, TypeReference<V> type) {
        try {
            return mapper.readValue(file, type);
        } catch (IOException e) {
            log.error("jackson from error, file path: {}, type: {}", file.getPath(), type, e);
            return null;
        }
    }

    public static <V> V from(Object jsonObj, TypeReference<V> type) {
        try {
            return mapper.readValue(jsonObj.toString(), type);
        } catch (IOException e) {
            log.error("jackson from error, json: {}, type: {}", jsonObj.toString(), type, e);
            return null;
        }
    }

    public static <V> V from(String json, TypeReference<V> type) {
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            log.error("jackson from error, json: {}, type: {}", json, type, e);
            return null;
        }
    }

    public static <V> String to(List<V> list) {
        try {
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            log.error("jackson to error, obj: {}", list, e);
            return null;
        }
    }

    public static <V> String to(V v) {
        try {
            return mapper.writeValueAsString(v);
        } catch (JsonProcessingException e) {
            log.error("jackson to error, obj: {}", v, e);
            return null;
        }
    }

    public static <V> void toFile(String path, List<V> list) {
        try (Writer writer = new FileWriter(new File(path), true)) {
            mapper.writer().writeValues(writer).writeAll(list);
            writer.flush();
        } catch (Exception e) {
            log.error("jackson to file error, path: {}, list: {}", path, list, e);
        }
    }

    public static <V> void toFile(String path, V v) {
        try (Writer writer = new FileWriter(new File(path), true)) {
            mapper.writer().writeValues(writer).write(v);
            writer.flush();
        } catch (Exception e) {
            log.error("jackson to file error, path: {}, obj: {}", path, v, e);
        }
    }

    public static String getString(String json, String key) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            JsonNode node = mapper.readTree(json);
            if (null != node) {
                return node.get(key).toString();
            } else {
                return null;
            }
        } catch (IOException e) {
            log.error("jackson get string error, json: {}, key: {}", json, key, e);
            return null;
        }
    }

    public static Integer getInt(String json, String key) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            JsonNode node = mapper.readTree(json);
            if (null != node) {
                return node.get(key).intValue();
            } else {
                return null;
            }
        } catch (IOException e) {
            log.error("jackson get int error, json: {}, key: {}", json, key, e);
            return null;
        }
    }

    public static Long getLong(String json, String key) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            JsonNode node = mapper.readTree(json);
            if (null != node) {
                return node.get(key).longValue();
            } else {
                return null;
            }
        } catch (IOException e) {
            log.error("jackson get long error, json: {}, key: {}", json, key, e);
            return null;
        }
    }

    public static Double getDouble(String json, String key) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            JsonNode node = mapper.readTree(json);
            if (null != node) {
                return node.get(key).doubleValue();
            } else {
                return null;
            }
        } catch (IOException e) {
            log.error("jackson get double error, json: {}, key: {}", json, key, e);
            return null;
        }
    }

    public static BigInteger getBigInteger(String json, String key) {
        if (StringUtils.isEmpty(json)) {
            return new BigInteger(String.valueOf(0.00));
        }
        try {
            JsonNode node = mapper.readTree(json);
            if (null != node) {
                return node.get(key).bigIntegerValue();
            } else {
                return null;
            }
        } catch (IOException e) {
            log.error("jackson get biginteger error, json: {}, key: {}", json, key, e);
            return null;
        }
    }

    public static BigDecimal getBigDecimal(String json, String key) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            JsonNode node = mapper.readTree(json);
            if (null != node) {
                return node.get(key).decimalValue();
            } else {
                return null;
            }
        } catch (IOException e) {
            log.error("jackson get bigdecimal error, json: {}, key: {}", json, key, e);
            return null;
        }
    }

    public static boolean getBoolean(String json, String key) {
        if (StringUtils.isEmpty(json)) {
            return false;
        }
        try {
            JsonNode node = mapper.readTree(json);
            if (null != node) {
                return node.get(key).booleanValue();
            } else {
                return false;
            }
        } catch (IOException e) {
            log.error("jackson get boolean error, json: {}, key: {}", json, key, e);
            return false;
        }
    }

    public static byte[] getByte(String json, String key) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            JsonNode node = mapper.readTree(json);
            if (null != node) {
                return node.get(key).binaryValue();
            } else {
                return null;
            }
        } catch (IOException e) {
            log.error("jackson get byte error, json: {}, key: {}", json, key, e);
            return null;
        }
    }

    public static <T> ArrayList<T> getList(String json, String key) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        String string = getString(json, key);
        return from(string, new TypeReference<ArrayList<T>>() {});
    }

    public static <T> String add(String json, String key, T value) {
        try {
            JsonNode node = mapper.readTree(json);
            add(node, key, value);
            return node.toString();
        } catch (IOException e) {
            log.error("jackson add error, json: {}, key: {}, value: {}", json, key, value, e);
            return json;
        }
    }

    private static <T> void add(JsonNode jsonNode, String key, T value) {
        if (value instanceof String) {
            ((ObjectNode) jsonNode).put(key, (String) value);
        } else if (value instanceof Short) {
            ((ObjectNode) jsonNode).put(key, (Short) value);
        } else if (value instanceof Integer) {
            ((ObjectNode) jsonNode).put(key, (Integer) value);
        } else if (value instanceof Long) {
            ((ObjectNode) jsonNode).put(key, (Long) value);
        } else if (value instanceof Float) {
            ((ObjectNode) jsonNode).put(key, (Float) value);
        } else if (value instanceof Double) {
            ((ObjectNode) jsonNode).put(key, (Double) value);
        } else if (value instanceof BigDecimal) {
            ((ObjectNode) jsonNode).put(key, (BigDecimal) value);
        } else if (value instanceof BigInteger) {
            ((ObjectNode) jsonNode).put(key, (BigInteger) value);
        } else if (value instanceof Boolean) {
            ((ObjectNode) jsonNode).put(key, (Boolean) value);
        } else if (value instanceof byte[]) {
            ((ObjectNode) jsonNode).put(key, (byte[]) value);
        } else {
            ((ObjectNode) jsonNode).put(key, to(value));
        }
    }

    public static String remove(String json, String key) {
        try {
            JsonNode node = mapper.readTree(json);
            ((ObjectNode) node).remove(key);
            return node.toString();
        } catch (IOException e) {
            log.error("jackson remove error, json: {}, key: {}", json, key, e);
            return json;
        }
    }

    public static <T> String update(String json, String key, T value) {
        try {
            JsonNode node = mapper.readTree(json);
            ((ObjectNode) node).remove(key);
            add(node, key, value);
            return node.toString();
        } catch (IOException e) {
            log.error("jackson update error, json: {}, key: {}, value: {}", json, key, value, e);
            return json;
        }
    }

    public static String format(String json) {
        try {
            JsonNode node = mapper.readTree(json);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (IOException e) {
            log.error("jackson format json error, json: {}", json, e);
            return json;
        }
    }

    public static boolean isJson(String json) {
        try {
            mapper.readTree(json);
            return true;
        } catch (Exception e) {
            log.error("jackson check json error, json: {}", json, e);
            return false;
        }
    }

    private static InputStream getResourceStream(String name) {
        return JsonUtil.class.getClassLoader().getResourceAsStream(name);
    }

    private static InputStreamReader getResourceReader(InputStream inputStream) {
        if (null == inputStream) {
            return null;
        }
        return new InputStreamReader(inputStream, StandardCharsets.UTF_8);
    }
}