package edu.spbu.matrix;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Разряженная матрица
 */
public class SparseMatrix implements Matrix {
  private int length, hight;
  private double[] val;
  private int[] x;
  private int[] y;

  public SparseMatrix(int length, int hight, int elem) {
    this.length = length;
    this.hight = hight;
    this.val = new double[elem];
    this.x = new int [elem];
    this.y = new int [elem];
  }
  /**
   * загружает матрицу из файла
   *
   * @param fileName
   */
  public SparseMatrix(String fileName) {
    try {
      File f = new File(fileName);
      Scanner input = new Scanner(f);
      String[] line;
      ArrayList<Double> a = new ArrayList<>();
      ArrayList<Integer> x = new ArrayList<>();
      ArrayList<Integer> y = new ArrayList<>();
      Double[] temp = {};
      int check = 0, str = 0;

      if (input.hasNextLine()) {
        line = input.nextLine().split(" ");
        check = line.length;
        temp = new Double[check];
        for (int i = 0; i < check; i++) {
          temp[i] = Double.parseDouble(line[i]);
          if (temp[i] != 0) {
            a.add(temp[i]);
            x.add(i);
            y.add(str);
          }
        }
        str++;
      }
      while (input.hasNextLine()) {
        line = input.nextLine().split(" ");
        if (check != line.length) {
          throw new IOException("Неверная размерность матрицы.");
        }
        temp = new Double[line.length];
        for (int i = 0; i < temp.length; i++) {
          temp[i] = Double.parseDouble(line[i]);
          if (temp[i] != 0) {
            a.add(temp[i]);
            x.add(i);
            y.add(str);
          }
        }
        str++;
      }
      double[] rezult = new double[a.size()];
      int[] rez_x = new int[a.size()];
      int[] rez_y = new int[a.size()];
      for (int i = 0; i < a.size(); i++) {
        rezult[i] = a.get(i);
        rez_x[i] = x.get(i);
        rez_y[i] = y.get(i);
      }
      this.val = rezult;
      this.x = rez_x;
      this.y = rez_y;
      this.hight = str - 1;
      this.length = check;
    } catch (IOException e) {
      System.out.println("Ошибка чтения файла.\n" + e.getMessage());
    }

  }

  /**
   * однопоточное умнджение матриц
   * должно поддерживаться для всех 4-х вариантов
   *
   * @param o
   * @return
   */
  @Override
  public Matrix mul(Matrix o) {
    if (o instanceof SparseMatrix) {
      if (this.hight != ((SparseMatrix) o).length) {
        return (null);
      }
      ArrayList<Double> a = new ArrayList<>();
      ArrayList<Integer> a_x = new ArrayList<>();
      ArrayList<Integer> a_y = new ArrayList<>();
      int elem = 0;
      List<int[]> b = Arrays.asList(((SparseMatrix) o).y);
      for (int i = 0; i < this.x.length; i++) {
        int t = b.indexOf(this.x[i]);
        if (t != -1){
          double r = this.y[t] * ((SparseMatrix) o).x[t];
          if (r != 0){
            a.add(r);
          }
        }
      }
    }
    return(null);
  }

  /**
   * многопоточное умножение матриц
   *
   * @param o
   * @return
   */
  @Override
  public Matrix dmul(Matrix o) {
    return null;
  }

  /**
   * спавнивает с обоими вариантами
   *
   * @param o
   * @return
   */
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof SparseMatrix)) {
      return (false);
    }
    if (this.hashCode() != hashCode()) {
      return (false);
    }
    if ((this.length != ((SparseMatrix) o).length) || (this.hight != ((SparseMatrix) o).hight) || (this.val.length != ((SparseMatrix) o).val.length)) {
      return (false);
    }
    if (!Arrays.equals(this.x, ((SparseMatrix) o).x)) {
      return (false);
    }
    if (!Arrays.equals(this.y, ((SparseMatrix) o).y)) {
      return (false);
    }
    for (int i = 0; i < this.x.length; i++) {
      if (Math.abs(this.val[i] - ((SparseMatrix) o).val[i]) > 1.0E-06) {
        return (false);
      }
    }
    return (true);
  }
}
