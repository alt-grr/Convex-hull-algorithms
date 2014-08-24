package it.sieradzki.convex_hull.algorithm;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.Comparator.comparingDouble;

public class JarvisMarch implements ConvexHullAlgorithm {

	@Override
	public List<Point2D> convexHullOf(Set<Point2D> points) {

		if (points == null || points.size() < 3) {
			throw new IllegalArgumentException("Number of points can't be less than 3");
		}

		List<Point2D> convexHull = new ArrayList<>();

		// Point with top right most position
		Point2D pointOnHull =
				points.stream().max(comparingDouble(Point2D::getX).thenComparingDouble(Point2D::getY)).get();

		Point2D firstPointFromSet = points.stream().findFirst().get();
		Point2D nextPointOnHull;
		do {
			convexHull.add(pointOnHull);

			nextPointOnHull = firstPointFromSet;
			for (Point2D point : points) {
				if (nextPointOnHull.equals(pointOnHull)) {
					nextPointOnHull = point;
					continue;
				}

				double onLeft = GeometryUtil.onLeftSideOfLine(point, pointOnHull, nextPointOnHull);
				if (onLeft > 0 || onLeft == 0 && point.distance(pointOnHull) > nextPointOnHull.distance(pointOnHull)) {
					nextPointOnHull = point;
				}
			}

			pointOnHull = nextPointOnHull;
		} while (!nextPointOnHull.equals(convexHull.get(0)));

		return convexHull;
	}
}
