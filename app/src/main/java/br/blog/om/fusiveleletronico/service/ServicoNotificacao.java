package br.blog.om.fusiveleletronico.service;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import br.blog.om.fusiveleletronico.MainActivity;

public class ServicoNotificacao extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

    }
}
