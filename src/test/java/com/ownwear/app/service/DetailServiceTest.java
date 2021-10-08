package com.ownwear.app.service;

import com.ownwear.app.form.IIndexHashTag;
import com.ownwear.app.form.IIndexUser;
import com.ownwear.app.form.IndexHashTag;
import com.ownwear.app.form.IndexUser;
import com.ownwear.app.model.User;
import com.ownwear.app.repository.LikePostRepository;
import com.ownwear.app.repository.PostHashTagRepository;
import com.ownwear.app.repository.PostRepository;
import com.ownwear.app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class DetailServiceTest {

    @Autowired
    private LikePostRepository likePostRepository;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostHashTagRepository postHashTagRepository;

    @Autowired
    private UserRepository userRepository;

    final ModelMapper mm = new ModelMapper();

    @Test
    public void a(){
//        Optional<Post> byId = postRepository.findById(8L);
//        byId.get().setPost_id(9L);
//        postRepository.save(byId.get());
//        List<LikePost> top6ByPost = likePostRepository.findTop6ByPost();
//        for (LikePost likePost : top6ByPost) {
//            ModelMapper mm = new ModelMapper();
//            LikepostForm map = mm.map(likePost, LikepostForm.class);
//            map.getPost().setEdate(null);
//            //System.out.println(mm.map(map.getPost(), PostForm.class));
//        }
      /*  List<Object[]> top9ByCountByHashtag = postHashTagRepository.findTop9ByCountByHashtag();
        for (Object[] o : top9ByCountByHashtag) {
            BigInteger intO = (BigInteger) o[0];
            String big = (String) o[1];
            Long longO = Long.parseLong(big);
            int i = intO.intValue();
            IndexHashTag postHashTagInfo = new IndexHashTag();
            postHashTagInfo.setHashtag_id(longO);
            postHashTagInfo.setCount(i);
            //System.out.println(postHashTagInfo);
        }*/

       /* List<IIndexHashTag> top9ByCountByHashtag = postHashTagRepository.findTop9ByCountByHashtagInterface();
        for (IIndexHashTag postHashTagInfo : top9ByCountByHashtag) {

            //System.out.println(postHashTagInfo.getHashtag_id());
            //System.out.println(postHashTagInfo.getCount());
        }
*/

        /*List<IIndexUser> iIndexUsers = userRepository.findTop7ByFollow();
        List<IndexUser> indexUsers = new ArrayList<>();
        for (IIndexUser iIndexUser : iIndexUsers) {
            IndexUser indexUser = new IndexUser();
            indexUser.setFollow(iIndexUser.getCount());
            Optional<User> byId = userRepository.findById(iIndexUser.getTo_user());
            indexUser.setUsername(byId.get().getUsername());
            //System.out.println(indexUser);
            indexUsers.add(indexUser);
        }*/
    }
}