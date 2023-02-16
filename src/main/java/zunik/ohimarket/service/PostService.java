package zunik.ohimarket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zunik.ohimarket.domain.Post;
import zunik.ohimarket.repository.PostRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository repository;

    @Transactional
    public void save(Post post) {
        repository.save(post);
    }

    @Transactional(readOnly = true)
    public Optional<Post> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Post> findAll() {
        return repository.findByOrderByCreatedAtDesc();
    }

    @Transactional
    public void increaseViews(Long id) {
        repository.increaseViews(id);
    }

}
