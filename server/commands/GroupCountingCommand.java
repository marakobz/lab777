package server.commands;

import common.exceptions.NoSuchCommandException;
import common.models.Ticket;
import common.util.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.utility.CollectionManager;
import server.utility.Console;
import server.utility.ResponseOutputer;

/**
 * Command 'group_counting'. Saves the collection to a file.
 */
public class GroupCountingCommand extends AbstractCommand{
    CollectionManager collectionManager;
    Ticket ticket;
    private Logger logger = LoggerFactory.getLogger("Group counting command");

    public GroupCountingCommand(CollectionManager collectionManager){
        super("group_counting","group the elements of the collection by the value of the CreationDate field, output the number of elements in each group");
        this.collectionManager = collectionManager;
    }

    /**
     * Execute of 'group_counting' command.
     */

    @Override
    public void execute(String argument, Object object, User user) throws NoSuchCommandException {
        try{
            if (!argument.isEmpty() || object != null) throw new NoSuchCommandException();
            collectionManager.groupCountingByCrDate();
            //logger.debug(String.valueOf(collectionManager.groupCountingByCrDate()));

        } catch (NoSuchCommandException e){
            Console.println("Used: '" + getName() + "'");

        }
    }

}
