package com.peach.common.util;

import com.peach.common.anno.NotBlank;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.ValidationException;
import java.lang.reflect.Field;

@Slf4j
public class InputParamChecker {

    private final Object target;

    private InputParamChecker(Object target) {
        if (target == null) {
            throw new IllegalArgumentException("参数对象不能为空");
        }
        this.target = target;
    }

    public static InputParamChecker of(Object target) {
        return new InputParamChecker(target);
    }

    /**
     * 校验使用了 @NotBlank 注解的字段
     */
    public InputParamChecker checkAnnotatedFields() throws ValidationException {
        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(NotBlank.class)) continue;
            field.setAccessible(true);
            try {
                Object value = field.get(target);
                if (!isBlank(value)) continue;
                String msg = field.getAnnotation(NotBlank.class).message();
                throw new IllegalArgumentException(
                        String.format("字段 [%s] 校验失败: %s", field.getName(), msg)
                );
            } catch (IllegalAccessException e) {
                throw new ValidationException("字段访问失败: " + field.getName());
            }
        }
        return this;
    }

    /**
     * 通过字段名检查是否为空
     */
    public InputParamChecker checkField(String fieldName) throws ValidationException {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(target);
            if (isBlank(value)) {
                throw new IllegalArgumentException(String.format("字段 [%s] 不能为空", fieldName));
            }
        } catch (NoSuchFieldException e) {
            throw new ValidationException("字段不存在: " + fieldName);
        } catch (IllegalAccessException e) {
            throw new ValidationException("字段访问失败: " + fieldName);
        }
        return this;
    }

    /**
     * 批量检查字段是否为空
     */
    public InputParamChecker checkFields(String... fieldNames) throws ValidationException {
        for (String fieldName : fieldNames) {
            checkField(fieldName);
        }
        return this;
    }

    private static boolean isBlank(Object value) {
        if (value == null) {
            return true;
        }

        if (value instanceof String) {
            return ((String) value).trim().isEmpty();
        }

        if (value instanceof Number) {
            // 可以根据业务调整：是否认为 0 是空，例如 return ((Number) value).doubleValue() == 0;
            return false; // 数值类型只判断 null，0 不视为“空”
        }

        if (value instanceof Boolean) {
            return false; // true/false 本身是合法值
        }

        // 对象本身不为 null，则认为非空
        return false;
    }

}
