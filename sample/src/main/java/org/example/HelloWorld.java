// made by jack
// this file is generated automatically. Do not modify!!!
package org.example;

import java.lang.Exception;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

public class HelloWorld {
  public static void main(String[] args) {
    int result = 1;
    for (int i = 1; i < 10; i++) {
      result = result * i ;
    }
    System.out.println(result);
  }

  String foo() {
    int i = 10;
    if(i<0) {
      return "bad";
    } else if(i<60) {
      return "normal";
    } else if(i<90) {
      return "good";
    } else {
      return "haha";
    }
  }

  List<String> beyond() {
    List<String> result = new ArrayList<>();
    result.add(new String("Java"));
    result.add(new String("C++"));
    result.add(new String("Python"));
    return result;
  }

  public static int k1() {
    int num = 10;
    try {
      num += 10;
      return num;
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      num += 10;
      return num;
    }
  }

  char hexDigit(int i) {
    return (char) (i < 10 ? i + '0' : i - 10 + 'a');
  }

  String byteToHex(int b) {
    char[] result = new char[2];
    result[0] = hexDigit((b >>> 4) & 0xf);
    result[1] = hexDigit(b & 0xf);
    return new String(result);
  }

  @Override
  @SuppressWarnings("only")
  public String toString() {
    return "Hoverboard";
  }
}
