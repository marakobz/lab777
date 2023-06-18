package server.commands;

import client.utility.OrganizationAsker;
import common.exceptions.*;
import common.models.Coordinates;
import common.models.Person;
import common.models.Ticket;
import common.models.TicketType;
import common.util.TicketRaw;
import common.util.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.utility.*;
import server.utility.CollectionManager;

import java.time.LocalDateTime;

/*
TODO сделать нормальное выполнение
 */

/**
 * This is command 'update'. Refreshes an element of collection which id equals given one.
 */
public class UpdateCommand extends AbstractCommand{
    CollectionManager collectionManager;
    OrganizationAsker organizationAsker;
    DatabaseCollectionHandler databaseCollectionHandler;
    private Logger logger = LoggerFactory.getLogger("Update command");

    public UpdateCommand(CollectionManager collectionManager, OrganizationAsker organizationAsker, DatabaseCollectionHandler databaseCollectionHandler){
        super("update", " ID - reload the value of the collection item");
        this.collectionManager = collectionManager;
        this.organizationAsker = organizationAsker;
        this.databaseCollectionHandler = databaseCollectionHandler;
    }

    /**
     * Execute of 'update' command.
     */

    @Override
    public void execute(String argument, Object object, User user) {
        try {
            if (argument.isEmpty() || object == null) throw new WrongAmountOfElementsException();

            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();

            var id = Integer.parseInt(argument);
            if (id <= 0) throw new NumberFormatException();
            var oldTicket = collectionManager.getById(id);

            if (oldTicket == null) throw new TicketNotFoundException();
            if (!oldTicket.getUser().equals(user)) throw new PermissionDeniedException();
            if (databaseCollectionHandler.checkTicketUserId(oldTicket.getId(), user)) throw new ManualDatabaseEditException();

            TicketRaw ticketRaw = (TicketRaw) object;

            databaseCollectionHandler.updateTicketById(id, ticketRaw);

            String name = ticketRaw.getName() == null ? oldTicket.getName() : ticketRaw.getName();
            Coordinates coordinates = ticketRaw.getCoordinates() == null ? oldTicket.getCoordinates() : ticketRaw.getCoordinates();
            LocalDateTime creationDate = oldTicket.getCreationDate();
            int price = ticketRaw.getPrice() == -100 ? oldTicket.getPrice() : ticketRaw.getPrice();
            long discount = ticketRaw.getDiscount() == -100 ? oldTicket.getDiscount() : ticketRaw.getDiscount();
            Boolean refundable = ticketRaw.getRefundable() == null ? oldTicket.getRefundable() : ticketRaw.getRefundable();
            TicketType type = ticketRaw.getType() == null ? oldTicket.getType() : ticketRaw.getType();
            Person person = ticketRaw.getPerson() == null ? oldTicket.getPerson() : ticketRaw.getPerson();

            collectionManager.removeFromCollection(oldTicket);


            collectionManager.addToCollection(
                    new Ticket(
                            id,
                            name,
                            coordinates,
                            creationDate,
                            price,
                            discount,
                            refundable,
                            type,
                            person,
                            user
                    ));
            ResponseOutputer.appendln("Ticket is changed");
            logger.debug("Ticket " + oldTicket + " is updated and replaced with new one");
        }catch (TicketNotFoundException | CollectionIsEmptyException | WrongAmountOfElementsException |
                PermissionDeniedException | DatabaseHandlingException | ManualDatabaseEditException e) {
            logger.error(e.getMessage());
        }
    }
}
