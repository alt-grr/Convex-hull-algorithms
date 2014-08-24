package it.sieradzki.convex_hull.algorithm;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingDouble;

public class GrahamScan implements ConvexHullAlgorithm {

	@Override
	public List<Point2D> convexHullOf(Set<Point2D> points) {

		if (points == null || points.size() < 3) {
			throw new IllegalArgumentException("Number of points can't be less than 3");
		}

		// Point with bottom left most position
		Point2D startingPoint =
				points.stream().min(comparingDouble(Point2D::getY).thenComparingDouble(Point2D::getX)).get();

		// All others points, sorted by polar angle, ascending
		List<Point2D> sortedPoints = points.stream()
				.filter(point -> !point.equals(startingPoint))
				.sorted(comparing((Point2D point) ->
								-(point.getX() - startingPoint.getX()) / (point.getY() - startingPoint.getY()))
				).collect(Collectors.toList());

		List<Point2D> stack = new ArrayList<>();
		stack.add(startingPoint);
		stack.add(sortedPoints.get(0));
		double clockwise;
		for (int i = 1; i < sortedPoints.size(); i++) {
			Point2D point = sortedPoints.get(i);

			while ((clockwise = areClockwise(stack.get(stack.size() - 2), stack.get(stack.size() - 1), point)) >= 0) {
				Point2D last = stack.get(stack.size() - 1);
				Point2D previousToLast = stack.get(stack.size() - 2);

				if (clockwise == 0 && point.distance(previousToLast) > last.distance(previousToLast)) {
					stack.remove(stack.size() - 1);
					stack.add(point);
				} else {
					stack.remove(stack.size() - 1);
				}
			}
			stack.add(point);
		}

		// Check if last 2 points are collinear with first point
		// If true, last point is redundant and must be removed
		Point2D first = stack.get(0);
		Point2D last = stack.get(stack.size() - 1);
		Point2D lastMinusOne = stack.get(stack.size() - 2);
		if (areClockwise(first, last, lastMinusOne) == 0) {
			stack.remove(last);
		}

		return stack;
	}

	/**
	 * Returns
	 * <ul>
	 *   <li>{@code > 0} if points are clockwise</li>
	 *   <li>{@code == 0} if points are collinear</li>
	 *   <li>{@code < 0} if points are counter-clockwise</li>
	 * </ul>
	 */
	private static double areClockwise(Point2D p1, Point2D p2, Point2D p3) {
		return -((p2.getX() - p1.getX()) * (p3.getY() - p1.getY()) - (p2.getY() - p1.getY()) * (p3.getX() - p1.getX()));
	}
}
