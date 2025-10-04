package com.example.book_management.repository

import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import javax.sql.DataSource

@SpringBootTest
@ActiveProfiles("test")
@Transactional
abstract class BaseRepositoryTest {

    @Autowired
    protected lateinit var dslContext: DSLContext

    @Autowired
    protected lateinit var dataSource: DataSource

    protected fun executeQuery(sql: String): Int {
        return dslContext.execute(sql)
    }

    protected fun <T> executeQuery(sql: String, mapper: (org.jooq.Record) -> T): List<T> {
        return dslContext.fetch(sql).map(mapper)
    }
}
