package me.uniquetrij.i8085;

public final class RegisterH extends Register
{
    //static Data data=new Data();
    public static final RegisterH RegisterH=new RegisterH();
    private RegisterH(){data=new Data();}

    void update()
    {
        if(super.data!=null)
        {
            data=super.data;
            fire();
        }

        HLpair.HLpair.propagate(new DataEx(data,RegisterL.RegisterL.data(),true));
    }
    void fire()
    {
        propertyChangeSupport.firePropertyChange("value", 5,data);
    }
    public Data data()
    {
        return data;
    }
}
