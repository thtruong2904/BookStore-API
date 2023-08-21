//package com.tranhuutruong.BookStoreAPI.Interface;
//
//import org.springframework.core.io.buffer.DataBufferUtils;
//import org.springframework.http.codec.multipart.FilePart;
//import org.springframework.util.StringUtils;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.util.UUID;
//
//public interface ImageInterface {
//    String getImageUrl(String name);
//
//    String save(FilePart file) throws IOException;
//
//    String save(BufferedImage bufferedImage, String fileName) throws IOException;
//
//    void delete(String name) throws IOException;
//
//    default String generateFileName(String originalFileName) {
//        return UUID.randomUUID().toString() + getExtension(originalFileName);
//    }
//
//    default String getExtension(String fileName)
//    {
//        return StringUtils.getFilenameExtension(fileName);
//    }
//
//    default byte[] getByteArrays(BufferedImage bufferedImage, String format) throws IOException {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        try {
//            ImageIO.write(bufferedImage, format, byteArrayOutputStream);
//            byteArrayOutputStream.flush();
//            return byteArrayOutputStream.toByteArray();
//        }
//        catch (IOException e)
//        {
//            throw e;
//        }
//        finally {
//            byteArrayOutputStream.close();
//        }
//    }
//
//    default byte[] convertFilePartToByteArray(FilePart file) {
//        return DataBufferUtils.join(file.content()).map(dataBuffer -> dataBuffer.asByteBuffer().array()).block();
//    }
//
//}
