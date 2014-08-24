package it.sieradzki.convex_hull;

import it.sieradzki.convex_hull.algorithm.ConvexHullAlgorithm;
import javafx.geometry.Point2D;

import java.util.*;

public class ConvexHullSolver {

	private static final Random RANDOM = new Random();

	public static List<Point2D> solve(Set<Point2D> points, ConvexHullAlgorithm algorithm) {
		return algorithm.convexHullOf(points);
	}

	public static Set<Point2D> generatePoints(int pointsNumber, int minCoordinate, int maxCoordinate) {

		if (pointsNumber <= 0) {
			throw new IllegalArgumentException("pointsNumber must greater than 0");
		}

		if (minCoordinate >= maxCoordinate) {
			throw new IllegalArgumentException("minCoordinate must less than maxCoordinate");
		}

		Set<Point2D> points = new LinkedHashSet<>(pointsNumber, 1.0f);
		for (int i = 0; i < pointsNumber; i++) {
			int x = RANDOM.nextInt((maxCoordinate - minCoordinate) + 1) + minCoordinate;
			int y = RANDOM.nextInt((maxCoordinate - minCoordinate) + 1) + minCoordinate;
			points.add(new Point2D(x, y));
		}
		return points;
	}
}
