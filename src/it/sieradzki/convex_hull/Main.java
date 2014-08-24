package it.sieradzki.convex_hull;

import it.sieradzki.convex_hull.algorithm.GrahamScan;
import it.sieradzki.convex_hull.algorithm.JarvisMarch;
import it.sieradzki.convex_hull.algorithm.QuickHull;
import javafx.geometry.Point2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static java.lang.System.out;

public class Main {

	public static void main(String[] args) {

		Set<Point2D> points = ConvexHullSolver.generatePoints(300, 10, 490);
//		Set<Point2D> points = new LinkedHashSet<Point2D>() {{
//			add(new Point2D(5, 5));
//			add(new Point2D(5, 0));
//			add(new Point2D(5, -3));
//			add(new Point2D(-1, -1));
//			add(new Point2D(-7, -7));
//			add(new Point2D(-7, -3));
//			add(new Point2D(-8, 4));
//			add(new Point2D(-1, 2));
//		}};

		out.println("Points: " + points);

		List<Point2D> grahamHull = ConvexHullSolver.solve(points, new GrahamScan());
		out.println("[GRAHAM] Points of hull: " + grahamHull);

		List<Point2D> jarvisHull = ConvexHullSolver.solve(points, new JarvisMarch());
		out.println("[JARVIS] Points of hull: " + jarvisHull);

		List<Point2D> quickHull = ConvexHullSolver.solve(points, new QuickHull());
		out.println("[QUICK] Points of hull: " + quickHull);

		drawHull(points, grahamHull, "grahamHull.png");
		drawHull(points, jarvisHull, "jarvisHull.png");
		drawHull(points, quickHull, "quickHull.png");
	}

	private static void drawHull(Set<Point2D> points, List<Point2D> hull, String fileName) {

		BufferedImage image = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHint(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		graphics.setRenderingHint(
				RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);

		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, 500, 500);

		graphics.setColor(Color.RED);
		for (Point2D point : points) {
			graphics.fillRect((int) point.getX() - 1, (int) point.getY() - 1, 2, 2);
		}

		for (int i = 1; i < hull.size(); i++) {
			Point2D a = hull.get(i - 1);
			Point2D b = hull.get(i);

			graphics.setColor(Color.BLUE);
			graphics.fillRect((int) a.getX() - 3, (int) a.getY() - 3, 6, 6);

			graphics.setColor(Color.GREEN);
			graphics.drawLine((int) a.getX(), (int) a.getY(), (int) b.getX(), (int) b.getY());
		}

		Point2D last = hull.get(hull.size() - 1);
		Point2D first = hull.get(0);

		graphics.setColor(Color.BLUE);
		graphics.fillRect((int) last.getX() - 3, (int) last.getY() - 3, 6, 6);

		graphics.setColor(Color.GREEN);
		graphics.drawLine((int) last.getX(), (int) last.getY(), (int) first.getX(), (int) first.getY());

		try {
			ImageIO.write(image, "png", new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
