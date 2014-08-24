package it.sieradzki.convex_hull.algorithm;

import javafx.geometry.Point2D;

class GeometryUtil {

	static double onLeftSideOfLine(Point2D point, Point2D a, Point2D b) {
		return (b.getX() - a.getX()) * (point.getY() - a.getY()) -
				(point.getX() - a.getX()) * (b.getY() - a.getY());
	}

	static double distanceFromLine(Point2D point, Point2D a, Point2D b) {
		return Math.abs(GeometryUtil.onLeftSideOfLine(point, a, b));
	}
}
