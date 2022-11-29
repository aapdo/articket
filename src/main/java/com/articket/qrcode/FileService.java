package com.articket.qrcode;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

@Slf4j
@Service("fileService")
public class FileService {


    /**
     * 파일 업로드
     *
     * @param EmfMap emfMap
     * @return EmfMap 조회조건에 검색된 데이터
     * @throws 비지니스 로직이나 DAO 처리 중 에러가 발생할 경우 Exception을 Throw 한다.
     *
     */
    public void uploadFile(MultipartHttpServletRequest multiRequest) throws Exception {

        // 파라미터 이름을 키로 파라미터에 해당하는 파일 정보를 값으로 하는 Map을 가져온다.
        Map<String, MultipartFile> files = multiRequest.getFileMap();

        // files.entrySet()의 요소를 읽어온다.
        Iterator<Map.Entry<String, MultipartFile>> itr = files.entrySet().iterator();

        MultipartFile mFile;

        // 파일이 업로드 될 경로를 지정한다.
        String filePath = "C:\\Users\\conf3\\Downloads\\";

        // 파일명이 중복되었을 경우, 사용할 스트링 객체
        String saveFileName = "", savaFilePath = "";

        // 읽어 올 요소가 있으면 true, 없으면 false를 반환한다.
        while (itr.hasNext()) {

            Map.Entry<String, MultipartFile> entry = itr.next();

            // entry에 값을 가져온다.
            mFile = entry.getValue();

            // 파일명
            String fileName = mFile.getOriginalFilename();

            // 확장자를 제외한 파일명
            String fileCutName = fileName.substring(0, fileName.lastIndexOf("."));

            // 확장자
            String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);

            // 저장될 경로와 파일명
            String saveFilePath = filePath + File.separator + fileName;

            // filePath에 해당되는 파일의 File 객체를 생성한다.
            File fileFolder = new File(filePath);

            if (!fileFolder.exists()) {
                // 부모 폴더까지 포함하여 경로에 폴더를 만든다.
                if (fileFolder.mkdirs()) {
                    log.info("[file.mkdirs] : Success");
                } else {
                    log.error("[file.mkdirs] : Fail");
                }
            }

            File saveFile = new File(saveFilePath);

            // saveFile이 File이면 true, 아니면 false
            // 파일명이 중복일 경우 덮어씌우지 않고, 파일명(1).확장자, 파일명(2).확장자 와 같은 형태로 생성한다.
            if (saveFile.isFile()) {
                boolean _exist = true;

                int index = 0;

                // 동일한 파일명이 존재하지 않을때까지 반복한다.
                while (_exist) {
                    index++;

                    saveFileName = fileCutName + "(" + index + ")." + fileExt;

                    String dictFile = filePath + File.separator + saveFileName;

                    _exist = new File(dictFile).isFile();

                    if (!_exist) {
                        savaFilePath = dictFile;
                    }
                }

                //생성한 파일 객체를 업로드 처리하지 않으면 임시파일에 저장된 파일이 자동적으로 삭제되기 때문에 transferTo(File f) 메서드를 이용해서 업로드처리한다.
                mFile.transferTo(new File(savaFilePath));
            } else {
                //생성한 파일 객체를 업로드 처리하지 않으면 임시파일에 저장된 파일이 자동적으로 삭제되기 때문에 transferTo(File f) 메서드를 이용해서 업로드처리한다.
                mFile.transferTo(saveFile);
            }
        }
    }

    public void save(byte[] qrByte, String fileName) throws IOException {
        String extension = ".png";
        String path = "./qr/";

        //ImageIO qr = new ImageIO();

        ByteArrayInputStream input_stream= new ByteArrayInputStream(qrByte);

        BufferedImage final_buffered_image = ImageIO.read(input_stream);

        ImageIO.write(final_buffered_image , "png", new File(path+fileName+extension) );

        /*
        System.out.println("폴더/파일 생성, 이름변경, 삭제\n");

        String path = System.getProperty("user.dir")
                + File.separator	// Windows('\'), Linux, MAC('/')
                + TEST_DIRECTORY;
        System.out.println("절대 경로 : " + path);
        File f = new File(path);
        System.out.println();

        // 폴더 생성: mkdir()
        if (!f.exists()) {	// 폴더가 존재하는지 체크, 없다면 생성
            if (f.mkdir())
                System.out.println("폴더 생성 성공");
            else
                System.out.println("폴더 생성 실패");
        } else {	// 폴더가 존재한다면
            System.out.println("폴더가 이미 존재합니다.");
        }
        System.out.println();

        // 파일 생성 : createNewFile()
        File f2 = new File(f, TEST_FILE);	// File(디렉터리 객체, 파일명)
        System.out.println(f2.getAbsolutePath());

        if (!f2.exists()) {	// 파일이 존재하지 않으면 생성
            try {
                if (f2.createNewFile())
                    System.out.println("파일 생성 성공");
                else
                    System.out.println("파일 생성 실패");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {	// 파일이 존재한다면
            System.out.println("파일이 이미 존재합니다.");
        }
        System.out.println();

        // 파일 이름 변경: renameTo()
        File f3 = new File(f, TEST_RENAME);	// 변경할 이름
        System.out.println(f3.getAbsolutePath());

        if (f2.exists()) {	// 파일이 존재할 때만 이름 변경
            if(f2.renameTo(f3))
                System.out.println("파일 이름 변경 성공");
            else
                System.out.println("파일 이름 변경 실패");
        } else {
            System.out.println("변경할 파일이 없습니다.");
        }
        System.out.println();


        // 파일 삭제: delete()
        File f4 = new File(f, TEST_RENAME);
        if (f4.exists()) {
            if (f4.delete())
                System.out.println("파일 삭제 성공");
            else
                System.out.println("파일 삭제 실패");
        } else {
            System.out.println("삭제할 파일이 없습니다.");
        }

         */
    }
}