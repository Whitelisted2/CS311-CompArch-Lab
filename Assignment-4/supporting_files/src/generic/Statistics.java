package generic;

import java.io.PrintWriter;

public class Statistics {
	
	// TODO add your statistics here
	static int numberOfInstructions;
	static int numberOfCycles;
	static float iPC;
	static float cPI;
	static int num_BranchTaken;
	static int num_RWInstructions;
	

	public static void printStatistics(String statFile)
	{
		try
		{
			PrintWriter writer = new PrintWriter(statFile);
			
			writer.println("Number of instructions executed = " + numberOfInstructions);
			writer.println("Number of cycles taken = " + numberOfCycles);
			writer.println("IPC = " + iPC);
			writer.println("CPI = " + cPI);
			System.out.println("Number of OF Stalls: " + (numberOfInstructions - num_RWInstructions));
			System.out.println("Number of Wrong Branch Instructions: " + num_BranchTaken);
			writer.close();
		}
		catch(Exception e)
		{
			Misc.printErrorAndExit(e.getMessage());
		}
	}
	 
	// TODO write functions to update statistics
	public static void setNumberOfInstructions(int numberOfInstructions) {
		Statistics.numberOfInstructions = numberOfInstructions;
	}

	public static void setNumberOfCycles(int numberOfCycles) {
		Statistics.numberOfCycles = numberOfCycles;
	}

	public static int getNumberOfInstructions() {
		return numberOfInstructions;
	}

	public static int getNumberOfCycles() {
		return numberOfCycles;
	}

	public static void setIPC(int numinst, int numcycles) {
		iPC = (float)numinst / (float)numcycles;
	}

	public static void setCPI(int numinst, int numcycles) {
		cPI = (float)numcycles / (float)numinst;
	}

	public static float getIPC() {
		return iPC;
	}

	public static float getCPI() {
		return cPI;
	}
	public static void setNumberOfOFInstructions(int n) {
		Statistics.numberOfInstructions = n;
	}
	
	public static int getNumberOfOFInstructions() {
		return numberOfInstructions;
	}
	
	public static void setNumberOfBranchTaken(int n) {
		Statistics.num_BranchTaken = n;
	}
	
	public static int getNumberOfBranchTaken() {
		return num_BranchTaken;
	}
	
	public static void setnumberOfRWInstructions(int n) {
		Statistics.num_RWInstructions = n;
	}
	
	public static int getNumberOfRWInstructions() {
		return num_RWInstructions;
	}
}
