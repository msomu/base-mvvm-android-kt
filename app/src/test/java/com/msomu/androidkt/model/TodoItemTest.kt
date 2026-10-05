package com.msomu.androidkt.model

import org.junit.Assert.assertEquals
import org.junit.Test

class TodoItemTest {
    @Test
    fun ownerLabel_matchesHomeRowContract() {
        val todo = TodoItem(completed = false, id = 1, title = "delectus aut autem", userId = 1)
        assertEquals("User #1", todo.ownerLabel)
    }
}
