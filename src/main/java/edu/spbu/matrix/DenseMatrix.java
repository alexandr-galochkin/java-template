package edu.spbu.matrix;
import sun.security.jca.GetInstance;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
/**
 * Плотная матрица
 */
class Invest implements Runnable{

    private int str, ch;
    private DenseMatrix a, b, rez;
    Invest(int k, int l, DenseMatrix i, DenseMatrix j, DenseMatrix c){
        rez = c;
        str = k;
        ch = l;
        a = i;
        b = j;
    }
    @Override
    public void run() {
        for (int i = str; i < str + ch; i++){
            if (i < a.hight)
            {
                for (int j = 0; j < rez.length; j++) {
                    for (int k = 0; k < a.length; k++) {
                        rez.val[i][j] += (a.val[i][k] * b.val[j][k]);
                    }
                }
            }
        }
    }
}
public class DenseMatrix implements Matrix
{
  int length, hight;
  double[][] val;
  public DenseMatrix(int length, int hight){
      this.length = length;
      this.hight = hight;
      val = new double[hight][length];
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
          DenseMatrix trans = ((DenseMatrix) o).transp();
          for (int i = 0; i < rez.hight; i++){
              for (int j = 0; j < rez.length; j++){
                  for (int k = 0; k < length; k++){
                      rez.val[i][j] += (val[i][k] * trans.val[j][k]);
                  }
              }
          }
          return (rez);
      }
      if (o instanceof SparseMatrix){
          if (length != ((SparseMatrix) o).hight) {
              return (null);
          }
          SparseMatrix rez = new SparseMatrix(hight, ((SparseMatrix) o).length);
          DenseMatrix trans = this.transp();
          for (Point key : ((SparseMatrix) o).val.keySet()) {
              for (int i = 0; i < ((SparseMatrix) o).length; i++){
                  Point q = new Point(i, key.y);
                  if (rez.val.containsKey(q))
                  {
                      double t = rez.val.get(q) + ((SparseMatrix) o).val.get(key)*trans.val[key.x][i];
                      rez.val.put(q, t);
                  } else {
                      double t = ((SparseMatrix) o).val.get(key)*trans.val[key.x][i];
                          rez.val.put(q, t);
                  }
              }
          }
          rez.val.entrySet().removeIf(entry -> Math.abs(entry.getValue()) < 1.0E-06);
          return rez;
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
      if (o instanceof DenseMatrix) {
          if (this.hight != ((DenseMatrix) o).length) {
              return (null);
          }
          DenseMatrix rez = new DenseMatrix(this.hight, ((DenseMatrix) o).length);
          DenseMatrix trans = ((DenseMatrix) o).transp();
          ArrayList<Thread> t = new ArrayList<>();
          int ch = this.hight/2000 + 1;
          for (int i = 0; i < rez.hight; i+=ch) {
              Invest act = new Invest(i, ch, this, trans, rez);
              t.add(new Thread(act));
              t.get(i/ch).start();
          }
          for (Thread p: t) {
              try {
                  p.join();
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
          }
          return (rez);
      }
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
  @Override
  public String toString() {
    StringBuilder dat = new StringBuilder();
    for (int i = 0; i < hight; i++){
        dat.append(Arrays.toString(val[i]));
        dat.append("\n");
    }
    return (dat.toString());
  }
   private DenseMatrix transp(){
      DenseMatrix rez = new DenseMatrix(hight, length);
      for (int i = 0; i < length; i++){
          for (int j = 0; j < hight; j++){
              rez.val[i][j] = val[j][i];
          }
      }
      return (rez);
  }
}
