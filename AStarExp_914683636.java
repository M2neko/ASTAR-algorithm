import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;

/// A sample AI that takes a very suboptimal path.
/**
 * This is a sample AI that moves as far horizontally as necessary to reach the target,
 * then as far vertically as necessary to reach the target.  It is intended primarily as
 * a demonstration of the various pieces of the program.
 * 
 */

public class AStarExp_914683636 implements AIModule
{   
    /// Creates the path to the goal.
    public List<Point> createPath(final TerrainMap map)
    {
		// Create Path, Start, and End
        final ArrayList<Point> path = new ArrayList<Point>();
        Point startPoint = map.getStartPoint();
        Point endpoint = map.getEndPoint();
        
		// Came_From Hashmap and visited Set
        HashMap<Point, Point> parentPoint = new HashMap<Point, Point>();
        HashSet<Point> visiedPoint = new HashSet<Point>();
        Map<Point, Double> gScore = new HashMap<Point, Double>();
        
		// PriortyQueue to ensure always select the min first
        Queue<Point> priorityQueue = new PriorityQueue<Point>(new Comparator<Point>()
        {
            public int compare(final Point pt1, final Point pt2)
            {
				// f(n) = g(n) + h(n)
                double dpt1 = gScore.get(pt1) + getHeuristic(map, pt1, endpoint);
                double dpt2 = gScore.get(pt2) + getHeuristic(map, pt2, endpoint);
                return dpt1 > dpt2 ? 1 : (dpt1 < dpt2 ? -1 : 0);
            }
        }
        );
        
        gScore.put(startPoint, 0.0);
        priorityQueue.add(startPoint);
        
        Point currentPoint = null;
        
        while (!priorityQueue.isEmpty())
        {
			// Min point first
            currentPoint = priorityQueue.remove();
            
            if (!visiedPoint.contains(currentPoint))
            {
                visiedPoint.add(currentPoint);
				// At EndPoint
                if (currentPoint.equals(endpoint))
                    break;
            }
            
			// Get Neighbors
            Point[] neighborPoints = map.getNeighbors(currentPoint);
            for (Point neighborPoint : neighborPoints)
            {
                if (!visiedPoint.contains(neighborPoint))
                {
                    double tScore = gScore.get(currentPoint) + map.getCost(currentPoint, neighborPoint);
                    if (!gScore.containsKey(neighborPoint) || tScore < gScore.get(neighborPoint))
                    {
						// Update gScore
                        gScore.put(neighborPoint, tScore);
                        parentPoint.put(neighborPoint, currentPoint);
                        priorityQueue.add(neighborPoint);
                    }
                }
            }
        }
        
        currentPoint = endpoint;
        path.add(0, currentPoint);
        
		// Recursively load into path
        while (!currentPoint.equals(startPoint))
        {
            currentPoint = parentPoint.get(currentPoint);
            path.add(0, currentPoint);
        }
        return path;
    }
	
	// The Heuristic to calculate Exp
    private double getHeuristic(final TerrainMap map, final Point pt1, final Point pt2)
    {
		// Chebyshev Distance
        double DistanceDifference = Math.max(Math.abs(pt1.x - pt2.x), Math.abs(pt1.y - pt2.y));
        double HeightDifference = map.getTile(pt2) - map.getTile(pt1);
		// Proved in Part1.pdf
        return DistanceDifference * Math.pow(2.0, HeightDifference / DistanceDifference);
    }

}
