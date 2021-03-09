package com.company.socialnetwork.SocialNetworkMicroservice.services;

import com.company.socialnetwork.SocialNetworkMicroservice.daos.IUser;
import com.company.socialnetwork.SocialNetworkMicroservice.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FollowServiceImplementation implements FollowService {

    @Autowired
    IUser iUser;

    @Override
    public void follow(int uerFollower, int userToFollow) {

    }

    /**
     *  TODO:
     *      -- get the properties to follow and un follow the user
     *
     * This process can be refactors using sync's or Main thread process
     * @param userFollower id from the user session
     * @param userToUnFollow user to follow
     */
    @Override
    public void unFollow(int userFollower, int userToUnFollow) {
        Optional<User> user = iUser.findById(userFollower);
        Optional<User> userToBeRemoved = iUser.findById(userToUnFollow);
        user.get().getFollowing().remove(userToBeRemoved.get());
    }

    @Override
    public List<User> getAllFollowings(int userId) {


        try {
            Optional<User> user = iUser.findById(userId);
            return user.get().getFollowing();
        }catch (Exception e){
            e.printStackTrace();
            return null ;
        }
    }
}