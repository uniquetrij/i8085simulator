package me.uniquetrij.i8085;

public final class RegisterL extends Register
{
    //static Data data=new Data();
    public static final RegisterL RegisterL=new RegisterL();
    private RegisterL(){data=new Data();}

    void update()
    {
        if(super.data!=null)
        {
            data=super.data;
            fire();
        }

        HLpair.HLpair.propagate(new DataEx(RegisterH.RegisterH.data(),data,true));
    }

    void fire()
    {
        propertyChangeSupport.firePropertyChange("value", 6,data);
    }

    public Data data()
    {
        return new Data(data);
    }
}
