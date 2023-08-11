public class ClothingCost {
    public static void main(String[] args) {
        int[][] price = {
                {14, 4, 11},
                {11, 14, 3},
                {14, 2, 10}
        };
        int minimumCost = minimumCost(price);
        System.out.println("Minimum cost required: " + minimumCost);
    }
    public static int minimumCost(int[][] price) {
        int minCost1 = 0;
        int minCost2 = 0;
        int minCost3 = 0;

        for (int[] person : price) {
            int newMinCost1 = person[0] + Math.min(minCost2, minCost3);
            int newMinCost2 = person[1] + Math.min(minCost1, minCost3);
            int newMinCost3 = person[2] + Math.min(minCost1, minCost2);

            minCost1 = newMinCost1;
            minCost2 = newMinCost2;
            minCost3 = newMinCost3;
        }

        return Math.min(minCost1, Math.min(minCost2, minCost3));
    }


}

