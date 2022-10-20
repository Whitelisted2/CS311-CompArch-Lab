package processor.pipeline;

import processor.Processor;
import configuration.Configuration;
import generic.*;
import processor.Clock;

public class InstructionFetch implements Element{
	
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	int currentPC;
	
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performIF()
	{

		if(IF_EnableLatch.isIF_enable())
		{
			if(IF_EnableLatch.isBusy == true){
				return;
			}
		
			if(EX_IF_Latch.getIS_Enable()){
				int newPC = EX_IF_Latch.getPC();
				containingProcessor.getRegisterFile().setProgramCounter(newPC);
				EX_IF_Latch.setIS_Enable(false);
			}

			currentPC = containingProcessor.getRegisterFile().getProgramCounter();
			System.out.println("Current PC is " + currentPC);
			// int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
			// IF_OF_Latch.setInstruction(newInstruction);
			// containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);
			// System.out.println(" inside IF 1");
			// String opcode = Integer.toBinaryString(newInstruction).substring(0,5);
			// System.out.println(opcode);
			// IF_EnableLatch.setIF_enable(false);

			//IF_OF_Latch.setOF_enable(true);

			Statistics.setNumberOfInstructions(Statistics.getNumberOfInstructions() + 1);
			Simulator.getEventQueue().addEvent(
				new MemoryReadEvent(
					Clock.getCurrentTime() + Configuration.mainMemoryLatency,
					this,
					containingProcessor.getMainMemory(),
					currentPC)
			);
			IF_EnableLatch.isBusy = true;

		}
		// else if(EX_IF_Latch.getIsBranch_enable()){
		// 	int newPC = EX_IF_Latch.getPC();
		// 	// System.out.println("\n"+newPC);
		// 	System.out.println(" Inside IF 2");
		// 	int newInst = containingProcessor.getMainMemory().getWord(newPC);
		// 	String opcode = Integer.toBinaryString(newInst).substring(0,5);
		// 	System.out.println(opcode);
		// 	IF_OF_Latch.setInstruction(newInst);
		// 	containingProcessor.getRegisterFile().setProgramCounter(newPC);
		// 	EX_IF_Latch.setIsBranch_enable(false);
		// 	IF_OF_Latch.setOF_enable(true);
		// }
	}

	@Override
	public void handleEvent(Event e) {
		if(IF_OF_Latch.isBusy == true) {
			System.out.println("IF OF Latch is busy !");
			e.setEventTime(Clock.getCurrentTime() + 1);
			Simulator.getEventQueue().addEvent(e);
		} else {
			MemoryResponseEvent event = (MemoryResponseEvent) e;
			System.out.println("Memory responding ...");
			if(EX_IF_Latch.IS_enable == false) {
				IF_OF_Latch.setInstruction(event.getValue());
			} else {
				IF_OF_Latch.setInstruction(0);
			}
			IF_OF_Latch.instPC = this.currentPC;
			containingProcessor.getRegisterFile().setProgramCounter(this.currentPC + 1);
			IF_OF_Latch.setOF_enable(true);
			IF_EnableLatch.isBusy = false;
		}
	}

}

