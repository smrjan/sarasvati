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

    Copyright 2008 Paul Lorenz
*/
package com.googlecode.sarasvati.hib;

import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.Process;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.WorkflowException;

public class HibNestedProcessNode extends HibNode
{
  protected String graphName;

  @Override
  public void execute (Engine engine, NodeToken token)
  {
    Graph subGraph = engine.getRepository().getLatestGraph( graphName );

    if ( subGraph == null )
    {
      throw new WorkflowException( "No version of graph named '" + graphName + "'. " +
                                   "Used by node " + getName() + " in graph " + getGraph().getName() );
    }

    Process subProcess =  engine.getFactory().newNestedProcess( subGraph, token );
    subProcess.getEnv().importEnv( token.getFullEnv() );
    engine.startProcess( subProcess );
  }
}