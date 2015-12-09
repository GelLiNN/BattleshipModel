public class Submarine extends Ship{
 private static final int LENGTH = 3;
 public static final char REFERENCE = 'S';
 
 public Submarine() {
  super(LENGTH, REFERENCE);
 }
 
 public int getLength() {
  return LENGTH;
 }
}