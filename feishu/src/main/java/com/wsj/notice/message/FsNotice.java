package com.wsj.notice.message;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FsNotice {
    String msg() default "";
    String[] openIds() default "";
}
