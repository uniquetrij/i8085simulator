package me.uniquetrij.i8085;

public final class RegisterE extends Register
{
    //static Data data=new Data();
    public static final RegisterE RegisterE=new RegisterE();
    private RegisterE(){data=new Data();}

    void update()
    {
        if(super.data!=null)
        {
            data=super.data;
            fire();
        }

        DEpair.DEpair.propagate(new DataEx(RegisterD.RegisterD.data(),data,true));
    }

    void fire()
    {
        propertyChangeSupport.firePropertyChange("value", 4,data);
    }

    public Data data()
    {
        return data;
    }
}
