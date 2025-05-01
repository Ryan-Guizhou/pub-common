package com.peach.common.util;

import com.peach.common.anno.NotBlank;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.ValidationException;
import java.lang.reflect.Field;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/5/1 16:26
 */
@Slf4j
public class InputParamChecker {

    private static final InputParamChecker INSTANCE;

    static {
        INSTANCE = new InputParamChecker();
    }


    private InputParamChecker() {}

    public static InputParamChecker checkRequiredParams(Object target) throws ValidationException {
        if (target == null) {
            throw new IllegalArgumentException("参数对象不能为空");
        }

        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(NotBlank.class)) {
                continue;
            }
            field.setAccessible(true);
            try {
                Object value = field.get(target);
                if (!isBlank(value)) {
                    continue;
                }
                String fieldName = field.getName();
                NotBlank annotation = field.getAnnotation(NotBlank.class);
                String msg = annotation.message();
                throw new IllegalArgumentException(
                        String.format("字段 [%s] 校验失败: %s", fieldName, msg)
                );
            } catch (IllegalAccessException e) {
                throw new ValidationException("字段访问失败: " + field.getName());
            }
        }
        return INSTANCE;
    }

    /**
     * 判断这个值是否为空
     * @param value
     * @return
     */
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
