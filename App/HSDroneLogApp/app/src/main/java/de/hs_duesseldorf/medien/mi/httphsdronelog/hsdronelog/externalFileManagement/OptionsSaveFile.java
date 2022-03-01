package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.externalFileManagement;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

/**
 * The OptionsSaveFile class provides access to the Options file.
 * It can read write data from and to the options file.
 */
public class OptionsSaveFile implements Serializable
{
    //INSTANCE VARIABLES//////////////////////////////////////////////////////////////////////////

    /**The application context to get the files dir path.*/
    private transient Context m_applicationContext;
    /**The file object of the save file*/
    private transient File m_saveFile;
    /**The stream which reads the {@link #m_saveFile}*/
    private transient BufferedReader m_fileReader;
    /**The stream which writes the {@link #m_saveFile}*/
    private transient BufferedWriter m_fileWriter;

    //Statements////////////////////

    /**The url to the web server. This is one of the statements read from the save file.*/
    private String m_webServerURL;
    /**The saved name for login. This is one of the statements read from the save file.*/
    private String m_loginName;
    /**Saves if the saving dialog for login name should be shown or not. This is one of
     * the statements read from the save file.*/
    private boolean m_showSavingDialog;

    //CONSTRUCTORS////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new OptionsSaveFile instance. This instance is used to read and write
     * data to the options file.
     */
    public OptionsSaveFile(Context applicationContext)
    {
        this.initializeObjects(applicationContext);

        this.m_webServerURL = null;
        this.m_loginName = null;
        this.m_showSavingDialog = true;

        //check if file exists otherwise set default values.
        if (this.m_saveFile.exists())
            this.readOptionFile();

        //check if all statements were set
        if (this.m_webServerURL == null)
            this.m_webServerURL = "https://dronelog.mi.medien.hs-duesseldorf.de";
        if (this.m_loginName == null)
            this.m_loginName = "";
    }

    /**
     * Initializes the necesarry objects for reading and saving data.
     * Call this method after you pass this object via Intend to another activity.
     * @param applicationContext The application Context of the app.
     */
    public void initializeObjects(Context applicationContext)
    {
        this.m_applicationContext = applicationContext;
        this.m_saveFile = new File(this.m_applicationContext.getFilesDir().getPath() + "/options.save");
        this.m_fileReader = null;
        this.m_fileWriter = null;
    }

    //PUBLIC METHODS//////////////////////////////////////////////////////////////////////////////

    /**
     * Gets the saved web server url from options file.
     * @return The saved web server url.
     */
    public String webServerURL()
    {
        return this.m_webServerURL;
    }

    /**
     * Gets the saved login name from options file.
     * @return The login name which was saved.
     */
    public String loginName()
    {
        return this.m_loginName;
    }

    /**
     * Gets the value for showing the saving dialog for the user name.
     * @return True if the saving dialog should be shown.
     */
    public boolean isShowSavingDialog()
    {
        return this.m_showSavingDialog;
    }

    /**
     * Sets the web server url. This value will be saved in the save file for the options.
     * @param webServerURL The url of the web server to connect to.
     */
    public void setWebServerURL(String webServerURL)
    {
        this.m_webServerURL = webServerURL;
        this.saveOptionFile();
    }

    /**
     * Sets the login name. This value will be saved in the save file for the options.
     * @param loginName The login name to be saved.
     */
    public void setLoginName(String loginName)
    {
        this.m_loginName = loginName;
        this.saveOptionFile();
    }

    /**
     * Sets the value if the saving dialog for username should be shown.
     * This value will be saved in the save file for the options.
     * @param showSavingDialog true if the saving dialog shoul be shown.
     */
    public void setShowSavingDialog(boolean showSavingDialog)
    {
        this.m_showSavingDialog = showSavingDialog;
        this.saveOptionFile();
    }

    //PRIVATE METHODS/////////////////////////////////////////////////////////////////////////////

    /**
     * Reads the data from the {@link #m_saveFile}.
     */
    private void readOptionFile()
    {
        try
        {
            this.m_fileReader = new BufferedReader(new FileReader(this.m_saveFile));
            String line;
            String[] splittedLine;

            //read each line of save file
            while ((line  = this.m_fileReader.readLine()) != null)
            {
                //split for :
                splittedLine = line.split(":::");

                if (splittedLine.length > 0)
                {
                    //check which signal word
                    switch (splittedLine[0])
                    {
                        case "webServerURL":
                            if (splittedLine.length > 1)
                                this.m_webServerURL = splittedLine[1];
                            break;
                        case "loginName":
                            if (splittedLine.length > 1)
                                this.m_loginName = splittedLine[1];
                            break;
                        case "showSavingDialog":
                            if (splittedLine.length > 1)
                                this.m_showSavingDialog = Boolean.parseBoolean(splittedLine[1]);
                            break;
                    }
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Saves the values currently set to the {@link #m_saveFile}.
     */
    private void saveOptionFile()
    {
        try
        {
            this.m_fileWriter = new BufferedWriter(new FileWriter(this.m_saveFile));

            this.m_fileWriter.write("webServerURL:::" + this.m_webServerURL);
            this.m_fileWriter.newLine();
            this.m_fileWriter.write("loginName:::" + this.m_loginName);
            this.m_fileWriter.newLine();
            this.m_fileWriter.write("showSavingDialog:::" + this.m_showSavingDialog);
            this.m_fileWriter.newLine();

            this.m_fileWriter.flush();
            this.m_fileWriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
