package me.uniquetrij.i8085;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public final class Memory
{
    public static final Memory Memory=new Memory();
    private static final PropertyChangeSupport propertyChangeSupport=new PropertyChangeSupport(Memory);
    public static final int MAX_CAPACITY=DataEx.MAX_VALUE+1;
    private Cell[]memory;
    private Memory()
    {
        memory=new Cell[0x10000];
        for(int i=0;i<memory.length;i++)
            memory[i]=new Cell();
    }

    public void set(DataEx address,Data data)
    {
        propertyChangeSupport.firePropertyChange("location", address,data);
        memory[address.value()].set(data);
    }

    public void reset(DataEx address)
    {
        propertyChangeSupport.firePropertyChange("location", address,new Data());
        memory[address.value()].set(new Data());
    }

    public void reset()
    {
        DataEx address=new DataEx();
        do
        {
            propertyChangeSupport.firePropertyChange("location", address,new Data());
            memory[address.value()].set(new Data());
            address=address.plus(1);
        }while(address.value()!=MAX_CAPACITY-1);

    }

    public Data data(DataEx address)
    {
        return memory[address.value()].data();
    }

    public void addPropertyChangeListener(final String propertyName,final PropertyChangeListener listener)
    {
        propertyChangeSupport.addPropertyChangeListener(propertyName,listener);
    }

    public void removePropertyChangeListener(final String propertyName,final PropertyChangeListener listener)
    {
        propertyChangeSupport.removePropertyChangeListener(propertyName,listener);
    }

    public void print()
    {
        System.out.println("        0  1  2  3  4  5  6  7  8  9  A  B  C  D  E  F");
        DataEx address=new DataEx();
        for(int i=0;i<memory.length/16;i++)
        {
            System.out.print(address.toHexString()+":   ");
            for(int j=0;j<16;j++,address=address.plus(1))
                System.out.print(data(address).toHexString()+" ");
            System.out.println();
        }
    }

    public void print(DataEx address)
    {
        address=address.minus(address.value()%16);
        System.out.println("        0  1  2  3  4  5  6  7  8  9  A  B  C  D  E  F");
        for(int i=address.value()/16;i<memory.length/16;i++)
        {
            System.out.print(address.toHexString()+":   ");
            for(int j=0;j<16;j++,address=address.plus(1))
                System.out.print(data(address).toHexString()+" ");
            System.out.println();
        }

    }

    public void print(DataEx addressStart, DataEx addressEnd)
    {
        addressStart=addressStart.minus(addressStart.value()%16);
        System.out.println("        0  1  2  3  4  5  6  7  8  9  A  B  C  D  E  F");
        for(int i=addressStart.value()/16;i<=addressEnd.value()/16;i++)
        {
            System.out.print(addressStart.toHexString()+":   ");
            for(int j=0;j<16;j++,addressStart=addressStart.plus(1))
                System.out.print(data(addressStart).toHexString()+" ");
            System.out.println();
        }
    }

}
