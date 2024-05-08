package com.shj.springchatting.controller;

import com.shj.springchatting.dto.test.TestRequestDto;
import com.shj.springchatting.dto.test.TestResponseDto;
import com.shj.springchatting.response.ResponseCode;
import com.shj.springchatting.response.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// @CrossOrigin(origins = "*", allowedHeaders = "*")  // SecurityConfig에 대신 만들어주었음.
@Tag(name = "Test")
@RestController
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/health")
    @Operation(summary = "서버 헬스체크 [jwt X]")
    public ResponseEntity<ResponseData> healthCheck() {
        return ResponseData.toResponseEntity(ResponseCode.HEALTHY_SUCCESS);
    }

    @PostMapping("/test/{testLong}")
    @Operation(summary = "테스트 [jwt X]", description = "URL : /test/{Long}?testStr={String} + RequestBody")
    public ResponseEntity<ResponseData<TestResponseDto>> testapi(
            @PathVariable(value = "testLong") Long testLong,  // value=""를 작성해주어야만, Swagger에서 api테스트할때 이름값이 뜸.
            @RequestParam(value = "testStr") String testStr,
            @RequestBody TestRequestDto testRequestDto) {

        TestResponseDto testResponseDto = new TestResponseDto(12345, testLong, testStr);
        testResponseDto.setInputInt(testRequestDto.getInputInt());
        testResponseDto.setInputLong(testRequestDto.getInputLong());
        testResponseDto.setInputStr(testRequestDto.getInputStr());

        return ResponseData.toResponseEntity(ResponseCode.TEST_SUCCESS, testResponseDto);
    }
}
