package com.peach.common.exception;

import com.peach.common.enums.StatusEnum;
import com.peach.common.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/2/26 14:16
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 全局运行时异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public Response handleRuntimeException(RuntimeException e) {
      log.error("GlobalExceptionHandler -> RuntimeException:", e);
      return Response.commonResponse(StatusEnum.FAIL.getCode(), e.getMessage());
    }

    /**
     * 全局业务异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(BusniessException.class)
    public Response handleBusinessException(BusniessException e) {
      log.error("GlobalExceptionHandler -> BusniessException:", e);
      return Response.commonResponse(StatusEnum.FAIL.getCode(), e.getMessage());
    }

    /**
     * 请求参数违法异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(IllegalParamExceprion.class)
    public Response handleIllegalParamException(IllegalParamExceprion e) {
        log.error("GlobalExceptionHandler -> IllegalParamExceprion:", e);
        return Response.commonResponse(StatusEnum.FAIL.getCode(), e.getMessage());
    }
}
