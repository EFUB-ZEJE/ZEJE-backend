package ewha.efub.zeje.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import ewha.efub.zeje.util.errors.CustomException;
import ewha.efub.zeje.util.errors.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class ImageUploadService {

    private AmazonS3 s3Client;
    public static final String CLOUD_FRONT_DOMAIN_NAME = "dnyenjxny3wzj.cloudfront.net";

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    public String uploadImage(String currentFile, MultipartFile file) throws IOException {
        boolean fileTypeFlag = true;

        if(file!=null){
            fileTypeFlag = checkContentType(file);

            if(fileTypeFlag == true) {
                deleteS3(0, currentFile);
                String fileName = uploadS3(0, file);
                return makeFileUrl(0, fileName);
            }
            else {
                return "type error";
            }
        }
        return "null error";
    }

    public String uploadImage(int directory, MultipartFile file) throws IOException {
        if(file == null) {
            throw new CustomException(ErrorCode.INVALID_IMAGE_FILE);
        }

        if(!checkContentType(file)) {
            throw new CustomException(ErrorCode.INVALID_IMAGE_FILE);
        }

        String fileName = uploadS3(directory, file);
        return makeFileUrl(directory, fileName);
    }



    public String uploadS3 (int directory, MultipartFile file) throws IOException {
        String fileName = makeFileName(file);
        String strDirectory;

        if(directory == 0) {
            strDirectory = "/profile";
        }
        else if (directory == 1) {
            strDirectory = "/diary";
        }
        else if (directory == 2) {
            strDirectory = "/review";
        }
        else {
            strDirectory = "/etc";
        }

        s3Client.putObject(new PutObjectRequest(bucket + strDirectory, fileName, file.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return fileName;
    }

    public void deleteS3 (int directory, String currentFilePath) {
        String strDirectory;

        if(directory == 0) strDirectory = "/profile";
        else if (directory == 1) strDirectory = "/diary";
        else strDirectory = "/review";

        if ("".equals(currentFilePath) == false && currentFilePath != null) {
            String currFile = parseFileName(currentFilePath);
            boolean isExistObject = s3Client.doesObjectExist(bucket + strDirectory, currFile);

            if (isExistObject == true) {
                s3Client.deleteObject(bucket + strDirectory, currFile);
            }
        }
    }

    public boolean checkContentType(MultipartFile file) {
        boolean fileTypeFlag;
        String contentType = file.getContentType();

        if(ObjectUtils.isEmpty(contentType)){
            fileTypeFlag = false;
        } else {
            if(contentType.contains("image/jpeg")) {
                fileTypeFlag = true;
            }
            else if(contentType.contains("image/png")) {
                fileTypeFlag = true;
            }
            else{
                fileTypeFlag = false;
            }
        }
        return fileTypeFlag;
    }

    public String parseFileName(String filePath) {
        int idx = filePath.lastIndexOf("/");
        String fileName = filePath.substring(idx+1);
        return fileName;
    }

    public String makeFileName(MultipartFile file) {
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = FilenameUtils.getBaseName(file.getOriginalFilename()) + "-" + date.format(new Date()) + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        return fileName;
    }

    public String makeFileUrl(Integer directory, String filename) {
        String strDirectory;

        if(directory == 0) strDirectory = "/profile/";
        else if (directory == 1) strDirectory = "/diary/";
        else strDirectory = "/review/";

        return "http://" + CLOUD_FRONT_DOMAIN_NAME + strDirectory + filename;
    }
}
