package ewha.efub.zeje.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
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

    //유저 프로필 사진 업로드용 함수
    public String uploadImage(String currentFile, MultipartFile file) throws IOException {
        boolean fileTypeFlag = true;

        //png, jpeg가 맞는지 확인
        if(file!=null){
            fileTypeFlag = checkContentType(file);

            //맞으면, file 올리기 -
            if(fileTypeFlag == true) {
                deleteS3(0, currentFile);
                String fileName = uploadS3(0, file);
                return fileName;
            }
            else {
                return "type error";
            }
        }
        return "null error";
    }

    /*프로필 사진 이외 업로드용 함수 - 프로필은 업데이트를 해서 추가코드가 더 있어서 서로 분리해서 오버로딩함!
    directory - s3내에 폴더 3개 만들어서 관리함 0.프로필 1.다이어리 2.리뷰*/
    public String uploadImage(int directory, MultipartFile file) throws IOException {
        boolean fileTypeFlag = true;

        //png, jpeg가 맞는지 확인
        if(file!=null){
            fileTypeFlag = checkContentType(file);

            if(fileTypeFlag == true) {
                String fileName = uploadS3(directory, file);
                return fileName;
            }
            else {
                return "type error";
            }
        }
        return "null error";
    }

    public String uploadS3 (int directory, MultipartFile file) throws IOException {
        String fileName = makeFileName(file);
        String strDirectory;

        if(directory == 0) strDirectory = "/profile";
        else if (directory == 1) strDirectory = "/diary";
        else strDirectory = "/review";

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
}
