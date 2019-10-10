package br.blog.om.fusiveleletronico.service;

import android.os.Looper;
import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import br.blog.om.fusiveleletronico.GetJson;
import br.blog.om.fusiveleletronico.MainActivity;

public class ServicoNotificacao extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Looper.prepare();
        GetJson getJson = new GetJson(MainActivity.getStaticContext());
    }
}
