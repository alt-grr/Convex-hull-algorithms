package it.sieradzki.convex_hull.algorithm;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingDouble;

public class QuickHull implements ConvexHullAlgorithm {

	@Override
	public List<Point2D> convexHullOf(Set<Point2D> points) {

		if (points == null || points.size() < 3) {
			throw new IllegalArgumentException("Number of points can't be less than 3");
		}

		Point2D minXminY =
				points.stream().min(comparingDouble(Point2D::getY).thenComparingDouble(Point2D::getX)).get();
		Point2D maxXmaxY =
				points.stream().max(comparingDouble(Point2D::getY).thenComparingDouble(Point2D::getX)).get();

		Set<Point2D> left = new HashSet<>();
		Set<Point2D> right = new HashSet<>();
		points.stream()
				.filter(point -> !point.equals(minXminY) && !point.equals(maxXmaxY))
				.forEach(point -> {
					double onTheLeft = GeometryUtil.onLeftSideOfLine(point, minXminY, maxXmaxY);
					if (onTheLeft > 0) {
						left.add(point);
					} else if (onTheLeft < 0) {
						right.add(point);
					}
				});

		List<Point2D> hull = new ArrayList<>();
		hull.add(minXminY);
		hull.addAll(quickHull(minXminY, maxXmaxY, right));
		hull.add(maxXmaxY);
		hull.addAll(quickHull(maxXmaxY, minXminY, left));

		return hull;
	}

	private List<Point2D> quickHull(Point2D a, Point2D b, Set<Point2D> points) {

		List<Point2D> hull = new ArrayList<>();

		if (points.size() < 2) {
			hull.addAll(points);
			return hull;
		}

		Point2D topmost = points.stream().max(comparing(point -> GeometryUtil.distanceFromLine(point, a, b))).get();
		Triangle triangle = new Triangle(a, b, topmost);

		Set<Point2D> outsideOfTriangle =
				points.stream().filter(point -> !triangle.contains(point)).collect(Collectors.toSet());

		Set<Point2D> left = new HashSet<>();
		Set<Point2D> right = new HashSet<>();
		outsideOfTriangle.stream()
				.filter(point -> !points.equals(topmost))
				.forEach(point -> {
					double onTheLeft = GeometryUtil.onLeftSideOfLine(point, a, topmost);
					if (onTheLeft > 0) {
						left.add(point);
					} else if (onTheLeft < 0) {
						right.add(point);
					}
				});

		hull.addAll(quickHull(a, topmost, right));
		hull.add(topmost);
		hull.addAll(quickHull(topmost, b, left));

		return hull;
	}


	/**
	 * Adapted from http://stackoverflow.com/a/25346777
	 */
	private class Triangle {

		private final double x3, y3;
		private final double y23, x32, y31, x13;
		private final double det, minD, maxD;

		Triangle(Point2D a, Point2D b, Point2D c) {
			this(a.getX(), a.getY(), b.getX(), b.getY(), c.getX(), c.getY());
		}

		Triangle(double x1, double y1, double x2, double y2, double x3, double y3) {

			this.x3 = x3;
			this.y3 = y3;
			y23 = y2 - y3;
			x32 = x3 - x2;
			y31 = y3 - y1;
			x13 = x1 - x3;
			det = y23 * x13 - x32 * y31;
			minD = Math.min(det, 0);
			maxD = Math.max(det, 0);
		}

		boolean contains(Point2D point) {
			return contains(point.getX(), point.getY());
		}

		boolean contains(double x, double y) {

			double dx = x - x3;
			double dy = y - y3;
			double a = y23 * dx + x32 * dy;

			if (a < minD || a > maxD) {
				return false;
			}

			double b = y31 * dx + x13 * dy;
			if (b < minD || b > maxD) {
				return false;
			}

			double c = det - a - b;
			if (c < minD || c > maxD) {
				return false;
			}

			return true;
		}
	}
}
