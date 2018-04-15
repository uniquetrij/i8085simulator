package me.uniquetrij.i8085;

public final class StackPointer extends SpecialRegister
{
    //static DataEx data=new DataEx();
    public static final StackPointer StackPointer=new StackPointer();
    private StackPointer(){data=new DataEx();}

    void update()
    {
        if(super.data!=null)
            propertyChangeSupport.firePropertyChange("value", 11,data=super.data);
            //data=super.data;
    }

    public DataEx data()
    {
        return data;
    }

    public void push(DataEx data)
    {
        set(StackPointer.data.minus(1));
        Memory.Memory.set(StackPointer.data,data.hi());
        set(StackPointer.data.minus(1));
        Memory.Memory.set(StackPointer.data,data.lo());
    }

    public DataEx pop()
    {
        if(data.value()==DataEx.MIN_VALUE)
        {
            throw new RuntimeException("Stack Empty");
        }
        Data lo=Memory.Memory.data(data);
        set(StackPointer.data.plus(1));
        Data hi=Memory.Memory.data(data);
        set(StackPointer.data.plus(1));
        return new DataEx(hi,lo,true);
    }

    public DataEx peek()
    {
        if(data.value()==DataEx.MIN_VALUE)return null;
        Data lo=Memory.Memory.data(data);
        Data hi=Memory.Memory.data(new DataEx(data.value()+1));
        return new DataEx(hi,lo);
    }

}
