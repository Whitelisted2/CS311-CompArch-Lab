package processor.pipeline;

import processor.Processor;
import generic.Instruction;
import generic.Operand;
import generic.Instruction.OperationType;
import generic.Operand.OperandType;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;

	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch) {
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}

	public String twosComplement(String bin) {

		String twos = "", ones = "";
		for (int i = 0; i < bin.length(); i++)
			ones += flip(bin.charAt(i));

		StringBuilder builder = new StringBuilder(ones);
		boolean addExtra = false;

		for (int i = ones.length() - 1; i > 0; i--) {

			if (ones.charAt(i) == '1')
				builder.setCharAt(i, '0');
			else {

				builder.setCharAt(i, '1');
				addExtra = true;
				break;
			}
		}

		if (addExtra == false)
			builder.append("1", 0, 7);

		twos = builder.toString();
		return twos;
	}

	public char flip(char c) {
		return (c == '0') ? '1' : '0';
	}

	public void performOF()
	{
		if(IF_OF_Latch.isOF_enable())
		{
			//TODO
			Instruction newInst = new Instruction();
			String newInstruction = Integer.toBinaryString(IF_OF_Latch.getInstruction());
			int cSize = newInstruction.length();
			cSize = 32 - cSize;
			for(int i=1;i<=cSize;i++)
				newInstruction = '0' + newInstruction;
			String opCode = newInstruction.substring(0, 5);
			OperationType[] opConv = OperationType.values();
			OperationType opInst = opConv[Integer.parseInt(opCode,2)];
			newInst.setOperationType(opInst);
			switch(opInst){

				case add:
				case sub:
				case mul:
				case div:
				case and:
				case or:
				case xor:
				case slt:
				case sll:
				case srl:
				case sra:
					Operand rs1 = new Operand();
					rs1.setOperandType(OperandType.Register);
					Operand rs2 = new Operand();
					rs2.setOperandType(OperandType.Register);
					Operand rd = new Operand();
					rd.setOperandType(OperandType.Register);
					rs1.setValue(Integer.parseInt(newInstruction.substring(5,10),2));
					rs2.setValue(Integer.parseInt(newInstruction.substring(10, 15),2));
					rd.setValue(Integer.parseInt(newInstruction.substring(15, 20),2));
					newInst.setSourceOperand1(rs1);
					newInst.setSourceOperand2(rs2);
					newInst.setDestinationOperand(rd);
					break;
				case addi:
				case andi:
				case muli:
				case ori:
				case slli:
				case slti:
				case srai:
				case srli:
				case subi:
				case xori:
				case divi:
				case store:
				case load:
					rs1 = new Operand();
					rs1.setOperandType(OperandType.Register);
					rs2 = new Operand();
					rs2.setOperandType(OperandType.Register);
					String immed = "";
					rs1.setValue(Integer.parseInt(newInstruction.substring(5, 10),2));
					rs2.setValue(Integer.parseInt(newInstruction.substring(10, 15),2));
					immed = newInstruction.substring(15, 32);
					if(immed.charAt(0)=='1'){
						immed = twosComplement(immed);
					}
					rd = new Operand();
					rd.setValue(Integer.parseInt(immed,2));
					newInst.setSourceOperand1(rs1);
					newInst.setSourceOperand2(rs2);
					newInst.setDestinationOperand(rd);
					break;
				case jmp:
					Operand imme = new Operand();
					imme.setOperandType(OperandType.Immediate);
					imme.setValue(Integer.parseInt(newInstruction.substring(10, 32),2));
					newInst.setDestinationOperand(imme);
					break;
				case beq:
				case bgt:
				case blt:
				case bne:
					rs1 = new Operand();
					rd = new Operand();
					imme = new Operand();
					rs1.setOperandType(OperandType.Register);
					rd.setOperandType(OperandType.Register);
					imme.setOperandType(OperandType.Immediate);
					rs1.setValue(Integer.parseInt(newInstruction.substring(5, 10),2));
					rd.setValue(Integer.parseInt(newInstruction.substring(10, 15),2));
					imme.setValue(Integer.parseInt(newInstruction.substring(15, 32)));
					newInst.setSourceOperand1(rs1);
					newInst.setSourceOperand2(rd);
					newInst.setDestinationOperand(imme);
					break;
				case end:
					break;
				default:
					break;
					
			}
			OF_EX_Latch.setInstruction(newInst);
			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);
		}
	}

}
