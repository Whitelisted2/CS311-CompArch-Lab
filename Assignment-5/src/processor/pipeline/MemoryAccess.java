package processor.pipeline;

import processor.Processor;
import generic.Element;
import generic.Event;
import generic.Instruction;
import generic.MemoryResponseEvent;
import generic.Event.EventType;
import generic.Instruction.OperationType;

public class MemoryAccess implements Element {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;

	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch,
			IF_EnableLatchType iF_EnableLatch) {
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}

	public void performMA() {

		if (EX_MA_Latch.getIsNop()&& EX_MA_Latch.isMA_enable() && EX_MA_Latch.isBusy == false) {
			MA_RW_Latch.setIsNop(true);
			MA_RW_Latch.setInstruction(null);
			EX_MA_Latch.setIsNop(false);
		} else if (EX_MA_Latch.isMA_enable() && EX_MA_Latch.isBusy == false) {
			Instruction instruction = EX_MA_Latch.getInstruction();
			int alu_result = EX_MA_Latch.getaluResult();
			System.out.println("MA is enabled: " + instruction);
			MA_RW_Latch.setaluResult(alu_result);
			OperationType op_type = instruction.getOperationType();
			switch (op_type) {
				case store:
					int val_store = containingProcessor.getRegisterFile()
							.getValue(instruction.getSourceOperand1().getValue());
					containingProcessor.getMainMemory().setWord(alu_result, val_store);
					break;

				case load:
					int load_result = containingProcessor.getMainMemory().getWord(alu_result);
					MA_RW_Latch.setldResult(load_result);
					break;

				default:
					break;
			}

			if (instruction.getOperationType().ordinal() == 29) {
				IF_EnableLatch.setIF_enable(false);
			}
			MA_RW_Latch.setInstruction(instruction);
			MA_RW_Latch.setRW_enable(true);
			// EX_MA_Latch.setMA_enable(false);
		}
	}

	@Override
	public void handleEvent(Event e) {
		// TODO Auto-generated method stub

		EX_MA_Latch.isBusy = false;

		if (e.getEventType() == EventType.MemoryResponse) {
			MemoryResponseEvent event = (MemoryResponseEvent) e;
			MA_RW_Latch.setaluResult(event.getValue());
			// MA_RW_Latch.inspc = EX_MA_Latch.insPC;
			MA_RW_Latch.setRW_enable(true);

		}

	}

}
