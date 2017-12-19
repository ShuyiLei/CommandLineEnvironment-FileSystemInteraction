package cs131.pa1.filter.concurrent;

import cs131.pa1.filter.Message;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

public class ConcurrentREPL {

	static String currentWorkingDirectory;
	
	public static void main(String[] args){
		currentWorkingDirectory = System.getProperty("user.dir");
		Scanner s = new Scanner(System.in);
		System.out.print(Message.WELCOME);
		String command;
		LinkedBlockingQueue<String> running_commands = new LinkedBlockingQueue<>();
		while(true) {
			//obtaining the command from the user
			System.out.print(Message.NEWCOMMAND);
			command = s.nextLine();
			if(command.equals("exit")) {
				break;
			} 
			else if(command.equals("repl_jobs")) {
				int i = 1;
				synchronized (running_commands) {
					for(String cmd:running_commands) {
						System.out.println("\t" + i + ". " + cmd );
						++i;
					}
				}

			}
			else if(command.trim().endsWith("&")) {
				String trimedCommand = command.trim();
				String realCommand = trimedCommand.substring(0, trimedCommand.lastIndexOf('&')).trim();
				if(realCommand != null) {
					Thread thread = null;
					synchronized (running_commands) {
						ConcurrentFilter filterlist = ConcurrentCommandBuilder.createFiltersFromCommand(realCommand);		
						while(filterlist != null) {
							//filterlist.process();
							thread = new Thread(filterlist);
							thread.run();
							filterlist = (ConcurrentFilter) filterlist.getNext();
						}
					}
					if(thread != null) {
						synchronized (running_commands) {
							running_commands.add(trimedCommand);
						}
						final Thread lastthread = thread;
						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									lastthread.join();
									synchronized (running_commands) {
									}
									Thread.sleep(1);
									synchronized (running_commands) {
										running_commands.remove(trimedCommand);
									}
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
						}).start();
					}
				}
			}
			else if(!command.trim().equals("")) {
				//building the filters list from the command
				ConcurrentFilter filterlist = ConcurrentCommandBuilder.createFiltersFromCommand(command);
				Thread thread = null;
				while(filterlist != null) {
					//filterlist.process();
					thread = new Thread(filterlist);
					thread.run();
					filterlist = (ConcurrentFilter) filterlist.getNext();
				}
				try {
					if(thread != null)
						thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		s.close();
		System.out.print(Message.GOODBYE);
	}

}
