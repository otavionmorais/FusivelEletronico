package com.example.primeiraaplicacao.service;

import androidx.annotation.NonNull;

import com.example.primeiraaplicacao.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class ServicoNotificacao extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

    }

}
