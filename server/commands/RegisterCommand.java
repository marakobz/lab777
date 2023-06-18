package server.commands;

import common.exceptions.DatabaseHandlingException;
import common.exceptions.UserAlreadyExistException;
import common.exceptions.WrongAmountOfElementsException;
import common.util.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.utility.*;

public class RegisterCommand extends AbstractCommand{
    private DatabaseUserManager databaseUserManager;
    private Logger logger = LoggerFactory.getLogger("Register command");

    public RegisterCommand(DatabaseUserManager databaseUserManager) {
        super("register",  "внутренняя команда");
        this.databaseUserManager = databaseUserManager;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            if (databaseUserManager.insertUser(user)) {
                ResponseOutputer.appendln("User " +
                        user.getUsername() + " is registered.");

                logger.debug("User " +
                        user.getUsername() + " is registered.");
            }
            else throw new UserAlreadyExistException();
        } catch (WrongAmountOfElementsException exception) {
            Console.printerror("Использование: эммм...эээ.это внутренняя команда...");
        } catch (ClassCastException exception) {
            Console.printerror("Переданный клиентом объект неверен!");
        } catch (DatabaseHandlingException exception) {
            Console.printerror(exception.getMessage());
            Console.printerror("Произошла ошибка при обращении к базе данных!");
        } catch (UserAlreadyExistException exception) {
            Console.printerror("Пользователь " + user.getUsername() + " уже существует!");
        }
    }
}
