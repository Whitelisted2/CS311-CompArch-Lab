package generic;

import java.io.PrintWriter;

public class Statistics {
	
	// TODO add your statistics here
	static int numberOfInstructions;
	static int numberOfOFStageInstructions;
	static int numberOfCycles;
	static int numberOfBranchTaken;
	static int numberOfRegisterWriteInstructions;
	static float IPC, CPI;

	public static void printStatistics(String statFile)
	{
		try
		{
			PrintWriter writer = new PrintWriter(statFile);
			
			writer.println("Number of instructions executed = " + numberOfInstructions);
			writer.println("Number of cycles taken = " + numberOfCycles);
			
			writer.close();
		}
		catch(Exception e)
		{
			Misc.printErrorAndExit(e.getMessage());
		}
	}
	
	// set instr, cycles
	public static void setNumberOfInstructions(int numberOfInstructions) {
		Statistics.numberOfInstructions = numberOfInstructions;
	}

	public static void setNumberOfCycles(int numberOfCycles) {
		Statistics.numberOfCycles = numberOfCycles;
	}

	// get instr, cycles
	public static int getNumberOfInstructions() {
		return numberOfInstructions;
	}

	public static int getNumberOfCycles() {
		return numberOfCycles;
	}
	
	// OF instrs
	public static void setNumberOfOFInstructions(int numberOfOFStageInstructions) {
		Statistics.numberOfOFStageInstructions = numberOfOFStageInstructions;
	}
	
	public static int getNumberOfOFInstructions() {
		return numberOfOFStageInstructions;
	}
	
	// branch taken
	public static void setNumberOfBranchTaken(int numberOfBranchTaken) {
		Statistics.numberOfBranchTaken = numberOfBranchTaken;
	}
	
	public static int getNumberOfBranchTaken() {
		return numberOfBranchTaken;
	}
	
	// rw instrs
	public static void setnumberOfRegisterWriteInstructions(int numberOfRegisterWriteInstructions) {
		Statistics.numberOfRegisterWriteInstructions = numberOfRegisterWriteInstructions;
	}
	
	public static int getNumberOfRegisterWriteInstructions() {
		return numberOfRegisterWriteInstructions;
	}

	// IPC, CPI
	public static void setIPC() {
		Statistics.IPC = (float)numberOfInstructions/(float)numberOfCycles;
		Statistics.CPI = (float)numberOfCycles/(float)numberOfInstructions;
	}
}
