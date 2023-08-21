//package com.tranhuutruong.BookStoreAPI.Service;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.cloud.storage.Blob;
//import com.google.cloud.storage.Bucket;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import com.google.firebase.cloud.StorageClient;
//import com.tranhuutruong.BookStoreAPI.Interface.ImageInterface;
//import com.tranhuutruong.BookStoreAPI.Security.config.FirebaseConfig;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.event.EventListener;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.http.codec.multipart.FilePart;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//
//import java.awt.image.BufferedImage;
//import java.io.IOException;
//
//@Service
//public class FirebaseImageService implements ImageInterface {
//
//    @Autowired
//    FirebaseConfig firebaseConfig;
//
//    @EventListener
//    public void init(ApplicationReadyEvent event)
//    {
//        try {
//
//            ClassPathResource serviceAccount = new ClassPathResource("firebase.json");
//
//            FirebaseOptions options = new FirebaseOptions.Builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
//                    .setStorageBucket(firebaseConfig.getBucketName())
//                    .build();
//
//            FirebaseApp.initializeApp(options);
//
//        } catch (Exception ex) {
//
//            ex.printStackTrace();
//
//        }
//    }
//
//    @Override
//    public String getImageUrl(String name) {
//        return String.format(firebaseConfig.imageUrl, name);
//    }
//
//    @Override
//    public String save(FilePart file) throws IOException {
//        Bucket bucket = StorageClient.getInstance().bucket();
//
//        String name = generateFileName(file.filename());
//
//        bucket.create(name,convertFilePartToByteArray(file),file.headers().getContentType().toString());
//        return firebaseConfig.prefixImageUrl.concat(name).concat(firebaseConfig.suffixImageUrl);
//    }
//
//    @Override
//    public String save(BufferedImage bufferedImage, String fileName) throws IOException {
//        byte[] bytes = getByteArrays(bufferedImage, getExtension(fileName));
//
//        com.google.cloud.storage.Bucket bucket = StorageClient.getInstance().bucket();
//
//        String name = generateFileName(fileName);
//
//        bucket.create(name, bytes);
//
//        return name;
//    }
//
//    @Override
//    public void delete(String name) throws IOException {
//        Bucket bucket = StorageClient.getInstance().bucket();
//
//        if (StringUtils.isEmpty(name)) {
//            throw new IOException("invalid file name");
//        }
//
//        Blob blob = bucket.get(name);
//
//        if (blob == null) {
//            throw new IOException("file not found");
//        }
//
//        blob.delete();
//    }
//}
