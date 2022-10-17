package com.urise.webapp.storage;

import com.urise.webapp.exception.NotFoundStorageException;
import com.urise.webapp.model.Resume;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.urise.webapp.storage.TestResumesGenerator.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class AbstractStorageTest {
    // folder test_storage will be created in root folder of the project
    protected static final File STORAGE_DIR = new File("test_storage");
    protected static final Path STORAGE_PATH = Paths.get("test_storage");

    // do not change uuids and names order, they used to check getAllSorted
    private static final Resume RESUME_1 = createTestResumeWithAllFields("uuid1", "Full name 2");
    private static final Resume RESUME_2 = createTestResumeWithAllFields("uuid2", "Full name 3");
    private static final Resume RESUME_3 = createTestResumeOnlyContacts("uuid3", "Full name 1");
    private static final Resume RESUME_4 = createTestResumeWithAllFieldsMinimal("uuid4", "Full name 2");
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
        storage.save(RESUME_4);
    }


    @Test
    @DisplayName("Clear storage")
    void itShouldClear() {
        // Given
        // When
        storage.clear();

        // Then
        assertEquals(0, storage.size());
    }

    @Test
    @DisplayName("Get resume from storage")
    void itShouldGet() {
        // Given
        // When
        Resume resumeFromStorage = storage.get(RESUME_1.getUuid());

        // Then
        assertThat(resumeFromStorage).isEqualTo(RESUME_1);
    }

    @Test
    @DisplayName("Throw when trying to get Resume with wrong uuid")
    void itShouldThrowWhenTryToGetWrongUuid() {
        // Given
        String uuid = "SomeStrangeUuid";

        // When
        // Then
        assertThatThrownBy(() -> storage.get(uuid))
                .isInstanceOf(NotFoundStorageException.class)
                .hasMessageContaining("Resume " + uuid + " not found!");
    }

    @Test
    @DisplayName("Delete resume")
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
    @DisplayName("Throw when trying to delete Resume with wrong uuid")
    void itShouldThrowWhenTryToDeleteWrongUuid() {
        // Given
        String uuid = "SomeStrangeUuid";

        // When
        // Then
        assertThatThrownBy(() -> storage.delete(uuid))
                .isInstanceOf(NotFoundStorageException.class)
                .hasMessageContaining("Resume " + uuid + " not found!");
    }

    @Test
    @DisplayName("Get all Resumes sorted")
    void itShouldGetAllSorted() {
        // Given
        ArrayList<Resume> expectedResumes = new ArrayList<>();
        expectedResumes.add(RESUME_3);
        expectedResumes.add(RESUME_1);
        expectedResumes.add(RESUME_4);
        expectedResumes.add(RESUME_2);

        // When
        List<Resume> resumesFromStorage = storage.getAllSorted();

        // Then
        assertThat(resumesFromStorage).isEqualTo(expectedResumes);
    }

    @Test
    @DisplayName("Save resume")
    void itShouldSave() {
        // Given
        String uuid = UUID.randomUUID().toString();
        String newFullName = "Some new Full name";
        Resume savedResume = new Resume(uuid, newFullName);

        // When
        storage.save(savedResume);

        // Then
        assertThat(storage.get(uuid)).isEqualTo(savedResume);
        assertEquals(5, storage.size());
    }

    @Test
    @DisplayName("Update resume")
    void itShouldUpdate() {
        // Given
        String uuid = RESUME_1.getUuid();
        String newFullName = "Some new Full name";
        Resume updatedResume = new Resume(uuid, newFullName);

        // When
        storage.update(updatedResume);

        // Then
        assertThat(storage.get(uuid)).isEqualTo(updatedResume);
        assertEquals(4, storage.size());
    }

    @Test
    @DisplayName("Check storage size")
    void itShouldGiveRightSize() {
        assertEquals(4, storage.size());
    }
}
