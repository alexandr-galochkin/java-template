package edu.spbu.matrix;
import java.io.*;
import java.util.ArrayList;
/**
 * Плотная матрица
 */
public class DenseMatrix implements Matrix
{
  public int length = 0, hight = 0;
  public ArrayList<double[]> val = new ArrayList<double[]>();
  /**
   * загружает матрицу из файла
   * @param fileName
   */
  public DenseMatrix(String fileName) {
    try(FileReader file = new FileReader(fileName)){
      int r = 0;
      while ((r != -1)&&(r != '\n')){
        length++;
      }
      file.reset();
      r = 0;
      while (r != -1){
        hight++;
        val.add(new double[length]);
        int i = 0;
        while ((r != -1) && (r != '\n')){
          val.get(hight)[i] = r;
        }
      }
    }
    catch (IOException ex){
      System.out.println("Ошибка чтения файла");
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
    return null;
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

  /**
   * спавнивает с обоими вариантами
   * @param o
   * @return
   */
  @Override public boolean equals(Object o) {
    return false;
  }
  @Override public String toString() {
    StringBuilder dat = new StringBuilder();
    for (int i = 0; i < this.hight; i++){
      for (int j = 0; j < this.length; j++){
        dat.append(this.get(i)[j].toString());
        dat.append(" ");
      }
      dat.append("\n");
    }
    return (dat.toString());
  }

}
