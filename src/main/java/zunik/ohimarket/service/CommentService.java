package zunik.ohimarket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zunik.ohimarket.domain.Comment;
import zunik.ohimarket.repository.CommentRepository;
import zunik.ohimarket.repository.PostRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void save(Comment comment) {
        // TODO 상위 Post가 삭제되지 않았는지 확인해야함
        comment.setToken(UUID.randomUUID().toString());
        commentRepository.save(comment);
        postRepository.increaseCommentCount(comment.getPostId());
    }

    @Transactional
    public Long delete(String token, Long memberId) {
        Comment comment = commentRepository.findByToken(token).get();
        Long postId = comment.getPostId();

        // TODO 인증자의 댓글이 아닐경우 삭제 실패
        commentRepository.delete(comment);
        postRepository.decreaseCommentCount(comment.getPostId());
        return postId;
    }
}
