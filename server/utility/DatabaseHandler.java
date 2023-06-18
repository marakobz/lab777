package server.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;


public class DatabaseHandler {
    private Logger logger = LoggerFactory.getLogger("DatabaseHandler");
    //Table's name
    public static final String TICKET_TABLE = "ticket";
    public static final String USER_TABLE = "users";
    public static final String PERSON_TABLE = "person";
    public static final String COORDINATES_TABLE = "coordinates";


    //TICKET_TABLE columns
    public static final String TICKET_TABLE_ID_COLUMN = "id";
    public static final String TICKET_TABLE_TICKET_NAME_COLUMN = "ticket_name";
    public static final String TICKET_TABLE_CREATION_DATE_COLUMN = "creation_date";
    public static final String TICKET_TABLE_PRICE_COLUMN = "price";
    public static final String TICKET_TABLE_DISCOUNT_COLUMN = "discount";
    public static final String TICKET_TABLE_REFUND_COLUMN = "refundable";
    public static final String TICKET_TABLE_TICKET_TYPE_COLUMN = "ticket_type";
    public static final String TICKET_TABLE_PERSON_ID_COLUMN = "person_id";
    public static final String TICKET_TABLE_USER_ID_COLUMN = "user_id";

    //USER_TABLE columns
    public static final String USER_TABLE_ID_COLUMN = "id";
    public static final String USER_TABLE_USERNAME_COLUMN = "username";
    public static final String USER_TABLE_PASSWORD_COLUMN = "password";

    //PERSON_TABLE columns
    public static final String PERSON_TABLE_ID_COLUMN = "id";
    public static final String PERSON_TABLE_DATE_OF_BIRTH_COLUMN = "date_of_birth";
    public static final String PERSON_TABLE_HEIGHT_COLUMN = "height";
    public static final String PERSON_TABLE_WEIGHT_COLUMN = "weight";
    public static final String PERSON_TABLE_COUNTRY_COLUMN = "nationality";
    // COORDINATES_TABLE column names
    public static final String COORDINATES_TABLE_TICKET_ID_COLUMN = "ticket_id";
    public static final String COORDINATES_TABLE_X_COLUMN = "x";
    public static final String COORDINATES_TABLE_Y_COLUMN = "y";

    private final String JDBC_DRIVER = "org.postgresql.Driver";

    private String url;
    private String user;
    private String password;
    private Connection connection;
    public DatabaseHandler(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;

        connectToDataBase();
    }

    /**
     * A class for connect to database.
     */
    private void connectToDataBase() {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(url, user, password);
            logger.debug("Connection with database is set.");
        } catch (SQLException exception) {
            logger.error("Mistake occurred while trying to connect to database");
        } catch (ClassNotFoundException exception) {
            logger.error("SQL driver was not found");
        }
    }

    /**
     * @param sqlStatement SQL statement to be prepared.
     * @param generateKeys Is keys needed to be generated.
     * @return Pprepared statement.
     * @throws SQLException When there's exception inside.
     */
    public PreparedStatement getPreparedStatement(String sqlStatement, boolean generateKeys) throws SQLException {
        PreparedStatement preparedStatement;
        try {
            if (connection == null) throw new SQLException();
            int autoGeneratedKeys = generateKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS;
            preparedStatement = connection.prepareStatement(sqlStatement, autoGeneratedKeys);
            logger.debug("SQL request '" + sqlStatement + "' is ready.");
            return preparedStatement;
        } catch (SQLException exception) {
            logger.error("Mistake occurred while preparing '" + sqlStatement + "' request.");
            if (connection == null) logger.error("Connection with database is not set");
            throw new SQLException(exception);
        }
    }

    /**
     * Close prepared statement.
     *
     * @param sqlStatement SQL statement to be closed.
     */
    public void closePreparedStatement(PreparedStatement sqlStatement) {
        if (sqlStatement == null) return;
        try {
            sqlStatement.close();
            logger.debug("SQL request '" + sqlStatement + "' is closed.");
        } catch (SQLException exception) {
            logger.error("Mistake occurred while closing '" + sqlStatement + "' SQL request.");
        }
    }

    /**
     * Close connection to database.
     */
    public void closeConnection() {
        if (connection == null) return;
        try {
            connection.close();
            logger.debug("The connection to the database is ended.");
        } catch (SQLException exception) {
            logger.error("An error occurred when disconnecting from the database");
        }
    }

    /**
     * Set commit mode of database.
     */
    public void setCommitMode() {
        try {
            if (connection == null) throw new SQLException();
            connection.setAutoCommit(false);
        } catch (SQLException exception) {
            logger.error("An error occurred while setting the database transaction mode");
        }
    }

    /**
     * Set normal mode of database.
     */
    public void setNormalMode() {
        try {
            if (connection == null) throw new SQLException();
            connection.setAutoCommit(true);
        } catch (SQLException exception) {
            logger.error("An error occurred while setting the normal database mode");
        }
    }

    /**
     * Commit database status.
     */
    public void commit() {
        try {
            if (connection == null) throw new SQLException();
            connection.commit();
        } catch (SQLException exception) {
            logger.error("An error occurred while confirming the new state of the database");
        }
    }

    /**
     * Roll back database status.
     */
    public void rollback() {
        try {
            if (connection == null) throw new SQLException();
            connection.rollback();
        } catch (SQLException exception) {
            logger.error("Mistake occurred while rolling back database condition");
        }
    }

    /**
     * Set save point of database.
     */
    public void setSavepoint() {
        try {
            if (connection == null) throw new SQLException();
            connection.setSavepoint();
        } catch (SQLException exception) {
            logger.error("Mistake occurred while trying to save database condition");
        }
    }
}
