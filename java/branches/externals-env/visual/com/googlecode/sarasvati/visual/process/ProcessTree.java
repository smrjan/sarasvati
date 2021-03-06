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

    Copyright 2008-2009 Paul Lorenz
*/
package com.googlecode.sarasvati.visual.process;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.visual.ProcessLookAndFeel;
import com.googlecode.sarasvati.visual.graph.GraphLayoutTree;

public class ProcessTree
{
  protected ProcessLookAndFeel lookAndFeel;
  protected GraphLayoutTree graphTree;

  protected Map<NodeToken, ProcessTreeNode> nodeTokenMap = new HashMap<NodeToken, ProcessTreeNode>();
  protected Map<Node, ProcessTreeNode>      nodeMap      = new HashMap<Node, ProcessTreeNode>();

  protected List<NodeToken> sortedTokenList = null;

  protected List<ProcessTreeNode> queue = new LinkedList<ProcessTreeNode>();

  private static class ParentTree
  {
    protected List<List<NodeToken>> tree = new LinkedList<List<NodeToken>>();

    public ParentTree (NodeToken token)
    {
      Set<NodeToken> processed = new HashSet<NodeToken>();

      List<NodeToken> nextLayer = new LinkedList<NodeToken>();
      nextLayer.add( token );

      while ( !nextLayer.isEmpty() )
      {
        tree.add( nextLayer );
        List<NodeToken> prevLayer = nextLayer;
        nextLayer = new LinkedList<NodeToken>();

        for ( NodeToken ancestor : prevLayer )
        {
          for ( ArcToken arcToken : ancestor.getParentTokens() )
          {
            if ( !processed.contains( arcToken.getParentToken() ) )
            {
              nextLayer.add( arcToken.getParentToken() );
              processed.add( arcToken.getParentToken() );
            }
          }
        }
      }

      tree.remove( 0 );
    }

    public int getDistance (ParentTree other)
    {
      int localDepth = 0;
      for ( List<NodeToken> localLayer : tree )
      {
        int otherDepth = 0;
        for ( List<NodeToken> otherLayer : other.tree )
        {
          if ( !Collections.disjoint( localLayer, otherLayer ) )
          {
            return localDepth + otherDepth;
          }
          otherDepth++;
        }
        localDepth++;
      }
      return Integer.MAX_VALUE;
    }
  }

  protected ProcessTreeNode getProcessTreeNode (ProcessTreeNode parent, Node node)
  {
    NodeToken parentToken = parent.getParentToken();

    NodeToken current = null;
    int currentDistance = 0;
    ParentTree parentTree = null;
    for ( NodeToken token : sortedTokenList )
    {
      if ( token.getNode().equals( node ) )
      {
        if ( current == null )
        {
          current = token;
        }
        else
        {
          if ( parentTree == null )
          {
            parentTree = new ParentTree( parentToken );
            currentDistance = parentTree.getDistance( new ParentTree( current ) );
          }
          int distance = parentTree.getDistance( new ParentTree( token ) );
          if ( distance < currentDistance )
          {
            current = token;
            currentDistance = distance;
          }
        }
      }
    }

    if ( current != null )
    {
      return nodeTokenMap.get( current );
    }

    return getNonTokenProcessTreeNode( parent, node );
  }

  public ProcessTreeNode getNonTokenProcessTreeNode (ProcessTreeNode parent, Node node)
  {
    ProcessTreeNode endNode = nodeMap.get( node );

    if ( endNode == null )
    {
      endNode = new ProcessTreeNode( parent, node );
      nodeMap.put( node, endNode );
      queue.add( endNode );
    }

    return endNode;
  }

  /*
   * 1. Find start node tokens
   * 2. Add start node tokens to first layer
   * 3. For each layer: for each element in the layer
   *   a. If it's a node token, process all arc token children
   *   b. If it's a node, process all arc children
   */
  public ProcessTree (GraphProcess process, ProcessLookAndFeel lookAndFeel)
  {
    Graph graph = process.getGraph();
    this.graphTree = new GraphLayoutTree( graph );
    this.lookAndFeel = lookAndFeel;

    for ( NodeToken token : process.getNodeTokens() )
    {
      nodeTokenMap.put( token, new ProcessTreeNode( token ) );
    }

    sortedTokenList = new ArrayList<NodeToken>( process.getNodeTokens() );

    Collections.sort( sortedTokenList, new Comparator<NodeToken>()
    {
      @Override
      public int compare( NodeToken o1, NodeToken o2 )
      {
        return o1.getCreateDate().compareTo( o2.getCreateDate() );
      }
    });

    for ( NodeToken token : nodeTokenMap.keySet() )
    {
      ProcessTreeNode childNode  = nodeTokenMap.get( token );
      for ( ArcToken parentArc : token.getParentTokens() )
      {
        ProcessTreeNode parentNode = nodeTokenMap.get( parentArc.getParentToken() );
        ProcessTreeArc processTreeArc = new ProcessTreeArc( parentArc, parentNode, childNode );
        parentNode.addChild( processTreeArc );
        childNode.addParent( parentNode );
      }
    }

    // We want to set position for all nodes with node tokens first
    // Later we'll go back and add in the rest of the nodes
    List<ProcessTreeNode> nextLayer = new LinkedList<ProcessTreeNode>();
    List<List<ProcessTreeNode>> layers = new LinkedList<List<ProcessTreeNode>>();

    Set<ProcessTreeNode> processed = new HashSet<ProcessTreeNode>();

    for ( ProcessTreeNode ptNode : nodeTokenMap.values() )
    {
      if ( ptNode.isStartTokenNode() )
      {
        ptNode.setDepth( 0 );
        ptNode.addToLayer( nextLayer );
        processed.add( ptNode );
      }
    }

    int depth = 1;

    // traverse token DAG, adding each processtreenode to the right layer
    // and setting its depth.
    while ( !nextLayer.isEmpty() )
    {
      layers.add( nextLayer );
      List<ProcessTreeNode> prevLayer = nextLayer;
      nextLayer = new LinkedList<ProcessTreeNode>();

      for ( ProcessTreeNode treeNode : prevLayer )
      {
        for ( ProcessTreeArc ptArc : treeNode.getChildren() )
        {
          if ( !processed.contains( ptArc.getChild() ) )
          {
            ProcessTreeNode childNode = ptArc.getChild();
            childNode.setDepth( depth );
            childNode.addToLayer( nextLayer );
            processed.add( childNode );
          }
        }
      }
      depth++;
    }

    // active tokens won't have been processed in the previous step
    for ( ArcToken arcToken : process.getActiveArcTokens() )
    {
      ProcessTreeNode parentNode = nodeTokenMap.get( arcToken.getParentToken() );
      ProcessTreeNode childNode  = getNonTokenProcessTreeNode( parentNode, arcToken.getArc().getEndNode() );
      ProcessTreeArc ptArc = new ProcessTreeArc( arcToken, parentNode, childNode );
      parentNode.addChild( ptArc );
      childNode.addParent( parentNode );
    }

    // Process all arcs which don't have arc tokens on them
    for ( ProcessTreeNode ptNode : nodeTokenMap.values() )
    {
      for ( Arc arc : graph.getOutputArcs( ptNode.getNode() ) )
      {
        if ( !ptNode.isTokenOnArc( arc ) && !arc.isSelfArc() )
        {
          // If the node has an active token we should point
          // to a version of the node with no token on it, unless
          // the arc is a back arc
          ProcessTreeNode child = ptNode.getToken().isComplete() || lookAndFeel.isBackArc( arc, graphTree.isBackArc( arc.getStartNode(), arc.getEndNode() ) ) ?
                                    getProcessTreeNode( ptNode, arc.getEndNode() ) :
                                    getNonTokenProcessTreeNode( ptNode, arc.getEndNode() );
          ProcessTreeArc arcTokenWrapper = new ProcessTreeArc( arc, ptNode, child );
          ptNode.addChild( arcTokenWrapper );
          child.addParent( ptNode );
        }
      }
    }

    // Process all nodes without tokens entries in queue
    while ( !queue.isEmpty() )
    {
      ProcessTreeNode ptNode = queue.remove( 0 );
      for ( Arc arc : graph.getOutputArcs( ptNode.getNode() ) )
      {
        ProcessTreeNode child = arc.isSelfArc() ? ptNode : getProcessTreeNode( ptNode, arc.getEndNode() );
        ProcessTreeArc arcTokenWrapper = new ProcessTreeArc( arc, ptNode, child );
        ptNode.addChild( arcTokenWrapper );
        child.addParent( ptNode );
      }
    }

    processed.clear();

    nextLayer = layers.get( 0 );
    processed.addAll( nextLayer );

    // set positioning for nodes that haven't been handled yet
    depth = 1;

    while ( !nextLayer.isEmpty() )
    {
      List<ProcessTreeNode> prevLayer = nextLayer;
      nextLayer = layers.size() > depth ? layers.get( depth ) : new LinkedList<ProcessTreeNode>();

      for ( ProcessTreeNode treeNode : prevLayer )
      {
        for ( ProcessTreeArc ptArc : treeNode.getChildren() )
        {
          ProcessTreeNode child = ptArc.getChild();
          if ( !processed.contains( child ) &&
               !( treeNode.isCompletedNodeToken() && child.hasNonCompleteNodeTokenParent() ) &&
               !( ptArc.getToken() == null &&
                  treeNode.getToken() != null &&
                  child.hasLowerParent( treeNode ) ) )
          {
            if ( child.getDepth() == -1 )
            {
              child.setDepth( depth );
              child.addToLayer( nextLayer );
            }
            processed.add( child );
          }
        }
      }
      depth++;
    }
  }

  public Iterable<ProcessTreeNode> getProcessTreeNodes ()
  {
    ArrayList<ProcessTreeNode> result = new ArrayList<ProcessTreeNode>( nodeTokenMap.size() + nodeMap.size() );
    result.addAll( nodeTokenMap.values() );
    result.addAll( nodeMap.values() );
    return result;
  }
}