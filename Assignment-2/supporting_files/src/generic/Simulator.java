package generic;

import java.io.FileInputStream;
import generic.Operand.OperandType;

public class Simulator {

	public static Map<String,String> opTable = new HashMap<>(){
		put("add", "00000");
		put("sub", "00010");
		put("mul", "00100");
		put("div", "00110");
		put("and", "01000");
		put("or", "01010");
		put("xor", "01100");
		put("slt", "01110");
		put("sll", "10000");
		put("srl", "10010");
		put("sra", "10100");
		put("addi", "00001");
		put("subi", "00011");
		put("muli", "00101");
		put("divi", "00111");
		put("andi", "01001");
		put("ori", "01011");
		put("xori", "01101");
		put("slti", "01111");
		put("slli", "10001");
		put("srli", "10011");
		put("srai", "10101");
		put("load", "10110");
		put("store", "10111");
		put("beq", "11001");
		put("bne", "11010");
		put("blt", "11011");
		put("bgt", "11100");
		put("jmp", "11000");

	}
		
	static FileInputStream inputcodeStream = null;
	
	public static void setupSimulation(String assemblyProgramFile)
	{	
		int firstCodeAddress = ParsedProgram.parseDataSection(assemblyProgramFile);
		ParsedProgram.parseCodeSection(assemblyProgramFile, firstCodeAddress);
		ParsedProgram.printState();
	}
	
	public static void assemble(String objectProgramFile)
	{
		//TODO your assembler code
		//1. open the objectProgramFile in binary mode
		//2. write the firstCodeAddress to the file
		//3. write the data to the file
		//4. assemble one instruction at a time, and write to the file
		//5. close the file
	}
	
}


	public static Map<String, String> getOpTable() {
		return opTable;
	}

	public static void setOpTable(Map<String, String> opTable) {
		Simulator.opTable = opTable;
	}