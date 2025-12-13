package com.fer.apuw.lab.tweetie.post.repository;

import com.fer.apuw.lab.tweetie.post.model.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.jooq.tables.Post.POST;


@Slf4j
@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository{
    private final DSLContext ctx;

    @Override
    @Transactional
    public Post createPost(Post post) {
        return ctx.insertInto(POST)
                .set(POST.USER_ID, post.getUserId())
                .set(POST.TITLE, post.getTitle())
                .set(POST.CONTENT, post.getContent())
                .set(POST.CREATED_AT, LocalDateTime.now())
                .returning()
                .fetchOneInto(Post.class);
    }

    @Override
    public Optional<Post> getPost(Long id) {
        return ctx.selectFrom(POST)
                .where(POST.ID.eq(id))
                .and(POST.DELETED_AT.isNull())
                .fetchOptionalInto(Post.class);
    }

    @Override
    public List<Post> getAllPosts() {
        return ctx.selectFrom(POST)
                .where(POST.DELETED_AT.isNull())
                .fetchInto(Post.class);
    }

    @Override
    public List<Post> getPostsByUserId(Long userId) {
        return ctx.selectFrom(POST)
                .where(POST.DELETED_AT.isNull())
                .and(POST.USER_ID.eq(userId))
                .fetchInto(Post.class);
    }

    @Override
    @Transactional
    public Optional<Post> updatePost(Post post) {
        var update = ctx.updateQuery(POST);

        if (post.getTitle() != null) {
            update.addValue(POST.TITLE, post.getTitle());
        }

        if (post.getContent() != null) {
            update.addValue(POST.CONTENT, post.getContent());
        }

        update.addConditions(POST.ID.eq(post.getId()));
        update.addConditions(POST.DELETED_AT.isNull());

        int affected = update.execute();
        if (affected == 0) {
            return Optional.empty();
        }

        return getPost(post.getId());
    }

    @Override
    public boolean softDeletePostById(Long id) {
        int affected = ctx.update(POST)
                .set(POST.DELETED_AT, LocalDateTime.now())
                .where(POST.ID.eq(id))
                .and(POST.DELETED_AT.isNull())
                .execute();
        return affected > 0;
    }
}
