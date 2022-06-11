package git.dimitrikvirik.anychatbackend.model.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    @Value("${spring.application.name}")
    private String applicationName;


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException ex,
                                                        HandlerMethod handlerMethod, HttpServletRequest request){
        String exceptionName = ex.getClass().getName();
        String MethodName = handlerMethod.getMethod().getName();
        return new ResponseEntity<>(ExceptionBody.of(ex, applicationName, MethodName, exceptionName, request.getRequestURI()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public  ResponseEntity<Object> responseStatusExceptionHandler(ResponseStatusException exception,
                                     HandlerMethod handlerMethod, HttpServletRequest request){
        String exceptionName = exception.getClass().getName();
        String methodName = handlerMethod.getMethod().getName();
        return new ResponseEntity<>(ExceptionBody.of(exception, applicationName,
                methodName, exceptionName, request.getRequestURI()), exception.getStatus());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ExceptionBody> handleMaxSizeException(MaxUploadSizeExceededException  ex,
                                                                HandlerMethod handlerMethod, HttpServletRequest request) {
        String exceptionName = ex.getClass().getName();
        String MethodName = handlerMethod.getMethod().getName();
        return new ResponseEntity<>(ExceptionBody.of(ex, applicationName, MethodName, exceptionName, request.getRequestURI()),
                HttpStatus.PAYLOAD_TOO_LARGE);
    }


    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<Object> httpMessageNotReadableExceptionHandler(MissingServletRequestPartException ex,
                                                                         HandlerMethod handlerMethod, HttpServletRequest request){
        String exceptionName = ex.getClass().getName();
        String MethodName = handlerMethod.getMethod().getName();
        return new ResponseEntity<>(ExceptionBody.of(ex, applicationName, MethodName, exceptionName, request.getRequestURI()),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> missingServletRequestParameterException(MissingServletRequestParameterException ex, HandlerMethod handlerMethod, HttpServletRequest request){
        String exceptionName = ex.getClass().getName();
        String methodName = handlerMethod.getMethod().getName();
        return new ResponseEntity<>(ExceptionBody.of(ex, applicationName,methodName,exceptionName,request.getRequestURI()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HandlerMethod handlerMethod, HttpServletRequest request){
        String exceptionName = ex.getClass().getName();
        String methodName = handlerMethod.getMethod().getName();
        return new ResponseEntity<>(ExceptionBody.of(ex, applicationName, methodName, exceptionName, request.getRequestURI()),HttpStatus.BAD_REQUEST);
    }




}
