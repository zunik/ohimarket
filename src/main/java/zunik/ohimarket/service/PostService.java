package zunik.ohimarket.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zunik.ohimarket.controller.dto.PostUpdateDto;
import zunik.ohimarket.domain.Comment;
import zunik.ohimarket.domain.Member;
import zunik.ohimarket.domain.Post;
import zunik.ohimarket.domain.PostCategory;
import zunik.ohimarket.controller.dto.PostCreateDto;
import zunik.ohimarket.exception.AccessDeniedException;
import zunik.ohimarket.exception.PostNotFoundException;
import zunik.ohimarket.service.dto.PostDetailResponseDto;
import zunik.ohimarket.repository.*;
import zunik.ohimarket.utils.ImgFileStore;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostCategoryRepository postCategoryRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final CommentQueryRepository commentQueryRepository;
    private final ImgFileStore imgFileStore;

    @Transactional
    public Post save(PostCreateDto createParam, Long memberId) {
        Post post = new Post();

        if (createParam.getImage() != null && !createParam.getImage().isEmpty()) {
            try {
                String imgName = imgFileStore.storeFile(createParam.getImage());
                post.setImgName(imgName);
            } catch (IOException e) {
                log.info("error", e);
            }
        }

        post.setPostCategoryName(createParam.getPostCategoryName());
        post.setTitle(createParam.getTitle());
        post.setContent(createParam.getContent());
        post.setMemberId(memberId);
        post.setToken(UUID.randomUUID().toString());
        postRepository.save(post);

        return post;
    }

    @Transactional
    public void delete(String postToken, Long memberId) throws PostNotFoundException, AccessDeniedException {
        Post post = postRepository.findByToken(postToken).orElseThrow(
                () -> new PostNotFoundException()
        );

        if (post.getMemberId() != memberId) {
            // ??????????????? ?????? ????????? ??????
            throw new AccessDeniedException();
        }

        // ???????????? ????????? Like, comment??? ?????? ???????????????.
        postLikeRepository.deleteByPostId(post.getId());
        commentRepository.deleteByPostId(post.getId());

        // ?????? ??????
        if (post.getImgName() != null) {
            imgFileStore.deleteFile(post.getImgName());
        }

        postRepository.delete(post);
    }

    @Transactional(readOnly = true)
    public PostDetailResponseDto getDetail(String postToken) throws PostNotFoundException {
        Post post = postRepository.findByToken(postToken).orElseThrow(
                () -> new PostNotFoundException()
        );

        PostCategory postCategory = postCategoryRepository.findById(post.getPostCategoryName()).get();
        Member creator = memberRepository.findById(post.getMemberId()).get();
        List<Comment> comments = commentQueryRepository.findByPostId(post.getId());

        PostDetailResponseDto responseDto = new PostDetailResponseDto();
        responseDto.setPost(post);
        responseDto.setPostCategory(postCategory);
        responseDto.setCreator(creator);
        responseDto.setComments(comments);

        return responseDto;
    }


    @Transactional
    public void update(String postToken, Long memberId, PostUpdateDto updateParam) throws PostNotFoundException {
        Post post = postRepository.findByToken(postToken).orElseThrow(
                () -> new PostNotFoundException()
        );

        if (post.getMemberId() != memberId) {
            // ??????????????? ?????? ????????? ??????
            throw new AccessDeniedException();
        }

        if (!updateParam.getImage().isEmpty()) {
            String oldImgName = post.getImgName();

            try {
                String imgName = imgFileStore.storeFile(updateParam.getImage());
                post.setImgName(imgName);
            } catch (IOException e) {
                log.info("error", e);
            }

            if (oldImgName != null) {
                // ?????? ??? ????????? ??????
                imgFileStore.deleteFile(oldImgName);
            }
        } else if (post.getImgName() != null && updateParam.getDeleteImg() != null) {
            // ?????? ????????? ?????? ???????????? ?????? ??????
            imgFileStore.deleteFile(post.getImgName());
            post.setImgName(null);
        }

        post.setTitle(updateParam.getTitle());
        post.setContent(updateParam.getContent());
        post.setPostCategoryName(updateParam.getPostCategoryName());
    }

    @Transactional(readOnly = true)
    public Post findByToken(String token) throws PostNotFoundException {
        return postRepository.findByToken(token).orElseThrow(
                () -> new PostNotFoundException()
        );
    }


    @Transactional(readOnly = true)
    public List<Post> findAll() {
        return postRepository.findByOrderByCreatedAtDesc();
    }

    @Transactional
    public void increaseViews(Long id) {
        postRepository.increaseViews(id);
    }

}
