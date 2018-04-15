package me.uniquetrij.i8085;

public final class HLpair extends SpecialRegister
{
    //static DataEx data=new DataEx(RegisterH.data,RegisterL.data);
    public static final HLpair HLpair=new HLpair();
    private HLpair(){data=new DataEx();}

    void update()
    {
        if(super.data!=null)
        {
            data=super.data;
            fire();
        }

        RegisterH.RegisterH.propagate(data.hi());
        RegisterL.RegisterL.propagate(data.lo());
    }
    void fire()
    {
        propertyChangeSupport.firePropertyChange("value", 9,data=super.data);
    }
    public DataEx data()
    {
        return data;
    }
}
