package me.uniquetrij.i8085;



public final class DataEx implements Comparable<DataEx>
{
    public static final int MAX_VALUE=65535;
    public static final int MIN_VALUE=0;
    public static final int SIZE=16;
    private String hex;
    private int value;

    public DataEx()
    {
        this.value=0;
        this.hex="0000";
    }

    public DataEx(DataEx data)
    {
        this.value=data.value;
        this.hex=data.hex;
    }

    public DataEx(String str)
    {
        try
        {
            char c=str.charAt(str.length()-1);
            int base=10;
            /*if(c=='h'||c=='H')
            {
                str=str.substring(0,str.length()-1);
                value=Integer.parseInt(str,16);
            }
            else
                value=Integer.parseInt(str);
            */
            switch(c)
            {
                case 'h':
                case 'H':
                    if(base==10)
                        base=16;
                case 'o':
                case 'O':
                    if(base==10)
                        base=8;
                case 'b':
                case 'B':
                    if(base==10)
                        base=2;
                default:
                    if(base!=10)
                        str=str.substring(0,str.length()-1);
            }
            value=Integer.parseInt(str,base);
        }

        catch(NumberFormatException e)
        {
            throw new NumberFormatException("Invalid Data."
                                           +"\nMake sure the digits are valid."
                                           +"\nDec Data may contain digits 0-1 only"
                                           +"\nHex Data may contain digits 0-1 and characters A-F only"
                                           +"\nHex must be appended with 'H' (non case sensitive)");
        }
        if(value<MIN_VALUE||value>MAX_VALUE)
        {
            throw new NumberFormatException("16-bit value limit exceeded."
                                           +"\ndec: "+value
                                           +"\nhex: "+Integer.toHexString(value)
                                           +"\nEnter data in range: dec 0 to 65535, hex 0000 to FFFF");
        }
        hex=Integer.toHexString(value).toUpperCase();
        hex="0000".substring(hex.length())+hex;
    }

    public DataEx(int value)
    {
        if(value<MIN_VALUE||value>MAX_VALUE)
        {
            throw new NumberFormatException("16-bit value limit exceeded."
                                           +"\ndec: "+value
                                           +"\nhex: "+Integer.toHexString(value)
                                           +"\nEnter data in range: dec 0 to 65535, hex 0000 to FFFF");
        }
        this.value=value;
        hex=Integer.toHexString(value).toUpperCase();
        hex="0000".substring(hex.length())+hex;
    }

    public DataEx(Data datahi,Data datalo,boolean hifirst)
    {
        DataEx data=new DataEx(datahi.value()*256+datalo.value());
        value=data.value;
        hex=data.hex;
    }

    public DataEx(Data datalo,Data datahi)
    {
        DataEx data=new DataEx(datahi.toHexString()+datalo.toHexString()+"H");
        value=data.value;
        hex=data.hex;
    }

    public int value()
    {
        return value;
    }

    public String toString()
    {
        return hex+'H';
    }

    public DataEx plus(DataEx data)
    {
        return new DataEx((this.value+data.value)%(MAX_VALUE+1));
    }

    public DataEx plus(int value)
    {
        return new DataEx((this.value+value)%(MAX_VALUE+1));
    }

    public DataEx minus(DataEx data)
    {
        return new DataEx((value-data.value+MAX_VALUE+1)%(MAX_VALUE+1));
    }

    public DataEx minus(int value)
    {
        return new DataEx((this.value-value+MAX_VALUE+1)%(MAX_VALUE+1));
    }

    public Data hi()
    {
        return new Data(hex.substring(0,2)+"h");
    }

    public Data lo()
    {
        return new Data(hex.substring(2)+"h");
    }

    public int compareTo(DataEx data)
    {
        return value-data.value;
    }

    public String toHexString()
    {
        return hex;
    }
}
