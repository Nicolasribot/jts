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

import static org.locationtech.jts.operation.overlayng.OverlayNG.DIFFERENCE;
import static org.locationtech.jts.operation.overlayng.OverlayNG.INTERSECTION;
import static org.locationtech.jts.operation.overlayng.OverlayNG.SYMDIFFERENCE;
import static org.locationtech.jts.operation.overlayng.OverlayNG.UNION;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.PrecisionModel;

import junit.textui.TestRunner;
import test.jts.GeometryTestCase;

public class OverlayNGTest extends GeometryTestCase {

  public static void main(String args[]) {
    TestRunner.run(OverlayNGTest.class);
  }

  public OverlayNGTest(String name) { super(name); }
  
  public void testEmptyAPolygonIntersection() {
    Geometry a = read("POLYGON EMPTY");
    Geometry b = read("POLYGON ((1 0, 2 5, 3 0, 1 0))");
    Geometry expected = read("POLYGON EMPTY");
    Geometry actual = intersection(a, b, 1);
    checkEqual(expected, actual);
  }
  
  public void testEmptyBIntersection() {
    Geometry a = read("POLYGON ((1 0, 2 5, 3 0, 1 0))");
    Geometry b = read("POLYGON EMPTY");
    Geometry expected = read("POLYGON EMPTY");
    Geometry actual = intersection(a, b, 1);
    checkEqual(expected, actual);
  }
  
  public void testEmptyABIntersection() {
    Geometry a = read("POLYGON EMPTY");
    Geometry b = read("POLYGON EMPTY");
    Geometry expected = read("POLYGON EMPTY");
    Geometry actual = intersection(a, b, 1);
    checkEqual(expected, actual);
  }
  
  public void testEmptyADifference() {
    Geometry a = read("POLYGON EMPTY");
    Geometry b = read("POLYGON ((1 0, 2 5, 3 0, 1 0))");
    Geometry expected = read("POLYGON EMPTY");
    Geometry actual = difference(a, b, 1);
    checkEqual(expected, actual);
  }
  
  public void testEmptyAUnion() {
    Geometry a = read("POLYGON EMPTY");
    Geometry b = read("POLYGON ((1 0, 2 5, 3 0, 1 0))");
    Geometry expected = read("POLYGON ((1 0, 2 5, 3 0, 1 0))");
    Geometry actual = union(a, b, 1);
    checkEqual(expected, actual);
  }
  
  public void testEmptyASymDifference() {
    Geometry a = read("POLYGON EMPTY");
    Geometry b = read("POLYGON ((1 0, 2 5, 3 0, 1 0))");
    Geometry expected = read("POLYGON ((1 0, 2 5, 3 0, 1 0))");
    Geometry actual = symDifference(a, b, 1);
    checkEqual(expected, actual);
  }
  
  public void testEmptyLinePolygonIntersection() {
    Geometry a = read("LINESTRING EMPTY");
    Geometry b = read("POLYGON ((1 0, 2 5, 3 0, 1 0))");
    Geometry expected = read("LINESTRING EMPTY");
    Geometry actual = intersection(a, b, 1);
    checkEqual(expected, actual);
  }
  
  public void testEmptyLinePolygonDifference() {
    Geometry a = read("LINESTRING EMPTY");
    Geometry b = read("POLYGON ((1 0, 2 5, 3 0, 1 0))");
    Geometry expected = read("LINESTRING EMPTY");
    Geometry actual = difference(a, b, 1);
    checkEqual(expected, actual);
  }
  
  public void testEmptyPointPolygonIntersection() {
    Geometry a = read("POINT EMPTY");
    Geometry b = read("POLYGON ((1 0, 2 5, 3 0, 1 0))");
    Geometry expected = read("POINT EMPTY");
    Geometry actual = intersection(a, b, 1);
    checkEqual(expected, actual);
  }
  
  public void testDisjointIntersection() {
    Geometry a = read("POLYGON ((60 90, 90 90, 90 60, 60 60, 60 90))");
    Geometry b = read("POLYGON ((200 300, 300 300, 300 200, 200 200, 200 300))");
    Geometry expected = read("POLYGON EMPTY");
    Geometry actual = intersection(a, b, 1);
    checkEqual(expected, actual);
  }
  
  public void testDisjointIntersectionNoOpt() {
    Geometry a = read("POLYGON ((60 90, 90 90, 90 60, 60 60, 60 90))");
    Geometry b = read("POLYGON ((200 300, 300 300, 300 200, 200 200, 200 300))");
    Geometry expected = read("POLYGON EMPTY");
    Geometry actual = intersectionNoOpt(a, b, 1);
    checkEqual(expected, actual);
  }
  
  public void testBoxTriIntersection() {
    Geometry a = read("POLYGON ((0 6, 4 6, 4 2, 0 2, 0 6))");
    Geometry b = read("POLYGON ((1 0, 2 5, 3 0, 1 0))");
    Geometry expected = read("POLYGON ((3 2, 1 2, 2 5, 3 2))");
    Geometry actual = intersection(a, b, 1);
    checkEqual(expected, actual);
  }
  
  public void testBoxTriUnion() {
    Geometry a = read("POLYGON ((0 6, 4 6, 4 2, 0 2, 0 6))");
    Geometry b = read("POLYGON ((1 0, 2 5, 3 0, 1 0))");
    Geometry expected = read("POLYGON ((0 6, 4 6, 4 2, 3 2, 3 0, 1 0, 1 2, 0 2, 0 6))");
    Geometry actual = union(a, b, 1);
    checkEqual(expected, actual);
  }
  
  public void test2spikesIntersection() {
    Geometry a = read("POLYGON ((0 100, 40 100, 40 0, 0 0, 0 100))");
    Geometry b = read("POLYGON ((70 80, 10 80, 60 50, 11 20, 69 11, 70 80))");
    Geometry expected = read("MULTIPOLYGON (((40 80, 40 62, 10 80, 40 80)), ((40 38, 40 16, 11 20, 40 38)))");
    Geometry actual = intersection(a, b, 1);
    checkEqual(expected, actual);
  }
  
  public void test2spikesUnion() {
    Geometry a = read("POLYGON ((0 100, 40 100, 40 0, 0 0, 0 100))");
    Geometry b = read("POLYGON ((70 80, 10 80, 60 50, 11 20, 69 11, 70 80))");
    Geometry expected = read("POLYGON ((0 100, 40 100, 40 80, 70 80, 69 11, 40 16, 40 0, 0 0, 0 100), (40 62, 40 38, 60 50, 40 62))");
    Geometry actual = union(a, b, 1);
    checkEqual(expected, actual);
  }
  
  public void testTriBoxIntersection() {
    Geometry a = read("POLYGON ((68 35, 35 42, 40 9, 68 35))");
    Geometry b = read("POLYGON ((20 60, 50 60, 50 30, 20 30, 20 60))");
    Geometry expected = read("POLYGON ((37 30, 35 42, 50 39, 50 30, 37 30))");
    Geometry actual = intersection(a, b, 1);
    checkEqual(expected, actual);
  }  
  
  public void testNestedShellsIntersection() {
    Geometry a = read("POLYGON ((100 200, 200 200, 200 100, 100 100, 100 200))");
    Geometry b = read("POLYGON ((120 180, 180 180, 180 120, 120 120, 120 180))");
    Geometry expected = read("POLYGON ((120 180, 180 180, 180 120, 120 120, 120 180))");
    Geometry actual = intersection(a, b, 1);
    checkEqual(expected, actual);
  }
  
  public void testNestedShellsUnion() {
    Geometry a = read("POLYGON ((100 200, 200 200, 200 100, 100 100, 100 200))");
    Geometry b = read("POLYGON ((120 180, 180 180, 180 120, 120 120, 120 180))");
    Geometry expected = read("POLYGON ((100 200, 200 200, 200 100, 100 100, 100 200))");
    Geometry actual = union(a, b, 1);
    checkEqual(expected, actual);
  }
  
  public void testATouchingNestedPolyUnion() {
    Geometry a = read("MULTIPOLYGON (((0 200, 200 200, 200 0, 0 0, 0 200), (50 50, 190 50, 50 200, 50 50)), ((60 100, 100 60, 50 50, 60 100)))");
    Geometry b = read("POLYGON ((135 176, 180 176, 180 130, 135 130, 135 176))");
    Geometry expected = read("MULTIPOLYGON (((0 0, 0 200, 50 200, 200 200, 200 0, 0 0), (50 50, 190 50, 50 200, 50 50)), ((50 50, 60 100, 100 60, 50 50)))");
    Geometry actual = union(a, b, 1);
    checkEqual(expected, actual);
  }

  public void testTouchingPolyDifference() {
    Geometry a = read("POLYGON ((200 200, 200 0, 0 0, 0 200, 200 200), (100 100, 50 100, 50 200, 100 100))");
    Geometry b = read("POLYGON ((150 100, 100 100, 150 200, 150 100))");
    Geometry expected = read("MULTIPOLYGON (((0 0, 0 200, 50 200, 50 100, 100 100, 150 100, 150 200, 200 200, 200 0, 0 0)), ((50 200, 150 200, 100 100, 50 200)))");
    Geometry actual = difference(a, b, 1);
    checkEqual(expected, actual);
  }

  public void testTouchingHoleUnion() {
    Geometry a = read("POLYGON ((100 300, 300 300, 300 100, 100 100, 100 300), (200 200, 150 200, 200 300, 200 200))");
    Geometry b = read("POLYGON ((130 160, 260 160, 260 120, 130 120, 130 160))");
    Geometry expected = read("POLYGON ((100 100, 100 300, 200 300, 300 300, 300 100, 100 100), (150 200, 200 200, 200 300, 150 200))");
    Geometry actual = union(a, b, 1);
    checkEqual(expected, actual);
  }
  
  public void testTouchingMultiHoleUnion() {
    Geometry a = read("POLYGON ((100 300, 300 300, 300 100, 100 100, 100 300), (200 200, 150 200, 200 300, 200 200), (250 230, 216 236, 250 300, 250 230), (235 198, 300 200, 237 175, 235 198))");
    Geometry b = read("POLYGON ((130 160, 260 160, 260 120, 130 120, 130 160))");
    Geometry expected = read("POLYGON ((100 300, 200 300, 250 300, 300 300, 300 200, 300 100, 100 100, 100 300), (200 300, 150 200, 200 200, 200 300), (250 300, 216 236, 250 230, 250 300), (300 200, 235 198, 237 175, 300 200))");
    Geometry actual = union(a, b, 1);
    checkEqual(expected, actual);
  }
  
  public void testBoxLineIntersection() {
    Geometry a = read("POLYGON ((100 200, 200 200, 200 100, 100 100, 100 200))");
    Geometry b = read("LINESTRING (50 150, 150 150)");
    Geometry expected = read("LINESTRING (100 150, 150 150)");
    Geometry actual = intersection(a, b, 1);
    checkEqual(expected, actual);
  }
  
  public void testBoxLineUnion() {
    Geometry a = read("POLYGON ((100 200, 200 200, 200 100, 100 100, 100 200))");
    Geometry b = read("LINESTRING (50 150, 150 150)");
    Geometry expected = read("GEOMETRYCOLLECTION (LINESTRING (50 150, 100 150), POLYGON ((100 200, 200 200, 200 100, 100 100, 100 150, 100 200)))");
    Geometry actual = union(a, b, 1);
    checkEqual(expected, actual);
  }
  
  public void testAdjacentBoxesIntersection() {
    Geometry a = read("POLYGON ((100 200, 200 200, 200 100, 100 100, 100 200))");
    Geometry b = read("POLYGON ((300 200, 300 100, 200 100, 200 200, 300 200))");
    Geometry expected = read("LINESTRING (200 100, 200 200)");
    Geometry actual = intersection(a, b, 1);
    checkEqual(expected, actual);
  }
  
  public void testAdjacentBoxesUnion() {
    Geometry a = read("POLYGON ((100 200, 200 200, 200 100, 100 100, 100 200))");
    Geometry b = read("POLYGON ((300 200, 300 100, 200 100, 200 200, 300 200))");
    Geometry expected = read("POLYGON ((100 100, 100 200, 200 200, 300 200, 300 100, 200 100, 100 100))");
    Geometry actual = union(a, b, 1);
    checkEqual(expected, actual);
  }
  
  public void testCollapseBoxGoreIntersection() {
    Geometry a = read("MULTIPOLYGON (((1 1, 5 1, 5 0, 1 0, 1 1)), ((1 1, 5 2, 5 4, 1 4, 1 1)))");
    Geometry b = read("POLYGON ((1 0, 1 2, 2 2, 2 0, 1 0))");
    Geometry expected = read("POLYGON ((2 0, 1 0, 1 1, 1 2, 2 2, 2 1, 2 0))");
    Geometry actual = intersection(a, b, 1);
    checkEqual(expected, actual);
  }
  
  public void testCollapseBoxGoreUnion() {
    Geometry a = read("MULTIPOLYGON (((1 1, 5 1, 5 0, 1 0, 1 1)), ((1 1, 5 2, 5 4, 1 4, 1 1)))");
    Geometry b = read("POLYGON ((1 0, 1 2, 2 2, 2 0, 1 0))");
    Geometry expected = read("POLYGON ((2 0, 1 0, 1 1, 1 2, 1 4, 5 4, 5 2, 2 1, 5 1, 5 0, 2 0))");
    Geometry actual = union(a, b, 1);
    checkEqual(expected, actual);
  }
  public void testSnapBoxGoreIntersection() {
    Geometry a = read("MULTIPOLYGON (((1 1, 5 1, 5 0, 1 0, 1 1)), ((1 1, 5 2, 5 4, 1 4, 1 1)))");
    Geometry b = read("POLYGON ((4 3, 5 3, 5 0, 4 0, 4 3))");
    Geometry expected = read("MULTIPOLYGON (((4 3, 5 3, 5 2, 4 2, 4 3)), ((4 0, 4 1, 5 1, 5 0, 4 0)))");
    Geometry actual = intersection(a, b, 1);
    checkEqual(expected, actual);
  }
  
  public void testSnapBoxGoreUnion() {
    Geometry a = read("MULTIPOLYGON (((1 1, 5 1, 5 0, 1 0, 1 1)), ((1 1, 5 2, 5 4, 1 4, 1 1)))");
    Geometry b = read("POLYGON ((4 3, 5 3, 5 0, 4 0, 4 3))");
    Geometry expected = read("POLYGON ((1 1, 1 4, 5 4, 5 3, 5 2, 5 1, 5 0, 4 0, 1 0, 1 1), (1 1, 4 1, 4 2, 1 1))");
    Geometry actual = union(a, b, 1);
    checkEqual(expected, actual);
  }
  
  public void testCollapseTriBoxIntersection() {
    Geometry a = read("POLYGON ((1 2, 1 1, 9 1, 1 2))");
    Geometry b = read("POLYGON ((9 2, 9 1, 8 1, 8 2, 9 2))");
    Geometry expected = read("POLYGON EMPTY");
    Geometry actual = intersection(a, b, 1);
    checkEqual(expected, actual);
  }
  
  public void testCollapseTriBoxUnion() {
    Geometry a = read("POLYGON ((1 2, 1 1, 9 1, 1 2))");
    Geometry b = read("POLYGON ((9 2, 9 1, 8 1, 8 2, 9 2))");
    Geometry expected = read("MULTIPOLYGON (((1 1, 1 2, 8 1, 1 1)), ((8 1, 8 2, 9 2, 9 1, 8 1)))");
    Geometry actual = union(a, b, 1);
    checkEqual(expected, actual);
  }

  /**
   * Fails because polygon A collapses totally, but one
   * L edge is still labelled with location A:iL due to being located
   * inside original A polygon by PiP test for incomplete edges.
   * That edge is then marked as in-result-area, but 
   * it is the only edge marked in-result, so result ring can't
   * be formed because ring is incomplete
   */
  public void testCollapseAIncompleteRingUnion() {
    Geometry a = read("POLYGON ((0.9 1.7, 1.3 1.4, 2.1 1.4, 2.1 0.9, 1.3 0.9, 0.9 0, 0.9 1.7))");
    Geometry b = read("POLYGON ((1 3, 3 3, 3 1, 1.3 0.9, 1 0.4, 1 3))");
    Geometry expected = read("POLYGON ((1 1, 1 2, 1 3, 3 3, 3 1, 2 1, 1 1))");
    Geometry actual = union(a, b, 1);
    checkEqual(expected, actual);
  }
  
  /**
   * Fails because edge of B is computed as Interior to A because it
   * is checked against full precision input, rather than collapsed linework.
   * 
   * Probably need to determine location against output rings
   */
  public void testCollapseResultShouldHavePolygonUnion() {
    Geometry a = read("POLYGON ((1 3.3, 1.3 1.4, 3.1 1.4, 3.1 0.9, 1.3 0.9, 1 -0.2, 0.8 1.3, 1 3.3))");
    Geometry b = read("POLYGON ((1 2.9, 2.9 2.9, 2.9 1.3, 1.7 1, 1.3 0.9, 1 0.4, 1 2.9))");
    Geometry expected = read("POLYGON ((1 1, 1 3, 3 3, 3 1, 2 1, 1 1))");
    Geometry actual = union(a, b, 1);
    checkEqual(expected, actual);
  }
  
  /**
   * Fails because current isResultAreaEdge does not accept L edges as result area boundary
   */
  public void testCollapseHoleAlongEdgeOfBIntersection() {
    Geometry a = read("POLYGON ((0 3, 3 3, 3 0, 0 0, 0 3), (1 1.2, 1 1.1, 2.3 1.1, 1 1.2))");
    Geometry b = read("POLYGON ((1 1, 2 1, 2 0, 1 0, 1 1))");
    Geometry expected = read("POLYGON ((1 1, 2 1, 2 0, 1 0, 1 1))");
    Geometry actual = intersection(a, b, 1);
    checkEqual(expected, actual);
  }

  /**
   * Fails because A holes collapse to L edges, and are not computed as in Int of A,
   * so are not included as result area edges.
   */
  public void testCollapseHolesAlongAllEdgesOfBIntersection() {
    Geometry a = read("POLYGON ((0 3, 3 3, 3 0, 0 0, 0 3), (1 2.2, 1 2.1, 2 2.1, 1 2.2), (2.1 2, 2.2 2, 2.1 1, 2.1 2), (2 0.9, 2 0.8, 1 0.9, 2 0.9), (0.9 1, 0.8 1, 0.9 2, 0.9 1))");
    Geometry b = read("POLYGON ((1 2, 2 2, 2 1, 1 1, 1 2))");
    Geometry expected = read("POLYGON ((1 2, 2 2, 2 1, 1 1, 1 2))");
    Geometry actual = intersection(a, b, 1);
    checkEqual(expected, actual);
  }

  public void testVerySmallBIntersection() {
    Geometry a = read("POLYGON ((2.526855443750341 48.82324221874807, 2.5258255 48.8235855, 2.5251389 48.8242722, 2.5241089 48.8246155, 2.5254822 48.8246155, 2.5265121 48.8242722, 2.526855443750341 48.82324221874807))");
    Geometry b = read("POLYGON ((2.526512100000002 48.824272199999996, 2.5265120999999953 48.8242722, 2.5265121 48.8242722, 2.526512100000002 48.824272199999996))");
    Geometry expected = read("POLYGON EMPTY");
    Geometry actual = intersection(a, b, 100000000);
    checkEqual(expected, actual);
  }
  
  /**
   * Currently noding is incorrect, producing one 2pt edge which is coincident
   * with a 3-pt edge.  The EdgeMerger doesn't check that merged edges are identical,
   * so merges the 3pt edge into the 2-pt edge.
   * FIXED by better noding.
   */
  public void testEdgeDisappears() {
    Geometry a = read("LINESTRING (2.1279144 48.8445282, 2.126884443750796 48.84555818124935, 2.1268845 48.8455582, 2.1268845 48.8462448)");
    Geometry b = read("LINESTRING EMPTY");
    Geometry expected = read("LINESTRING EMPTY");
    Geometry actual = intersection(a, b, 1000000);
    checkEqual(expected, actual);
  }

  /**
   * Probably due to B collapsing completely and disconnected edges being located incorrectly in B interior.
   * Have seen other cases of this as well.
   * Also - a B edge is marked as a Hole, which is incorrect.
   * 
   * FIXED - copy-paste error in Edge.mergedRingRole
   */
  public void testBcollapseLocateIssue() {
    Geometry a = read("POLYGON ((2.3442078 48.9331054, 2.3435211 48.9337921, 2.3428345 48.9358521, 2.3428345 48.9372253, 2.3433495 48.9370537, 2.3440361 48.936367, 2.3442078 48.9358521, 2.3442078 48.9331054))");
    Geometry b = read("POLYGON ((2.3442078 48.9331054, 2.3435211 48.9337921, 2.3433494499999985 48.934307100000005, 2.3438644 48.9341354, 2.3442078 48.9331055, 2.3442078 48.9331054))");
    Geometry expected = read("POLYGON EMPTY");
    Geometry actual = intersection(a, b, 1000);
    checkEqual(expected, actual);
  }
  
  /**
   * Fails because a component of B collapses completely and labelling is wrong.
   * Labelling marks a single collapsed edge as B:i.
   * Edge is only connected to two other edges both marked B:e.
   * B:i edge is included in area result edges, and faild because it does not form a ring.
   * 
   * Perhaps a fix is to ignore connected single Bi edges which do not form a ring?
   * This may be dangerous since it may hide other labelling problems?
   * 
   * FIXED by computing location of both edge endpoints.
   */
  public void testBcollapseEdgeLabeledInterior() {
    Geometry a = read("POLYGON ((2.384376506250038 48.91765596875102, 2.3840332 48.916626, 2.3840332 48.9138794, 2.3833466 48.9118195, 2.3812866 48.9111328, 2.37854 48.9111328, 2.3764801 48.9118195, 2.3723602 48.9159393, 2.3703003 48.916626, 2.3723602 48.9173126, 2.3737335 48.9186859, 2.3757935 48.9193726, 2.3812866 48.9193726, 2.3833466 48.9186859, 2.384376506250038 48.91765596875102))");
    Geometry b = read("MULTIPOLYGON (((2.3751067666731345 48.919143677778855, 2.3757935 48.9193726, 2.3812866 48.9193726, 2.3812866 48.9179993, 2.3809433 48.9169693, 2.3799133 48.916626, 2.3771667 48.916626, 2.3761368 48.9169693, 2.3754501 48.9190292, 2.3751067666731345 48.919143677778855)), ((2.3826108673454116 48.91893115612326, 2.3833466 48.9186859, 2.3840331750033394 48.91799930833141, 2.3830032 48.9183426, 2.3826108673454116 48.91893115612326)))");
    Geometry expected = read("POLYGON ((2.375 48.91833333333334, 2.375 48.92, 2.381666666666667 48.92, 2.381666666666667 48.91833333333334, 2.381666666666667 48.916666666666664, 2.38 48.916666666666664, 2.3766666666666665 48.916666666666664, 2.375 48.91833333333334))");
    Geometry actual = intersection(a, b, 600);
    checkEqual(expected, actual);
  }
  
  /**
   * This failure is due to B inverting due to an snapped intersection being added 
   * to a segment by a nearby vertex, and the snap vertex "jumped" across another segment.
   * This is because the nearby snap intersection tolerance in SnapIntersectionAdder was too large (FACTOR = 10).
   * 
   * FIXED by reducing the tolerance factor to 100.
   * 
   * However, it may be that there is no safe tolerance level?  
   * Perhaps there can always be situations where a snap intersection will jump across a segment?
   */
  public void testBNearVertexSnappingCausesInversion() {
    Geometry a = read("POLYGON ((2.2494507 48.8864136, 2.2484207 48.8867569, 2.2477341 48.8874435, 2.2470474 48.8874435, 2.2463608 48.8853836, 2.2453308 48.8850403, 2.2439575 48.8850403, 2.2429276 48.8853836, 2.2422409 48.8860703, 2.2360611 48.8970566, 2.2504807 48.8956833, 2.2494507 48.8864136))");
    Geometry b = read("POLYGON ((2.247734099999997 48.8874435, 2.2467041 48.8877869, 2.2453308 48.8877869, 2.2443008 48.8881302, 2.243957512499544 48.888473487500455, 2.2443008 48.8888168, 2.2453308 48.8891602, 2.2463608 48.8888168, 2.247734099999997 48.8874435))");
    Geometry expected = read("POLYGON EMPTY");
    Geometry actual = intersection(a, b, 200);
    checkEqual(expected, actual);
  }
  
  public static Geometry difference(Geometry a, Geometry b, double scaleFactor) {
    PrecisionModel pm = new PrecisionModel(scaleFactor);
    return OverlayNG.overlay(a, b, pm, DIFFERENCE);
  }
  
  public static Geometry symDifference(Geometry a, Geometry b, double scaleFactor) {
    PrecisionModel pm = new PrecisionModel(scaleFactor);
    return OverlayNG.overlay(a, b, pm, SYMDIFFERENCE);
  }
  
  public static Geometry intersection(Geometry a, Geometry b, double scaleFactor) {
    PrecisionModel pm = new PrecisionModel(scaleFactor);
    return OverlayNG.overlay(a, b, pm, INTERSECTION);
  }
  
  public static Geometry intersectionNoOpt(Geometry a, Geometry b, double scaleFactor) {
    PrecisionModel pm = new PrecisionModel(scaleFactor);
    OverlayNG ov = new OverlayNG(a, b, pm, INTERSECTION);
    ov.setOptimized(false);
    return ov.getResultGeometry();
  }
  
  public static Geometry union(Geometry a, Geometry b, double scaleFactor) {
    PrecisionModel pm = new PrecisionModel(scaleFactor);
    return OverlayNG.overlay(a, b, pm, UNION);
  }
  
}
