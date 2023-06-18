package server.commands;

import common.exceptions.NoSuchCommandException;
import common.util.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.utility.*;
import client.utility.UserIO;


/**
 * Command 'execute_script'. Saves the collection to a file.
 */
public class ExecuteScriptCommand extends AbstractCommand{
    private UserIO userIO;
    private Logger logger = LoggerFactory.getLogger("Execute script command");

    public ExecuteScriptCommand(UserIO userIO){
        super("execute_script", "read and execute the script from the specified file");
        this.userIO = userIO;
    }

    /**
     * Execute of 'execute_script' command.
     */

    @Override
    public void execute(String argument, Object object, User user) throws NoSuchCommandException {
        if (argument.isEmpty() || object != null) {
            Console.println("Used: '" + getName() + "'");
            throw new NoSuchCommandException();
        }
        ResponseOutputer.appendln("Reading the given script ...");
        ResponseOutputer.appendln(userIO.startReadScript(argument));

        logger.debug("Reading the given script ...");
        logger.debug((String) userIO.startReadScript(argument));

    }
}

