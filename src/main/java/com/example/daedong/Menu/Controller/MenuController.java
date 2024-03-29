package com.example.daedong.Menu.Controller;

import com.example.daedong.Dto.*;
import com.example.daedong.Main.Repository.UserRepository;
import com.example.daedong.Menu.FAQRepository;
import com.example.daedong.Menu.Service.MenuServiceImpl;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/daedong/menu")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MenuController {

    private final FAQRepository faqRepository;
    private final MenuServiceImpl menuService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @PostMapping("/createchatroom")
    public String CreateChatRoom(@RequestBody User user){
        String ObjectId = menuService.createChatRoom(user);
        menuService.insertChatRoomOnjectId(user,ObjectId);
        return ObjectId;
    }

    @GetMapping("/pastList/{userId}")
    public List<PastChatRoom> PastList(@PathVariable String userId) throws Exception{
        return menuService.selectPastChatTitle(userId);
    }

    @PostMapping("/updatetitle/{newChatTitle}")
    public String UpdateChatTitle(@RequestBody ChatRoom chatRoom, @PathVariable String newChatTitle){
        return menuService.updateChatTitle(chatRoom.get_id(), newChatTitle);
    }

    @PostMapping("deletechatroom")
    public String DeleteChatRoom(@RequestBody ChatRoom chatRoom){
        return menuService.deleteChatRoom(chatRoom.get_id());
    }

//    회원 정보 수정
    @PutMapping("/updateuserinformation/{_id}")
    public String updateUserInformation(@RequestBody UserForm userForm, @PathVariable String _id){
        User user = new User();
        user.set_id(_id);
        user.setName(userForm.getName());
        user.setPassword(passwordEncoder.encode(userForm.getPassword()));
        user.setSchoolEmail(userForm.getSchoolEmail());
        user.setSchoolName(userForm.getSchoolName());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setPushAlarm(userForm.isPushAlarm());
        user.setPersonalInformation(userForm.isPersonalInformation());
        user.setChatRoomOid(userRepository.findById(_id).get().getChatRoomOid());
        boolean isUpdate = menuService.update(user);
        if(isUpdate == false){
            return "false";
        }
        return "success";

    }

//    회원 정보 삭제 (회원 탈퇴)
    @PostMapping("/deleteuser")
    public String deleteUser(@RequestBody User user){
       boolean isDelete = menuService.deleteBySchoolEmail(user);
       if(isDelete == false){
           return "false";
       }
       return "success";
    }

    @GetMapping("/faq")
    public List<FAQs> findAll(){
        return faqRepository.findAll();
    }
}
