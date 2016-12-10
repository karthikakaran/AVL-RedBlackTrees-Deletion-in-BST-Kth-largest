//===========================================================================================================================
//	Program : To perform selection of K largest elements
//===========================================================================================================================
//	@author: Karthika, Nevhetha, Kritika
// 	Date created: 2016/10/09
//===========================================================================================================================
import java.util.*;

public class Selection<T> {

	private static int q = 0, t = 0;
	/** Procedure to recursively select k elements
	 * Runs in time O(n) where n is the size of the array
	 * @param arr : input array
	 * @param p : Integer, start index of the array
	 * @param r : Integer, end index of the array
	 * @param k : Integer, k largest elements
	 * @return : Integer, position from where largest k elements can be fetched
	 */
	public static<T extends Comparable<? super T>> int select(T[] arr, int p, int r, int k) {
		partition(arr, p , r);
		if (r - t >= k) {
			return select(arr, t + 1, r, k);
		} else if (r - q + 1 >= k) {
			return (r - k + 1);
		} else
			return select(arr, p, q - 1, k - (r - q + 1));
	}
	
	/** Procedure to recursively partition in 3 way 
	 * Runs in time O(n) where n is the size of the array
	 * @param arr : input array
	 * @param p : Integer, start index of the array
	 * @param r : Integer, end index of the array
	 */
	private static<T extends Comparable<? super T>> void partition(T[] arr, int p, int r) {
		//Random element and exchange with first element
		int y = p + (int)(Math.random()*(r - p));
		swap(arr, p, y);
		t = p;
		q = p;
		T pivot = arr[p];
		T tmp;
		
		for(int j = p+1; j <= r; j++) {
			//partition elements less than pivot to left side
			if (arr[j].compareTo(pivot) < 0) {
				tmp = arr[q];
				arr[q] = arr[j];
				arr[j] = arr[t+1];
				arr[t+1] = tmp;
				q++;
				t++;
			} else if (arr[j].compareTo(pivot) == 0) {
				//partition elements equal to pivot in the middle
				swap(arr, t+1, j);
				t++;
			}
		}
	}
	
	/** Procedure to swap 2 elements
	 * Runs in time O(n) where n is the size of the array
	 * @param arr : input array
	 * @param x : Integer, one index
	 * @param y : Integer, second index
	 */
	private static<T extends Comparable<? super T>> void swap (T arr[], int x, int y) {
		T tmp = arr[x];
		arr[x] = arr[y];
		arr[y] = tmp;
	}
	/** Procedure to externally sort using PriorityQueue
	 * Runs in time O(nlogk) where n is the size of the array
	 * @param arr : input array
	 * @param p : Integer, start index of the array
	 * @param r : Integer, end index of the array
	 * @param k : Integer, k largest elements
	 * @return : Integer, returning k
	 */
	public static<T extends Comparable<? super T>> int selectPriotityQueue(T[] arr, int p, int r, int k) {
		PriorityQueue<T> priorityQueue = new PriorityQueue<>();
		int i = 0;
		//Adding k elements to the priority queue
		for (;i < k; i++) {
			priorityQueue.add(arr[i]);
		}
		//when a larger element is found among the remaining elements replace the smallest
		//element in the heap by that element
		while (i < r) {
			if(priorityQueue.peek().compareTo(arr[i]) < 0) {
				priorityQueue.add(arr[i]);
				priorityQueue.poll();
			}
			i++;
		}
		//fetching only the k largest elements and fill the arrays first k elements.
		for (i = 0; i < k; i++) {
			arr[i] = priorityQueue.poll();
		}
		return k;
	}
	
	public static void main(String args[]) {
		int r = 1000000, k = 0;
		Scanner in = new  Scanner(System.in);
		System.out.println("Enter k value ::");
		k = in.nextInt();
		in.close();
		Integer[] arr = new Integer[r];
		Integer[] arr2 = new Integer[r];
		for(int p = 0; p < r; p++) {
			arr[p] = new Integer(p + 1);
			arr2[p] = new Integer(p + 1);
		}
		Timer time = new Timer();
		selectPriotityQueue(arr, 0, r, k);
		System.out.println("External sorting gives Kth largest numbers as :: ");
		for (int i = 0; i < k; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println("\n" + time.end());
		Timer time1 = new Timer();
		q = select(arr2, 0, r - 1, k);
		System.out.println("\nInternal sorting gives Kth largest numbers as :: ");
		for (int i = q; i < r; i++) {
			System.out.print(arr2[i] + " ");
		}
		System.out.println("\n" + time1.end());
	}
}
