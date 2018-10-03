package edu.spbu.matrix;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MatrixTest
{
  /**
   * ожидается 4 таких теста
   */
  @Test
  public void equal(){
    Matrix m1 = new DenseMatrix("test.src\\m1.txt");
    Matrix e = new DenseMatrix("test.src\\e.txt");
    Matrix m3 = new DenseMatrix("test.src\\m3.txt");
    assertEquals(false, m1.equals(e));
    assertEquals(true, m1.equals(m3));
  }
  @Test
  public void mulDD() {
    Matrix m1 = new DenseMatrix("test.src\\m1.txt");
    Matrix e = new DenseMatrix("test.src\\e.txt");
    Matrix m2 = new DenseMatrix("test.src\\m2.txt");
    Matrix expected = new DenseMatrix("test.src\\rezult.txt");
    assertEquals(expected, m1.mul(m2));
    assertEquals(null, m2.mul(e));
  }
}
