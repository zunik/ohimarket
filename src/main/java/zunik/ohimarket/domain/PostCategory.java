package zunik.ohimarket.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Data
public class PostCategory {
    @Id
    private String name;
    private String displayName;
    // 카테고리 노출 순서 정보
    private int sortNum;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
