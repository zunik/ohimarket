package zunik.ohimarket.domain;


import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@DynamicInsert
@Data
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long memberId;

    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    private String postCategoryName;

    @ColumnDefault("0")
    private Long views;

    @ColumnDefault("0")
    private Long likeCount;

    @ColumnDefault("0")
    private Long commentCount;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
