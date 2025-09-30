package com.example.book_management.repository

import com.example.book_management.dto.author.Author
import com.example.book_management.dto.author.AuthorId
import org.jooq.DSLContext
import com.example.book_management.dto.author.AuthorName
import com.example.book_management.dto.author.BirthDate
import com.example.book_management.tables.references.AUTHORS
import org.springframework.stereotype.Repository
import java.time.LocalDateTime


/**
 * 著者リポジトリインターフェース
 * ドメイン層とインフラ層の境界を定義
 */
@Repository
class AuthorRepository(private val dsl: DSLContext) {
    /**
     * 著者を保存する
     */
    fun insert(id: AuthorId, name: AuthorName, birthDay: BirthDate) =
        dsl.insertInto(AUTHORS)
            .set(AUTHORS.ID, id.value)
            .set(AUTHORS.NAME, name.value)
            .set(AUTHORS.BIRTH_DATE, birthDay.value)
            .execute()

    fun existsByNameAndBirthDate(name: AuthorName, birthDate: BirthDate): Boolean =
        dsl.fetchExists(
            AUTHORS,
            AUTHORS.NAME.eq(name.value).and(AUTHORS.BIRTH_DATE.eq(birthDate.value))
        )

    /**
     * 著者を更新する（楽観排他制御付き）
     */
    fun update(id: AuthorId, name: AuthorName, birthDate: BirthDate, expectedUpdatedAt: LocalDateTime): Int =
        dsl.update(AUTHORS)
            .set(AUTHORS.NAME, name.value)
            .set(AUTHORS.BIRTH_DATE, birthDate.value)
            .set(AUTHORS.UPDATED_AT, LocalDateTime.now())
            .where(AUTHORS.ID.eq(id.value))
            .and(AUTHORS.UPDATED_AT.eq(expectedUpdatedAt))
            .execute()

    /**
     * 著者を取得する
     */
    fun findById(id: AuthorId): Author? =
        dsl.selectFrom(AUTHORS)
            .where(AUTHORS.ID.eq(id.value))
            .fetchOne()
            ?.let { record -> Author.fromRecord(record) }


}
