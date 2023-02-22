package zunik.ohimarket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zunik.ohimarket.controller.dto.CommentCreateDto;
import zunik.ohimarket.controller.dto.CommentUpdateDto;
import zunik.ohimarket.domain.Comment;
import zunik.ohimarket.domain.Member;
import zunik.ohimarket.domain.Post;
import zunik.ohimarket.repository.CommentRepository;
import zunik.ohimarket.repository.PostRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void save(CommentCreateDto createParam, Member loginMember) {
        String postToken = createParam.getPostToken();

        // TODO 상위 Post가 삭제되지 않았는지 확인해야함
        Post post = postRepository.findByToken(postToken).get();

        Comment comment = new Comment();
        comment.setPostId(post.getId());
        comment.setMember(loginMember);
        comment.setContent(createParam.getContent());
        comment.setToken(UUID.randomUUID().toString());

        commentRepository.save(comment);
        postRepository.increaseCommentCount(comment.getPostId());
    }

    @Transactional(readOnly = true)
    public Optional<Comment> findByToken(String commentToken) {
        return commentRepository.findByToken(commentToken);
    }

    @Transactional
    public String update(String commentToken, CommentUpdateDto updateParam) {
        Comment comment = commentRepository.findByToken(commentToken).get();
        Post post = postRepository.findById(comment.getPostId()).get();

        // TODO 인증자의 댓글이 아닐경우 삭제 실패 (memberId) 가 아니라 찾아야함

        comment.setContent(updateParam.getContent());
        return post.getToken();
    }

    @Transactional
    public String delete(String token, Long memberId) {
        Comment comment = commentRepository.findByToken(token).get();
        Post post = postRepository.findById(comment.getPostId()).get();

        // TODO 인증자의 댓글이 아닐경우 삭제 실패

        commentRepository.delete(comment);
        postRepository.decreaseCommentCount(comment.getPostId());
        return post.getToken();
    }
}
