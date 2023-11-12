package com.mycompany.myddns;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class MyDDNS {

    static String domain = "djelenic";
    static String token = "28830c35-5da3-4203-890f-dbdf596c1c64";
    static String routerIP;
    static Logger logger;

    public static void main(String[] args) throws IOException {

        MyFormatter myFormatter = new MyFormatter();
        //FileHandler fileHandler = new FileHandler("my-%g.log",10*1024*1024,10);
        FileHandler fileHandler = new FileHandler("log-%g.log",0,31);
        fileHandler.setFormatter(myFormatter);

//        SimpleFormatter simpleFormatter = new SimpleFormatter();  
//        FileHandler simpleHandler = new FileHandler("simple.log");
//        simpleHandler.setFormatter(simpleFormatter);
//
//        XMLFormatter xmlFormatter = new XMLFormatter();  
//        FileHandler xmlHandler = new FileHandler("xml.log");
//        xmlHandler.setFormatter(xmlFormatter);

        logger = Logger.getLogger(MyDDNS.class.getName());
        logger.addHandler(fileHandler);
//        logger.addHandler(simpleHandler);
//        logger.addHandler(xmlHandler);
        
        
        ScheduledExecutorService es = Executors.newSingleThreadScheduledExecutor();
        es.scheduleAtFixedRate(Updater::checkAndUpdate, 0, 1, TimeUnit.MINUTES);
        
        logger.info("working... ");
        

    }

}
