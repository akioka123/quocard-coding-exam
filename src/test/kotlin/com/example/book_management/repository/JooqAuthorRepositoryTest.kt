package com.example.book_management.repository

import com.example.book_management.dto.author.Author
import com.example.book_management.dto.author.AuthorId
import com.example.book_management.dto.author.AuthorName
import com.example.book_management.dto.author.BirthDate
import com.example.book_management.tables.references.AUTHORS
import example.testconfig.JooqTestSchemaConfig
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.dao.DuplicateKeyException
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@ActiveProfiles("test")
@Import(JooqTestSchemaConfig::class)
@Sql(scripts = ["/sql/JooqAuthorRepository.sql"])
@DisplayName("JooqAuthorRepository 統合テスト")
class JooqAuthorRepositoryTest : BaseRepositoryTest() {

    @Autowired
    private lateinit var authorRepository: JooqAuthorRepository

    @Nested
    @DisplayName("insert メソッド")
    inner class InsertTest {

        @Test
        @DisplayName("正常系：有効な著者情報で挿入が成功する")
        fun insert_success() {
            // Given
            val id = AuthorId(UUID.randomUUID())
            val name = AuthorName("テスト著者")
            val birthDate = BirthDate(LocalDate.of(1990, 1, 1))
            val author = Author(
                id, name, birthDate, LocalDateTime.now(), LocalDateTime.now()
            )

            // When
            authorRepository.insert(author, emptyList())

            // Then
            val insertedAuthor = authorRepository.findById(id)
            assertThat(insertedAuthor).isNotNull()
            assertThat(insertedAuthor!!.id).isEqualTo(id)
            assertThat(insertedAuthor.name).isEqualTo(name)
            assertThat(insertedAuthor.birthDate).isEqualTo(birthDate)
        }

        @Test
        @DisplayName("異常系：重複IDで挿入時に例外が発生する")
        fun insert_duplicateId() {
            // Given
            val id = AuthorId(UUID.randomUUID())
            val name1 = AuthorName("著者1")
            val name2 = AuthorName("著者2")
            val birthDate = BirthDate(LocalDate.of(1990, 1, 1))
            val author1 = createTestAuthor(id, name1, birthDate)
            val author2 = createTestAuthor(id, name2, birthDate)

            authorRepository.insert(author1, emptyList())

            // When & Then
            assertThatThrownBy { authorRepository.insert(author2, emptyList()) }
                .isInstanceOf(DuplicateKeyException::class.java)
        }

        @Test
        @DisplayName("境界値：最小文字数の著者名で挿入が成功する")
        fun insert_minLengthName() {
            // Given
            val id = AuthorId(UUID.randomUUID())
            val name = AuthorName("A")
            val birthDate = BirthDate(LocalDate.of(1990, 1, 1))

            // When
            val author = createTestAuthor(id, name, birthDate)
            authorRepository.insert(author, emptyList())

            // Then
            val insertedAuthor = authorRepository.findById(id)
            assertThat(insertedAuthor).isNotNull()
            assertThat(insertedAuthor!!.name.value).isEqualTo("A")
        }

        @Test
        @DisplayName("境界値：最大文字数の著者名で挿入が成功する")
        fun insert_maxLengthName() {
            // Given
            val id = AuthorId(UUID.randomUUID())
            val name = AuthorName("A".repeat(20)) // VARCHAR(20)の最大長
            val birthDate = BirthDate(LocalDate.of(1990, 1, 1))

            // When
            val author = createTestAuthor(id, name, birthDate)
            authorRepository.insert(author, emptyList())

            // Then
            val insertedAuthor = authorRepository.findById(id)
            assertThat(insertedAuthor).isNotNull()
            assertThat(insertedAuthor!!.name.value).isEqualTo("A".repeat(20))
        }
    }

    @Nested
    @DisplayName("update メソッド")
    inner class UpdateTest {

        @Test
        @DisplayName("正常系：有効な著者情報で更新が成功する")
        fun update_success() {
            // Given
            val id = AuthorId(UUID.randomUUID())
            val originalName = AuthorName("元の名前")
            val newName = AuthorName("新しい名前")
            val birthDate = BirthDate(LocalDate.of(1990, 1, 1))

            val author = createTestAuthor(id, originalName, birthDate)
            authorRepository.insert(author, emptyList())

            // 挿入された著者のupdatedAtを取得
            val insertedAuthor = authorRepository.findById(id)
            assertThat(insertedAuthor).isNotNull()
            val expectedUpdatedAt = insertedAuthor!!.updatedAt

            Thread.sleep(100) // updated_atの差分を作る

            // When
            val updatedRows = authorRepository.update(id, newName, birthDate, expectedUpdatedAt)

            // Then
            assertThat(updatedRows).isEqualTo(1)
            val updatedAuthor = authorRepository.findById(id)
            assertThat(updatedAuthor).isNotNull()
            assertThat(updatedAuthor!!.name).isEqualTo(newName)
        }

        @Test
        @DisplayName("異常系：存在しない著者IDで更新時に0件更新される")
        fun update_nonExistentAuthor() {
            // Given
            val id = AuthorId(UUID.randomUUID())
            val name = AuthorName("テスト著者")
            val birthDate = BirthDate(LocalDate.of(1990, 1, 1))
            val updatedAt = LocalDateTime.now()

            // When
            val updatedRows = authorRepository.update(id, name, birthDate, updatedAt)

            // Then
            assertThat(updatedRows).isEqualTo(0)
        }

        @Test
        @DisplayName("異常系：楽観排他制御で更新日時が異なる場合0件更新される")
        fun update_optimisticLockingFailure() {
            // Given
            val id = AuthorId(UUID.randomUUID())
            val name = AuthorName("テスト著者")
            val birthDate = BirthDate(LocalDate.of(1990, 1, 1))
            val wrongUpdatedAt = LocalDateTime.of(2020, 1, 1, 0, 0)

            val author = createTestAuthor(id, name, birthDate)
            authorRepository.insert(author, emptyList())

            // When
            val updatedRows = authorRepository.update(id, name, birthDate, wrongUpdatedAt)

            // Then
            assertThat(updatedRows).isEqualTo(0)
        }

        @Test
        @DisplayName("境界値：空の書籍IDリストで更新")
        fun update_emptyBookIds() {
            // Given
            val id = AuthorId(UUID.randomUUID())
            val name = AuthorName("テスト著者")
            val birthDate = BirthDate(LocalDate.of(1990, 1, 1))

            val author = createTestAuthor(id, name, birthDate)
            authorRepository.insert(author, emptyList())

            // 挿入された著者のupdatedAtを取得
            val insertedAuthor = authorRepository.findById(id)
            assertThat(insertedAuthor).isNotNull()
            val expectedUpdatedAt = insertedAuthor!!.updatedAt

            Thread.sleep(100) // updated_atの差分を作る

            // When
            val updatedRows = authorRepository.update(id, name, birthDate, expectedUpdatedAt)

            // Then
            assertThat(updatedRows).isEqualTo(1)
            val updatedAuthor = authorRepository.findById(id)
            assertThat(updatedAuthor).isNotNull()
            assertThat(updatedAuthor!!.name).isEqualTo(name)
        }
    }

    @Nested
    @DisplayName("findById メソッド")
    inner class FindByIdTest {

        @Test
        @DisplayName("正常系：存在する著者IDで著者情報が返される")
        fun findById_exists() {
            // Given
            val id = AuthorId(UUID.randomUUID())
            val name = AuthorName("テスト著者")
            val birthDate = BirthDate(LocalDate.of(1990, 1, 1))

            val author = createTestAuthor(id, name, birthDate)
            authorRepository.insert(author, emptyList())

            // When
            val foundAuthor = authorRepository.findById(id)

            // Then
            assertThat(foundAuthor).isNotNull()
            assertThat(foundAuthor!!.id).isEqualTo(id)
            assertThat(foundAuthor.name).isEqualTo(name)
            assertThat(foundAuthor.birthDate).isEqualTo(birthDate)
        }

        @Test
        @DisplayName("正常系：存在しない著者IDでnullが返される")
        fun findById_notExists() {
            // Given
            val id = AuthorId(UUID.randomUUID())

            // When
            val foundAuthor = authorRepository.findById(id)

            // Then
            assertThat(foundAuthor).isNull()
        }
    }

    @Nested
    @DisplayName("SQL発行確認テスト")
    inner class SqlExecutionTest {

        @Test
        @DisplayName("実際のSQLが発行されることを確認")
        fun verifySqlExecution() {
            // Given
            val id = AuthorId(UUID.randomUUID())
            val name = AuthorName("SQLテスト著者")
            val birthDate = BirthDate(LocalDate.of(1990, 1, 1))

            // When
            val author = createTestAuthor(id, name, birthDate)
            authorRepository.insert(author, emptyList())

            // Then - 直接SQLで確認
            val count = dslContext.selectCount()
                .from(AUTHORS)
                .where(AUTHORS.ID.eq(id.value))
                .fetchOne(0, Int::class.java)

            assertThat(count).isEqualTo(1)

            // 実際のレコードを取得して確認
            val record = dslContext.selectFrom(AUTHORS)
                .where(AUTHORS.ID.eq(id.value))
                .fetchOne()

            assertThat(record).isNotNull()
            assertThat(record!!.getValue(AUTHORS.NAME)).isEqualTo("SQLテスト著者")
            assertThat(record.getValue(AUTHORS.BIRTH_DATE)).isEqualTo(LocalDate.of(1990, 1, 1))
        }
    }

    // テストデータ作成用のヘルパーメソッド
    private fun createTestAuthor(id: AuthorId, name: AuthorName, birthDate: BirthDate): Author {
        return Author(
            id = id,
            name = name,
            birthDate = birthDate,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }
}
