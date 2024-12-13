package com.programmingtechie.HIS.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadResponse {
    private String fileName;  // Tên tệp duy nhất đã tải lên
    private String bucketName; // Tên bucket chứa tệp
}
