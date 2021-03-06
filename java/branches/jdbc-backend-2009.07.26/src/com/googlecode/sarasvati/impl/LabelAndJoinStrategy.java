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
package com.googlecode.sarasvati.impl;

import java.util.List;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.GraphProcess;

/**
 * Implements a join strategy in which nodes will wait for arc tokens to be
 * present on all incoming arcs which share the same name as the current incoming
 * arc token before completing the join.
 *
 * @author Paul Lorenz
 */
public class LabelAndJoinStrategy extends AndJoinStrategy
{
  @Override
  protected List<? extends Arc> getJoiningArcs (GraphProcess process, ArcToken token)
  {
    return process.getGraph().getInputArcs( token.getArc().getEndNode(), token.getArc().getName() );
  }
}
