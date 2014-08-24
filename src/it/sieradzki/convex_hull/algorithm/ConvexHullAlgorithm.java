package it.sieradzki.convex_hull.algorithm;

import javafx.geometry.Point2D;

import java.util.List;
import java.util.Set;

@FunctionalInterface
public interface ConvexHullAlgorithm {

	List<Point2D> convexHullOf(Set<Point2D> points);
}
