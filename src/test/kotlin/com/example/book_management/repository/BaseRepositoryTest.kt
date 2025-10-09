package com.example.book_management.repository

import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
@Transactional
abstract class BaseRepositoryTest {

    @Autowired
    protected lateinit var dslContext: DSLContext

}
