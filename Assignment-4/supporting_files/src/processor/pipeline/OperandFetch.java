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
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_LatchE;

	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableE) {
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_LatchE = iF_EnableE;
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

	public boolean misterConflict(Instruction instC, int regA, int regB){
		if(instC != null&&!instC.getOperationType().equals(OperationType.end)){
			// System.out.println("Checking conflicts for " + instC.getOperationType());
			System.out.println("Instruction in conflicter : "+ instC);
			int dest_op = instC.getDestinationOperand().getValue();
			if(dest_op == regA || dest_op == regB){
				System.out.println("Data Conflict!");
				return true;
			}
			if(instC.getOperationType().ordinal() == 6 || instC.getOperationType().ordinal() == 7){
				if(regA == 31 || regB == 31){
					System.out.println("Data, Division conflict!");
					return true;
				}
			}
		}
		return false;
	}

	public boolean mrsDivConflict(Instruction instC, int regA, int a){return false;}

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
			Instruction inst_ex = OF_EX_Latch.getInstruction();
			Instruction inst_ma = EX_MA_Latch.getInstruction();
			Instruction inst_rw = MA_RW_Latch.getInstruction();
			boolean conflict_check = false;
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
					int num1 = rs1.getValue();
					int num2 = rs2.getValue();
					rd.setValue(Integer.parseInt(newInstruction.substring(15, 20),2));
					if(misterConflict(inst_ex,num1,num2)||misterConflict(inst_ma, num1, num2)||misterConflict(inst_rw, num1, num2)){
						conflict_check = true;
					}
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
					rd = new Operand();
					rd.setOperandType(OperandType.Immediate);
					if(immed.charAt(0)=='1'){
						immed = twosComplement(immed);
						rd.setValue(-1*Integer.parseInt(immed,2));
					}
					else rd.setValue(Integer.parseInt(immed,2));
					num1 = rs1.getValue();
					if(misterConflict(inst_ex,num1,0)||misterConflict(inst_ma, num1, 0)||misterConflict(inst_rw, num1, 0)){
						conflict_check = true;
					}
					newInst.setSourceOperand1(rs1);
					newInst.setSourceOperand2(rd);
					newInst.setDestinationOperand(rs2);
					break;
				case jmp:
					Operand imme = new Operand();
					imme.setOperandType(OperandType.Immediate);
					rd = new Operand();
					rd.setOperandType(OperandType.Register);
					rd.setValue(Integer.parseInt(newInstruction.substring(5, 10),2));
					newInst.setSourceOperand1(rd);
					String immedi = newInstruction.substring(10, 32);
					imme.setValue(Integer.parseInt(immedi,2));
					if(immedi.charAt(0)=='1'){
						immedi = twosComplement(immedi);
						imme.setValue(-1*Integer.parseInt(immedi,2));
					}
					// System.out.println("\n" + imme);
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
					immedi = newInstruction.substring(15, 32);
					imme.setValue(Integer.parseInt(immedi,2));
					if(immedi.charAt(0)=='1'){
						immedi = twosComplement(immedi);
						imme.setValue(-1*Integer.parseInt(immedi,2));
					}
					num1 = rs1.getValue();
					num2 = rd.getValue();
					if(misterConflict(inst_ex,num1,num2)||misterConflict(inst_ma, num1, num2)||misterConflict(inst_rw, num1, num2)){
						conflict_check = true;
					}
					newInst.setSourceOperand1(rs1);
					newInst.setSourceOperand2(rd);
					newInst.setDestinationOperand(imme);
					break;
				case end:
					// System.out.println("Inside end");
					break;
				default:
					break;
					
			}
			if(conflict_check == true){
				IF_LatchE.setIF_enable(false);
				OF_EX_Latch.setIsNOP(true);
				OF_EX_Latch.setInstruction(null);
			}
			else {OF_EX_Latch.setInstruction(newInst);}
			// IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);
		}
	}

}
