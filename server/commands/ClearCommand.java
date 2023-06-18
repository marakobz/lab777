package server.commands;

import common.exceptions.*;
import common.models.Ticket;
import common.util.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.utility.CollectionManager;
import server.utility.Console;
import server.utility.DatabaseCollectionHandler;
import server.utility.ResponseOutputer;


/**
 * Command 'clear'. Saves the collection to a file.
 */
public class ClearCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    private DatabaseCollectionHandler databaseCollectionHandler;
    private Logger logger = LoggerFactory.getLogger("Clear command");


    public ClearCommand(CollectionManager collectionManager, DatabaseCollectionHandler databaseCollectionHandler) {
     super("clear", "clear collection");
     this.collectionManager = collectionManager;
     this.databaseCollectionHandler = databaseCollectionHandler;
    }

    /**
     * Execute of 'clear' command.
     */

    @Override
    public void execute(String argument, Object object, User user) throws NoSuchCommandException {
        try {
            if (!argument.isEmpty() || object!= null) throw new WrongAmountOfElementsException();
            for (Ticket ticket : collectionManager.getCollection()) {
                if (!ticket.getUser().equals(user)) throw new PermissionDeniedException();
                if (databaseCollectionHandler.checkTicketUserId(ticket.getId(), user)) throw new ManualDatabaseEditException();
            }
            databaseCollectionHandler.clearCollection();
            collectionManager.clearCollection();
            ResponseOutputer.appendln("Collection is clear");
            logger.debug("Collection is clear");
        } catch (WrongAmountOfElementsException exception) {
            Console.println("Used: '" + getName() + "'");
        } catch (PermissionDeniedException | DatabaseHandlingException | ManualDatabaseEditException e) {
            Console.printerror(e.getMessage());
        }
    }
}