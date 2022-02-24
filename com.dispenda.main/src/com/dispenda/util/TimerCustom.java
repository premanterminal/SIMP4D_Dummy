package com.dispenda.util;

import java.util.Timer;
import java.util.TimerTask;

public class TimerCustom extends Timer {
	private Runnable task;
    private TimerTask timerTask;
    
    public void start(Runnable runnable){
    	task = runnable;
    	timerTask = new TimerTask(){
    		@Override
    		public void run() {
    			// TODO Auto-generated method stub
//    			task.run();
    		}
    	};
    }

    public void schedule(Runnable runnable, long delay) {
    	task = runnable;
    	timerTask = new TimerTask() { 
    		public void run() { 
    			task.run(); 
            }
        };
        super.schedule(timerTask, delay);        
    }

    public void reschedule(long delay) {
        System.out.println("rescheduling after seconds "+delay);
        timerTask.cancel();
        timerTask = new TimerTask() { 
        	public void run() { 
        		task.run(); 
        	}
        };
        super.schedule(timerTask, delay);        
    }
}
	/*
	
	
	private TimerTaskLogin task;
	
	public TimerCustom() {
		
	}
	
	@Override
	public void schedule(TimerTask task, long delay) {
		// TODO Auto-generated method stub
		this.task = task;
		super.schedule(this.task, delay);
	}
	
	public void reschedule(long delay){
//		super.purge();
		task = new TimerTaskLogin();
		super.schedule(task, delay);
	}
}*/
