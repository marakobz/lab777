package server.utility;

import common.exceptions.DatabaseHandlingException;
import common.util.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUserManager {

    private Logger logger = LoggerFactory.getLogger("DatabaseUserManager");
    // USER_TABLE
    private final String SELECT_USER_BY_ID = "SELECT * FROM " + DatabaseHandler.USER_TABLE +
            " WHERE " + DatabaseHandler.USER_TABLE_ID_COLUMN + " = ?";
    private final String SELECT_USER_BY_USERNAME = "SELECT * FROM " + DatabaseHandler.USER_TABLE +
            " WHERE " + DatabaseHandler.USER_TABLE_USERNAME_COLUMN + " = ?";
    private final String SELECT_USER_BY_USERNAME_AND_PASSWORD = SELECT_USER_BY_USERNAME + " AND " +
            DatabaseHandler.USER_TABLE_PASSWORD_COLUMN + " = ?";
    private final String INSERT_USER = "INSERT INTO " +
            DatabaseHandler.USER_TABLE + " (" +
            DatabaseHandler.USER_TABLE_USERNAME_COLUMN + ", " +
            DatabaseHandler.USER_TABLE_PASSWORD_COLUMN + ") VALUES (?, ?)";

    private final DatabaseHandler databaseHandler;

    public DatabaseUserManager(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    /**
     * @param userId User id.
     * @return User by id.
     * @throws SQLException When there's exception inside.
     */
    public User getUserById(int userId) throws SQLException {
        User user;
        PreparedStatement preparedSelectUserByIdStatement = null;
        try {
            preparedSelectUserByIdStatement =
                    databaseHandler.getPreparedStatement(SELECT_USER_BY_ID, false);
            preparedSelectUserByIdStatement.setInt(1, userId);
            ResultSet resultSet = preparedSelectUserByIdStatement.executeQuery();
            logger.debug("Выполнен запрос SELECT_USER_BY_ID.");
            if (resultSet.next()) {
                user = new User(
                        resultSet.getString(DatabaseHandler.USER_TABLE_USERNAME_COLUMN),
                        resultSet.getString(DatabaseHandler.USER_TABLE_PASSWORD_COLUMN)
                );
            } else throw new SQLException();
        } catch (SQLException exception) {
            logger.error("Mistake occurred while trying to process SELECT_USER_BY_ID!");
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectUserByIdStatement);
        }
        return user;
    }

    /**
     * Check user by username and password.
     *
     * @param user User.
     * @return Result set.
     * @throws DatabaseHandlingException When there's exception inside.
     */
    public boolean checkUserByUsernameAndPassword(User user) throws DatabaseHandlingException {
        PreparedStatement preparedSelectUserByUsernameAndPasswordStatement = null;
        try {
            preparedSelectUserByUsernameAndPasswordStatement =
                    databaseHandler.getPreparedStatement(SELECT_USER_BY_USERNAME_AND_PASSWORD, false);
            preparedSelectUserByUsernameAndPasswordStatement.setString(1, user.getUsername());
            preparedSelectUserByUsernameAndPasswordStatement.setString(2, user.getPassword());
            ResultSet resultSet = preparedSelectUserByUsernameAndPasswordStatement.executeQuery();
            logger.debug("Request is completed: SELECT_USER_BY_USERNAME_AND_PASSWORD.");
            return resultSet.next();
        } catch (SQLException exception) {
            logger.error("Mistake occurred while trying to process SELECT_USER_BY_USERNAME_AND_PASSWORD!");
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectUserByUsernameAndPasswordStatement);
        }
    }

    /**
     * Get user id by username.
     *
     * @param user User.
     * @return User id.
     * @throws DatabaseHandlingException When there's exception inside.
     */
    public int getUserIdByUsername(User user) throws DatabaseHandlingException {
        int userId;
        PreparedStatement preparedSelectUserByUsernameStatement = null;
        try {
            preparedSelectUserByUsernameStatement =
                    databaseHandler.getPreparedStatement(SELECT_USER_BY_USERNAME, false);
            preparedSelectUserByUsernameStatement.setString(1, user.getUsername());
            ResultSet resultSet = preparedSelectUserByUsernameStatement.executeQuery();
            logger.info("Request is completed: SELECT_USER_BY_USERNAME.");
            if (resultSet.next()) {
                userId = resultSet.getInt(DatabaseHandler.USER_TABLE_ID_COLUMN);
            } else userId = -1;
            return userId;
        } catch (SQLException exception) {
            logger.error(String.valueOf(exception.getErrorCode()));
            logger.error(exception.getMessage());
            logger.error("Mistake occurred while trying to process SELECT_USER_BY_USERNAME!");
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectUserByUsernameStatement);
        }
    }

    /**
     * Insert user.
     *
     * @param user User.
     * @return Status of insert.
     * @throws DatabaseHandlingException When there's exception inside.
     */
    public boolean insertUser(User user) throws DatabaseHandlingException {
        PreparedStatement preparedInsertUserStatement = null;
        try {
            if (getUserIdByUsername(user) != -1) return false;
            preparedInsertUserStatement =
                    databaseHandler.getPreparedStatement(INSERT_USER, false);
            preparedInsertUserStatement.setString(1, user.getUsername());
            preparedInsertUserStatement.setString(2, user.getPassword());
            if (preparedInsertUserStatement.executeUpdate() == 0) throw new SQLException();
            logger.debug("Request is completed: INSERT_USER.");
            return true;
        } catch (SQLException exception) {
            logger.error("Mistake occurred while trying to process INSERT_USER!");
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedInsertUserStatement);
        }
    }
}
