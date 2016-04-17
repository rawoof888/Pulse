package com.hive3.pulse;

import com.parse.ParseClassName;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


@ParseClassName("Chat")

public class Conversation {

    public static final int STATUS_SENDING = 0;
    public static final int STATUS_SENT = 1;
    public static final int STATUS_FAILED = 2;
    private int status = STATUS_SENT;


    private String msg,sender,receiver;
    private Date date;
    public boolean isMine;
    private int message_id;


    private static DateFormat timeFormat = new SimpleDateFormat("K:mma");



    public Conversation(String msg, Date date, String sender)
    {
        this.msg = msg;
        this.date = date;
        this.sender = sender;
    }


    public Conversation(String Sender, String Receiver, String messageString, Date date, int ID, boolean isMINE){
        this.sender = sender;
        this.receiver = receiver;
        this.msg = messageString;
        this.date = date;
        this.message_id = ID;
        this.isMine = isMINE;





    }


    public Conversation()
    {
    }


    public String getMsg()
    {
        return msg;
    }


    public void setMsg(String msg)
    {
        this.msg = msg;
    }


    public boolean isSent()
    {
        return ParseUser.getCurrentUser().getUsername().equals(sender);
    }


    public Date getDate()
    {
        return date;
    }


    public void setDate(Date date)
    {
        this.date = date;
    }


    public String getSender()
    {
        return sender;
    }


    public void setSender(String sender)
    {
        this.sender = sender;
    }


    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public int getStatus()
    {
        return status;
    }


    public void setStatus(int status)
    {
        this.status = status;
    }


    public static String getCurrentTime() {

        Date today = Calendar.getInstance().getTime();
        return timeFormat.format(today);
    }





}
