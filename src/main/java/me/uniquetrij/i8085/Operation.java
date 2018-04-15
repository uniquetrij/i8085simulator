package me.uniquetrij.i8085;





public final class Operation
{
    public static final String SYNTAX=""
            /**Data Transfer Instruction Syntax**/
        +"*MOV  <R|M>,<R|M>;2"
        +"*MVI  <R>,<8-bit> | <M>,<8-bit>;2"
        +"*LDA  <16-bit>;1"
        +"*LDAX  <B|D Reg. pair>;1"
        +"*LXI  <B|D|H|SP Reg. pair>,<16-bit>;2"
        +"*LHLD  <16-bit>;1"
        +"*STA  <16-bit>;1"
        +"*STAX  <B|D Reg. pair>;1"
        +"*SHLD  <16-bit>;1"
        +"*XCHG  <none>;0"
        +"*SPHL  <none>;0"
        +"*XTHL  <none>;0"
        +"*PUSH  <B|D|H|PSW Reg. pair>;1"
        +"*POP  <B|D|H|PSW Reg. pair>;1"
        +"*OUT  <8-bit>;1"
        +"*IN  <8-bit>;1"
            /**Arithmetic Instruction Syntax**/
        +"*ADD  <R|M>;1"
        +"*ADI  <R|M>;1"
        +"*ADI  <8-bit>;1"
        +"*ACI  <8-bit>;1"
        +"*DAD  <B|D|H|SP Reg. pair>;1"
        +"*SUB  <R|M>;1"
        +"*SBB  <R|M>;1"
        +"*SUI  <8-bit>;1"
        +"*SBI  <8-bit>;1"
        +"*INR  <R|M>;1"
        +"*INX  <B|D|H|SP Reg. pair>;1"
        +"*DCR  <R|M>;1"
        +"*DCX  <B|D|H|SP Reg. pair>;1"
        +"*DAA  <none>;0"
            /**Branching Instruction Syntax**/
        +"*JMP  <16-bit>;1"
        +"*JC  <16-bit>;1"
        +"*JNC  <16-bit>;1"
        +"*JP  <16-bit>;1"
        +"*JM  <16-bit>;1"
        +"*JZ  <16-bit>;1"
        +"*JNZ  <16-bit>;1"
        +"*JPE  <16-bit>;1"
        +"*JPO  <16-bit>;1"
        +"*CALL  <16-bit>;1"
        +"*CC  <16-bit>;1"
        +"*CNC  <16-bit>;1"
        +"*CP  <16-bit>;1"
        +"*CM  <16-bit>;1"
        +"*CZ  <16-bit>;1"
        +"*CNZ  <16-bit>;1"
        +"*CPE  <16-bit>;1"
        +"*CPO  <16-bit>;1"
        +"*RET  <none>;0"
        +"*RC  <none>;0"
        +"*RNC  <none>;0"
        +"*RP  <none>;0"
        +"*RM  <none>;0"
        +"*RZ  <none>;0"
        +"*RNZ  <none>;0"
        +"*RPE  <none>;0"
        +"*RPO  <none>;0"
        +"*PCHL  <none>;0"
        +"*RST  <0|1|2|3|4|5|6|7>;1"
            /**Logical Instruction Syntax**/
        +"*CMP  <R|M>;1"
        +"*CPI  <8-bit>;1"
        +"*ANA  <R|M>;1"
        +"*ANI  <8-bit>;1"
        +"*XRA  <R|M>;1"
        +"*XRI  <8-bit>;1"
        +"*ORA  <R|M>;1"
        +"*ORI  <8-bit>;1"
        +"*RLC  <none>;0"
        +"*RRC  <none>;0"
        +"*RAL  <none>;0"
        +"*RAR  <none>;0"
        +"*CMA  <none>;0"
        +"*CMC  <none>;0"
        +"*STC  <none>;0"
            /**Control Instruction Syntax**/
        +"*NOP  <none>;0"
        +"*HLT  <none>;0"
        +"*DI  <none>;0"
        +"*EI  <none>;0"
        +"*RIM  <none>;0"
        +"*SIM  <none>;0"
            /**Assembler Directives Syntax**/
        +"*DS  <value>;1"
        +"*DB  <8-bit>;-1"
        +"*DW  <16-bit>;-1"
        +"*EQU  <16-bit>;1"
        +"*SET  <16-bit>;1"
        +"*ORG  <16-bit>;1"
        +"*END  <none>;0"
        +"*";

    private String word;
    public Operation(String word)
    {
        this.word=word;
    }

    public String toString()
    {
        return word;
    }

    public static String syntax(String operation)
    {
        if(!isValid(operation))return null;
        operation=operation.trim();
        operation=SYNTAX.substring(SYNTAX.indexOf("*"+operation+" "));
        return operation.substring(1,operation.indexOf(';'));
    }

    public static boolean isValid(String operation)
    {
        return SYNTAX.indexOf("*"+operation+" ")>=0;
    }

    public static int checkLabel(String label)
    {
        if(Character.isDigit(label.charAt(0)))//bigins with a digit
            return 1;

        if(label.equals("A")||label.equals("B")||label.equals("C")||label.equals("D")||label.equals("E")||label.equals("PC")||label.equals("SP")||label.equals("PSW"))//given label is a register name
            return 2;

        try{new DataEx(label);}//check hex number
        catch(NumberFormatException e){return 0;}//not hex
        //else it is hex
        return 3;
    }

    public static int needOperands(String operation)
    {
        if(!isValid(operation))return -1;
        operation=operation.trim();
        operation=SYNTAX.substring(SYNTAX.indexOf(operation));
        return Integer.parseInt(operation.substring(operation.indexOf(';')+1,operation.indexOf('*')));
    }
}
