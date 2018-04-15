package me.uniquetrij.i8085;






import java.util.StringTokenizer;
public final class InstructionTokenizer
{
    private String[]token=new String[5];//label[0],operation[1],parameterLast[2],parameterFirst[3],comments[4]
    private int[]indices={-1,-1,-1,-1,-1};//indices of the tokens in the instruction (trimmed of extra spaces)
    private int count=5;//count of tokens to determine hasMoreTokens()
    private int index=0;//index of token to return each token in token[] through nextToken()

    //InstructionTokenizer constructor
    public InstructionTokenizer(String instruction)//tokenizes the instruction string
    {
        //instruction=instruction.replace("\t"," ");//replace all tabs with white space

        //obtain the comments, if any
        int ci=instruction.indexOf(';');
        if(ci>=0)//comment present
        {
            token[4]=instruction.substring(ci);
            indices[4]=ci;
        }

        //obtain other tokens
        instruction=(instruction.toUpperCase()+';');//.trim();//appending a ';' for next operation and trimming of all extra spaces
        instruction=instruction.substring(0,instruction.indexOf(';'));//removing all comments including and after';'

        if(instruction.length()==0)//empty instruction line
        {
            token[0]=token[1]=token[2]=token[3]=null;
            return;
        }

        //else instruction exists
        token[1]="";//set operation to empty
        char c='\0';//each character to be obtained from the instruction
        int i=0;//pointer to each character to be obtained from the instruction

        //obtain the first token, may be an operation or a label
        while(i<instruction.length())//consider it to be an operation
        {
            if(!Character.isLetterOrDigit(c=instruction.charAt(i++)))//all characters in operation obtained, c found a non-alphanumeric character
                break;
            //else append
            token[1]+=c;
        }
        indices[1]=0;//says operation exists at the begining

        if(!Operation.isValid(token[1]))//could be a label
        {
            //check whether its a label
            if(c!=':')
                //trim all white spaces before ':' which denotes its a label
                while(i<instruction.length())
                    if(!Character.isWhitespace(c=instruction.charAt(i++)))//all spaces trimmed, c found a new character
                        break;

            if(c!=':')//neither it is a valid label, except for directives EQU or SET
            {
                token[0]="";//set label to empty

                i--;//check from last obtained character
                while(i<instruction.length())//obtain directive
                {
                    if(!Character.isLetterOrDigit(c=instruction.charAt(i++)))//all characters in operation obtained, c found a non-alphanumeric character
                        break;
                    //else append
                    token[0]+=c;
                }

                if(token[0].equals("EQU")||token[0].equals("SET"))//label valid for these directive
                {
                    //set label, directive as operation and their indices
                    String temp=token[0];
                    token[0]=token[1];
                    token[1]=temp;
                    indices[0]=0;
                    indices[1]=i-4;
                }
                else//invalid (label) operation
                    throw new MnemonicException(new Operation(token[1]),0,instruction);//("Unknown Operation: "+token[1]);
            }

            else
            {
                if(token[1].equals(""))//label is empty
                    throw new MnemonicException(instruction,0);//("Undefined Label");

                //check validity of labels
                switch(Operation.checkLabel(token[1]))
                {
                    case 1: throw new MnemonicException(instruction,token[0],0);//("Label Name cannot begin with a digit");
                    case 2: throw new MnemonicException(instruction,0,token[0]);//("Register Name Cannot be used as label");
                    case 3: throw new MnemonicException(instruction,token[0],0);//("Label Name cannot be a hex number");
                }

                //else it is a valid label
                token[0]=token[1];//set as label
                token[1]="";//set operation to empty
                indices[0]=0;//says label exists at the begining
                indices[1]=-1;//says operation is not yet present

                //trim all white spaces before the operation, may be none
                while(i<instruction.length())
                    if(!Character.isWhitespace(c=instruction.charAt(i++)))//all spaces trimmed, c found a new character
                        break;

                if(i==instruction.length())//no operation mentioned in instruction
                {
                    token[1]="NOP";//set to No Operation
                    indices[1]=i;//says operation is at the end of instruction
                    return;//as no parameters mentioned
                }

                //else operation present
                i--;//next check from the last found character

                indices[1]=i;//says operation exists at i

                //now obtain the operation
                while(i<instruction.length())
                {
                    if(!Character.isLetterOrDigit(c=instruction.charAt(i++)))//all characters in operation obtained, c found a non-alphanumeric character
                        break;
                    token[1]+=c;
                }
                if(token[1].equals("EQU")||token[1].equals("SET"))
                    throw new MnemonicException(instruction,new Operation(token[1]),i-token[1].length()-1);//("EQU and SET must be preceeded by a label without a colon");
                if(token[1].equals("ORG"))
                    throw new MnemonicException(0,instruction);//("ORG must not be preceeded by a label");
                if(!Operation.isValid(token[1]))//invalid instruction
                    throw new MnemonicException(new Operation(token[1]),i-token[1].length()-1,instruction);//"Unknown Operation: "+token[1]);
            }
        }
        if(c!=' ')//may contain illegal characters
        {
            if(c==':')throw new MnemonicException(new Operation(token[1]),instruction,i-1);//"Syntax Error.\nKeyword can not be used as Label: "+token[1]);
            if(!Character.isLetterOrDigit(c))throw new MnemonicException(new Operation(token[1]),instruction,c,i-1);//("Syntax Error.\nInvalid Character: "+c);
        }
        if((token[1].equals("EQU")||token[1].equals("SET"))&&token[0]==null)
            throw new MnemonicException(instruction,new Operation(token[1]),i-token[1].length()-1);//("EQU and SET must be preceeded by a label without a colon");

        int operands=Operation.needOperands(token[1]);//no of parameters needed for this operation

        switch(operands)//obtain the parameters
        {
            case 2: c=' ';//initializing if reached to end of instruction ie parameter not present
                    token[3]="";
                    //trim all white spaces before token[3]
                    while(i<instruction.length())
                        if(!Character.isWhitespace(c=instruction.charAt(i++)))//all spaces trimmed, c found a new character
                            break;

                    if(c==' ')//parameter not present
                        throw new MnemonicException(i-1,new Operation(token[1]),instruction);//("Syntax Error.\nExpected: Parameter(s)");

                    if(c==':')//illegal character, denoting an peration a label
                        throw new MnemonicException(new Operation(token[1]),instruction,i-1);//"Syntax Error.\nKeyword can not be used as Label: "+token[1]);

                    i--;//next check from the last found character

                    indices[3]=i;//says parameter exists at i

                    //obtain token[3]
                    while(i<instruction.length())
                    {
                        if(!Character.isLetterOrDigit(c=instruction.charAt(i++)))//all characters in operation obtained, c found a non-alphanumeric character
                            break;
                        token[3]+=c;
                    }

                    if(token[3].equals(""))//parameter is empty
                        throw new MnemonicException(i-1,new Operation(token[1]),instruction);//("Syntax Error.\nExpected: Parameter(s)");

                    i--;//next check from the last found character

                    //trim all white spaces after token[3]
                    while(i<instruction.length())
                        if(!Character.isWhitespace(c=instruction.charAt(i++)))//all spaces trimmed, c found a new character
                            break;

                    if(c!=',')//illegal character, seperation of two parameters
                        throw new MnemonicException(i-1,instruction,new Operation(token[1]));//("Syntax Error.\nExpected: ,");

                    //else everything went fine, procees

            case 1: c=' ';//initializing if reached to end of instruction ie parameter not present
                    token[2]="";
                    //trim all white spaces before token[2]
                    while(i<instruction.length())
                        if(!Character.isWhitespace(c=instruction.charAt(i++)))//all spaces trimmed, c found a new character
                            break;

                    if(c==' ')//parameter not present
                        throw new MnemonicException(i-1,new Operation(token[1]),instruction);//("Syntax Error.\nExpected: Parameter(s)");

                    i--;//next check from the last found character

                    /*if(i<instruction.length())//parameter present
                        i--;//next check from the last found character*/

                    indices[2]=i;//says parameter exists at i

                    //obtain token[2], could be a series of labels
                    while(i<instruction.length())
                    {
                        StringTokenizer stk=new StringTokenizer(instruction.substring(i),"+-",true);
                        boolean flag=false;//for balancing sub-tokens in case more than 1 token is present
                        while(stk.hasMoreTokens())
                        {
                            String s=stk.nextToken();
                            i+=s.length();//finding index
                            int k=labelError(s);//find invalidity index of the token
                            if(k>=0)//if invalidity obtained
                            {
                                flag=true;
                                i=i-s.length()+k+1;//obtain index in instruction
                                break;
                            }
                            else token[2]+=s.trim();
                        }
                        if(flag==true)//sub-tokens are unbalanced
                            throw new MnemonicException(instruction,i-1,new Operation(token[1]));//("Syntax Error.\nContains: Extra Token(s)");
                    }
                    //System.out.println(token[2]);
                    if(token[2].equals(""))//parameter is empty
                        throw new MnemonicException(i-1,new Operation(token[1]),instruction);//("Syntax Error.\nExpected: Parameter(s)");

            case 0: c=' ';//initializing if reached to end of instruction
                    //trim all trailing white spaces
                    if(c==' ')
                        while(i<instruction.length())
                            if(!Character.isWhitespace(c=instruction.charAt(i++)))//illegal extra token present
                                break;

                    if(!Character.isWhitespace(c))
                    {
                        if(token[1].equals("END"))
                        {
                            i--;
                            indices[4]=i;
                            token[4]="";
                            while(i<instruction.length())
                                token[4]+=instruction.charAt(i++);
                        }
                        else
                            throw new MnemonicException(instruction,i-1,new Operation(token[1]));//("Syntax Error.\nContains: Extra Token(s)");
                    }
                    break;
            default: StringTokenizer stk=new StringTokenizer(instruction.substring(i)," ,");
                     token[2]="";//instruction.substring(i);
                     while(stk.hasMoreTokens())
                     {
                         String s=stk.nextToken();
                         token[2]+=s+",";
                     }

        }

    }
    private int labelError(String s)//checks a series of labels concatinated by +/-
    {
        if(s.length()==1)
        {
            char c=s.charAt(0);
            if(c=='-'||c=='+')return -1;
        }
        String temp=s.trim();
        int i=s.length()-((s+';').trim().length()-1);
        boolean flag=false;
        for(int j=0;j<temp.length();j++)
        {
            char c=temp.charAt(j);
            if(!Character.isLetterOrDigit(c))
                if(!Character.isWhitespace(c))
                    return i+j;
                else
                    flag=true;
            else
                if(flag==true)
                    return i+j;
        }
        return -1;
    }
    public String nextToken()
    {
        if(index>=count)throw new java.util.NoSuchElementException();
        return token[index++];
    }
    public int countTokens()
    {
        return count-index;
    }
    public boolean hasMoreTokens()
    {
        return index<count;
    }
    public int indexOfWord(int i)
    {
        return indices[i];
    }
}
