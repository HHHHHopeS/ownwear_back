package com.ownwear.app.service;

import com.ownwear.app.entity.Follow;
import com.ownwear.app.entity.User;
import com.ownwear.app.repository.FollowRepository;
import com.ownwear.app.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class FollowService {
    private FollowRepository followRepository;
    private UserRepository userRepository;

    public Boolean toggle(Long current_userid, Long target_userid) {
        Optional<User> currentUserOp = userRepository.findById(current_userid);
        Optional<User> targetUserOp = userRepository.findById(target_userid);
        if (currentUserOp.isPresent() && targetUserOp.isPresent()) {
            User currentUser = currentUserOp.get();
            User targetUser = targetUserOp.get();
            Optional<Follow> byUsers = followRepository.findByUsers(currentUser, targetUser);
            if (byUsers.isPresent()) {
                followRepository.deleteById(byUsers.get().getFollowid());
                return false;
            } else {
                Follow follow = new Follow(currentUser,targetUser);
                followRepository.save(follow);
                return true;
            }
        }
        return null;//todo 잘못된 요청

    }
}
