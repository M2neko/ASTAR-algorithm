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

public class MtStHelensDiv_914683636 implements AIModule
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
				// Revised Comparator
                return dpt1 > dpt2 ? 1 : -1;
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
				{
					// Revised return style
					for (Point tempPoint = endpoint; tempPoint != null; tempPoint = parentPoint.get(tempPoint))
						path.add(0, tempPoint);
					return path;
				}
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
		return null;
    }
	
	// The Heuristic to calculate Div
    private double getHeuristic(final TerrainMap map, final Point pt1, final Point pt2)
    {
		// Chebyshev Distance
        double DistanceDifference = Math.max(Math.abs(pt1.x - pt2.x), Math.abs(pt1.y - pt2.y));
        double RatioHeight = map.getTile(pt1) / (map.getTile(pt2) + 1.0);
		// Revised Heuristic
		// Proved in Part4.pdf
        return DistanceDifference / 2.0 * Math.pow(RatioHeight * 2.0, 1.0 / DistanceDifference);
    }

}
