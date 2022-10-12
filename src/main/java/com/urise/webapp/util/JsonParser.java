package com.urise.webapp.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.urise.webapp.model.AbstractSection;

import java.io.Reader;
import java.io.Writer;
import java.time.LocalDate;

public interface JsonParser {
    Gson GSON = new GsonBuilder()
            .registerTypeAdapter(AbstractSection.class, new JsonSectionAdapter())
            .registerTypeAdapter(LocalDate.class, new JsonLocalDateAdapter())
            .create();

    default <T> T read(Reader reader, Class<T> clazz) {
        return GSON.fromJson(reader, clazz);
    }

    default <T> T read(String value, Class<T> clazz) {
        return GSON.fromJson(value, clazz);
    }

    default <T> void write(T object, Writer writer) {
        GSON.toJson(object, writer);
    }

    default <T> String write(T object) {
        return GSON.toJson(object);
    }
}
