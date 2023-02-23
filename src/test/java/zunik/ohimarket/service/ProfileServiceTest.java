package zunik.ohimarket.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zunik.ohimarket.controller.dto.CommentCreateDto;
import zunik.ohimarket.controller.dto.PostCreateDto;
import zunik.ohimarket.controller.dto.SignUpDto;
import zunik.ohimarket.domain.Comment;
import zunik.ohimarket.domain.Member;
import zunik.ohimarket.domain.Post;
import zunik.ohimarket.service.dto.ProfileResponseDto;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
class ProfileServiceTest {
    @Autowired
    CommentService commentService;
    @Autowired ProfileService profileService;
    @Autowired MemberService memberService;
    @Autowired PostService postService;

    @BeforeEach
    void beforeEach() {
        SignUpDto memberData = new SignUpDto();
        memberData.setEmail("chazunik@gmail.com");
        memberData.setNickname("test");
        memberData.setIntroduction("");
        memberData.setPassword("123");

        memberService.save(memberData);
    }


    @Test
    @Transactional
    @DisplayName("하나에 게시물에 여러 개의 댓글을 썼어도. '댓글단 글'에는 하나만 표시가 돼야 함")
    void multipleComments() {
        // given
        Member member = memberService.findByEmail("chazunik@gmail.com");
        Post post = createPost(member.getId());

        // when
        createComment(post.getToken(), member);
        createComment(post.getToken(), member);
        createComment(post.getToken(), member);

        // then
        ProfileResponseDto profileData = profileService.getDetail(member.getToken());
        assertThat(profileData.getCommentaryPosts().size()).isEqualTo(1);
    }


    private Post createPost(Long memberId) {
        PostCreateDto postData = new PostCreateDto();
        postData.setPostCategoryName("question");
        postData.setTitle("테스트");
        postData.setContent("테스트");
        return postService.save(postData, memberId);
    }

    private Comment createComment(String postToken, Member member) {
        CommentCreateDto commentData = new CommentCreateDto();
        commentData.setPostToken(postToken);
        commentData.setContent("댓글내용");
        return commentService.save(commentData, member);
    }
}