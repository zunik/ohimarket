package zunik.ohimarket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zunik.ohimarket.domain.Post;
import zunik.ohimarket.repository.PostRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository repository;

    @Transactional
    public void save(Post post) {
        repository.save(post);
    }

    @Transactional
    public List<Post> findAll() {
        return repository.findByOrderByCreatedAtDesc();
    }
}
