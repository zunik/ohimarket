package zunik.ohimarket.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zunik.ohimarket.domain.Member;
import zunik.ohimarket.domain.Post;
import zunik.ohimarket.exception.MemberNotFoundException;
import zunik.ohimarket.repository.PostQueryRepository;
import zunik.ohimarket.repository.dto.MemberSummeryDto;
import zunik.ohimarket.service.dto.ProfileResponseDto;
import zunik.ohimarket.repository.MemberRepository;
import zunik.ohimarket.repository.PostRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PostQueryRepository postQueryRepository;

    @Transactional(readOnly = true)
    public ProfileResponseDto getDetail(String memberToken) throws MemberNotFoundException {
        Member member = memberRepository.findByToken(memberToken).orElseThrow(
                () -> new MemberNotFoundException()
        );
        MemberSummeryDto summeryDto = postQueryRepository.summaryByMemberId(member.getId());

        List<Post> writtenPosts = postRepository.findByMemberIdOrderByCreatedAtDesc(member.getId());
        List<Post> commentaryPosts = postQueryRepository.getCommentaryPosts(member.getId());

        ProfileResponseDto profileResponseDto = new ProfileResponseDto();
        profileResponseDto.setMember(member);
        profileResponseDto.setSummary(summeryDto);
        profileResponseDto.setWrittenPosts(writtenPosts);
        profileResponseDto.setCommentaryPosts(commentaryPosts);

        return profileResponseDto;
    }

}
