package org.foresthat.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {

    @Test
    void execTest() {
        assertEquals("hello from test!", Command.exec("echo 'hello from test!'"));
    }
}