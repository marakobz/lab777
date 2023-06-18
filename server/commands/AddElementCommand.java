package server.commands;

import common.exceptions.DatabaseHandlingException;
import common.exceptions.NoSuchCommandException;
import common.exceptions.WrongAmountOfElementsException;
import common.util.TicketRaw;
import common.util.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.utility.CollectionManager;
import server.utility.Console;
import server.utility.DatabaseCollectionHandler;
import server.utility.ResponseOutputer;

/**
 * Command 'add_element'. Saves the collection to a file.
 */
public class AddElementCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    private DatabaseCollectionHandler databaseCollectionHandler;
    private Logger logger = LoggerFactory.getLogger("Add element command");


    public AddElementCommand(CollectionManager collectionManager, DatabaseCollectionHandler databaseCollectionHandler){
        super("add","add a new item to the collection");
        this.collectionManager = collectionManager;
        this.databaseCollectionHandler = databaseCollectionHandler;

    }

    /**
     * Execute of 'add_element' command.
     */
    @Override
    public void execute(String argument, Object object, User user) throws NoSuchCommandException {
        try {
            if (!argument.isEmpty() || object == null) throw new WrongAmountOfElementsException();
            TicketRaw ticketRaw = (TicketRaw) object;
            collectionManager.addToCollection(databaseCollectionHandler.insertTicket(ticketRaw, user)
            );
            ResponseOutputer.appendln("Ticket is created");
            logger.debug("Ticket" + ticketRaw + " is created");
        } catch (WrongAmountOfElementsException e) {
            ResponseOutputer.appenderror("Used: " + getName());
        } catch (DatabaseHandlingException e) {
            Console.printerror(e.getMessage());
        }
    }
}
