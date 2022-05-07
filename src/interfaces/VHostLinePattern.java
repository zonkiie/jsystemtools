package interfaces;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface VHostLinePattern
{
	public String pattern() default "";
}
