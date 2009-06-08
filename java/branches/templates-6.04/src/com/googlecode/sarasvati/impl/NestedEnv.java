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

import java.util.HashSet;
import java.util.Set;

import com.googlecode.sarasvati.Env;

public class NestedEnv implements Env
{
  protected Env outerEnv;
  protected Env innerEnv;

  public NestedEnv (Env outerEnv, Env innerEnv)
  {
    this.outerEnv = outerEnv;
    this.innerEnv = innerEnv;
  }

  @Override
  public Iterable<String> getAttributeNames ()
  {
    Set<String> names = new HashSet<String>();

    for ( String name : outerEnv.getAttributeNames() )
    {
      names.add( name );
    }

    for ( String name : innerEnv.getAttributeNames() )
    {
      names.add( name );
    }

    return names;
  }

  @Override
  public String getAttribute (String name)
  {
    return outerEnv.hasAttribute( name ) ? outerEnv.getAttribute( name ) :
                                           innerEnv.getAttribute( name );
  }

  @Override
  public <T> T getAttribute (String name, Class<T> type)
  {
    return outerEnv.hasAttribute( name ) ? outerEnv.getAttribute( name, type ) :
                                           innerEnv.getAttribute( name, type );
  }

  @Override
  public boolean hasAttribute (String name)
  {
    return outerEnv.hasAttribute( name ) || innerEnv.hasAttribute( name );
  }

  @Override
  public void removeAttribute (String name)
  {
    outerEnv.removeAttribute( name );
  }

  @Override
  public void setAttribute (String name, Object value)
  {
    outerEnv.setAttribute(name, value);
  }

  @Override
  public void setTransientAttribute (String name, Object value)
  {
    outerEnv.setTransientAttribute( name, value );
  }

  @Override
  public boolean hasTransientAttribute (String name)
  {
    return outerEnv.hasAttribute( name ) || innerEnv.hasAttribute( name );
  }

  @Override
  public Object getTransientAttribute (String name)
  {
    return outerEnv.hasAttribute( name ) ? outerEnv.getTransientAttribute( name ) : innerEnv.getTransientAttribute( name );
  }

  @Override
  public void removeTransientAttribute (String name)
  {
    outerEnv.removeTransientAttribute( name );
  }

  @Override
  public Iterable<String> getTransientAttributeNames()
  {
    Set<String> names = new HashSet<String>();

    for ( String name : outerEnv.getTransientAttributeNames() )
    {
      names.add( name );
    }

    for ( String name : innerEnv.getTransientAttributeNames() )
    {
      names.add( name );
    }

    return names;
  }

  /**
   * Imports the given env into the outer env
   */
  @Override
  public void importEnv(Env env)
  {
    outerEnv.importEnv( env );
  }
}