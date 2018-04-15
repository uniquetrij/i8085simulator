package me.uniquetrij.i8085;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public final class Output
{

    public static final Output Output=new Output();
    private static final PropertyChangeSupport propertyChangeSupport=new PropertyChangeSupport(Output);
    public static final int MAX_CAPACITY=Data.MAX_VALUE+1;
    private Cell[]iounit;
    private Output()
    {
        iounit=new Cell[0x100];
        for(int i=0;i<iounit.length;i++)
            iounit[i]=new Cell();
    }

    public void set(Data address,Data data)
    {
        propertyChangeSupport.firePropertyChange("location", address,data);
        iounit[address.value()].set(data);
    }

    public void reset(Data address)
    {
        propertyChangeSupport.firePropertyChange("location", address,new Data());
        iounit[address.value()].set(new Data());
    }

    public void reset()
    {
        Data address=new Data();
        do
        {
            propertyChangeSupport.firePropertyChange("location", address,new Data());
            iounit[address.value()].set(new Data());
            address=address.plus(1);
        }while(address.value()!=MAX_CAPACITY-1);

    }

    public Data data(Data address)
    {
        return iounit[address.value()].data();
    }

    public void addPropertyChangeListener(final String propertyName,final PropertyChangeListener listener)
    {
        propertyChangeSupport.addPropertyChangeListener(propertyName,listener);
    }

    public void removePropertyChangeListener(final String propertyName,final PropertyChangeListener listener)
    {
        propertyChangeSupport.removePropertyChangeListener(propertyName,listener);
    }
}
