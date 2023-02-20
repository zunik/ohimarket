package zunik.ohimarket.repository.dto;

import lombok.Data;

@Data
public class MemberSummeryDto {
    private Long totalViews;
    private Long totalLikeCount;
    private Long totalCommentCount;

    {
        this.totalViews = 0L;
        this.totalLikeCount = 0L;
        this.totalCommentCount = 0L;
    }
}
