package server.commands;


import java.io.Serializable;

/**
 * Abstract Command class contains Object methods, name and description.
 */
public abstract class AbstractCommand implements ICommand, Serializable {
    private String name;
    private String description;


    public AbstractCommand(String name, String description){
        this.name = name;
        this.description = description;
    }
    /**
     * @return Name and usage way of the command.
     */
    @Override
    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return name + " : " + description;
    };

    @Override
    public int hashCode() {
        return name.hashCode() + description.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AbstractCommand other = (AbstractCommand) obj;
        return name.equals(other.name) && description.equals(other.description);
    }


}