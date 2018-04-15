package me.uniquetrij.i8085.giu;
/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Uniquetrij
 */
@SuppressWarnings("serial")
public class FindReplace extends javax.swing.JFrame
{

    private JTextComponent jTextComponent;
    private int index;
    private boolean caretpos;
    private boolean replace;
    private int rpindex;
    private javax.swing.JCheckBox findCased;
    private javax.swing.JTextField findField;
    private javax.swing.JButton findNext;
    private javax.swing.JPanel findPanel;
    private javax.swing.JButton findPrev;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton replaceAll;
    private javax.swing.JButton replaceAllAbove;
    private javax.swing.JButton replaceAllBelow;
    private javax.swing.JTextField replaceField;
    private javax.swing.JButton replaceOnce;
    private javax.swing.JPanel replacePanel;

    public FindReplace(JTextComponent jTextComponent,boolean replace)
    {
        try
        {
            for(UIManager.LookAndFeelInfo info:UIManager.getInstalledLookAndFeels())
            {
                if("Nimbus".equals(info.getName()))
                {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch(ClassNotFoundException|InstantiationException|IllegalAccessException|UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(FindReplace.class.getName()).log(java.util.logging.Level.SEVERE,null,ex);
        }

        this.jTextComponent=jTextComponent;
        this.replace=replace;

        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents()
    {

        findPanel=new javax.swing.JPanel();
        findField=new javax.swing.JTextField();
        findNext=new javax.swing.JButton();
        findCased=new javax.swing.JCheckBox();
        jLabel1=new javax.swing.JLabel();
        findPrev=new javax.swing.JButton();
        replacePanel=new javax.swing.JPanel();
        replaceField=new javax.swing.JTextField();
        jLabel2=new javax.swing.JLabel();
        replaceOnce=new javax.swing.JButton();
        replaceAll=new javax.swing.JButton();
        replaceAllAbove=new javax.swing.JButton();
        replaceAllBelow=new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        index=jTextComponent.getCaretPosition();
        caretpos=true;

        jTextComponent.addMouseListener(new MouseListener()
        {

            @Override
            public void mouseClicked(MouseEvent e)
            {
                //System.out.println("mouseClicked");
            }

            @Override
            public void mousePressed(MouseEvent e)
            {
                //System.out.println("mousePressed");
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                //System.out.println("mouseReleased");
                index=jTextComponent.getCaretPosition();
                caretpos=true;
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                //System.out.println("mouseEntered");
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                //System.out.println("mouseExited");
            }
        });

        findPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null,"Find",javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,javax.swing.border.TitledBorder.DEFAULT_POSITION,new java.awt.Font("Tahoma",1,14))); // NOI18N

        findField.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        findField.setText(jTextComponent.getSelectedText());
        findField.selectAll();

        findNext.setText("Next");
        findNext.addActionListener(new java.awt.event.ActionListener()
        {

            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                findNextActionPerformed(evt);
            }
        });

        findCased.setText("Match Case");

        jLabel1.setFont(new java.awt.Font("Tahoma",0,12)); // NOI18N
        jLabel1.setText("Key:");

        findPrev.setText("Prev");
        findPrev.addActionListener(new java.awt.event.ActionListener()
        {

            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                findPrevActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout findPanelLayout=new javax.swing.GroupLayout(findPanel);
        findPanel.setLayout(findPanelLayout);
        findPanelLayout.setHorizontalGroup(
                findPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(findPanelLayout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(findPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,false).addGroup(findPanelLayout.createSequentialGroup().addComponent(findCased).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(findPrev).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(findNext)).addComponent(findField)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE)));
        findPanelLayout.setVerticalGroup(
                findPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(findPanelLayout.createSequentialGroup().addGroup(findPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(findField,javax.swing.GroupLayout.PREFERRED_SIZE,javax.swing.GroupLayout.DEFAULT_SIZE,javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel1)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(findPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(findCased).addComponent(findNext).addComponent(findPrev))));

        replacePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null,"Replace",javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,javax.swing.border.TitledBorder.DEFAULT_POSITION,new java.awt.Font("Tahoma",1,14))); // NOI18N
        replacePanel.setEnabled(false);


        jLabel2.setFont(new java.awt.Font("Tahoma",0,12)); // NOI18N
        jLabel2.setText("With:");

        replaceOnce.setText("Once");
        replaceOnce.addActionListener(new java.awt.event.ActionListener()
        {

            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                replaceOnceActionPerformed(evt);
            }
        });

        replaceAll.setText("All");
        replaceAll.addActionListener(new java.awt.event.ActionListener()
        {

            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                replaceAllActionPerformed(evt);
            }
        });

        replaceAllAbove.setText("All Above");
        replaceAllAbove.addActionListener(new java.awt.event.ActionListener()
        {

            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                replaceAllAboveActionPerformed(evt);
            }
        });

        replaceAllBelow.setText("All Below");
        replaceAllBelow.addActionListener(new java.awt.event.ActionListener()
        {

            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                replaceAllBelowActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout replacePanelLayout=new javax.swing.GroupLayout(replacePanel);
        replacePanel.setLayout(replacePanelLayout);
        replacePanelLayout.setHorizontalGroup(
                replacePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(replacePanelLayout.createSequentialGroup().addGap(4,4,4).addComponent(jLabel2).addGap(10,10,10).addGroup(replacePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(replaceField,javax.swing.GroupLayout.PREFERRED_SIZE,199,javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(replacePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(javax.swing.GroupLayout.Alignment.LEADING,replacePanelLayout.createSequentialGroup().addComponent(replaceAllAbove).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(replaceAllBelow)).addGroup(javax.swing.GroupLayout.Alignment.LEADING,replacePanelLayout.createSequentialGroup().addComponent(replaceOnce,javax.swing.GroupLayout.PREFERRED_SIZE,77,javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(replaceAll,javax.swing.GroupLayout.PREFERRED_SIZE,75,javax.swing.GroupLayout.PREFERRED_SIZE)))).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE)));
        replacePanelLayout.setVerticalGroup(
                replacePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(replacePanelLayout.createSequentialGroup().addGroup(replacePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(replaceField,javax.swing.GroupLayout.PREFERRED_SIZE,javax.swing.GroupLayout.DEFAULT_SIZE,javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel2)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(replacePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(replaceOnce).addComponent(replaceAll)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(replacePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(replaceAllAbove).addComponent(replaceAllBelow)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE)));

        javax.swing.GroupLayout layout=new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(findPanel,javax.swing.GroupLayout.DEFAULT_SIZE,javax.swing.GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE).addComponent(replacePanel,javax.swing.GroupLayout.DEFAULT_SIZE,javax.swing.GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(findPanel,javax.swing.GroupLayout.PREFERRED_SIZE,javax.swing.GroupLayout.DEFAULT_SIZE,javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(replacePanel,javax.swing.GroupLayout.DEFAULT_SIZE,108,Short.MAX_VALUE)));

        pack();
        if(replace)
        {
            setTitle("Replace");
            replacePanel.setEnabled(true);
            replacePanel.setVisible(true);
            replace=false;
        }
        else
        {
            setTitle("Find");
            replacePanel.setEnabled(false);
            replacePanel.setVisible(false);
            setSize(300,130);
        }
        setLocationRelativeTo(jTextComponent);

    }// </editor-fold>

    private void findPrevActionPerformed(ActionEvent evt)
    {
        caretpos=false;
        if(jTextComponent.getSelectedText()!=null)
        {
            index=jTextComponent.getSelectionStart();
        }
        if(findCased.isSelected())
        {
            find(true,findField.getText(),index-1,true);
        }
        else
        {
            find(true,findField.getText(),index-1);
        }
    }

    private void findNextActionPerformed(ActionEvent evt)
    {
        if(caretpos)
        {
            index--;
            caretpos=false;
        }
        if(findCased.isSelected())
        {
            find(findField.getText(),index+1,true);
        }
        else
        {
            find(findField.getText(),index+1);
        }
    }

    public void find(String key,int index)
    {
        key=key.toUpperCase();
        try
        {
            Document doc=jTextComponent.getDocument();
            Element e=doc.getRootElements()[0];
            String str=e.getDocument().getText(0,e.getEndOffset()).toUpperCase();
            while(index<str.length())
            {
                if(str.substring(index).indexOf(key)==0)
                {
                    jTextComponent.select(this.index=index,index+key.length());
                    return;
                }
                index++;
            }
            if(index>=(rpindex=jTextComponent.getDocument().getLength()))
            {
                if(!replace)
                {
                    JOptionPane.showConfirmDialog(this,"Reached To The End Of File.","Warning",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
                }
                else
                {
                    rpindex=index;
                }
            }
        }
        catch(BadLocationException|HeadlessException e)
        {
        }
    }

    public void find(boolean rev,String key,int index)
    {
        key=key.toUpperCase();
        try
        {
            Document doc=jTextComponent.getDocument();
            Element e=doc.getRootElements()[0];
            String str=e.getDocument().getText(0,e.getEndOffset()).toUpperCase();
            while(index>=0)
            {
                if(str.substring(index).indexOf(key)==0)
                {
                    jTextComponent.select(this.index=index,index+key.length());
                    return;
                }
                --index;
            }
            if(index<=(rpindex=0))
            {
                if(!replace)
                {
                    JOptionPane.showConfirmDialog(this,"Reached To The Begining Of File.","Warning",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
                }
                else
                {
                    rpindex=index;
                }
            }
        }
        catch(BadLocationException|HeadlessException e)
        {
        }
    }

    public void find(String key,int index,boolean cased)
    {
        try
        {
            Document doc=jTextComponent.getDocument();
            Element e=doc.getRootElements()[0];
            String str=e.getDocument().getText(0,e.getEndOffset());
            while(index<str.length())
            {
                if(str.substring(index).indexOf(key)==0)
                {
                    jTextComponent.select(this.index=index,index+key.length());
                    return;
                }
                index++;
            }
            if(index>=(rpindex=jTextComponent.getDocument().getLength()))
            {
                if(!replace)
                {
                    JOptionPane.showConfirmDialog(this,"Reached To The End Of File.","Warning",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
                }
                else
                {
                    rpindex=index;
                }
            }
        }
        catch(BadLocationException|HeadlessException e)
        {
        }
    }

    public void find(boolean rev,String key,int index,boolean cased)
    {
        try
        {
            Document doc=jTextComponent.getDocument();
            Element e=doc.getRootElements()[0];
            String str=e.getDocument().getText(0,e.getEndOffset());
            while(index>=0)
            {
                if(str.substring(index).indexOf(key)==0)
                {
                    jTextComponent.select(this.index=index,index+key.length());
                    return;
                }
                --index;
            }
            if(index<=(rpindex=0))
            {
                if(!replace)
                {
                    JOptionPane.showConfirmDialog(this,"Reached To The Begining Of File.","Warning",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
                }
                else
                {
                    rpindex=index;
                }
            }
        }
        catch(BadLocationException|HeadlessException e)
        {
        }
    }

    private void replaceOnceActionPerformed(java.awt.event.ActionEvent evt)
    {
        if(jTextComponent.getSelectedText()==null)
        {
            if(!replace)
            {
                JOptionPane.showConfirmDialog(this,"Find A String First.","Error",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
            }
            return;
        }
        String s=replaceField.getText();
        jTextComponent.replaceSelection(replaceField.getText());
    }

    private void replaceAllActionPerformed(java.awt.event.ActionEvent evt)
    {
        replace=true;
        rpindex=index=0;
        while(rpindex<jTextComponent.getDocument().getLength())
        {
            findNextActionPerformed(evt);
            replaceOnceActionPerformed(evt);
        }
        index=jTextComponent.getDocument().getLength()-1;
        replace=false;
    }

    private void replaceAllAboveActionPerformed(java.awt.event.ActionEvent evt)
    {
        replace=true;
        rpindex=index=jTextComponent.getCaretPosition();
        while(rpindex>=0)
        {
            findPrevActionPerformed(evt);
            replaceOnceActionPerformed(evt);
        }
        index=0;
        caretpos=true;
        replace=false;
    }

    private void replaceAllBelowActionPerformed(java.awt.event.ActionEvent evt)
    {
        replace=true;
        rpindex=index=jTextComponent.getCaretPosition();
        while(rpindex<jTextComponent.getDocument().getLength())
        {
            findNextActionPerformed(evt);
            replaceOnceActionPerformed(evt);
        }
        index=jTextComponent.getDocument().getLength()-1;
        replace=false;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        java.awt.EventQueue.invokeLater(new Runnable()
        {

            public void run()
            {
                new FindReplace(null,false).setVisible(true);
            }
        });
    }
}