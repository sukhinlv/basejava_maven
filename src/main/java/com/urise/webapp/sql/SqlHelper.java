package com.urise.webapp.sql;

import com.urise.webapp.exception.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class SqlHelper {
    private final ConnectionFactory connectionFactory;
    private final SqlToStorageException sqlExceptionConverter = new SqlExceptionConverter();

    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = Objects.requireNonNull(connectionFactory, "Connection factory must not be null");
    }

    public <T> T execute(String prepStatement, SQLExecutor<T> executor) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(prepStatement)) {
            return executor.execute(ps);
        } catch (SQLException e) {
            throw sqlExceptionConverter.convert(e);
        }
    }

    public <T> T transactionalExecute(SqlTransaction<T> executor) {
        try (Connection conn = connectionFactory.getConnection()) {
            try {
                conn.setAutoCommit(false);
                T result = executor.execute(conn);
                conn.commit();
                return result;
            } catch (SQLException e) {
                conn.rollback();
                throw sqlExceptionConverter.convert(e);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
}