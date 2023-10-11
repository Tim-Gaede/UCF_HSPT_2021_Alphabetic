import java.util.*;

public class roadtrip {
	// the number of letters we consider
	static final int numLets = 'J' - 'A' + 1;
	// a large number
	static final long big = Long.MAX_VALUE / 4;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		// test cases
		int t = sc.nextInt();
		for(int tt = 1; tt <= t; ++tt) {
			// cities and roads
			int n = sc.nextInt(), m = sc.nextInt();
			// city names
			
			// we will create a map of each city name to its
			// respective index, to keep things simple with
			// numbers rather than strings
			String[] cities = new String[n];
			HashMap<String, Integer> cityIndex = new HashMap<>();
			for(int i = 0; i < n; ++i) {
				cities[i] = sc.next();
				cityIndex.put(cities[i], i);
			}
			
			// create our adjacency list
			ArrayDeque<edge>[] edges = new ArrayDeque[n];
			for(int i = 0; i < n; ++i) {
				edges[i] = new ArrayDeque<>();
			}
			
			for(int i = 0; i < m; ++i) {
				String cityA = sc.next(), cityB = sc.next();
				// use the map to get the respective indices
				int a = cityIndex.get(cityA), b = cityIndex.get(cityB);
				int w = sc.nextInt();
				
				// add edges in both directions
				edges[a].add(new edge(b, w));
				edges[b].add(new edge(a, w));
			}
			
			// the key to this solution is that we will split up each node
			// into 10 nodes, one for each letter between 'A' and 'J';
			// then, we will compute the shortest distance to each node for
			// each letter we have acquired along our shortest path;
			// this method is called a node-splitting Dijkstra's
			
			long[][] dist = new long[numLets][n];
			// initially we will fill our distance array with very large values
			for(long[] a : dist) {
				Arrays.fill(a, big);
			}
			// create a PriorityQueue of states
			PriorityQueue<state> pq = new PriorityQueue<>();
			
			// go through each city and consider candidates for our starting city
			// (those with the first letter A)
			for(int i = 0; i < n; ++i) {
				if(cities[i].charAt(0) == 'A') {
					// set the distance to zero, as we possibly start here
					dist[0][i] = 0;
					// add this as a start to the queue
					pq.add(new state(0, i, 0));
				}
			}
			
			// run our node-splitting dijkstra's
			while(!pq.isEmpty()) {
				state curr = pq.poll();
				
				// if we have already reached this letter and index with a better
				// distance, then there is no need to explore it again
				if(curr.d > dist[curr.let][curr.idx]) {
					continue;
				}
				
				// consider each outgoing edge
				for(edge e : edges[curr.idx]) {
					// compute new letter
					int newLet = curr.let;
					// if we are going to a city that has the next letter we need,
					// then increment our new letter
					if(cities[e.v].charAt(0) - 'A' == curr.let + 1) {
						++newLet;
					}
					
					// compute new distance
					long newD = curr.d + e.w;
					
					// see if this new distance is an improvement
					if(newD < dist[newLet][e.v]) {
						dist[newLet][e.v] = newD;
						pq.add(new state(newLet, e.v, newD));
					}
				}
			}
			
			// compute our answer
			long out = big;
			// for each node, see if reaching this node as the last node
			// in our road trip is the best answer
			for(int i = 0; i < n; ++i) {
				if(dist[numLets - 1][i] < out) {
					out = dist[numLets - 1][i];
				}
			}
			
			// print
			System.out.println(out);
		}
	}
	static class state implements Comparable<state>{
		int let, idx;
		long d;
		state(int ll, int ii, long dd){
			let = ll;
			idx = ii;
			d = dd;
		}
		public int compareTo(state in) {
			// sort by smaller distances first
			return Long.compare(d, in.d);
		}
	}
	static class edge{
		int v, w;
		edge(int vv, int ww){
			v = vv;
			w = ww;
		}
	}
}
/*

samples for convenience:

2
11 10
Alabama
Alaska
Buffalo
Columbia
Delaware
Elfville
Florida
Georgia
Hawaii
Idaho
Jupiter
Alabama Jupiter 1
Jupiter Alaska 2
Idaho Jupiter 1
Georgia Idaho 1
Hawaii Georgia 1
Florida Hawaii 1
Elfville Florida 1
Elfville Delaware 1
Delaware Columbia 1
Buffalo Columbia 1
10 9
Avalon
Harrison
Dover
Bermuda
Gillian
Camelot
Jackson
Elliot
Florida
Iguana
Dover Avalon 3
Avalon Gillian 10
Florida Avalon 71
Avalon Bermuda 5
Jackson Avalon 5
Avalon Harrison 52
Elliot Avalon 6
Iguana Avalon 4
Camelot Avalon 5

*/
