package zunik.ohimarket.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "찾을 수 없는 유저 입니다.")
public class MemberNotFoundException extends RuntimeException {
}
