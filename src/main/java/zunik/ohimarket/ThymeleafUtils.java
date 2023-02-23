package zunik.ohimarket;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.*;

/**
 * thymeleaf 내에서 사용할 수 있는 기능을 가지고 있습니다.
 */
@Component
public class ThymeleafUtils {
    /**
     * datetime 을 받아서 지금과 비교한뒤, 경과한 시간을 표시해줍니다.
     * 사용예시) th:text="${@thymeleafUtils.datetimeToStr(createdAt)}"
     */
    public String datetimeToStr(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();

        Long seconds = SECONDS.between(dateTime, now);

        if (seconds < 60) {
            return seconds + "초 전";
        } else if (seconds < 3600) {
            Long minutes = seconds / 60;
            return minutes + "분 전";
        } else if (seconds < 86400) {
            Long hours = seconds / 3600;
            return hours + "시간 전";
        }

        Long days = DAYS.between(dateTime, now);
        if (days == 1) {
            return "어제";
        } else if (days == 2) {
            return "그저께";
        }
        return days + "일 전";
    }
}
