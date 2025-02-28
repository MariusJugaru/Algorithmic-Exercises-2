import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Numarare {
	static class Task {
		public static final String INPUT_FILE = "numarare.in";
		public static final String OUTPUT_FILE = "numarare.out";

		// max vertices
		public static final int NMAX = (int)1e5 + 5; // 10^5 + 5 = 100.005
		long mod = 1000000007;

		// n = number of vertices, m = number of edges
		int n, m;

		// chain = number of elementary chains
		long chain = 0;

		// adj_<x>[node] = adjacency list for graph x
		@SuppressWarnings("unchecked")
		ArrayList<Integer>[] adj_1 = new ArrayList[NMAX];
		@SuppressWarnings("unchecked")
		ArrayList<Integer>[] adj_2 = new ArrayList[NMAX];

		// intersection of the two adjacency lists
		@SuppressWarnings("unchecked")
		ArrayList<Integer>[] adj = new ArrayList[NMAX];

		public void solve() {
			readInput();
			writeOutput(getResult());
		}

		private void readInput() {
			try {
				Scanner sc = new Scanner(new BufferedReader(new FileReader(
								INPUT_FILE)));
				n = sc.nextInt();
				m = sc.nextInt();

				for (int node = 1; node <= n; node++) {
					adj_1[node] = new ArrayList<>();
					adj_2[node] = new ArrayList<>();
				}

				for (int i = 1, x, y; i <= m; i++) {
					// edge (x, y)
					x = sc.nextInt();
					y = sc.nextInt();
					adj_1[x].add(y);
				}

				for (int i = 1, x, y; i <= m; i++) {
					// edge (x, y)
					x = sc.nextInt();
					y = sc.nextInt();
					adj_2[x].add(y);
				}

				sc.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(long chains) {
			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(
								OUTPUT_FILE)));
				pw.printf("%d\n", chains);
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		// computes the intersection of the adjacency lists
		private void getIntersection() {
			for (int node = 1; node <= n; node++) {
				adj[node] = new ArrayList<>();
			}

			for (int i = 1; i <= n; i++) {
				for (Integer neigh1 : adj_1[i]) {
					if (adj_2[i].contains(neigh1)) {
						adj[i].add(neigh1);
					}
				}
			}
		}

		private long getResult() {
			getIntersection();

			ArrayList<Integer> topsort = topologicalSort();

			long[] dp = new long[n + 1];
			Arrays.fill(dp, 0);
			dp[1] = 1;

			for (Integer aux : topsort) {
				for (Integer neigh : adj[aux]) {
					dp[neigh] = (dp[neigh] + dp[aux]) % mod;
				}
			}

			return dp[n];
		}

		public ArrayList<Integer> topologicalSort() {
			// Vector to store indegree of each vertex
			int[] indegree = new int[n + 1];
			for (int i = 1; i <= n; i++) {
				for (Integer vertex : adj[i]) {
					indegree[vertex]++;
				}
			}
	
			// We want our topological sort to begin from the first node
			Queue<Integer> q = new LinkedList<>();
			q.add(1);

			ArrayList<Integer> result = new ArrayList<>();

			while (!q.isEmpty()) {
				int node = q.poll();
				result.add(node);
				// Decrease indegree of adjacent vertices as the
				// current node is in topological order
				for (int adjacent : adj[node]) {
					indegree[adjacent]--;
					// If indegree becomes 0, push it to the queue
					if (indegree[adjacent] == 0) {
						q.add(adjacent);
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
