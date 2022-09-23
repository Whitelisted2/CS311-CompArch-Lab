package processor.pipeline;
import generic.Instruction;
import generic.Operand.OperandType;
import processor.Processor;

public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	private boolean b;
	
	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	

	public void performEX()
	{
		boolean jmpRes = false;
		/* blabla
		 * 
		 */
		if(OF_EX_Latch.isEX_enable()){
			Instruction instruction = OF_EX_Latch.getInstruction();
			EX_MA_Latch.setInstruction(instruction);
			String opType = instruction.getOperationType().toString();
			int nowPc = containingProcessor.getRegisterFile().programCounter -1;
			
			int aluResult = 0;
			b = opType.equals("addi") || opType.equals("subi") || opType.equals("muli") || opType.equals("divi") || opType.equals("andi") || opType.equals("ori") || opType.equals("xori") || opType.equals("slti") || opType.equals("slli") || opType.equals("srli") || opType.equals("srai") || opType.equals("load") || opType.equals("store");
			if(b){
				int rs1 = containingProcessor.getRegisterFile().getValue(instruction.getSourceOperand1().getValue());
				int immed = instruction.getSourceOperand2().getValue();
				int rd = containingProcessor.getRegisterFile().getValue(instruction.getDestinationOperand().getValue());
				switch(opType){
					case "addi":
						aluResult = rs1 + immed;
						break;
					case "subi":
						aluResult = rs1 - immed;
						break;
					case "muli":
						aluResult = rs1*immed;
						break;
					case "divi":
						aluResult = rs1/immed;
						containingProcessor.getRegisterFile().setValue(31, rs1%immed);
						// System.out.println(aluResult);
						break;
					case "andi":
						aluResult = rs1&immed;
						break;
					case "ori":
						aluResult = rs1 | immed;
						break;
					case "xori":
						aluResult = rs1 ^ immed;
						break;
					case "slti":
						aluResult = 0;
						if(immed > rs1) aluResult = 1;
						break;
					case "slli": 
						aluResult = rs1 << immed;
						break;
					case "srli": 
						aluResult = rs1 >>> immed;
						break;
					case "srai": 
						aluResult = rs1 >> immed;
						break;
					case "load":
						aluResult = rs1 + immed;
						break;
					case "store":
						aluResult = rd + immed;
						// System.out.println("rs 1 :"+ rs1+ "\n"+ "immed :"+immed +"\n"+ "rd : "+ rd);
						// System.out.println("Trying to store " + rd + " in " +  aluResult);
						break;
					default: 
						System.out.print("Issue detected in Execute.java, switch(OpType) for R2I");
				}}
			else if(opType.equals("add") || opType.equals("sub") || opType.equals("mul") || opType.equals("div") || opType.equals("and") || opType.equals("or") || opType.equals("xor") || opType.equals("slt") || opType.equals("sll") || opType.equals("srl") || opType.equals("sra")){
				int rs1 = containingProcessor.getRegisterFile().getValue(instruction.getSourceOperand1().getValue());
				int rs2 = containingProcessor.getRegisterFile().getValue(instruction.getSourceOperand2().getValue());
				switch(opType){
					case "add":
						aluResult = rs1 + rs2;
						break;
					case "sub":
						aluResult = rs1 - rs2;
						break;
					case "mul":
						aluResult = rs1 * rs2;
						break;
					case "div":
						aluResult = rs1 / rs2;
						containingProcessor.getRegisterFile().setValue(31, rs1%rs2);
						break;
					case "and": 
						aluResult = rs1 & rs2;
						break;
					case "or":
						aluResult = rs1 | rs2;
						break;
					case "xor":
						aluResult = rs1 ^ rs2;
						break;
					case "slt":
						aluResult = 0;
						if(rs2 > rs1) aluResult = 1;
						break;
					case "sll":
						aluResult = rs1 << rs2;
						break;
					case "srl":
						aluResult = rs1 >>> rs2;
						break;
					case "sra":
						aluResult = rs1 >> rs2;
						break;
					default:
						System.out.print("Issue detected in R3 type switch");
				}}
			else if(opType.equals("jmp")){
				OperandType jmpType = instruction.getDestinationOperand().getOperandType();
				int immed = 0;
				if(jmpType == OperandType.Immediate)
					immed = instruction.getDestinationOperand().getValue();
				else immed = containingProcessor.getRegisterFile().getValue(instruction.getDestinationOperand().getValue());
				aluResult = immed + nowPc ;
				jmpRes = true;
				EX_IF_Latch.setIsBranch_enable(true,aluResult);
			}
			else if(opType.equals("end")){
				/*
				 * do something
				 */
			}
			else{
				// for palindrome, remove ++nowpc ...
				// for desc, removing pc++ makes it go mad. with nowpc++, wrong hash
				// ++nowPc;
				int rs1 = containingProcessor.getRegisterFile().getValue(instruction.getSourceOperand1().getValue());
				int rd = containingProcessor.getRegisterFile().getValue(instruction.getSourceOperand2().getValue());
				int immed = instruction.getDestinationOperand().getValue();
				switch(opType){
					case "beq":
						if(rs1==rd){
							aluResult = nowPc + immed;
							jmpRes = true;
							EX_IF_Latch.setIsBranch_enable(true,aluResult);
						}
						break;
					case "bgt":
						if(rs1>rd){
							aluResult = nowPc + immed;
							jmpRes = true;
							// System.out.println(aluResult + "bgt and pc is " + nowPc);
							EX_IF_Latch.setIsBranch_enable(true,aluResult);
						}
						break;
					case "bne":
						if(rs1!=rd){
							aluResult = nowPc + immed;
							jmpRes = true;
							EX_IF_Latch.setIsBranch_enable(true,aluResult);
						}
						break;
					case "blt":
						if(rs1<rd){
							aluResult = nowPc + immed;
							jmpRes = true;
							EX_IF_Latch.setIsBranch_enable(true,aluResult);
						}
						break;
					default:
						System.out.print("Issue detected in R2I type, for branch statements");
						break;
				}
			}
			// System.out.println("At " + containingProcessor.getRegisterFile().getProgramCounter()+ " with " + aluResult + " and optype " + opType);
			EX_MA_Latch.setaluResult(aluResult);
		}
		OF_EX_Latch.setEX_enable(false);
		if(jmpRes == false)
		{EX_MA_Latch.setMA_enable(true);}
		}


	}


