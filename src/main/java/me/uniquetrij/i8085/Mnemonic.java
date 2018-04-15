package me.uniquetrij.i8085;






import java.util.NoSuchElementException;
import java.util.StringTokenizer;
public final class Mnemonic
{
    private Data[]dataList;//opcode/data array equivalence of this mnemonic
    private String[]word;//word list of this mnemonic

    public Mnemonic()
    {
        Data[]dataList=null;
        word=null;
    }

    public Mnemonic(String instruction,String labels,int lncount)
    {
        //instruction=instruction.replace("\t"," ").trim();
        InstructionTokenizer words;//each word in the instruction
        try
        {
            words=new InstructionTokenizer(instruction);//tokenize instruction
        }
        catch(MnemonicException e)//error occured while tokenizing
        {
            throw e;
        }
        //else tokenization was successful

        String t0=words.nextToken(),t1="",t2="",t3="";//label[0],operation[1],parameter2[2],parameter1[3]

        if(t0!=null)
            if(labels.indexOf('*'+t0+':')>=0)
                throw new MnemonicException(instruction,t0);//("Label Already Defined");
            else
                labels=Assambler.appendLabel(t0,Assambler.getAddress().toString());

        int opcode=0;//dataList value of the operation
        Data d=new Data();//for 8-bit parameter
        DataEx dx=new DataEx();//ffor 16-bit parameter
        while(words.hasMoreTokens())//there are more words
        {
            try
            {
                switch(t1=words.nextToken())//obtain the operation and switch it
                {

                    /**Data Transfer Instruction Mnemonics**/
                    case "MOV":
                    {
                        switch(t2=words.nextToken())
                        {
                            case "B":   opcode=0x0;break;

                            case "C":   opcode=0x1;break;

                            case "D":   opcode=0x2;break;

                            case "E":   opcode=0x3;break;

                            case "H":   opcode=0x4;break;

                            case "L":   opcode=0x5;break;

                            case "M":   opcode=0x6;break;

                            case "A":   opcode=0x7;break;

                            default :   throw new MnemonicException(new Operation(t1),instruction,t2,words.indexOfWord(2));
                        }
                        switch(t3=words.nextToken())
                        {
                            case "B":   opcode+=0x40;break;

                            case "C":   opcode+=0x48;break;

                            case "D":   opcode+=0x50;break;

                            case "E":   opcode+=0x58;break;

                            case "H":   opcode+=0x60;break;

                            case "L":   opcode+=0x68;break;

                            case "M":   opcode+=0x70;break;

                            case "A":   opcode+=0x78;break;

                            default :   throw new MnemonicException(new Operation(t1),instruction,t3,words.indexOfWord(3));
                        }
                        dataList=new Data[]{new Data(opcode)};
                        word=new String[]{t0,t1,t3,t2,words.nextToken()};
                    }break;

                    case "MVI":
                    {
                        d=dLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        switch(t3=words.nextToken())
                        {
                            case "B":   opcode=0x06;break;

                            case "C":   opcode=0x0e;break;

                            case "D":   opcode=0x16;break;

                            case "E":   opcode=0x1e;break;

                            case "H":   opcode=0x26;break;

                            case "L":   opcode=0x2e;break;

                            case "M":   opcode=0x36;break;

                            case "A":   opcode=0x3e;break;

                            default :   throw new MnemonicException(new Operation(t1),instruction,t3,words.indexOfWord(3));
                        }
                        dataList=new Data[]{new Data(opcode),d};
                        word=new String[]{t0,t1,t3,t2,words.nextToken()};
                    }break;

                    case "LDA":
                    {
                        opcode=0x3a;

                        dx=dxLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        dataList=new Data[]{new Data(opcode),dx.lo(),dx.hi()};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "LDAX":
                    {
                        switch(t2=words.nextToken())
                        {
                            case "B":   opcode=0x0a;break;

                            case "D":   opcode=0x1a;break;

                            case "PC":   case "SP":   case "PSW": case "H":   throw new MnemonicException(new Operation(t1),instruction,words.indexOfWord(2),t2);

                            default :   throw new MnemonicException(new Operation(t1),instruction,t2,words.indexOfWord(2));
                        }
                        dataList=new Data[]{new Data(opcode)};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "LXI":
                    {
                        dx=dxLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        switch(t3=words.nextToken())
                        {
                            case "B":   opcode=0x01;break;

                            case "D":   opcode=0x11;break;

                            case "H":   opcode=0x21;break;

                            case "SP":  opcode=0x31;break;

                            case "PC":   case "PSW": throw new MnemonicException(new Operation(t1),instruction,words.indexOfWord(2),t2);

                            default :   throw new MnemonicException(new Operation(t1),instruction,t3,words.indexOfWord(3));
                        }
                        dataList=new Data[]{new Data(opcode),dx.lo(),dx.hi()};
                        word=new String[]{t0,t1,t3,t2,words.nextToken()};
                    }break;

                    case "LHLD":
                    {
                        opcode=0x2a;

                        dx=dxLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        dataList=new Data[]{new Data(opcode),dx.lo(),dx.hi()};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "STA":
                    {
                        opcode=0x32;

                        dx=dxLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        dataList=new Data[]{new Data(opcode),dx.lo(),dx.hi()};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "STAX":
                    {
                        switch(t2=words.nextToken())
                        {
                            case "B":   opcode=0x02;break;

                            case "D":   opcode=0x12;break;

                            case "PC":   case "SP":   case "PSW": case "H":   throw new MnemonicException(new Operation(t1),instruction,words.indexOfWord(2),t2);

                            default :   throw new MnemonicException(new Operation(t1),instruction,t2,words.indexOfWord(2));
                        }
                        dataList=new Data[]{new Data(opcode)};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "SHLD":
                    {
                        opcode=0x22;

                        dx=dxLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        dataList=new Data[]{new Data(opcode),dx.lo(),dx.hi()};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "XCHG":
                    {
                        dataList=new Data[]{new Data(0xeb)};
                        word=new String[]{t0,t1,words.nextToken(),words.nextToken(),words.nextToken()};
                    }break;

                    case "SPHL":
                    {
                        dataList=new Data[]{new Data(0xf9)};
                        word=new String[]{t0,t1,words.nextToken(),words.nextToken(),words.nextToken()};
                    }break;

                    case "XTHL":
                    {
                        dataList=new Data[]{new Data(0xe3)};
                        word=new String[]{t0,t1,words.nextToken(),words.nextToken(),words.nextToken()};
                    }break;

                    case "PUSH":
                    {
                        switch(t2=words.nextToken())
                        {
                            case "B":   opcode=0xc5;break;

                            case "D":   opcode=0xd5;break;

                            case "H":   opcode=0xe5;break;

                            case "A":   case "PSW": opcode=0xf5;break;

                            case "PC":   case "SP": throw new MnemonicException(new Operation(t1),instruction,words.indexOfWord(2),t2);

                            default :   throw new MnemonicException(new Operation(t1),instruction,t2,words.indexOfWord(2));
                        }
                        dataList=new Data[]{new Data(opcode)};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "POP":
                    {
                        switch(t2=words.nextToken())
                        {
                            case "B":   opcode=0xc1;break;

                            case "D":   opcode=0xd1;break;

                            case "H":   opcode=0xe1;break;

                            case "A":   case "PSW": opcode=0xf1;break;

                            case "PC":   case "SP": throw new MnemonicException(new Operation(t1),instruction,words.indexOfWord(2),t2);

                            default :   throw new MnemonicException(new Operation(t1),instruction,t2,words.indexOfWord(2));
                        }
                        dataList=new Data[]{new Data(opcode)};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "OUT":
                    {
                        opcode=0xd3;

                        d=dLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        dataList=new Data[]{new Data(opcode),d};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "IN":
                    {
                        opcode=0xdb;

                        d=dLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        dataList=new Data[]{new Data(opcode),d};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    /**Arithmatic Instruction Mnemonics**/

                    case "ADD":
                    {
                        switch(t2=words.nextToken())
                        {
                            case "B":   opcode+=0x80;break;

                            case "C":   opcode+=0x81;break;

                            case "D":   opcode+=0x82;break;

                            case "E":   opcode+=0x83;break;

                            case "H":   opcode+=0x84;break;

                            case "L":   opcode+=0x85;break;

                            case "M":   opcode+=0x86;break;

                            case "A":   opcode+=0x87;break;

                            default :   throw new MnemonicException(new Operation(t1),instruction,t2,words.indexOfWord(2));
                        }
                        dataList=new Data[]{new Data(opcode)};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "ADC":
                    {
                        switch(t2=words.nextToken())
                        {
                            case "B":   opcode+=0x88;break;

                            case "C":   opcode+=0x89;break;

                            case "D":   opcode+=0x8a;break;

                            case "E":   opcode+=0x8b;break;

                            case "H":   opcode+=0x8c;break;

                            case "L":   opcode+=0x8d;break;

                            case "M":   opcode+=0x8e;break;

                            case "A":   opcode+=0x8f;break;

                            default :   throw new MnemonicException(new Operation(t1),instruction,t2,words.indexOfWord(2));
                        }
                        dataList=new Data[]{new Data(opcode)};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "ADI":
                    {
                        opcode=0xc6;

                        d=dLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        dataList=new Data[]{new Data(opcode),d};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "ACI":
                    {
                        opcode=0xce;

                        d=dLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        dataList=new Data[]{new Data(opcode),d};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "DAD":
                    {
                        switch(t2=words.nextToken())
                        {
                            case "B":   opcode=0x09;break;

                            case "D":   opcode=0x19;break;

                            case "H":   opcode=0x29;break;

                            case "SP":  opcode=0x39;break;

                            case "PC":   case "PSW": throw new MnemonicException(new Operation(t1),instruction,words.indexOfWord(2),t2);

                            default :   throw new MnemonicException(new Operation(t1),instruction,t2,words.indexOfWord(2));
                        }
                        dataList=new Data[]{new Data(opcode)};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "SUB":
                    {
                        switch(t2=words.nextToken())
                        {
                            case "B":   opcode+=0x90;break;

                            case "C":   opcode+=0x91;break;

                            case "D":   opcode+=0x92;break;

                            case "E":   opcode+=0x93;break;

                            case "H":   opcode+=0x94;break;

                            case "L":   opcode+=0x95;break;

                            case "M":   opcode+=0x96;break;

                            case "A":   opcode+=0x97;break;

                            default :   throw new MnemonicException(new Operation(t1),instruction,t2,words.indexOfWord(2));
                        }
                        dataList=new Data[]{new Data(opcode)};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "SBB":
                    {
                        switch(t2=words.nextToken())
                        {
                            case "B":   opcode+=0x98;break;

                            case "C":   opcode+=0x99;break;

                            case "D":   opcode+=0x9a;break;

                            case "E":   opcode+=0x9b;break;

                            case "H":   opcode+=0x9c;break;

                            case "L":   opcode+=0x9d;break;

                            case "M":   opcode+=0x9e;break;

                            case "A":   opcode+=0x9f;break;

                            default :   throw new MnemonicException(new Operation(t1),instruction,t2,words.indexOfWord(2));
                        }
                        dataList=new Data[]{new Data(opcode)};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "SUI":
                    {
                        opcode=0xd6;

                        d=dLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        dataList=new Data[]{new Data(opcode),d};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "SBI":
                    {
                        opcode=0xde;

                        d=dLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        dataList=new Data[]{new Data(opcode),d};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "INR":
                    {
                        switch(t2=words.nextToken())
                        {
                            case "B":   opcode+=0x04;break;

                            case "C":   opcode+=0x0c;break;

                            case "D":   opcode+=0x14;break;

                            case "E":   opcode+=0x1c;break;

                            case "H":   opcode+=0x24;break;

                            case "L":   opcode+=0x2c;break;

                            case "M":   opcode+=0x34;break;

                            case "A":   opcode+=0x3c;break;

                            default :   throw new MnemonicException(new Operation(t1),instruction,t2,words.indexOfWord(2));
                        }
                        dataList=new Data[]{new Data(opcode)};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "INX":
                    {
                        switch(t2=words.nextToken())
                        {
                            case "B":   opcode=0x03;break;

                            case "D":   opcode=0x13;break;

                            case "H":   opcode=0x23;break;

                            case "SP":  opcode=0x33;break;

                            case "PC":   case "PSW": throw new MnemonicException(new Operation(t1),instruction,words.indexOfWord(2),t2);

                            default :   throw new MnemonicException(new Operation(t1),instruction,t2,words.indexOfWord(2));
                        }
                        dataList=new Data[]{new Data(opcode)};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "DCR":
                    {
                        switch(t2=words.nextToken())
                        {
                            case "B":   opcode+=0x05;break;

                            case "C":   opcode+=0x0d;break;

                            case "D":   opcode+=0x15;break;

                            case "E":   opcode+=0x1d;break;

                            case "H":   opcode+=0x25;break;

                            case "L":   opcode+=0x2d;break;

                            case "M":   opcode+=0x35;break;

                            case "A":   opcode+=0x3d;break;

                            default :   throw new MnemonicException(new Operation(t1),instruction,t2,words.indexOfWord(2));
                        }
                        dataList=new Data[]{new Data(opcode)};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "DCX":
                    {
                        switch(t2=words.nextToken())
                        {
                            case "B":   opcode=0x0b;break;

                            case "D":   opcode=0x1b;break;

                            case "H":   opcode=0x2b;break;

                            case "SP":  opcode=0x3b;break;

                            case "PC":   throw new MnemonicException(new Operation(t1),instruction,words.indexOfWord(2),t2);

                            default :   throw new MnemonicException(new Operation(t1),instruction,t2,words.indexOfWord(2));
                        }
                        dataList=new Data[]{new Data(opcode)};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "DAA":
                    {
                        dataList=new Data[]{new Data(0x27)};
                        word=new String[]{t0,t1,words.nextToken(),words.nextToken(),words.nextToken()};
                    }break;

                    /**Branching Instruction Mnemonics**/

                    case "JMP":
                    {
                        opcode=0xc3;

                        dx=dxLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        dataList=new Data[]{new Data(opcode),dx.lo(),dx.hi()};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "JC":
                    {
                        opcode=0xda;

                        dx=dxLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        dataList=new Data[]{new Data(opcode),dx.lo(),dx.hi()};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "JNC":
                    {
                        opcode=0xd2;

                        dx=dxLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        dataList=new Data[]{new Data(opcode),dx.lo(),dx.hi()};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "JP":
                    {
                        opcode=0xf2;

                        dx=dxLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        dataList=new Data[]{new Data(opcode),dx.lo(),dx.hi()};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "JM":
                    {
                        opcode=0xfa;

                        dx=dxLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        dataList=new Data[]{new Data(opcode),dx.lo(),dx.hi()};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "JZ":
                    {
                        opcode=0xca;

                        dx=dxLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        dataList=new Data[]{new Data(opcode),dx.lo(),dx.hi()};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "JNZ":
                    {
                        opcode=0xc2;

                        dx=dxLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        dataList=new Data[]{new Data(opcode),dx.lo(),dx.hi()};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "JPE":
                    {
                        opcode=0xea;

                        dx=dxLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        dataList=new Data[]{new Data(opcode),dx.lo(),dx.hi()};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "JPO":
                    {
                        opcode=0xe2;

                        dx=dxLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        dataList=new Data[]{new Data(opcode),dx.lo(),dx.hi()};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "CALL":
                    {
                        opcode=0xcd;

                        dx=dxLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        dataList=new Data[]{new Data(opcode),dx.lo(),dx.hi()};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "CC":
                    {
                        opcode=0xdc;

                        dx=dxLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        dataList=new Data[]{new Data(opcode),dx.lo(),dx.hi()};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "CNC":
                    {
                        opcode=0xd4;

                        dx=dxLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        dataList=new Data[]{new Data(opcode),dx.lo(),dx.hi()};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "CP":
                    {
                        opcode=0xf4;

                        dx=dxLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        dataList=new Data[]{new Data(opcode),dx.lo(),dx.hi()};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "CM":
                    {
                        opcode=0xfc;

                        dx=dxLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        dataList=new Data[]{new Data(opcode),dx.lo(),dx.hi()};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "CZ":
                    {
                        opcode=0xcc;

                        dx=dxLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        dataList=new Data[]{new Data(opcode),dx.lo(),dx.hi()};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "CNZ":
                    {
                        opcode=0xc4;

                        dx=dxLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        dataList=new Data[]{new Data(opcode),dx.lo(),dx.hi()};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "CPE":
                    {
                        opcode=0xec;

                        dx=dxLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        dataList=new Data[]{new Data(opcode),dx.lo(),dx.hi()};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "CPO":
                    {
                        opcode=0xe4;

                        dx=dxLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        dataList=new Data[]{new Data(opcode),dx.lo(),dx.hi()};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "RET":
                    {
                        opcode=0xc9;
                        dataList=new Data[]{new Data(opcode)};
                        word=new String[]{t0,t1,words.nextToken(),words.nextToken(),words.nextToken()};
                    }break;

                    case "RC":
                    {
                        opcode=0xd8;
                        dataList=new Data[]{new Data(opcode)};
                        word=new String[]{t0,t1,words.nextToken(),words.nextToken(),words.nextToken()};
                    }break;

                    case "RNC":
                    {
                        opcode=0xd0;
                        dataList=new Data[]{new Data(opcode)};
                        word=new String[]{t0,t1,words.nextToken(),words.nextToken(),words.nextToken()};
                    }break;

                    case "RP":
                    {
                        opcode=0xf0;
                        dataList=new Data[]{new Data(opcode)};
                        word=new String[]{t0,t1,words.nextToken(),words.nextToken(),words.nextToken()};
                    }break;

                    case "RM":
                    {
                        opcode=0xf8;
                        dataList=new Data[]{new Data(opcode)};
                        word=new String[]{t0,t1,words.nextToken(),words.nextToken(),words.nextToken()};
                    }break;

                    case "RZ":
                    {
                        opcode=0xc8;
                        dataList=new Data[]{new Data(opcode)};
                        word=new String[]{t0,t1,words.nextToken(),words.nextToken(),words.nextToken()};
                    }break;

                    case "RNZ":
                    {
                        opcode=0xc0;
                        dataList=new Data[]{new Data(opcode)};
                        word=new String[]{t0,t1,words.nextToken(),words.nextToken(),words.nextToken()};
                    }break;

                    case "RPE":
                    {
                        opcode=0xe8;
                        dataList=new Data[]{new Data(opcode)};
                        word=new String[]{t0,t1,words.nextToken(),words.nextToken(),words.nextToken()};
                    }break;

                    case "RPO":
                    {
                        opcode=0xe0;
                        dataList=new Data[]{new Data(opcode)};
                        word=new String[]{t0,t1,words.nextToken(),words.nextToken(),words.nextToken()};
                    }break;

                    case "PCHL":
                    {
                        dataList=new Data[]{new Data(0xe9)};
                        word=new String[]{t0,t1};
                    }break;

                    case "RST":
                    {
                        switch(t2=words.nextToken())
                        {
                            case "0":   opcode=0xc7;break;
                            case "1":   opcode=0xcf;break;
                            case "2":   opcode=0xd7;break;
                            case "3":   opcode=0xdf;break;
                            case "4":   opcode=0xe7;break;
                            case "5":   opcode=0xef;break;
                            case "6":   opcode=0xf7;break;
                            case "7":   opcode=0xff;break;
                        }
                        dataList=new Data[]{new Data(opcode)};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    /**Logical Instruction Mnemonics**/

                    case "CMP":
                    {
                        switch(t2=words.nextToken())
                        {
                            case "B":   opcode+=0xb8;break;

                            case "C":   opcode+=0xb9;break;

                            case "D":   opcode+=0xba;break;

                            case "E":   opcode+=0xbb;break;

                            case "H":   opcode+=0xbc;break;

                            case "L":   opcode+=0xbd;break;

                            case "M":   opcode+=0xbe;break;

                            case "A":   opcode+=0xbf;break;

                            default :   throw new MnemonicException(new Operation(t1),instruction,t2,words.indexOfWord(2));
                        }
                        dataList=new Data[]{new Data(opcode)};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "CPI":
                    {
                        opcode=0xfe;

                        d=dLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        dataList=new Data[]{new Data(opcode),d};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "ANA":
                    {
                        switch(t2=words.nextToken())
                        {
                            case "B":   opcode+=0xa0;break;

                            case "C":   opcode+=0xa1;break;

                            case "D":   opcode+=0xa2;break;

                            case "E":   opcode+=0xa3;break;

                            case "H":   opcode+=0xa4;break;

                            case "L":   opcode+=0xa5;break;

                            case "M":   opcode+=0xa6;break;

                            case "A":   opcode+=0xa7;break;

                            default :   throw new MnemonicException(new Operation(t1),instruction,t2,words.indexOfWord(2));
                        }
                        dataList=new Data[]{new Data(opcode)};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "ANI":
                    {
                        opcode=0xe6;

                        d=dLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        dataList=new Data[]{new Data(opcode),d};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "XRA":
                    {
                        switch(t2=words.nextToken())
                        {
                            case "B":   opcode+=0xa8;break;

                            case "C":   opcode+=0xa9;break;

                            case "D":   opcode+=0xaa;break;

                            case "E":   opcode+=0xab;break;

                            case "H":   opcode+=0xac;break;

                            case "L":   opcode+=0xad;break;

                            case "M":   opcode+=0xae;break;

                            case "A":   opcode+=0xaf;break;

                            default :   throw new MnemonicException(new Operation(t1),instruction,t2,words.indexOfWord(2));
                        }
                        dataList=new Data[]{new Data(opcode)};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "XRI":
                    {
                        opcode=0xee;

                        d=dLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        dataList=new Data[]{new Data(opcode),d};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "ORA":
                    {
                        switch(t2=words.nextToken())
                        {
                            case "B":   opcode+=0xb0;break;

                            case "C":   opcode+=0xb1;break;

                            case "D":   opcode+=0xb2;break;

                            case "E":   opcode+=0xb3;break;

                            case "H":   opcode+=0xb4;break;

                            case "L":   opcode+=0xb5;break;

                            case "M":   opcode+=0xb6;break;

                            case "A":   opcode+=0xb7;break;

                            default :   throw new MnemonicException(new Operation(t1),instruction,t2,words.indexOfWord(2));
                        }
                        dataList=new Data[]{new Data(opcode)};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "ORI":
                    {
                        opcode=0xf6;

                        d=dLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);

                        dataList=new Data[]{new Data(opcode),d};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "RLC":
                    {
                        dataList=new Data[]{new Data(0x07)};
                        word=new String[]{t0,t1,words.nextToken(),words.nextToken(),words.nextToken()};
                    }break;

                    case "RRC":
                    {
                        dataList=new Data[]{new Data(0x0f)};
                        word=new String[]{t0,t1,words.nextToken(),words.nextToken(),words.nextToken()};
                    }break;

                    case "RAL":
                    {
                        dataList=new Data[]{new Data(0x17)};
                        word=new String[]{t0,t1,words.nextToken(),words.nextToken(),words.nextToken()};
                    }break;

                    case "RAR":
                    {
                        dataList=new Data[]{new Data(0x1f)};
                        word=new String[]{t0,t1,words.nextToken(),words.nextToken(),words.nextToken()};
                    }break;

                    case "CMA":
                    {
                        dataList=new Data[]{new Data(0x2f)};
                        word=new String[]{t0,t1,words.nextToken(),words.nextToken(),words.nextToken()};
                    }break;

                    case "CMC":
                    {
                        dataList=new Data[]{new Data(0x3f)};
                        word=new String[]{t0,t1,words.nextToken(),words.nextToken(),words.nextToken()};
                    }break;

                    case "STC":
                    {
                        dataList=new Data[]{new Data(0x37)};
                        word=new String[]{t0,t1,words.nextToken(),words.nextToken(),words.nextToken()};
                    }break;

                    /**Control Instruction Mnemonics**/

                    case "NOP":
                    {
                        dataList=new Data[]{new Data()};
                        word=new String[]{t0,t1,words.nextToken(),words.nextToken(),words.nextToken()};
                    }break;

                    case "HLT":
                    {
                        dataList=new Data[]{new Data(0x76)};
                        word=new String[]{t0,t1,words.nextToken(),words.nextToken(),words.nextToken()};
                    }break;

                    case "DI":
                    {
                        dataList=new Data[]{new Data(0xf3)};
                        word=new String[]{t0,t1,words.nextToken(),words.nextToken(),words.nextToken()};
                    }break;

                    case "EI":
                    {
                        dataList=new Data[]{new Data(0xfb)};
                        word=new String[]{t0,t1,words.nextToken(),words.nextToken(),words.nextToken()};
                    }break;

                    case "RIM":
                    {
                        dataList=new Data[]{new Data(0x20)};
                        word=new String[]{t0,t1,words.nextToken(),words.nextToken(),words.nextToken()};
                    }break;

                    case "SIM":
                    {
                        dataList=new Data[]{new Data(0x30)};
                        word=new String[]{t0,t1,words.nextToken(),words.nextToken(),words.nextToken()};
                    }break;

                    /**Assembler Directives**/

                    case "DS":
                    {
                        dataList=new Data[new DataEx(t2=words.nextToken()).value()];
                        for(int i=0;i<dataList.length;i++)dataList[i]=new Data();
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "DB":
                    {
                        //dataList=new Data[]{dLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false)};
                        StringTokenizer stk=new StringTokenizer(t2=words.nextToken(),",");
                        int i=0;
                        int n=0;
                        dataList=new Data[stk.countTokens()];
                        while(stk.hasMoreTokens())
                        {
                            String s=stk.nextToken();
                            n=instruction.indexOf(s);
                            dataList[i++]=dLabels(instruction,t1,s,words.indexOfWord(2)+n,labels,lncount,false);
                        }
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "DW":
                    {
                        /*dx=dxLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);
                        dataList=new Data[]{dx.lo(),dx.hi()};
                        */
                        StringTokenizer stk=new StringTokenizer(t2=words.nextToken(),",");
                        int i=0;
                        int n=0;
                        dataList=new Data[stk.countTokens()*2];
                        while(stk.hasMoreTokens())
                        {
                            String s=stk.nextToken();
                            n=instruction.indexOf(s);
                            dx=dxLabels(instruction,t1,s,words.indexOfWord(2)+n,labels,lncount,false);
                            dataList[i++]=dx.lo();
                            dataList[i++]=dx.hi();
                        }
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "EQU":
                    {
                        Assambler.appendLabel(sLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels));
                        dataList=new Data[]{};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "SET":
                    {
                        Assambler.appendLabel(dxLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false).toString());
                        dataList=new Data[]{};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "ORG":
                    {
                        dx=dxLabels(instruction,t1,t2=words.nextToken(),words.indexOfWord(2)-1,labels,lncount,false);
                        Assambler.setOrigin(dx);
                        dataList=new Data[]{};
                        word=new String[]{t0,t1,words.nextToken(),t2,words.nextToken()};
                    }break;

                    case "END":
                    {
                        dataList=new Data[]{new Data(0xed)};
                        word=new String[]{t0,t1,words.nextToken(),words.nextToken(),words.nextToken()};
                    }break;

                    default:    throw new MnemonicException("Undefined Operation: "+t1);
                }
            }
            catch(NullPointerException e)
            {
                word=new String[]{t0,t1,words.nextToken(),words.nextToken(),words.nextToken()};
            }
        }
    }

    public Data dLabels(String instruction,String t1,String t2,int count1,String labels,int lncount,boolean endProcess)
    {
        if(Character.isLetterOrDigit(t2.charAt(0)))t2='+'+t2;//prepend sign to the first token
        int index=count1+1;
        StringTokenizer label=new StringTokenizer(t2,"+-",true);//tokenize at every sign index
        int value=0;//total value of all the tokens
        int sign=1;//sign of token being processed
        String s;//token to be processed
        //int count1=words.indexOfWord(2)-1;//for indexing in case of throwing errors
        int count2=0;//length of last token processed
        try//processing the labels
        {
            while(label.hasMoreTokens())//process the tokens
            {
                //determe sign of the token
                if(label.nextToken().equals("-"))
                    sign=-1;
                else
                    sign=1;

                s=label.nextToken();//obtain the following token

                //determine indices of the token
                count1+=s.length()+1;
                count2=s.substring(s.indexOf(s=s.trim())).length();

                try//considering the token as a value
                {
                    value+=new Data(s).value()*sign;
                }
                catch(NumberFormatException e)//token is not a value, consider it to be a label
                {
                    if(Operation.checkLabel(s)!=0)//not a valid label
                        throw e;

                    s='*'+s+':';//format label syntax as in labels list
                    if(labels.indexOf(s)>=0)//label present in labels list
                    {
                        //obtain the value of the label from the labels list
                        s=labels.substring(labels.indexOf(s)+1);
                        s=s.substring(s.indexOf(':')+1,s.indexOf('*'));

                        value+=new Data(s).value()*sign;//update value
                    }
                    else//label not present in labels list
                    {
                        if(endProcess==true)
                            throw new MnemonicException(count1-count2,instruction,s.substring(1,s.length()-1));//("Undefined Label"+s);
                        Assambler.appendUnknownLabel(instruction,t1,t2.substring(1),index-1,lncount+Assambler.getLineCountOffset(),1);
                        return new Data();
                    }

                }
            }
            return new Data(value);//value obtained properly
        }
        catch(NumberFormatException|NoSuchElementException e)//error occured while processing tokens, especially while returning the value
        {
            throw new MnemonicException(new Operation(t1),instruction,t2,index,e);
        }
    }

    public DataEx dxLabels(String instruction,String t1,String t2,int count1,String labels,int lncount,boolean endProcess)
    {
        if(Character.isLetterOrDigit(t2.charAt(0)))t2="+"+t2;//prepend sign to the first token
        int index=count1+1;
        StringTokenizer label=new StringTokenizer(t2,"+-",true);//tokenize at every sign index
        int value=0;//total value of all the tokens
        int sign=1;//sign of token being processed
        String s;//token to be processed
        //int count1=words.indexOfWord(2)-1;//for indexing in case of throwing errors
        int count2=0;//length of last token processed
        try//processing the labels
        {
            while(label.hasMoreTokens())//process the tokens
            {
                //determe sign of the token
                if(label.nextToken().equals("-"))
                    sign=-1;
                else
                    sign=1;

                s=label.nextToken();//obtain the following token

                //determine indices of the token
                count1+=s.length()+1;
                count2=s.substring(s.indexOf(s=s.trim())).length();

                try//considering the token as a value
                {
                    value+=new DataEx(s).value()*sign;
                }
                catch(NumberFormatException e)//token is not a value, consider it to be a label
                {
                    if(Operation.checkLabel(s)!=0)//not a valid label
                        throw e;

                    s='*'+s+':';//format label syntax as in labels list
                    if(labels.indexOf(s)>=0)//label present in labels list
                    {
                        //obtain the value of the label from the labels list
                        s=labels.substring(labels.indexOf(s)+1);
                        s=s.substring(s.indexOf(':')+1,s.indexOf('*'));

                        value+=new DataEx(s).value()*sign;//update value
                    }
                    else//label not present in labels list
                    {
                        if(endProcess==true)
                            throw new MnemonicException(count1-count2,instruction,s.substring(1,s.length()-1));//("Undefined Label"+s);
                        Assambler.appendUnknownLabel(instruction,t1,t2.substring(1),index-1,lncount+Assambler.getLineCountOffset(),2);
                        return new DataEx();
                    }
                }
            }
            return new DataEx(value);//value obtained properly
        }
        catch(NumberFormatException|NoSuchElementException e)//error occured while processing tokens, especially while returning the value
        {
            throw new MnemonicException(new Operation(t1),instruction,t2,index,e);
        }
        catch(Exception e)//error occured while processing tokens, especially while returning the value
        {
            throw e;
        }
    }

    private String sLabels(String instruction,String t1,String t2,int count1,String labels)
    {
        if(Character.isLetterOrDigit(t2.charAt(0)))t2='+'+t2;//prepend sign to the first token
        int index=count1+1;
        StringTokenizer label=new StringTokenizer(t2,"+-",true);//tokenize at every sign index
        int value=0;//total value of all the tokens
        int sign=1;//sign of token being processed
        String s;//token to be processed
        //int count1=words.indexOfWord(2)-1;//for indexing in case of throwing errors
        int count2=0;//length of last token processed
        try//processing the labels
        {
            while(label.hasMoreTokens())//process the tokens
            {
                //determe sign of the token
                if(label.nextToken().equals("-"))
                    sign=-1;
                else
                    sign=1;

                s=label.nextToken();//obtain the following token

                //determine indices of the token
                count1+=s.length()+1;
                count2=s.substring(s.indexOf(s=s.trim())).length();

                try//considering the token as a value
                {
                    if(s.charAt(s.length()-1)=='H')//it is a hex value
                        value+=Integer.parseInt(s.substring(0,s.length()-1),16)*sign;
                    else//it is dec value
                        value+=Integer.parseInt(s)*sign;
                }
                catch(NumberFormatException e)//token is not a value, consider it to be a label
                {
                    s='*'+s+':';//format label syntax as in labels list
                    if(labels.indexOf(s)>=0)//label present in labels list
                    {
                        //obtain the value of the label from the labels list
                        s=labels.substring(labels.indexOf(s)+1);
                        s=s.substring(s.indexOf(':')+1,s.indexOf('*'));

                        if(s.charAt(s.length()-1)=='H')//it is a hex value
                            value+=Integer.parseInt(s.substring(0,s.length()-1).trim())*sign;
                        else//it is dec value
                            value+=Integer.parseInt(s.trim())*sign;
                    }
                    else//label not present in labels list
                        throw new MnemonicException(count1-count2,instruction,s.substring(1,s.length()-1));//("Undefined Label"+s);
                }
            }
            //return Integer.toHexString(val)+'H';
            return Integer.toString(value);//value obtained properly
        }
        catch(NumberFormatException e)//error occured while processing tokens, especially while returning the value
        {
            throw new MnemonicException(new Operation(t1),instruction,t2,index,e);
        }
    }


    public Data[]getDataList()
    {
        return dataList;
    }

    @Override
    public String toString()
    {
        String mnemonic="";
        if(word[1]!=null)
            mnemonic+=word[1]+" ";
        if(word[0]!=null)
        {
            if(mnemonic.equals("EQU ")||mnemonic.equals("SET "))
                mnemonic=word[0]+"  "+mnemonic;
            else
                mnemonic=word[0]+": "+mnemonic;
        }
        if(word[2]!=null)
            mnemonic+=word[2]+',';
        if(word[3]!=null)
            mnemonic+=word[3]+' ';
        if(word[4]!=null)
        {
            StringTokenizer stk=new StringTokenizer(word[4]);
            mnemonic+=stk.nextToken();
            while(stk.hasMoreTokens())mnemonic+=' '+stk.nextToken();
        }
        return mnemonic;
    }
    public void print(){
//        System.out.println(toString());
    }
}