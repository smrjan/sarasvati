/*
    This file is part of Sarasvati.

    Sarasvati is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Sarasvati is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Sarasvati.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2009 Paul Lorenz
*/
package com.googlecode.sarasvati.editor.dialog;

import java.awt.Point;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.googlecode.sarasvati.editor.model.EditorArc;
import com.googlecode.sarasvati.editor.model.EditorGraphMember;

public class DialogFactory
{
  protected static JFrame frame = null;

  private static PreferencesDialog preferencesDialog = null;

  public static void setFrame (JFrame frame)
  {
    DialogFactory.frame = frame;
    preferencesDialog = new PreferencesDialog( frame );
  }

  public static JDialog newGraphMemberPropertiesDialog (EditorGraphMember<?> graphMember)
  {
    return new GraphMemberPropertiesDialog( frame, graphMember );
  }

  public static JDialog newArcPropertiesDialog (EditorArc arc)
  {
    return new ArcPropertiesDialog( frame, arc );
  }

  public static void showError (String error)
  {
    JOptionPane.showMessageDialog( frame, error, "Error", JOptionPane.ERROR_MESSAGE );
  }

  public static void showInfo (String msg)
  {
    JOptionPane.showMessageDialog( frame, msg, "Info", JOptionPane.INFORMATION_MESSAGE );
  }

  public static JDialog newPreferencesDialog ()
  {
    Point location = frame.getLocation();
    preferencesDialog.setLocation( location.x + 50, location.y + 10 );
    preferencesDialog.selectGeneral();
    return preferencesDialog;
  }
}
