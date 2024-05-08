package com.shj.springchatting.dto.test;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class TestResponseDto {

    @Setter
    private Integer inputInt;
    @Setter
    private Long inputLong;
    @Setter
    private String inputStr;

    private Integer testInt;
    private Long testLong;
    private String testStr;

    public TestResponseDto(Integer testInt, Long testLong, String testStr) {
        this.testInt = testInt;
        this.testLong = testLong;
        this.testStr = testStr;
    }
}
