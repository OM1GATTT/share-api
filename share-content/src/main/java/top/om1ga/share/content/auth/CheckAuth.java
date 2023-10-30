package top.om1ga.share.content.auth;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月23日 19:55
 * @description AuthAspect
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckAuth {
    String value();
}
