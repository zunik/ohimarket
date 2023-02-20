package zunik.ohimarket.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import zunik.ohimarket.domain.Comment;

import static zunik.ohimarket.domain.QMember.member;
import static zunik.ohimarket.domain.QComment.comment;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class CommentQueryRepository {
    private final JPAQueryFactory query;

    public List<Comment> findByPostId(Long postId) {
        return query.selectFrom(comment)
                .join(comment.member, member).fetchJoin()
                .where(comment.postId.eq(postId))
                .orderBy(comment.createdAt.asc())
                .fetch();
    }
}
