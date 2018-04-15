/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.uniquetrij.i8085.giu;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.*;

/**
 *
 * @author Uniquetrij
 */
@SuppressWarnings("serial")
public final class EditorPane extends JScrollPane implements Serializable {
    //<editor-fold defaultstate="collapsed" desc="instance members">

    private JTextPane editor;
    private JTextPane lines;
    private boolean saved;
    private boolean changed;
    private int lncount;
    private String brakepts;
    private int fontsize;

    //</editor-fold>
    public EditorPane() {
        this(13);
    }

    public EditorPane(int fontsize) {
        lncount = 1;
        brakepts = "*";
        saved = true;
        changed = false;
        this.fontsize = fontsize;

        editor = new JTextPane() {

            @Override
            public boolean getScrollableTracksViewportWidth() {
                Component parent = getParent();
                ComponentUI ui = getUI();
                return parent != null ? (ui.getPreferredSize(this).width <= parent.getSize().width) : true;
            }
        };
        editor.setFont(new Font(Font.MONOSPACED, 0, fontsize));
        editor.setBorder(new MatteBorder(8, 5, 0, 0, Color.WHITE));
        editor.setSelectedTextColor(Color.MAGENTA);
        editor.setSelectionColor(Color.YELLOW);
        editor.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent evt) {
                if (changed == false) {
                    firePropertyChange("changed", saved = false, changed = true);
                } else {
                    saved = false;
                }
                linesUpdate(0, false);

            }

            @Override
            public void keyPressed(KeyEvent evt) {

            }

            @Override
            public void keyReleased(KeyEvent evt) {
                firePropertyChange("undo/redoable", true, false);
            }
        });
        editor.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent evt) {
                if (changed == false); //firePropertyChange("changed",saved=false,changed=true);
                else;
                //saved=false;
                try {
                    Document d = evt.getDocument();
                    if (evt.getChange(d.getDefaultRootElement()) != null) {
                        linesUpdate(0, false);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void insertUpdate(DocumentEvent evt) {
                if (changed == false) {
                    firePropertyChange("changed", saved = false, changed = true);
                } else {
                    saved = false;
                }
                try {
                    Document d = evt.getDocument();
                    if (evt.getChange(d.getDefaultRootElement()) != null) {
                        linesUpdate(0, false);
                    }
                } catch (Exception e) {
                }

            }

            @Override
            public void removeUpdate(DocumentEvent evt) {
                if (changed == false) {
                    firePropertyChange("changed", saved = false, changed = true);
                } else {
                    saved = false;
                }
                try {
                    Document d = evt.getDocument();
                    if (evt.getChange(d.getDefaultRootElement()) != null) {
                        linesUpdate(0, false);
                    }
                } catch (Exception e) {
                }
            }

        });

        editor.setText(";TODO: write assembly code here. "
                + "\n\n"
                + "END");
        editor.setCaretPosition(editor.getText().length()-4);

        lines = new JTextPane() {
        };
        lines.setDisabledTextColor(Color.RED);
        lines.setEnabled(false);
        lines.setFont(new Font(Font.MONOSPACED, 0, fontsize));
        lines.setEditable(false);
        lines.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent evt) {
                linesActionPerformed(evt);
            }
        });

        JPanel margin = new JPanel();
        margin.setBorder(new MatteBorder(0, 0, 0, 1, Color.BLACK));
        margin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        margin.add(lines);
        margin.setBackground(lines.getBackground());

        linesUpdate(0, false);

        setViewportView(editor);
        setRowHeaderView(margin);
    }

    public void linesUpdate(int line, boolean brake) {
        StyledDocument doc = lines.getStyledDocument();
        SimpleAttributeSet align = new SimpleAttributeSet();
        StyleConstants.setAlignment(align, StyleConstants.ALIGN_RIGHT);
        doc.setParagraphAttributes(0, 0, align, false);
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        StyleConstants.setFontSize(def, editor.getFont().getSize());
        Style regular = doc.addStyle("regular", def);

        // Create a linestyle style
        Style linestyle = doc.addStyle("lines", regular);
        StyleConstants.setFontSize(linestyle, 10);
        StyleConstants.setForeground(linestyle, Color.RED);

        // Create a brkptstyle style
        Style brkptstyle = doc.addStyle("brkptstyle", regular);
        StyleConstants.setFontSize(brkptstyle, 10);
        StyleConstants.setBackground(brkptstyle, Color.YELLOW);
        StyleConstants.setForeground(brkptstyle, Color.RED);

        lines.setText("");
        int caretPosition = editor.getDocument().getLength();
        Element root = editor.getDocument().getDefaultRootElement();

        try {
            if ((line == 1 && brake) || brakepts.indexOf("*1*") >= 0) {
                doc.insertString(0, "stop", brkptstyle);
            } else {
                doc.insertString(0, "   1", linestyle);
            }

            for (lncount = 2; lncount < root.getElementIndex(caretPosition) + 2; lncount++) {
                doc.insertString(doc.getLength(), System.getProperty("line.separator"), regular);
                if ((line == lncount && brake) || brakepts.indexOf("*" + lncount + "*") >= 0) {
                    doc.insertString(doc.getLength(), "stop", brkptstyle);
                } else {
                    doc.insertString(doc.getLength(), lncount + "", linestyle);
                }
            }
        } catch (Exception e) {
        }
        lncount--;
    }

    public void linesActionPerformed(MouseEvent evt) {
        int c = lines.getCaretPosition();
        double lnheight = lines.getSize().getHeight() / lncount;
        int line;
        boolean brake = brakepts.indexOf("*" + (line = (int) (evt.getY() / lnheight) + 1) + "*") == -1;

        if (brake) {
            brakepts += line + "*";
        } else {
            brakepts = brakepts.replace("*" + line + "*", "*");
        }
        linesUpdate(line, brake);
        lines.setCaretPosition(c);
    }

    public String getBrakePoints() {
        return brakepts;
    }

    public JTextPane getEditor() {
        return editor;
    }

    public int getLineCount() {
        return lncount;
    }

    public void setSaved() {
        saved = true;
        changed = false;
    }

    public void setChanged() {
        changed = true;
        saved = false;
    }

    public void resetsaved() {
        saved = false;
    }

    public void resetChanged() {
        changed = false;
    }

    public boolean isSaved() {
        return saved;
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                EditorPane ep = new EditorPane(13);
                JFrame frame = new JFrame();
                frame.add(ep);
                frame.setSize(500, 500);
                frame.setVisible(true);
            }
        });
    }

    public void setFontSize(int size) {
        editor.setFont(new Font(Font.MONOSPACED, 0, size));
        linesUpdate(0, false);
        lines.setFont(new Font(Font.MONOSPACED, 0, size));
    }

}
