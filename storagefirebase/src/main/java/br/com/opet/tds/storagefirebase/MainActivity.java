package br.com.opet.tds.storagefirebase;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CHOOSE = 1234;
    private ImageView imageSelector;
    private List<Uri> mSelected;
    private StorageReference mStorage;
    private FirebaseFirestore db;
    private FirebaseUser mUser;
    private StorageReference userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageSelector = findViewById(R.id.imageSelector);
        mStorage = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        Picasso.get().load(R.drawable.placeholder).into(imageSelector);

    }

    @Override
    protected void onStart(){
        super.onStart();
        //Picasso.get().load(R.drawable.placeholder).into(imageSelector);
    }

    public void saveOnFirebase(View view) {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        userRef = mStorage.child("images/"+mUser.getEmail()+"/"+Util.getTimestamp()+".png");
        userRef.putFile(mSelected.get(0))
        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                userRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        CollectionReference colecao = db.collection("users").document(mUser.getEmail()).collection("links");
                        Map<String,Object> link = new HashMap<>();
                        link.put("link",uri.toString());
                        colecao.add(link);
                        Toast.makeText(MainActivity.this, "Tarefa Executada!", Toast.LENGTH_SHORT).show();
                        Picasso.get().load(R.drawable.placeholder).into(imageSelector);
                    }
                });
            }
        });
    }

    public void selectImageFromDisk(View view) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if(aBoolean) {
                            Matisse.from(MainActivity.this)
                                    .choose(MimeType.ofImage())
                                    .countable(false)
                                    .thumbnailScale(0.9f)
                                    .imageEngine(new GlideV4Engine())
                                    .forResult(REQUEST_CODE_CHOOSE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            Log.d("Matisse", "mSelected: " + mSelected);

            Picasso.get().load(mSelected.get(0)).into(imageSelector);
        }
    }

    public void openImageList(View view) {
        Intent novatela = new Intent(MainActivity.this,ListarImagensActivity.class);
        startActivity(novatela);
    }
}
