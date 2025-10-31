package com.fer.apuw.lab.tweetie.user.repository;

import com.fer.apuw.lab.tweetie.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static com.example.jooq.tables.AppUser.APP_USER;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final DSLContext ctx;

    @Override
    public User getUserById(long id) {
        return ctx.selectFrom(APP_USER)
                .where(APP_USER.ID.eq(id))
                .fetchOneInto(User.class);
    }

    @Override
    public User getUserByUsername(String username) {
        return ctx.selectFrom(APP_USER)
                .where(APP_USER.USERNAME.eq(username))
                .fetchOneInto(User.class);
    }

    @Override
    public User getUserByEmail(String email) {
        return ctx.selectFrom(APP_USER)
                .where(APP_USER.EMAIL.eq(email))
                .fetchOneInto(User.class);
    }

    @Override
    public User createUser(User user) {
        return ctx.insertInto(APP_USER)
                .set(APP_USER.USERNAME, user.getUsername())
                .set(APP_USER.EMAIL, user.getEmail())
                .set(APP_USER.PASSWORD, user.getPassword())
                .set(APP_USER.CREATED_AT, user.getCreatedAt())
                .returning()
                .fetchOneInto(User.class);
    }

    @Override
    public void updateUser(User user) {
        var update = ctx.updateQuery(APP_USER);
        if (user.getUsername() != null) {
            update.addValue(APP_USER.USERNAME, user.getUsername());
        }
        if (user.getEmail() != null) {
            update.addValue(APP_USER.EMAIL, user.getEmail());
        }
        if (user.getPassword() != null) {
            update.addValue(APP_USER.PASSWORD, user.getPassword());
        }
        update.addConditions(APP_USER.ID.eq(user.getId()));
        update.execute();
    }

    @Override
    public void deleteUser(User user) {
        ctx.update(APP_USER)
                .set(APP_USER.DELETED_AT, user.getDeletedAt())
                .where(APP_USER.ID.eq(user.getId()))
                .execute();
    }
}
