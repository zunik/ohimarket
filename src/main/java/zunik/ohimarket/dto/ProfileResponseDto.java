package zunik.ohimarket.dto;

import lombok.Data;
import zunik.ohimarket.domain.Member;

@Data
public class ProfileResponseDto {
    private Member member;
    private int totalLikeCount;
    private int totalViews;
}
