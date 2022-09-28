package com.urise.webapp.storage;

import com.urise.webapp.exception.NotFoundStorageException;
import com.urise.webapp.model.Resume;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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
        Resume resumeFromStorage = storage.get(RESUME_1.getUuid());

        // Then
        assertThat(resumeFromStorage).isEqualTo(RESUME_1);
    }

    @Test
    void itShouldDelete() {
        // Given
        String uuid = RESUME_1.getUuid();

        // When
        storage.delete(uuid);

        // Then
        assertThatThrownBy(() -> storage.get(uuid))
                .isInstanceOf(NotFoundStorageException.class)
                .hasMessageContaining("Resume " + uuid + " not found!");
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