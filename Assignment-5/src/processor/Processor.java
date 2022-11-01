package processor;

import processor.memorysystem.MainMemory;
import processor.pipeline.*;

public class Processor {

    int stalls;
    int wrong_input;
    int freezed_stalls;
    int freezed_wrong_input;
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

    public Processor() {
        stalls = 0;
        wrong_input = 0;
        registerFile = new RegisterFile();
        mainMemory = new MainMemory();

        IF_EnableLatch = new IF_EnableLatchType();
        IF_OF_Latch = new IF_OF_LatchType();
        OF_EX_Latch = new OF_EX_LatchType();
        EX_MA_Latch = new EX_MA_LatchType();
        EX_IF_Latch = new EX_IF_LatchType();
        MA_RW_Latch = new MA_RW_LatchType();

        IFUnit = new InstructionFetch(this, IF_EnableLatch, IF_OF_Latch, EX_IF_Latch);
        OFUnit = new OperandFetch(this, IF_OF_Latch, OF_EX_Latch, EX_MA_Latch, MA_RW_Latch, IF_EnableLatch);
        EXUnit = new Execute(this, OF_EX_Latch, EX_MA_Latch, EX_IF_Latch, IF_OF_Latch, MA_RW_Latch);
        MAUnit = new MemoryAccess(this, EX_MA_Latch, MA_RW_Latch, IF_OF_Latch, OF_EX_Latch);
        RWUnit = new RegisterWrite(this, MA_RW_Latch, IF_EnableLatch, IF_OF_Latch, OF_EX_Latch, EX_MA_Latch);
    }

    public void printState(int memoryStartingAddress, int memoryEndingAddress) {
        System.out.println(registerFile.getContentsAsString());

        System.out.println(mainMemory.getContentsAsString(memoryStartingAddress, memoryEndingAddress));
    }

    public int getFreezed_stalls() {
        return freezed_stalls;
    }

    public void setFreezed_stalls(int freezed_stalls) {
        this.freezed_stalls = freezed_stalls;
    }

    public int getFreezed_wrong_input() {
        return freezed_wrong_input;
    }

    public void setFreezed_wrong_input(int freezed_wrong_input) {
        this.freezed_wrong_input = freezed_wrong_input;
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

    public int getStalls() {
        return stalls;
    }

    public void setStalls(int stalls) {
        this.stalls = stalls;
    }

    public int getWrong_input() {
        return wrong_input;
    }

    public void setWrong_input(int wrong_input) {
        this.wrong_input = wrong_input;
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
