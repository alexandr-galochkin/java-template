package edu.spbu.matrix;

import org.junit.Test;

import static org.junit.Assert.*;

public class MatrixTest
{
  /**
   * ожидается 4 таких теста
   */
  @Test
  public void equalD(){
    Matrix m1 = new DenseMatrix("test.src\\m1.txt");
    Matrix e = new DenseMatrix("test.src\\e.txt");
    Matrix m3 = new DenseMatrix("test.src\\m3.txt");
    assertNotEquals(m1, e);
    assertEquals(m1, m3);
  }
  @Test
  public void mulDD() {
    Matrix m1 = new DenseMatrix("test.src\\m1.txt");
    Matrix e = new DenseMatrix("test.src\\e.txt");
    Matrix m2 = new DenseMatrix("test.src\\m2.txt");
    Matrix expected = new DenseMatrix("test.src\\rezult.txt");
    assertEquals(expected, m1.mul(m2));
    assertNull(m2.mul(e));
  }
  @Test
  public void equalS(){
    Matrix m1 = new SparseMatrix("test.src\\m1.txt");
    Matrix e = new SparseMatrix("test.src\\e.txt");
    Matrix m3 = new SparseMatrix("test.src\\m3.txt");
    assertNotEquals(m1, e);
    assertEquals(m3, m1);
  }
}
