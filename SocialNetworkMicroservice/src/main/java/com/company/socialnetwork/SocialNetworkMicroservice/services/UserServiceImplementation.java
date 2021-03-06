package com.company.socialnetwork.SocialNetworkMicroservice.services;


import com.company.socialnetwork.SocialNetworkMicroservice.daos.IUser;
import com.company.socialnetwork.SocialNetworkMicroservice.entities.User;
import com.company.socialnetwork.SocialNetworkMicroservice.services.errorhandlers.UserNotInDataBaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private IUser iUser;


    @Override
    public List<User> searchAllUsers() {
        return iUser.findAll();
    }

    @Override
    public Optional<User> searchSpecificUser(Integer id) throws UserNotInDataBaseException {

        Optional<User> users = iUser.findById(id);
        if(users.isPresent()){
            User user = users.get();
            if(user.getUserId() == null){
                throw new UserNotInDataBaseException("THe user is not present in database");
            }

        }
        return users;
    }

    /**
     * @param user object to be inserted into database
     * @return the new user saved
     */
    @Override
    public User saveNewUser(User user) {
        return iUser.save(user);
    }

    /**
     *
     * @param userToFollower the session active from te user
     * @param userToFollow the user who gonna follow
     */
    @Override
    public void follow(int userToFollower, int userToFollow) {

        Optional<User> findUserToFollow = iUser.findById(userToFollow);
        Optional<User> userFollower = iUser.findById(userToFollower);
        userFollower.ifPresent(user -> user.getFollowers().add(findUserToFollow.get()));
        iUser.save(userFollower.get());

    }

    /**
     * @param user primary key to fetch the from database
     * @return object which the user info and how many users are following
     */
    @Override
    public Object sendInfoUser(int user){
        Optional<User> userFound = iUser.findById(user);
        HashMap<Object, Object> userInfo = new HashMap<>();
        userInfo.put("User", userFound);
        userInfo.put("Following",userFound.get().getFollowers().size());
        return userInfo;
    }

    /**
     * This process can be refactors using sync's or Main thread process
     * @param userFollower id from the user session
     * @param userToDelete user to follow
     */
    @Override
    public void unFollowUser(int userFollower, int userToDelete){
        Optional<User> user = iUser.findById(userFollower);
        Optional<User> userToBeRemoved = iUser.findById(userToDelete);
        user.get().getFollowers().remove(userToBeRemoved.get());
    }

    /**
     *
     * @param id from the user follower
     * @return all the followers
     */
    @Override
    public List<User> getAllFollowers(Integer id) {

        Optional<User> user = iUser.findById(id);
        return user.map(User::getFollowers).orElse(null);

    }
}
