package com.example.daedong.Sign.Controller;

import com.example.daedong.Dto.LoginDto;
import com.example.daedong.Dto.User;
import com.example.daedong.Dto.UserForm;
import com.example.daedong.Main.Repository.UserRepository;
import com.example.daedong.Sign.Service.SignServiceImpl;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/daedong")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SignController {
    private final SignServiceImpl signService;

    private final UserRepository userRepository;

    // 로그인 페이지
//    @GetMapping("/login.do")
//    public String openLogin(){
//        return "login";
//    }

    // 로그인
    @PostMapping("/login")
    @ResponseBody
    public User login(HttpServletRequest request, @RequestBody LoginDto loginDto) {

        // 1. 회원 정보 조회
//        String schoolEmail = request.getParameter("schoolEmail");
//        String password = request.getParameter("password");
        User user = signService.login(loginDto.getSchoolEmail(), loginDto.getPassword());

        // 2. 세션에 회원 정보 저장 & 세션 유지 시간 설정
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("loginUser", user);
            session.setMaxInactiveInterval(60 * 30);
        }

        return user;
    }

    // 로그아웃
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        try {
            System.out.print("before : ");
            Enumeration<String> attributes = session.getAttributeNames();
            while (attributes.hasMoreElements()) {
                String attribute = (String) attributes.nextElement();
                System.out.println(attribute + " : " + session.getAttribute(attribute));
            }

            session.invalidate();

            System.out.print("after : ");
            while (attributes.hasMoreElements()) {
                String attribute = (String) attributes.nextElement();
                System.out.println(attribute + " : " + session.getAttribute(attribute));
            }
            return "success";
        } catch (Exception e) {
            // 예외 처리 로직을 여기에 추가
            e.printStackTrace(); // 예외 정보를 출력하거나 로깅할 수 있습니다.
            return "error";
        }
    }

    // 회원가입
    @PostMapping("/join")
    public String saveMember(@RequestBody final UserForm userForm) {
        return signService.saveMember(userForm);
    }


    //    SchoolEmail 중복 체크
    @GetMapping("/repeatCheck")
    public boolean repeatCheckSchoolEmail(@RequestParam String schoolEmail) {
        return signService.checkSchoolEmail(schoolEmail);
    }
}
