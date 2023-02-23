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

    // 노출용 고유 값
    @Column(unique = true, nullable = false)
    private String token;
    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    @Column(nullable = false)
    private String postCategoryName;

    // 조회수
    @ColumnDefault("0")
    private Long views;

    // 총 좋아요 수
    @ColumnDefault("0")
    private Long likeCount;

    // 총 댓글 수
    @ColumnDefault("0")
    private Long commentCount;

    // 이미지 파일 이름 (ex. 20230223-1311_5af12c0a-b6f5-46de-b292-f14ca8c4350b.jpg)
    private String imgName;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
