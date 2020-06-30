package com.winteree.api.entity;

import lombok.Data;

import java.io.File;

/**
 * <p>Title: FileDTO</p>
 * <p>Description: 文件传输类</p>
 *
 * @author RenFei
 * @date : 2020-06-30 16:00
 */
@Data
public class FileDTO {
    String fileUrl;
    File file;
}
