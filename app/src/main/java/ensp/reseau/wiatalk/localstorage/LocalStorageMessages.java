package ensp.reseau.wiatalk.localstorage;

import android.content.Context;

import java.util.ArrayList;

import ensp.reseau.wiatalk.model.Group;
import ensp.reseau.wiatalk.model.IPCall;
import ensp.reseau.wiatalk.model.Message;
import ensp.reseau.wiatalk.model.MessageFile;
import ensp.reseau.wiatalk.model.User;

public class LocalStorageMessages {
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

    public static Message getMessageById(String messageId, Context context){
        MessageDAO messageDAO = new MessageDAO(context);
        messageDAO.open();
        Message message = null;
        try{
            message = messageDAO.getMessagesById(messageId);
        } finally {
            messageDAO.close();
        }
        return message;
    }

    public static MessageFile getMessageFileById(String messageFileId, Context context){
        MessageFileDAO messageFileDAO = new MessageFileDAO(context);
        messageFileDAO.open();
        MessageFile messageFile = null;
        try{
            messageFile = messageFileDAO.getMessageFileById(messageFileId);
        } finally {
            messageFileDAO.close();
        }
        return messageFile;
    }

    public static ArrayList<Message> getGroupsMessages(Group group, Context context){
        MessageDAO messageDAO = new MessageDAO(context);
        messageDAO.open();
        ArrayList<Message> messages = null;
        try{
            messages = messageDAO.getGroupsMessages(group.get_id());
            for (Message message: messages){
                message.setGroup(group);
                message.setSender(getUserById(message.getSenderId(), context));
                message.setReply(getMessageById(message.getReplyId(), context));
                if (!message.getFileId().isEmpty()) message.setFile(getMessageFileById(message.getFileId(), context));
            }
        } finally {
            messageDAO.close();
        }
        return messages;
    }

    public static void storeMessages(Message message, Context context){
        MessageDAO messageDAO = new MessageDAO(context);
        try{
            messageDAO.open();
            messageDAO.storeMessage(message);
            if (message.getFile()!=null) storeMessageFile(message.getFile(), context);
        } finally {
            messageDAO.close();
        }
    }

    public static void storeIPCalls(IPCall ipCall, Context context){
        IPCallsDAO ipCallsDAO = new IPCallsDAO(context);
        try{
            ipCallsDAO.open();
            ipCallsDAO.storeIPCall(ipCall);
        } finally {
            ipCallsDAO.close();
        }
    }

    public static void storeMessageFile(MessageFile messageFile, Context context){
        MessageFileDAO messageFileDAO = new MessageFileDAO(context);
        try{
            messageFileDAO.open();
            messageFileDAO.storeMessageFile(messageFile);
        } finally {
            messageFileDAO.close();
        }
    }
}
