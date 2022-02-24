package com.dispenda.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Simple demo that uses java.util.Timer to schedule a task to execute
 * every 5 seconds and have a delay if you give any input in console.
 */

public class DateThreadScheduler extends Thread {  
    Timer timer;
    BufferedReader br ;
    String data = null;
    Date dNow ;
    SimpleDateFormat ft;

    public DateThreadScheduler() {

        timer = new Timer();
        timer.schedule(new RemindTask(), 0, 5*1000); 
        br = new BufferedReader(new InputStreamReader(System.in));
        start();
    }

    public void run(){

        while(true){
            try {
                data =br.readLine();
                if(data != null && !data.trim().equals("") ){
                    timer.cancel();
                    timer = new Timer();
                    dNow = new Date( );
                    ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
                    System.out.println("Modified Current Date ------> " + ft.format(dNow));
                    timer.schedule(new RemindTask(), 5*1000 , 5*1000);
                }

            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String args[]) {
        System.out.format("Printint the time and date was started...\n");
        new DateThreadScheduler();
    }
}

class RemindTask extends TimerTask {
    Date dNow ;
    SimpleDateFormat ft;

    public void run() {

        dNow = new Date();
        ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
        System.out.println("Current Date: " + ft.format(dNow));
    }
}
