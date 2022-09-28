package com.urise.webapp.sql;

import com.urise.webapp.exception.FoundStorageException;
import com.urise.webapp.exception.StorageException;
import org.postgresql.util.PSQLException;

import java.sql.SQLException;

public class ExceptionUtil {
    private ExceptionUtil() {
    }

    public static StorageException convertException(SQLException e) {
        if (e instanceof PSQLException) {
//            https://postgrespro.ru/docs/postgresql/9.5/errcodes-appendix
            if (e.getSQLState().equals("23505")) {
                return new FoundStorageException(e);
            }
        }
        return new StorageException(e);
    }
}