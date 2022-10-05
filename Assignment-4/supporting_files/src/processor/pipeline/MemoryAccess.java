package processor.pipeline;

import generic.Instruction;
import processor.Processor;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	public void performMA()
	{
		if(EX_MA_Latch.isMA_enable()){
			Instruction instruction = EX_MA_Latch.getInstruction();
			String op = instruction.getOperationType().toString();
			int aluResult = EX_MA_Latch.getaluResult();
			MA_RW_Latch.setaluResult(aluResult);

			if(op.equals("load")){
				int ldResult = containingProcessor.getMainMemory().getWord(aluResult);
				MA_RW_Latch.setldResult(ldResult);
			}else if(op.equals("store")){
				// aluResult is location of storing
				// rs1 contains data to be stored
				int rs1 = instruction.getSourceOperand1().getValue();
				int inp = containingProcessor.getRegisterFile().getValue(rs1);
				containingProcessor.getMainMemory().setWord(aluResult, inp);
			}

			MA_RW_Latch.setInstruction(instruction);
			EX_MA_Latch.setMA_enable(false);
			MA_RW_Latch.setRW_enable(true);
		}
	}

}
