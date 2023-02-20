package zunik.ohimarket.service.dto;

import lombok.Data;
import zunik.ohimarket.domain.Comment;
import zunik.ohimarket.domain.Member;
import zunik.ohimarket.domain.Post;
import zunik.ohimarket.domain.PostCategory;

import java.util.List;

@Data
public class PostDetailResponseDto {
    private Post post;
    private PostCategory postCategory;
    private Member creator;

    private List<Comment> comments;
}
