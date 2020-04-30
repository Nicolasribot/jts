/*
 * Copyright (c) 2019 Martin Davis.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 *
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */
package org.locationtech.jts.operation.overlayng;

import org.locationtech.jts.geom.Location;
import org.locationtech.jts.geomgraph.Position;

/**
 * A label for a pair of {@link OverlayEdge}s which records
 * the topological information for the edge
 * in the {@link OverlayGraph} containing it.
 * The label is shared between both OverlayEdges
 * of a symmetric pair. 
 * This means that accessors for orientation-sensitive information
 * need to provide the orientation of the containing OverlayEdge.
 * <p>
 * A label contains the topological {@link Location}s for 
 * the two overlay input geometries.
 * A labelled input geometry may be either a Line or an Area.
 * In both cases, the label locations are populated
 * with the locations for the edge {@link Position}s
 * once they are computed by topological evaluation.
 * The label also records the dimension of each geometry,
 * and in the case of area boundary edges, the role
 * of the originating ring (which allows
 * determination of the edge role in collapse cases).
 * <p>
 * For each input geometry, the label indicates that an edge is in one of the following states
 * (denoted by the <code>dim</code> field),
 * and contains some ancillary information for each state:
 * <ul>
 * <li>A <b>Boundary</b> edge of an input Area (polygon)
 *   <ul>
 *   <li><code>dim</code> = DIM_BOUNDARY</li>
 *   <li><code>locLeft, locRight</code> : the locations of the edge sides for the area parent input geometry</li>
 *   <li><code>isHole</code> : whether the 
 * edge was in a shell or a hole</li>
 *   </ul>
 * </li>
 * <li>A <b>Collapsed</b> edge of an input Area 
 * (which had two or more parent edges)
 *   <ul>
 *   <li><code>dim</code> = DIM_COLLAPSE</li>
 *   <li><code>locLine</code> : the location of the 
 * edge relative to the area parent input geometry</li>
 *   <li><code>isHole</code> : whether some 
 * contributing edge was in a shell (<code>false</code>), 
 * or otherwise that all were in holes</li> (<code>true</code>)
 *   </ul>
 * </li>
 * <li>An edge from an input <b>Line</b>
 *   <ul>
 *   <li><code>dim</code> = DIM_LINE</li>
 *   <li><code>locLine</code> : INTERIOR</li>
 *   </ul>
 * </li>
 * <li>An edge which is <b>Not Part</b> of the input geometry
 * (and hence must be part of the other geometry).
 *   <ul>
 *   <li><code>dim</code> = NOT_PART</li>
 *   </ul>
 * </li>
 * </ul>
 * Note that:
 * <ul>
 * <li>an edge cannot be both a Collapse edge and a Line edge in the same input geometry, 
 * because each input geometry must be homogeneous.
 * <li>an edge may be an Boundary edge in one input geometry 
 * and a Line or Collapse edge in the other input.
 * </ul>
 * 
 * @author Martin Davis
 *
 */
class OverlayLabel {
  
  private static final char SYM_UNKNOWN = '#';
  private static final char SYM_BOUNDARY = 'B';
  private static final char SYM_COLLAPSE = 'C';
  private static final char SYM_LINE = 'L';
  
  public static final int DIM_UNKNOWN = -1;
  public static final int DIM_NOT_PART = DIM_UNKNOWN;
  public static final int DIM_LINE = 1;
  public static final int DIM_BOUNDARY = 2;
  public static final int DIM_COLLAPSE = 3;
  
  /**
   * Indicates that the location is currently unknown
   */
  public static int LOC_UNKNOWN = Location.NONE;
  
  
  private int aDim = DIM_NOT_PART;
  private boolean aIsHole = false;
  private int aLocLeft = LOC_UNKNOWN;
  private int aLocRight = LOC_UNKNOWN;
  private int aLocLine = LOC_UNKNOWN;
  
  private int bDim = DIM_NOT_PART;
  private boolean bIsHole = false;
  private int bLocLeft = LOC_UNKNOWN;
  private int bLocRight = LOC_UNKNOWN;
  private int bLocLine = LOC_UNKNOWN;

  
  public OverlayLabel(int index, int locLeft, int locRight, boolean isHole)
  {
    initBoundary(index, locLeft, locRight, isHole);
  }

  public OverlayLabel(int index)
  {
    initLine(index);
  }

  public OverlayLabel()
  {
  }

  public OverlayLabel(OverlayLabel lbl) {
    this.aLocLeft = lbl.aLocLeft;
    this.aLocRight = lbl.aLocRight;
    this.aLocLine = lbl.aLocLine;
    this.aDim = lbl.aDim;
    this.aIsHole = lbl.aIsHole;
    
    this.bLocLeft = lbl.bLocLeft;
    this.bLocRight = lbl.bLocRight;
    this.bLocLine = lbl.bLocLine;
    this.bDim = lbl.bDim;
    this.bIsHole = lbl.bIsHole;
  }

  public int dimension(int index) {
    if (index == 0)
      return aDim;
    return bDim;
  }
  
  public void initBoundary(int index, int locLeft, int locRight, boolean isHole) {
    if (index == 0) {
      aDim = DIM_BOUNDARY;
      aIsHole = isHole;
      aLocLeft = locLeft;
      aLocRight = locRight;
      aLocLine = Location.INTERIOR;
    }
    else {
      bDim = DIM_BOUNDARY;
      bIsHole = isHole;
      bLocLeft = locLeft;
      bLocRight = locRight;
      bLocLine = Location.INTERIOR;
    }
  }
  
  public void initCollapse(int index, boolean isHole) {
    if (index == 0) {
      aDim = DIM_COLLAPSE;
      aIsHole = isHole;
    }
    else {
      bDim = DIM_COLLAPSE;
      bIsHole = isHole;
    }
  }
  
  public void initLine(int index) {
    if (index == 0) {
      aDim = DIM_LINE;
    }
    else {
      bDim = DIM_LINE;
    }
  }
  
  public void initNotPart(int index) {
    // this assumes locations are initialized to UNKNOWN
    if (index == 0) {
      aDim = DIM_NOT_PART;
    }
    else {
      bDim = DIM_NOT_PART;
    }
  }
  
  /*
  public void initAsLine(int index, int locInArea) {
    int loc = normalizeLocation(locInArea);
    if (index == 0) {
      aDim = DIM_LINE;
      aLocLine = loc;
    }
    else {
      bDim = DIM_LINE;
      bLocLine = loc;
    }
  }
  */
  
  /*
   // Not needed so far
  public void setToNonPart(int index, int locInArea) {
    int loc = normalizeLocation(locInArea);
    if (index == 0) {
      aDim = DIM_NOT_PART;
      aLocInArea = loc;
      aLocLeft = loc;
      aLocRight = loc;
    }
    else {
      bDim = DIM_NOT_PART;
      bLocInArea = loc;
      aLocLeft = loc;
      aLocRight = loc;
    }
  }
  */
  
  /**
   * Sets the line location.
   * 
   * This is used to set the locations for linear edges 
   * encountered during area label propagation.
   * 
   * @param index source to update
   * @param loc location to set
   */
  public void setLocationLine(int index, int loc) {
    if (index == 0) {
      aLocLine = loc;
    }
    else {
      bLocLine = loc;
    }
  }
  
  public void setLocationAll(int index, int loc) {
    if (index == 0) {
      aLocLine = loc;
      aLocLeft = loc;
      aLocRight = loc;
    }
    else {
      bLocLine = loc;
      bLocLeft = loc;
      bLocRight = loc;
    }
  }
  
  public void setLocationCollapse(int index) {
    int loc = isHole(index) ? Location.INTERIOR : Location.EXTERIOR;
    if (index == 0) {
      aLocLine = loc;
    }
    else {
      bLocLine = loc;
    }
  }   

  public boolean isLine() {
    return aDim == DIM_LINE || bDim == DIM_LINE;
  }
  
  public boolean isLine(int index) {
    if (index == 0) {
      return aDim == DIM_LINE;
    }
    return bDim == DIM_LINE;
  }

  public boolean isLinear(int index) {
    if (index == 0) {
      return aDim == DIM_LINE || aDim == DIM_COLLAPSE;
    }
    return bDim == DIM_LINE || bDim == DIM_COLLAPSE;
  }

  public boolean isKnown(int index) {
    if (index == 0) {
      return aDim != DIM_UNKNOWN;
    }
    return bDim != DIM_UNKNOWN;
  }

  public boolean isNotPart(int index) {
    if (index == 0) {
      return aDim == DIM_NOT_PART;
    }
    return bDim == DIM_NOT_PART;
  }

  public boolean isBoundaryEither() {
    return aDim == DIM_BOUNDARY || bDim == DIM_BOUNDARY;
  }
  
  public boolean isBoundaryBoth() {
    return aDim == DIM_BOUNDARY && bDim == DIM_BOUNDARY;
  }
  
  public boolean isBoundary(int index) {
    if (index == 0) {
      return aDim == DIM_BOUNDARY;
    }
    return bDim == DIM_BOUNDARY;
  }
  
  public boolean isLineLocationUnknown(int index) {
    if (index == 0) {
      return aLocLine == LOC_UNKNOWN;
    }
    else {
      return bLocLine == LOC_UNKNOWN;
    }
  }

  public boolean isHole(int index) {
    if (index == 0) {
      return aIsHole;
    }
    else {
      return bIsHole;
    }
  }
  
  public boolean isCollapse(int index) {
    return dimension(index) == DIM_COLLAPSE;
  }
  
  public int getLineLocation(int index) {
    if (index == 0) {
      return aLocLine;
    }
    else {
      return bLocLine;
    }
  }
  
  public boolean isInArea(int index) {
    if (index == 0) {
      return aLocLine == Location.INTERIOR;
    }
    return bLocLine == Location.INTERIOR;
  }
  
  public int getLocation(int index, int position, boolean isForward) {
    if (index == 0) {
      switch (position) {
        case Position.LEFT: return isForward ? aLocLeft : aLocRight;
        case Position.RIGHT: return isForward ? aLocRight : aLocLeft;
        case Position.ON: return aLocLine;
      }
    }
    switch (position) {
      case Position.LEFT: return isForward ? bLocLeft : bLocRight;
      case Position.RIGHT: return isForward ? bLocRight : bLocLeft;
      case Position.ON: return bLocLine;
    }
    return LOC_UNKNOWN;
  }
  
  /**
   * Gets the location for this label for either
   * a Boundary or a Line edge.
   * This supports a simple determination of
   * whether the edge should be included as a result edge.
   * 
   * @param index the source index
   * @param position the position for a boundary label
   * @param isForward the direction for a boundary label
   * @return the location for the specified position
   */
  public int getLocationBoundaryOrLine(int index, int position, boolean isForward) {
    if (isBoundary(index)) {
      return getLocation(index, position, isForward);
    }
    return getLineLocation(index);
  }
  
  /**
   * Gets the linear location for the given source.
   * 
   * @param index the source index
   * @return the linear location for the source
   */
  public int getLocation(int index) {
    if (index == 0) {
      return aLocLine;
    }
    return bLocLine;
  }

  public boolean hasSides(int index) {
    if (index == 0) {
      return aLocLeft != LOC_UNKNOWN
          || aLocRight != LOC_UNKNOWN;
    }
    return bLocLeft != LOC_UNKNOWN
        || bLocRight != LOC_UNKNOWN;
  }
  
  public OverlayLabel copy() {
    return new OverlayLabel(this);
  }
    
  public OverlayLabel copyFlip() {
    OverlayLabel lbl = new OverlayLabel();
    
    lbl.aLocLeft = this.aLocRight;
    lbl.aLocRight = this.aLocLeft;
    lbl.aLocLine = this.aLocLine;
    lbl.aDim = this.aDim;
    
    lbl.bLocLeft = this.bLocRight;
    lbl.bLocRight = this.bLocLeft;
    lbl.bLocLine = this.bLocLine;
    lbl.bDim = this.bDim;
    
    return lbl;
  }
    
  public String toString()
  {
    return toString(true);
  }

  public String toString(boolean isForward)
  {
    StringBuilder buf = new StringBuilder();
    buf.append("A:");
    buf.append(locationString(0, isForward));
    buf.append("/B:");
    buf.append(locationString(1, isForward));
    return buf.toString();
  }

  private String locationString(int index, boolean isForward) {
    StringBuilder buf = new StringBuilder();
    if (isBoundary(index)) {
      buf.append( Location.toLocationSymbol( getLocation(index, Position.LEFT, isForward) ) );
      buf.append( Location.toLocationSymbol( getLocation(index, Position.RIGHT, isForward) ) );
    }
    else {
      buf.append( Location.toLocationSymbol( index == 0 ? aLocLine : bLocLine ));
    }
    if (isKnown(index))
      buf.append( dimensionSymbol(index == 0 ? aDim : bDim) );
    if (isCollapse(index)) {
      buf.append( ringRoleSymbol( index == 0 ? aIsHole : bIsHole ));
    }
    return buf.toString();
  }

  public static Object ringRoleSymbol(boolean isHole) {
    return isHole ? 'h' : 's';
  }

  public static char dimensionSymbol(int dim) {
    switch (dim) {
    case DIM_LINE: return SYM_LINE;
    case DIM_COLLAPSE: return SYM_COLLAPSE;
    case DIM_BOUNDARY: return SYM_BOUNDARY;
    }
    return SYM_UNKNOWN;
  }


}
