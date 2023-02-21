package zunik.ohimarket.service.dto;

import lombok.Data;
import zunik.ohimarket.domain.Member;
import zunik.ohimarket.domain.Post;
import zunik.ohimarket.repository.dto.MemberSummeryDto;

import java.util.List;

@Data
public class ProfileResponseDto {
    private Member member;
    private MemberSummeryDto summary;

    private List<Post> writtenPosts;

    private List<Post> commentaryPosts;
}
