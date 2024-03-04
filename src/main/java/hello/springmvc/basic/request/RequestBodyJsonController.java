package hello.springmvc.basic.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.springmvc.basic.HelloData;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
public class RequestBodyJsonController {

    private ObjectMapper objectMapper = new ObjectMapper();

    //1.서블릿과 동일
    @PostMapping("/request-body-json-v1")
    public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);

        response.getWriter().write("ok");
    }

    //2.문자 받는거 쉽게 하기
    @PostMapping("/request-body-json-v2")
    public String requestBodyJsonV2(@RequestBody String messageBody) throws IOException {
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);

        return "ok";
    }

    //3.매핑 바로 하기. request 바디에 직접만든 객체 직접 연결해서 컨버트
    //@RequestBody 없으면 modelAttribute 붙어버림. -> null 들어감
    @PostMapping("/request-body-json-v3")
    public String requestBodyJsonV3(@RequestBody HelloData helloData) {

        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }

    //4.HttpEntity 사용
    //HttpEntity 사용할 때는 httpEntity.getBody()로 꺼내야 한다
    @PostMapping("/request-body-json-v4")
    @ResponseBody
    public String requestBodyJsonV4(HttpEntity<HelloData> httpEntity) {
        HelloData data = httpEntity.getBody();
        log.info("username={}, age={}", data.getUsername(), data.getAge());

        return "ok";
    }

    @PostMapping("/request-body-json-v5")
    @ResponseBody
    //http json 컨버터 - request에 적용되지만 나갈때도 적용됨. 리턴 타입에 객체를 쓸 수 있음
    //http컨버터에 의해 문자가 json이 됨. 반환 할 때 http컨버터에 의해 객체가 다시 json문자열이 되서 나감.
    //accept 설정이 json을 허용해야 한다.
    public HelloData requestBodyJsonV5(@RequestBody HelloData data) {
        log.info("username={}, age={}", data.getUsername(), data.getAge());

        return data;
    }
}
