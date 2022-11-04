package processor.pipeline;

import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Operand;
import processor.Processor;

import java.util.Arrays;

public class Execute {
    Processor containingProcessor;
    OF_EX_LatchType OF_EX_Latch;
    EX_MA_LatchType EX_MA_Latch;
    EX_IF_LatchType EX_IF_Latch;
    IF_OF_LatchType IF_OF_Latch;
    MA_RW_LatchType MA_RW_Latch;
    IF_EnableLatchType IF_EnableLatch;

    public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch,
            EX_IF_LatchType eX_IF_Latch, IF_OF_LatchType iF_OF_Latch, MA_RW_LatchType mA_RW_Latch,
            IF_EnableLatchType iF_EnableLatch) {
        this.containingProcessor = containingProcessor;
        this.OF_EX_Latch = oF_EX_Latch;
        this.EX_MA_Latch = eX_MA_Latch;
        this.EX_IF_Latch = eX_IF_Latch;
        this.IF_OF_Latch = iF_OF_Latch;
        this.MA_RW_Latch = mA_RW_Latch;
        this.IF_EnableLatch = iF_EnableLatch;
    }

    public void performEX() {
        if (EX_MA_Latch.isMA_busy())
            OF_EX_Latch.setEX_busy(true);
        else
            OF_EX_Latch.setEX_busy(false);
        if (OF_EX_Latch.isEX_enable() && !EX_MA_Latch.isMA_busy()) {
            boolean branchDetect = false;
            if (OF_EX_Latch.isNop) {
                EX_MA_Latch.isNop = true;
            } else {
                EX_MA_Latch.isNop = false;
                Instruction cmd = OF_EX_Latch.getInstruction();
                EX_MA_Latch.setInstruction(cmd);
                OperationType cmd_op = cmd.getOperationType();
                int cmd_op_opcode = Arrays.asList(OperationType.values()).indexOf(cmd_op);
                int currentPC = containingProcessor.getRegisterFile().getProgramCounter() - 1;
                String opType = cmd_op.toString();
                boolean b = opType.equals("addi") || opType.equals("subi") || opType.equals("muli") || opType.equals("divi")
                || opType.equals("andi") || opType.equals("ori") || opType.equals("xori") || opType.equals("slti")
                || opType.equals("slli") || opType.equals("srli") || opType.equals("srai") || opType.equals("load");
                int ALU_output = 0;

                if (opType.equals("add") || opType.equals("sub") || opType.equals("mul") || opType.equals("div")
					|| opType.equals("and") || opType.equals("or") || opType.equals("xor") || opType.equals("slt")
					|| opType.equals("sll") || opType.equals("srl") || opType.equals("sra")) {
				int op1 = containingProcessor.getRegisterFile().getValue(cmd.getSourceOperand1().getValue());
				int op2 = containingProcessor.getRegisterFile().getValue(cmd.getSourceOperand2().getValue());
				switch (cmd_op) {
					case add:
						ALU_output = op1 + op2;
						break;
					case sub:
						ALU_output = op1 - op2;
						break;
					case mul:
						ALU_output = op1 * op2;
						break;
					case div:
						ALU_output = op1 / op2;
						int remainder = op1 % op2;
						containingProcessor.getRegisterFile().setValue(31, remainder);
						break;
					case and:
						ALU_output = op1 & op2;
						break;
					case or:
						ALU_output = op1 | op2;
						break;
					case xor:
						ALU_output = op1 ^ op2;
						break;
					case slt:
						if (op1 < op2)
							ALU_output = 1;
						else
							ALU_output = 0;
						break;
					case sll:
						ALU_output = op1 << op2;
						break;
					case srl:
						ALU_output = op1 >>> op2;
						break;
					case sra:
						ALU_output = op1 >> op2;
						break;
					default:
						break;
				}
			} else if (b) {
				int i = cmd.getSourceOperand1().getValue();
				int op1 = containingProcessor.getRegisterFile().getValue(i);
				int op2 = cmd.getSourceOperand2().getValue();

				switch (cmd_op) {
					case addi:
						ALU_output = op1 + op2;
						break;
					case subi:
						ALU_output = op1 - op2;
						break;
					case muli:
						ALU_output = op1 * op2;
						break;
					case divi:
						ALU_output = op1 / op2;
						int remainder = op1 % op2;
						containingProcessor.getRegisterFile().setValue(31, remainder);
						break;
					case andi:
						ALU_output = op1 & op2;
						break;
					case ori:
						ALU_output = op1 | op2;
						break;
					case xori:
						ALU_output = op1 ^ op2;
						break;
					case slti:
						if (op1 < op2)
							ALU_output = 1;
						else
							ALU_output = 0;
						break;
					case slli:
						ALU_output = op1 << op2;
						break;
					case srli:
						ALU_output = op1 >>> op2;
						break;
					case srai:
						ALU_output = op1 >> op2;
						break;
					case load:
						ALU_output = op1 + op2;
						break;
					default:
						break;
				}
			} else if (cmd_op.equals(OperationType.store)) {
                    int SOP1 = cmd.getDestinationOperand().getValue();
                    int SOP2 = containingProcessor.getRegisterFile().getValue(cmd.getSourceOperand2().getValue());
                    ALU_output = SOP1 + SOP2;
                } else if (cmd_op.equals(OperationType.jmp)) {
                    Operand.OperandType OPERNDTYPE = cmd.getDestinationOperand().getOperandType();
                    int immediate = 0;
                    if (OPERNDTYPE == Operand.OperandType.Register) {
                        immediate = containingProcessor.getRegisterFile().getValue(
                                cmd.getDestinationOperand().getValue());
                    } else {
                        immediate = cmd.getDestinationOperand().getValue();
                    }
                    ALU_output = immediate;
                    branchDetect = true;
                } else if (cmd_op.equals(OperationType.beq)||cmd_op.equals(OperationType.bgt)||cmd_op.equals(OperationType.bne)||cmd_op.equals(OperationType.blt)) {
                    int SOP1 = containingProcessor.getRegisterFile().getValue(cmd.getSourceOperand1().getValue());
                    int SOP2 = containingProcessor.getRegisterFile().getValue(cmd.getSourceOperand2().getValue());
                    int immediate = cmd.getDestinationOperand().getValue();
                    switch(cmd_op){
                    case beq:
                        if (SOP1 == SOP2) {
                            ALU_output = immediate;
                            branchDetect = true;
                        }
                    break;
                    case bne:
                        if (SOP1 != SOP2) {
                            ALU_output = immediate;
                            branchDetect = true;
                        }
                    break;
                    case blt:
                        if (SOP1 < SOP2) {
                            ALU_output = immediate;
                            branchDetect = true;
                        }
                    break;
                    case bgt:
                        if (SOP1 > SOP2) {
                            ALU_output = immediate;
                            branchDetect = true;
                        }
                    break;
                        default:
                            break;
                    }
                }
                if (branchDetect) {
                    EX_IF_Latch.isBranchTaken = true;
                    EX_IF_Latch.offset = ALU_output - 1;
                    IF_EnableLatch.setIF_enable(true);
                    OF_EX_Latch.setEX_enable(false);
                    IF_OF_Latch.setOF_enable(false);
                    //nop time
                    Instruction nop = new Instruction();
                    nop.setOperationType(OperationType.nop);
                    OF_EX_Latch.setInstruction(nop);
                }
                EX_MA_Latch.setALU_result(ALU_output);
                EX_MA_Latch.insPC = OF_EX_Latch.insPC;
                if (OF_EX_Latch.getInstruction().getOperationType().toString().equals("end")) {
                    OF_EX_Latch.setEX_enable(false);
                }
            }
            OF_EX_Latch.setEX_enable(false);
            EX_MA_Latch.setMA_enable(true);
        }
    }
}
