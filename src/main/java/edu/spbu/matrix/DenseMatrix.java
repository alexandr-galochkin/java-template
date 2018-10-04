package edu.spbu.matrix;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
/**
 * Плотная матрица
 */
public class DenseMatrix implements Matrix
{
  private int length, hight;
  private double[][] val;
  public DenseMatrix(int length, int hight){
      this.length = length;
      this.hight = hight;
      this.val = new double[length][hight];
  }
  public DenseMatrix(String fileName)
  {
      try {
          File f = new File(fileName);
          Scanner input = new Scanner(f);
          String[] line;
          ArrayList<Double[]> a = new ArrayList<>();
          Double[] temp = {};
          int check = 0;
          if (input.hasNextLine()){
              line = input.nextLine().split(" ");
              check = line.length;
              temp = new Double[check];
              for (int i=0; i<check; i++) {
                  temp[i] = Double.parseDouble(line[i]);
              }
              a.add(temp);
          }
          while (input.hasNextLine()) {
              line = input.nextLine().split(" ");
              if (check != line.length){
                  throw new IOException ("Неверная размерность матрицы.");
                }
                temp = new Double[line.length];
              for (int i=0; i<temp.length; i++) {
                  temp[i] = Double.parseDouble(line[i]);
              }
              a.add(temp);
          }
          double[][] result = new double[a.size()][temp.length];
          for (int i=0; i<result.length; i++) {
              for (int j=0; j<result[0].length; j++) {
                  result[i][j] = a.get(i)[j];
              }
          }
          this.val = result;
          this.hight = result.length;
          this.length = result[0].length;
      } catch(IOException e) {
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
  @Override public Matrix mul(Matrix o)
  {
      if (o instanceof DenseMatrix){
          if (this.hight != ((DenseMatrix) o).length){
              return (null);
          }
          DenseMatrix rez = new DenseMatrix(this.hight, ((DenseMatrix) o).length);
          for (int i = 0; i < rez.hight; i++){
              for (int j = 0; j < rez.length; j++){
                  for (int k = 0; k < this.length; k++){
                      rez.val[i][j] += (this.val[i][k] * ((DenseMatrix) o).val[k][j]);
                  }
              }
          }
          return (rez);
      }
      return (null);
  }

  /**
   * многопоточное умножение матриц
   *
   * @param o
   * @return
   */
  @Override public Matrix dmul(Matrix o)
  {
    return null;
  }

  @Override
  public int hashCode() {
        return (Arrays.deepHashCode(this.val) + this.hight + this.length);
}
  @Override
  public boolean equals(Object o) {
      if (!(o instanceof DenseMatrix)){
          return(false);
      }
    if (this.hashCode() != (o.hashCode())) {
        return (false);
    }
    if ((this.length !=((DenseMatrix) o).length)||(this.hight !=((DenseMatrix) o).hight)){
        return (false);
    }
    for (int i = 0; i < this.hight; i++){
        for (int j = 0; j < this.length; j++){
            if (Math.abs(this.val[i][j] -((DenseMatrix) o).val[i][j]) > 1.0E-06){
                return(false);
            }
        }
    }
    return(true);
  }
  @Override public String toString() {
    StringBuilder dat = new StringBuilder();
    for (int i = 0; i < this.hight; i++){
        dat.append(Arrays.toString(this.val[i]));
        dat.append("\n");
    }
    return (dat.toString());
  }
}
