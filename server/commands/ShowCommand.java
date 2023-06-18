package server.commands;

import common.exceptions.NoSuchCommandException;
import common.util.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.utility.*;

/**
 * This is command 'show'. Prints all elements of collection.
 */

public class ShowCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    private Logger logger = LoggerFactory.getLogger("Show command");

    public ShowCommand(CollectionManager collectionManager) {
        super("show", "show all collection's elements");
        this.collectionManager = collectionManager;
    }

    /**
     * Execute of 'show' command.
     */
    @Override
    public void execute(String argument, Object object, User user) {
        try {
            if (!argument.isEmpty() || object != null) throw new NoSuchCommandException();
            ResponseOutputer.appendln("\nAll elements of collection: \n" +  collectionManager.toString());
            logger.debug("\nAll elements of collection: \n" +  collectionManager.toString());

        }catch (NoSuchCommandException ex){
            Console.println("Collection is empty\n");
        }

    }
}
