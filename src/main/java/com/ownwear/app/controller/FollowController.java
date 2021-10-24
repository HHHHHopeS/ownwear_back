package com.ownwear.app.controller;

import com.ownwear.app.service.FollowService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/follow")
public class FollowController {
    private FollowService followService;

    @GetMapping("/toggle")
    public Boolean toggle(Long current_userid,Long target_userid){
        System.out.println("current_userid = " + current_userid);
        return followService.toggle(current_userid,target_userid);
    }


}
