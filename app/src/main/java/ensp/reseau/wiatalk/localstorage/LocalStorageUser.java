package ensp.reseau.wiatalk.localstorage;

import android.content.Context;

import java.util.ArrayList;

import ensp.reseau.wiatalk.model.AdminsGroups;
import ensp.reseau.wiatalk.model.Group;
import ensp.reseau.wiatalk.model.IPCall;
import ensp.reseau.wiatalk.model.Message;
import ensp.reseau.wiatalk.model.MessageFile;
import ensp.reseau.wiatalk.model.User;
import ensp.reseau.wiatalk.model.UsersGroups;

public class LocalStorageUser {

    public static void storeUser(User user, Context context, boolean isMe) {
        UserDAO userDAO = new UserDAO(context);
        try{
            userDAO.open();
            User existingUser = userDAO.getUserById(user.get_Id());
            if (existingUser==null) userDAO.storeUser(user, isMe);
            else userDAO.updateUser(user);

            if (isMe){
                for (Group group: user.getGroups()) LocalStorageDiscussions.storeGroup(group, context);
            }
        }
        finally {
            userDAO.close();
        }
    }

    public static ArrayList<IPCall> getCallHistory(Context context){
        IPCallsDAO ipCallsDAO = new IPCallsDAO(context);
        ipCallsDAO.open();
        ArrayList<IPCall> ipCalls = null;
        try{
            ipCalls = ipCallsDAO.getAllIPCall();
        } finally {
            ipCallsDAO.close();
        }
        return ipCalls;
    }

    public static User getUserById(String userId, Context context){
        UserDAO userDAO = new UserDAO(context);
        userDAO.open();
        User sender = null;
        try{
            sender = userDAO.getUserById(userId);
        } finally {
            userDAO.close();
        }
        return sender;
    }

    private static ArrayList<UsersGroups> getUserGroups(String userId, Context context){
        UsersGroupsDAO usersGroupsDAO = new UsersGroupsDAO(context);
        ArrayList<UsersGroups> userGroups = new ArrayList<>();
        try{
            usersGroupsDAO.open();
            userGroups = usersGroupsDAO.getUserGroupsIds(userId);
        } catch (Exception e){e.printStackTrace();}
        finally {
            usersGroupsDAO.close();
        }
        return userGroups;
    }

    public static void populateUser(User user, Context context, boolean isMe) {
        ArrayList<UsersGroups> userGroups = getUserGroups(user.get_Id(), context);
        ArrayList<Group> groups = new ArrayList<>();
        for (UsersGroups ug: userGroups){
            groups.add(LocalStorageDiscussions.getGroupById(ug.getGroupId(), context));
        }
        if (!isMe){
            int i=0;
            while (i<groups.size()){
                if (groups.get(i).getType()==Group.TYPE_IB) groups.remove(i);
                else i++;
            }
        }
        user.setGroups(groups);
    }

    public static User getMe(Context context){
        UserDAO userDAO = new UserDAO(context);
        User user = null;
        userDAO.open();
        try{
            user = userDAO.getMe();
        } catch (Exception e) {e.printStackTrace();}
        finally {
            userDAO.close();
        }
        return user;
    }

    public static ArrayList<User> getOtherUsers(Context context){
        UserDAO userDAO = new UserDAO(context);
        ArrayList<User> users = new ArrayList<>();
        userDAO.open();
        try{
            users = userDAO.getOthersUsers();
        } finally {
            userDAO.close();
        }
        return users;
    }

    public static void updateUser(User user, Context context){
        UserDAO userDAO = new UserDAO(context);
        userDAO.open();
        try{
            userDAO.updateUser(user);
        } finally {
            userDAO.close();
        }
    }

}
