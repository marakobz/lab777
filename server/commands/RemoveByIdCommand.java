package server.commands;

import common.exceptions.DatabaseHandlingException;
import common.exceptions.ManualDatabaseEditException;
import common.exceptions.NoSuchCommandException;
import common.exceptions.PermissionDeniedException;
import common.models.Ticket;
import common.util.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.utility.*;


/**
 * Command 'remove_by_id'. Saves the collection to a file.
 */
public class RemoveByIdCommand extends AbstractCommand{
    CollectionManager collectionManager;
    DatabaseCollectionHandler databaseCollectionHandler;
    private Logger logger = LoggerFactory.getLogger("Remove by id command");

    public RemoveByIdCommand(CollectionManager collectionManager, DatabaseCollectionHandler databaseCollectionHandler){
        super("remove_by_id","delete an item from the collection by its id");
        this.collectionManager = collectionManager;
        this.databaseCollectionHandler = databaseCollectionHandler;
    }

    /**
     * Execute of 'remove_by_id' command.
     */


    @Override
    public void execute(String argument, Object object, User user) {
        if (argument.isEmpty() || object != null) {
            throw new NoSuchCommandException();
        }
        try {
            if (collectionManager.collectionSize() == 0) {
                Console.println("Cannot remove object");
            }
            int id = Integer.parseInt(argument);
            Ticket ticketToRemove = collectionManager.getById(id);

            if (ticketToRemove == null) {
                Console.println("Cannot remove object");
            }
            if (ticketToRemove != null && !ticketToRemove.getUser().equals(user)) throw new PermissionDeniedException();
            if (ticketToRemove != null && databaseCollectionHandler.checkTicketUserId(ticketToRemove.getId(), user))
                throw new ManualDatabaseEditException();

            databaseCollectionHandler.deleteTicketById(id);
            collectionManager.removeFromCollection(ticketToRemove);
            ResponseOutputer.appendln("Ticket is deleted");
            logger.debug("Ticket " + ticketToRemove + " is deleted");
        } catch (NumberFormatException e) {
            Console.println("the argument must be a long number");
        } catch (DatabaseHandlingException | ManualDatabaseEditException e) {
            Console.printerror(e.getMessage());
        } catch (PermissionDeniedException e) {
            throw new RuntimeException(e);
        }
    }

}
