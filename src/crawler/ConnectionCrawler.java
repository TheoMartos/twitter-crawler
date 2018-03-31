/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui représente les connection avec les indexeurs et qui permet de gérer les flux associés.
*/

package crawler;

import java.io.*;
import java.net.*;
import common.Connection;

class ConnectionCrawler extends Connection
{
    private static final int CONF_CODE = 0;
    private BufferedReader in;
    private PrintWriter out;
    private Garbage tweets;

    private static int id = 0;

    /**
    *   Constructeur de la classe qui initialise le sockect, les flux réseaux et le garbage.
    *   @param s    Le socket issu de la connexion avec le sokect serveur.
    *   @param g    Le gargabe qui stocke les tweets
    */
    public ConnectionCrawler(Socket s, Garbage g)
    {
        this.connection = s;
        this.tweets = g;
        this.setName("Client-" + id++);
        try
        {
            this.in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(connection.getOutputStream())), true);
        }
        catch(IOException e)
        {
            e.printStackTrace(System.err);
        }
        this.state = false;
    }

    /**
    *   Le thread écoute en permance l'indexeur et lui envoie le prochain tweet à traiter.
    *   Stop le thread s'il reçoit STOP.
    */
    @Override
    public void run()
    {
        this.state = true;
        try
        {
            String query = "";
            while(state)
            {
                query = in.readLine();
                if (query == null)
                {
                    System.out.println("Connection close by " + this.getName());
                    break;
                }
                switch(query)
                {
                    case "NEXT":
                    {
                        out.println(tweets.getNextElement());
                        break;
                    }
                    case "STOP":
                    {
                        state = false;
                        break;
                    }
                }
            }
        }
        catch(IOException e)
        {
            if(state)
                e.printStackTrace(System.err);
        }
        finally
        {
            close();
        }
    }

    /**
    *   Méthode pour fermer tous les flux et le socket pour mettre un terme à la connexion.
    */
    @Override
    public void close()
    {
        try
        {
            in.close();
            out.close();
            if(!connection.isClosed())
                connection.close();
        }
        catch(IOException e)
        {
            e.printStackTrace(System.err);
        }
        finally
        {
            state = false;
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
