package generic;

import java.io.*;
import java.nio.ByteBuffer;
import generic.Operand.OperandType;
import java.util.HashMap;
import java.util.Map;


public class Simulator {

	public static Map<String,String> opTable = new HashMap<>(){{
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

	}};
		
	static FileInputStream inputcodeStream = null;
	
	public static void setupSimulation(String assemblyProgramFile)
	{	
		int firstCodeAddress = ParsedProgram.parseDataSection(assemblyProgramFile);
		ParsedProgram.parseCodeSection(assemblyProgramFile, firstCodeAddress);
		ParsedProgram.printState();
	}
	

	public static void assemble(String objectProgramFile)
	{
		FileOutputStream weno;
		try{
			//1. open the objectProgramFile in binary mode
			weno = new FileOutputStream(objectProgramFile);
			BufferedOutputStream mech = new BufferedOutputStream(weno);

			//2. write the firstCodeAddress to the file
			byte[] codeAddress = ByteBuffer.allocate(4).putInt(ParsedProgram.firstCodeAddress).array();
			mech.write(codeAddress);

			//3. write the data to the file
			for(int i=0; i<ParsedProgram.data.size(); i++){
				byte[] lettuce = ByteBuffer.allocate(4).putInt(ParsedProgram.data.get(i)).array();
				mech.write(lettuce);
			}

			//4. assemble one instruction at a time, and write to the file
			for(int i=0; i<ParsedProgram.code.size(); i++){
				String bisaun = "";
				String opType = ParsedProgram.code.get(i).toString();
				if(opType.equals("jmp")){
					bisaun += (opTable.get(ParsedProgram.code.get(i).operationType.toString()));
					bisaun += "00000";
					int destAddr = 0;
					if(ParsedProgram.code.get(i).destinationOperand.toString().equals("Label")){
						destAddr = ParsedProgram.symtab.get(ParsedProgram.code.get(i).destinationOperand.labelValue);
					}
					else if(ParsedProgram.code.get(i).destinationOperand.toString().equals("Immediate")){
						destAddr = ParsedProgram.code.get(i).destinationOperand.value;
					}
					int pc = ParsedProgram.code.get(i).programCounter;
					int offset = destAddr - pc;
					if(offset>=0){
						String binRep = Integer.toBinaryString(offset);
						int binOffset = 22- binRep.length();
						String concata = "";
						if(binOffset!=0){
							for(int j=0;j<binOffset;j++){
								concata.concat("0");
							}
						}
						bisaun.concat(concata);
						bisaun.concat(binRep);
					}
					if(offset<0)
					{
						String binRep = Integer.toBinaryString(offset);
						String subRep = binRep.substring(10, 32);
						bisaun.concat(subRep);
					}
				}
				else if(opType.equals("load")||opType.equals("store")){

				}
				else if(opType.equals("add") || opType.equals("sub") || opType.equals("mul") || opType.equals("div") || opType.equals("and") || opType.equals("or") || opType.equals("xor") || opType.equals("slt") || opType.equals("sll") || opType.equals("srl") || opType.equals("sra")){

				}
				else if(opType.equals("addi") || opType.equals("subi") || opType.equals("muli") || opType.equals("divi") || opType.equals("andi") || opType.equals("ori") || opType.equals("xori") || opType.equals("slti") || opType.equals("slli") || opType.equals("srli") || opType.equals("srai")){

				}
				else if(opType.equals("beq") || opType.equals("bgt") || opType.equals("bne") || opType.equals("blt")){

				}
				else if(opType.equals("end")){
					
				}
			}
			




		} catch(FileNotFoundException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}

		//5. close the file
	}
	



	public static Map<String, String> getOpTable() {
		return opTable;
	}

	public static void setOpTable(Map<String, String> opTable) {
		Simulator.opTable = opTable;
	}}