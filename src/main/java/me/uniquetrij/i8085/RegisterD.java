package me.uniquetrij.i8085;

public final class RegisterD extends Register
{
    //static Data data=new Data();
    public static final RegisterD RegisterD=new RegisterD();
    private RegisterD(){data=new Data();}

    void update()
    {
        if(super.data!=null)
        {
            data=super.data;
            fire();
        }

        DEpair.DEpair.propagate(new DataEx(data,RegisterE.RegisterE.data(),true));
    }

    void fire()
    {
        propertyChangeSupport.firePropertyChange("value", 3,data);
    }

    public Data data()
    {
        return data;
    }
}
