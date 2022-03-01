package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.util.json;

/**
 * This interface listens to a JSONReader while parsing. If the JSONReader reads a new JSON statement
 * it will call the method which belongs to the event / statement.
 */
public interface JSONReaderListener
{
    /**
     * This method is called at the beginning of parsing.
     */
    void beginOfJSON();

    /**
     * This method is called if the end of the json is reached.
     */
    void endOfJSON();

    /**
     * This method is called if a new json object begins.
     */
    void beginOfObject();

    /**
     * This method is called if the current json object ends.
     */
    void endOfObject();

    /**
     * This method is called if a new json array begins.
     * @param name The name of the array.
     */
    void beginOfArray(String name);

    /**
     * This method is called if the current json array ends.
     */
    void endOfArray();

    /**
     * This method is called if there is an json attribute with a value.
     * @param name The name of the attribute.
     * @param value The value of the attribute.
     */
    void newAttribute(String name, String value);

    /**
     * This method is called if an Exception is thrown.
     * @param e The Exception instance.
     */
    void exceptionThrown(Exception e);

}
