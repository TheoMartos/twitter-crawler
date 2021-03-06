/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui contient les constantes du programme.
*/

package common;

import java.io.*;

public class ConfigurationIndexor extends Configuration
{
    public final String HOSTNAME_CRAWLER;
    public final int PORT_CRAWLER;
    public final String HOSTNAME_ANALYSER;
    public final int PORT_ANALYSER;
    public final int THREAD_NUMBER;

    public ConfigurationIndexor()
    {
        this.HOSTNAME_CRAWLER = "localhost";
        this.PORT_CRAWLER = 2200;
        this.HOSTNAME_ANALYSER = "localhost";
        this.PORT_ANALYSER = 2201;
        this.THREAD_NUMBER = 1;
        if(ERROR_STREAM == null)
        {
            try
            {
                File f = new File("../error_log_indexor.txt");
                int i = 1;
                while(f.exists())
                {
                    f = new File("../error_log_indexor_" + i++ + ".txt");
                }
                System.out.println("Error filename : " + f.getName());
                ERROR_STREAM = new PrintStream(f);
            }
            catch(IOException e)
            {
                e.printStackTrace();
                System.out.println(ANSI_RED + "Error, error_stream redirected to console." + ANSI_RESET);
                ERROR_STREAM = System.err;
            }
        }
    }

    public PrintStream ERROR_STREAM()
    {
        return ERROR_STREAM;
    }

    @Override
    public String toString()
    {
        return "Crawler hostname : " + HOSTNAME_CRAWLER + "\n\tCarwler port : " + PORT_CRAWLER + "\n\tAnalyser hostname : " + HOSTNAME_ANALYSER + "\n\tAnalyser port : " + PORT_ANALYSER;
    }
}
