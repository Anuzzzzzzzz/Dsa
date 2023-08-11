import java.util.*;

public class MinStepsToCompleteTasks {

    public static int minSteps(int N, int[][] prerequisites) {
        List<List<Integer>> graph = new ArrayList<>();
        int[] inDegree = new int[N + 1];

        for (int i = 0; i <= N; i++) {
            graph.add(new ArrayList<>());
        }

        for (int[] prereq : prerequisites) {
            int x = prereq[0];
            int y = prereq[1];
            graph.get(x).add(y);
            inDegree[y]++;
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 1; i <= N; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        int steps = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int current = queue.poll();
                for (int neighbor : graph.get(current)) {
                    inDegree[neighbor]--;
                    if (inDegree[neighbor] == 0) {
                        queue.add(neighbor);
                    }
                }
            }
            steps++;
        }

        for (int i = 1; i <= N; i++) {
            if (inDegree[i] > 0) {
                return -1; // There's a cycle, can't complete all tasks
            }
        }

        return steps;
    }

    public static void main(String[] args) {
        int N = 3;
        int[][] prerequisites = {{1, 3}, {2, 3}};
        int result = minSteps(N, prerequisites);
        System.out.println("Minimum steps: " + result);
    }
}

