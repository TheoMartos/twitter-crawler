/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui représente les connection avec les indexeurs et qui permet de gérer les flux associés.
*/

package analyser;

import java.io.*;
import java.net.*;
import common.*;

class ConnectionAnalyser extends Connection
{
    private static final int CONF_CODE = 2;
    private static int id = 0;
    private ObjectInputStream in;
    private DataContainer dataContainer;
    private ConfigurationAnalyser conf;

    public ConnectionAnalyser(Socket s, DataContainer d)
    {
        this.conf = (ConfigurationAnalyser)ConfigFactory.getConf(CONF_CODE);
        this.connection = s;
        try
        {
            this.in = new ObjectInputStream(connection.getInputStream());
        }
        catch(IOException e)
        {
            e.printStackTrace(conf.ERROR_STREAM());
        }
        this.dataContainer = d;
        this.state = false;
        setName("Client-" + id++);
    }

    @Override
    public void run()
    {
        state = true;
        while(state)
        {
            try
            {
                // On écoute pour obtenir l'objet tweet
                Tweet tweet = (Tweet)in.readObject();
                if(tweet != null)
                    dataContainer.add(tweet);
            }
            catch(EOFException e)
            {
                System.out.println(conf.ANSI_BLUE + "Connection close by " + this.getName() + conf.ANSI_RESET);
                close();
            }
            catch(SocketException e)
            {
                System.out.println(conf.ANSI_BLUE + "Connection lost with " + this.getName() + conf.ANSI_RESET);
                close();
            }
            catch(IOException e)
            {
                System.out.println(conf.ANSI_RED + "An error occured, please check the last log file." + conf.ANSI_RESET);
                e.printStackTrace(conf.ERROR_STREAM());
            }
            catch(ClassNotFoundException e)
            {
                System.out.println(conf.ANSI_RED + "An error occured, please check the last log file." + conf.ANSI_RESET);
                e.printStackTrace(conf.ERROR_STREAM());
            }
        }
    }

    public void close()
    {
        if(state)
        {
            try
            {
                in.close();
                connection.close();
            }
            catch(IOException e)
            {
                System.out.println(conf.ANSI_RED + "An error occured, please check the last log file." + conf.ANSI_RESET);
                e.printStackTrace(conf.ERROR_STREAM());
            }
            finally
            {
                state = false;
            }
        }
    }

    /**
    *   Méthode toString, pour afficher les informations de l'instance de Connection.
    */
    @Override
    public String toString()
    {
        return this.getName() + " " + connection.toString();
    }

}
