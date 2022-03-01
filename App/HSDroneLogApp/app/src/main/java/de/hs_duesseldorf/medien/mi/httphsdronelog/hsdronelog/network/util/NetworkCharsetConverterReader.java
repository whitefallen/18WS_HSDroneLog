package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.util;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The NetworkCharsetConverterReader class is a filter for character streams.
 * It converts encoded escape characters read from the stream to its decoded character.
 */
public class NetworkCharsetConverterReader extends FilterReader
{
    /**A buffer to save characters read. For escape character detection you have to read more than one
     * character at the same time. The characters are saved in case that there was no escape char.*/
    private Integer[] m_charBuffer;
    /**The current index for the {@link #m_charBuffer}. This pointer points onto the current buffer position
     * so you can get all characters placed in the buffer in the right order.*/
    private int m_bufferPointer;

    /**
     * Creates a new NetworkCharsetConverterReader instance. Use this instance to convert
     * encoded escape chars to decoded escape chars.
     * @param in Reader object providing the underlying stream.
     */
    public NetworkCharsetConverterReader(Reader in)
    {
        super(in);
        this.m_charBuffer = new Integer[6];
        this.m_bufferPointer = 0;
    }

    /**
     * Reads a single character. Converts escape characters into a charset the app can handle.
     * @return The character read, as an integer in the range 0 to 65535 (0x00-0xffff), or -1 if the end of the stream has been reached
     * @throws IOException If an I/O error occurs
     */
    public int read() throws IOException
    {

        //if a possible unicode was no unicode => return values from buffer
        if(this.m_charBuffer[this.m_bufferPointer] != null)
        {
            //return next value from buffer
            return this.nextValueFromBuffer();
        }
        //read next char from stream and check for unicode
        else
        {
            //return the next value from reader
            return this.nextValueFromReader();
        }
    }

    /**
     * Reads characters into a portion of an array. Converts escape characters into a charset the app can handle.
     * @param cbuf Destination buffer
     * @param off Offset at which to start storing characters
     * @param len Maximum number of characters to read
     * @return The number of characters read, or -1 if the end of the stream has been reached
     * @throws IOException If an I/O error occurs
     */
    public int read(char[] cbuf, int off, int len) throws IOException
    {
        int charactersRead = 0;
        int currentChar;

        while (charactersRead < len && (currentChar = this.read()) != -1)
        {
            cbuf[off + charactersRead] = (char)(currentChar);
            charactersRead++;
        }

        return charactersRead;
    }

    /**
     * Reads the next char from the in Reader. If the coming char sequence is a unicode encoded char,
     * then it reads the whole char sequence and converts it into the decoded char.
     * To detect an unicode encoded char more than one char has to be read to check the signal char sequence "\\u".
     * So if a '\\' is read then the {@link #m_charBuffer} will be filled with the next characters.
     * @return The next character to be returned
     * @throws IOException If an I/o Exception occurs
     */
    private int nextValueFromReader() throws IOException {
        //read next character from reader
        int result = in.read();

        //check if current char is \ => possible unicode
        if (result == '\\')
        {
            this.fillSignalCharsIntoBuffer(result);

            //check if characters read are unicode
            if ( this.isUnicode() )
            {
                try
                {
                    //get the values for the unicode decryption
                    this.fillUnicodeValuesIntoBuffer();

                    //parse unicode chars into decoded char
                    result = Integer.parseInt(
                            IntStream.range(2, 6)
                                    .parallel()
                                    .map(i -> this.m_charBuffer[i])
                                    .mapToObj(c -> Character.toString((char) c))
                                    .collect(Collectors.joining())
                            , 16
                    );

                    this.clearBuffer();
                } catch (IOException e) {}
            }
            //check if first characters read are an escape char
            else if (this.isEscapeChar())
            {
                this.m_bufferPointer++;

                result = this.nextValueFromBuffer();
            }
            else
            {
                result = this.nextValueFromBuffer();
            }
        }

        return result;
    }

    /**
     * Gets the values for a unicode encoded char. So the method will read the next 4 chars from in
     * saves them in indexes 2 - 5
     * @throws IOException If the end of stream is reached and less than 4 chars were read.
     */
    private void fillUnicodeValuesIntoBuffer() throws IOException {
        for ( int i = 2 ; i < 6 ; i++)
        {
            this.m_charBuffer[i] = this.in.read();

            if (this.m_charBuffer[i] == -1)
            {
                this.m_charBuffer[i] = null;
                throw new IOException("Reached end of stream");
            }
        }
    }

    /**
     * Fills the {@link #m_charBuffer} with the next two characters.
     * @param firstValue The character read already to add.
     * @throws IOException If an I/O Exception occures.
     */
    private void fillSignalCharsIntoBuffer(int firstValue) throws IOException
    {
        //Add value to buffer index 0
        this.m_charBuffer[0] = firstValue;
        //get next char
        this.m_charBuffer[1] = this.in.read();
    }

    /**
     * Gets the next value from the buffer to be returned
     * @return The next value from the buffer.
     */
    private int nextValueFromBuffer()
    {
        //get value to return and set buffer value to null
        int result = this.m_charBuffer[this.m_bufferPointer];
        this.m_charBuffer[this.m_bufferPointer] = null;

        //Log.d("Pointer", this.m_bufferPointer + " " + (char)(result));

        //increment buffer pointer
        this.m_bufferPointer++;

        //if buffer pointer is higher than buffers length then set pointer to standard value
        if (this.m_bufferPointer >= this.m_charBuffer.length || this.m_charBuffer[this.m_bufferPointer] == null)
        {
            this.m_bufferPointer = 0;
            this.clearBuffer();
        }

        //return value
        return result;
    }

    /**
     * Clears the {@link #m_charBuffer}. The values are set to null.
     */
    private void clearBuffer()
    {
        for (int i = 0 ; i < this.m_charBuffer.length ; i++ )
            this.m_charBuffer[i] = null;
    }

    /**
     * Checks if the current values in the buffer are a unicode formatted char.
     * @return true if the current first two chars indicates a unicode encoded char.
     */
    private boolean isUnicode()
    {
        //check if signal chars for unicode exists
        return this.m_charBuffer[0] == '\\' && this.m_charBuffer[1] == 'u';
    }

    /**
     * Checks if the first two chars read are an escape char (char pairs like \/ or \\)
     * @return true if the chars are a escape char
     */
    private boolean isEscapeChar()
    {
        return this.m_charBuffer[0] == '\\' && (this.m_charBuffer[1] == '\\' || this.m_charBuffer[1] == '/');
    }
}
