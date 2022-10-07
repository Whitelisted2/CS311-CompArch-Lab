package processor.pipeline;

import generic.Instruction;
import processor.Processor;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableE)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableE;
	}
	
	public void performMA()
	{
		System.out.println("EX MA Latch status is " + EX_MA_Latch.isMA_enable());
		if(EX_MA_Latch.getIsNOP()){
			MA_RW_Latch.setIsNOP(true);
			MA_RW_Latch.setInstruction(null);
			EX_MA_Latch.setIsNOP(false);
		}
		else if(EX_MA_Latch.isMA_enable()){
			// System.out.println("Your pc is ma :" + pc);
			Instruction instruction = EX_MA_Latch.getInstruction();
			System.out.println(instruction);
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
			if(op.equals("end)")){
				IF_EnableLatch.setIF_enable(false);
			}

			MA_RW_Latch.setInstruction(instruction);
			// EX_MA_Latch.setMA_enable(false);
			MA_RW_Latch.setRW_enable(true);
		}
	}

}
