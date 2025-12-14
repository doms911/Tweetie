package com.fer.apuw.lab.tweetie.user.repository;

import com.fer.apuw.lab.tweetie.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.jooq.tables.AppUser.APP_USER;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final DSLContext ctx;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> getUserById(Long id) {
        return ctx.selectFrom(APP_USER)
                .where(APP_USER.ID.eq(id))
                .and(APP_USER.DELETED_AT.isNull())
                .fetchOptionalInto(User.class);
    }

    @Override
    public List<User> getAllUsers() {
        return ctx.selectFrom(APP_USER)
                .where(APP_USER.DELETED_AT.isNull())
                .fetchInto(User.class);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return ctx.selectFrom(APP_USER)
                .where(APP_USER.USERNAME.eq(username))
                .and(APP_USER.DELETED_AT.isNull())
                .fetchOptionalInto(User.class);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return ctx.selectFrom(APP_USER)
                .where(APP_USER.EMAIL.eq(email))
                .and(APP_USER.DELETED_AT.isNull())
                .fetchOptionalInto(User.class);
    }

    @Override
    public boolean existByUsername(String username) {
        Integer count = ctx.selectCount()
                .from(APP_USER)
                .where(APP_USER.USERNAME.eq(username))
                .and(APP_USER.DELETED_AT.isNull())
                .fetchOne(0, Integer.class);
        return count != null && count > 0;
    }

    @Override
    public boolean existByEmail(String email) {
        Integer count = ctx.selectCount()
                .from(APP_USER)
                .where(APP_USER.EMAIL.eq(email))
                .and(APP_USER.DELETED_AT.isNull())
                .fetchOne(0, Integer.class);
        return count != null && count > 0;
    }

    @Override
    @Transactional
    public User createUser(User user) {
        String hashedPwd = passwordEncoder.encode(user.getPassword());
        return ctx.insertInto(APP_USER)
                .set(APP_USER.USERNAME, user.getUsername())
                .set(APP_USER.EMAIL, user.getEmail())
                .set(APP_USER.ROLE_TYPE, user.getRoleType().name())
                .set(APP_USER.PASSWORD, hashedPwd)
                .set(APP_USER.CREATED_AT, LocalDateTime.now())
                .returning()
                .fetchOneInto(User.class);
    }

    @Override
    @Transactional
    public Optional<User> updateUser(User user) {
        var update = ctx.updateQuery(APP_USER);

        if (user.getId() != null) {
            update.addValue(APP_USER.USERNAME, user.getUsername());
        }

        if (user.getEmail() != null) {
            update.addValue(APP_USER.EMAIL, user.getEmail());
        }

        update.addConditions(APP_USER.ID.eq(user.getId()));
        update.addConditions(APP_USER.DELETED_AT.isNull());

        int affected = update.execute();
        if (affected == 0) {
            return Optional.empty();
        }

        return getUserById(user.getId());
    }

    @Override
    @Transactional
    public Optional<User> updateUserPassword(User user) {
        var update = ctx.updateQuery(APP_USER);

        if  (user.getPassword() != null) {
            String hashed = passwordEncoder.encode(user.getPassword());
            update.addValue(APP_USER.PASSWORD, hashed);
        }

        update.addConditions(APP_USER.ID.eq(user.getId()));
        update.addConditions(APP_USER.DELETED_AT.isNull());

        int affected = update.execute();

        if (affected == 0) {
            return Optional.empty();
        }

        return getUserById(user.getId());
    }

    @Override
    public boolean softDeleteUserById(Long id) {
        int affected = ctx.update(APP_USER)
                .set(APP_USER.DELETED_AT, LocalDateTime.now())
                .where(APP_USER.ID.eq(id))
                .and(APP_USER.DELETED_AT.isNull())
                .execute();

        return affected > 0;
    }
}
