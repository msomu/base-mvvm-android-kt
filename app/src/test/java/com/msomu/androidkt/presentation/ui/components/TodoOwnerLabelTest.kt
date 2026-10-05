package com.msomu.androidkt.presentation.ui.components

import org.junit.Assert.assertEquals
import org.junit.Test

class TodoOwnerLabelTest {
    @Test
    fun todoOwnerLabel_includesUserId() {
        assertEquals("User #1", todoOwnerLabel(1))
        assertEquals("User #8612", todoOwnerLabel(8612))
    }
}
