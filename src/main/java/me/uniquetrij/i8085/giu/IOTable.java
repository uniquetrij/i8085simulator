package me.uniquetrij.i8085.giu;

import me.uniquetrij.i8085.Data;
import me.uniquetrij.i8085.DataEx;
import me.uniquetrij.i8085.IOunit;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author Uniquetrij
 */
@SuppressWarnings("serial")
public final class IOTable extends JScrollPane implements Editable,Serializable
{
    //<editor-fold defaultstate="collapsed" desc="instance members">

    private JTable iotable;
    private JTable addrtable;
    private String[] ioheader;
    private Object[][] iodata;
    private Object[][] addrdata;
    private IOunit iounit;
    private Data address;

    private boolean working;
    private boolean editable;
    private boolean viewable;
    private boolean goTo;

    public IOTable(){this(13);}

    public IOTable(int fontsize)
    {
        iounit=IOunit.IOunit;
        iounit.addPropertyChangeListener("location",new PropertyChangeListener()
        {

            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                setCellData((Data)evt.getOldValue(),(Data)evt.getNewValue());
            }
        });

        generateTableParameters();

        DefaultTableCellRenderer cellrenderer=new DefaultTableCellRenderer();
        cellrenderer.setHorizontalAlignment(JLabel.CENTER);

        iotable=new JTable(iodata,ioheader)
        {

            @Override
            public boolean isCellEditable(int rowIndex,int colIndex)
            {
                return false;
            }
            /*
             * @Override
             * public String getToolTipText(MouseEvent evt)
             * {
             * Point p=evt.getPoint();
             * DataEx address=new
             * DataEx(convertRowIndexToModel(rowAtPoint(p))*0x10+convertColumnIndexToModel(columnAtPoint(p)));
             * return address.toHexString();
             * }
             *
             */

            @Override
            public Component prepareRenderer(TableCellRenderer renderer,int rowIndex,int colIndex)
            {
                Component c=super.prepareRenderer(renderer,rowIndex,colIndex);
                if(c instanceof JComponent)
                {
                    JComponent jc=(JComponent)c;
                    jc.setToolTipText(new Data(rowIndex*16+colIndex).toHexString()+" Hex - I/O Location");


                    if(!isCellSelected(rowIndex,colIndex))
                    {
                        if((rowIndex+colIndex)%2!=0)
                            jc.setBackground(new Color(250,250,250));
                        else if((rowIndex+colIndex)%2==0&&rowIndex%2!=0)
                            jc.setBackground(new Color(240,240,240));
                        else
                            jc.setBackground(Color.WHITE);
                    }
                }
                return c;
            }
        };
        iotable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        iotable.setFont(new Font(Font.MONOSPACED,0,fontsize));
        iotable.getTableHeader().setResizingAllowed(false);
        iotable.getTableHeader().setFont(new Font(Font.MONOSPACED,Font.BOLD,fontsize));
        iotable.getTableHeader().setBorder(BorderFactory.createRaisedBevelBorder());
        for(int i=0;i<ioheader.length;i++)
        {
            TableColumn column=iotable.getColumnModel().getColumn(i);
            column.setPreferredWidth(fontsize+15);
            column.setCellRenderer(cellrenderer);
        }
        cellrenderer=(DefaultTableCellRenderer)iotable.getTableHeader().getDefaultRenderer();
        cellrenderer.setHorizontalAlignment(JLabel.CENTER);
        iotable.getTableHeader().setDefaultRenderer(cellrenderer);
        iotable.getTableHeader().setEnabled(false);

        iotable.addMouseListener(new ClickListener()
        {

            @Override
            public void singleClick(MouseEvent e)
            {
                if(viewable)
                {
                    working=true;
                    displayCellDialog();
                }
            }

            @Override
            public void doubleClick(MouseEvent e)
            {
                if(editable)
                {
                    working=true;
                    editCellDialog(false);
                }
            }

            @Override
            public void rightClick(MouseEvent evt)
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        iotable.setCursor(new Cursor(Cursor.HAND_CURSOR));
        iotable.setColumnSelectionAllowed(true);
        iotable.setRowSelectionAllowed(true);
        iotable.setRowHeight(fontsize+12);
        iotable.setBorder(new MatteBorder(0,0,1,1,Color.BLACK));

        addrtable=new JTable(addrdata,new String[]
                {
                    "Address"
                })
        {
            /*
             * @Override
             * public String getToolTipText(MouseEvent evt)
             * {
             * return "Address";
             * }
             */

            @Override
            public Component prepareRenderer(TableCellRenderer renderer,int rowIndex,int colIndex)
            {
                Component component=super.prepareRenderer(renderer,rowIndex,colIndex);
                if(component instanceof JComponent)
                {
                    JComponent jc=(JComponent)component;
                    jc.setToolTipText("Address");
                    jc.setFont(iotable.getTableHeader().getFont());
                }
                return component;
            }
        };
        addrtable.setBackground(iotable.getTableHeader().getBackground());
        addrtable.setBorder(iotable.getTableHeader().getBorder());
        addrtable.setEnabled(false);
        addrtable.setFont(new Font(Font.MONOSPACED,Font.BOLD,fontsize));
        addrtable.getTableHeader().setResizingAllowed(false);
        addrtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        addrtable.getColumnModel().getColumn(0).setPreferredWidth(60);
        addrtable.getColumnModel().getColumn(0).setCellRenderer(cellrenderer);
        addrtable.setBorder(BorderFactory.createRaisedBevelBorder());
        addrtable.getTableHeader().setBorder(BorderFactory.createRaisedBevelBorder());
        addrtable.setRowHeight(iotable.getRowHeight());
        addrtable.getColumnModel().getColumn(0).setCellRenderer(cellrenderer);


        JPanel margin=new JPanel();
        margin.setBorder(new MatteBorder(-5,-5,0,-5,Color.BLACK));
        margin.add(addrtable);

        setViewportView(iotable);
        setRowHeaderView(margin);
        setCorner(JScrollPane.UPPER_LEFT_CORNER,addrtable.getTableHeader());
        getViewport().addChangeListener(new ChangeListener()
        {

            @Override
            public void stateChanged(ChangeEvent e)
            {
                MouseEvent me=new MouseEvent(iotable,0,0,0,-1,-1,0,false);
                for(MouseMotionListener ml:iotable.getMouseMotionListeners())
                    ml.mouseMoved(me);
            }
        });

        iotable.addFocusListener(new FocusListener(){

            @Override
            public void focusGained(FocusEvent e)
            {

            }

            @Override
            public void focusLost(FocusEvent e)
            {
                if(!working)
                    iotable.clearSelection();
            }
        });

    }

    private void generateTableParameters()
    {
        working=false;
        editable=viewable=true;
        ioheader=new String[0x10];
        for(int i=0;i<ioheader.length;i++)
            ioheader[i]=Integer.toHexString(i).toUpperCase();

        iodata=new String[0x10][0x10];
        for(int i=0;i<iodata.length;i++)
            for(int j=0;j<iodata[i].length;j++)
                iodata[i][j]="00";

        addrdata=new Object[0x10][1];
        for(int i=0;i<addrdata.length;i++)
        {
            String temp=Integer.toHexString(i*0x10).toUpperCase();
            temp="00".substring(temp.length())+temp;
            addrdata[i][0]=temp;
        }
    }

    private void displayCellDialog()
    {
        goTo=false;
        address=new Data(iotable.getSelectedRow()*0x10+iotable.getSelectedColumn());
        new ValueDisplayer("I/O Location: "+address.toHexString()+" Hex",iounit.data(address).value(),editable,this);
        working=false;
    }

    private void editCellDialog(boolean addrset)
    {
        goTo=false;
        if(!addrset)
            address=new Data(iotable.getSelectedRow()*0x10+iotable.getSelectedColumn());
        new ValueEditor("I/O Location: "+address.toHexString()+" Hex",iounit.data(address).value(),false,this);
        working=false;

    }

    @Override
    public void editValue()
    {
        editCellDialog(true);
    }

    @Override
    public void setValue(Data data)
    {
        //setCellData(address,data);
        iounit.set(address,data);
    }

    @Override
    public void setValue(DataEx data)
    {
    }

    private void setCellData(Data address,Data data)
    {
        int row=address.value()/0x10;
        int col=address.value()%0x10;
        iodata[row][col]=data.toHexString();
        ((AbstractTableModel) iotable.getModel()).fireTableCellUpdated(row, col);
    }

    public void setEditable(boolean editable)
    {
        this.editable=editable;
    }

    public void setViewable(boolean viewable)
    {
        this.viewable=viewable;
    }

    public static void main(String args[])
    {
        try
        {
            for(javax.swing.UIManager.LookAndFeelInfo info:javax.swing.UIManager.getInstalledLookAndFeels())
                if("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
        }
        catch(ClassNotFoundException|InstantiationException|IllegalAccessException|javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(IOTable.class.getName()).log(java.util.logging.Level.SEVERE,null,ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable()
        {

            @Override
            public void run()
            {
                IOTable it=new IOTable(13);
                IOunit.IOunit.set(new Data(0x11),new Data(10));
                JFrame frame=new JFrame();
                frame.add(it);
                frame.setSize(600,700);
                frame.setVisible(true);
            }
        });

    }
}