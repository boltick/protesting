package com.gramant.jtr.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.METHOD;

/**
 * Description
 *
 * @version (VCS$Id:$)
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({METHOD})
public @interface Test {
    public String[] groups();
    public String parentMethod() default "";
    public boolean firstMethod() default false;
    public boolean runIfPreviousMethodFailed() default false;
    public boolean runIfPreviousMethodSkipped() default false;
    public String[] skipIfPreviousGroupNotPassed() default {""};
}
