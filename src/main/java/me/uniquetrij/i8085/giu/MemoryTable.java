package me.uniquetrij.i8085.giu;

import me.uniquetrij.i8085.Data;
import me.uniquetrij.i8085.DataEx;
import me.uniquetrij.i8085.Memory;
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

@SuppressWarnings("serial")
public final class MemoryTable extends JScrollPane implements Editable,Serializable
{
    //<editor-fold defaultstate="collapsed" desc="instance members">
    private JTable memtable;
    private JTable addrtable;
    private String[] memheader;
    private Object[][] memdata;
    private Object[][] addrdata;
    private Memory memory;
    private DataEx address;
    private boolean working;
    private boolean editable;
    private boolean viewable;
    //</editor-fold>

    public MemoryTable()
    {
        this(13);
    }

    public MemoryTable(int fontsize)
    {
        memory=Memory.Memory;
        memory.addPropertyChangeListener("location",new PropertyChangeListener()
        {

            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                setCellData((DataEx)evt.getOldValue(),(Data)evt.getNewValue());
            }
        });

        generateTableParameters();

        DefaultTableCellRenderer cellrenderer=new DefaultTableCellRenderer();
        cellrenderer.setHorizontalAlignment(JLabel.CENTER);

        memtable=new JTable(memdata,memheader)
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
                    jc.setToolTipText(new DataEx(rowIndex*16+colIndex).toHexString()+" Hex - Memory Location");

                    if(!isCellSelected(rowIndex,colIndex))
                        if((rowIndex+colIndex)%2!=0)
                            jc.setBackground(new Color(250,250,250));
                        else if((rowIndex+colIndex)%2==0&&rowIndex%2!=0)
                            jc.setBackground(new Color(240,240,240));
                        else
                            jc.setBackground(Color.WHITE);
                }
                return c;
            }
        };
        memtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        memtable.setFont(new Font(Font.MONOSPACED,0,fontsize));
        memtable.getTableHeader().setResizingAllowed(false);
        memtable.getTableHeader().setFont(new Font(Font.MONOSPACED,Font.BOLD,fontsize));
        memtable.getTableHeader().setBorder(BorderFactory.createRaisedBevelBorder());
        for(int i=0;i<memheader.length;i++)
        {
            TableColumn column=memtable.getColumnModel().getColumn(i);
            column.setPreferredWidth(fontsize+15);
            column.setCellRenderer(cellrenderer);
        }
        cellrenderer=(DefaultTableCellRenderer)memtable.getTableHeader().getDefaultRenderer();
        cellrenderer.setHorizontalAlignment(JLabel.CENTER);
        memtable.getTableHeader().setDefaultRenderer(cellrenderer);
        memtable.getTableHeader().setEnabled(false);
        memtable.addMouseListener(new ClickListener()
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
        memtable.setCursor(new Cursor(Cursor.HAND_CURSOR));
        memtable.setColumnSelectionAllowed(true);
        memtable.setRowSelectionAllowed(true);
        memtable.setRowHeight(fontsize+12);
        memtable.setBorder(new MatteBorder(0,0,1,1,Color.BLACK));
        memtable.getTableHeader().addMouseListener(new ClickListener()
        {

            @Override
            public void singleClick(MouseEvent e)
            {
            }

            @Override
            public void doubleClick(MouseEvent e)
            {
                goToCellDialog();
            }

            @Override
            public void rightClick(MouseEvent evt)
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });

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
                    jc.setFont(memtable.getTableHeader().getFont());
                }
                return component;
            }
        };
        addrtable.setBackground(memtable.getTableHeader().getBackground());
        addrtable.setBorder(memtable.getTableHeader().getBorder());
        addrtable.setEnabled(false);
        addrtable.setFont(new Font(Font.MONOSPACED,Font.BOLD,fontsize));
        addrtable.getTableHeader().setResizingAllowed(false);
        addrtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        addrtable.getColumnModel().getColumn(0).setPreferredWidth(60);
        addrtable.getColumnModel().getColumn(0).setCellRenderer(cellrenderer);
        addrtable.setBorder(BorderFactory.createRaisedBevelBorder());
        addrtable.getTableHeader().setBorder(BorderFactory.createRaisedBevelBorder());
        addrtable.setRowHeight(memtable.getRowHeight());
        addrtable.getColumnModel().getColumn(0).setCellRenderer(cellrenderer);
        addrtable.addMouseListener(new ClickListener()
        {

            @Override
            public void singleClick(MouseEvent e)
            {
            }

            @Override
            public void doubleClick(MouseEvent e)
            {
                goToCellDialog();
            }

            @Override
            public void rightClick(MouseEvent evt)
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });

        JPanel margin=new JPanel();
        margin.setBorder(new MatteBorder(-5,-5,0,-5,Color.BLACK));
        margin.add(addrtable);

        setViewportView(memtable);
        setRowHeaderView(margin);
        setCorner(JScrollPane.UPPER_LEFT_CORNER,addrtable.getTableHeader());
        setCorner(JScrollPane.LOWER_LEFT_CORNER,null);
        setCorner(JScrollPane.UPPER_RIGHT_CORNER,null);
        getViewport().addChangeListener(new ChangeListener()
        {

            @Override
            public void stateChanged(ChangeEvent e)
            {
                MouseEvent me=new MouseEvent(memtable,0,0,0,-1,-1,0,false);
                for(MouseMotionListener ml:memtable.getMouseMotionListeners())
                    ml.mouseMoved(me);
            }
        });

        memtable.addFocusListener(new FocusListener()
        {

            @Override
            public void focusGained(FocusEvent e)
            {
            }

            @Override
            public void focusLost(FocusEvent e)
            {
                if(!working)
                    memtable.clearSelection();
            }
        });
    }

    private void generateTableParameters()
    {
        working=false;
        editable=viewable=true;

        memheader=new String[0x10];
        for(int i=0;i<memheader.length;i++)
            memheader[i]=Integer.toHexString(i).toUpperCase();

        memdata=new String[0x1000][0x10];
        for(int i=0;i<memdata.length;i++)
            for(int j=0;j<memdata[i].length;j++)
                memdata[i][j]="00";

        addrdata=new Object[0x1000][1];
        for(int i=0;i<addrdata.length;i++)
        {
            String temp=Integer.toHexString(i*0x10).toUpperCase();
            temp="0000".substring(temp.length())+temp;
            addrdata[i][0]=temp;
        }
    }

    private void displayCellDialog()
    {
        address=new DataEx(memtable.getSelectedRow()*0x10+memtable.getSelectedColumn());
        new ValueDisplayer("Memory Location: "+address.toHexString()+" Hex",memory.data(address).value(),editable,this);
        working=false;
    }

    private void editCellDialog(boolean addrset)
    {
        if(!addrset)
            address=new DataEx(memtable.getSelectedRow()*0x10+memtable.getSelectedColumn());
        new ValueEditor("Memory Location: "+address.toHexString()+" Hex",memory.data(address).value(),false,this);
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
        memory.set(address,data);
    }

    @Override
    public void setValue(DataEx data)
    {
        goToCell(data);
    }

    private void setCellData(DataEx address,Data data)
    {
        int row=address.value()/0x10;
        int col=address.value()%0x10;
        memdata[row][col]=data.toHexString();
        ((AbstractTableModel)memtable.getModel()).fireTableCellUpdated(row,col);
    }

    public void setEditable(boolean editable)
    {
        this.editable=editable;
    }

    public void setViewable(boolean viewable)
    {
        this.viewable=viewable;
    }

    public void goToCell(DataEx address)
    {
        int row=address.value()/0x10;
        int col=address.value()%0x10;
        //memtable.scrollRectToVisible(memtable.getCellRect(row+0x10, 0, true));
        memtable.changeSelection(row,col,false,false);
    }

    public void goToCellDialog()
    {
        new ValueEditor("Go To Memory @ Address",0x8000,true,this);
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
            java.util.logging.Logger.getLogger(MemoryTable.class.getName()).log(java.util.logging.Level.SEVERE,null,ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable()
        {

            @Override
            public void run()
            {
                MemoryTable mt=new MemoryTable(13);
                Memory.Memory.set(new DataEx(0x0011),new Data(10));

                JFrame frame=new JFrame();

                frame.add(mt);
                frame.setSize(600,700);
                frame.setVisible(true);


            }
        });

    }
}