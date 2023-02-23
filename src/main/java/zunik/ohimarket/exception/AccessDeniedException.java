package zunik.ohimarket.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "해당 행위에 권한이 없습니다.")
public class AccessDeniedException extends RuntimeException {
}
