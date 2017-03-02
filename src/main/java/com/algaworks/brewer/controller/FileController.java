package com.algaworks.brewer.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.algaworks.brewer.model.FileMeta;

public class FileController {
	
	private static final String UPLOAD_DIR = "D:/var/arquivos/";
	
	private List<FileMeta> files = new ArrayList<>();
	
	@RequestMapping(value = "/files/upload", method = RequestMethod.POST)
	public @ResponseBody List<FileMeta> upload(MultipartHttpServletRequest request) {
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;
		FileMeta fileMeta = null;
		while (itr.hasNext()) {
			mpf = request.getFile(itr.next());
			fileMeta = new FileMeta();
			fileMeta.setFileName(mpf.getOriginalFilename());
			fileMeta.setFileSize(mpf.getSize() / 1024 + " kb");
			fileMeta.setFileType(mpf.getContentType());
			try {
				fileMeta.setBytes(mpf.getBytes());
				FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(UPLOAD_DIR + mpf.getOriginalFilename()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			files.add(fileMeta);
		}
		return files;
	}
	
	@RequestMapping(value = "/files/download/{value}", method = RequestMethod.GET)
    public void get(HttpServletResponse response,@PathVariable String value){
        FileMeta getFile = files.get(Integer.parseInt(value));
        try {      
               response.setContentType(getFile.getFileType());
               response.setHeader("Content-Type", getFile.getFileType());
               FileCopyUtils.copy(getFile.getBytes(), response.getOutputStream());
        }catch (IOException e) {
               e.printStackTrace();
        }
    }

	public List<FileMeta> getFiles() {
		return files;
	}
}
