package server.commands;

import common.exceptions.WrongAmountOfElementsException;
import common.models.Ticket;
import common.util.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.utility.CollectionManager;
import server.utility.Console;
import server.utility.ResponseOutputer;

import java.util.Iterator;

/**
 * Command 'average_of_discount'. Saves the collection to a file.
 */
public class AverageOfDiscountCommand extends AbstractCommand {
    CollectionManager collectionManager;
    private Logger logger = LoggerFactory.getLogger("Average of discount command");


    public AverageOfDiscountCommand(CollectionManager collectionManager) {
        super("average_of_discount", "print the average value of the discount field for all items in the collection");
        this.collectionManager = collectionManager;
    }

    /**
     * Execute of 'average_of_discount' command.
     */


    @Override
    public void execute(String argument, Object object, User user){
        try {
            if (!argument.isEmpty()) throw new WrongAmountOfElementsException();
            Iterator<Ticket> disc = collectionManager.getCollection().iterator();
            int sum = 0;
            int count = 0;
            while (disc.hasNext()) {
                Ticket ticket = disc.next();
                sum += ticket.getDiscount();
                count += 1;
            }
            ResponseOutputer.appendln("The average \"discount\" is: " + sum / count);
            logger.debug("The average \"discount\" is: " + sum / count);

        } catch (WrongAmountOfElementsException e) {
            Console.println("Used: '" + getName() + "'");
        }
    }
}
