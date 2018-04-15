package me.uniquetrij.i8085.giu;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Uniquetrij
 */
@SuppressWarnings("serial")
public class ValueDisplayer  extends JDialog
{
    private String title;
    private int value;
    private boolean editable;
    private DisplayPanel displayPanel;
    private Editable owner;
    public ValueDisplayer(String title,int value,boolean editable,Editable owner)
    {
        this.editable=editable;
        this.value=value;
        this.title="Display - "+title;
        this.owner=owner;
        initComponents();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents()
    {
        displayPanel=new DisplayPanel();
        javax.swing.GroupLayout layout=new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(displayPanel,javax.swing.GroupLayout.PREFERRED_SIZE,javax.swing.GroupLayout.DEFAULT_SIZE,javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(0,Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE).addComponent(displayPanel,javax.swing.GroupLayout.PREFERRED_SIZE,javax.swing.GroupLayout.DEFAULT_SIZE,javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));


        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0),"close");
        getRootPane().getActionMap().put("close",new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                dispose();
            }
        });
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),"edit");
        getRootPane().getActionMap().put("edit",new AbstractAction()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                displayPanel.editActionPerformed(e);
            }
        });

        setTitle(title);
        setResizable(false);
        pack();
        setLocationRelativeTo((Component)owner);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        //setAlwaysOnTop(true);
        setModal(true);
        setVisible(true);
        //setLocationRelativeTo(caller);
    }// </editor-fold>

    private class DisplayPanel extends javax.swing.JLayeredPane
    {
        /**
         * Creates new form DisplayPanel
         */
        //<editor-fold defaultstate="collapsed" desc="Instance Members">
        private javax.swing.JButton edit;
        private javax.swing.JTextField fieldBin;
        private javax.swing.JTextField fieldDec;
        private javax.swing.JTextField fieldHex;
        private javax.swing.JTextField fieldOct;
        private javax.swing.JButton ok;
        private javax.swing.JPanel panelBin;
        private javax.swing.JPanel panelDec;
        private javax.swing.JPanel panelHex;
        private javax.swing.JPanel panelOct;
        //</editor-fold>

        public DisplayPanel()
        {
            initComponents();
        }

        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">
        private void initComponents()
        {
            panelHex=new javax.swing.JPanel();
            fieldHex=new javax.swing.JTextField();
            panelDec=new javax.swing.JPanel();
            fieldDec=new javax.swing.JTextField();
            panelOct=new javax.swing.JPanel();
            fieldOct=new javax.swing.JTextField();
            panelBin=new javax.swing.JPanel();
            fieldBin=new javax.swing.JTextField();
            edit=new javax.swing.JButton();
            ok=new javax.swing.JButton();

            setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

            panelHex.setBorder(javax.swing.BorderFactory.createTitledBorder(null,"Hex",javax.swing.border.TitledBorder.CENTER,javax.swing.border.TitledBorder.DEFAULT_POSITION));
            panelHex.setToolTipText("Hexadecimal representation");
            panelHex.setOpaque(false);

            javax.swing.GroupLayout panelHexLayout=new javax.swing.GroupLayout(panelHex);
            panelHex.setLayout(panelHexLayout);
            panelHexLayout.setHorizontalGroup(
                    panelHexLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0,68,Short.MAX_VALUE));
            panelHexLayout.setVerticalGroup(
                    panelHexLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0,27,Short.MAX_VALUE));

            add(panelHex,new org.netbeans.lib.awtextra.AbsoluteConstraints(0,0,80,50));

            fieldHex.setEditable(false);
            fieldHex.setFont(new java.awt.Font("Tahoma",0,13)); // NOI18N
            fieldHex.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            fieldHex.setFocusable(false);
            add(fieldHex,new org.netbeans.lib.awtextra.AbsoluteConstraints(10,20,60,-1));

            panelDec.setBorder(javax.swing.BorderFactory.createTitledBorder(null,"Dec",javax.swing.border.TitledBorder.CENTER,javax.swing.border.TitledBorder.DEFAULT_POSITION));
            panelDec.setToolTipText("Decimal Representation");
            panelDec.setOpaque(false);

            javax.swing.GroupLayout panelDecLayout=new javax.swing.GroupLayout(panelDec);
            panelDec.setLayout(panelDecLayout);
            panelDecLayout.setHorizontalGroup(
                    panelDecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0,68,Short.MAX_VALUE));
            panelDecLayout.setVerticalGroup(
                    panelDecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0,27,Short.MAX_VALUE));

            add(panelDec,new org.netbeans.lib.awtextra.AbsoluteConstraints(80,0,80,50));

            fieldDec.setEditable(false);
            fieldDec.setFont(new java.awt.Font("Tahoma",0,13)); // NOI18N
            fieldDec.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            fieldDec.setFocusable(false);
            add(fieldDec,new org.netbeans.lib.awtextra.AbsoluteConstraints(90,20,60,-1));

            panelOct.setBorder(javax.swing.BorderFactory.createTitledBorder(null,"Oct",javax.swing.border.TitledBorder.CENTER,javax.swing.border.TitledBorder.DEFAULT_POSITION));
            panelOct.setToolTipText("Octal representation");
            panelOct.setOpaque(false);

            javax.swing.GroupLayout panelOctLayout=new javax.swing.GroupLayout(panelOct);
            panelOct.setLayout(panelOctLayout);
            panelOctLayout.setHorizontalGroup(
                    panelOctLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0,68,Short.MAX_VALUE));
            panelOctLayout.setVerticalGroup(
                    panelOctLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0,27,Short.MAX_VALUE));

            add(panelOct,new org.netbeans.lib.awtextra.AbsoluteConstraints(160,0,80,50));

            fieldOct.setEditable(false);
            fieldOct.setFont(new java.awt.Font("Tahoma",0,13)); // NOI18N
            fieldOct.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            fieldOct.setFocusable(false);
            add(fieldOct,new org.netbeans.lib.awtextra.AbsoluteConstraints(170,20,60,-1));

            panelBin.setBorder(javax.swing.BorderFactory.createTitledBorder(null,"Bin",javax.swing.border.TitledBorder.CENTER,javax.swing.border.TitledBorder.DEFAULT_POSITION));
            panelBin.setToolTipText("Binary representation");
            panelBin.setOpaque(false);

            javax.swing.GroupLayout panelBinLayout=new javax.swing.GroupLayout(panelBin);
            panelBin.setLayout(panelBinLayout);
            panelBinLayout.setHorizontalGroup(
                    panelBinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0,148,Short.MAX_VALUE));
            panelBinLayout.setVerticalGroup(
                    panelBinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0,27,Short.MAX_VALUE));

            add(panelBin,new org.netbeans.lib.awtextra.AbsoluteConstraints(0,50,160,50));

            fieldBin.setEditable(false);
            fieldBin.setFont(new java.awt.Font("Tahoma",0,13)); // NOI18N
            fieldBin.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            fieldBin.setFocusable(false);
            add(fieldBin,new org.netbeans.lib.awtextra.AbsoluteConstraints(10,70,140,-1));

            edit.setText("Edit");
            edit.setToolTipText("Change value of this cell");
            edit.setEnabled(editable);
            edit.addActionListener(new java.awt.event.ActionListener()
            {

                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    editActionPerformed(evt);
                }
            });
            add(edit,new org.netbeans.lib.awtextra.AbsoluteConstraints(170,60,60,-1));

            ok.setText("Ok");
            ok.setToolTipText("Close display dialog");
            ok.addActionListener(new java.awt.event.ActionListener()
            {

                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    okActionPerformed(evt);
                }
            });
            add(ok,new org.netbeans.lib.awtextra.AbsoluteConstraints(170,90,60,-1));

            fieldDec.setText(Integer.toString(value));
            fieldOct.setText(Integer.toOctalString(value));
            try
            {
                String text="";
                fieldHex.setText("00".substring((text=Integer.toHexString(value).toUpperCase()).length())+text);

                text="00000000".substring((text=Integer.toBinaryString(value).toUpperCase()).length())+text;
                fieldBin.setText(text.substring(0,4)+" "+text.substring(4));
            }
            catch(StringIndexOutOfBoundsException e)
            {
                String text="";
                fieldHex.setText("0000".substring((text=Integer.toHexString(value).toUpperCase()).length())+text);

                text="0000000000000000".substring((text=Integer.toBinaryString(value).toUpperCase()).length())+text;
                fieldBin.setText(text.substring(0,4)+" "+text.substring(4,8)+" "+text.substring(8,12)+" "+text.substring(12));
            }


        }// </editor-fold>

        private void okActionPerformed(ActionEvent evt)
        {
            dispose();
        }

        private void editActionPerformed(ActionEvent evt)
        {
            dispose();
            owner.editValue();
        }
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
            java.util.logging.Logger.getLogger(ValueDisplayer.class.getName()).log(java.util.logging.Level.SEVERE,null,ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                new ValueDisplayer("Test Value Display",0xFFFF,false,null);
            }
        });
    }

}
