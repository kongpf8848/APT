// made by jack
// this file is generated automatically. Do not modify!!!
package org.example;

import java.lang.String;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Inherited
public @interface Permission {
  String[] sequence() default {};

  boolean write() default true;

  boolean extract() default false;
}
