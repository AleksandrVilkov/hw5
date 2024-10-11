package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HomeWorkTest {

    HomeWork homeWork = new HomeWork();

    @Test
    void check() {
        Assertions.assertEquals(-1.0, homeWork.calculate("1 + 2 * ( 3 - 4 )"));
        Assertions.assertEquals(11.0, homeWork.calculate("1 + 2 * ( sqr(3) - 4 )"));
        Assertions.assertEquals(21.0, homeWork.calculate("1 + pow(2,2) * ( sqr(3) - 4 )"));
        Assertions.assertEquals(20.0, homeWork.calculate("sin(0) + pow(2,2) * ( sqr(3) - 4 )"));
        Assertions.assertEquals(21.0, homeWork.calculate("cos(0) + pow(2,2) * ( sqr(3) - 4 )"));
        Assertions.assertEquals(1.0, homeWork.calculate("cos(0) + pow(2,2) * ( sqr(3) - sqr(3) )"));
        Assertions.assertEquals(-3.0, homeWork.calculate("( ( 1 + 2 ) * ( 3 - 4 ) )"));
        Assertions.assertEquals(-3.0, homeWork.calculate("( 1 + 2 ) * ( 3 - 4 )"));
    }

}