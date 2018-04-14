/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe Principale du module d'analyse
**/

package analyser;

import common.*;
import java.io.*;
import java.util.*;

public class Analyser extends Thread
{
    private static final int CONF_CODE = 2;
    private ConfigurationAnalyser conf;
    private AnalyserServer server;
    private DataContainer dataContainer;
    private HITS hits;
    private Scanner sc;
    private boolean state;

    public Analyser()
    {
        this.conf = (ConfigurationAnalyser)ConfigFactory.getConf(CONF_CODE);
        this.dataContainer = new DataContainer();
        this.server = new AnalyserServer(dataContainer);
        this.hits = new HITS(dataContainer, 10);
        this.sc = new Scanner(System.in);
        this.state = false;
    }

    public void run()
    {
        state = true;
        server.start();

        while(state)
        {
            System.out.print("> ");
            String q = sc.nextLine().toLowerCase();
            switch(q)
            {
                case "start":
                {
                    hits.start();
                    System.out.println("HITS started");
                    break;
                }
                case "stop":
                {
                    state = false;
                    break;
                }
                default:
                {
                    System.out.println("incorrect");
                }
            }
        }
        //
        // try
        // {
        //     server.join();
        // }
        // catch(InterruptedException e)
        // {
        //     e.printStackTrace(conf.ERROR_STREAM());
        // }
    }

}
