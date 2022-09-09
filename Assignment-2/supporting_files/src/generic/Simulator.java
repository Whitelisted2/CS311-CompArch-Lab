package generic;

import java.io.*;
import java.nio.ByteBuffer;
import generic.Operand.OperandType;
import java.util.HashMap;


public class Simulator {

	public static HashMap<String,String> opTable = new HashMap<String, String>(){{
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
			// System.out.println(opTable.get("load"));
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
				String bisaun = ""; // line to append to file
				String opType = ParsedProgram.code.get(i).getOperationType().toString();
				// System.out.println(opType);
				if(opType.equals("jmp")){
					// RI type
					bisaun += (opTable.get("jmp")); // opcode of jmp
					bisaun += "00000"; // no register in jmp 
					int destAddr = 0;
					if(ParsedProgram.code.get(i).destinationOperand.operandType == OperandType.Label){ 
						destAddr = ParsedProgram.symtab.get(ParsedProgram.code.get(i).destinationOperand.labelValue);
					}
					else if(ParsedProgram.code.get(i).destinationOperand.operandType == OperandType.Immediate){
						destAddr = ParsedProgram.code.get(i).destinationOperand.value;
					}
					int pc = ParsedProgram.code.get(i).programCounter;
					int offset = destAddr - pc;
					System.out.println("pc= "+pc);
					System.out.println("offset= "+offset);
					if(offset>=0){
						String binRep = Integer.toBinaryString(offset);
						int binOffset = 22 - binRep.length();
						String concata = "";
						if(binOffset!=0){
							for(int j=0;j<binOffset;j++){
								concata = concata.concat("0");
							}
						}
						bisaun = bisaun.concat(concata);
						bisaun = bisaun.concat(binRep);
					} else {
						String binRep = Integer.toBinaryString(offset);
						String subRep = binRep.substring(10, 32);
						bisaun = bisaun.concat(subRep);
					}
				}
				else if(opType.equals("load")||opType.equals("store")){
					bisaun += (opTable.get(opType));
					// System.out.println("ttttt");
					// System.out.println(bisaun);
					// System.out.println(ParsedProgram.code.get(i).sourceOperand1.operandType.toString());
					if(ParsedProgram.code.get(i).sourceOperand1.operandType.toString().equals("Register")){
						bisaun = bisaun.concat(String.format("%5s", Integer.toBinaryString(ParsedProgram.code.get(i).sourceOperand1.value)).replace(' ', '0'));
						// System.out.println("ttt"+bisaun+"www");
					}
					if(ParsedProgram.code.get(i).destinationOperand.operandType.toString().equals("Register")){
						bisaun = bisaun.concat(String.format("%5s", Integer.toBinaryString(ParsedProgram.code.get(i).destinationOperand.value)).replace(' ', '0'));
					}
					if(ParsedProgram.code.get(i).sourceOperand2.operandType.toString().equals("Label")){
						String labeVal = String.format("%5s", Integer.toBinaryString(ParsedProgram.symtab.get(ParsedProgram.code.get(i).sourceOperand2.labelValue))).replace(' ', '0');
						int labeSize = labeVal.length();
						String labeConcat = "";
						if(labeSize != 17){
							int toBe = 17 - labeSize;
							for(int j=0; j<toBe; j++)
								labeConcat += "0";
							labeConcat = labeConcat.concat(labeVal);
						}
						bisaun = bisaun.concat(labeConcat);
					}
					if(ParsedProgram.code.get(i).sourceOperand2.operandType.toString().equals("Immediate")){
						String labeVal = Integer.toBinaryString(ParsedProgram.code.get(i).sourceOperand2.value);
						int labeSize = labeVal.length();
						String labeConcat = "";
						if(labeSize != 17){
							int toBe = 17 - labeSize;
							for(int j=0; j<toBe; j++)
								labeConcat += "0";
							labeConcat = labeConcat.concat(labeVal);
						}
						bisaun = bisaun.concat(labeConcat);	
					}

				}
				else if(opType.equals("add") || opType.equals("sub") || opType.equals("mul") || opType.equals("div") || opType.equals("and") || opType.equals("or") || opType.equals("xor") || opType.equals("slt") || opType.equals("sll") || opType.equals("srl") || opType.equals("sra")){
					bisaun += (opTable.get(opType));
					if(ParsedProgram.code.get(i).sourceOperand1.operandType.toString().equals("Register")){
						bisaun = bisaun.concat(String.format("%5s", Integer.toBinaryString(ParsedProgram.code.get(i).sourceOperand1.value)).replace(' ', '0'));
					}
					if(ParsedProgram.code.get(i).sourceOperand2.operandType.toString().equals("Register")){
						bisaun = bisaun.concat(String.format("%5s", Integer.toBinaryString(ParsedProgram.code.get(i).sourceOperand2.value)).replace(' ', '0'));
					}
					if(ParsedProgram.code.get(i).destinationOperand.operandType.toString().equals("Register")){
						bisaun = bisaun.concat(String.format("%5s", Integer.toBinaryString(ParsedProgram.code.get(i).destinationOperand.value)).replace(' ', '0'));
					}
					bisaun = bisaun.concat("000000000000");
				}
				else if(opType.equals("addi") || opType.equals("subi") || opType.equals("muli") || opType.equals("divi") || opType.equals("andi") || opType.equals("ori") || opType.equals("xori") || opType.equals("slti") || opType.equals("slli") || opType.equals("srli") || opType.equals("srai")){
					bisaun += (opTable.get(opType));
					if(ParsedProgram.code.get(i).sourceOperand1.operandType.toString().equals("Register")){
						bisaun = bisaun.concat(String.format("%5s", Integer.toBinaryString(ParsedProgram.code.get(i).sourceOperand1.value)).replace(' ', '0'));
					}
					// if(ParsedProgram.code.get(i).sourceOperand2.operandType.toString().equals("Register")){
					// 	bisaun = bisaun.concat(String.format("%5s", Integer.toBinaryString(ParsedProgram.code.get(i).sourceOperand2.value)).replace(' ', '0'));
					// }
					if(ParsedProgram.code.get(i).destinationOperand.operandType.toString().equals("Register")){
						bisaun = bisaun.concat(String.format("%5s", Integer.toBinaryString(ParsedProgram.code.get(i).destinationOperand.value)).replace(' ', '0'));
					}
					if(ParsedProgram.code.get(i).sourceOperand2.operandType.toString().equals("Immediate")){
						String labeVal = Integer.toBinaryString(ParsedProgram.code.get(i).sourceOperand2.value);
						int labeSize = labeVal.length();
						String labeConcat = "";
						if(labeSize != 17){
							int toBe = 17 - labeSize;
							for(int j=0; j<toBe; j++)
								labeConcat += "0";
							labeConcat = labeConcat.concat(labeVal);
						}
						bisaun = bisaun.concat(labeConcat);	
					}

				}
				else if(opType.equals("beq") || opType.equals("bgt") || opType.equals("bne") || opType.equals("blt")){
					bisaun += (opTable.get(opType));
					if(ParsedProgram.code.get(i).sourceOperand1.operandType.toString().equals("Register")){
						bisaun = bisaun.concat(String.format("%5s", Integer.toBinaryString(ParsedProgram.code.get(i).sourceOperand1.value)).replace(' ', '0'));
					}
					if(ParsedProgram.code.get(i).sourceOperand2.operandType.toString().equals("Register")){
						bisaun = bisaun.concat(String.format("%5s", Integer.toBinaryString(ParsedProgram.code.get(i).sourceOperand2.value)).replace(' ', '0'));
					}
					// if(ParsedProgram.code.get(i).destinationOperand.operandType.toString().equals("Label")){
					// 	String labeVal = String.format("%5s", Integer.toBinaryString(ParsedProgram.symtab.get(ParsedProgram.code.get(i).destinationOperand.labelValue))).replace(' ', '0');
					// 	int labeSize = labeVal.length();
					// 	String labeConcat = "";
					// 	if(labeSize != 17){
					// 		int toBe = 17 - labeSize;
					// 		for(int j=0; j<toBe; j++)
					// 			labeConcat += "0";
					// 		labeConcat = labeConcat.concat(labeVal);
					// 	}
					// 	bisaun = bisaun.concat(labeConcat);
					// }
					int destAddr = 0;
					if(ParsedProgram.code.get(i).destinationOperand.operandType == OperandType.Label){ 
						destAddr = ParsedProgram.symtab.get(ParsedProgram.code.get(i).destinationOperand.labelValue);
					}
					else if(ParsedProgram.code.get(i).destinationOperand.operandType == OperandType.Immediate){
						destAddr = ParsedProgram.code.get(i).destinationOperand.value;
					}
					int pc = ParsedProgram.code.get(i).programCounter;
					int offset = destAddr - pc;
					System.out.println("pc= "+pc);
					System.out.println("offset= "+offset);
					if(offset>=0){
						String binRep = Integer.toBinaryString(offset);
						int binOffset = 17 - binRep.length();
						String concata = "";
						if(binOffset!=0){
							for(int j=0;j<binOffset;j++){
								concata = concata.concat("0");
							}
						}
						bisaun = bisaun.concat(concata);
						bisaun = bisaun.concat(binRep);
	
					} else {
						String binRep = Integer.toBinaryString(offset);
						String subRep = binRep.substring(15, 32);
						bisaun = bisaun.concat(subRep);
					}
					// 11100 00011 00000 (imm)
				}
				else if(opType.equals("end")){
					bisaun = bisaun.concat("11101000000000000000000000000000");
				}
				System.out.println(bisaun);
				int b_int = (int)Long.parseLong(bisaun, 2);
				byte[] toThefile = ByteBuffer.allocate(4).putInt(b_int).array();
				mech.write(toThefile);
			}
			//5. close the file
			mech.close();

		} catch(FileNotFoundException e){
			e.printStackTrace();

		} catch(IOException e){
			e.printStackTrace();

		}
	}

	public static HashMap<String, String> getOpTable() {
		return opTable;
	}

	public static void setOpTable(HashMap<String, String> opTable) {
		Simulator.opTable = opTable;
	}}