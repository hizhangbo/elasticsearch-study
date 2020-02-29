package io.github.hizhangbo.annotations;

import java.lang.annotation.*;

/**
 * @author Bob
 * @date 2020-02-29 20:23
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Document {

    String indexName();

    @Deprecated
    String type() default "_doc";


}
