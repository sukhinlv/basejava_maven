package com.urise.webapp;

import com.urise.webapp.storage.SqlStorage;
import com.urise.webapp.storage.Storage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final String PROPS = "/resumes.properties";
    private static String storageDir;
    private static SqlStorage storage;
    private static final Config INSTANCE = new Config();

    private Config() {
        // https://jdbc.postgresql.org/documentation/81/load.html
        // https://stackoverflow.com/questions/34763020/class-fornamedriver-class
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (InputStream is = Config.class.getResourceAsStream(PROPS)) {
            var properties = new Properties();
            properties.load(is);
            storageDir = properties.getProperty("storage.dir");
            String dbUrl = properties.getProperty("db.url");
            String dbUser = properties.getProperty("db.user");
            String dbPassword = properties.getProperty("db.password");
            storage = new SqlStorage(dbUrl, dbUser, dbPassword);
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file " + PROPS, e);
        }
    }

    public static Config get() {
        return INSTANCE;
    }

    public String getStorageDir() {
        return storageDir;
    }

    public Storage getStorage() {
        return storage;
    }
}