package server.commands;

import common.exceptions.WrongAmountOfElementsException;
import common.util.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.utility.*;

import java.time.LocalDate;

/**
 * Command 'info'. Saves the collection to a file.
 */

public class InfoCommand extends AbstractCommand {
    private Logger logger = LoggerFactory.getLogger("Info command");

    private LocalDate creationDate;
    private CollectionManager collectionManager;

    public InfoCommand(CollectionManager collectionManager) {
        super("info", "shows information about the commands");
        this.collectionManager = collectionManager;
        creationDate = LocalDate.now();
    }

    /**
     * Execute of 'info' command.
     */

    @Override
    public void execute(String argument, Object object, User user) {
        try {
            if (!argument.isEmpty() || object != null) throw new WrongAmountOfElementsException();

            ResponseOutputer.appendln(
                    "Info about collection:" + " \n"
                            + "  Creation Date:" + creationDate + " \n"
                            + "  Number of elements:" + collectionManager.collectionSize()
            );
            logger.debug(
                    "Info about collection:" + " \n"
                            + "  Creation Date:" + creationDate + " \n"
                            + "  Number of elements:" + collectionManager.collectionSize()
            );
        } catch (WrongAmountOfElementsException exception) {
            Console.println("Used: '" + getName() + "'");
        }
    }

}

