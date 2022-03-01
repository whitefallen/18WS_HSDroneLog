package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.parser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.util.NetworkRequestParser;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.util.json.JSONReader;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.util.json.JSONReaderListener;

/**
 * The Json Request Parser class can parse a json formatted string received by a network request from
 * a HSDroneLog web server. It parses the json with help of the {@link JSONReader} and saves the
 * data in a format the {@link de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.NetworkListener}
 * can deal with.
 */
public class JSONRequestParser extends NetworkRequestParser implements JSONReaderListener
{
    //CLASS VARIABLES/////////////////////////////////////////////////////////////////////////////

    private static boolean deletedImages = false;

    //INSTANCE VARIABLES//////////////////////////////////////////////////////////////////////////

    /**The application context from the app to get the path to the directory of the app on drive.*/
    private Context m_applicationContext;
    /**The JSONReader object to read the json formatted raw string and to get the single elements of
     * the json formatted string.*/
    private JSONReader m_jsonReader;
    /**This value is true if the json statement which is parsed is in the json data array*/
    private boolean m_jsonStatementsInDataArray;
    /**A map to save the parsed data of the current json object of the data array.*/
    private Map<String, String> m_currentJsonObjectMap;
    /**The counter how many arrays are currently open in the data array.*/
    private int m_jsonArrayDeepness;
    /**The name of the current json array which is read*/
    private String m_currentJsonArrayName;
    /**A buffer to safe all elements of an array which is in the data array.*/
    private String m_jsonStringBufferForElementsArray;

    //CONSTRUCTORS////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new Network Request Parser which can parse a json formatted string from the
     * HSDroneLog web server.
     * @param applicationContext The application context to get access to
     *                           the application directory path. This is used to save images
     *                           in the applications directory.
     */
    public JSONRequestParser(Context applicationContext)
    {
        this.m_applicationContext = applicationContext;

        //initialize values
        this.resetValues();

        if (!deletedImages)
        {
            this.deleteAllFiles(new File(this.m_applicationContext.getFilesDir().getPath() + "/images"));
            deletedImages = true;
        }
    }

    //PRIVATE////////////////////////////////////////////////////////////////////////////

    /**
     * Deletes all files from a directory if the directory exists.
     * @param root The root directory.
     */
    private void deleteAllFiles(File root)
    {
        if (root.exists())
        {
            File[] filesList = root.listFiles();
            for ( File current : filesList)
            {
                if (current.isDirectory())
                    this.deleteAllFiles(current);
                else
                    current.delete();
            }
        }
    }

    /**
     * Gets an image from a url and decodes it into a bitmap.
     * @param url The url of the image to get.
     * @return The created bitmap instance.
     */
    private Bitmap getBitmap(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Bitmap d = BitmapFactory.decodeStream(is);
            is.close();
            return d;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //METHODS FROM NetworkRequestParser///////////////////////////////////////////////////////////

    @Override
    protected void parse(String rawResult)
    {
        this.m_jsonReader = new JSONReader();
        m_jsonReader.parseJSON(this, rawResult);
    }

    /**
     * Resets the values of the parser sub class so the next request can use the same object.
     */
    @Override
    public void resetValues()
    {
        this.m_requestResultsList = null;
        this.m_jsonReader = null;
        this.m_jsonStatementsInDataArray = false;
        this.m_currentJsonObjectMap = null;
        this.m_jsonArrayDeepness = 0;
        this.m_currentJsonArrayName = null;
        this.m_jsonStringBufferForElementsArray = "";
    }

    //METHODS FROM JSONReaderListener/////////////////////////////////////////////////////////////

    /**
     * This method is called if a new json object begins.
     */
    @Override
    public void beginOfObject()
    {
        //Log.d("Object", "New Object");

        if (this.m_jsonStatementsInDataArray)
        {
            this.m_currentJsonObjectMap = new TreeMap<>();
        }
    }

    /**
     * This method is called if the current json object ends.
     */
    @Override
    public void endOfObject()
    {
        //Log.d("Object", "EndObject");

        if (this.m_jsonStatementsInDataArray && this.m_jsonArrayDeepness == 0)
        {
            //add current map to result list
            this.m_requestResultsList.add(this.m_currentJsonObjectMap);
            this.m_currentJsonObjectMap = null;
        }
    }

    /**
     * This method is called if a new json array begins.
     *
     * @param name The name of the array.
     */
    @Override
    public void beginOfArray(String name)
    {
        //Log.d("Array", name);

        if (name.equals("data"))
        {
            this.m_jsonStatementsInDataArray = true;
            this.m_jsonArrayDeepness = 0;
        }
        else
        {
            this.m_jsonArrayDeepness++;
            this.m_currentJsonArrayName = name;
        }
    }

    /**
     * This method is called if the current json array ends.
     */
    @Override
    public void endOfArray()
    {
        //Log.d("Array", "EndArray");

        if(this.m_jsonArrayDeepness == 0)
        {
            this.m_jsonStatementsInDataArray = false;
        }
        else
        {
            if(!this.m_jsonStringBufferForElementsArray.isEmpty())
            {
                //set new attribute with all information from the array
                this.m_currentJsonObjectMap.put(this.m_currentJsonArrayName, this.m_jsonStringBufferForElementsArray);

                //clear buffer for elements array
                this.m_jsonStringBufferForElementsArray = "";
                //clear current array name
                this.m_currentJsonArrayName = null;
            }
            else
            {
                //add current map to result list
                this.m_requestResultsList.add(this.m_currentJsonObjectMap);
                this.m_currentJsonObjectMap = null;
                this.m_currentJsonObjectMap = new TreeMap<>();
            }

            this.m_jsonArrayDeepness--;
        }
    }

    /**
     * This method is called if there is an json attribute with a value.
     * @param name  The name of the attribute.
     * @param value The value of the attribute.
     */
    @Override
    public void newAttribute(String name, String value)
    {
        //Log.d("Attribute", name + ": " + value);

        if (name != null)
        {
            if (name.equals("success"))
                this.m_requestIsSuccessful = Boolean.parseBoolean(value);

            else if (name.equals("message"))
                this.m_messageFromWebServer = value;

            else if (name.equals("session"))
                this.setNetworkThreadSession(value);

            else if (this.m_jsonStatementsInDataArray) {
                this.newAttributeInDataArray(name, value);

            }
        }
        else
        {
            if (!this.m_jsonStringBufferForElementsArray.isEmpty())
                this.m_jsonStringBufferForElementsArray += ',';

            this.m_jsonStringBufferForElementsArray += value;
        }
    }

    /**
     * The processing of an attribute which is in the data array of an request's answer.
     * @param name  The name of the attribute.
     * @param value The value of the attribute.
     */
    private void newAttributeInDataArray(String name, String value) {
        //if attribute contains image data then download the image
        if (name.equals("bild") || name.equals("profilbild"))
        {
            this.saveImageOnDrive(value);

            value = "/images" + value;
        }

        //check if a current map exists
        if (this.m_currentJsonObjectMap != null)
            this.m_currentJsonObjectMap.put(name, value);
    }

    /**
     * Gets an image and saves it in the application directory. The image is saved to:
     * application path/images/relativeImagePath
     * @param relativeImagePath The image path relative to the webServerUrl.
     */
    private void saveImageOnDrive(String relativeImagePath)
    {
        File imgFile = new File(this.m_applicationContext.getFilesDir().getPath() + "/images" + relativeImagePath);

        //check if file already exists
        if (!imgFile.exists())
        {
            Bitmap img = this.getBitmap(this.webServerURLString() + relativeImagePath);

            if (img != null)
            {

                File imgDirectory = new File(imgFile.getParent());
                imgDirectory.mkdirs();

                //save img from bitmap
                try
                {
                    FileOutputStream out = new FileOutputStream(imgFile);

                    img.compress(Bitmap.CompressFormat.JPEG, 100, out);

                    out.flush();
                    out.close();
                    imgFile.setLastModified(System.currentTimeMillis());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

                Log.d("HSDroneLog Network", "Downloaded image:" + imgFile.getAbsolutePath());
            }
        }
    }

    /**
     * This method is called if an Exception is thrown.
     *
     * @param e The Exception instance.
     */
    @Override
    public void exceptionThrown(Exception e)
    {
        this.m_exceptionOccured = e;
    }

    /**
     * This method is called at the beginning of parsing.
     */
    @Override
    public void beginOfJSON() {}

    /**
     * This method is called if the end of the json is reached.
     */
    @Override
    public void endOfJSON()
    {
        /*for (Map<String, String> entry : this.m_requestResultsList)
            Log.d("MAP OBJECT", entry.toString());*/
    }
}
