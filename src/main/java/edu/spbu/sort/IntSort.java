package edu.spbu.sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by artemaliev on 07/09/15.
 */
public class IntSort
{
  public static void quickSort(int array[], int left, int right) {
    if (array.length == 0)
      return;

    if (left >= right)
      return;

    int cr = left + (right - left) / 2;
    int mid = array[cr];

    int i = left, j = right;
    while (i <= j) {
      while (array[i] < mid) {
        i++;
      }

      while (array[j] > mid) {
        j--;
      }

      if (i <= j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        i++;
        j--;
      }
    }

    if (left < j)
      quickSort(array, left, j);

    if (right > i)
      quickSort(array, i, right);
  }
  public static void sort (int array[]) {
      quickSort(array, 0, array.length -1);
  }

  public static void sort (List<Integer> list) {
    Collections.sort(list);
  }
}
