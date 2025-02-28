import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Trenuri {
	static class Task {
		public static final String INPUT_FILE = "trenuri.in";
		public static final String OUTPUT_FILE = "trenuri.out";

		// m = number of edges
		int m;

		// starting and ending city points
		String start, end;

		// nodes hashmap, nodes contain informations about each city (name, neighbours, indegree)
		HashMap<String, Node> nodes = new HashMap<>();

		// List of the city names
		ArrayList<String> cities = new ArrayList<>();

		static class Node {
			String city;

			ArrayList<Node> adj = new ArrayList<>();
			int indegree = 0;

			Node(String _city) {
				city = _city;
			}
		}

		public void solve() {
			readInput();
			writeOutput(getResult());
		}

		private void readInput() {
			try {
				String src, dst;
				Scanner sc = new Scanner(new BufferedReader(new FileReader(
								INPUT_FILE)));
				start = sc.next();
				end = sc.next();

				// add the cities into the hash map
				nodes.put(start, new Node(start));
				nodes.put(end, new Node(end));

				// add the city names into the list
				cities.add(start);
				cities.add(end);

				// read city direct paths
				m = sc.nextInt();
				for (int node = 0; node < m; node++) {
					src = sc.next();
					dst = sc.next();

					Node src_node = nodes.get(src);
					Node dst_node = nodes.get(dst);

					if (src_node == null) {
						src_node = new Node(src);
						nodes.put(src, src_node);
						cities.add(src);
					}
					if (dst_node == null) {
						dst_node = new Node(dst);
						nodes.put(dst, dst_node);
						cities.add(dst);
					}
					
					src_node.adj.add(dst_node);
					dst_node.indegree++;
				}

				sc.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(long max_cities) {
			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(
								OUTPUT_FILE)));
				pw.printf("%d\n", max_cities);
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}


		private long getResult() {
			ArrayList<String> topsort = topologicalSort();
			
			HashMap<String, Integer> dp = new HashMap<>();
			int start_idx = topsort.indexOf(start);
			int end_idx = topsort.indexOf(end);

			dp.put(start, 1);

			for (int i = start_idx; i <= end_idx; i++) {
				String city = topsort.get(i);
				Node city_Node = nodes.get(city);

				// we skip cities that are not along our path from start -> end
				if (dp.get(city) == null) {
					continue;
				}

				// get for each city the maximum cities you can visit from start -> city
				for (Node neigh : city_Node.adj) {
					if (dp.get(neigh.city) == null) {
						dp.put(neigh.city, dp.get(city) + 1);
					} else if (dp.get(neigh.city) < dp.get(city) + 1) {
						dp.put(neigh.city, dp.get(city) + 1);
					}
				}

			}

			return dp.get(end);
		}

		public ArrayList<String> topologicalSort() {
			Queue<String> q = new LinkedList<>();

			// Search the city list for cities that are "starting points" and add them to the queue
			for (String aux : cities) {
				Node city = nodes.get(aux);

				if (city.indegree == 0) {
					q.add(aux);
				}
			}

			ArrayList<String> result = new ArrayList<>();

			while (!q.isEmpty()) {
				String city = q.poll();
				Node node = nodes.get(city);
				result.add(city);

				// Decrease indegree of adjacent vertices as the
				// current node is in topological order
				for (Node adjacent : node.adj) {
					adjacent.indegree--;
					// If indegree becomes 0, push it to the queue
					if (adjacent.indegree == 0) {
						q.add(adjacent.city);
					}
				}
			}

			return result;
		}
	}

	public static void main(String[] args) {
		new Task().solve();
	}
}
