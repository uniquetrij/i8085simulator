package me.uniquetrij.i8085;



public final class Data implements Comparable<Data>
{
    public static final int MAX_VALUE=255;
    public static final int MIN_VALUE=0;
    public static final int SIZE=8;
    private String hex;
    private int value;

    public Data()
    {
        this.value=0;
        this.hex="00";
    }

    public Data(Data data)
    {
        this.value=data.value;
        this.hex=data.hex;
    }

    public Data(String str)
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
            throw new NumberFormatException("8-bit value limit exceeded."
                                           +"\ndec: "+value
                                           +"\nhex: "+Integer.toHexString(value)
                                           +"\nEnter data in range: dec 0 to 255, hex 00 to FF");
        }
        hex=Integer.toHexString(value).toUpperCase();
        hex="00".substring(hex.length())+hex;
    }

    public Data(int value)
    {
        if(value<MIN_VALUE||value>MAX_VALUE)
        {
            throw new NumberFormatException("8-bit value limit exceeded."
                                               +"\ndec: "+value
                                               +"\nhex: "+Integer.toHexString(value)
                                               +"\nEnter data in range: dec 0 to 255, hex 00 to FF");
        }
        this.value=value;
        hex=Integer.toHexString(value).toUpperCase();
        hex="00".substring(hex.length())+hex;
    }

    public int value()
    {
        return value;
    }

    public int hi()
    {
        return Integer.parseInt(hex.substring(0,1),16);
    }

    public int lo()
    {
        return Integer.parseInt(hex.substring(1),16);
    }

    public String toString()
    {
        return hex+'H';
    }

    public Data plus(Data data)
    {
        return new Data((value+data.value)%(MAX_VALUE+1));
    }

    public Data plus(int value)
    {
        return new Data((this.value+value)%(MAX_VALUE+1));
    }

    public Data minus(Data data)
    {
        return new Data((value-data.value+MAX_VALUE+1)%(MAX_VALUE+1));
    }

    public Data minus(int value)
    {
        return new Data((this.value-value+MAX_VALUE+1)%(MAX_VALUE+1));
    }

    public int compareTo(Data data)
    {
        return value-data.value;
    }

    public String toHexString()
    {
        return hex;
    }
}
