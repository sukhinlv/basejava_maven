package com.urise.webapp.util;

import com.urise.webapp.model.Resume;
import com.urise.webapp.model.TextSection;
import com.urise.webapp.storage.ResumeTestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonParserTest {

    private final JsonParser jsonParser = new JsonParserObject();

    @BeforeEach
    void setUp() {
    }

    @Test
    void testRWResume() {
        var resume = ResumeTestData.createTestResume("UUID_1", "NAME_1");
        String jsonString = jsonParser.write(resume);
        Resume resumeFromJsonString = jsonParser.read(jsonString, Resume.class);
        Assertions.assertEquals(resume, resumeFromJsonString);
    }

    @Test
    void testRWSections() {
        var section = new TextSection("section data");
        String jsonString = jsonParser.write(section);
        TextSection sectionFromJsonString = jsonParser.read(jsonString, TextSection.class);
        Assertions.assertEquals(section, sectionFromJsonString);
    }
}