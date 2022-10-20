package generic;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import processor.Clock;
import processor.Processor;

public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	static EventQueue eventQueue;
	public static long storeresp;
	public static int inst_count;	

	public static void setupSimulation(String assemblyProgramFile, Processor p)
	{
		// ----------------------------
		eventQueue = new EventQueue();
		storeresp = 0;

		Simulator.processor = p;
		try
		{
			loadProgram(assemblyProgramFile);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		simulationComplete = false;
	}
	
	static void loadProgram(String assemblyProgramFile) throws IOException
	{
		/*
		 * TODO
		 * 1. load the program into memory according to the program layout described
		 *    in the ISA specification
		 * 2. set PC to the address of the first instruction in the main
		 * 3. set the following registers:
		 *     x0 = 0
		 *     x1 = 65535
		 *     x2 = 65535
		 */
		InputStream is = null;
		try
		{
			is = new FileInputStream(assemblyProgramFile);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		DataInputStream dis = new DataInputStream(is);

		try{
				
			int address = 0;
			int pc = dis.readInt();
			processor.getRegisterFile().setProgramCounter(pc);

			while(dis.available() > 0)
			{
				// int next = dis.readInt();
				// if(address == -1)
				// {
				// 	processor.getRegisterFile().setProgramCounter(next);
				// }
				// else
				// {
				int value = dis.readInt();
				processor.getMainMemory().setWord(address, value);
				// }
				address += 1;
			}
			
			processor.getRegisterFile().setValue(0, 0);
			processor.getRegisterFile().setValue(1, 65535);
			processor.getRegisterFile().setValue(2, 65535);
			
			//System.out.println(processor.getRegisterFile().getProgramCounter());
			//String output = processor.getMainMemory().getContentsAsString(0, 15);
			//System.out.println(output);

			dis.close(); 
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public static EventQueue getEventQueue() {
		return eventQueue;
	}
			
	public static void simulate()
	{
		while(simulationComplete == false)
		{
			processor.getRWUnit().performRW();
			processor.getMAUnit().performMA();
			processor.getEXUnit().performEX();

			eventQueue.processEvents();

			processor.getOFUnit().performOF();
			processor.getIFUnit().performIF();

			Clock.incrementClock();
			// Statistics.setNumberOfInstructions(Statistics.getNumberOfInstructions() + 1);
			Statistics.setNumberOfCycles(Statistics.getNumberOfCycles() + 1);
		}
		
		// TODO
		// set statistics
		
		//print statistics
		System.out.println("Number of Cycles: " + Statistics.getNumberOfCycles());
		System.out.println("Number of OF Stalls: " + (Statistics.getNumberOfInstructions() - Statistics.getNumberOfRegisterWriteInstructions()));
		System.out.println("Number of Wrong Branch Instructions: " + Statistics.getNumberOfBranchTaken());
	}
	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
}
