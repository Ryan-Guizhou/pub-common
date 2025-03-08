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

    @ExceptionHandler(RuntimeException.class)
    public Response handleBusinessException(RuntimeException e) {
      log.error("GlobalExceptionHandler -> RuntimeException:", e);
      return Response.commonResponse(StatusEnum.FAIL.getCode(), e.getMessage());
    }

    @ExceptionHandler(BusniessException.class)
    public Response handleBusinessException(BusniessException e) {
      log.error("GlobalExceptionHandler -> BusniessException:", e);
      return Response.commonResponse(StatusEnum.FAIL.getCode(), e.getMessage());
    }
}
