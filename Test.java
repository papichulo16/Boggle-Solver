public class Test {
  public static void main (String args[]) {
    byte alice = 'a';
    char bob = 'c';

    System.out.println((char) (((alice ^ 0xff) & 0xff) ^ 0xff));
    System.out.println((char)((byte) bob));
  }
}
