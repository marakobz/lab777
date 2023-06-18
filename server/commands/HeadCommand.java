package server.commands;

import common.exceptions.NoSuchCommandException;
import common.util.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.utility.CollectionManager;
import server.utility.ResponseOutputer;

/**
 * Command 'head'. Saves the collection to a file.
 */
public class HeadCommand extends AbstractCommand {
    CollectionManager collectionManager;
    private Logger logger = LoggerFactory.getLogger("Head command");

    public HeadCommand(CollectionManager collectionManager) {
        super("head", " show first element of collection");
        this.collectionManager = collectionManager;
    }

    /**
     * Execute of 'head' command.
     */

    @Override
    public void execute(String argument, Object object, User user) throws NoSuchCommandException {
        if (!argument.isEmpty() || object != null) {
            throw new NoSuchCommandException();
        }
        if (collectionManager.getCollection().isEmpty()) {
            ResponseOutputer.appendln("Collection is empty");
            logger.debug("Collection is empty");
        }else {
            ResponseOutputer.appendln(collectionManager.getFirst());
            logger.debug(String.valueOf(collectionManager.getFirst()));
        }

    }
}
