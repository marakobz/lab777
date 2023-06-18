package server.commands;

import common.exceptions.DatabaseHandlingException;
import common.exceptions.UserIsNotFoundException;
import common.exceptions.WrongAmountOfElementsException;
import common.util.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.utility.*;

public class LoginCommand extends AbstractCommand{
    private DatabaseUserManager databaseUserManager;
    private Logger logger = LoggerFactory.getLogger("Login command");

    public LoginCommand(DatabaseUserManager databaseUserManager) {
        super("login", "внутренняя команда");
        this.databaseUserManager = databaseUserManager;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            if (databaseUserManager.checkUserByUsernameAndPassword(user)){
                ResponseOutputer.appendln("User " +
                    user.getUsername() + " is authorized.");

                logger.debug("User " +
                        user.getUsername() + " is authorized.");
            }
            else throw new UserIsNotFoundException();
        } catch (WrongAmountOfElementsException exception) {
            Console.printerror("Использование: эммм...эээ.это внутренняя команда...");
        } catch (ClassCastException exception) {
            Console.printerror("Переданный клиентом объект неверен!");
        } catch (DatabaseHandlingException exception) {
            Console.printerror("Произошла ошибка при обращении к базе данных!");
        } catch (UserIsNotFoundException exception) {
            Console.printerror("Неправильные имя пользователя или пароль!");
        }
    }
}
