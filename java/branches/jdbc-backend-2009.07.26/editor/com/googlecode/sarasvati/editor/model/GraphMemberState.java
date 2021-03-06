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

package com.googlecode.sarasvati.editor.model;

import java.util.Map;

public class GraphMemberState
{
  private final String name;
  private final Map<String,String> customProperties;

  public GraphMemberState (final String name,
                           final Map<String,String> customProperties)
  {
    this.name = name;
    this.customProperties = customProperties;
  }

  public String getName ()
  {
    return name;
  }

  /**
   * @return the customProperties
   */
  public Map<String, String> getCustomProperties ()
  {
    return customProperties;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode ()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((customProperties == null) ? 0 : customProperties.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals (Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof GraphMemberState))
      return false;
    GraphMemberState other = (GraphMemberState) obj;
    if (customProperties == null)
    {
      if (other.customProperties != null)
        return false;
    } else if (!customProperties.equals( other.customProperties ))
      return false;
    if (name == null)
    {
      if (other.name != null)
        return false;
    } else if (!name.equals( other.name ))
      return false;
    return true;
  }
}
