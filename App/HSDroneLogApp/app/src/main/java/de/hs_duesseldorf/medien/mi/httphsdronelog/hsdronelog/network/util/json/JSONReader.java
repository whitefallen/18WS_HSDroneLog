package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.util.json;

import java.security.InvalidParameterException;
import java.util.Stack;

/**
 * A JSONReader reads a json formatted String and hands over the data to a {@link JSONReaderListener}.
 * So a component can react individually to the parsed data.
 * <br>
 * This object will loop through the complete input String and interprets every single character.
 */
public class JSONReader
{
    //INSTANCE VARIABLES//////////////////////////////////////////////////////////////////////////

    /**The listener to hand over the results*/
    private JSONReaderListener m_jsonReaderListener;
    /**The String instance to get the input chars from*/
    private String m_jsonString;
    /**Saves the character read from the {@link #m_jsonString}*/
    private char m_characterRead;
    /**Saves each '"','{','[' and removes it when it is closed.*/
    private Stack<Character> m_signalCharStack;
    /**The buffer to check words.*/
    private String m_wordBuffer;
    /**Saves the name of the current json statement*/
    private String m_name;

    //CONSTRUCTORS////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new JSONReader instance. This object is used to parse a json formatted String.
     * @see {@link #parseJSON(JSONReaderListener, String)} for more information.
     */
    public JSONReader()
    {
        this.m_signalCharStack = new Stack<>();

        this.reset();
    }

    //PUBLIC METHODS//////////////////////////////////////////////////////////////////////////////

    /**
     * Parses a json formatted String. This method is using a {@link JSONReaderListener} to hand over the data.<br>
     * Note that the methods from the listener are called directly after reading the specific part of the String.
     * So {@link JSONReaderListener#exceptionThrown(Exception)} can occur after receiving some data.
     * If that happens you should discard all the data received so far.
     * @param listener The JSONReaderListener instance to handle over the data.
     * @param jsonString The json formatted string.
     * @throws InvalidParameterException If the listener is null.
     */
    public synchronized void parseJSON(JSONReaderListener listener, String jsonString) throws InvalidParameterException
    {
        if (listener == null)
            throw new InvalidParameterException("listener cant be null.");

        this.m_jsonReaderListener = listener;
        this.m_jsonString = jsonString;

        this.startParsing();
    }

    //PRIVATE METHODS/////////////////////////////////////////////////////////////////////////////

    /**
     * Resets this instance so that the instance is in the same state like after the construction.
     */
    private void reset() {
        this.m_jsonReaderListener = null;
        this.m_jsonString = null;
        this.m_characterRead = 0;
        this.m_signalCharStack.clear();
        this.m_wordBuffer = "";
        this.m_name = null;
    }

    /**
     * Starts parsing the json string. This method calls methods from a {@link JSONReaderListener} when
     * the specific part of the json was read.
     */
    private void startParsing()
    {
        //Log.d("JSON PARSER", this.m_jsonString);

        //beginning of parsing
        this.m_jsonReaderListener.beginOfJSON();

        try
        {
            //check if json input string is valid
            this.checkForLegalJsonString();

            //loop for every char in json String
            for (int i = 0 ; i < this.m_jsonString.length() ; i++)
            {
                //read character at current position
                this.m_characterRead = this.m_jsonString.charAt(i);

                //check for case of character read
                switch (this.m_characterRead)
                {
                    //signal character for a starting json object
                    case '{':
                        this.signalCharBeginObjectRead();
                        break;
                    //signal character for a closing json object
                    case '}':
                        this.signalCharEndObjectRead(i);
                        break;
                    //signal character for a starting json array
                    case '[':
                        signalCharBeginArrayRead(i);
                        break;
                    //signal character for a closing json array
                    case ']':
                        this.signalCharEndArrayRead(i);
                        break;
                    //signal character for a starting json name or value
                    case '"':
                        this.signalCharStartingNameOrValueRead(i);
                        break;
                    //signal character that the following part is the value of the previous name (this can also be a starting array)
                    case ':':
                        this.signalCharValuePartRead(i);
                        break;
                    //signal character that the current json statement ends and a new one will follow
                    case ',':
                        this.signalCharStatementSeparatorRead(i);
                        break;
                    //default handling of characters
                    default:
                        this.defaultHandling();
                }

            } // end while

            this.m_jsonReaderListener.endOfJSON();
        } catch (IllegalStateException e) {
            this.m_jsonReaderListener.exceptionThrown(e);
        } finally {
            //reset instance to originally state
            this.reset();
        }
    }

    /**
     * Checks if a new json object starts or the signal char belongs to a name / value and must
     * be handled by {@link #defaultHandling()} method.
     */
    private void signalCharBeginObjectRead()
    {
        //check if the character implies a beginning json object
        if(this.m_signalCharStack.isEmpty() || this.m_signalCharStack.peek() != '"')
        {
            //push signal char to stack + hand over begin of object to listener
            this.m_signalCharStack.push('{');
            this.m_jsonReaderListener.beginOfObject();
        }
        else
        {
            //default handling because char belongs to a name or value
            this.defaultHandling();
        }
    }

    /**
     * Checks if a new json object ends or the signal char belongs to a name / value and must
     * be handled by {@link #defaultHandling()} method.
     * @param charReadPosition The position of the character read.
     * @throws IllegalStateException If this instance is in an illegal state for an operation.
     */
    private void signalCharEndObjectRead(int charReadPosition) throws IllegalStateException
    {
        //check if this operation is valid in the current state of this instance
        this.checkForInvalidOperation(charReadPosition);

        //check if an json object can be closed
        if(this.m_signalCharStack.peek() == '{')
        {
            //check if there is any name value for an attribute so the attribute has not been hand over to the listener yet.
            if(this.m_name != null)
                //hand over new attribute
                this.handOverNewAttribute();

            //remove beginning json object signal char from stack + hand over end of object to listener
            this.m_signalCharStack.pop();
            this.m_jsonReaderListener.endOfObject();

            //clear word buffer
            this.m_wordBuffer = "";
        }
        else
        {
            //default handling because char belongs to a name or value
            this.defaultHandling();
        }
    }

    /**
     * Checks if a new json array begins or the signal char belongs to a name / value and must
     * be handled by {@link #defaultHandling()} method.
     * @param charReadPosition The position of the character read.
     * @throws IllegalStateException If this instance is in an illegal state for an operation.
     */
    private void signalCharBeginArrayRead(int charReadPosition) throws IllegalStateException
    {
        //check if this operation is valid in the current state of this instance
        this.checkForInvalidOperation(charReadPosition);

        //checks if currently reading a name or value statement
        if(this.m_signalCharStack.peek() != '"')
        {
            //push signal char to stack + hand over begin of array to listener
            this.m_signalCharStack.push('[');
            this.m_jsonReaderListener.beginOfArray(this.m_name);
            this.m_name = null;
        }
        else
        {
            //default handling because char belongs to a name or value
            this.defaultHandling();
        }
    }

    /**
     * Checks if a json array ends or the signal char belongs to a name / value and must
     * be handled by {@link #defaultHandling()} method.
     * @param charReadPosition The position of the character read.
     * @throws IllegalStateException If this instance is in an illegal state for an operation.
     */
    private void signalCharEndArrayRead(int charReadPosition) throws IllegalStateException
    {
        //check if this operation is valid in the current state of this instance
        this.checkForInvalidOperation(charReadPosition);

        //checks if currently reading a name or value statement
        if(this.m_signalCharStack.peek() == '[')
        {
            //check if there is any name value for an attribute so the attribute has not been hand over to the listener yet.
            if(this.m_name != null)
                //hand over new attribute
                this.handOverNewAttribute();
            else if (!this.m_wordBuffer.isEmpty())
                //hand over new attribute with name = null
                this.handOverNewAttribute();

            //remove beginning json array signal char from stack + hand over end of array to listener
            this.m_signalCharStack.pop();
            this.m_jsonReaderListener.endOfArray();

            //clear word buffer
            this.m_wordBuffer = "";
        }
        else
        {
            //default handling because char belongs to a name or value
            this.defaultHandling();
        }
    }

    /**
     * Checks if a json name or value begins or the signal char belongs to a name / value and must
     * be handled by {@link #defaultHandling()} method.
     * @param charReadPosition The position of the character read.
     * @throws IllegalStateException If this instance is in an illegal state for an operation.
     */
    private void signalCharStartingNameOrValueRead(int charReadPosition) throws IllegalStateException
    {
        //check if this operation is valid in the current state of this instance
        this.checkForInvalidOperation(charReadPosition);

        //checks if currently reading a name or value statement
        if(this.m_signalCharStack.peek() != '"')
        {
            //push signal char to the stack
            this.m_signalCharStack.push('"');
        }
        else
        {
            //pop last item on signal char stack because the current name / value ends
            this.m_signalCharStack.pop();
        }
    }


    /**
     * Checks if a json value will follow the last statement or the signal char belongs to a name / value and must
     * be handled by {@link #defaultHandling()} method.
     * @param charReadPosition The position of the character read.
     * @throws IllegalStateException If this instance is in an illegal state for an operation.
     */
    private void signalCharValuePartRead(int charReadPosition) throws IllegalStateException
    {
        //check if this operation is valid in the current state of this instance
        this.checkForInvalidOperation(charReadPosition);

        //checks if currently reading a name or value statement
        if(this.m_signalCharStack.peek() != '"')
        {
            //name read so save word from buffer in name and clear word buffer
            this.m_name = this.m_wordBuffer;
            this.m_wordBuffer = "";
        }
        else
        {
            //default handling because char belongs to a name or value
            this.defaultHandling();
        }
    }

    /**
     * Checks if a json attribute has not been saved yet and saves it or if the signal char belongs to a name / value and must
     * be handled by {@link #defaultHandling()} method.
     * @param charReadPosition The position of the character read.
     * @throws IllegalStateException If this instance is in an illegal state for an operation.
     */
    private void signalCharStatementSeparatorRead(int charReadPosition)
    {
        //check if this operation is valid in the current state of this instance
        this.checkForInvalidOperation(charReadPosition);

        //checks if currently reading a name or value statement
        if(this.m_signalCharStack.peek() != '"')
        {
            //check if there is any name value for an attribute so the attribute has not been hand over to the listener yet.
            if(this.m_name != null)
                //hand over new attribute
                this.handOverNewAttribute();
            else if (this.m_signalCharStack.peek() == '[')
            {
                //hand over new attribute
                this.handOverNewAttribute();
            }

            //clear word buffer
            this.m_wordBuffer = "";
        }
        else
        {
            //default handling because char belongs to a name or value
            this.defaultHandling();
        }
    }

    /**
     * Checks if the current state of this instance is legal for a certain operation.
     * This methods checks if the {@link #m_signalCharStack} is empty and so the only valid operation
     * is to begin a new json object.
     * @param i The position of the character read.
     * @throws IllegalStateException If this instace is in an illegal state for an operation.
     */
    private void checkForInvalidOperation(int i) throws IllegalStateException
    {
        if (this.m_signalCharStack.isEmpty())
            throw new IllegalStateException("Invalid char at position " + i + " ('" + this.m_characterRead + "'). " +
                    "Cant close not existing json object.\n" + this.m_jsonString);
    }

    /**
     * Checks if {@link #m_jsonString} has a valid value. This method does not check for null value.
     * @throws IllegalStateException if the jsonString has an invalid value and so this instance is in
     * an illegal state.
     */
    private void checkForLegalJsonString() throws IllegalStateException
    {
        if (this.m_jsonString.length() == 0)
            throw new IllegalStateException("Length of json input string is 0");
        if (!this.m_jsonString.startsWith("{"))
            throw new IllegalStateException("Json String has to start with an beginning json object.\n" + this.m_jsonString);
        if (!this.m_jsonString.endsWith("}"))
            throw new IllegalStateException("Json String has to end with an ending json object.\n" + this.m_jsonString);
    }

    /**
     * The default handling for characters read.
     * This method will add the character to the {@link #m_wordBuffer}.
     */
    private void defaultHandling ()
    {
        this.m_wordBuffer += this.m_characterRead;
    }

    /**
     * Hands over a new json attribute to the {@link #m_jsonReaderListener}.
     */
    private void handOverNewAttribute()
    {
        this.m_jsonReaderListener.newAttribute(this.m_name, this.m_wordBuffer);
        this.m_wordBuffer = "";
        this.m_name = null;
    }

}
