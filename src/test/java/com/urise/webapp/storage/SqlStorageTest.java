package com.urise.webapp.storage;

public class SqlStorageTest extends AbstractStorageTest {

    public SqlStorageTest() {
        super(new SqlStorage("jdbc:postgresql://localhost:5432/resumes_test", "basejava_user", "Cfnehy"));
    }
}
