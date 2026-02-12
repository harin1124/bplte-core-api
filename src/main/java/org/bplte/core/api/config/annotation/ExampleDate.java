package org.bplte.core.api.config.annotation;

import io.swagger.v3.oas.annotations.media.Schema;
import org.bplte.core.api.core.swagger.SwaggerConstant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Swagger 날짜 example 전용 커스텀 어노테이션
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Schema(example = SwaggerConstant.DATE_EXAMPLE)
public @interface ExampleDate {
}
