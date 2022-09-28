package com.urise.webapp.storage;

public class SqlStorageTest extends AbstractStorageTest {

    public SqlStorageTest() {
        // you have to create such test base with credentials or use yours
        // for production database use super(Config.get().getStorage());
        super(new SqlStorage("jdbc:postgresql://localhost:5432/resumes_test", "basejava_user", "Cfnehy"));
    }
}