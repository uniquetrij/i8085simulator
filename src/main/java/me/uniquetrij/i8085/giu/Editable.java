/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.uniquetrij.i8085.giu;

import me.uniquetrij.i8085.DataEx;
import me.uniquetrij.i8085.Data;
import javax.swing.DefaultButtonModel;

/**
 *
 * @author Uniquetrij
 */
public interface Editable
{
    public void editValue();
    public void setValue(Data data);

    public void setValue(DataEx d);
}
