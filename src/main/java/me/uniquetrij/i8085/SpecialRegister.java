package me.uniquetrij.i8085;



import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class SpecialRegister
{
    final PropertyChangeSupport propertyChangeSupport=new PropertyChangeSupport(this);
    DataEx data;

    /*
    public SpecialRegister()
    {
        update();
    }

    public SpecialRegister(DataEx data)
    {
        this.data=data;
        update();
    }
    */

    public void set(DataEx data)
    {
        this.data=data;
        update();
    }

    public void propagate(DataEx data)
    {
        this.data=data;
        fire();
    }

    public void reset()
    {
        data=new DataEx();
        update();
    }

    public void addPropertyChangeListener(final String propertyName,final PropertyChangeListener listener)
    {
        propertyChangeSupport.addPropertyChangeListener(propertyName,listener);
    }
    public void removePropertyChangeListener(final String propertyName,final PropertyChangeListener listener)
    {
        propertyChangeSupport.removePropertyChangeListener(propertyName,listener);
    }

    public DataEx data()
    {
        return data;
    }

    void update()
    {
        return;
    }

    void fire()
    {
        return;
    }

    public String toString()
    {
        DataEx data=data();
        String s="";
        for(int i=15;i>=0;i--)
        {
            if((data.value()&(int)Math.pow(2,i))>0)
                s+="D"+i+": 1\n";
            else
                s+="D"+i+": 0\n";
        }
        return s;
    }

    void print()
    {
//        System.out.println(this);
    }

}