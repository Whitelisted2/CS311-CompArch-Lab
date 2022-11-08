package processor;

import configuration.Configuration;
import processor.memorysystem.Cache;
import processor.memorysystem.MainMemory;
import processor.pipeline.*;

public class Processor {

    RegisterFile registerFile;
    MainMemory mainMemory;

    IF_EnableLatchType IF_EnableLatch;
    IF_OF_LatchType IF_OF_Latch;
    OF_EX_LatchType OF_EX_Latch;
    EX_MA_LatchType EX_MA_Latch;
    EX_IF_LatchType EX_IF_Latch;
    MA_RW_LatchType MA_RW_Latch;

    InstructionFetch IFUnit;
    OperandFetch OFUnit;
    Execute EXUnit;
    MemoryAccess MAUnit;
    RegisterWrite RWUnit;

    Cache cache_i,cache_d;

    public Processor() {

        registerFile = new RegisterFile();
        mainMemory = new MainMemory();

        IF_EnableLatch = new IF_EnableLatchType();
        IF_OF_Latch = new IF_OF_LatchType();
        OF_EX_Latch = new OF_EX_LatchType();
        EX_MA_Latch = new EX_MA_LatchType();
        EX_IF_Latch = new EX_IF_LatchType();
        MA_RW_Latch = new MA_RW_LatchType();

        cache_i = new Cache(this,Configuration.L1i_latency,Configuration.L1i_numberOfLines);
        cache_d = new Cache(this,Configuration.L1d_latency, Configuration.L1d_numberOfLines);

        IFUnit = new InstructionFetch(this, IF_EnableLatch, IF_OF_Latch, EX_IF_Latch,cache_i);
        OFUnit = new OperandFetch(this, IF_OF_Latch, OF_EX_Latch, EX_MA_Latch, MA_RW_Latch, IF_EnableLatch);
        EXUnit = new Execute(this, OF_EX_Latch, EX_MA_Latch, EX_IF_Latch, IF_OF_Latch, MA_RW_Latch,IF_EnableLatch);
        MAUnit = new MemoryAccess(this, EX_MA_Latch, MA_RW_Latch, IF_OF_Latch, OF_EX_Latch,cache_d);
        RWUnit = new RegisterWrite(this, MA_RW_Latch, IF_EnableLatch, IF_OF_Latch, OF_EX_Latch, EX_MA_Latch);
    }

    public void printState(int memoryStartingAddress, int memoryEndingAddress) {
        System.out.println(registerFile.getContentsAsString());

        System.out.println(mainMemory.getContentsAsString(memoryStartingAddress, memoryEndingAddress));
    }

    public RegisterFile getRegisterFile() {
        return registerFile;
    }

    public void setRegisterFile(RegisterFile registerFile) {
        this.registerFile = registerFile;
    }

    public MainMemory getMainMemory() {
        return mainMemory;
    }

    public void setMainMemory(MainMemory mainMemory) {
        this.mainMemory = mainMemory;
    }

    public InstructionFetch getIFUnit() {
        return IFUnit;
    }

    public OperandFetch getOFUnit() {
        return OFUnit;
    }

    public Execute getEXUnit() {
        return EXUnit;
    }

    public MemoryAccess getMAUnit() {
        return MAUnit;
    }

    public RegisterWrite getRWUnit() {
        return RWUnit;
    }

}
