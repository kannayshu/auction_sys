package com.auction_sys.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created By liuda on 2018/6/2
 */
public interface FileService {
    String upload(MultipartFile file, String path);
}
