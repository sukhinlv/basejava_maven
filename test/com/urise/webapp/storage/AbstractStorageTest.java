package com.urise.webapp.storage;

import com.urise.webapp.Config;
import com.urise.webapp.exception.FoundDuplicateStorageException;
import com.urise.webapp.exception.NotFoundStorageException;
import com.urise.webapp.model.Resume;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.urise.webapp.storage.ResumeTestData.*;

public abstract class AbstractStorageTest {
    protected static final File STORAGE_DIR = new File(Config.get().getStorageDir());
    protected static final Path STORAGE_PATH = Paths.get(Config.get().getStorageDir());

    protected static final String UUID_1 = "uuid3";
    protected static final String UUID_2 = "uuid2";
    protected static final String UUID_3 = "uuid1";

    // необходимо проверить что "если fullName разных людей совпадает, то сортируйте дополнительно по uuid"
    protected static final String NAME_1 = "Сидор Сидорович Сидоров";
    protected static final String NAME_2 = "Иван Иваныч Иванов";
    protected static final String NAME_3 = "Сидор Сидорович Сидоров";
    protected static final String DUMMY = "Dummy";
    protected static final Resume RESUME_1 = createTestResume(UUID_1, NAME_1);
    protected static final Resume RESUME_2 = createTestResume2(UUID_2, NAME_2);
    protected static final Resume RESUME_3 = createTestResume3(UUID_3, NAME_3);
    protected static final Resume RESUME_DUMMY = createTestResume(DUMMY, DUMMY);
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
    void clear() {
        storage.clear();
        for (int i = 0; i < AbstractArrayStorage.STORAGE_CAPACITY; i++) {
            Assertions.assertNull(((ArrayStorage) storage).storage[i]);
        }
        assertSize(0);
    }

    @Test
    void delete() throws NotFoundStorageException {
        storage.delete(UUID_2);
        Assertions.assertAll(
                () -> assertSize(2),
                () -> Assertions.assertThrows(NotFoundStorageException.class, () -> storage.delete(DUMMY)));
    }

    @Test
    void get() throws NotFoundStorageException {
        Assertions.assertAll(
                () -> assertGet(RESUME_1),
                () -> assertGet(RESUME_2),
                () -> assertGet(RESUME_3),
                () -> Assertions.assertThrows(NotFoundStorageException.class, () -> assertGet(RESUME_DUMMY)));
    }

    @Test
    void getAllSorted() {
        ArrayList<Resume> expected = new ArrayList<>();
        expected.add(RESUME_2);
        expected.add(RESUME_3);
        expected.add(RESUME_1);
        List<Resume> actually = storage.getAllSorted();
        Assertions.assertAll(
                () -> assertSize(3),
                () -> Assertions.assertEquals(expected, actually));
    }

    @Test
    void save() throws FoundDuplicateStorageException {
        Resume newResume = createTestResume(DUMMY, DUMMY);
        storage.save(newResume);
        Assertions.assertAll(
                () -> assertSize(4),
                () -> assertGet(newResume),
                () -> Assertions.assertThrows(FoundDuplicateStorageException.class, () -> storage.save(RESUME_1)));
    }

    @Test
    void size() {
        assertSize(3);
    }

    @Test
    void update() throws NotFoundStorageException {
        Resume newResume = createTestResume(UUID_3, NAME_3);
        storage.update(newResume);
        Assertions.assertAll(
                () -> assertSize(3),
                () -> Assertions.assertEquals(newResume, storage.get(newResume.getUuid())),
                () -> Assertions.assertThrows(NotFoundStorageException.class, () -> storage.update(createTestResume(DUMMY, DUMMY))));
    }

    void assertGet(Resume resume) {
        Assertions.assertEquals(resume, storage.get(resume.getUuid()));
    }

    void assertSize(int size) {
        Assertions.assertEquals(size, storage.size());
    }
}