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
public @interface TestPostcondition {
    public String[] groups();
}
