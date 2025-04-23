package com.example.qrcode.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;

@Controller
public class MainController {

    @GetMapping("/")
    public Object checkUserAgent(@RequestHeader("User-Agent") String userAgent) {
//        String userAgent = Optional.ofNullable(request.getHeader(HttpHeaders.USER_AGENT))
//                .orElse("")
//                .toLowerCase();

        if (userAgent.contains("wb") || userAgent.contains("webview") || userAgent.contains("qsecr") || userAgent.contains("curl") || userAgent.contains("bot")) {
//            return new RedirectView("http://localhost:5500/benign.html");
            return "benign";
        }

//        return new RedirectView("http://localhost:5500/evil3.html");
        return "evil3";
    }

    @GetMapping("/loop")
    public Object loop(HttpSession httpSession) {
        Integer count = (Integer) httpSession.getAttribute("redirectCount");
        if (count == null) {
            count = 0;
        }

        System.out.println("Redirect Count: " + count);

        if (count < 11) {
            httpSession.setAttribute("redirectCount", count + 1);
            return new RedirectView("/loop");
        }
        else {
            // 리디렉션 끝, 세션 초기화 후 pageB로 이동
            httpSession.removeAttribute("redirectCount");
            return "evil3";
        }

//        return new RedirectView("http://localhost:5500/evil3.html");
//        return "evil3";
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download() {
        File file = new File("server/file/cat.jpg");

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"cat.zip\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }


    @GetMapping("/evil-3")
    public Object evil_3() {
//        return "evil3";
        return new RedirectView("http://localhost:5500/evil3.html");
    }
}