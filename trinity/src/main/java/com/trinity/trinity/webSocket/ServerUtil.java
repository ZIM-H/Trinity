//package com.trinity.trinity.webSocket;
//
//import io.netty.handler.ssl.SslContext;
//import io.netty.handler.ssl.SslContextBuilder;
//import io.netty.handler.ssl.util.SelfSignedCertificate;
//import lombok.NoArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//
//import javax.net.ssl.SSLException;
//import java.security.cert.CertificateException;
//
///**
// * Some useful methods for server side.
// */
//@Configuration
//@NoArgsConstructor
//public class ServerUtil {
//
////    private static final boolean SSL = System.getProperty("ssl") != null;
//
//
//    @Bean
//    public SslContext sslCtx() throws CertificateException, SSLException {
//        return ServerUtil.buildSslContext();
//    }
//
//
//    public static SslContext buildSslContext() throws CertificateException, SSLException {
////        if (!SSL) {
////            System.out.println("없다");
////            return SslContextBuilder.forClient().build();
////        }
//        System.out.println("있다");
//        SelfSignedCertificate ssc = new SelfSignedCertificate();
//        return SslContextBuilder
//                .forServer(ssc.certificate(), ssc.privateKey())
//                .build();
//    }
//}
