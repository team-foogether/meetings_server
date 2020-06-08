package foogether.meetings.utils;

import java.io.File;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import foogether.meetings.domain.Entity.Meeting;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Component
public class FileUtils {

//	// 요청에 포함된 파일을 추출해서 저장하고 해당 파일의 정보를 반환
//	public List<MeetingImgsDto> parseFileInfo(int meetingIdx, MultipartHttpServletRequest request) throws Exception {
//
//		if(ObjectUtils.isEmpty(request)) {
//			return null;
//		}
//
//		// 파일 정보
//		List<MeetingImgsDto> fileInfoList = new ArrayList();
//
//		// 파일 저장 경로 생성
//		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd"); // 20200421\
//		ZonedDateTime now = ZonedDateTime.now();
//		String storedDir = "C://foogether_images/" + now.format(dtf);		// 오늘 날짜의 디렉토리 // images/20200421 이렇게 만들어진다.
//
//		File fileDir = new File(storedDir); // java.io.File
//
//		if(!fileDir.exists()) {	// 이 디렉토리가 없으면 만들어라
//			fileDir.mkdir();
//
//		}
//
////		// 요청에 포함된 파일을 추출해서 저장경로에 저장하고  파일 정보를 fileInfoList에 추가
//		Iterator<String> fileTagNames = request.getFileNames(); // <input type="file" name="???" .../>
//		while (fileTagNames.hasNext()) {
//			String originalFileExtension;
//			// 같은 <input type="file" name="???"> 태그 이름의 파일을 추출
//			List<MultipartFile> files = request.getFiles(fileTagNames.next());
//			for(MultipartFile file : files) {
//				if(!file.isEmpty()) {
//					String contentType = file.getContentType();
//					if (ObjectUtils.isEmpty(contentType)) {	// Content Type이 가장 먼저 있어서 이게 없으면 잘못된 데이터
//						break;
//					} else {
//						// 파일이 실행되지 않도록 이미지로만 그리고 저장할때도 이름을 바꿔서 저장
//						if (contentType.contains("image/jpeg")) {
//							originalFileExtension = ".jpg";
//						} else if (contentType.contains("image/png")) {
//							originalFileExtension = ".png";
//						} else if (contentType.contains("image/gif")) {
//							originalFileExtension = "gif";
//						} else {
//							break;
//						}
//					}
//
//					// 저장 파일명 생성
//					// 저장 파일명 : 나노시간 + 파일 확장자
//					String storedFileName = Long.toString(System.nanoTime()) + originalFileExtension;
//
//					// 파일 정보를 fileInfoList에 추가
//					MeetingImgsDto boardFile = new MeetingImgsDto(
//					        meetingIdx,
//					        storedDir + "/" + storedFileName
//					);
////					boardFile.setBoardIdx(meetingIdx);
////					boardFile.setFileSize(file.getSize());
////					boardFile.setOriginalFileName(file.getOriginalFilename());
////					boardFile.setStoredFilePath(storedDir + "/" + storedFileName);
//					fileInfoList.add(boardFile);
//
//					// 파일을 저장
//					fileDir = new File(storedDir + "/" + storedFileName);
//					file.transferTo(fileDir);
//
//				}
//			}
//
//		}
//
//
//		return fileInfoList;
//	}

//	public List<Meeting> parseFileInfo2(int meetingIdx, MultipartHttpServletRequest request) throws Exception {
//		if(ObjectUtils.isEmpty(request)) {
//			return null;
//		}
//
//		// 파일 정보
//		List<MeetingImgs> fileInfoList = new ArrayList();
//
//		// 파일 저장 경로 생성
//		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd"); // 20200421\
//		ZonedDateTime now = ZonedDateTime.now();
//		String storedDir = "images/" + now.format(dtf);		// 오늘 날짜의 디렉토리 // images/20200421 이렇게 만들어진다.
//
//		File fileDir = new File(storedDir); // java.io.File
//
//		if(!fileDir.exists()) {	// 이 디렉토리가 없으면 만들어라
//			fileDir.mkdir();
//
//		}
//
//		// 요청에 포함된 파일을 추출해서 저장경로에 저장하고  파일 정보를 fileInfoList에 추가
//		Iterator<String> fileTagNames = request.getFileNames(); // <input type="file" name="???" .../>
//		while (fileTagNames.hasNext()) {
//			String originalFileExtension;
//			// 같은 <input type="file" name="???"> 태그 이름의 파일을 추출
//			List<MultipartFile> files = request.getFiles(fileTagNames.next());
//			for(MultipartFile file : files) {
//				if(!file.isEmpty()) {
//					String contentType = file.getContentType();
//					if (ObjectUtils.isEmpty(contentType)) {	// Content Type이 가장 먼저 있어서 이게 없으면 잘못된 데이터
//						break;
//					} else {
//						// 파일이 실행되지 않도록 이미지로만 그리고 저장할때도 이름을 바꿔서 저장
//						if (contentType.contains("image/jpeg")) {
//							originalFileExtension = ".jpg";
//						} else if (contentType.contains("image/png")) {
//							originalFileExtension = ".png";
//						} else if (contentType.contains("image/gif")) {
//							originalFileExtension = "gif";
//						} else {
//							break;
//						}
//					}
//
//					// 저장 파일명 생성
//					// 저장 파일명 : 나노시간 + 파일 확장자
//					String storedFileName = Long.toString(System.nanoTime()) + originalFileExtension;
//
//					// 파일 정보를 fileInfoList에 추가
//					BoardFileEntity boardFile = new BoardFileEntity();
//					// boardFile.setBoardIdx(boardIdx);
//					boardFile.setFileSize(file.getSize());
//					boardFile.setCreatorId("admin");
//					boardFile.setOriginalFileName(file.getOriginalFilename());
//					boardFile.setStoredFilePath(storedDir + "/" + storedFileName);
//					fileInfoList.add(boardFile);
//
//					// 파일을 저장
//					fileDir = new File(storedDir + "/" + storedFileName);
//					file.transferTo(fileDir);
//
//				}
//			}
//
//		}
//
//
//		return fileInfoList;
//	}
}
