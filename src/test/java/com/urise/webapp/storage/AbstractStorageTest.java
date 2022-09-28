package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class AbstractStorageTest {
    private static final Resume RESUME_1 = new Resume(UUID.randomUUID().toString(), "");
    private static final Resume RESUME_2 = new Resume(UUID.randomUUID().toString(), "");
    private static final Resume RESUME_3 = new Resume(UUID.randomUUID().toString(), "");
    protected final Storage storage;

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    void setUp() {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }


    @Test
    void itShouldClear() {
        // Given
        // When
        storage.clear();

        // Then
        assertEquals(0, storage.size());
    }

    @Test
    void itShouldGet() {
        // Given
        // When
        // Then
    }

    @Test
    void itShouldDelete() {
        // Given
        // When
        // Then
    }

    @Test
    void itShouldGetAllSorted() {
        // Given
        // When
        // Then
    }

    @Test
    void itShouldSave() {
        // Given
        // When
        // Then
    }

    @Test
    void itShouldUpdate() {
        // Given
        // When
        // Then
    }

    @Test
    void itShouldGiveRightSize() {
        // Given
        // When
        // Then
        assertEquals(3, storage.size());
    }
}