package server.commands;

import common.models.Ticket;
import common.util.TicketRaw;
import common.util.User;
import common.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.utility.*;
import client.utility.OrganizationAsker;

import java.time.LocalDateTime;

/**
 * This is command 'remove_greater_key'. Remove elements which have key that is more than given.
 */
public class RemoveGreaterCommand extends AbstractCommand implements ICommand {
    CollectionManager collectionManager;
    OrganizationAsker organizationAsker;
    private Logger logger = LoggerFactory.getLogger("Remove greater command");

    public RemoveGreaterCommand(CollectionManager collectionManager, OrganizationAsker organizationAsker){
        super("remove_greater", "remove all items from the collection that exceed the specified");
        this.collectionManager = collectionManager;
        this.organizationAsker = organizationAsker;
    }

    /**
     * Execute of 'remove_greater' command.
     */

    @Override
    public void execute(String argument, Object object, User user) {
        try {
            if (argument.isEmpty() || object != null) {
                throw new NoSuchCommandException();
            }
            if (collectionManager.collectionSize() == 0) {
                Console.println("Cannot remove object");
            }
            TicketRaw ticketRaw = (TicketRaw) object;
           var ticket = new Ticket(
                   0,
                    ticketRaw.getName(),
                    ticketRaw.getCoordinates(),
                    LocalDateTime.now(),
                    ticketRaw.getPrice(),
                    ticketRaw.getDiscount(),
                    ticketRaw.getRefundable(),
                    ticketRaw.getType(),
                    ticketRaw.getPerson(),
                    user
            );
            Ticket ticketColl = collectionManager.getByValue(ticket);
            if (ticketColl == null) {
                Console.println("Cannot remove object");
            }
            collectionManager.removeGreater(ticketColl);
            ResponseOutputer.appendln("Greater of ticket is removed");
            logger.debug("Greater of " + ticketColl + " is deleted");
        } catch (NoSuchCommandException e) {
            Console.printerror(e.getMessage());
        }
    }
}
