package edu.spbu.matrix;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Разряженная матрица
 */
public class SparseMatrix implements Matrix {
  int length, hight;
  Map<Point, Double> val;

  public SparseMatrix(int length, int hight){
      this.length = length;
      this.hight = hight;
      val = new HashMap<>((length * hight) / 200);
  }

  public SparseMatrix(int length, int hight, double[][] elem) {
    this.length = length;
    this.hight = hight;
    val = new HashMap<>((length * hight) / 200);
    for (int i = 0; i < hight; i++){
      for (int j = 0; j < length; j++){
        if (elem[i][j] != 0)
        {
          val.put(new Point(i,j), elem[i][j]);
        }
      }
    }
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
      Double[] temp = {};
      int check = 0, str = 0;
      val = null;
      if (input.hasNextLine()) {
        line = input.nextLine().split(" ");
        check = line.length;
        val = new HashMap<>((check * check) / 200);
        temp = new Double[check];
        for (int i = 0; i < check; i++) {
          temp[i] = Double.parseDouble(line[i]);
          if (temp[i] != 0) {
              val.put(new Point(0, i), temp[i]);
          }
        }
        str++;
      }
      while (input.hasNextLine()) {
        line = input.nextLine().split(" ");
        if (check != line.length) {
          throw new IOException("Неверная размерность матрицы.");
        }
        temp = new Double[check];
        for (int i = 0; i < check; i++) {
          temp[i] = Double.parseDouble(line[i]);
          if (temp[i] != 0) {
              val.put(new Point(str, i), temp[i]);
          }
        }
        str++;
      }
      this.hight = str;
      this.length = check;
    } catch (IOException e) {
      System.out.println("Ошибка чтения файла.\n" + e.getMessage());
    }

  }

  /**
   * однопоточное умножение матриц
   * должно поддерживаться для всех 4-х вариантов
   *
   * @param o
   * @return
   */
  @Override
  public SparseMatrix mul(Matrix o) {
    if (o instanceof SparseMatrix) {
      if (length != ((SparseMatrix) o).hight) {
        return (null);
      }
      SparseMatrix rez = new SparseMatrix(((SparseMatrix) o).length, hight);
        for (Point key : val.keySet()) {
            for (int i = 0; i < ((SparseMatrix) o).length; i++) {
                Point p = new Point(key.y, i);
                if (((SparseMatrix) o).val.containsKey(p)) {
                    Point q = new Point(key.x, p.y);
                    if (rez.val.containsKey(q)) {
                        double t = rez.val.get(q) + val.get(key) * ((SparseMatrix) o).val.get(p);
                        rez.val.put(q, t);
                    } else {
                        double t = val.get(key) * ((SparseMatrix) o).val.get(p);
                        rez.val.put(q, t);
                    }
                }
            }
        }
        rez.val.entrySet().removeIf(entry -> Math.abs(entry.getValue()) < 1.0E-06);
        return rez;
    }
    if (o instanceof DenseMatrix) {
        if (length != ((DenseMatrix) o).hight) {
            return (null);
        }
        SparseMatrix rez = new SparseMatrix(((DenseMatrix) o).length, hight);
        for (Point key : val.keySet()) {
            for (int i = 0; i < ((DenseMatrix) o).length; i++){
                Point q = new Point(key.x, i);
                if (rez.val.containsKey(q))
                {
                    double t = rez.val.get(q) + val.get(key)*((DenseMatrix) o).val[key.y][i];
                    rez.val.put(q, t);
                } else {
                    double t = val.get(key)*((DenseMatrix) o).val[key.y][i];
                    rez.val.put(q, t);
                }
            }
        }
        rez.val.entrySet().removeIf(entry -> Math.abs(entry.getValue()) < 1.0E-06);
        return rez;
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

    @Override
    public int hashCode() {
        return (val.hashCode() + hight + length);
    }
  /**
   * сравнивает с обоими вариантами
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
    if ((length != ((SparseMatrix) o).length) || (hight != ((SparseMatrix) o).hight)|| (val.size() != ((SparseMatrix) o).val.size())){
      return (false);
    }
      for (Point key : val.keySet()) {
          if (!((SparseMatrix) o).val.containsKey(key))
              return (false);
          if (Math.abs(val.get(key) - ((SparseMatrix) o).val.get(key)) >= 1.0E-06){
              return(false);
          }
      }
    return (true);
  }

  @Override
  public String toString() {
      double[][] temp = new  double[hight][length];
      for (Map.Entry<Point, Double> pointDoubleEntry : val.entrySet()){
          temp[pointDoubleEntry.getKey().x][pointDoubleEntry.getKey().y] = pointDoubleEntry.getValue();
      }
      StringBuilder dat = new StringBuilder();
      for (int i = 0; i < hight; i++){
          dat.append(Arrays.toString(temp[i]));
          dat.append("\n");
      }
      return (dat.toString());
  }
}
