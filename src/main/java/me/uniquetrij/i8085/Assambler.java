package me.uniquetrij.i8085;


import java.util.StringTokenizer;
public final class Assambler
{
    private static int lncount;//count of lines in sourcecode
    private static int offset;
    private static int opcount;//count of operations in sourcecode
    private static int bytcount;//count of memory bytes needed for the sourcecode
    private static String clist;//list of data/opcode equivalence of sourcecode. FORMAT: <address><:><opcode> <<data lo> <data hi>>
    private static String clistwidth;//width of clist as prepended in each line of list
    private static String labelwidth;//max width of any label used in sourcecode
    private static String mlist;//list of mnemonic equivalence the sourcecode. FORMAT: <label> <:> <operation> <<operand><,><operand>>
    private static String list;//list of all mnemonic, data/opcode and addresses equivalence of sourcecode. FORMAT: <<address>: <opcode> <<data lo> <data hi>>>  <<label> <:> <operation> <<operand><,><operand>>>
    private static String addrlist;
    private static String labels;//labels used in sourcecode. FORMAT: <*><label><:><address><*>
    private static String unklabels;//list of unknown labels, which are declared later but used earlier in the sourcecode. FORMAT: <*><instruction><:><operation><:><label><:><index><:><lncount><:><byte 1/2><*>";
    private static DataEx addr;//address where each instruction of the program is to be loaded
    private static DataEx origin;//origin of the program
    private static boolean assambled;//says whether the sourcecode is assambled or not
    private static boolean orgset;//to check origin set or not

    public Assambler()
    {
        //to initialize all class variables with default values
        lncount=offset=opcount=bytcount=0;
        labelwidth=list=clist=mlist="";
        clistwidth="                     ";
        labels=unklabels=addrlist="*";
        addr=new DataEx();
        origin=new DataEx();
        orgset=false;
        assambled=false;
    }

    public static void assamble(String sourcecode)
    {
        boolean endset=false;//to check terminating conditions set or not

        StringTokenizer lines=new StringTokenizer(sourcecode,"\n",true);//break sourcecode into lines
        String line="";//each line of sourcecode is a mnemonic
        Mnemonic mnemonic=new Mnemonic();//break each line into mnemonic words

        while(lines.hasMoreTokens())//process each mnemonic
        {
            lncount++;//lines processed
            try//consider each line as mnemonic
            {
                line=lines.nextToken();//obtain a line of sourcecode and remove all extra white spaces
                mnemonic=new Mnemonic(line.replace("\t"," ").trim(),labels,lncount);//convert the line into a mnemonic
            }
            catch(MnemonicException e)//line couldnot be converted, invalid mnemonic
            {
                throw new AssamblerException(lncount,e);//notify this error
            }
            //else it is a valid mnemonic

            Data[]dataList=mnemonic.getDataList();//obtain the data list equivalence of the mnemonic

            if(dataList==null)//may be comment line with no operation
            {
                //skip this line
                clist+=" \n";
                mlist+=mnemonic+" \n";

                if(lines.hasMoreTokens()&&!line.equals("\n"))lines.nextToken();//remove "\n" returned as a token at end of each line and check for the next mnemonic
                continue;
            }

            if(dataList.length==1)//check END of assembly
            {
                if(dataList[0].value()==0xed)
                {
                    endset=true;//END of assembly
                    clist+=" \n";//no data part
                    mlist+=mnemonic+"\n";//add the mnemonic

                    //now append all comments after END
                    line="";//will contain all lines after END
                    while(lines.hasMoreTokens())
                        line+=labelwidth+clistwidth+lines.nextToken();

                    break;//file processing complete
                }
            }

            //update lists in respective proper formats
            if(dataList.length>0)//dataList exists
            {
                opcount++;
                clist+=addr.toHexString()+": ";
                addrlist+=lncount+":"+addr+"*";
            }

            //append the mnemonic
            mlist+=mnemonic+"\n";
            for(int i=0;i<dataList.length;i++)
            {
                if(bytcount++>Memory.MAX_CAPACITY)//exceed memory limits
                    throw new AssamblerException(lncount,line);//("Program couldnot be loaded at Origin: "+addr+"\nExceed memory limits")
                //else space available

                //append data/opcode
                clist+=dataList[i].toHexString()+" ";
                addr=addr.plus(1);//next data/opcode will be inserted into next address
                if((i+1)%3==0&&i<dataList.length-1)//data/opcode contains more than 3 tokens, append 3 to each line
                {
                    clist=clist+"\n"+addr.toHexString()+": ";
                    mlist+=" \n";
                    offset++;
                }
            }
            clist+=" \n";

            if(lines.hasMoreTokens())lines.nextToken();//remove "\n" returned as a token at end of each line
            orgset=true;
        }

        if(endset==false)
            throw new AssamblerException(lncount);//("END not set");

        //resolve unknown labels if possible, in case labels were declared later but used earlier in the sourcecode
        clist=resolveUnknownLabels();

        //generate list
        boolean shblanks=false;//whether blank lines will be shown
        StringTokenizer stcl=new StringTokenizer(clist,"\n");
        StringTokenizer stml=new StringTokenizer(mlist,"\n");
        while(stml.hasMoreTokens())
        {
            //tokenize the lists
            String m=stml.nextToken();
            String c=stcl.nextToken();

            if(c.trim().length()==0&&m.trim().length()==0)//blank line
            {
                if(shblanks==true)
                {
                    list+=" \n";
                    shblanks=false;
                }
                continue;
            }
            else shblanks=true;

            int i=m.indexOf(": ");
            c+=clistwidth.substring(c.length());
            m=labelwidth.substring(i>0?i+2:0)+m;
            list+=c+m+"\n";
        }
        list+=line;//append all comment lines after END

        assambled=true;//says assably completed successfully
    }

    public static String appendLabel(String label,String value)//to update a new label and its address value
    {
        labels+=label+':'+value+'*';

        while(labelwidth.length()<label.length()+2)
            labelwidth+=" ";

        return labels;
    }

    public static void appendLabel(String value)//to propagate the parameter value to a newly appended label for EQU and SET directives
    {
        labels=labels.substring(0,labels.lastIndexOf(':')+1);
        labels+=value+'*';
    }

    public static void appendUnknownLabel(String instruction,String operation,String label,int index,int lncount,int words)//to add an unknown label and its attributes
    {
        unklabels+=instruction+":"+operation+":"+label+":"+index+":"+lncount+":"+words+"*";
    }

    private static String resolveUnknownLabels()//to update the unknown labels
    {
        StringTokenizer stcl=new StringTokenizer(clist,"\n");
        clist="";
        StringTokenizer stu=new StringTokenizer(unklabels,"*");
        int i=1;
        while(stu.hasMoreTokens())
        {
            StringTokenizer stul=new StringTokenizer(stu.nextToken(),":");
            String instruction=stul.nextToken(),t1=stul.nextToken(),t2=stul.nextToken();
            int count1=Integer.parseInt(stul.nextToken()),lncount=Integer.parseInt(stul.nextToken()),bytes=Integer.parseInt(stul.nextToken());
            while(i<lncount)
            {
                clist+=stcl.nextToken()+"\n";
                i++;
            }

            try
            {
                if(bytes==1)//8 bit data
                {
                    Data d=new Mnemonic().dLabels(instruction,t1,t2,count1,labels,lncount,true);
                    String s=stcl.nextToken();
                    s=s.substring(0,9);//remove "<address><:><opcode>" and use the <<data>> part of opcode/data list which is to be updated
                    s+=d.toHexString();
                    clist+=s+"\n";
                    i++;
                }
                else//16 bit data
                {
                    DataEx dx=new Mnemonic().dxLabels(instruction,t1,t2,count1,labels,lncount,true);
                    String s=stcl.nextToken();
                    s=s.substring(0,9);//remove "<address><:><opcode>" and use the <<data><><data>> part of opcode/data list which is to be updated
                    s+=dx.lo().toHexString()+" "+dx.hi().toHexString();
                    clist+=s+"\n";
                    i++;
                }
            }
            catch(MnemonicException e)
            {
                throw new AssamblerException(lncount,e);//notify this error
            }
        }
        while(stcl.hasMoreTokens())//append the remaining
            clist+=stcl.nextToken()+"\n";

        return clist;
    }

    public static void setBrakePoint(String label)
    {
        if(Operation.checkLabel(label)==0)
        {
            int index;
            if((index=labels.indexOf(label.trim()))>=0)
            {
                label=labels.substring(index);
                label=label.substring(label.indexOf(":")+1,label.indexOf("*"));
            }
            else
                throw new AssamblerException("Label not found: "+label);
        }
        try
        {
            Simulator.setBrakePoint(new DataEx(label));
        }
        catch(NumberFormatException e)
        {
            throw new AssamblerException("Invalid brake point address: "+label);
        }
    }

    public static void setBrakePoint(int lncount)
    {
        String address=addrlist.substring(addrlist.indexOf("*"+lncount+":")+1);
        address=address.substring(address.indexOf(":")+1,address.indexOf("*"));
        setBrakePoint(address);
    }

    public static void setOrigin(DataEx address)
    {
        addr=address;
        if(orgset==false)
        {
            origin=address;
            orgset=true;
        }
    }
    public static DataEx getOrigin(){return origin;}
    public static DataEx getAddress(){return addr;}

    public static String getCodeList(){return clist;}
    public static String getMnemonicList(){return mlist;}
    public static String getList(){return list;}
    public static int getLineCount(){return lncount;}
    public static int getLineCountOffset(){return offset;}
    public static int getOperationCount(){return opcount;}
    public static int getByteCount(){return bytcount;}
    public static String getAddressLinesList(){return addrlist;}
    public static boolean isOrgSet(){return mlist.indexOf("ORG")==0;}

}
