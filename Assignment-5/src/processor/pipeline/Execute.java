package processor.pipeline;

import configuration.Configuration;
import generic.*;
import generic.Instruction.OperationType;
import processor.Clock;
import processor.Processor;

import java.util.Arrays;

public class Execute implements Element {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	IF_OF_LatchType IF_OF_Latch;
	MA_RW_LatchType MA_RW_Latch;

	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch,
			EX_IF_LatchType eX_IF_Latch, IF_OF_LatchType iF_OF_Latch, MA_RW_LatchType mA_RW_Latch) {
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}

	public void performEX() {
		if (EX_MA_Latch.isMA_busy()) {
			return;
		} else if (OF_EX_Latch.isEX_enable()) {
			if (OF_EX_Latch.isEX_busy()) {
				EX_MA_Latch.setMA_enable(false);
				return;
			}
			Instruction cmd = OF_EX_Latch.getInstruction();
			OperationType op_type = cmd.getOperationType();
			String opType = op_type.toString();
			EX_MA_Latch.setInstruction(cmd);
			OperationType cmd_op = cmd.getOperationType();
			int cmd_op_opcode = Arrays.asList(OperationType.values()).indexOf(cmd_op);
			int currentPC = containingProcessor.getRegisterFile().getProgramCounter() - 1;

			int ALU_output = 0;
			boolean b = opType.equals("addi") || opType.equals("subi") || opType.equals("muli") || opType.equals("divi")
					|| opType.equals("andi") || opType.equals("ori") || opType.equals("xori") || opType.equals("slti")
					|| opType.equals("slli") || opType.equals("srli") || opType.equals("srai") || opType.equals("load");
			if (opType.equals("add") || opType.equals("sub") || opType.equals("mul") || opType.equals("div")
					|| opType.equals("and") || opType.equals("or") || opType.equals("xor") || opType.equals("slt")
					|| opType.equals("sll") || opType.equals("srl") || opType.equals("sra")) {
				int op1 = containingProcessor.getRegisterFile().getValue(cmd.getSourceOperand1().getValue());
				int op2 = containingProcessor.getRegisterFile().getValue(cmd.getSourceOperand2().getValue());
				Simulator.getEventQueue().addEvent(
						new ExecutionCompleteEvent(Clock.getCurrentTime() + Configuration.ALU_latency, this, this));
				OF_EX_Latch.setEX_busy(true);
				EX_MA_Latch.setMA_enable(false);
				switch (op_type) {
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
				Simulator.getEventQueue().addEvent(
						new ExecutionCompleteEvent(Clock.getCurrentTime() + Configuration.ALU_latency, this, this));
				OF_EX_Latch.setEX_busy(true);
				EX_MA_Latch.setMA_enable(false);
				int i = cmd.getSourceOperand1().getValue();
				int op1 = containingProcessor.getRegisterFile().getValue(i);
				int op2 = cmd.getSourceOperand2().getValue();

				switch (op_type) {
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
			} else if (cmd_op_opcode == 23) {
				int SOP1 = containingProcessor.getRegisterFile().getValue(cmd.getDestinationOperand().getValue());
				int SOP2 = cmd.getSourceOperand2().getValue();
				ALU_output = SOP1 + SOP2;
			} else if (cmd_op_opcode == 24) {
				/*
				 * OperandType OPERNDTYPE = cmd.getDestinationOperand().getOperandType();
				 * int immediate = 0;
				 * if (OPERNDTYPE == OperandType.Register)
				 * {
				 * immediate = containingProcessor.getRegisterFile().getValue(
				 * cmd.getDestinationOperand().getValue());
				 * }
				 * else
				 * {
				 * immediate = cmd.getDestinationOperand().getValue();
				 * }
				 * ALU_output = immediate + currentPC;
				 * EX_IF_Latch.setIS_enable(true, ALU_output);
				 */
				// DONE IN OF.
			} else if (op_type.equals(OperationType.beq) || op_type.equals(OperationType.bne)
					|| op_type.equals(OperationType.blt) || op_type.equals(OperationType.bgt)) {
				int op1 = containingProcessor.getRegisterFile().getValue(cmd.getSourceOperand1().getValue());
				int op2 = containingProcessor.getRegisterFile().getValue(cmd.getSourceOperand2().getValue());
				int imm = cmd.getDestinationOperand().getValue();
				switch (op_type) {
					case beq:
						if (op1 == op2) {
							ALU_output = imm + currentPC;
							containingProcessor.getRegisterFile().setProgramCounter(ALU_output);
							EX_MA_Latch.setFlag(1);
						}
						break;
					case bne:
						if (op1 != op2) {
							ALU_output = imm + currentPC;
							containingProcessor.getRegisterFile().setProgramCounter(ALU_output);
							EX_MA_Latch.setFlag(1);
						}

						break;
					case blt:
						if (op1 < op2) {
							ALU_output = imm + currentPC;
							containingProcessor.getRegisterFile().setProgramCounter(ALU_output);
							EX_MA_Latch.setFlag(1);
						}
						break;
					case bgt:
						if (op1 > op2) {
							ALU_output = imm + currentPC;
							containingProcessor.getRegisterFile().setProgramCounter(ALU_output);
							EX_MA_Latch.setFlag(1);
						}
						break;
					default:
						break;
				}
			}

			else if (cmd_op.toString().equals("nop"))
				;
			EX_MA_Latch.setALU_result(ALU_output);
			if (!IF_OF_Latch.isOF_enable()) {
				OF_EX_Latch.setEX_enable(false);
			}
		}
	}

	@Override
	public void handleEvent(Event e) {
		// System.out.println(" Handling MemoryResponse Event in EX Stage");
		if (EX_MA_Latch.isMA_busy()) {
			e.setEventTime(Clock.getCurrentTime() + 1);
			Simulator.getEventQueue().addEvent(e);
		} else {
			// System.out.println(" is in ex");
			EX_MA_Latch.setMA_enable(true);
			OF_EX_Latch.setEX_busy(false);
		}
	}
}
