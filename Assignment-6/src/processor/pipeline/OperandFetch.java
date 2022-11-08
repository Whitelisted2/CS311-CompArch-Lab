package processor.pipeline;

import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Operand;
import generic.Operand.OperandType;
import processor.Processor;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OperandFetch {
    Processor containingProcessor;
    IF_EnableLatchType IF_EnableLatch;
    IF_OF_LatchType IF_OF_Latch;
    OF_EX_LatchType OF_EX_Latch;
    EX_MA_LatchType EX_MA_Latch;
    MA_RW_LatchType MA_RW_Latch;


    public OperandFetch(Processor containingProcessor,
                        IF_OF_LatchType iF_OF_Latch,
                        OF_EX_LatchType oF_EX_Latch,
                        EX_MA_LatchType eX_MA_Latch,
                        MA_RW_LatchType mA_RW_Latch,
                        IF_EnableLatchType if_enableLatch) {
        this.containingProcessor = containingProcessor;
        this.IF_OF_Latch = iF_OF_Latch;
        this.OF_EX_Latch = oF_EX_Latch;
        this.EX_MA_Latch = eX_MA_Latch;
        this.MA_RW_Latch = mA_RW_Latch;
        this.IF_EnableLatch = if_enableLatch;
    }

    public static char opposite(char c) {
        return (c == '0') ? '1' : '0';
    }

    public static String twosComplement(String binary) {
        String twos = "", ones = "";
        for (int i = 0; i < binary.length(); i++) {
            ones += opposite(binary.charAt(i));
        }

        StringBuilder builder = new StringBuilder(ones);
        boolean flag = false;
        for (int i = ones.length() - 1; i > 0; i--) {
            if (ones.charAt(i) == '1') {
                builder.setCharAt(i, '0');
            } else {
                builder.setCharAt(i, '1');
                flag = true;
                break;
            }
        }
        if (!flag) {
            builder.append("1", 0, 7);
        }
        twos = builder.toString();
        return twos;
    }

    public void performOF() {
        if(OF_EX_Latch.isEX_busy()) IF_OF_Latch.setOF_busy(true);
        else IF_OF_Latch.setOF_busy(false);
        // if(IF_OF_Latch.isOF_enable() == false) OF_EX_Latch.EX_enable = false;
        if(IF_OF_Latch.isOF_enable() && !OF_EX_Latch.isEX_busy())
        {
            int inst = IF_OF_Latch.getInstruction();
            //System.out.println(inst);
            OperationType[] operationType = OperationType.values();
            String instb = Integer.toBinaryString(inst);
            //System.out.println(instb);
            if (instb.length() != 32) {
                int limm = instb.length();
                String lRepeated = "";
                if ((32 - limm) != 0) {
                    String s = "0";
                    int q = 32 - limm;
                    lRepeated = IntStream.range(0, q).mapToObj(i -> s).collect(Collectors.joining(""));
                }
                instb = lRepeated + instb;
            }
            String opcode = instb.substring(0, 5);
            //System.out.println(opcode);
            int opcodei = Integer.parseInt(opcode, 2);
            //System.out.println(opcodei);
            OperationType operation = operationType[opcodei];
            //System.out.println(operation);
            Instruction instn = new Instruction();
            Operand rs1;
            int reg_no;
            Operand rs2;
            Operand rd;
            String cons;
            int cons_val;
			switch(operation){

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
                rs1 = new Operand();
                rs1.setOperandType(OperandType.Register);
                reg_no = Integer.parseInt(instb.substring(5, 10), 2);
                rs1.setValue(reg_no);

                rs2 = new Operand();
                rs2.setOperandType(OperandType.Register);
                reg_no = Integer.parseInt(instb.substring(10, 15), 2);
                rs2.setValue(reg_no);

                rd = new Operand();
                rd.setOperandType(OperandType.Register);
                reg_no = Integer.parseInt(instb.substring(15, 20), 2);
                rd.setValue(reg_no);

                instn.setOperationType(operationType[opcodei]);
                instn.setSourceOperand1(rs1);
                instn.setSourceOperand2(rs2);
                instn.setDestinationOperand(rd);
                break;
                case end:
                instn.setOperationType(operationType[opcodei]);
                break;
            case jmp:
                Operand op = new Operand();
                cons = instb.substring(10, 32);
                cons_val = Integer.parseInt(cons, 2);
                if (cons.charAt(0) == '1') {
                    cons = twosComplement(cons);
                    cons_val = Integer.parseInt(cons, 2) * -1;
                }
                if (cons_val != 0) {
                    op.setOperandType(OperandType.Immediate);
                    op.setValue(cons_val);
                } else {
                    reg_no = Integer.parseInt(instb.substring(5, 10), 2);
                    op.setOperandType(OperandType.Register);
                    op.setValue(reg_no);
                }

                instn.setOperationType(operationType[opcodei]);
                instn.setDestinationOperand(op);
                //OperandType OPERNDTYPE = instn.getDestinationOperand().getOperandType();
                //int immediate = 0;
                //if (OPERNDTYPE == OperandType.Register) {
                  //  immediate = containingProcessor.getRegisterFile().getValue(
                  //          instn.getDestinationOperand().getValue());
                //} else {
                  //  immediate = instn.getDestinationOperand().getValue();
                //}
                //int cPC = containingProcessor.getRegisterFile().programCounter - 1;
                //int output = immediate + cPC;
                //containingProcessor.getRegisterFile().setProgramCounter(output);
                    break;
               case beq:
               case bne:
               case blt:
               case bgt:
               case store:
                rs1 = new Operand();
                rs1.setOperandType(OperandType.Register);
                reg_no = Integer.parseInt(instb.substring(5, 10), 2);
                rs1.setValue(reg_no);

                // destination register
                rs2 = new Operand();
                rs2.setOperandType(OperandType.Register);
                reg_no = Integer.parseInt(instb.substring(10, 15), 2);
                rs2.setValue(reg_no);

                // Immediate value
                rd = new Operand();
                rd.setOperandType(OperandType.Immediate);
                cons = instb.substring(15, 32);
                cons_val = Integer.parseInt(cons, 2);
                if (cons.charAt(0) == '1') {
                    cons = twosComplement(cons);
                    cons_val = Integer.parseInt(cons, 2) * -1;
                }
                rd.setValue(cons_val);

                instn.setOperationType(operationType[opcodei]);
                instn.setSourceOperand1(rs1);
                instn.setSourceOperand2(rs2);
                instn.setDestinationOperand(rd);
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
				case load:
                // Source register 1
                rs1 = new Operand();
                rs1.setOperandType(OperandType.Register);
                reg_no = Integer.parseInt(instb.substring(5, 10), 2);
                rs1.setValue(reg_no);

                // Destination register
                rd = new Operand();
                rd.setOperandType(OperandType.Register);
                reg_no = Integer.parseInt(instb.substring(10, 15), 2);
                rd.setValue(reg_no);

                // Immediate values
                rs2 = new Operand();
                rs2.setOperandType(OperandType.Immediate);
                cons = instb.substring(15, 32);
                cons_val = Integer.parseInt(cons, 2);
                if (cons.charAt(0) == '1') {
                    cons = twosComplement(cons);
                    cons_val = Integer.parseInt(cons, 2) * -1;
                }
                rs2.setValue(cons_val);

                instn.setOperationType(operationType[opcodei]);
                instn.setSourceOperand1(rs1);
                instn.setSourceOperand2(rs2);
                instn.setDestinationOperand(rd);
            break;

            default:
                instn.setOperationType(OperationType.nop);
            break;
            }

                OF_EX_Latch.isNop = false;
                OF_EX_Latch.setInstruction(instn);
                OF_EX_Latch.insPC = IF_OF_Latch.insPC;
                OF_EX_Latch.setEX_enable(true);
                IF_EnableLatch.setIF_enable(true);

            if(operation.toString().equals("end")) {
                IF_OF_Latch.setOF_enable(false);
                IF_EnableLatch.setIF_enable(false);
            }
            IF_OF_Latch.setOF_enable(false);
            OF_EX_Latch.setEX_enable(true);
        }
    }

}
